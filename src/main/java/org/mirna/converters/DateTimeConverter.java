package org.mirna.converters;

import org.mirna.Descriptor;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.DateTimeField;

import java.lang.reflect.Field;

public class DateTimeConverter extends StringConverter {

    public DateTimeConverter(Field field) {
        super(field);
    }

    @Override
    public String toText(Object value) {
        if (!Descriptor.isValid(value, DateTimeField.class))
            throw new MirnaException(Strs.INVALID_PARAMETER, value);
        return super.toText(value);
    }

    @Override
    public Object fromText(String text) {
        return super.fromText(text);
    }
}
