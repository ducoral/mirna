package org.mirna.converters;

public interface Converter {

    String toStr(Object value);

    Object toObj(String value);
}
