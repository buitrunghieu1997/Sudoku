package com.example.latrodectus.sudoku.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.latrodectus.sudoku.activity.MainActivity;
import com.example.latrodectus.sudoku.activity.Setting;
import com.example.latrodectus.sudoku.activity.StartScreen;
import com.example.latrodectus.sudoku.model.Grid;
import com.example.latrodectus.sudoku.model.NumberButton;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;

public class SudokuEngine {
    private static SudokuEngine instance;

    private Grid grid = null;

    private int[][] sudoku;

    private int selectedX = Constant.INIT_CELL_POSITION, selectedY = Constant.INIT_CELL_POSITION;

    private SudokuEngine() {
    }

    public static SudokuEngine getInstance() {
        if (instance == null) {
            instance = new SudokuEngine();
        }
        return instance;
    }

    public void createGrid(Context context) { //vi sudokuCell can context
        sudoku = SudokuGenerator.getInstance().generateGrid();
        sudoku = SudokuGenerator.getInstance().removeElements(sudoku, SharedPreferencesObject.getHardLevel(context));
        grid = new Grid(context);
        grid.setGrid(sudoku, SudokuGenerator.getInstance().getFlag(sudoku));
    }

    public void setGrid(Context context, int[][] sudoku, int[][] flag) {
        grid = new Grid(context);
        grid.setGrid(sudoku, flag);
    }

    public Grid getGrid() {
        return grid;
    }

    public void setSelectedPosition(int x, int y) {
        selectedX = x;
        selectedY = y;
    }

    public void setNumber(int number) {
        if (selectedX != Constant.INIT_CELL_POSITION && selectedY != Constant.INIT_CELL_POSITION) {
            grid.setNumber(selectedX, selectedY, number);
        }
        //sau khi dien them 1 so thi goi ham checkGame
        if(grid.checkGame()) {
            SharedPreferencesObject.storePlayState(getGrid().getContext(), false);
            showDialog("Chúc mừng", "Bạn đã hoàn thành game, hãy quay trở lại màn hình chính", grid.getContext());
        }
    }

    public int[][] getSudoku() {
        return sudoku;
    }

    public void showDialog(String title, String message, final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
