package org.mirna;

import org.junit.jupiter.api.Test;

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

    @Line(identifier = "ident")
    static class LineCase {

        @StringField(position = 1, length = 10)
        private String fieldCase1;

        @IntegerField(position = 2, length = 20)
        private Integer fieldCase2;

        @DecimalField(position = 3, length = 30)
        private Double fieldCase3;

        @DateTimeField(position = 4)
        private Date fieldCase4;

        @CustomField(position = 5, length = 50, converter = CustomConverterCase.class)
        private Object fieldCase5;
    }

    static class LineInvalidCase {

        @StringField(position = 0, length = 0)
        private Integer fieldCase1;

        @IntegerField(position = 0, length = 0)
        private Date fieldCase2;

        @DecimalField(position = 0, length = 0)
        private Integer fieldCase3;

        @DateTimeField(position = 0)
        private String fieldCase4;
    }

    Field validCaseField(String name) throws NoSuchFieldException {
        return LineCase.class.getDeclaredField(name);
    }

    Field invalidCaseField(String name) throws NoSuchFieldException {
        return LineInvalidCase.class.getDeclaredField(name);
    }

    @Test
    void converters() throws NoSuchFieldException {
        assertEquals(StringConverter.class, new Mapping(validCaseField("fieldCase1")).converter().getClass());
        assertEquals(IntegerConverter.class, new Mapping(validCaseField("fieldCase2")).converter().getClass());
        assertEquals(DecimalConverter.class, new Mapping(validCaseField("fieldCase3")).converter().getClass());
        assertEquals(DateTimeConverter.class, new Mapping(validCaseField("fieldCase4")).converter().getClass());
        assertEquals(CustomConverter.class, new Mapping(validCaseField("fieldCase5")).converter().getClass());
    }

    @Test
    void isTypeSupported() throws NoSuchFieldException {
        assertTrue(Mapping.isTypeSupported(validCaseField("fieldCase1")));
        assertTrue(Mapping.isTypeSupported(validCaseField("fieldCase2")));
        assertTrue(Mapping.isTypeSupported(validCaseField("fieldCase3")));
        assertTrue(Mapping.isTypeSupported(validCaseField("fieldCase4")));
        assertTrue(Mapping.isTypeSupported(validCaseField("fieldCase5")));
        assertFalse(Mapping.isTypeSupported(invalidCaseField("fieldCase1")));
        assertFalse(Mapping.isTypeSupported(invalidCaseField("fieldCase2")));
        assertFalse(Mapping.isTypeSupported(invalidCaseField("fieldCase3")));
        assertFalse(Mapping.isTypeSupported(invalidCaseField("fieldCase4")));
    }

    @Test
    void LineCase() {
        Mapping mapping = new Mapping(LineCase.class);
        assertEquals("ident", mapping.identifier());
        assertEquals(0, mapping.position());
        assertEquals(5, mapping.length());
    }

    @Test
    void stringFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(LineCase.class.getDeclaredField("fieldCase1"));
        assertEquals("fieldCase1", mapping.field().getName());
        assertEquals(1, mapping.position());
        assertEquals(10, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.LEFT, mapping.align());
    }

    @Test
    void integerFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(LineCase.class.getDeclaredField("fieldCase2"));
        assertEquals("fieldCase2", mapping.field().getName());
        assertEquals(2, mapping.position());
        assertEquals(20, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals(Align.RIGHT, mapping.align());
    }

    @Test
    void decimalFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(LineCase.class.getDeclaredField("fieldCase3"));
        assertEquals("fieldCase3", mapping.field().getName());
        assertEquals(3, mapping.position());
        assertEquals(30, mapping.length());
        assertEquals(' ', mapping.fill());
        assertEquals('\0', mapping.separator());
        assertEquals(2, mapping.decimals());
        assertEquals(Align.RIGHT, mapping.align());
    }

    @Test
    void dateTimeFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(LineCase.class.getDeclaredField("fieldCase4"));
        assertEquals("fieldCase4", mapping.field().getName());
        assertEquals(4, mapping.position());
        assertEquals("ddMMyyyy", mapping.format());
        assertEquals(8, mapping.length());
    }

    @Test
    void customFieldCase() throws NoSuchFieldException {
        Mapping mapping = new Mapping(LineCase.class.getDeclaredField("fieldCase5"));
        assertEquals("fieldCase5", mapping.field().getName());
        assertEquals(5, mapping.position());
        assertEquals(50, mapping.length());
        assertEquals(CustomConverter.class, mapping.converter().getClass());
        assertEquals(CustomConverterCase.class, ((CustomConverter) mapping.converter()).custom().getClass());
    }
}