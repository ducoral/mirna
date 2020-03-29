package org.mirna;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
}
