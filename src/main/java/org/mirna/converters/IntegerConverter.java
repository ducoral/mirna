package org.mirna.converters;

import org.mirna.Mapping;

import java.math.BigInteger;

public class IntegerConverter extends StringConverter {

    public IntegerConverter(Mapping mapping) {
        super(mapping);
    }

    @Override
    public String toText(Object value) {
        return super.toText(String.valueOf(value));
    }

    @Override
    public Object fromText(String text) {
        return new BigInteger((String)super.fromText(text));
    }
}
