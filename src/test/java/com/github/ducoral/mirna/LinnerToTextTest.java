package com.github.ducoral.mirna;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinnerToTextTest {

    @Line(identifier = "ident1")
    static class LineCase1 {

        @FieldStr(position = 1, length = 1)
        char fieldCase1 = 'c';

        @FieldStr(position = 2, length = 10, fill = '*', align = Align.RIGHT)
        String fieldCase2 = "str2";

        @FieldStr(position = 3, length = 15, fill = '-')
        String fieldCase3 = "str3";
    }

    @Test
    void testCase1() {
        String expected = "ident1"
                + "c"
                + "******str2"
                + "str3-----------";
        Assertions.assertEquals(expected, new Linner(LineCase1.class).toText(new LineCase1()));
    }

    @Line(identifier = "ident2")
    static class LineCase2 {

        @FieldInt(position = 1, length = 10, fill = '0')
        byte fieldCase1 = 123;

        @FieldInt(position = 2, length = 15, align = Align.LEFT)
        short fieldCase2 = 456;

        @FieldInt(position = 3, length = 20, fill = '_')
        int fieldCase3 = 789;

        @FieldInt(position = 4, length = 25, fill = '*', align = Align.LEFT)
        long fieldCase4 = 1234567890;

        @FieldInt(position = 5, length = 30, fill = '0')
        BigInteger fieldCase5 = new BigInteger("12345678901234567890");
    }

    @Test
    void testCase2() {
        String expected = "ident2"
                + "0000000123"
                + "456            "
                + "_________________789"
                + "1234567890***************"
                + "000000000012345678901234567890";
        assertEquals(expected, new Linner(LineCase2.class).toText(new LineCase2()));
    }

    @Line(identifier = "ident3")
    static class LineCase3 {

        @FieldDec(position = 1, length = 10, fill = '0')
        float fieldCase1 = 12.34f;

        @FieldDec(position = 2, length = 15, decimals = 3, align = Align.LEFT)
        double fieldCase2 = 56.789;

        @FieldDec(position = 3, length = 20, decimals = 4, fill = '*', separator = '.')
        float fieldCase3 = 1234.567f;

        @FieldDec(position = 4, length = 25, decimals = 5, separator = ',')
        double fieldCase4 = 12345.67890;

        @FieldDec(position = 5, length = 30, decimals = 9, fill = '0')
        BigDecimal fieldCase5 = new BigDecimal("123456789.123456789");
    }

    @Test
    void testCase3() {
        String expected = "ident3"
                + "0000001234"
                + "56789          "
                + "***********1234.5670"
                + "              12345,67890"
                + "000000000000123456789123456789";
        assertEquals(expected, new Linner(LineCase3.class).toText(new LineCase3()));
    }

    @Line(identifier = "ident4")
    static class LineCase4 {

        @FieldDtm(position = 1)
        Date fieldCase1 = new GregorianCalendar(2020, MARCH, 21).getTime();

        @FieldDtm(position = 2, format = "ddMMyy")
        Date fieldCase2 = new GregorianCalendar(2020, MARCH, 21).getTime();

        @FieldDtm(position = 3, format = "dd/MM/yyyy")
        Date fieldCase3 = new GregorianCalendar(2020, MARCH, 21).getTime();

        @FieldDtm(position = 4, format = "yyyy-MM-dd")
        Date fieldCase4 = new GregorianCalendar(2020, MARCH, 21).getTime();
    }

    @Test
    void testCase4() {
        String expected = "ident4"
                + "21032020"
                + "210320"
                + "21/03/2020"
                + "2020-03-21";
        assertEquals(expected, new Linner(LineCase4.class).toText(new LineCase4()));
    }

    static class CustomObject {
        final String string;
        final Integer integer;
        CustomObject(String string, Integer integer) {
            this.string = string;
            this.integer = integer;
        }
    }

    static class CustomConverterCase implements Converter {
        @Override
        public String toText(Object value) {
            CustomObject custom = (CustomObject) value;
            return custom.string + custom.integer;
        }

        @Override
        public Object fromText(String text) {
            return new CustomObject(text, 0); // not tested
        }
    }

    @Line(identifier = "ident5")
    static class LineCase5 {

        @FieldCtm(position = 1, length = 10, converter = CustomConverterCase.class)
        CustomObject fieldCase1 = new CustomObject("ten", 10);

        @FieldCtm(position = 2, length = 20, fill = '*', align = Align.RIGHT, converter = CustomConverterCase.class)
        CustomObject fieldCase2 = new CustomObject("twenty", 20);
    }

    @Test
    void testCase5() {
        String expected = "ident5"
                + "ten10     "
                + "************twenty20";
        assertEquals(expected, new Linner(LineCase5.class).toText(new LineCase5()));
    }
}