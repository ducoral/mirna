package org.mirna.converters;

public interface Converter {

    String toStr(Object value);

    Object fromStr(String value);
}
