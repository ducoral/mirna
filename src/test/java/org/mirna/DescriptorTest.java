package org.mirna;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;
import org.mirna.converters.DateTimeConverter;
import org.mirna.converters.DecimalConverter;
import org.mirna.converters.IntegerConverter;
import org.mirna.converters.StringConverter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirna.Align.LEFT;
import static org.mirna.Align.RIGHT;

class DescriptorTest {

    @MirnaRecord(identifier = "ident")
    private static class MirnaRecordCase {
        @StringField(position = 1, length = 1, fill = '*', align = RIGHT)
        private String strF;
        @DecimalField(position = 2, length = 2, fill = '0', align = LEFT, separator = '.', decimals = 4)
        private Double decF;
        @IntegerField(position = 3, length = 3, fill = '_', align = LEFT)
        private Integer intF;
        @DateTimeField(position = 4, format = "ddMMyy")
        private Date dtmF;
        @CustomField(position = 5, length = 5, fill = '-', align = RIGHT, converter = StringConverter.class)
        private StringBuilder ctmF;
    }

    private static Field getField(String name) {
        try {
            return MirnaRecordCase.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    @Test
    void isAnnotated() {
        assertTrue(Descriptor.isAnnotated(getField("strF")));
        assertTrue(Descriptor.isAnnotated(getField("decF")));
        assertTrue(Descriptor.isAnnotated(getField("intF")));
        assertTrue(Descriptor.isAnnotated(getField("dtmF")));
        assertTrue(Descriptor.isAnnotated(getField("ctmF")));
    }

    @Test
    void isValid() {
        byte byteValue = 1;
        short shortValue = 1;
        int intValue = 1;
        long longValue = 1;
        float floatValue = 1;
        double doubleValue = 1;
        char charValue = '1';
        BigInteger bigIntegerValue = new BigInteger("1");
        BigDecimal bigDecimalValue = new BigDecimal("1");
        String stringValue = "1";
        Date dateValue = new Date();
        StringBuilder objectValue = new StringBuilder();

        assertTrue(Descriptor.isValid(byteValue,         IntegerField.class));
        assertTrue(Descriptor.isValid(shortValue,        IntegerField.class));
        assertTrue(Descriptor.isValid(intValue,          IntegerField.class));
        assertTrue(Descriptor.isValid(longValue,         IntegerField.class));
        assertTrue(Descriptor.isValid(bigIntegerValue,   IntegerField.class));
        assertTrue(Descriptor.isValid(floatValue,        DecimalField.class));
        assertTrue(Descriptor.isValid(doubleValue,       DecimalField.class));
        assertTrue(Descriptor.isValid(bigDecimalValue,   DecimalField.class));
        assertTrue(Descriptor.isValid(dateValue,         DateTimeField.class));
        assertTrue(Descriptor.isValid(charValue,         StringField.class));
        assertTrue(Descriptor.isValid(stringValue,       StringField.class));
        assertTrue(Descriptor.isValid(objectValue,       CustomField.class));

        assertFalse(Descriptor.isValid(byteValue,        DecimalField.class));
        assertFalse(Descriptor.isValid(shortValue,       DecimalField.class));
        assertFalse(Descriptor.isValid(intValue,         DecimalField.class));
        assertFalse(Descriptor.isValid(longValue,        DecimalField.class));
        assertFalse(Descriptor.isValid(bigIntegerValue,  DecimalField.class));

        assertFalse(Descriptor.isValid(byteValue,        DateTimeField.class));
        assertFalse(Descriptor.isValid(shortValue,       DateTimeField.class));
        assertFalse(Descriptor.isValid(intValue,         DateTimeField.class));
        assertFalse(Descriptor.isValid(longValue,        DateTimeField.class));
        assertFalse(Descriptor.isValid(bigIntegerValue,  DateTimeField.class));

        assertFalse(Descriptor.isValid(byteValue,        StringField.class));
        assertFalse(Descriptor.isValid(shortValue,       StringField.class));
        assertFalse(Descriptor.isValid(intValue,         StringField.class));
        assertFalse(Descriptor.isValid(longValue,        StringField.class));
        assertFalse(Descriptor.isValid(bigIntegerValue,  StringField.class));

        assertFalse(Descriptor.isValid(floatValue,       IntegerField.class));
        assertFalse(Descriptor.isValid(doubleValue,      IntegerField.class));
        assertFalse(Descriptor.isValid(bigDecimalValue,  IntegerField.class));

        assertFalse(Descriptor.isValid(floatValue,       DateTimeField.class));
        assertFalse(Descriptor.isValid(doubleValue,      DateTimeField.class));
        assertFalse(Descriptor.isValid(bigDecimalValue,  DateTimeField.class));

        assertFalse(Descriptor.isValid(floatValue,       StringField.class));
        assertFalse(Descriptor.isValid(doubleValue,      StringField.class));
        assertFalse(Descriptor.isValid(bigDecimalValue,  StringField.class));

        assertFalse(Descriptor.isValid(dateValue,        IntegerField.class));
        assertFalse(Descriptor.isValid(dateValue,        DecimalField.class));
        assertFalse(Descriptor.isValid(dateValue,        StringField.class));

        assertFalse(Descriptor.isValid(charValue,        IntegerField.class));
        assertFalse(Descriptor.isValid(stringValue,      IntegerField.class));

        assertFalse(Descriptor.isValid(charValue,        DecimalField.class));
        assertFalse(Descriptor.isValid(stringValue,      DecimalField.class));

        assertFalse(Descriptor.isValid(charValue,        DateTimeField.class));
        assertFalse(Descriptor.isValid(stringValue,      DateTimeField.class));
    }


    @Test
    void createCaseMirnaRecord() {
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getAnnotation(MirnaRecord.class));
        assertEquals("", des.name);
        assertEquals(0, des.position);
        assertEquals(5, des.length);
        assertEquals('\0', des.fill);
        assertEquals("", des.format);
        assertEquals('\0', des.separator);
        assertEquals(0, des.decimals);
        assertEquals("'ident'", des.value);
        assertNull(des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseStringField() {
        // @StringField(pos = 1, len = 1, fil = '*', ali = RIGHT) private String strF;
        Descriptor des = Descriptor.create(getField("strF"));
        assertEquals("strF", des.name);
        assertEquals(1, des.position);
        assertEquals(1, des.length);
        assertEquals('*', des.fill);
        assertEquals("", des.format);
        assertEquals('\0', des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<string>", des.value);
        assertEquals(StringConverter.class, des.converter);
        assertEquals(RIGHT, des.align);
    }

    @Test
    void createCaseIntegerField() {
        // @IntegerField(pos = 3, len = 3, fil = '_', ali = LEFT) private Integer intF;
        Descriptor des = Descriptor.create(getField("intF"));
        assertEquals("intF", des.name);
        assertEquals(3, des.position);
        assertEquals(3, des.length);
        assertEquals('_', des.fill);
        assertEquals("", des.format);
        assertEquals('\0', des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<integer>", des.value);
        assertEquals(IntegerConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseDecimalField() {
        // @DecimalField(pos = 2, len = 2, fil = '0', ali = LEFT, sep = true, dec = 4) private Double decF;
        Descriptor des = Descriptor.create(getField("decF"));
        assertEquals("decF", des.name);
        assertEquals(2, des.position);
        assertEquals(2, des.length);
        assertEquals('0', des.fill);
        assertEquals("", des.format);
        assertEquals('.', des.separator);
        assertEquals(4, des.decimals);
        assertEquals("<decimal>", des.value);
        assertEquals(DecimalConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseDateTimeField() {
        // @DateTimeField(pos = 4, fmt = "ddMMyy") private Date dtmF;
        Descriptor des = Descriptor.create(getField("dtmF"));
        assertEquals("dtmF", des.name);
        assertEquals(4, des.position);
        assertEquals(6, des.length);
        assertEquals('\0', des.fill);
        assertEquals("ddMMyy", des.format);
        assertEquals('\0', des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<date time>", des.value);
        assertEquals(DateTimeConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseCustomField() {
        // @CustomField(pos = 5, len = 5, fil = '-', ali = RIGHT, con = StringConverter.class) private StringBuilder ctmF;
        Descriptor des = Descriptor.create(getField("ctmF"));
        assertEquals("ctmF", des.name);
        assertEquals(5, des.position);
        assertEquals(5, des.length);
        assertEquals('-', des.fill);
        assertEquals("", des.format);
        assertEquals('\0', des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<custom>", des.value);
        assertEquals(StringConverter.class, des.converter);
        assertEquals(RIGHT, des.align);
    }
}