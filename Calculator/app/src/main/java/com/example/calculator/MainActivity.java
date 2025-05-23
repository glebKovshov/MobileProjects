package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String currentNumber = "";
    private String firstNumber = "";
    private String operation = "";
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        // Назначаем обработчики для всех кнопок
        setNumberButtonClickListeners();
        setOperationButtonClickListeners();
    }

    private void setNumberButtonClickListeners() {
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_dot
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                Button button = (Button) v;
                String buttonText = button.getText().toString();

                if (isNewOperation) {
                    currentNumber = "";
                    isNewOperation = false;
                }

                if (buttonText.equals(".") && currentNumber.contains(".")) {
                    return;
                }

                currentNumber += buttonText;
                updateDisplay();
            });
        }
    }

    private void setOperationButtonClickListeners() {
        int[] operationButtonIds = {
                R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide
        };

        for (int id : operationButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                if (currentNumber.isEmpty()) return;

                if (!firstNumber.isEmpty()) {
                    calculateResult();
                }

                Button button = (Button) v;
                operation = button.getText().toString();
                firstNumber = currentNumber;
                currentNumber = "";
                updateDisplay();
            });
        }

        findViewById(R.id.btn_equals).setOnClickListener(v -> {
            if (firstNumber.isEmpty() || operation.isEmpty()) return;
            calculateResult();
            operation = "";
            isNewOperation = true;
        });

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            currentNumber = "";
            firstNumber = "";
            operation = "";
            isNewOperation = true;
            updateDisplay();
        });
    }

    private void calculateResult() {
        if (firstNumber.isEmpty() || currentNumber.isEmpty()) return;

        double num1 = Double.parseDouble(firstNumber);
        double num2 = Double.parseDouble(currentNumber);
        double result = 0;

        switch (operation) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "×":
                result = num1 * num2;
                break;
            case "÷":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    display.setText("Error");
                    return;
                }
                break;
        }

        if (result == (int) result) {
            currentNumber = String.valueOf((int) result);
        } else {
            currentNumber = String.valueOf(result);
        }

        firstNumber = "";
        updateDisplay();
    }

    private void updateDisplay() {
        if (!currentNumber.isEmpty()) {
            display.setText(currentNumber);
        } else {
            display.setText("0");
        }
    }
}