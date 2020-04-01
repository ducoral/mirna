package org.mirna;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FieldedTest {

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

        @FieldStr(position = 1, length = 10)
        private String fieldCase1;

        @FieldInt(position = 2, length = 20)
        private Integer fieldCase2;

        @FieldDec(position = 3, length = 30)
        private Double fieldCase3;

        @FieldDtm(position = 4)
        private Date fieldCase4;

        @FieldCtm(position = 5, length = 50, converter = CustomConverterCase.class)
        private Object fieldCase5;
    }

    static class LineInvalidCase {

        @FieldStr(position = 0, length = 0)
        private Integer fieldCase1;

        @FieldInt(position = 0, length = 0)
        private Date fieldCase2;

        @FieldDec(position = 0, length = 0)
        private Integer fieldCase3;

        @FieldDtm(position = 0)
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
        assertEquals(ConverterStr.class, new Fielded(validCaseField("fieldCase1")).converter().getClass());
        assertEquals(ConverterInt.class, new Fielded(validCaseField("fieldCase2")).converter().getClass());
        assertEquals(ConverterDec.class, new Fielded(validCaseField("fieldCase3")).converter().getClass());
        assertEquals(ConverterDtm.class, new Fielded(validCaseField("fieldCase4")).converter().getClass());
        assertEquals(ConverterCtm.class, new Fielded(validCaseField("fieldCase5")).converter().getClass());
    }

    @Test
    void isTypeSupported() throws NoSuchFieldException {
        assertTrue(Fielded.isTypeSupported(validCaseField("fieldCase1")));
        assertTrue(Fielded.isTypeSupported(validCaseField("fieldCase2")));
        assertTrue(Fielded.isTypeSupported(validCaseField("fieldCase3")));
        assertTrue(Fielded.isTypeSupported(validCaseField("fieldCase4")));
        assertTrue(Fielded.isTypeSupported(validCaseField("fieldCase5")));
        assertFalse(Fielded.isTypeSupported(invalidCaseField("fieldCase1")));
        assertFalse(Fielded.isTypeSupported(invalidCaseField("fieldCase2")));
        assertFalse(Fielded.isTypeSupported(invalidCaseField("fieldCase3")));
        assertFalse(Fielded.isTypeSupported(invalidCaseField("fieldCase4")));
    }

    @Test
    void LineCase() {
        Fielded fielded = new Fielded(LineCase.class);
        assertEquals("ident", fielded.identifier());
        assertEquals(0, fielded.position());
        assertEquals(5, fielded.length());
    }

    @Test
    void stringFieldCase() throws NoSuchFieldException {
        Fielded fielded = new Fielded(LineCase.class.getDeclaredField("fieldCase1"));
        assertEquals("fieldCase1", fielded.field().getName());
        assertEquals(1, fielded.position());
        assertEquals(10, fielded.length());
        assertEquals(' ', fielded.fill());
        assertEquals(Align.LEFT, fielded.align());
    }

    @Test
    void integerFieldCase() throws NoSuchFieldException {
        Fielded fielded = new Fielded(LineCase.class.getDeclaredField("fieldCase2"));
        assertEquals("fieldCase2", fielded.field().getName());
        assertEquals(2, fielded.position());
        assertEquals(20, fielded.length());
        assertEquals(' ', fielded.fill());
        assertEquals(Align.RIGHT, fielded.align());
    }

    @Test
    void decimalFieldCase() throws NoSuchFieldException {
        Fielded fielded = new Fielded(LineCase.class.getDeclaredField("fieldCase3"));
        assertEquals("fieldCase3", fielded.field().getName());
        assertEquals(3, fielded.position());
        assertEquals(30, fielded.length());
        assertEquals(' ', fielded.fill());
        assertEquals('\0', fielded.separator());
        assertEquals(2, fielded.decimals());
        assertEquals(Align.RIGHT, fielded.align());
    }

    @Test
    void dateTimeFieldCase() throws NoSuchFieldException {
        Fielded fielded = new Fielded(LineCase.class.getDeclaredField("fieldCase4"));
        assertEquals("fieldCase4", fielded.field().getName());
        assertEquals(4, fielded.position());
        assertEquals("ddMMyyyy", fielded.format());
        assertEquals(8, fielded.length());
    }

    @Test
    void customFieldCase() throws NoSuchFieldException {
        Fielded fielded = new Fielded(LineCase.class.getDeclaredField("fieldCase5"));
        assertEquals("fieldCase5", fielded.field().getName());
        assertEquals(5, fielded.position());
        assertEquals(50, fielded.length());
        assertEquals(ConverterCtm.class, fielded.converter().getClass());
        assertEquals(CustomConverterCase.class, ((ConverterCtm) fielded.converter()).custom().getClass());
    }
}