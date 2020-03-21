package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MappingTest {

    static class CustomConverterCase implements Converter {

        @Override
        public String toText(Object value) {
            return String.valueOf(value);
        }

        @Override
        public Object fromText(String text) {
            return text;
        }
    }

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

        @CustomField(position = 5, length = 50, converter = CustomConverterCase.class)
        private Object ctmField;
    }

    static class MirnaRecordInvalidCase {

        @StringField(position = 0, length = 0)
        private Integer fieldCase1;

        @IntegerField(position = 0, length = 0)
        private Date fieldCase2;

        @DecimalField(position = 0, length = 0)
        private Integer fieldCase3;

        @DateTimeField(position = 0)
        private String fieldCase4;
    }

    Field validField(String name) throws NoSuchFieldException {
        return MirnaRecodCase.class.getDeclaredField(name);
    }

    Field invalidField(String name) throws NoSuchFieldException {
        return MirnaRecordInvalidCase.class.getDeclaredField(name);
    }

    @Test
    void isTypeSupported() throws NoSuchFieldException {
        assertTrue(Mapping.isTypeSupported(validField("strField")));
        assertTrue(Mapping.isTypeSupported(validField("intField")));
        assertTrue(Mapping.isTypeSupported(validField("decField")));
        assertTrue(Mapping.isTypeSupported(validField("dtmField")));
        assertTrue(Mapping.isTypeSupported(validField("ctmField")));
        assertFalse(Mapping.isTypeSupported(invalidField("fieldCase1")));
        assertFalse(Mapping.isTypeSupported(invalidField("fieldCase2")));
        assertFalse(Mapping.isTypeSupported(invalidField("fieldCase3")));
        assertFalse(Mapping.isTypeSupported(invalidField("fieldCase4")));
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
        assertEquals("strField", mapping.field().getName());
        assertEquals(1, mapping.position());
        assertEquals(10, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.LEFT, mapping.align());
    }

    @Test
    void integerFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("intField"));
        assertEquals("intField", mapping.field().getName());
        assertEquals(2, mapping.position());
        assertEquals(20, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.RIGHT, mapping.align());
    }

    @Test
    void decimalFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("decField"));
        assertEquals("decField", mapping.field().getName());
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
        assertEquals("dtmField", mapping.field().getName());
        assertEquals(4, mapping.position());
        assertEquals("ddMMyyyy", mapping.format());
    }

    @Test
    void customFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(MirnaRecodCase.class.getDeclaredField("ctmField"));
        assertEquals("ctmField", mapping.field().getName());
        assertEquals(5, mapping.position());
        assertEquals(50, mapping.length());
        assertEquals(CustomConverterCase.class, mapping.converter().getClass());
    }
}