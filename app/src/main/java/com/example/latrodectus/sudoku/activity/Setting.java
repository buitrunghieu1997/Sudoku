package com.example.latrodectus.sudoku.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.bgrservice.MusicService;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;

public class Setting extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener {
    Switch sfx, bgr;
    RadioButton easy, medium, hard, hell, test;
    private boolean isBound = false;
    private MusicService musicService;
    Intent music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.trans));
        getSupportActionBar().hide();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        sfx = findViewById(R.id.sound_effect);
        bgr = findViewById(R.id.background_music);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);
        hell = findViewById(R.id.hell);
        test = findViewById(R.id.test);

        sfx.setOnCheckedChangeListener(this);
        bgr.setOnCheckedChangeListener(this);
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);
        hell.setOnClickListener(this);
        test.setOnClickListener(this);

        if (SharedPreferencesObject.getBgrState(this) != 0) {
            bgr.setChecked(true);
        } else {
            bgr.setChecked(false);
        }

        if (SharedPreferencesObject.getSfxState(this) != 0) {
            sfx.setChecked(true);
        } else {
            sfx.setChecked(false);
        }

        switch (SharedPreferencesObject.getHardLevel(this)) {
            case Constant.MEDIUM_LEVEL_EMPTY_CELL: {
                easy.setChecked(true);
                break;
            }
            case Constant.EASY_LEVEL_EMPTY_CELL: {
                medium.setChecked(true);
                break;
            }
            case Constant.HARD_LEVEL_EMPTY_CELL: {
                hard.setChecked(true);
                break;
            }
            case Constant.HELL_LEVEL_EMPTY_CELL: {
                hell.setChecked(true);
                break;
            }
            case Constant.TEST_LEVEL_EMPTY_CELL: {
                test.setChecked(true);
                break;
            }
            default: {
                medium.setChecked(true);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int hardLevel = Constant.INIT_STATE;
        if (v.getId() == easy.getId()) {
            hardLevel = Constant.EASY_LEVEL_EMPTY_CELL;
        } else if (v.getId() == medium.getId()) {
            hardLevel = Constant.MEDIUM_LEVEL_EMPTY_CELL;
        } else if (v.getId() == hard.getId()) {
            hardLevel = Constant.HARD_LEVEL_EMPTY_CELL;
        } else if (v.getId() == hell.getId()) {
            hardLevel = Constant.HELL_LEVEL_EMPTY_CELL;
        } else if (v.getId() == test.getId()) {
            hardLevel = Constant.TEST_LEVEL_EMPTY_CELL;
        } else {
            //do nothing
        }
        SharedPreferencesObject.storeHardLevel(this, hardLevel);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int bgrState = Constant.INIT_STATE, sfxState = Constant.INIT_STATE;
        if (buttonView.getId() == sfx.getId()) {
            if (isChecked) {
                sfxState = Constant.STATE_ON;
            } else {
                sfxState = Constant.STATE_OFF;
            }
            SharedPreferencesObject.storeSfxState(this, sfxState);
        } else if (buttonView.getId() == bgr.getId()) {
            if (isChecked) {
                bgrState = Constant.STATE_ON;
                doBindService();
                music = new Intent();
                Intent intent = music.setClass(this, MusicService.class);
                startService(music);
            } else {
                bgrState = Constant.STATE_OFF;
                doBindService();
                music = new Intent();
                Intent intent = music.setClass(this, MusicService.class);
                stopService(music);
            }
            SharedPreferencesObject.storeBgrState(this, bgrState);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
