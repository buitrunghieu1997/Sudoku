package com.example.latrodectus.sudoku.model.Cell;

import android.content.Context;
import android.view.View;

//ParentCell khoi tao moi thu de cell extends va chi co ham xu ly
public class ParentCell extends View {
    private int value;
    private boolean isModifiable = true; //khong cho phep sua nhung o duoc khoi tao

    public ParentCell(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setModifiable(boolean modifiable) { //khong cho phep sua nhung o duoc khoi tao
        isModifiable = modifiable;
    }

        public boolean getModifiable() {
        return isModifiable;
    }

    public void setInitValue(int value) { //tao ham nay de khi khoi tao ban dau khong can kiem tra bien modifiable
        this.value = value;
    }

    public void setValue(int value) {
        if (isModifiable) {
            this.value = value;

            invalidate();
        }
    }

    public int getValue() {
        return value;
    }
}
