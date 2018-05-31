package com.example.latrodectus.sudoku.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.latrodectus.sudoku.utils.Constant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class SharedPreferencesObject {
    private static int count = 0;

    public static int getCount() {
        return count;
    }

    public static boolean storePlayState(Context context, boolean playState) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("PLAY", playState);
        return editor.commit();
    }

    public static boolean getPlayState(Context context) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean playState = sp.getBoolean("PLAY", false);

        return playState;
    }

    public static boolean storeHardLevel(Context context, int hardLevel) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt("HARD_LEVEL", hardLevel);
        return editor.commit();
    }

    public static boolean storeBgrState(Context context, int bgrState) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt("BGR", bgrState);
        return editor.commit();
    }

    public static boolean storeSfxState(Context context, int sfxState) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt("SFX", sfxState);
        return editor.commit();
    }

    public static int getHardLevel(Context context) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int hardLevel = sp.getInt("HARD_LEVEL", 0);

        return hardLevel;
    }

    public static int getBgrState(Context context) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int bgrState = sp.getInt("BGR", 0);

        return bgrState;
    }

    public static int getSfxState(Context context) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int sfxState = sp.getInt("SFX", 0);

        return sfxState;
    }

    public static boolean storeGrid(Context context, int[][] sudoku) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CELL_COUNT", Constant.MAX_CELL);

        for (int i = 0; i < Constant.MAX_CELL; i++) {
            editor.remove("CELL_VALUE_" + i);
            editor.putInt("CELL_VALUE_" + i, sudoku[i % Constant.MAX_COLUMN][i / Constant.MAX_ROW]);
            if(sudoku[i % Constant.MAX_COLUMN][i / Constant.MAX_ROW] > 0) {
                count++;
            }
        }
        return editor.commit();
    }

    public static boolean storeFlag(Context context, int[][] flag) {
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CELL_FLAG_COUNT", Constant.MAX_CELL);

        for (int i = 0; i < Constant.MAX_CELL; i++) {
            editor.remove("CELL_FLAG_" + i);
            editor.putInt("CELL_FLAG_" + i, flag[i % Constant.MAX_COLUMN][i / Constant.MAX_ROW]);
        }
        return editor.commit();
    }

    public static int[][] getGrid(Context context) {
        int[][] sudoku = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int size = sp.getInt("CELL_COUNT", 0);

        for (int i = 0; i < size; i++) {
            sudoku[i % Constant.MAX_COLUMN][i / Constant.MAX_ROW] = sp.getInt("CELL_VALUE_" + i, 0);
        }
        return sudoku;
    }

    public static int[][] getFlag(Context context) {
        int[][] flag = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];
        android.content.SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int size = sp.getInt("CELL_FLAG_COUNT", 0);

        for (int i = 0; i < size; i++) {
            flag[i % Constant.MAX_COLUMN][i / Constant.MAX_ROW] = sp.getInt("CELL_FLAG_" + i, 0);
        }
        return flag;
    }

    public static boolean storeScore() {
        return false;
    }

    public static int[] getScore() {
        return null;
    }

    public static boolean storeHint() {
        return false;
    }

    public static int[] getHint() {
        return null;
    }

    public static boolean storeTime() {
        return false;
    }

    public static int[] getTime() {
        return null;
    }
}
