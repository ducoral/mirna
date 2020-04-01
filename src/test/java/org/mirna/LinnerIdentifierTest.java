package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinnerIdentifierTest {

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
        assertEquals("ident1", new Linner(LineCase1.class).identifier());
        assertEquals("ident2", new Linner(LineCase2.class).identifier());
        assertEquals("ident3", new Linner(LineCase3.class).identifier());
    }
}