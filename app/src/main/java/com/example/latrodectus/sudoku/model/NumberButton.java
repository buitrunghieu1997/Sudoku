package com.example.latrodectus.sudoku.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.latrodectus.sudoku.activity.MainActivity;
import com.example.latrodectus.sudoku.storage.SharedPreferencesObject;
import com.example.latrodectus.sudoku.viewmodel.SudokuEngine;

public class NumberButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {

    private int number;

    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SudokuEngine.getInstance().setNumber(number);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
