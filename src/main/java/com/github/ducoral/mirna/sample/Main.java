package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Mirna;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {

MyDocument myDocument = new MyDocument(
    new HeaderLine("header", 123),
    Arrays.asList(
            new DetailLine("str1", 10, BigDecimal.valueOf(1.23)),
            new DetailLine("str2", 20, BigDecimal.valueOf(4.56)),
            new DetailLine("str3", 30, BigDecimal.valueOf(7.89))),
    new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), "footer", 456)
);


        Mirna.writeDocument(myDocument, new PrintWriter(System.out));

    }
}
