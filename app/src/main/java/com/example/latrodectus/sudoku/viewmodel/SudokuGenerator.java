package com.example.latrodectus.sudoku.viewmodel;

import com.example.latrodectus.sudoku.utils.Constant;

import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    private static SudokuGenerator instance;

    private ArrayList<ArrayList<Integer>> available = new ArrayList<>(); //mang chua cac so co the dien vao bang sudoku

    int[][] flag = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];

    private Random rand = new Random();

    private SudokuGenerator() {
        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                flag[x][y] = 1; // No conflict
            }
        }

    }

    public static SudokuGenerator getInstance() {
        if (instance == null) {
            instance = new SudokuGenerator();
        }
        return instance;
    }

    public int[][] generateGrid() {
        int[][] sudoku = new int[Constant.MAX_COLUMN][Constant.MAX_ROW];

        int currentPos = 0;

        while (currentPos < Constant.MAX_CELL) {
            if (currentPos == 0) {
                clearGrid(sudoku);
            }
            if (available.get(currentPos).size() != 0) {
                int i = rand.nextInt(available.get(currentPos).size()); //chon 1 vi tri ngau nhien
                int number = available.get(currentPos).get(i); //lay ra gia tri cua no trong available

                if (!checkOnConflict(sudoku, currentPos, number)) {
                    int xPos = currentPos % Constant.MAX_COLUMN;
                    int yPos = currentPos / Constant.MAX_ROW;

                    sudoku[xPos][yPos] = number;

                    available.get(currentPos).remove(i); //loai so da dung ra khoi mang available va quay lui

                    currentPos++;
                } else {
                    available.get(currentPos).remove(i); //loai so khong thoa man ra khoi mang available va quay lui
                }
            } else {
                for (int i = 1; i <= Constant.NUMBER_COUNT; i++) {
                    available.get(currentPos).add(i); //khoi tao mot arraylist cho 1 o neu no chua duoc khoi tao
                }
                currentPos--; //quay lui
            }
        }

        return sudoku;
    }

    public int[][] removeElements(int[][] sudoku, int hardLevel) {
        int i = 0;

        while (i < hardLevel) {
            int x = rand.nextInt(9);
            int y = rand.nextInt(9);

            if (sudoku[x][y] != 0) {
                sudoku[x][y] = 0;
                i++; //dem so o trong
            }
        }

        return sudoku;
    }

    public void clearGrid(int[][] sudoku) {
        available.clear();

        for (int y = 0; y < Constant.MAX_ROW; y++) {
            for (int x = 0; x < Constant.MAX_COLUMN; x++) {
                sudoku[x][y] = Constant.AFTER_CLEAR_CELL_VALUE;
            }
        }

        for (int x = 0; x < Constant.MAX_CELL; x++) {
            available.add(new ArrayList<Integer>()); //mang available chua 81 arraylist moi arraay tu 1 -> 9 duoc khoi tao chi ra cac so co the dien vao o do
            for (int i = 1; i <= 9; i++) {
                available.get(x).add(i);
            }
        }
    }

    public int[][] getFlag(int[][] sudoku) {
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

    /**
     * Return true nếu có xung đột trong bảng Sudoku
     *
     * @param sudoku     mảng số nguyên hai chiều lưu giá trị của 81 ô
     * @param currentPos số thứ tự của 1 ô nằm trong khoảng 1 -> 81
     * @return true nếu có xung đột
     */

    public boolean checkOnConflict(int[][] sudoku, int currentPos, final int number) {
        int xPos = currentPos % Constant.MAX_COLUMN;
        int yPos = currentPos / Constant.MAX_ROW;

        if (checkHorizontalConflict(sudoku, xPos, yPos, number)
                || checkVerticalConflict(sudoku, xPos, yPos, number)
                || checkRegionConflict(sudoku, xPos, yPos, number)) {
            return true;
        }

        return false;
    }

    /**
     * Return true nếu có xung đột trong hàng ngang Sudoku
     *
     * @param sudoku mảng số nguyên hai chiều lưu giá trị của 81 ô
     * @param xPos   cột hiện tại
     * @param yPos   hàng hiện tại
     * @param number giá trị được xác định bởi hàng cột
     * @return true nếu có xung đột
     */
    private boolean checkHorizontalConflict(final int[][] sudoku, final int xPos, final int yPos, final int number) {
        for (int x = xPos - 1; x >= 0; x--) {
            if (number == sudoku[x][yPos]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true nếu có xung đột trong cột dọc Sudoku
     *
     * @param sudoku mảng số nguyên hai chiều lưu giá trị của 81 ô
     * @param xPos   cột hiện tại
     * @param yPos   hàng hiện tại
     * @param number giá trị được xác định bởi hàng cột
     * @return true nếu có xung đột
     */
    private boolean checkVerticalConflict(final int[][] sudoku, final int xPos, final int yPos, final int number) {
        for (int y = yPos - 1; y >= 0; y--) {
            if (number == sudoku[xPos][y]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true nếu có xung đột trong vùng 3x3
     *
     * @param sudoku mảng số nguyên hai chiều lưu giá trị của 81 ô
     * @param xPos   cột hiện tại
     * @param yPos   hàng hiện tại
     * @param number giá trị được xác định bởi hàng cột
     * @return true nếu có xung đột
     */
    private boolean checkRegionConflict(final int[][] sudoku, final int xPos, final int yPos, final int number) {
        int xRegion = xPos / Constant.MAX_COLUMN_REGION;
        int yRegion = yPos / Constant.MAX_ROW_REGION;
        for (int x = xRegion * Constant.MAX_COLUMN_REGION; x < xRegion * Constant.MAX_COLUMN_REGION + 3; x++) {
            for (int y = yRegion * Constant.MAX_ROW_REGION; y < yRegion * Constant.MAX_ROW_REGION + 3; y++) {
                if ((x != xPos || y != yPos) && number == sudoku[x][y]) {
                    return true;
                }
            }
        }
        return false;
    }


}
