package org.mirna.converters;

import java.lang.reflect.Field;

public class CustomConverter extends StringConverter {

    public CustomConverter(Field field) {
        super(field);
    }
}
