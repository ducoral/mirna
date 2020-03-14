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
    private static class MirnaRecordForTest {
        @StringField(pos = 1, len = 1, fil = '*', ali = RIGHT)                              private String strF;
        @DecimalField(pos = 2, len = 2, fil = '0', ali = LEFT, sep = true, dec = 4)         private Double decF;
        @IntegerField(pos = 3, len = 3, fil = '_', ali = LEFT)                              private Integer intF;
        @DateTimeField(pos = 4, fmt = "ddMMyy")                                             private Date dtmF;
        @CustomField(pos = 5, len = 5, fil = '-', ali = RIGHT, con = CustomConverter.class) private StringBuilder ctmF;
    }

    @Test
    void isAnnotated() throws NoSuchFieldException {
        assertTrue(Descriptor.isAnnotated(MirnaRecordForTest.class.getDeclaredField("strF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordForTest.class.getDeclaredField("decF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordForTest.class.getDeclaredField("intF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordForTest.class.getDeclaredField("dtmF")));
        assertTrue(Descriptor.isAnnotated(MirnaRecordForTest.class.getDeclaredField("ctmF")));
    }

    @Test
    void createCaseMirnaRecord() {
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getAnnotation(MirnaRecord.class));
        assertEquals("", des.name);
        assertEquals(0, des.pos);
        assertEquals(5, des.len);
        assertEquals('\0', des.fil);
        assertEquals("", des.fmt);
        assertFalse(des.sep);
        assertEquals(0, des.dec);
        assertEquals("'ident'", des.val);
        assertNull(des.con);
        assertEquals(LEFT, des.ali);
    }

    @Test
    void createCaseStringField() throws NoSuchFieldException {
        // @StringField(pos = 1, len = 1, fil = '*', ali = RIGHT) private String strF;
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getDeclaredField("strF"));
        assertEquals("strF", des.name);
        assertEquals(1, des.pos);
        assertEquals(1, des.len);
        assertEquals('*', des.fil);
        assertEquals("", des.fmt);
        assertFalse(des.sep);
        assertEquals(0, des.dec);
        assertEquals("<string>", des.val);
        assertEquals(StringConverter.class, des.con);
        assertEquals(RIGHT, des.ali);
    }

    @Test
    void createCaseIntegerField() throws NoSuchFieldException {
        // @IntegerField(pos = 3, len = 3, fil = '_', ali = LEFT) private Integer intF;
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getDeclaredField("intF"));
        assertEquals("intF", des.name);
        assertEquals(3, des.pos);
        assertEquals(3, des.len);
        assertEquals('_', des.fil);
        assertEquals("", des.fmt);
        assertFalse(des.sep);
        assertEquals(0, des.dec);
        assertEquals("<integer>", des.val);
        assertEquals(IntegerConverter.class, des.con);
        assertEquals(LEFT, des.ali);
    }
    @Test
    void createCaseDecimalField() throws NoSuchFieldException {
        // @DecimalField(pos = 2, len = 2, fil = '0', ali = LEFT, sep = true, dec = 4) private Double decF;
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getDeclaredField("decF"));
        assertEquals("decF", des.name);
        assertEquals(2, des.pos);
        assertEquals(2, des.len);
        assertEquals('0', des.fil);
        assertEquals("", des.fmt);
        assertTrue(des.sep);
        assertEquals(4, des.dec);
        assertEquals("<decimal>", des.val);
        assertEquals(DecimalConverter.class, des.con);
        assertEquals(LEFT, des.ali);
    }

    @Test
    void createCaseDateTimeField() throws NoSuchFieldException {
        // @DateTimeField(pos = 4, fmt = "ddMMyy") private Date dtmF;
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getDeclaredField("dtmF"));
        assertEquals("dtmF", des.name);
        assertEquals(4, des.pos);
        assertEquals(6, des.len);
        assertEquals('\0', des.fil);
        assertEquals("ddMMyy", des.fmt);
        assertFalse(des.sep);
        assertEquals(0, des.dec);
        assertEquals("<date time>", des.val);
        assertEquals(DateTimeConverter.class, des.con);
        assertEquals(LEFT, des.ali);
    }

    @Test
    void createCaseCustomField() throws NoSuchFieldException {
        // @CustomField(pos = 5, len = 5, fil = '-', ali = RIGHT, con = CustomConverter.class) private StringBuilder ctmF;
        Descriptor des = Descriptor.create(MirnaRecordForTest.class.getDeclaredField("ctmF"));
        assertEquals("ctmF", des.name);
        assertEquals(5, des.pos);
        assertEquals(5, des.len);
        assertEquals('-', des.fil);
        assertEquals("", des.fmt);
        assertFalse(des.sep);
        assertEquals(0, des.dec);
        assertEquals("<custom>", des.val);
        assertEquals(CustomConverter.class, des.con);
        assertEquals(RIGHT, des.ali);
    }
}