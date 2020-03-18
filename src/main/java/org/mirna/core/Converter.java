package org.mirna.core;

public interface Converter {

    String toText(Object value);

    Object fromText(String text);
}
