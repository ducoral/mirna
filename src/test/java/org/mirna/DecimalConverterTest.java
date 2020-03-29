package org.mirna;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalConverterTest {

    static class LineCase {

        @DecimalField(position = 0, length = 12, fill = '0')
        float fieldCase1;

        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', decimals = 3)
        Float fieldCase2;

        @DecimalField(position = 0, length = 12, separator = ',', decimals = 4)
        double fieldCase3;

        @DecimalField(position = 0, length = 12, fill = '0', align = Align.LEFT, decimals = 0)
        Double fieldCase4;

        @DecimalField(position = 0, length = 12, fill = '0', separator = '.', align = Align.LEFT, decimals = 1)
        BigDecimal fieldCase5;

        @DecimalField(position = 0, length = 12, separator = ',', align = Align.LEFT)
        BigDecimal fieldCase6;
    }

    DecimalConverter converter(String field) {
        return new DecimalConverter(new Mapping(getField(field)));
    }

    Field getField(String field) {
        try {
            return LineCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    @Test
    void toText() {
        assertEquals("000012345678", converter("fieldCase1").toText( Float.parseFloat("123456.78")));
        assertEquals("00123456.780", converter("fieldCase2").toText(Float.valueOf("123456.780")));
        assertEquals(" 123456,7890", converter("fieldCase3").toText(Double.parseDouble("123456.789")));
        assertEquals("123456000000", converter("fieldCase4").toText(Double.valueOf("123456.789")));
        assertEquals("123456.70000", converter("fieldCase5").toText(new BigDecimal("123456.789")));
        assertEquals("123456,78   ", converter("fieldCase6").toText(new BigDecimal("123456.789")));
    }

    @Test
    void fromText() {
       assertEquals(Float.parseFloat("123456.78"), converter("fieldCase1").fromText("000012345678"));
       assertEquals(Float.valueOf("123456.789"), converter("fieldCase2").fromText("00123456.789"));
       assertEquals(Double.parseDouble("123456.7890"), converter("fieldCase3").fromText(" 123456,7890"));
       assertEquals(Double.valueOf("123456"), converter("fieldCase4").fromText("123456000000"));
       assertEquals(new BigDecimal("123456.7"), converter("fieldCase5").fromText("123456.70000"));
       assertEquals(new BigDecimal("123456.78"), converter("fieldCase6").fromText("123456,78   "));
    }
}