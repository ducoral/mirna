package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Mirna;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main1(String[] args) {

        Mirna.report(MyDocument.class);
    }

    public static void main3(String[] args) {
        String text =
            "Hheader        00123\n" +
            "Dstr1000100000000123\n" +
            "Dstr2000200000000456\n" +
            "Dstr3000300000000789\n" +
            "F10042020  255:0:255\n";

        MyDocument myDocFromText = Mirna.readDocument(MyDocument.class, new StringReader(text));
    }

    public static void main(String[] args) {
        MyDocument myDocument = new MyDocument(
                new HeaderLine("header", 123),
                Arrays.asList(
                        new DetailLine("str1", 10, BigDecimal.valueOf(1.23)),
                        new DetailLine("str2", 20, BigDecimal.valueOf(4.56)),
                        new DetailLine("str3", 30, BigDecimal.valueOf(7.89))),
                new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), Color.MAGENTA)
        );

        Mirna.writeDocument(myDocument, new PrintWriter(System.out));
   }
}
