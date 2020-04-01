package org.mirna;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterDtmTest {

    static class LineCase {
        @FieldDtm(position = 1) private Date fieldCase1;
        @FieldDtm(position = 2, format = "dd/MM/yyyy") private Date fieldCase2;
        @FieldDtm(position = 3, format = "yyyy/MM/dd") private Date fieldCase3;
    }

    private static ConverterDtm converter(String field) {
        try {
            return new ConverterDtm(new Fielded(LineCase.class.getDeclaredField(field)));
        } catch (NoSuchFieldException e) {
            throw new Oops(e.getMessage(), e);
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