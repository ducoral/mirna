package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.MirnaException;
import org.mirna.annotations.DateTimeField;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeConverterTest {

    static class MirnaRecordCase {
        @DateTimeField(position = 1) private Date fieldCase1;
        @DateTimeField(position = 2, format = "dd/MM/yyyy") private Date fieldCase2;
        @DateTimeField(position = 3, format = "yyyy/MM/dd") private Date fieldCase3;
    }

    private static DateTimeConverter converter(String field) {
        try {
            return new DateTimeConverter(new Mapping(MirnaRecordCase.class.getDeclaredField(field)));
        } catch (NoSuchFieldException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    @Test
    void toText() {
        Date date = new GregorianCalendar(2020, MARCH, 15).getTime();
        assertEquals("15032020", converter("fieldCase1").toText(date));
        assertEquals("15/03/2020", converter("fieldCase2").toText(date));
        assertEquals("2020/03/15", converter("fieldCase3").toText(date));
    }

    @Test
    void fromText() {
        Date date = new GregorianCalendar(2020, MARCH, 15).getTime();
        assertEquals(format(date), format(converter("fieldCase1").fromText("15032020")));
        assertEquals(format(date), format(converter("fieldCase2").fromText("15/03/2020")));
        assertEquals(format(date), format(converter("fieldCase3").fromText("2020/03/15")));
    }

    private static String format(Object date) {
        return new SimpleDateFormat("ddMMyyyy").format((Date)date);
    }
}