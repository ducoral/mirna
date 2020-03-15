package org.mirna.converters;

import org.mirna.Descriptor;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.IntegerField;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class IntegerConverter extends StringConverter {

    public IntegerConverter(Field field) {
        super(field);
    }

    @Override
    public String toText(Object value) {
        if (!Descriptor.isValid(value, IntegerField.class))
            throw new MirnaException(Strs.MSG_INVALID_PARAMETER, value);
        return super.toText(String.valueOf(value));
    }

    @Override
    public Object fromText(String text) {
        return new BigInteger((String)super.fromText(text));
    }
}
