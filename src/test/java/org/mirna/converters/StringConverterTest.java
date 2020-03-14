package org.mirna.converters;

import org.junit.jupiter.api.Test;
import org.mirna.Align;
import org.mirna.Descriptor;
import org.mirna.annotations.StringField;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringConverterTest {

    private static class MirnaRecordCase {
        @StringField(position = 0, length = 10, fill = '*') String fieldCase1;
        @StringField(position = 0, length = 10, fill = '+', align = Align.RIGHT) String fieldCase2;
    }

    private static StringConverter converter(String field) throws NoSuchFieldException {
        return new StringConverter(MirnaRecordCase.class.getDeclaredField(field));
    }

    @Test
    void toStr() throws NoSuchFieldException {
        assertEquals("abcde*****", converter("fieldCase1").toStr("abcde"));
        assertEquals("a*********", converter("fieldCase1").toStr("a"));
        assertEquals("+++++abcde", converter("fieldCase2").toStr("abcde"));
        assertEquals("+++++++++a", converter("fieldCase2").toStr("a"));
    }

    @Test
    void fromStr() throws NoSuchFieldException {
        assertEquals("abcde", converter("fieldCase1").fromStr("abcde*****"));
        assertEquals("a", converter("fieldCase1").fromStr("a*********"));
        assertEquals("*", converter("fieldCase1").fromStr("**********"));
        assertEquals("abcde", converter("fieldCase2").fromStr("+++++abcde"));
        assertEquals("a", converter("fieldCase2").fromStr("+++++++++a"));
        assertEquals("+", converter("fieldCase2").fromStr("++++++++++"));
    }

    @Test
    void descriptor() throws NoSuchFieldException {
        Descriptor expected = Descriptor.create(MirnaRecordCase.class.getDeclaredField("fieldCase1"));
        Descriptor actual = converter("fieldCase1").descriptor();
        assertEquals(expected.position, actual.position);
        assertEquals(expected.length, actual.length);
        assertEquals(expected.fill, actual.fill);
        assertEquals(expected.align, actual.align);

        expected = Descriptor.create(MirnaRecordCase.class.getDeclaredField("fieldCase2"));
        actual = converter("fieldCase2").descriptor();
        assertEquals(expected.position, actual.position);
        assertEquals(expected.length, actual.length);
        assertEquals(expected.fill, actual.fill);
        assertEquals(expected.align, actual.align);
    }
}