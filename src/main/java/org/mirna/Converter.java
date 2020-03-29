package org.mirna;

public interface Converter {

    String toText(Object value);

    Object fromText(String text);
}
