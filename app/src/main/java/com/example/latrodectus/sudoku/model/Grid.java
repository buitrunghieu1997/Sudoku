package com.example.latrodectus.sudoku.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.latrodectus.sudoku.activity.MainActivity;
import com.example.latrodectus.sudoku.activity.StartScreen;
import com.example.latrodectus.sudoku.logic.SudokuChecker;
import com.example.latrodectus.sudoku.model.Cell.SudokuCell;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;

public class Grid {
    private SudokuCell[][] sudoku = new SudokuCell[Constant.MAX_COLUMN][Constant.MAX_ROW];

    private Context context;

    public Context getContext() {
        return context;
    }

    public Grid(Context context) {
        this.context = context;

        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                sudoku[x][y] = new SudokuCell(context); //ham khoi tao cua sudokuCell chi can context nen o tren cung chi can truyen context
            }
        }
    }

    public void setGrid(int[][] grid, int[][] flag) {
        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                sudoku[x][y].setInitValue(grid[x][y]); //doc comment trong ham setInitValue
                if (grid[x][y] != Constant.EMPTY_CELL_VALUE && flag[x][y] == 0) {
                    sudoku[x][y].setModifiable(false);
                }
            }
        }
    }

    public SudokuCell[][] getGrid() {
        return sudoku;
    }

    public SudokuCell getElement(int x, int y) {
        return sudoku[x][y];
    }

    public int[][] getArayNum() {
        int[][] sudoku = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];
        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                sudoku[x][y] = getElement(x, y).getValue();
            }
        }
        return sudoku;
    }

    public SudokuCell getElement(int position) {
        int x = position % Constant.MAX_COLUMN;
        int y = position / Constant.MAX_ROW;
        return sudoku[x][y];
    }

    public void setNumber(int x, int y, int number) {
        sudoku[x][y].setValue(number); //set gia tri cho 1 cell, dung khi an 1 buttom
    }

    public boolean checkGame() {
        if (SudokuChecker.getInstance().checkSudoku(getArayNum())) {
            SharedPreferencesObject.storePlayState(context, false);
            return true;
        }
        return false;
    }
}
