package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDec;
import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

import java.math.BigDecimal;

@Line(identifier = "D")
public class DetailLine {

    @FieldStr(position = 1, length = 4)
    private String fieldStr;

    @FieldInt(position = 2, length = 5, fill = '0')
    private int fieldInt;

    @FieldDec(position = 3, length = 10, fill = '0')
    private BigDecimal fieldDec;

    public DetailLine() { }

    public DetailLine(String fieldStr, int fieldInt, BigDecimal fieldDec) {
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
}
