package org.mirna.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrsTest {

    @Test
    void resource() {
        Assertions.assertEquals(
                Utils.resource().getString("msg.invalid.parameter").replace("{0}", "mirna"),
                Strs.MSG_INVALID_PARAMETER.format("mirna")
        );
    }

    @Test
    void parameters() {
        assertEquals(
                "bla param1 bla param2 bla param3 bla!",
                Strs.TEST_MSG_WITH_PARAMETERS.format("param1", "param2", "param3")
        );
    }
}