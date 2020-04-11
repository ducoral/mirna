package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDec;
import com.github.ducoral.mirna.FieldDtm;
import com.github.ducoral.mirna.Item;
import com.github.ducoral.mirna.Line;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Line(identifier = "A")
public class AnotherLine {

    @FieldDtm(position = 1, format = "yyyyMMdd")
    private Date fieldDtm;

    @FieldDec(position = 2, length = 11, separator = '.', decimals = 4)
    private BigDecimal fieldDec;

    @Item
    private ItemLine item;

    public AnotherLine() { }

    public AnotherLine(Date fieldDtm, BigDecimal fieldDec, ItemLine item) {
        this.fieldDtm = fieldDtm;
        this.fieldDec = fieldDec;
        this.item = item;
    }

    public Date getFieldDtm() {
        return fieldDtm;
    }

    public void setFieldDtm(Date fieldDtm) {
        this.fieldDtm = fieldDtm;
    }

    public BigDecimal getFieldDec() {
        return fieldDec;
    }

    public ItemLine getItem() {
        return item;
    }

    public void setItem(ItemLine item) {
        this.item = item;
    }

    public void setFieldDec(BigDecimal fieldDec) {
        this.fieldDec = fieldDec;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
            + "{fieldDtm: " + new SimpleDateFormat("dd/MM/yyyy").format(fieldDtm)
            + ", fieldDec: " + fieldDec
            + ", item: " + item.toString() + "}";
    }
}
