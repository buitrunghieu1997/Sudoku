package com.example.latrodectus.sudoku.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.logic.SudokuChecker;
import com.example.latrodectus.sudoku.bgrservice.MusicService;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;

public class StartScreen extends AppCompatActivity implements View.OnClickListener {
    Button continueBtn, startBtn, settingBtn, aboutBtn, quitBtn;
    ImageView logo;
    public static boolean isIncompleted = false;
    private boolean isBound = false;
    private MusicService musicService;
    Intent music;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferencesObject.storePlayState(this, false);
        if (SharedPreferencesObject.getGrid(this) != null && SharedPreferencesObject.getCount()!= 81) {
            SharedPreferencesObject.storePlayState(this, true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen); //disable phim back quay tro lai man hinh truoc
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.trans));
        getSupportActionBar().hide();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        continueBtn = findViewById(R.id.continue_btn);
        startBtn = findViewById(R.id.start_btn);
        settingBtn = findViewById(R.id.setting_btn);
        aboutBtn = findViewById(R.id.about_btn);
        quitBtn = findViewById(R.id.quit_btn);
        logo = findViewById(R.id.logo_start);

        Animation animation = new AlphaAnimation(Constant.FULL_APPEARANCE, Constant.TRANSPARENT_30);
        animation.setDuration(Constant.ANIMATION_TIME_1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        logo.startAnimation(animation);

        continueBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        if (SharedPreferencesObject.getBgrState(this) != 0) {
            doBindService();
            music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }
        SharedPreferencesObject.storePlayState(this, false);
        if (SharedPreferencesObject.getGrid(this) != null && !SudokuChecker.getInstance().checkSudoku(SharedPreferencesObject.getGrid(this))) {
            SharedPreferencesObject.storePlayState(this, true);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(StartScreen.this, R.anim.button_click));
        int id = v.getId();
        if (id == continueBtn.getId()) {
            if (SharedPreferencesObject.getPlayState(this)) {
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "You do not have any incompleted game. Let start a new game", Toast.LENGTH_SHORT).show();
            }
        } else if (id == startBtn.getId()) {
            if (SharedPreferencesObject.getPlayState(this)) {
                String title = "Start a new game";
                String message = "You have an imcompleted game before, do you really want to start a new game?";
                showDialog(title, message, Constant.TYPE_CONFIRM);
            } else {
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                startActivity(intent);
                SharedPreferencesObject.storePlayState(this, true);
            }
        } else if (id == settingBtn.getId()) {
            Intent intent = new Intent(StartScreen.this, Setting.class);
            startActivity(intent);
        } else if (id == aboutBtn.getId()) {
            Intent intent = new Intent(StartScreen.this, About.class);
            startActivity(intent);
        } else if (id == quitBtn.getId()) {
            String title = "Exit game";
            String message = "Do you really want to exit?";
            showDialog(title, message, Constant.TYPE_EXIT);
        } else {
            //do nothing
        }
    }

    public void showDialog(String title, String message, final String type) {
        final Context context = this.getBaseContext();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                switch (type) {
                                    case Constant.TYPE_EXIT: {
                                        finishAffinity();
                                        System.exit(Constant.EXIT_CODE);
                                        break;
                                    }
                                    case Constant.TYPE_CONFIRM: {
                                        Intent intent = new Intent(StartScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        SharedPreferencesObject.storePlayState(context, false);
                                    }
                                }
                            }
                        })

                .setNeutralButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        })

                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            musicService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            unbindService(Scon);
            isBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        String title = "Thoát game";
        String message = "Bạn có muốn thoát game không?";
        showDialog(title, message, Constant.TYPE_EXIT);
        if(music != null) {
            stopService(music);
        }
    }
}
