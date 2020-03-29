package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mirna.Utils.match;

public class UtilsMatchTest {

    @MirnaRecord(identifier = "ident1")
    static class MirnaRecordCase1 { }

    @MirnaRecord(identifier = "other")
    static class MirnaRecordCase2 { }

    @MirnaRecord(identifier = "123")
    static class MirnaRecordCase3 { }

    @Test
    void testMatch() {
        assertTrue(match(MirnaRecordCase1.class, "ident1string"));
        assertFalse(match(MirnaRecordCase1.class, "identstring"));

        assertTrue(match(MirnaRecordCase2.class, "otherstring"));
        assertFalse(match(MirnaRecordCase2.class, "iotherstring"));

        assertTrue(match(MirnaRecordCase3.class, "123string"));
        assertFalse(match(MirnaRecordCase3.class, "1123identstring"));
   }
}
