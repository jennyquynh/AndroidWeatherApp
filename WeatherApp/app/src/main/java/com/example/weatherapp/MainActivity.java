package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    //put your own API key here!
    private static final String YourAPIKey = "";

    TextView temp;
    TextView weather;
    TextView humidity;
    TextView wind;
    TextView pressure;
    EditText city;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.temp);
        weather = findViewById(R.id.weather);
        humidity = findViewById(R.id.humidity);
        city = findViewById(R.id.city);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        mQueue = Volley.newRequestQueue(this);


        city.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String cityName = city.getText().toString();
                    jsonParse(cityName);
                    return true;
                }
                return false;
            }
        });
    }

    private void jsonParse(String city)
    {
        //https://api.openweathermap.org/data/2.5/weather?q=New Orleans&APPID=d8c432b083acdf47fe0f47f0c0c3b2aa&units=imperial
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&"+ YourAPIKey +"&units=imperial";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONObject main = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherInfo = weatherArray.getJSONObject(0);
                            JSONObject windObj = response.getJSONObject("wind");

                            String tempVal = String.valueOf((int) main.getDouble("temp"));
                            String humidityVal = String.valueOf(main.getInt("humidity"));
                            String description =  String.valueOf(weatherInfo.getString("description"));
                            String windVal = String.valueOf(windObj.getDouble("speed"));
                            String pressureVal = String.valueOf(main.getInt("pressure"));

                            temp.setText(tempVal + (char) 0x00B0);
                            weather.setText(description);
                            humidity.setText(humidityVal + "%");
                            wind.setText(windVal + " mph");
                            pressure.setText(pressureVal + " hpa");

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        mQueue.add(request);
    }
}
