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

    @Line(identifier = "1")
    static class LineType1 {

        @FieldInt(position = 1, length = 5, fill = '0')
        int field1;

        @FieldStr(position = 2, length = 20)
        String field2;

        @FieldDec(position = 3, length = 5, fill = '0')
        BigDecimal field3;

        public LineType1() {
        }

        public LineType1(int field1, String field2, BigDecimal field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Line(identifier = "2")
    static class LineType2 {

        @FieldStr(position = 1, length = 10)
        String field1;

        @FieldDtm(position = 2)
        Date field2;

        @FieldStr(position = 3, length = 12)
        String field3;

        public LineType2() {
        }

        public LineType2(String field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Line(identifier = "3")
    static class LineType3 {

        @FieldInt(position = 1, length = 10)
        Integer field1;

        @FieldDtm(position = 2, format = "ddMMyy")
        Date field2;

        @FieldStr(position = 3, length = 14, align = Align.RIGHT, fill = '*')
        String field3;

        public LineType3() {
        }

        public LineType3(Integer field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Document
    static class DocumentCase {

        @Header
        LineType1 header;

        @Item
        LineType2 item;

        @Footer
        LineType3 footer;

        public DocumentCase() {
        }

        public DocumentCase(LineType1 header, LineType2 item, LineType3 footer) {
            this.header = header;
            this.item = item;
            this.footer = footer;
        }
    }

    @Test
    void writeDocument() {
        Date date = new GregorianCalendar(2020, MARCH, 24).getTime();
        DocumentCase document = new DocumentCase(
                new LineType1(15, "string-test", new BigDecimal("12.34")),
                new LineType2("type2", date, "abc"),
                new LineType3(123, date, "string test"));
        String expected =
                "100015string-test         01234\n" +
                "2type2     24032020abc         \n" +
                "3       123240320***string test\n";
        StringWriter writer = new StringWriter();
        Mirna.writeDocument(document, writer);
        assertEquals(expected, writer.toString());

        date = new GregorianCalendar(2020, MARCH, 25).getTime();
        document = new DocumentCase(
                new LineType1(123, "another string", new BigDecimal("567.89")),
                new LineType2("value2", date, "efg"),
                new LineType3(456789, date, "hijlmnop"));
        expected =
                "100123another string      56789\n" +
                "2value2    25032020efg         \n" +
                "3    456789250320******hijlmnop\n";
        writer = new StringWriter();
        Mirna.writeDocument(document, writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    void writeLines() {
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

        mirna.register(new LineType1(15, "string-test", new BigDecimal("12.34")));
        mirna.register(new LineType2("type2", date1, "abc"));
        mirna.register(new LineType3(123, date1, "string test"));
        mirna.register(new LineType1(123, "another string", new BigDecimal("567.89")));
        mirna.register(new LineType2("value2", date2, "efg"));
        mirna.register(new LineType3(456789, date2, "hijlmnop"));

        StringWriter writer = new StringWriter();
        mirna.writeLines(writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    void readerDocument() {
        String text =
            "100015string-test         01234\n" +
            "2type2     24032020abc         \n" +
            "3       123240320***string test\n";

        DocumentCase document = Mirna.readDocument(DocumentCase.class, new StringReader(text));

        assertEquals(15, document.header.field1);
        assertEquals("string-test", document.header.field2);
        assertEquals(new BigDecimal("12.34"), document.header.field3);

        Date date = new GregorianCalendar(2020, MARCH, 24).getTime();
        assertEquals("type2", document.item.field1);
        assertEquals(date, document.item.field2);
        assertEquals("abc", document.item.field3);

        assertEquals(123, document.footer.field1);
        assertEquals(date, document.footer.field2);
        assertEquals("string test", document.footer.field3);

        text =
            "100123another string      56789\n" +
            "2value2    25032020efg         \n" +
            "3    456789250320******hijlmnop\n";

        document = Mirna.readDocument(DocumentCase.class, new StringReader(text));

        assertEquals(123, document.header.field1);
        assertEquals("another string", document.header.field2);
        assertEquals(new BigDecimal("567.89"), document.header.field3);

        date = new GregorianCalendar(2020, MARCH, 25).getTime();
        assertEquals("value2", document.item.field1);
        assertEquals(date, document.item.field2);
        assertEquals("efg", document.item.field3);

        assertEquals(456789, document.footer.field1);
        assertEquals(date, document.footer.field2);
        assertEquals("hijlmnop", document.footer.field3);
    }

    @Test
    void readerLines() {
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

        mirna.register(LineType1.class);
        mirna.register(LineType2.class);
        mirna.register(LineType3.class);

        List<?> records = mirna.readLines(new StringReader(text));

        assertEquals(6, records.size());

        LineType1 type1 = (LineType1) records.get(0);
        assertEquals(15, type1.field1);
        assertEquals("string-test", type1.field2);
        assertEquals(new BigDecimal("12.34"), type1.field3);

        LineType2 type2 = (LineType2) records.get(1);
        assertEquals("type2", type2.field1);
        assertEquals(date1, type2.field2);
        assertEquals("abc", type2.field3);

        LineType3 type3 = (LineType3) records.get(2);
        assertEquals(123, type3.field1);
        assertEquals(date1, type3.field2);
        assertEquals("string test", type3.field3);

        type1 = (LineType1) records.get(3);
        assertEquals(123, type1.field1);
        assertEquals("another string", type1.field2);
        assertEquals(new BigDecimal("567.89"), type1.field3);

        type2 = (LineType2) records.get(4);
        assertEquals("value2", type2.field1);
        assertEquals(date2, type2.field2);
        assertEquals("efg", type2.field3);

        type3 = (LineType3) records.get(5);
        assertEquals(456789, type3.field1);
        assertEquals(date2, type3.field2);
        assertEquals("hijlmnop", type3.field3);
    }

    @Test
    void readerRecordsExceptionCase1() {
        String text =
                "400015string-test         01234\n";

        Mirna mirna = new Mirna();
        mirna.register(LineType1.class);
        mirna.register(LineType2.class);
        mirna.register(LineType3.class);

        assertThrows(
                Oops.class,
                () -> mirna.readLines(new StringReader(text)),
                Strs.MSG_UNMAPPED_LINE.format(1, "400015string-test         01234"));
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

        mirna.register(LineType1.class);
        mirna.register(LineType2.class);
        mirna.register(LineType3.class);

        assertThrows(
                Oops.class,
                () -> mirna.readLines(new StringReader(text)),
                Strs.MSG_UNMAPPED_LINE.format(6, "4    456789250320******hijlmnop"));
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

        mirna.register(LineType1.class);
        mirna.register(LineType2.class);
        mirna.register(LineType3.class);

        assertThrows(
                Oops.class,
                () -> mirna.readLines(new StringReader(text)),
                Strs.MSG_ERROR_PARSING_LINE.format(3));
    }
}