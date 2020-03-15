package org.mirna.converters;

import org.junit.jupiter.api.Test;
import org.mirna.Align;
import org.mirna.annotations.DecimalField;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalConverterTest {

    private static class MirnaRecordCase {
        @DecimalField(position = 0, length = 12, fill = '0') Double fieldCase1;
        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', decimals = 3) Double fieldCase2;
        @DecimalField(position = 0, length = 12, separator = ',', decimals = 4) Double fieldCase3;
        @DecimalField(position = 0, length = 12, fill = '0', align = Align.LEFT, decimals = 0) Double fieldCase4;
        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', align = Align.LEFT, decimals = 1) Double fieldCase5;
        @DecimalField(position = 0, length = 12, separator = ',', align = Align.LEFT) Double fieldCase6;
    }

    private static DecimalConverter converter(String field) throws NoSuchFieldException {
        return new DecimalConverter(MirnaRecordCase.class.getDeclaredField(field));
    }

    @Test
    void toText() throws NoSuchFieldException {
        assertEquals("000012345678", converter("fieldCase1").toText(123456.789));
        assertEquals("00123456.789", converter("fieldCase2").toText(123456.789));
        assertEquals(" 123456,7890", converter("fieldCase3").toText(123456.789));
        assertEquals("123456000000", converter("fieldCase4").toText(123456.789));
        assertEquals("123456.70000", converter("fieldCase5").toText(123456.789));
        assertEquals("123456,78   ", converter("fieldCase6").toText(123456.789));
    }

    @Test
    void fromText() throws NoSuchFieldException {
       assertEquals(new BigDecimal("123456.78"), converter("fieldCase1").fromText("000012345678"));
       assertEquals(new BigDecimal("123456.789"), converter("fieldCase2").fromText("00123456.789"));
       assertEquals(new BigDecimal("123456.7890"), converter("fieldCase3").fromText(" 123456,7890"));
       assertEquals(new BigDecimal("123456"), converter("fieldCase4").fromText("123456000000"));
       assertEquals(new BigDecimal("123456.7"), converter("fieldCase5").fromText("123456.70000"));
       assertEquals(new BigDecimal("123456.78"), converter("fieldCase6").fromText("123456,78   "));
    }
}