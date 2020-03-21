package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.IntegerField;

import java.lang.reflect.Field;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerConverterTest {

    static class MirnaRecordCase {
        @IntegerField(position = 0, length = 10, fill = '0') Integer fieldCase1;
        @IntegerField(position = 1, length = 10, align = Align.LEFT) Integer fieldCase2;
    }

    IntegerConverter converter(String field) {
        return new IntegerConverter(new Mapping(getField(field)));
    }

    Field getField(String field) {
        try {
            return MirnaRecordCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(Strs.MSG_INTERNAL_ERROR);
        }
    }

    @Test
    void toText() {
        assertEquals("0000000123", converter("fieldCase1").toText(123));
        assertEquals("123       ", converter("fieldCase2").toText(123));
    }

    @Test
    void fromText() {
        assertEquals(BigInteger.valueOf(123), converter("fieldCase1").fromText("0000000123"));
        assertEquals(BigInteger.valueOf(123), converter("fieldCase2").fromText("123       "));
    }

}