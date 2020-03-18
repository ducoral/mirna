package org.mirna.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateTimeConverter extends StringConverter {

    public DateTimeConverter(Mapping mapping) {
        super(mapping);
        mapping.put(Mapping.LENGTH, mapping.format().length());
    }

    @Override
    public String toText(Object value) {
        return super.toText(new SimpleDateFormat(mapping.format()).format((Date)value));
    }

    @Override
    public Object fromText(String text) {
        try {
            return new SimpleDateFormat(mapping.format()).parse((String) super.fromText(text));
        } catch (ParseException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }
}
