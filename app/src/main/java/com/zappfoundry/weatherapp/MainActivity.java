package com.zappfoundry.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IToggleCity {
    Boolean isDay;
    private static MainActivity instance;
    IToggleCity iToggleCity;
    ArrayList<String> arrayList=new ArrayList<>();
DatabaseHelper databaseHelper;
    String apiID="8b8c8c2660e0b4db3a0fc5073ae2836a";
    List<cityObjectModel> cityList =new ArrayList<>();
    RecyclerView recyclerViewCity;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        instance=this;

        final String[] cityName = new String[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchView=findViewById(R.id.searchView);
        recyclerViewCity=findViewById(R.id.recyclerviewSavedData);
        getData("Chicago");
        loadDBData();
        iToggleCity=this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                 cityName[0]=query;
                    cityObjectModel cityObjectModelObj=new cityObjectModel();
                    cityObjectModelObj=getdbData(cityName);
               if( cityObjectModelObj.getCityName()!="")
               {

                   animate( cityObjectModelObj);
               }
               else
               {
                   cityName[0] = query;
                   getData(cityName[0]);
               }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        context=this;
        Intent intent = new Intent(this, DataDeleter.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 8640000, pendingIntent);
        final RelativeLayout constraintLayoutTodayCard =findViewById(R.id.constraintLayoutTodayCard);
        final RelativeLayout constraintLayoutMain =findViewById(R.id.constraintLayoutMain);
        final ConstraintLayout OfflineLayout=findViewById(R.id.OfflineLayout);
        if(isOnline()){constraintLayoutTodayCard.setVisibility(View.VISIBLE);OfflineLayout.setVisibility(View.GONE);}else{constraintLayoutMain.setVisibility(View.GONE);OfflineLayout.setVisibility(View.VISIBLE);}
        Button tryAgainButton=findViewById(R.id.buttonTryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){constraintLayoutMain.setVisibility(View.VISIBLE);OfflineLayout.setVisibility(View.GONE);recreate();}else{constraintLayoutMain.setVisibility(View.GONE);OfflineLayout.setVisibility(View.VISIBLE);}

            }
        });
        //constraintLayoutTodayCard.setBackgroundColor(R.color.night2blue);
       /* int colorFrom = getResources().getColor(R.color.colorPrimary);
        int colorTo = getResources().getColor(R.color.blue);
        @SuppressLint("RestrictedApi") ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(5000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //constraintLayoutMain.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
*/



        recyclerViewCity.setHasFixedSize(true);

        recyclerViewCity.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        final cityRecyclerViewHandler  cityRecyclerViewHandlerobj = new cityRecyclerViewHandler( cityList ,context,iToggleCity);
        recyclerViewCity.setAdapter(cityRecyclerViewHandlerobj);








    }
    public static MainActivity getInstance() {
        return instance;
    }
    void getData(final String cityName)
    {
        final cityObjectModel cityObjectModel=new cityObjectModel();
        final RequestQueue queue = Volley.newRequestQueue(this);
        final TextView tvcityName=findViewById(R.id.cityName);
        final String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+apiID;
        final String timezoneURL="https://dev.virtualearth.net/REST/v1/TimeZone/?query="+cityName+"&key=AjuoJGMFZOhYDFS_jzxwcu8khcsTmLi5lcuk5EMV4HwrIedU7eV9PS1IMyh-0nAu";



        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Response", response.toString());
                        try {
                            JSONArray jsonArrayWeather=response.getJSONArray("weather");
                            Log.d("Weather", jsonArrayWeather.toString());
                            JSONObject object=  jsonArrayWeather.getJSONObject(0);
                            String weatherStatus=object.getString("main");
                            Log.d("Test",weatherStatus);

                            String weatherDescription=  object.getString("description");
                            Log.d("Test",weatherDescription);
                            final TextView tvweatherStatus=findViewById(R.id.textViewWeatherStatus);
                            final TextView tvweatherDescription=findViewById(R.id.textViewWeatherDescription);  final TextView weatherType=findViewById(R.id.Temperature);




                            TextView tvCityName=findViewById(R.id.cityName);
                            tvCityName.setText(cityName);

                            final RelativeLayout constraintLayoutTodayCard =findViewById(R.id.constraintLayoutTodayCard);
                            int timeStampOBJ= response.getInt("dt") ;
                            int timezone=response.getInt("timezone");
                            int time=timeStampOBJ+timezone;
                            String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(new Date(time * 1000L));
                            Log.d("Test",(String.valueOf(dateAsText))  );
                            Log.d("Test",(String.valueOf(timezone))  );
                            JSONObject jsonArrayTemp=response.getJSONObject("main");
                            Log.d("Weather", jsonArrayTemp.toString());

                            int kelvin=jsonArrayTemp.getInt("temp");
                            int kelvinFeelsLike=jsonArrayTemp.getInt("feels_like");
                            int pressure=jsonArrayTemp.getInt("pressure");
                            int humidity=jsonArrayTemp.getInt("humidity");


                            final TextView tvPressure=findViewById(R.id.tvPressure);
                            final TextView tvHumidity=findViewById(R.id.tvHumidity);
                            tvPressure.setText(String.valueOf(pressure) );
                            tvHumidity.setText(String.valueOf((humidity)));

                            int  celsius = (int) (kelvin-273.15);

                            int  celsiusFeelsLike = (int) (kelvinFeelsLike-273.15);
                            JSONObject jsonArrayWind=response.getJSONObject("wind");


                            float windSpeed= (float) jsonArrayWind.getDouble("speed");
                            final TextView tvWindSpeed=findViewById(R.id.tvWind);final TextView tempFeelsLike =findViewById(R.id.tvTempFeelsLike);
                            tvWindSpeed.setText(String.valueOf(windSpeed));

                          //  weatherType.setText(String.valueOf(celsius)+"°C");

                            tempFeelsLike.setText("Feels like "+String.valueOf(celsiusFeelsLike)+"°C");
                            int x= (Integer.parseInt(String.valueOf(dateAsText.charAt(11)))*10+(Integer.parseInt(String.valueOf(dateAsText.charAt(12)))))-5;
                            if(x<0){x+=24;}

                            Log.d("Test",(String.valueOf(x))  );

                            cityObjectModel.setCityName(cityName);
                            cityObjectModel.setWeatherDescription(weatherDescription);
                            cityObjectModel.setWeatherType(weatherStatus);
                            cityObjectModel.setPressure(pressure);
                            cityObjectModel.setTemperature1(celsius);
                            cityObjectModel.setTemperatureFeelsLike(celsiusFeelsLike);
                            cityObjectModel.setWindSpeed(windSpeed);
                            cityObjectModel.setHumidity(humidity);
                            cityObjectModel.setTimeFactor(x);
                            //displayData(cityObjectModel);
                            animate(cityObjectModel);





                            if( cityList.stream().anyMatch(o -> o.getCityName().equals(cityName)))
                            {}
                            else
                            {
                                cityList.add(cityObjectModel);

                            }

                           refreshCachedCityData();
                            databaseHelper= new DatabaseHelper(MainActivity.this);
                            databaseHelper.Addtext(cityName,weatherStatus,weatherDescription,celsius,celsiusFeelsLike,humidity,pressure,windSpeed);

                            //arrayList=databaseHelper.getAllText();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        TextView test=findViewById(R.id.Temperature);
                        Toast toast=Toast.makeText(getApplicationContext(),"Please Enter A Valid City Name!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );


        queue.add(getRequest);

    }
    void clearCityList()
    {
        cityList.clear();
    }

    void changeBackground(String weatherType)
    {
        ImageView icon=findViewById(R.id.imageView2);
        icon.setVisibility(View.VISIBLE);
        ImageView humidityIcon=findViewById(R.id.imageViewHumidity);
        ImageView windIcon=findViewById(R.id.imageViewWind);
        ImageView pressureIcon=findViewById(R.id.imageViewPressure);


if(isDay){
    humidityIcon.setImageResource(R.drawable.drop);humidityIcon.setVisibility(View.VISIBLE);
    windIcon.setImageResource(R.drawable.wind);windIcon.setVisibility(View.VISIBLE);
    pressureIcon.setImageResource(R.drawable.gauge);pressureIcon.setVisibility(View.VISIBLE);


}
else
{
    humidityIcon.setImageResource(R.drawable.drop_);humidityIcon.setVisibility(View.VISIBLE);
    windIcon.setImageResource(R.drawable.wind_);windIcon.setVisibility(View.VISIBLE);
    pressureIcon.setImageResource(R.drawable.gauge_);pressureIcon.setVisibility(View.VISIBLE);
}

        final RelativeLayout constraintLayoutTodayCard =findViewById(R.id.constraintLayoutTodayCard);
        int colorFrom = getResources().getColor(R.color.colorPrimary);
        int colorTo;
        switch  (weatherType)
    {
        case "Rain" : if(isDay){            icon.setImageResource(R.drawable.rain2);   break;
        }else{            icon.setImageResource(R.drawable.rain_1);   break;
        }
        case "Clouds" :  if(isDay){            icon.setImageResource(R.drawable.cloud);   break;
        }else{            icon.setImageResource(R.drawable.cloud_);   break;
        }

        case "Haze" :  if(isDay){            icon.setImageResource(R.drawable.cloudy);   break;
        }else{            icon.setImageResource(R.drawable.cloudy_);   break;
        }

        case "Clear" :  if(isDay){            icon.setImageResource(R.drawable.sun);   break;
        }else{            icon.setImageResource(R.drawable.moon);   break;
        }


        default: break;

    }

    }
    void refreshCachedCityData( )
    {

        final cityRecyclerViewHandler cityRecyclerViewHandlerobj = new cityRecyclerViewHandler(cityList,context,iToggleCity);
        recyclerViewCity.setAdapter(cityRecyclerViewHandlerobj);
        cityRecyclerViewHandlerobj.notifyDataSetChanged();
    }
    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {
        int colorFrom = getResources().getColor(R.color.colorPrimary);
        int colorTo; final RelativeLayout constraintLayoutTodayCard =findViewById(R.id.constraintLayoutTodayCard);

        int fadeInDuration = 500; // Configure time values here
        int timeBetween = 300;
        int fadeOutDuration = 500;

        imageView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        ColorDrawable viewColor = (ColorDrawable) constraintLayoutTodayCard.getBackground();
        colorFrom = getResources().getColor(R.color.night2blue);
        colorTo=getResources().getColor(R.color.dayblue);
        @SuppressLint("RestrictedApi") final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1300); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                constraintLayoutTodayCard.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });


        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - (images.length - 1) > imageIndex) {
                  //  animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
                    colorAnimation.start();
                }
                else {
                    if (forever == true){
                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
                        colorAnimation.start();
                    }
                }
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {Toast toast=Toast.makeText(getApplicationContext(),"Online",Toast.LENGTH_LONG);
            toast.show();
            return true;
        } else {
            Toast toast=Toast.makeText(getApplicationContext(),"App not connected to the internet",Toast.LENGTH_LONG);
            toast.show();

            return false;
        }
    }

