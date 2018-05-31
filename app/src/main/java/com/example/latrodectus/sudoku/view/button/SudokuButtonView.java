package com.example.latrodectus.sudoku.view.button;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.activity.MainActivity;
import com.example.latrodectus.sudoku.activity.StartScreen;
import com.example.latrodectus.sudoku.logic.SudokuChecker;
import com.example.latrodectus.sudoku.model.Grid;
import com.example.latrodectus.sudoku.model.NumberButton;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.utils.Constant;
import com.example.latrodectus.sudoku.viewmodel.SudokuEngine;
import com.example.latrodectus.sudoku.viewmodel.SudokuGenerator;

//chua cac button trong grid
public class SudokuButtonView extends GridView {
    public SudokuButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SudokuButtonViewAdapter adapter = new SudokuButtonViewAdapter(context);

        setAdapter(adapter);
    }


    //inner class
    class SudokuButtonViewAdapter extends BaseAdapter implements View.OnClickListener {

        private Context context;

        public SudokuButtonViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click));
        }

        @Override
        public int getCount() {
            return Constant.BUTTON_COUNT; //9 so va nut xoa
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                v = inflater.inflate(R.layout.button, parent, false);

                final NumberButton button;
                button = (NumberButton) v;
                button.setId(position); //set id theo mang cac nut

                if (position < Constant.DEL_BUTTON_POSITION) {
                    button.setText(String.valueOf(position + 1));
                    button.setNumber(position + 1);
                    return button;
                } else {
                    switch (position) {
                        case 9: {
                            button.setText(R.string.del_button_label);
                            button.setNumber(Constant.EMPTY_CELL_VALUE); //khi number = 0 thi se k ve trong ham onDrawNum
                            break;
                        }
                        case 10: {
                            button.setText(R.string.check_button_label);
                            button.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int[][] sudoku = SudokuEngine.getInstance().getGrid().getArayNum();
                                    int[][] flag = getConflictPositionArray(sudoku);
                                    for (int y = 0; y < Constant.MAX_COLUMN; y++) {
                                        for (int x = 0; x < Constant.MAX_ROW; x++) {
                                            if (flag[x][y] == 0) {
                                                SudokuEngine.getInstance().setSelectedPosition(x, y);
                                                SudokuEngine.getInstance().setNumber(0);
                                                flag[x][y] = 1;
                                            }
                                        }
                                    }
                                }
                            });
                            break;
                        }
                        case 11: {
                            button.setText(R.string.hint_button_label);
                            button.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int[][] sudoku = SudokuEngine.getInstance().getGrid().getArayNum();
                                    int[] result = getHint(sudoku);
                                    SudokuEngine.getInstance().setSelectedPosition(result[1], result[2]);
                                    SudokuEngine.getInstance().setNumber(result[0]);
                                }
                            });
                            break;
                        }
                    }
                }
            }
            return v;
        }
    }

    private int[] getHint(int[][] sudoku) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku[x][y] == 0) {
                    for (int i = 1; i <= 9; i++) {
                        if (checkA(sudoku, x, y, i)) {
                            int[] result = {i, x, y};
                            return result;
                        }
                    }
                }
            }
        }
        int[] result = {0, -1, -1};
        return result;
    }

    private boolean checkA(int[][] sudoku, int xP, int yP, int num) {
        return (checkV(sudoku, xP, yP, num) && checkH(sudoku, xP, yP, num) && checkR(sudoku, xP, yP, num));
    }

    private boolean checkV(int[][] sudoku, int xP, int yP, int num) {
        for (int x = 0; x < 9; x++) {
            if (num == sudoku[x][yP]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkH(int[][] sudoku, int xP, int yP, int num) {
        for (int y = 0; y < 9; y++) {
            if (num == sudoku[xP][y]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkR(int[][] sudoku, int xP, int yP, int num) {
        int xR = xP / 3;
        int yR = yP / 3;
        for (int x = xR * 3; x < xR * 3 + 3; x++) {
            for (int y = yR * 3; y < yR * 3 + 3; y++) {
                if (num == sudoku[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int setFlag(int[][] sudoku, int xP, int yP, int num) {
        if (checkA(sudoku, xP, yP, num)) {
            return 1;
        }
        return 0;
    }

    public int[][] getConflictPositionArray(int[][] sudoku) {
        int[][] flag = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku[x][y] != 0) {
                    flag[x][y] = setFlag(sudoku, x, y, sudoku[x][y]);
                    System.out.println();
                } else {
                    flag[x][y] = 1;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(flag[i][j] + "");
            }
            System.out.println();
        }
        return flag;
    }
}
