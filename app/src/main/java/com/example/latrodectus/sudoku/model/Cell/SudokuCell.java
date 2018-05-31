package com.example.latrodectus.sudoku.model.Cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class SudokuCell extends ParentCell {
    private Paint paint;

    public SudokuCell(Context context) {
        super(context);

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNumber(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE); //chi ke ma k to mau
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void drawNumber(Canvas canvas) {
        if (!getModifiable()) {
            paint.setColor(Color.rgb(255, 200, 215));
        } else {
            paint.setColor(Color.rgb(255, 255, 255));
        }
            paint.setTextSize(80);
        paint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(getValue()), 0, String.valueOf(getValue()).length(), bounds); //Lấy biên của số cần vẽ
        if (getValue() != 0) {
            //lay trung diem cua bound lam diem can chinh de ve
            canvas.drawText(String.valueOf(getValue()),
                    (getWidth() - bounds.width()) / 2,
                    (getHeight() + bounds.height()) / 2, paint);
        }
    }
}
