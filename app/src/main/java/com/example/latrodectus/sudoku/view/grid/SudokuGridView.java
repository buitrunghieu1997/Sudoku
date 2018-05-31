package com.example.latrodectus.sudoku.view.grid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.latrodectus.sudoku.R;
import com.example.latrodectus.sudoku.utils.Constant;
import com.example.latrodectus.sudoku.viewmodel.SudokuEngine;

public class SudokuGridView extends GridView {
    private final Context context;

    public SudokuGridView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        SudokuGridViewAdapter gridViewAdapter = new SudokuGridViewAdapter(context);

        setAdapter(gridViewAdapter);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x = position % Constant.MAX_ROW;
                int y = position / Constant.MAX_COLUMN;

                SudokuEngine.getInstance().setSelectedPosition(x, y);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    //inner class - khi khong can viet tach biet mot class ra ben ngoai, tranh su can thiep k can thiet, cho phep goi instance ben ngoai class
    class SudokuGridViewAdapter extends BaseAdapter {
        private Context context;

        public SudokuGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return Constant.MAX_CELL;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return Constant.DEFAULT_ITEM_ID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //tra ve 1 doi tuong view (o day la cell) tai vi tri position
            View view = SudokuEngine.getInstance().getGrid().getElement(position);
            for (int y = 0; y < Constant.MAX_ROW_REGION; y++) {
                for (int x = 0; x < Constant.MAX_COLUMN_REGION; x++) {
                    if ((x + y) % 2 == 0) {
                        for (int i = y * 3; i < y * 3 + 3; i++) {
                            for (int j = x * 3; j < x * 3 + 3; j++) {
                                if ( position == (i * 9 + j)) {
                                    view.setBackgroundColor(Color.argb(100, 255, 255, 255));
                                }
                            }
                        }
                    }
                }
            }

            /*if ((position >= 0 && position < 3) || (position >= 6 && position < 12) ||
                    (position >= 15 && position < 21) || (position >= 24 && position < 27) ||
                    (position >= 30 && position < 33) || (position >= 39 && position < 42) ||
                    (position >= 48 && position < 51) || (position >= 54 && position < 57) ||
                    (position >= 60 && position < 66) || (position >= 69 && position < 66) ||
                    (position >= 69 && position < 75) || (position >= 78 && position < 81)) {
                view.setBackgroundColor(Color.argb(100, 255, 255, 255));

            }*/
            return view;
        }
    }
}
