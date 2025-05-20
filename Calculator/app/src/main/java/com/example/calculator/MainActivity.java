package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etNumber1, etNumber2;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber1 = findViewById(R.id.etNumber1);
        etNumber2 = findViewById(R.id.etNumber2);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnAdd).setOnClickListener(operatorClick('+'));
        findViewById(R.id.btnSubtract).setOnClickListener(operatorClick('-'));
        findViewById(R.id.btnMultiply).setOnClickListener(operatorClick('*'));
        findViewById(R.id.btnDivide).setOnClickListener(operatorClick('/'));
    }

    private View.OnClickListener operatorClick(char operator) {
        return v -> {
            String num1Str = etNumber1.getText().toString();
            String num2Str = etNumber2.getText().toString();

            if (num1Str.isEmpty() || num2Str.isEmpty()) {
                tvResult.setText("Введите оба числа!");
                return;
            }

            try {
                float num1 = Float.parseFloat(num1Str);
                float num2 = Float.parseFloat(num2Str);
                float result;

                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            tvResult.setText("Деление на ноль!");
                            return;
                        }
                        result = num1 / num2;
                        break;
                    default:
                        result = 0;
                }

                tvResult.setText("Результат: " + result);
            } catch (NumberFormatException e) {
                tvResult.setText("Ошибка: введите корректные числа");
            }
        };
    }
}
