package org.mirna.converters;

import org.junit.jupiter.api.Test;
import org.mirna.Align;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.IntegerField;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerConverterTest {

    private static class MirnaRecordCase {
        @IntegerField(position = 0, length = 10, fill = '0') Integer fieldCase1;
        @IntegerField(position = 1, length = 10, align = Align.LEFT) Integer fieldCase2;
    }

    private static IntegerConverter converter(String field) {
        return new IntegerConverter(getField(field));
    }

    private static Field getField(String field) {
        try {
            return MirnaRecordCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(Strs.INTERNAL_ERROR);
        }
    }

    @Test
    void toText() {
        assertEquals("0000000123", converter("fieldCase1").toText(123));
        assertEquals("123       ", converter("fieldCase2").toText(123));
        assertThrows(MirnaException.class, () -> converter("fieldCase1").toText(new Date()));
    }

    @Test
    void fromText() {
        assertEquals(BigInteger.valueOf(123), converter("fieldCase1").fromText("0000000123"));
        assertEquals(BigInteger.valueOf(123), converter("fieldCase2").fromText("123       "));
    }

}