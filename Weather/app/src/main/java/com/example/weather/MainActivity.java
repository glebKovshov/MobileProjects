package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private ImageView imageViewIcon;
    private TextView textViewTemp, textViewDesc;
    private final String API_KEY = "31828415e794eedcac36058a814eb63e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewDesc = findViewById(R.id.textViewDesc);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString().trim();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите город", Toast.LENGTH_SHORT).show();
                } else {
                    getWeather(city);
                }
            }
        });
    }

    private void getWeather(String city) {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }
                JSONObject jsonObject = new JSONObject(json.toString());

                double temp = jsonObject.getJSONObject("main").getDouble("temp");
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                String description = weatherArray.getJSONObject(0).getString("description");
                String icon = weatherArray.getJSONObject(0).getString("icon");

                runOnUiThread(() -> {
                    textViewTemp.setText("Температура: " + temp + "°C");
                    textViewDesc.setText(capitalize(description));
                    new Thread(() -> {
                        try {
                            URL iconUrl = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
                            InputStream iconStream = iconUrl.openStream();
                            final android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(iconStream);
                            runOnUiThread(() -> imageViewIcon.setImageBitmap(bitmap));
                        } catch (Exception e) {
                            runOnUiThread(() -> imageViewIcon.setImageResource(0));
                        }
                    }).start();
                });
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            runOnUiThread(() -> {
                textViewTemp.setText("Ошибка");
                textViewDesc.setText("Не удалось получить погоду");
                imageViewIcon.setImageResource(0);
            });
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
