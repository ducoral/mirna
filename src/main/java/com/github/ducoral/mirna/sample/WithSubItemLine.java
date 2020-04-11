package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Item;
import com.github.ducoral.mirna.Line;

import java.util.List;

@Line(identifier = "S")
public class WithSubItemLine {

    @FieldStr(position = 1, length = 15)
    private String fieldStr;

    @FieldInt(position = 2, length = 4)
    private int fieldInt;

    @Item
    private List<DetailLine> details;

    public WithSubItemLine() { }

    public WithSubItemLine(String fieldStr, int fieldInt, List<DetailLine> details) {
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
        this.details = details;
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

    public List<DetailLine> getDetails() {
        return details;
    }

    public void setDetails(List<DetailLine> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{fieldStr: " + fieldStr
                + ", fieldInt: " + fieldInt
                + ", details: " + details.toString() + "}";
    }
}
