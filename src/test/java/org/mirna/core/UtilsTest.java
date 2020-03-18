package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilsTest {

    @Test
    void chars() {
        assertEquals("**", Utils.chars(2, '*'));
        assertEquals("0000000", Utils.chars(7, '0'));
    }

    @Test
    void fixStr() {
        assertEquals("00001234", Utils.fixStr("1234", 8, '0', Align.RIGHT));
        assertEquals("1234", Utils.fixStr("1234567", 4, ' ', Align.RIGHT));
        assertEquals("12345          ", Utils.fixStr("12345", 15, ' ', Align.LEFT));
        assertEquals("345", Utils.fixStr("12345", 3, ' ', Align.LEFT));
    }

    @Test
    void fixLeft() {
        assertEquals("00001234", Utils.fixRight("1234", 8, '0'));
        assertEquals("1234", Utils.fixRight("1234567", 4, ' '));
    }

    @Test
    void fixRight() {
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
        @CustomField(position = 3, length = 15, converter = StringConverter.class) private StringBuilder customField3;
        @StringField(position = 1, length = 10) private String stringField1;
        @IntegerField(position = 2, length = 5) private Integer integerField2;
    }

    @MirnaRecord(identifier = "ident_2")
    public static class MirnaRecordCase2 {
        @DecimalField(position = 3, length = 10) private Float decimalField3;
        @DateTimeField(position = 2) private Date dateField2;
        @DecimalField(position = 4, length = 15) private Double decimalField4;
        @DateTimeField(position = 1) private Date dateField1;
    }

    @MirnaRecord(identifier = "ident_3")
    public static class MirnaRecordCase3 {
        @DecimalField(position = 3, length = 10) private float decimalField3;
        @DecimalField(position = 2, length = 15) private double decimalField2;
        @IntegerField(position = 1, length = 5) private int integerField1;
    }
}
