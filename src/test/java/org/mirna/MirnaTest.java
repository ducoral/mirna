package org.mirna;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;
import org.mirna.core.Align;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MirnaTest {

    @MirnaRecord(identifier = "1")
    static class MirnaRecordType1 {

        @IntegerField(position = 1, length = 5, fill = '0')
        final
        int field1;

        @StringField(position = 2, length = 20)
        final
        String field2;

        @DecimalField(position = 3, length = 5, fill = '0')
        final
        BigDecimal field3;

        public MirnaRecordType1(int field1, String field2, BigDecimal field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @MirnaRecord(identifier = "2")
    static class MirnaRecordType2 {

        @StringField(position = 1, length = 10)
        final
        String field1;

        @DateTimeField(position = 2)
        final
        Date field2;

        @StringField(position = 3, length = 12)
        final
        String field3;

        public MirnaRecordType2(String field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @MirnaRecord(identifier = "3")
    static class MirnaRecordType3 {

        @IntegerField(position = 1, length = 10)
        final
        Integer field1;

        @DateTimeField(position = 2, format = "ddMMyy")
        final
        Date field2;

        @StringField(position = 3, length = 14, align = Align.RIGHT, fill = '*')
        final
        String field3;

        public MirnaRecordType3(Integer field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Test
    void write() {
        Date date1 = new GregorianCalendar(2020, MARCH, 24).getTime();
        Date date2 = new GregorianCalendar(2020, MARCH, 25).getTime();

        List<?> records = Arrays.asList(
                new MirnaRecordType1(15, "string-test", new BigDecimal("12.34")),
                new MirnaRecordType2("type2", date1, "abc"),
                new MirnaRecordType3(123, date1, "string test"),
                new MirnaRecordType1(123, "another string", new BigDecimal("567.89")),
                new MirnaRecordType2("value2", date2, "efg"),
                new MirnaRecordType3(456789, date2, "hijlmnop")
        );

        String expected =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       123240320***string test\n" +
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "3    456789250320******hijlmnop\n";

        StringWriter writer = new StringWriter();
        MirnaWriter mirnaWriter = Mirna.writer(records);
        Mirna.writer(records).write(writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    void reader() {
        fail();
    }

    @Test
    void registerCase1() {
        // writer
        fail();
    }

    @Test
    void registerCase2() {
        // reader
        fail();
    }
}