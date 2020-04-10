package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDtm;
import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

import java.util.Date;

@Line(identifier = "F")
public class FooterLine {

    @FieldDtm(position = 1)
    private Date fieldDtm;

    @FieldStr(position = 2, length = 6)
    private String fieldStr;

    @FieldInt(position = 3, length = 5, fill = '0')
    private int fieldInt;

    public FooterLine() { }

    public FooterLine(Date fieldDtm, String fieldStr, int fieldInt) {
        this.fieldDtm = fieldDtm;
        this.fieldStr = fieldStr;
        this.fieldInt = fieldInt;
    }

    public Date getFieldDtm() {
        return fieldDtm;
    }

    public void setFieldDtm(Date fieldDtm) {
        this.fieldDtm = fieldDtm;
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
}
