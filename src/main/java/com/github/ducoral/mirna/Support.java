package com.github.ducoral.mirna;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

enum Support {

    FIELD(Line.class, FieldStr.class, FieldInt.class, FieldDec.class, FieldDtm.class, FieldCtm.class),

    ITEM(Header.class, Footer.class, Item.class),

    HEADER_FOOTER(Header.class, Footer.class);

    final Class<? extends Annotation>[] array;

    Stream<Class<? extends Annotation>> stream() {
        return Stream.of(array);
    }

    @SafeVarargs
    Support(Class<? extends Annotation>... support) {
        array = support;
    }
}