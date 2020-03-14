package org.mirna.converters;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class IntegerConverter extends StringConverter {

    public IntegerConverter(Field field) {
        super(field);
    }

    @Override
    public Object fromText(String text) {
        return new BigInteger((String) super.fromText(text));
    }
}
