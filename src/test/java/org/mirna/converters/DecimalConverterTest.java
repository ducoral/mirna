package org.mirna.converters;

import org.junit.jupiter.api.Test;
import org.mirna.Align;
import org.mirna.annotations.DecimalField;

class DecimalConverterTest {

    private static class MirnaRecordCase {
        @DecimalField(position = 0, length = 10, fill = '0') Double fieldCase1;
        @DecimalField(position = 0, length = 10, fill = '0', separator = '.', align = Align.LEFT) Double fieldCase2;
        @DecimalField(position = 0, length = 10, separator = ',') Double fieldCase3;
    }

    @Test
    void toText() {

    }

    @Test
    void fromText() {

    }

}