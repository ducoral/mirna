package org.mirna.converters;

import org.mirna.Descriptor;

import java.lang.reflect.Field;

public abstract class AbstractConverter implements Converter {

    private final Field field;

    public AbstractConverter(Field field) {
        this.field = field;
    }

    @Override
    public String toStr(Object value) {
        return null;
    }

    @Override
    public Object toObj(String value) {
        return null;
    }

    protected Descriptor descriptor() {
        return Descriptor.create(field);
    }
}
