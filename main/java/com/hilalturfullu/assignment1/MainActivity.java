package com.hilalturfullu.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView inputText, resultText;
    private String input = "";
    private String currentOperator = "";
    private double result = 0;
    private boolean isNewInput = true;
    private boolean isDegreeMode = true;
    private boolean isSecondaryFunctions = false;
    private double memoryValue = 0;


    // Only initialize buttons that exist in current layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize core components
        inputText = findViewById(R.id.inputText);
        resultText = findViewById(R.id.resultText);

        // Safe initialization for landscape buttons
        initializeIfExists(R.id.secondaryFuncToggle);
        initializeIfExists(R.id.sqrt);
        initializeIfExists(R.id.log);
        initializeIfExists(R.id.ln);
        initializeIfExists(R.id.factorial);
        initializeIfExists(R.id.e);
        initializeIfExists(R.id.pi);
        initializeIfExists(R.id.openParen);
        initializeIfExists(R.id.closeParen);
        initializeIfExists(R.id.sec);
        initializeIfExists(R.id.csc);
        initializeIfExists(R.id.cot);
        initializeIfExists(R.id.sinh);
        initializeIfExists(R.id.cosh);
        initializeIfExists(R.id.tanh);
        initializeIfExists(R.id.degRadToggle);
        initializeIfExists(R.id.memoryClear);
        initializeIfExists(R.id.memoryRecall);
        initializeIfExists(R.id.memoryAdd);
        initializeIfExists(R.id.memorySubtract);
        initializeIfExists(R.id.memoryStore);
    }

    private void initializeIfExists(int buttonId) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(this::ButtonClick);
        }
    }

    public void ButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        // Handle mode toggles first
        if (buttonText.equals("2nd")) {
            toggleSecondaryFunctions();
            return;
        }
        if (buttonText.equals("DEG") || buttonText.equals("RAD")) {
            toggleAngleMode(button);
            return;
        }

        // Handle other buttons based on current mode
        if (isSecondaryFunctions) {
            handleSecondaryFunction(buttonText);
        } else {
            handlePrimaryFunction(buttonText);
        }
    }

    private void toggleSecondaryFunctions() {
        isSecondaryFunctions = !isSecondaryFunctions;
    }

    private void toggleAngleMode(Button button) {
        isDegreeMode = !isDegreeMode;
        button.setText(isDegreeMode ? "DEG" : "RAD");
    }

    private void handlePrimaryFunction(String buttonText) {
        switch (buttonText) {
            case "C":
                clearAll();
                break;
            case "⌫":
                backspace();
                break;
            case "+/-":
                toggleSign();
                break;
            case "%":
                calculatePercentage();
                break;
            case "÷":
            case "×":
            case "-":
            case "+":
                handleOperator(buttonText);
                break;
            case "=":
                calculateResult();
                break;
            case ".":
                handleDecimalPoint();
                break;
            case "√":
                calculateSquareRoot();
                break;
            case "log":
                calculateLog10();
                break;
            case "ln":
                calculateNaturalLog();
                break;
            case "n!":
                calculateFactorial();
                break;
            case "e":
                appendConstant("2.718281828459045");
                break;
            case "π":
                appendConstant("3.141592653589793");
                break;
            case "(":
            case ")":
                appendParenthesis(buttonText);
                break;
            case "sin":
            case "cos":
            case "tan":
                handleTrigFunction(buttonText);
                break;
            case "MC":
                memoryClear();
                break;
            case "MR":
                memoryRecall();
                break;
            case "M+":
                memoryAdd();
                break;
            case "M-":
                memorySubtract();
                break;
            case "MS":
                memoryStore();
                break;
            default:
                appendNumber(buttonText);
        }
    }

    private void handleSecondaryFunction(String buttonText) {
        switch (buttonText) {
            case "sin⁻¹":
                calculateArcSin();
                break;
            case "cos⁻¹":
                calculateArcCos();
                break;
            case "tan⁻¹":
                calculateArcTan();
                break;
            case "sinh":
                calculateSinh();
                break;
            case "cosh":
                calculateCosh();
                break;
            case "tanh":
                calculateTanh();
                break;
            case "sec":
                calculateSec();
                break;
            case "csc":
                calculateCsc();
                break;
            case "cot":
                calculateCot();
                break;
            default:
                handlePrimaryFunction(buttonText);
        }
    }

    private void clearAll() {
        input = "";
        currentOperator = "";
        result = 0;
        isNewInput = true;
        updateDisplay();
    }

    private void backspace() {
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
            if (input.isEmpty()) {
                input = "0";
                isNewInput = true;
            }
            updateDisplay();
        }
    }

    private void toggleSign() {
        if (input.isEmpty()) {
            input = "-0";
        } else {
            if (input.charAt(0) == '-') {
                input = input.substring(1);
                if (input.isEmpty() || input.equals("0")) input = "0";
            } else {
                if (input.equals("0") && input.length() == 1) {
                    input = "-0";
                } else if (!input.equals("0")) {
                    input = "-" + input;
                }
            }
        }
        updateDisplay();
    }

    private void calculatePercentage() {
        if (input.isEmpty()) return;
        try {
            double value = Double.parseDouble(input);
            value = value / 100;
            input = removeTrailingZeros(String.valueOf(value));
            updateDisplay();
        } catch (NumberFormatException e) {
            resultText.setText("Error");
        }
    }

    private void handleOperator(String operator) {
        if (!input.isEmpty()) {
            if (!currentOperator.isEmpty()) {
                calculateResult();
            }
            currentOperator = operator;
            result = Double.parseDouble(input);
            isNewInput = true;
            updateDisplay();
        } else if (!currentOperator.isEmpty()) {
            currentOperator = operator;
            updateDisplay();
        }
    }

    private void handleDecimalPoint() {
        if (isNewInput) {
            input = "0.";
            isNewInput = false;
        } else if (!input.contains(".")) {
            input += ".";
        }
        updateDisplay();
    }

    private void appendNumber(String number) {
        if (isNewInput || input.equals("0") || input.equals("-0")) {
            if (input.equals("-0")) {
                input = "-" + number;
            } else {
                input = number;
            }
            isNewInput = false;
        } else {
            input += number;
        }
        updateDisplay();
    }

    private void calculateResult() {
        if (input.isEmpty() || currentOperator.isEmpty()) return;

        try {
            double inputValue = Double.parseDouble(input);
            switch (currentOperator) {
                case "+":
                    result += inputValue;
                    break;
                case "-":
                    result -= inputValue;
                    break;
                case "×":
                    result *= inputValue;
                    break;
                case "÷":
                    if (inputValue != 0) result /= inputValue;
                    else {
                        resultText.setText("Error");
                        return;
                    }
                    break;
                case "^":
                    result = Math.pow(result, inputValue);
                    break;
            }

            input = removeTrailingZeros(String.valueOf(result));
            currentOperator = "";
            isNewInput = true;
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Scientific functions
    private void calculateSquareRoot() {
        try {
            double value = Double.parseDouble(input);
            if (value < 0) throw new Exception();
            input = removeTrailingZeros(String.valueOf(Math.sqrt(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateLog10() {
        try {
            double value = Double.parseDouble(input);
            if (value <= 0) throw new Exception();
            input = removeTrailingZeros(String.valueOf(Math.log10(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateNaturalLog() {
        try {
            double value = Double.parseDouble(input);
            if (value <= 0) throw new Exception();
            input = removeTrailingZeros(String.valueOf(Math.log(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateFactorial() {
        try {
            int value = Integer.parseInt(input);
            if (value < 0) throw new Exception();
            long factorial = 1;
            for (int i = 2; i <= value; i++) factorial *= i;
            input = String.valueOf(factorial);
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void handleTrigFunction(String function) {
        try {
            double value = Double.parseDouble(input);
            if (!isDegreeMode) value = Math.toDegrees(value);

            double result = 0;
            switch (function) {
                case "sin": result = Math.sin(Math.toRadians(value)); break;
                case "cos": result = Math.cos(Math.toRadians(value)); break;
                case "tan": result = Math.tan(Math.toRadians(value)); break;
            }

            input = removeTrailingZeros(String.valueOf(result));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Inverse trigonometric functions
    private void calculateArcSin() {
        try {
            double value = Double.parseDouble(input);
            double result = Math.asin(value);
            if (isDegreeMode) result = Math.toDegrees(result);
            input = removeTrailingZeros(String.valueOf(result));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateArcCos() {
        try {
            double value = Double.parseDouble(input);
            double result = Math.acos(value);
            if (isDegreeMode) result = Math.toDegrees(result);
            input = removeTrailingZeros(String.valueOf(result));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateArcTan() {
        try {
            double value = Double.parseDouble(input);
            double result = Math.atan(value);
            if (isDegreeMode) result = Math.toDegrees(result);
            input = removeTrailingZeros(String.valueOf(result));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Hyperbolic functions
    private void calculateSinh() {
        try {
            double value = Double.parseDouble(input);
            input = removeTrailingZeros(String.valueOf(Math.sinh(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateCosh() {
        try {
            double value = Double.parseDouble(input);
            input = removeTrailingZeros(String.valueOf(Math.cosh(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateTanh() {
        try {
            double value = Double.parseDouble(input);
            input = removeTrailingZeros(String.valueOf(Math.tanh(value)));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Reciprocal functions
    private void calculateSec() {
        try {
            double value = Math.cos(Math.toRadians(Double.parseDouble(input)));
            input = removeTrailingZeros(String.valueOf(1 / value));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateCsc() {
        try {
            double value = Math.sin(Math.toRadians(Double.parseDouble(input)));
            input = removeTrailingZeros(String.valueOf(1 / value));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void calculateCot() {
        try {
            double value = Math.tan(Math.toRadians(Double.parseDouble(input)));
            input = removeTrailingZeros(String.valueOf(1 / value));
            updateDisplay();
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Memory functions
    private void memoryClear() { memoryValue = 0; }

    private void memoryRecall() {
        input = removeTrailingZeros(String.valueOf(memoryValue));
        updateDisplay();
    }

    private void memoryAdd() {
        try {
            memoryValue += Double.parseDouble(input);
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void memorySubtract() {
        try {
            memoryValue -= Double.parseDouble(input);
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    private void memoryStore() {
        try {
            memoryValue = Double.parseDouble(input);
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Helper methods
    private void appendConstant(String constant) {
        input = isNewInput ? constant : input + constant;
        isNewInput = false;
        updateDisplay();
    }

    private void appendParenthesis(String paren) {
        input += paren;
        updateDisplay();
    }

    private String removeTrailingZeros(String number) {
        if (number.contains(".")) {
            number = number.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        return number;
    }


    private void updateDisplay() {
        String displayText = "";
        if (!currentOperator.isEmpty()) {
            displayText = removeTrailingZeros(String.valueOf(result)) + " " + currentOperator;
        }
        inputText.setText(displayText);

        // Show memory indicator if value exists
        if (memoryValue != 0) {
            resultText.setText("M: " + input);
        } else {
            resultText.setText(input.isEmpty() ? "0" : input);
        }
    }
}