package org.mirna;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MirnaTest {

    @MirnaRecord(identifier = "1")
    static class MirnaRecordType1 {

        @IntegerField(position = 1, length = 5, fill = '0')
        int field1;

        @StringField(position = 2, length = 20)
        String field2;

        @DecimalField(position = 3, length = 5, fill = '0')
        BigDecimal field3;

        public MirnaRecordType1() {
        }

        public MirnaRecordType1(int field1, String field2, BigDecimal field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @MirnaRecord(identifier = "2")
    static class MirnaRecordType2 {

        @StringField(position = 1, length = 10)
        String field1;

        @DateTimeField(position = 2)
        Date field2;

        @StringField(position = 3, length = 12)
        String field3;

        public MirnaRecordType2() {
        }

        public MirnaRecordType2(String field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @MirnaRecord(identifier = "3")
    static class MirnaRecordType3 {

        @IntegerField(position = 1, length = 10)
        Integer field1;

        @DateTimeField(position = 2, format = "ddMMyy")
        Date field2;

        @StringField(position = 3, length = 14, align = Align.RIGHT, fill = '*')
        String field3;

        public MirnaRecordType3() {
        }

        public MirnaRecordType3(Integer field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Test
    void writeRecords() {
        Date date1 = new GregorianCalendar(2020, MARCH, 24).getTime();
        Date date2 = new GregorianCalendar(2020, MARCH, 25).getTime();

        String expected =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       123240320***string test\n" +
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "3    456789250320******hijlmnop\n";

        Mirna mirna = new Mirna();

        mirna.register(new MirnaRecordType1(15, "string-test", new BigDecimal("12.34")));
        mirna.register(new MirnaRecordType2("type2", date1, "abc"));
        mirna.register(new MirnaRecordType3(123, date1, "string test"));
        mirna.register(new MirnaRecordType1(123, "another string", new BigDecimal("567.89")));
        mirna.register(new MirnaRecordType2("value2", date2, "efg"));
        mirna.register(new MirnaRecordType3(456789, date2, "hijlmnop"));

        StringWriter writer = new StringWriter();
        mirna.writeRecords(writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    void readerRecords() {
        Date date1 = new GregorianCalendar(2020, MARCH, 24).getTime();
        Date date2 = new GregorianCalendar(2020, MARCH, 25).getTime();

        String text =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       123240320***string test\n" +
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "3    456789250320******hijlmnop\n";

        Mirna mirna = new Mirna();

        mirna.register(MirnaRecordType1.class);
        mirna.register(MirnaRecordType2.class);
        mirna.register(MirnaRecordType3.class);

        List<?> records = mirna.readRecords(new StringReader(text));

        assertEquals(6, records.size());

        MirnaRecordType1 type1 = (MirnaRecordType1) records.get(0);
        assertEquals(15, type1.field1);
        assertEquals("string-test", type1.field2);
        assertEquals(new BigDecimal("12.34"), type1.field3);

        MirnaRecordType2 type2 = (MirnaRecordType2) records.get(1);
        assertEquals("type2", type2.field1);
        assertEquals(date1, type2.field2);
        assertEquals("abc", type2.field3);

        MirnaRecordType3 type3 = (MirnaRecordType3) records.get(2);
        assertEquals(123, type3.field1);
        assertEquals(date1, type3.field2);
        assertEquals("string test", type3.field3);

        type1 = (MirnaRecordType1) records.get(3);
        assertEquals(123, type1.field1);
        assertEquals("another string", type1.field2);
        assertEquals(new BigDecimal("567.89"), type1.field3);

        type2 = (MirnaRecordType2) records.get(4);
        assertEquals("value2", type2.field1);
        assertEquals(date2, type2.field2);
        assertEquals("efg", type2.field3);

        type3 = (MirnaRecordType3) records.get(5);
        assertEquals(456789, type3.field1);
        assertEquals(date2, type3.field2);
        assertEquals("hijlmnop", type3.field3);
    }

    @Test
    void readerRecordsExceptionCase1() {
        String text =
                "400015string-test         01234\n";

        Mirna mirna = new Mirna();
        mirna.register(MirnaRecordType1.class);
        mirna.register(MirnaRecordType2.class);
        mirna.register(MirnaRecordType3.class);

        assertThrows(
                MirnaException.class,
                () -> mirna.readRecords(new StringReader(text)),
                Strs.MSG_UNMAPPED_RECORD.format(1, "400015string-test         01234"));
    }

    @Test
    void readerRecordsExceptionCase2() {
        String text =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       123240320***string test\n" +
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "4    456789250320******hijlmnop\n";

        Mirna mirna = new Mirna();

        mirna.register(MirnaRecordType1.class);
        mirna.register(MirnaRecordType2.class);
        mirna.register(MirnaRecordType3.class);

        assertThrows(
                MirnaException.class,
                () -> mirna.readRecords(new StringReader(text)),
                Strs.MSG_UNMAPPED_RECORD.format(6, "4    456789250320******hijlmnop"));
    }

    @Test
    void readerRecordsExceptionCase3() {
        String text =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       12324XX20***string test\n" +
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "3    456789250320******hijlmnop\n";

        Mirna mirna = new Mirna();

        mirna.register(MirnaRecordType1.class);
        mirna.register(MirnaRecordType2.class);
        mirna.register(MirnaRecordType3.class);

        assertThrows(
                MirnaException.class,
                () -> mirna.readRecords(new StringReader(text)),
                Strs.MSG_ERROR_PARSING_TEXT.format(3));
    }
}