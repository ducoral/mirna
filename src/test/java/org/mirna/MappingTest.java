package org.mirna;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;
import org.mirna.converters.StringConverter;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingTest {

    @MirnaRecord(identifier = "ident")
    static class MirnaRecodCase {

        @StringField(position = 1, length = 10)
        private String strField;

        @IntegerField(position = 2, length = 20)
        private Integer intField;

        @DecimalField(position = 3, length = 30)
        private Double decField;

        @DateTimeField(position = 4)
        private Date dtmField;

        @CustomField(position = 5, length = 50, converter = StringConverter.class)
        private Object ctmField;
    }

    @Test
    void mirnaRecordCase() {
        Mapping mapping = new Mapping(MirnaRecodCase.class);
        assertEquals("ident", mapping.identifier());
        assertEquals(0, mapping.position());
    }

    @Test
    void stringFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("strField"));
        assertEquals(1, mapping.position());
        assertEquals(10, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.LEFT, mapping.align());
    }

    @Test
    void integerFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("intField"));
        assertEquals(2, mapping.position());
        assertEquals(20, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.RIGHT, mapping.align());
    }

    @Test
    void decimalFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("decField"));
        assertEquals(3, mapping.position());
        assertEquals(30, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals('\0', mapping.separator());
        assertEquals(2, mapping.decimals());
        assertEquals(Align.RIGHT, mapping.align());
    }

    @Test
    void dateTimeFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("dtmField"));
        assertEquals(4, mapping.position());
        assertEquals("ddMMyyyy", mapping.format());
    }

    @Test
    void customFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("ctmField"));
        assertEquals(5, mapping.position());
        assertEquals(50, mapping.length());
        assertEquals(StringConverter.class, mapping.converter());
    }
}