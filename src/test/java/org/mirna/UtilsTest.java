package org.mirna;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;
import org.mirna.converters.CustomConverter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilsTest {

    @Test
    void chars() {
        assertEquals("**", Utils.chars(2, '*'));
        assertEquals("0000000", Utils.chars(7, '0'));
    }

    @Test
    void fillLeft() {
        assertEquals("00001234", Utils.fixRight("1234", 8, '0'));
        assertEquals("1234", Utils.fixRight("1234567", 4, ' '));
    }

    @Test
    void fillRight() {
        assertEquals("12345          ", Utils.fixLeft("12345", 15, ' '));
        assertEquals("345", Utils.fixLeft("12345", 3, ' '));
    }

    @Test
    void strList() {
        assertEquals(Arrays.asList("orange", "10", "true"), Utils.strList("orange", 10, true));
    }

    @Test
    void resource() {
        assertNotNull(Utils.resource());
    }

    @MirnaRecord(identifier = "ident_1")
    public static class MirnaRecordCase1 {
        @CustomField(pos = 3, len = 15, con = CustomConverter.class) private StringBuilder customField3;
        @StringField(pos = 1, len = 10) private String stringField1;
        @IntegerField(pos = 2, len = 5) private Integer integerField2;
    }

    @MirnaRecord(identifier = "ident_2")
    public static class MirnaRecordCase2 {
        @DecimalField(pos = 3, len = 10) private Float decimalField3;
        @DateTimeField(pos = 2) private Date dateField2;
        @DecimalField(pos = 4, len = 15) private Double decimalField4;
        @DateTimeField(pos = 1) private Date dateField1;
    }

    @MirnaRecord(identifier = "ident_3")
    public static class MirnaRecordCase3 {
        @DecimalField(pos = 3, len = 10) private float decimalField3;
        @DecimalField(pos = 2, len = 15) private double decimalField2;
        @IntegerField(pos = 1, len = 5) private int integerField1;
    }

    @Test
    void descriptors() {
        List<Descriptor> desc = Utils.descriptors(MirnaRecordCase1.class);
        assertEquals(4, desc.size());
        assertEquals(desc.get(0).pos, 0);
        assertEquals(desc.get(1).pos, 1);
        assertEquals(desc.get(2).pos, 2);
        assertEquals(desc.get(3).pos, 3);

        desc = Utils.descriptors(MirnaRecordCase2.class);
        assertEquals(5, desc.size());
        assertEquals(desc.get(0).pos, 0);
        assertEquals(desc.get(1).pos, 1);
        assertEquals(desc.get(2).pos, 2);
        assertEquals(desc.get(3).pos, 3);
        assertEquals(desc.get(4).pos, 4);

        desc = Utils.descriptors(MirnaRecordCase3.class);
        assertEquals(4, desc.size());
        assertEquals(desc.get(0).pos, 0);
        assertEquals(desc.get(1).pos, 1);
        assertEquals(desc.get(2).pos, 2);
        assertEquals(desc.get(3).pos, 3);
    }

    @Test
    void report() {
        String expected =
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| campo          | pos | de | até | tam | valor     |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| id do registro |   0 |  1 |   7 |   7 | 'ident_1' |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| string field1  |   1 |  8 |  17 |  10 | <string>  |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| integer field2 |   2 | 18 |  22 |   5 | <integer> |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| custom field3  |   3 | 23 |  37 |  15 | <custom>  |\n" +
            "+----------------+-----+----+-----+-----+-----------+";
        assertEquals(expected, Utils.report(MirnaRecordCase1.class));

        expected =
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| campo          | pos | de | até | tam | valor       |\n" +
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| id do registro |   0 |  1 |   7 |   7 | 'ident_2'   |\n" +
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| date field1    |   1 |  8 |  15 |   8 | <date time> |\n" +
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| date field2    |   2 | 16 |  23 |   8 | <date time> |\n" +
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| decimal field3 |   3 | 24 |  33 |  10 | <decimal>   |\n" +
            "+----------------+-----+----+-----+-----+-------------+\n" +
            "| decimal field4 |   4 | 34 |  48 |  15 | <decimal>   |\n" +
            "+----------------+-----+----+-----+-----+-------------+";
        assertEquals(expected, Utils.report(MirnaRecordCase2.class));

        expected =
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| campo          | pos | de | até | tam | valor     |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| id do registro |   0 |  1 |   7 |   7 | 'ident_3' |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| integer field1 |   1 |  8 |  12 |   5 | <integer> |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| decimal field2 |   2 | 13 |  27 |  15 | <decimal> |\n" +
            "+----------------+-----+----+-----+-----+-----------+\n" +
            "| decimal field3 |   3 | 28 |  37 |  10 | <decimal> |\n" +
            "+----------------+-----+----+-----+-----+-----------+";
        assertEquals(expected, Utils.report(MirnaRecordCase3.class));
    }
}
