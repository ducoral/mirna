package org.mirna;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mirna.Rule.validateLine;

class RuleValidationLineTest {

    @Line(identifier = "id")
    static
    class LineValidCase {
        @FieldStr(position = 1, length = 10)
        String fieldCase1;
    }

    static class LineInvalidCase1 {
    }

    static class LineInvalidCase2 {
        @FieldStr(position = 1, length = 10)
        String fieldCase1;
    }

    @Line(identifier = "id")
    static
    class LineInvalidCase3 {
    }

    @Line(identifier = "id")
    static
    class LineInvalidCase4 {
        @FieldStr(position = 1, length = 10)
        Integer fieldCase1;
    }


    @Line(identifier = "id")
    static
    class LineInvalidCase5 {
        @FieldStr(position = 1, length = 10)
        String fieldCase1;

        @FieldStr(position = 1, length = 5)
        String fieldCase2;
    }

    @Line(identifier = "id")
    static
    class LineInvalidCase6 {
        @FieldStr(position = 1, length = 10)
        String fieldCase1;

        @FieldStr(position = 3, length = 5)
        String fieldCase2;
    }

    @Test
    void testValidate() {
        assertDoesNotThrow(() -> validateLine(LineValidCase.class));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase1.class),
                Strs.MSG_MISSING_CONFIGURATION.format(LineInvalidCase1.class));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase2.class),
                Strs.MSG_ANNOTATION_NOT_PRESENT.format(
                        Line.class.getName(),
                        LineInvalidCase2.class.getName()));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase3.class),
                Strs.MSG_MISSING_FIELD_CONFIG.format(LineInvalidCase3.class.getName()));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase4.class),
                Strs.MSG_INVALID_FIELD_TYPE.format("fieldCase1", FieldStr.class.getName()));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase5.class),
                Strs.MSG_DUPLICATE_POSITION_CONFIG.format(2, "fieldCase2"));

        assertThrows(
                Oops.class,
                () -> validateLine(LineInvalidCase6.class),
                Strs.MSG_MISSING_POSITION_CONFIG.format(2, "fieldCase2"));
    }
}