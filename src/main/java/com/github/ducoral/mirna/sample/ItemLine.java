package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDec;
import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

import java.math.BigDecimal;

@Line(identifier = "I")
public class ItemLine {

    @FieldStr(position = 1, length = 7, fill = '*')
    String fieldStr;

    @FieldInt(position = 2, length = 3, fill = '0')
    int fieldInt;

    @FieldDec(position = 3, length = 9, fill = '0', decimals = 4, separator = ',')
    BigDecimal fieldDec;

    public ItemLine() { }

    public ItemLine(String fieldStr, int fieldInt, BigDecimal fieldDec) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
        this.fieldDec = fieldDec;
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

    public BigDecimal getFieldDec() {
        return fieldDec;
    }

    public void setFieldDec(BigDecimal fieldDec) {
        this.fieldDec = fieldDec;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{fieldStr: " + fieldStr
                + ", fieldInt: " + fieldInt
                + ", fieldDec: " + fieldDec + "}";
    }
}
