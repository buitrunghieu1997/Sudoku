package com.example.latrodectus.sudoku.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.utils.Constant;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.trans));
        getSupportActionBar().hide();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        final ImageView avatar = findViewById(R.id.avatar);
        final TextView appName = findViewById(R.id.appName);
        final TextView name = findViewById(R.id.name);
        final TextView id = findViewById(R.id.id);
        final TextView subject = findViewById(R.id.subject);

        avatar.setVisibility(View.INVISIBLE);
        appName.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        id.setVisibility(View.INVISIBLE);
        subject.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 738);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 739);
            }
        }

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(Constant.SLEEP_TIME_1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                // fade out view nicely
                AlphaAnimation alphaAnim = new AlphaAnimation(Constant.TRANSPARENT, Constant.FULL_APPEARANCE);
                alphaAnim.setDuration(Constant.ANIMATION_TIME_3000);
                alphaAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        avatar.setVisibility(View.INVISIBLE);
                        appName.setVisibility(View.INVISIBLE);
                        name.setVisibility(View.INVISIBLE);
                        id.setVisibility(View.INVISIBLE);
                        subject.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        avatar.setVisibility(View.VISIBLE);
                        appName.setVisibility(View.VISIBLE);
                        name.setVisibility(View.VISIBLE);
                        id.setVisibility(View.VISIBLE);
                        subject.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(SplashScreen.this, StartScreen.class);
                        startActivity(intent);
                    }

                });

                avatar.startAnimation(alphaAnim);
                appName.startAnimation(alphaAnim);
                name.startAnimation(alphaAnim);
                id.startAnimation(alphaAnim);
                subject.startAnimation(alphaAnim);
            }
        }.execute();
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
