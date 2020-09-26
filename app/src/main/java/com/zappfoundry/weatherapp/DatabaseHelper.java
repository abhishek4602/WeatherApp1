package com.zappfoundry.weatherapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="WeatherApp";

    private static final String TABLE_NAME="city_weather";
    public static final String  CITY_ID = "id";
    public static final String  CITY_NAME = "cityname";
    public static final String cityWeather = "cityWeather";
    public static final String cityWeatherDescription = "cityWeatherDescription";
    public static final String cityTemperature = "cityTemperature";
    public static final String cityTempFeelsLike = "cityTempFeelsLike";
    public static final String cityHumidity = "cityHumidity";
    public static final String cityPressure = "cityPressure";
    public static final String cityWind = "cityWind";


    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
String createTable="create table "+TABLE_NAME+"(id integer primary key, cityname text,cityWeather text,cityWeatherDescription text, cityTemperature number,cityTempFeelsLike number,cityHumidity number,cityPressure number,cityWind float)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean Addtext(String cityname  ,String cityWeather ,String cityWeatherDescription  ,int cityTemperature, int cityTempFeelsLike  ,int cityHumidity,int cityPressure ,float cityWind)

    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("cityname",cityname);
        contentValues.put("cityWeather",cityWeather);
        contentValues.put("cityWeatherDescription",cityWeatherDescription);
        contentValues.put("cityTemperature",cityTemperature);
        contentValues.put("cityTempFeelsLike",cityTempFeelsLike);
        contentValues.put("cityHumidity",cityHumidity);
        contentValues.put("cityPressure",cityPressure);
        contentValues.put("cityWind",cityWind);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return true;
    }
   /* public ArrayList getAllText()
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList=new ArrayList<>();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return  arrayList;
    }*/

    public Cursor getData(String cityName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from city_weather where cityname='"+cityName+"'", null );
        return res;
    }

    public ArrayList<String> getAllText() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from city_weather", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CITY_NAME)));
            array_list.add(res.getString(res.getColumnIndex(cityWeather)));
            array_list.add(res.getString(res.getColumnIndex(cityWeatherDescription)));
            array_list.add(res.getString(res.getColumnIndex(cityTemperature)));
            array_list.add(res.getString(res.getColumnIndex(cityTempFeelsLike)));
            array_list.add(res.getString(res.getColumnIndex(cityHumidity)));
            array_list.add(res.getString(res.getColumnIndex(cityPressure)));
            array_list.add(res.getString(res.getColumnIndex(cityWind)));
            res.moveToNext();
        }
        return array_list;
    }

    void deleteAllData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
