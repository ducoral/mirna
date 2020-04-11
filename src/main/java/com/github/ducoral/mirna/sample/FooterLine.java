package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Line(identifier = "F")
public class FooterLine {

    @FieldDtm(position = 1)
    private Date fieldDtm;

    @FieldCtm(position = 2, length = 11, align = Align.RIGHT, converter = ColorConverter.class)
    private Color fieldCtm;

    public FooterLine() { }

    public FooterLine(Date fieldDtm, Color fieldCtm) {
        this.fieldDtm = fieldDtm;
        this.fieldCtm = fieldCtm;
    }

    public Date getFieldDtm() {
        return fieldDtm;
    }

    public void setFieldDtm(Date fieldDtm) {
        this.fieldDtm = fieldDtm;
    }

    public Color getFieldCtm() {
        return fieldCtm;
    }

    public void setFieldCtm(Color fieldCtm) {
        this.fieldCtm = fieldCtm;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{fieldDtm: " + new SimpleDateFormat("dd/MM/yyyy").format(fieldDtm)
                + ", fieldCtm: " + fieldCtm.toString()
                + "}";
    }
}
