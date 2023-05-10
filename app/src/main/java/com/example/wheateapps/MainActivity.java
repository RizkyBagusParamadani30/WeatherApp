package com.example.wheateapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView  Elevation,City, curent_weather, cur_temp;

    private ImageView weather_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Elevation = (TextView) findViewById(R.id.Elevation);
        City = (TextView) findViewById(R.id.City);
//        Daily = (TextView) findViewById(R.id.Daily);
        curent_weather = (TextView) findViewById(R.id.curent_weather);
        cur_temp = (TextView) findViewById(R.id.cur_temp);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

        getData();
    }

    private void getData() {
        String myUrl = "https://api.open-meteo.com/v1/forecast?latitude=-7.96071&longitude=112.6360&daily=weathercode&current_weather=true&timezone=auto";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //Create a JSON object containing information from the API.
                            JSONObject myJsonObject = new JSONObject(response.toString());
                            double latitude = myJsonObject.getDouble("latitude");
                            double longitude = myJsonObject.getDouble("longitude");
                            double elevation = myJsonObject.getDouble("elevation");

                            // Perform reverse geocoding to get the city name
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String city = addresses.get(0).getLocality();

                            // Display elevation, and city name
                            Elevation.setText("Elevation :" + " "+ String.valueOf(elevation));
                            City.setText(city);

                            //Display the current weather
                            JSONObject current = myJsonObject.getJSONObject("current_weather");
                            String temperature = current.getString("temperature");
                            int weathercode1 = current.getInt("weathercode");
                            String weatherString1 = HelperWeather.getWeatherText(weathercode1);
                            curent_weather.setText(weatherString1);
                            cur_temp.setText(temperature  + "°");

                            // Get the daily weather information
                            JSONObject daily = myJsonObject.getJSONObject("daily");
                            JSONArray time = daily.getJSONArray("time");
                            JSONArray weathercode = daily.getJSONArray("weathercode");
                            List<DailyWeather> dailyWeatherList = new ArrayList<>();
                            // Convert time string to date format
                            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            for (int i = 0; i < time.length(); i++) {
                                String timeString = time.getString(i);

                                // Parse time string to Date object
                                Date date = inputDateFormat.parse(timeString);

                                // Convert Date object to desired output format (e.g. "Mon, May 16")
                                SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH);
                                String dateFormatted = outputDateFormat.format(date);

                                int code = weathercode.getInt(i);
                                String weatherText = HelperWeather.getWeatherText(code);
                                dailyWeatherList.add(new DailyWeather(dateFormatted, code, weatherText));
                            }
                            RecyclerView recyclerView = findViewById(R.id.Daily);
                            AdapterRecycleDaily dailyWeatherAdapter = new AdapterRecycleDaily(dailyWeatherList);
                            recyclerView.setAdapter(dailyWeatherAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(dailyWeatherAdapter);

                            // Display Weather Icon in Header
                            weather_icon.setImageResource(HelperWeather.getWeatherIcon(weathercode1));


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                volleyError -> Toast.makeText(
                                MainActivity.this,
                                volleyError.getMessage(),
                                Toast.LENGTH_SHORT)
                        .show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }
}