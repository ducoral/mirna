package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordIdentifierTest {

    @MirnaRecord(identifier = "ident1")
    static class MirnaRecordCase1 {
    }

    @MirnaRecord(identifier = "ident2")
    static class MirnaRecordCase2 {
    }

    @MirnaRecord(identifier = "ident3")
    static class MirnaRecordCase3 {
    }

    @Test
    void identifier() {
        assertEquals("ident1", new Record(MirnaRecordCase1.class).identifier());
        assertEquals("ident2", new Record(MirnaRecordCase2.class).identifier());
        assertEquals("ident3", new Record(MirnaRecordCase3.class).identifier());
    }
}