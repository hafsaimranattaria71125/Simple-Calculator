/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author DELL
 */
public class CalcValid implements ActionListener {
    CalcGUI ref;
    CaclLogic logic; // Kept your original class name

    public CalcValid(CalcGUI g) {
        ref = g;
    }

    // Helper: check if char is an operator
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Helper: get the current number segment after the last operator
    private String getCurrentNumberSegment(String expr) {
        int lastOp = -1;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                lastOp = i;
            }
        }
        return expr.substring(lastOp + 1);
    }

    // Helper: find index of last operator in expression
    private int getLastOperatorIndex(String expr) {
        int lastOp = -1;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                lastOp = i;
            }
        }
        return lastOp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String current = ref.tf1.getText();
        String prevRes = ref.tf2.getText();

        // FIX: Safe lastChar — avoids index error on empty string
        char lastChar = current.isEmpty() ? '\0' : current.charAt(current.length() - 1);

        switch (command) {

            // ─── CLEAR ────────────────────────────────────────────────────────
            case "C":
                ref.tf1.setText("");
                ref.tf2.setText("");
                break;

            // ─── DELETE ───────────────────────────────────────────────────────
            case "DEL":
                if (current.isEmpty()) return;

                // FIX: After '=', DEL clears everything cleanly
                if (lastChar == '=') {
                    ref.tf1.setText(prevRes);
                    ref.tf2.setText("");
                    return;
                }
                ref.tf1.setText(current.substring(0, current.length() - 1));
                break;

            // ─── EQUALS ───────────────────────────────────────────────────────
            case "=":
                // FIX: Prevent duplicate '=' press
                if (lastChar == '=') return;
                if (current.isEmpty()) return;

                // FIX: Strip trailing operator before evaluating (e.g. "5+" → "5")
                if (isOperator(lastChar)) {
                    current = current.substring(0, current.length() - 1);
                }
                if (current.isEmpty()) return;

                if (current.matches(".*[+\\-*/].*")) {
                    logic = new CaclLogic(current);
                    String ans = logic.solve();
                    ref.tf1.setText(current + "=");
                    ref.tf2.setText(ans);
                } else {
                    // Single number: "6=" → result is itself
                    ref.tf1.setText(current + "=");
                    ref.tf2.setText(current);
                }
                break;

            // ─── DECIMAL POINT ────────────────────────────────────────────────
            case ".":
                // FIX: Only check current number segment for existing dot
                String currentSeg = getCurrentNumberSegment(current);
                if (currentSeg.contains(".")) return; // Already has dot in this number

                if (current.isEmpty() || isOperator(lastChar)) {
                    // FIX: Auto-prefix "0." when dot pressed after operator or on empty
                    ref.tf1.setText(current + "0.");
                } else if (lastChar == '=') {
                    // FIX: After result, start fresh decimal number
                    ref.tf1.setText("0.");
                } else {
                    ref.tf1.setText(current + ".");
                }
                break;

            // ─── CONSTANTS: π and e ───────────────────────────────────────────
            case "π":
            case "e":
                // FIX: Use .equals() instead of == for string comparison (was a bug)
                String constVal = command.equals("e")
                        ? String.valueOf(Math.E)
                        : String.valueOf(Math.PI);

                if (lastChar == '=') {
                    // After result, start fresh with constant
                    ref.tf1.setText(constVal);
                    break;
                }

                if (current.isEmpty() || isOperator(lastChar)) {
                    // Safe to just append constant
                    ref.tf1.setText(current + constVal);
                } else if (Character.isDigit(lastChar) || lastChar == '.') {
                    // FIX: If a number was being typed, replace it with the constant
                    // e.g. user typed "3" then pressed π → strip "3", insert π value
                    int i = current.length() - 1;
                    while (i >= 0 && (Character.isDigit(current.charAt(i)) || current.charAt(i) == '.')) {
                        i--;
                    }
                    current = current.substring(0, i + 1);
                    ref.tf1.setText(current + constVal);
                } else {
                    ref.tf1.setText(current + constVal);
                }
                break;

            // ─── OPERATORS: + - * / ───────────────────────────────────────────
            case "+":
            case "-":
            case "*":
            case "/":
                // FIX: If previous result was an error, reset everything first
                if (prevRes.contains("Can't") || prevRes.contains("Error") || prevRes.contains("Infinity") || prevRes.contains("NaN")) {
                    ref.tf1.setText("0" + command);
                    ref.tf2.setText("");
                    break;
                }

                if (current.isEmpty()) {
                    if (command.equals("-")) {
                        ref.tf1.setText("-");
                    } else if (!prevRes.isEmpty()) {
                        ref.tf1.setText(prevRes + command);
                    } else {
                        ref.tf1.setText("0" + command);
                    }
                    break;
                }

                if (lastChar == '=') {
                    // FIX: Also guard here — don't continue from an error result
                    if (prevRes.contains("Can't") || prevRes.contains("Error") || prevRes.contains("Infinity") || prevRes.contains("NaN")) {
                        ref.tf1.setText("0" + command);
                        ref.tf2.setText("");
                    } else {
                        ref.tf1.setText(prevRes + command);
                    }
                    break;
                }
                // ... rest of operator case unchanged

                if (isOperator(lastChar)) {
                    // FIX: Replace last operator instead of stacking (e.g. "5+*" → "5*")
                    current = current.substring(0, current.length() - 1);
                    ref.tf1.setText(current + command);
                    break;
                }

                // FIX: Chain evaluation — e.g. 5+2* evaluates 5+2=7, then shows 7*
                if (current.matches(".*[+\\-*/].*")) {
                    logic = new CaclLogic(current);
                    String ans = logic.solve();
                    ref.tf2.setText(ans);
                    ref.tf1.setText(ans + command);
                } else {
                    ref.tf1.setText(current + command);
                }
                break;

            // ─── DIGIT INPUT (0-9) ────────────────────────────────────────────
            default:
                // After '=', pressing a digit starts fresh
                if (lastChar == '=') {
                    ref.tf1.setText(command);
                    ref.tf2.setText("");
                    return;
                }

                int lastOpIndex = getLastOperatorIndex(current);
                String num1 = (lastOpIndex == -1) ? current : current.substring(0, lastOpIndex);
                String num2 = (lastOpIndex == -1) ? current : current.substring(lastOpIndex + 1);
                // Determine which segment we are currently editing
                String activeSegment = (lastOpIndex == -1) ? num1 : num2;

                // FIX: Enforce 15-digit limit — count only digits, ignore '.' and '-'
                String digitsOnly = activeSegment.replaceAll("[^0-9]", "");
                if (digitsOnly.length() >= 15) return;

                // FIX: Prevent leading zeros (e.g. "00", "007") per segment
                if ("0".equals(activeSegment) && "0".equals(command)) return;
                if ("0".equals(activeSegment) && !command.equals(".")) {
                    // Replace leading zero with new digit (e.g. "0" + "5" → "5")
                    if (lastOpIndex == -1) {
                        ref.tf1.setText(command);
                    } else {
                        ref.tf1.setText(current.substring(0, lastOpIndex + 1) + command);
                    }
                    return;
                }

                // FIX: Empty field — just set the digit
                if (current.isEmpty()) {
                    ref.tf1.setText(command);
                    return;
                }

                // Default: append digit
                ref.tf1.setText(current + command);
                break;
        }
    }
}