cityObjectModel getdbData(String[] cityName) {
    cityObjectModel cityObjectModel = null;
    try {
        databaseHelper = new DatabaseHelper(MainActivity.this);
        Cursor rs = databaseHelper.getData(cityName[0]);
        //  id_To_Update = Value;
        rs.moveToFirst();
        cityObjectModel = new cityObjectModel();

        String dbcityname = rs.getString(rs.getColumnIndex(DatabaseHelper.CITY_NAME));
        String dbweather = rs.getString(rs.getColumnIndex(DatabaseHelper.cityWeather));
        String dbweatherdesc = rs.getString(rs.getColumnIndex(DatabaseHelper.cityWeatherDescription));
        String dbtemp = rs.getString(rs.getColumnIndex(DatabaseHelper.cityTemperature));
        String dbtmpfeel = rs.getString(rs.getColumnIndex(DatabaseHelper.cityTempFeelsLike));
        String dbwind = rs.getString(rs.getColumnIndex(DatabaseHelper.cityWind));
        String dbpressure = rs.getString(rs.getColumnIndex(DatabaseHelper.cityPressure));
        String dbhumidity = rs.getString(rs.getColumnIndex(DatabaseHelper.cityHumidity));
        cityObjectModel.setCityName(dbcityname);
        cityObjectModel.setWeatherType(dbweather);
        cityObjectModel.setWeatherDescription(dbweatherdesc);
        cityObjectModel.setTemperatureFeelsLike(Integer.parseInt(dbtmpfeel));
        cityObjectModel.setTemperature1(Integer.parseInt(dbtemp));
        cityObjectModel.setHumidity(Integer.parseInt(dbhumidity));
        cityObjectModel.setPressure(Integer.parseInt(dbpressure));

        Log.d("W", (String.valueOf(arrayList)));
        Log.d("W", (String.valueOf(dbcityname)));
        Log.d("W", (String.valueOf(dbweatherdesc)));
        Log.d("W", (String.valueOf(dbweatherdesc)));
        Log.d("W", (String.valueOf(dbtmpfeel)));

        return cityObjectModel;
    } catch (Exception e) {
        cityObjectModel.setCityName("");
        return cityObjectModel;
    }
}


