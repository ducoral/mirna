package org.mirna.converters;

public interface Converter {

    String toText(Object value);

    Object fromText(String text);
}
