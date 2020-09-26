package com.zappfoundry.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenHandlerActivity extends AppCompatActivity

{
    private int shortAnimationDuration;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final TextView textViewWeatherApp = findViewById(R.id.textViewWeatherApp);
        textViewWeatherApp.setAlpha(0f);
        textViewWeatherApp.setVisibility(View.VISIBLE);

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        textViewWeatherApp.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);


        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over

                textViewWeatherApp.animate()
                        .alpha(0f)
                        .setDuration(shortAnimationDuration)
                        .setListener(null);
            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreenHandlerActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }

}
