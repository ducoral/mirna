package org.mirna.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat();
        System.out.println(sdf.toLocalizedPattern());
        System.out.println(sdf.toPattern());
        System.out.println(new Date());
    }
}
