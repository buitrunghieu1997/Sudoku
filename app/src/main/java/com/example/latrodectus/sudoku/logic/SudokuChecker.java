package com.example.latrodectus.sudoku.logic;

import com.example.latrodectus.sudoku.utils.Constant;

public class SudokuChecker {
    private static SudokuChecker instance;

    private SudokuChecker() {
    }

    public static SudokuChecker getInstance() {
        if (instance == null) {
            instance = new SudokuChecker();
        }
        return instance;
    }

    public boolean checkSudoku(int[][] sudoku) {
        return (checkHorizontal(sudoku) && checkVertical(sudoku) && checkRegions(sudoku));
    }

    private boolean checkHorizontal(int[][] sudoku) {
        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int xP = 0; xP < Constant.MAX_COLUMN; xP++) {
                if (sudoku[xP][y] == Constant.EMPTY_CELL_VALUE) {
                    return false;
                }
                for (int x = xP + 1; x < Constant.MAX_COLUMN; x++) {
                    if (sudoku[xP][y] == sudoku[x][y] || sudoku[x][y] == Constant.EMPTY_CELL_VALUE) {
                        return false; //duyet tu so dang xet xem cac so sau co trung hoac trong k
                    }
                }
            }
        }
        return true;
    }

    private boolean checkVertical(int[][] sudoku) {
        for (int x = 0; x < Constant.MAX_COLUMN; x++) {
            for (int yP = 0; yP < Constant.MAX_ROW; yP++) {
                if (sudoku[x][yP] == Constant.EMPTY_CELL_VALUE) {
                    return false; //neu con o trong
                }
                for (int y = yP + 1; y < Constant.MAX_ROW; y++) {
                    if (sudoku[x][yP] == sudoku[x][y] || sudoku[x][y] == Constant.EMPTY_CELL_VALUE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkRegions(int[][] sudoku) {
        for (int xRegion = 0; xRegion < Constant.MAX_COLUMN_REGION; xRegion++) { // co 3 o 3x3
            for (int yRegion = 0; yRegion < Constant.MAX_ROW_REGION; yRegion++) {
                if (!checkRegion(sudoku, xRegion, yRegion)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRegion(int[][] sudoku, int xRegion, int yRegion) {
        for (int xP = xRegion * Constant.MAX_REGION; xP < xRegion * Constant.MAX_REGION + Constant.MAX_COLUMN_REGION; xP++) {
            for (int yP = xRegion * Constant.MAX_REGION; yP < xRegion * Constant.MAX_REGION + Constant.MAX_ROW_REGION; yP++) {
                for (int x = xP; x < xRegion * Constant.MAX_REGION + Constant.MAX_COLUMN_REGION; x++) {
                    for (int y = yP; y < xRegion * Constant.MAX_REGION + Constant.MAX_ROW_REGION; y++) {
                        if (((x != xP || y != yP) && sudoku[xP][yP] == sudoku[x][y]) || sudoku[x][y] == Constant.EMPTY_CELL_VALUE) { //x!=xP || y!=yP kiem tra neu 2 diem k trung nhau
                            return false; //2 cau lenh duyet xP,yP, hai cau duyet x,y kiem tra trung hoac rong
                        }
                    }
                }
            }
        }
        return true;
    }
}
