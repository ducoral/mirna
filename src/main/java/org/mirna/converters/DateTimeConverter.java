package org.mirna.converters;

import org.mirna.Descriptor;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.DateTimeField;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter extends StringConverter {

    public DateTimeConverter(Field field) {
        super(field);
    }

    @Override
    public String toText(Object value) {
        if (!Descriptor.isValid(value, DateTimeField.class))
            throw new MirnaException(Strs.MSG_INVALID_PARAMETER, value);
        return super.toText(new SimpleDateFormat(descriptor().format).format((Date)value));
    }

    @Override
    public Object fromText(String text) {
        try {
            return new SimpleDateFormat(descriptor().format).parse((String)super.fromText(text));
        } catch (ParseException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }
}
