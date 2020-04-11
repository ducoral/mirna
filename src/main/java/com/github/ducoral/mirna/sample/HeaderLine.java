package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

@Line(identifier = "H")
public class HeaderLine {

    @FieldStr(position = 1, length = 14)
    private String fieldStr;

    @FieldInt(position = 2, length = 5, fill = '0')
    private int fieldInt;

    public HeaderLine() { }

    public HeaderLine(String fieldStr, int fieldInt) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
    }

    public String getFieldStr() {
        return fieldStr;
    }

    public void setFieldStr(String fieldStr) {
        this.fieldStr = fieldStr;
    }

    public int getFieldInt() {
        return fieldInt;
    }

    public void setFieldInt(int fieldInt) {
        this.fieldInt = fieldInt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{fieldStr:" + fieldStr
                + ", fieldInt:" + fieldInt
                + "}";
    }
}
