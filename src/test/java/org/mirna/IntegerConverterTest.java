package org.mirna;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerConverterTest {

    static class MirnaRecordCase {
        @IntegerField(position = 0, length = 10, fill = '0')
        byte fieldCase1;

        @IntegerField(position = 1, length = 10, align = Align.LEFT)
        Byte fieldCase2;

        @IntegerField(position = 0, length = 10, fill = '0')
        short fieldCase3;

        @IntegerField(position = 1, length = 10, align = Align.LEFT)
        Short fieldCase4;

        @IntegerField(position = 0, length = 10, fill = '0')
        int fieldCase5;

        @IntegerField(position = 1, length = 10, align = Align.LEFT)
        Integer fieldCase6;

        @IntegerField(position = 0, length = 10, fill = '0')
        long fieldCase7;

        @IntegerField(position = 1, length = 10, align = Align.LEFT)
        Long fieldCase8;

        @IntegerField(position = 0, length = 10, fill = '0')
        BigInteger fieldCase9;
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
        assertEquals("0000000123", converter("fieldCase1").toText((byte) 123));
        assertEquals("123       ", converter("fieldCase2").toText(Byte.valueOf("123")));
        assertEquals("0000000123", converter("fieldCase3").toText((short) 123));
        assertEquals("123       ", converter("fieldCase4").toText(Short.valueOf("123")));
        assertEquals("0000000123", converter("fieldCase5").toText(123));
        assertEquals("123       ", converter("fieldCase6").toText(Integer.valueOf("123")));
        assertEquals("0000000123", converter("fieldCase7").toText((long) 123));
        assertEquals("123       ", converter("fieldCase8").toText(Long.valueOf("123")));
        assertEquals("0000000123", converter("fieldCase9").toText(new BigInteger("123")));
    }

    @Test
    void fromText() {
        assertEquals((byte) 123, converter("fieldCase1").fromText("0000000123"));
        assertEquals(Byte.valueOf("123"), converter("fieldCase2").fromText("123       "));
        assertEquals((short) 123, converter("fieldCase3").fromText("0000000123"));
        assertEquals(Short.valueOf("123"), converter("fieldCase4").fromText("123       "));
        assertEquals(123, converter("fieldCase5").fromText("0000000123"));
        assertEquals(Integer.valueOf("123"), converter("fieldCase6").fromText("123       "));
        assertEquals((long) 123, converter("fieldCase7").fromText("0000000123"));
        assertEquals(Long.valueOf("123"), converter("fieldCase8").fromText("123       "));
        assertEquals(new BigInteger("123"), converter("fieldCase9").fromText("0000000123"));
    }
}