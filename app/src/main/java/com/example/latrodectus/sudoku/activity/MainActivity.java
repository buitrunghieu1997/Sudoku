package com.example.latrodectus.sudoku.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.logic.SudokuChecker;
import com.example.latrodectus.sudoku.model.Cell.ParentCell;
import com.example.latrodectus.sudoku.model.Grid;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;
import com.example.latrodectus.sudoku.viewmodel.SudokuEngine;
import com.example.latrodectus.sudoku.viewmodel.SudokuGenerator;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    public static SudokuEngine engine = SudokuEngine.getInstance();
    ActionBar actionBar;

    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.trans));
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        actionBar = getSupportActionBar();
        actionBar = getSupportActionBar();
        actionBar.setTitle("ActionBarDemo3");
        actionBar.setSubtitle("Version3.0");
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true); // allow custom views to be shown
        actionBar.setDisplayHomeAsUpEnabled(true); // show ‘UP’ affordance < button
        actionBar.setDisplayShowHomeEnabled(true); // allow app icon – logo to be shown
        actionBar.setHomeButtonEnabled(true);
        actionBar.setLogo(R.drawable.menu_icon);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(100, 255, 255, 255)));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        SharedPreferencesObject.storeGrid(this, SudokuEngine.getInstance().getGrid().getArayNum());
        if (SudokuEngine.getInstance().getGrid().checkGame()) {
            SharedPreferencesObject.storePlayState(this, false);
        } else {
            SharedPreferencesObject.storePlayState(this, true);
        }
        SharedPreferencesObject.storePlayState(this, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(MainActivity.this, StartScreen.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_share) {
            if (item.getItemId() == R.id.action_share) {
                Bitmap bm = screenShot(getWindow().getDecorView().getRootView());
                File file = saveBitmap(bm, "mantis_image.png");
                Log.i("chase", "filepath: " + file.getAbsolutePath());
                Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "share via"));
            }
            return super.onOptionsItemSelected(item);
            //return true;
        } else if (id == R.id.action_about) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_custom);
            dialog.setTitle("About Us");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private int[][] getFlag(int[][] sudoku) {
        int[][] flag = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];
        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                if (sudoku[x][y] > 0) {
                    flag[x][y] = 0; //khong duoc sua
                } else {
                    flag[x][y] = 1; //duoc sua
                }
            }
        }
        return flag;
    }

    private void fetch() {
        if (!SharedPreferencesObject.getPlayState(this)) {
            engine.createGrid(this);
            int[][] sudoku = engine.getSudoku();
            int[][] flag = getFlag(sudoku);
            SharedPreferencesObject.storeFlag(this, flag);
            if (SudokuEngine.getInstance().getGrid().checkGame()) {
                SharedPreferencesObject.storePlayState(this, false);
            } else {
                SharedPreferencesObject.storePlayState(this, true);
            }
        } else {
            if (SharedPreferencesObject.getCount() == 81) {
                engine.createGrid(this);
                int[][] sudoku = engine.getSudoku();
                int[][] flag = getFlag(sudoku);
                SharedPreferencesObject.storeFlag(this, flag);
                    SharedPreferencesObject.storePlayState(this, false);
            } else {
                engine.setGrid(this, SharedPreferencesObject.getGrid(this), SharedPreferencesObject.getFlag(this));
            }
        }
    }

    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
