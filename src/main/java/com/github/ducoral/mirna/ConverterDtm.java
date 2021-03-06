package com.github.ducoral.mirna;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class ConverterDtm extends ConverterStr {

    public ConverterDtm(Fielded fielded) {
        super(fielded);
        fielded.put(Fielded.LENGTH, fielded.format().length());
    }

    @Override
    public String toText(Object value) {
        String text = value == null
                ? "null"
                : new SimpleDateFormat(fielded.format()).format((Date) value);
        return super.toText(text);
    }

    @Override
    public Object fromText(String text) {
        try {
            return new SimpleDateFormat(fielded.format()).parse((String) super.fromText(text));
        } catch (ParseException e) {
            throw new Oops(e.getMessage(), e);
        }
    }
}
