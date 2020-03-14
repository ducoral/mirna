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
    void toText() throws NoSuchFieldException {
        assertEquals("abcde*****", converter("fieldCase1").toText("abcde"));
        assertEquals("a*********", converter("fieldCase1").toText("a"));
        assertEquals("+++++abcde", converter("fieldCase2").toText("abcde"));
        assertEquals("+++++++++a", converter("fieldCase2").toText("a"));
    }

    @Test
    void fromText() throws NoSuchFieldException {
        assertEquals("abcde", converter("fieldCase1").fromText("abcde*****"));
        assertEquals("a", converter("fieldCase1").fromText("a*********"));
        assertEquals("*", converter("fieldCase1").fromText("**********"));
        assertEquals("abcde", converter("fieldCase2").fromText("+++++abcde"));
        assertEquals("a", converter("fieldCase2").fromText("+++++++++a"));
        assertEquals("+", converter("fieldCase2").fromText("++++++++++"));
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