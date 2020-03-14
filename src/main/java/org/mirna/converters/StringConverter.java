package org.mirna.converters;

import org.mirna.Descriptor;

import java.lang.reflect.Field;

public abstract class StringConverter implements Converter {

    private final Field field;

    public StringConverter(Field field) {
        this.field = field;
    }

    @Override
    public String toStr(Object value) {
        return String.valueOf(value);
    }

    @Override
    public Object toObj(String value) {
        return value;
    }

    protected Descriptor descriptor() {
        return Descriptor.create(field);
    }
}
