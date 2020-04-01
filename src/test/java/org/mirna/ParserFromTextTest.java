package org.mirna;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.GregorianCalendar.MARCH;
import static org.junit.jupiter.api.Assertions.*;

class ParserFromTextTest {

    @Line(identifier = "id1")
    static class LineCase1 {

        @StringField(position = 1, length = 5)
        String fieldCase1;

        @StringField(position = 2, length = 10, fill = '*', align = Align.RIGHT)
        String fieldCase2;
    }

    @Test
    void testCase1() {
        String text = "id1str1 ******str2";
        Object record = new Parser(LineCase1.class).fromText(text);
        assertEquals(LineCase1.class, record.getClass());
        assertEquals("str1", ((LineCase1) record).fieldCase1);
        assertEquals("str2", ((LineCase1) record).fieldCase2);
    }

    @Line(identifier = "id2")
    static class LineCase2 {

        @IntegerField(position = 1, length = 10, fill = '0')
        Integer fieldCase1;

        @IntegerField(position = 2, length = 15, align = Align.LEFT)
        Integer fieldCase2;
    }

    @Test
    void testCase2() {
        String text = "id20000000123456            ";
        Object record = new Parser(LineCase2.class).fromText(text);
        assertEquals(LineCase2.class, record.getClass());
        assertEquals(123, ((LineCase2) record).fieldCase1);
        assertEquals(456, ((LineCase2) record).fieldCase2);
    }

    @Line(identifier = "id3")
    static class LineCase3 {

        @DecimalField(position = 1, length = 10, fill = '0')
        BigDecimal fieldCase1;

        @DecimalField(position = 2, length = 15, separator = '.', align = Align.LEFT)
        BigDecimal fieldCase2;

        @DecimalField(position = 3, length = 20, decimals = 4)
        BigDecimal fieldCase3;
    }

    @Test
    void testCase3() {
        String text = "id30000123456123456.78                1234567891";
        Object record = new Parser(LineCase3.class).fromText(text);
        assertEquals(LineCase3.class, record.getClass());
        assertEquals(new BigDecimal("1234.56"), ((LineCase3) record).fieldCase1);
        assertEquals(new BigDecimal("123456.78"), ((LineCase3) record).fieldCase2);
        assertEquals(new BigDecimal("123456.7891"), ((LineCase3) record).fieldCase3);
    }

    @Line(identifier = "id4")
    static class LineCase4 {

        @DateTimeField(position = 1)
        Date fieldCase1;

        @DateTimeField(position = 2, format = "yyyy-MM-dd")
        Date fieldCase2;

        @DateTimeField(position = 3, format = "dd/MM/yyyy")
        Date fieldCase3;
    }

    @Test
    void testCase4() {
        String text = "id4220320202020-03-2222/03/2020";
        Object record = new Parser(LineCase4.class).fromText(text);
        assertEquals(LineCase4.class, record.getClass());
        assertEquals(new GregorianCalendar(2020, MARCH, 22).getTime(), ((LineCase4) record).fieldCase1);
        assertEquals(new GregorianCalendar(2020, MARCH, 22).getTime(), ((LineCase4) record).fieldCase2);
        assertEquals(new GregorianCalendar(2020, MARCH, 22).getTime(), ((LineCase4) record).fieldCase3);
    }

    static class CustomObject {
        final String string;
        final Integer integer;

        public CustomObject(String string, Integer integer) {
            this.string = string;
            this.integer = integer;
        }
    }

    static class CustomConverterCase implements Converter {

        @Override
        public String toText(Object value) {
            CustomObject custom = (CustomObject) value;
            return custom.string + '.' + custom.integer;
        }

        @Override
        public Object fromText(String text) {
            int dot = text.indexOf('.');
            return new CustomObject(text.substring(0, dot), Integer.valueOf(text.substring(dot + 1)));
        }
    }

    @Line(identifier = "id5")
    static class LineCase5 {

        @CustomField(position = 1, length = 10, converter = CustomConverterCase.class)
        CustomObject fieldCase1;

        @CustomField(position = 2, length = 15, converter = CustomConverterCase.class, fill = '*')
        CustomObject fieldCase2;

        @CustomField(position = 3, length = 20, converter = CustomConverterCase.class, fill = '_', align = Align.RIGHT)
        CustomObject fieldCase3;
    }

    @Test
    void testCase5() {
        String text = "id5one.1     two.2**********_____________three.3";
        Object record = new Parser(LineCase5.class).fromText(text);
        assertEquals(LineCase5.class, record.getClass());
        assertEquals("one", ((LineCase5) record).fieldCase1.string);
        assertEquals(1, ((LineCase5) record).fieldCase1.integer);
        assertEquals("two", ((LineCase5) record).fieldCase2.string);
        assertEquals(2, ((LineCase5) record).fieldCase2.integer);
        assertEquals("three", ((LineCase5) record).fieldCase3.string);
        assertEquals(3, ((LineCase5) record).fieldCase3.integer);
    }

    @Line(identifier = "id6")
    static class LineCase6 {

        @IntegerField(position = 1, length = 10)
        Integer fieldCase1;
    }

    @Test
    void testParseException() {
        String text = "id6      abcd";
        assertThrows(
                MirnaException.class,
                () -> new Parser(LineCase6.class).fromText(text),
                Strs.MSG_ERROR_PARSING_FIELD.format("      abcd", "fieldCase1"));
    }
}