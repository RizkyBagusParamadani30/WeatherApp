package com.example.wheateapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecycleDaily extends RecyclerView.Adapter<AdapterRecycleDaily.ViewHolder> {
    private List<DailyWeather> dailyWeatherList;

    public AdapterRecycleDaily(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyWeather dailyWeather = dailyWeatherList.get(position);
        holder.dateTextView.setText(dailyWeather.getDate());
        holder.weatherTextView.setText(dailyWeather.getWeatherText());
        holder.weatherIconImageView.setImageResource(HelperWeather.getWeatherIcon(dailyWeather.getWeatherCode()));
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView weatherTextView;
        ImageView weatherIconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_rv);
            weatherTextView = itemView.findViewById(R.id.weather_rv);
            weatherIconImageView = itemView.findViewById(R.id.weather_icon_rv);
        }
    }
}
