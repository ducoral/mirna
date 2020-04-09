package com.github.ducoral.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.github.ducoral.mirna.Rule.match;

public class RuleMatchTest {

    @Line(identifier = "ident1")
    static class LineCase1 { }

    @Line(identifier = "other")
    static class LineCase2 { }

    @Line(identifier = "123")
    static class LineCase3 { }

    @Test
    void testMatch() {
        assertTrue(match(LineCase1.class, "ident1string"));
        assertFalse(match(LineCase1.class, "identstring"));

        assertTrue(match(LineCase2.class, "otherstring"));
        assertFalse(match(LineCase2.class, "iotherstring"));

        assertTrue(match(LineCase3.class, "123string"));
        assertFalse(match(LineCase3.class, "1123identstring"));
   }
}
