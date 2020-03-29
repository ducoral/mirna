package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserIdentifierTest {

    @Line(identifier = "ident1")
    static class LineCase1 {
    }

    @Line(identifier = "ident2")
    static class LineCase2 {
    }

    @Line(identifier = "ident3")
    static class LineCase3 {
    }

    @Test
    void identifier() {
        assertEquals("ident1", new Parser(LineCase1.class).identifier());
        assertEquals("ident2", new Parser(LineCase2.class).identifier());
        assertEquals("ident3", new Parser(LineCase3.class).identifier());
    }
}