package com.example.latrodectus.sudoku.bgrservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.latrodectus.sudoku.R;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer player;
    private int length = 0;

    public MusicService() {
    }

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.bgr_music);
        player.setOnErrorListener(this);

        if (player != null) {
            player.setLooping(true);
            player.setVolume(100, 100);
        }


        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(player, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (player.isPlaying()) {
            player.pause();
            length = player.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (player.isPlaying() == false) {
            player.seekTo(length);
            player.start();
        }
    }

    public void stopMusic() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            try {
                player.stop();
                player.release();
            } finally {
                player = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (player != null) {
            try {
                player.stop();
                player.release();
            } finally {
                player = null;
            }
        }
        return false;
    }
}