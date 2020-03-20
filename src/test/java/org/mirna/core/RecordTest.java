package org.mirna.core;

import org.junit.jupiter.api.Test;
import org.mirna.annotations.MirnaRecord;
import org.mirna.annotations.StringField;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordTest {

    @MirnaRecord(identifier = "id")
    public static class MirnaRecordValidCase {
        @StringField(position = 1, length = 10)
        String fieldCase1;
    }

    static class MirnaRecordInvalidCase1 {
    }

    static class MirnaRecordInvalidCase2 {
        @StringField(position = 1, length = 10)
        String fieldCase1;
    }

    @MirnaRecord(identifier = "id")
    static class MirnaRecordInvalidCase3 {
    }

    @MirnaRecord(identifier = "id")
    static class MirnaRecordInvalidCase4 {
        @StringField(position = 1, length = 10)
        Integer fieldCase1;
    }


    @MirnaRecord(identifier = "id")
    static class MirnaRecordInvalidCase5 {
        @StringField(position = 1, length = 10)
        String fieldCase1;

        @StringField(position = 1, length = 5)
        String fieldCase2;
    }

    @MirnaRecord(identifier = "id")
    static class MirnaRecordInvalidCase6 {
        @StringField(position = 1, length = 10)
        String fieldCase1;

        @StringField(position = 3, length = 5)
        String fieldCase2;
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> new Record(MirnaRecordValidCase.class));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase1.class),
                Strs.MSG_MISSING_CONFIGURATION.format(MirnaRecordInvalidCase1.class));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase2.class),
                Strs.MSG_ANNOTATION_NOT_PRESENT.format(
                        MirnaRecord.class.getName(),
                        MirnaRecordInvalidCase2.class.getName()));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase3.class),
                Strs.MSG_MISSING_FIELD_CONFIG.format(MirnaRecordInvalidCase3.class.getName()));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase4.class),
                Strs.MSG_INVALID_FIELD_TYPE.format("fieldCase1", StringField.class.getName()));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase5.class),
                Strs.MSG_DUPLICATE_POSITION_CONFIG.format(2, "fieldCase2"));

        assertThrows(
                MirnaException.class,
                () -> new Record(MirnaRecordInvalidCase6.class),
                Strs.MSG_MISSING_POSITION_CONFIG.format(2, "fieldCase2"));
    }
}