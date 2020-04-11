package com.github.ducoral.mirna;

import java.math.BigDecimal;

class ConverterDec extends ConverterStr {

    public ConverterDec(Fielded fielded) {
        super(fielded);
    }

    @Override
    public String toText(Object value) {
        if (value == null)
            return super.toText("null");
        String txt = new BigDecimal(String.valueOf(value))
                .setScale(fielded.decimals(), BigDecimal.ROUND_DOWN)
                .toString();
        if (fielded.separator() == '\0')
            txt = txt.replace(".", "");
        else if (fielded.separator() != '.')
            txt = txt.replace(".", String.valueOf(fielded.separator()));
        return super.toText(txt);
    }

    @Override
    public Object fromText(String text) {
        setRemoveFill(fielded.fill() != '0' || fielded.align() == Align.LEFT || fielded.decimals() == 0);
        String value = (String) super.fromText(text);
        if (fielded.separator() == '\0')
            value = new StringBuilder(value)
                    .insert(value.length() - fielded.decimals(), '.')
                    .toString();
        else if (fielded.separator() != '.')
            value = value.replace(fielded.separator(), '.');
        Class<?> type = fielded.field().getType();
        if (type == Float.TYPE)
            return Float.parseFloat(value);
        else if (type == Float.class)
            return Float.valueOf(value);
        else if (type == Double.TYPE)
            return Double.parseDouble(value);
        else if (type == Double.class)
            return Double.valueOf(value);
        else
            return new BigDecimal(value);
    }
}
