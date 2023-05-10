package com.example.wheateapps;

public class DailyWeather {
    private String date;
    private int weatherCode;
    private String weatherText;

    public DailyWeather(String date, int weatherCode, String weatherText) {
        this.date = date;
        this.weatherCode = weatherCode;
        this.weatherText = weatherText;
    }

    public String getDate() {
        return date;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public String getWeatherText() {
        return weatherText;
    }
}
