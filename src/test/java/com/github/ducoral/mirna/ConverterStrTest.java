package com.github.ducoral.mirna;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterStrTest {

    static class LineCase {
        @FieldStr(position = 0, length = 10, fill = '*')
        String fieldCase1;

        @FieldStr(position = 0, length = 10, fill = '+', align = Align.RIGHT)
        String fieldCase2;

        @FieldStr(position = 0, length = 1)
        char fieldCase3;
    }

    ConverterStr converter(String field) {
        return new ConverterStr(new Fielded(getField(field)));
    }

    Field getField(String field) {
        try {
            return LineCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new Oops(Strs.MSG_INTERNAL_ERROR);
        }
    }

    @Test
    void toText() {
        assertEquals("abcde*****", converter("fieldCase1").toText("abcde"));
        assertEquals("a*********", converter("fieldCase1").toText("a"));
        assertEquals("+++++abcde", converter("fieldCase2").toText("abcde"));
        assertEquals("+++++++++a", converter("fieldCase2").toText("a"));
        assertEquals("a", converter("fieldCase3").toText('a'));
    }

    @Test
    void fromText() {
        assertEquals("abcde", converter("fieldCase1").fromText("abcde*****"));
        assertEquals("a", converter("fieldCase1").fromText("a*********"));
        assertEquals("*", converter("fieldCase1").fromText("**********"));
        assertEquals("abcde", converter("fieldCase2").fromText("+++++abcde"));
        assertEquals("a", converter("fieldCase2").fromText("+++++++++a"));
        assertEquals("+", converter("fieldCase2").fromText("++++++++++"));
        assertEquals('a', converter("fieldCase3").fromText("a"));
    }
}