void animate( cityObjectModel cityObjectModelObj){
    final TextView tvweatherStatus=findViewById(R.id.textViewWeatherStatus);
    final TextView tvweatherDescription=findViewById(R.id.textViewWeatherDescription);
    final ImageView demoImage = (ImageView) findViewById(R.id.imageView);
    final TextView tvCityName=findViewById(R.id.cityName);
    final TextView tvPressure=findViewById(R.id.tvPressure);
    final TextView tvHumidity=findViewById(R.id.tvHumidity);
    final TextView tvWindSpeed=findViewById(R.id.tvWind);
    final TextView tempFeelsLike =findViewById(R.id.tvTempFeelsLike);
    final TextView tvtemp =findViewById(R.id.Temperature);
int x=cityObjectModelObj.getTimeFactor();
    tvweatherStatus.setText(cityObjectModelObj.getWeatherType());
    tvweatherDescription.setText(cityObjectModelObj.getWeatherDescription());
    tvCityName.setText(cityObjectModelObj.getCityName());
    tvWindSpeed.setText(String.valueOf( cityObjectModelObj.getWindSpeed()));
    tvHumidity.setText(String.valueOf(cityObjectModelObj.getHumidity()));
    tvPressure.setText(String.valueOf(cityObjectModelObj.getPressure()));
    tempFeelsLike.setText("Feels like "+String.valueOf(cityObjectModelObj.getTemperatureFeelsLike())+"°C");
    tvtemp.setText(String.valueOf(cityObjectModelObj.getTemperature1())+"°C");
    if(x>=16&&x<19)
    {
        isDay=true;
        changeBackground((String) tvweatherStatus.getText());

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        tvweatherStatus.setAnimation(animation);
        demoImage.setAnimation(animation);
        tvCityName.setAnimation(animation);
        tvHumidity.setAnimation(animation);
        tvPressure.setAnimation(animation);
        tvWindSpeed.setAnimation(animation);
        tempFeelsLike.setAnimation(animation);
        tvweatherStatus.setAnimation(animation);
        tvweatherDescription.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                demoImage.setImageResource(R.drawable.dusk);
                tvCityName.setTextColor(Color.parseColor("#000000"));
                tvweatherStatus.setTextColor(Color.parseColor("#000000"));
tvtemp.setTextColor(Color.parseColor("#000000"));
                tvHumidity.setTextColor(Color.parseColor("#000000"));
                tvPressure.setTextColor(Color.parseColor("#000000"));
                tvWindSpeed.setTextColor(Color.parseColor("#000000"));
                tempFeelsLike.setTextColor(Color.parseColor("#000000"));
                tvweatherStatus.setTextColor(Color.parseColor("#000000"));
                tvweatherDescription.setTextColor(Color.parseColor("#000000"));
            }
        }, 2000);


    }
    else  if(x>=10&&x<16  )
    {
        isDay=true;  changeBackground((String) tvweatherStatus.getText());

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        tvweatherStatus.setAnimation(animation);
        demoImage.setAnimation(animation);
        tvCityName.setAnimation(animation);
        tvHumidity.setAnimation(animation);
        tvPressure.setAnimation(animation);
        tvWindSpeed.setAnimation(animation);
        tempFeelsLike.setAnimation(animation);
        tvweatherStatus.setAnimation(animation);
        tvweatherDescription.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                demoImage.setImageResource(R.drawable.day2);
                tvCityName.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherStatus.setTextColor(Color.parseColor("#FFFFFF"));
                tvtemp.setTextColor(Color.parseColor("#FFFFFF"));
                tvHumidity.setTextColor(Color.parseColor("#FFFFFF"));
                tvPressure.setTextColor(Color.parseColor("#FFFFFF"));
                tvWindSpeed.setTextColor(Color.parseColor("#FFFFFF"));
                tempFeelsLike.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherStatus.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }, 2000);


    }
    else  if(x<10&&x>=6  )
    {
        isDay=true;   changeBackground((String) tvweatherStatus.getText());

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        tvweatherStatus.setAnimation(animation);
        demoImage.setAnimation(animation);
        tvCityName.setAnimation(animation);
        tvHumidity.setAnimation(animation);
        tvPressure.setAnimation(animation);
        tvWindSpeed.setAnimation(animation);
        tempFeelsLike.setAnimation(animation);
        tvweatherStatus.setAnimation(animation);
        tvweatherDescription.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                demoImage.setImageResource(R.drawable.day);
                tvCityName.setTextColor(Color.parseColor("#000000"));
                tvweatherStatus.setTextColor(Color.parseColor("#000000"));
                tvtemp.setTextColor(Color.parseColor("#000000"));
                tvHumidity.setTextColor(Color.parseColor("#000000"));
                tvPressure.setTextColor(Color.parseColor("#000000"));
                tvWindSpeed.setTextColor(Color.parseColor("#000000"));
                tempFeelsLike.setTextColor(Color.parseColor("#000000"));
                tvweatherStatus.setTextColor(Color.parseColor("#000000"));
                tvweatherDescription.setTextColor(Color.parseColor("#000000"));
            }
        }, 2000);


    }
    else
    {

        isDay=false;   changeBackground((String) tvweatherStatus.getText());


        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        demoImage.setAnimation(animation);
        tvCityName.setAnimation(animation);
        tvweatherStatus.setAnimation(animation);
        tvHumidity.setAnimation(animation);
        tvPressure.setAnimation(animation);
        tvWindSpeed.setAnimation(animation);
        tempFeelsLike.setAnimation(animation);
        tvweatherStatus.setAnimation(animation);
        tvweatherDescription.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                demoImage.setImageResource(R.drawable.night);
                tvCityName.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherStatus.setTextColor(Color.parseColor("#FFFFFF"));
                tvHumidity.setTextColor(Color.parseColor("#FFFFFF"));
                tvPressure.setTextColor(Color.parseColor("#FFFFFF"));
                tvWindSpeed.setTextColor(Color.parseColor("#FFFFFF"));
                tempFeelsLike.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherStatus.setTextColor(Color.parseColor("#FFFFFF"));
                tvweatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
                tvtemp.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }, 2000);


    }

}
@RequiresApi(api = Build.VERSION_CODES.N)
void loadDBData()
{
    try {
        databaseHelper = new DatabaseHelper(MainActivity.this);
       // databaseHelper.deleteAllData();
        arrayList = databaseHelper.getAllText();



   for(int i=0;i<arrayList.size()-8;i+=8)
   {
       cityObjectModel c=new cityObjectModel();
       String cityName=arrayList.get(i);
       c.setCityName(arrayList.get(i));

       c.setWeatherType(arrayList.get(i+1));
       c.setWeatherDescription(arrayList.get(i+2));
       c.setTemperature1(Integer.parseInt( arrayList.get(i+3)));
       c.setTemperatureFeelsLike(Integer.parseInt(arrayList.get(i+4)));
       float f=Float.valueOf(arrayList.get(i+5));
       int m=(int)f;
       c.setHumidity(m);
       float f1=Float.valueOf(arrayList.get(i+6));
       int m11=(int)f;
       c.setPressure(m11);
       float f2=Float.valueOf(arrayList.get(i+7));
       c.setWindSpeed(f2);
       if( cityList.stream().anyMatch(o -> o.getCityName().equals(cityName)))
       {
       }
       else
       {
           cityList.add(c);

        }


   }
    refreshCachedCityData( );
    Log.e("DBDAta",arrayList.toString());
    }
    catch (Exception e)
    {

    }}

    @Override
    public void ToggleCityI(cityObjectModel cityObjectModel) {
        animate(cityObjectModel);
    }

}


