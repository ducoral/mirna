package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }


    @Test
    void report() {
    }
}
