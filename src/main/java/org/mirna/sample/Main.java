package org.mirna.sample;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        BigDecimal big = new BigDecimal("12345678965.562").setScale(4, BigDecimal.ROUND_DOWN);
        System.out.println(big.toString());
    }
}
