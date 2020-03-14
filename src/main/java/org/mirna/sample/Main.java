package org.mirna.sample;

import org.mirna.MirnaException;
import org.mirna.Strs;

public class Main {

    public static void main(String[] args) {
        throw new MirnaException(Strs.INVALID_PARAMETER, "teste");
    }
}
