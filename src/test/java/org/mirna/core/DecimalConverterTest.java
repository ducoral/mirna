package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.DecimalField;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalConverterTest {

    static class MirnaRecordCase {
        @DecimalField(position = 0, length = 12, fill = '0') Double fieldCase1;
        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', decimals = 3) Double fieldCase2;
        @DecimalField(position = 0, length = 12, separator = ',', decimals = 4) Double fieldCase3;
        @DecimalField(position = 0, length = 12, fill = '0', align = Align.LEFT, decimals = 0) Double fieldCase4;
        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', align = Align.LEFT, decimals = 1) Double fieldCase5;
        @DecimalField(position = 0, length = 12, separator = ',', align = Align.LEFT) Double fieldCase6;
    }

    DecimalConverter converter(String field) {
        return new DecimalConverter(new Mapping(getField(field)));
    }

    Field getField(String field) {
        try {
            return MirnaRecordCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    @Test
    void toText() {
        assertEquals("000012345678", converter("fieldCase1").toText(123456.789));
        assertEquals("00123456.789", converter("fieldCase2").toText(123456.789));
        assertEquals(" 123456,7890", converter("fieldCase3").toText(123456.789));
        assertEquals("123456000000", converter("fieldCase4").toText(123456.789));
        assertEquals("123456.70000", converter("fieldCase5").toText(123456.789));
        assertEquals("123456,78   ", converter("fieldCase6").toText(123456.789));
    }

    @Test
    void fromText() {
       assertEquals(new BigDecimal("123456.78"), converter("fieldCase1").fromText("000012345678"));
       assertEquals(new BigDecimal("123456.789"), converter("fieldCase2").fromText("00123456.789"));
       assertEquals(new BigDecimal("123456.7890"), converter("fieldCase3").fromText(" 123456,7890"));
       assertEquals(new BigDecimal("123456"), converter("fieldCase4").fromText("123456000000"));
       assertEquals(new BigDecimal("123456.7"), converter("fieldCase5").fromText("123456.70000"));
       assertEquals(new BigDecimal("123456.78"), converter("fieldCase6").fromText("123456,78   "));
    }
}