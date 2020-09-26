package com.zappfoundry.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class cityRecyclerViewHandler extends RecyclerView.Adapter<cityRecyclerViewHandler.RecyclerViewHolder> {
    private List<cityObjectModel> cityList;
    IToggleCity iToggleCity;
    Context context;
    public cityRecyclerViewHandler(List<cityObjectModel> cityListReceived, Context receivedContext,IToggleCity iToggleCityreceived)
    {
        this.cityList=cityListReceived;
        this.context=receivedContext;
        this.iToggleCity=iToggleCityreceived;
    }
    @NonNull
    @Override
    public cityRecyclerViewHandler.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.saved_item,null);
        final RecyclerViewHolder rvHolder=new RecyclerViewHolder(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cityRecyclerViewHandler.RecyclerViewHolder holder, int position)
    {
        cityObjectModel city =cityList.get(position);
            holder.cityName.setText(city.getCityName());
            holder.cityWeatherType.setText(String.valueOf( city.getTemperature1())+"°C");
            switch (city.getWeatherType()){
                case "Rain": holder.imageViewBackground.setImageResource(R.drawable.rain);
                    holder.cityName.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.cityWeatherType.setTextColor(Color.parseColor("#FFFFFF"));
                break;
                case "Clouds":
                    holder.imageViewBackground.setImageResource(R.drawable.cloudynight);
                    holder.cityName.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.cityWeatherType.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case "Haze":
                    holder.imageViewBackground.setImageResource(R.drawable.sunny);
                    holder.cityName.setTextColor(Color.parseColor("#000000"));
                    holder.cityWeatherType.setTextColor(Color.parseColor("#000000"));
                    break;
                case "Clear":
                    holder.imageViewBackground.setImageResource(R.drawable.cloudyday);
                    holder.cityName.setTextColor(Color.parseColor("#000000"));
                    holder.cityWeatherType.setTextColor(Color.parseColor("#000000"));
                    break;
                default:holder.imageViewBackground.setImageResource(R.drawable.cloudyday);
                    holder.cityName.setTextColor(Color.parseColor("#000000"));
                    holder.cityWeatherType.setTextColor(Color.parseColor("#000000"));
                    break;

            }
            holder.cardViewCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                iToggleCity.ToggleCityI(city);
                }
            });


    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
    class RecyclerViewHolder extends RecyclerView.ViewHolder   {

        TextView cityName,cityWeatherType;
        ImageView imageViewBackground;
        CardView cardViewCity;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName=itemView.findViewById(R.id.tvCityName);
            cityWeatherType=itemView.findViewById(R.id.tvCityWeatherType);
            imageViewBackground=itemView.findViewById(R.id.imageViewBackground);
            cardViewCity=itemView.findViewById(R.id.CardViewItem);

        }





    }
}
