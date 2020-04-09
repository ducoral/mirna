package com.github.ducoral.mirna;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.stream.Stream;

enum Primitive {

    STRING(Character.TYPE, Character.class, String.class),

    INTEGER(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
            Byte.class, Short.class, Integer.class, Long.class, BigInteger.class),

    DECIMAL(Float.TYPE, Float.class, Double.TYPE, Double.class, BigDecimal.class),

    DATETIME(Date.class);

    Stream<Class<? extends Serializable>> stream() {
        return Stream.of(array);
    }

    final Class<? extends Serializable>[] array;

    @SafeVarargs
    Primitive(Class<? extends Serializable>... primitive) {
        array = primitive;
    }
}
