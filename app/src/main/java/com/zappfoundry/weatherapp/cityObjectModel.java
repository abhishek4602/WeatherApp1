package com.zappfoundry.weatherapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.Timestamp;
@Entity(tableName = "cityObjectModel")
public class cityObjectModel {

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    @ColumnInfo(name = "cityName")
    String cityName;


    public int getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(int temperature1) {
        this.temperature1 = temperature1;
    }

    @ColumnInfo(name = "temperature1")
    int temperature1 ;

    public int getTemperatureFeelsLike() {
        return temperatureFeelsLike;
    }

    public void setTemperatureFeelsLike(int temperatureFeelsLike) {
        this.temperatureFeelsLike = temperatureFeelsLike;
    }
    @ColumnInfo(name = "temperatureFeelsLike")
    int temperatureFeelsLike;

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }
    @ColumnInfo(name = "windSpeed")
    Float windSpeed;

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    @ColumnInfo(name = "humidity")
    int humidity;

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }
@ColumnInfo(name = "weatherType")
    String weatherType;

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
    @ColumnInfo(name = "weatherDescription")
    String weatherDescription;
    @PrimaryKey(autoGenerate = true)
int cityID;

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getPressure() {
        return Pressure;
    }

    public void setPressure(int pressure) {
        Pressure = pressure;
    }

    /*
            public java.sql.Timestamp getTimeStamp() {
                return timeStamp;
            }

        *//*
    public void setTimeStamp(java.sql.Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
    @ColumnInfo(name = "timeStamp")
    java.sql.Timestamp timeStamp;
*/
int Pressure;

    public int getTimeFactor() {
        return timeFactor;
    }

    public void setTimeFactor(int timeFactor) {
        this.timeFactor = timeFactor;
    }

    int timeFactor;
}
