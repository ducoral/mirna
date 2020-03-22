package org.mirna.core;

import java.math.BigInteger;

class IntegerConverter extends StringConverter {

    public IntegerConverter(Mapping mapping) {
        super(mapping);
    }

    @Override
    public String toText(Object value) {
        return super.toText(String.valueOf(value));
    }

    @Override
    public Object fromText(String text) {
        text = (String) super.fromText(text);
        Class<?> type = mapping.field().getType();
        if (type == Byte.TYPE)
            return Byte.parseByte(text);
        else if (type == Byte.class)
            return Byte.valueOf(text);
        else if (type == Short.TYPE)
            return Short.parseShort(text);
        else if (type == Short.class)
            return Short.valueOf(text);
        else if (type == Integer.TYPE)
            return Integer.parseInt(text);
        else if (type == Integer.class)
            return Integer.valueOf(text);
        else if (type == Long.TYPE)
            return Long.parseLong(text);
        else if (type == Long.class)
            return Long.valueOf(text);
        else return new BigInteger(text);
    }
}
