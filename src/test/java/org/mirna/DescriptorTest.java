package org.mirna;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.*;
import org.mirna.converters.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirna.Align.LEFT;
import static org.mirna.Align.RIGHT;

class DescriptorTest {

    @MirnaRecord(identifier = "ident")
    private static class MirnaRecordCase {
        @StringField(position = 1, length = 1, fill = '*', align = RIGHT) private String strF;
        @DecimalField(position = 2, length = 2, fill = '0', align = LEFT, separator = true, decimals = 4) private Double decF;
        @IntegerField(position = 3, length = 3, fill = '_', align = LEFT) private Integer intF;
        @DateTimeField(position = 4, format = "ddMMyy") private Date dtmF;
        @CustomField(position = 5, length = 5, fill = '-', align = RIGHT, converter = CustomConverter.class) private StringBuilder ctmF;
    }

    @Test
    void isAnnotated() throws NoSuchFieldException {
        assertTrue(Descriptor.isAnnotated(MirnaRecordCase.class.getDeclaredField("strF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordCase.class.getDeclaredField("decF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordCase.class.getDeclaredField("intF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordCase.class.getDeclaredField("dtmF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordCase.class.getDeclaredField("ctmF")));
    }

    @Test
    void createCaseMirnaRecord() {
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getAnnotation(MirnaRecord.class));
        assertEquals("", des.name);
        assertEquals(0, des.position);
        assertEquals(5, des.length);
        assertEquals('\0', des.fill);
        assertEquals("", des.format);
        assertFalse(des.separator);
        assertEquals(0, des.decimals);
        assertEquals("'ident'", des.value);
        assertNull(des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseStringField() throws NoSuchFieldException {
        // @StringField(pos = 1, len = 1, fil = '*', ali = RIGHT) private String strF;
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getDeclaredField("strF"));
        assertEquals("strF", des.name);
        assertEquals(1, des.position);
        assertEquals(1, des.length);
        assertEquals('*', des.fill);
        assertEquals("", des.format);
        assertFalse(des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<string>", des.value);
        assertEquals(StringConverter.class, des.converter);
        assertEquals(RIGHT, des.align);
    }

    @Test
    void createCaseIntegerField() throws NoSuchFieldException {
        // @IntegerField(pos = 3, len = 3, fil = '_', ali = LEFT) private Integer intF;
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getDeclaredField("intF"));
        assertEquals("intF", des.name);
        assertEquals(3, des.position);
        assertEquals(3, des.length);
        assertEquals('_', des.fill);
        assertEquals("", des.format);
        assertFalse(des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<integer>", des.value);
        assertEquals(IntegerConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }
    @Test
    void createCaseDecimalField() throws NoSuchFieldException {
        // @DecimalField(pos = 2, len = 2, fil = '0', ali = LEFT, sep = true, dec = 4) private Double decF;
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getDeclaredField("decF"));
        assertEquals("decF", des.name);
        assertEquals(2, des.position);
        assertEquals(2, des.length);
        assertEquals('0', des.fill);
        assertEquals("", des.format);
        assertTrue(des.separator);
        assertEquals(4, des.decimals);
        assertEquals("<decimal>", des.value);
        assertEquals(DecimalConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseDateTimeField() throws NoSuchFieldException {
        // @DateTimeField(pos = 4, fmt = "ddMMyy") private Date dtmF;
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getDeclaredField("dtmF"));
        assertEquals("dtmF", des.name);
        assertEquals(4, des.position);
        assertEquals(6, des.length);
        assertEquals('\0', des.fill);
        assertEquals("ddMMyy", des.format);
        assertFalse(des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<date time>", des.value);
        assertEquals(DateTimeConverter.class, des.converter);
        assertEquals(LEFT, des.align);
    }

    @Test
    void createCaseCustomField() throws NoSuchFieldException {
        // @CustomField(pos = 5, len = 5, fil = '-', ali = RIGHT, con = CustomConverter.class) private StringBuilder ctmF;
        Descriptor des = Descriptor.create(MirnaRecordCase.class.getDeclaredField("ctmF"));
        assertEquals("ctmF", des.name);
        assertEquals(5, des.position);
        assertEquals(5, des.length);
        assertEquals('-', des.fill);
        assertEquals("", des.format);
        assertFalse(des.separator);
        assertEquals(0, des.decimals);
        assertEquals("<custom>", des.value);
        assertEquals(CustomConverter.class, des.converter);
        assertEquals(RIGHT, des.align);
    }
}