package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.StringField;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringConverterTest {

    static class MirnaRecordCase {
        @StringField(position = 0, length = 10, fill = '*')
        String fieldCase1;

        @StringField(position = 0, length = 10, fill = '+', align = Align.RIGHT)
        String fieldCase2;

        @StringField(position = 0, length = 1)
        char fieldCase3;
    }

    StringConverter converter(String field) {
        return new StringConverter(new Mapping(getField(field)));
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