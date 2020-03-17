package org.mirna.converters;

import org.junit.jupiter.api.Test;
import org.mirna.Align;
import org.mirna.Mapping;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.StringField;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringConverterTest {

    private static class MirnaRecordCase {
        @StringField(position = 0, length = 10, fill = '*') String fieldCase1;
        @StringField(position = 0, length = 10, fill = '+', align = Align.RIGHT) String fieldCase2;
    }

    private static StringConverter converter(String field) {
        return new StringConverter(new Mapping(getField(field)));
    }

    private static Field getField(String field) {
        try {
            return MirnaRecordCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(Strs.MSG_INTERNAL_ERROR);
        }
    }

    @Test
    void toText() {
        assertEquals("abcde*****", converter("fieldCase1").toText("abcde"));
        assertEquals("a*********", converter("fieldCase1").toText("a"));
        assertEquals("+++++abcde", converter("fieldCase2").toText("abcde"));
        assertEquals("+++++++++a", converter("fieldCase2").toText("a"));
    }

    @Test
    void fromText() {
        assertEquals("abcde", converter("fieldCase1").fromText("abcde*****"));
        assertEquals("a", converter("fieldCase1").fromText("a*********"));
        assertEquals("*", converter("fieldCase1").fromText("**********"));
        assertEquals("abcde", converter("fieldCase2").fromText("+++++abcde"));
        assertEquals("a", converter("fieldCase2").fromText("+++++++++a"));
        assertEquals("+", converter("fieldCase2").fromText("++++++++++"));
    }
}