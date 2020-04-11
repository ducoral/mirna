package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Mirna;

import java.awt.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== toTextExample() =====================================");
        toTextExample();

        System.out.println("=== writeDocumentExample() ==============================");
        writeDocumentExample();

        System.out.println("=== fromTextExample() ===================================");
        fromTextExample();

        System.out.println("=== readDocumentExample() ===============================");
        readDocumentExample();

        System.out.println("=== toTextComplexExample() ==============================");
        toTextComplexExample();

        System.out.println("=== fromTextComplexExample() ============================");
        fromTextComplexExample();

        System.out.println("=== Mirna.report(MyDocument.class) ======================");
        Mirna.report(MyDocument.class);

        System.out.println("=== Mirna.report(MyComplexDocument.class) ===============");
        Mirna.report(MyComplexDocument.class);
    }

    private static void toTextExample() {
        System.out.println(Mirna.toText(myDocumentObj()));
    }

    private static void writeDocumentExample() {
        StringWriter writer = new StringWriter();
        Mirna.writeDocument(myDocumentObj(), writer);
        System.out.println(writer.toString());
    }

    private static void fromTextExample() {
        MyDocument document = Mirna.fromText(MyDocument.class, myDocumentTxt());
        System.out.println(document);
    }

    private static void readDocumentExample() {
        MyDocument document = Mirna.readDocument(MyDocument.class, new StringReader(myDocumentTxt()));
        System.out.println(document);
    }

    private static void toTextComplexExample() {
        System.out.println(Mirna.toText(myComplexDocumentObj()));
    }

    private static void fromTextComplexExample() {
        MyComplexDocument complexDocument = Mirna.fromText(MyComplexDocument.class, myComplexDocumentTxt());
        System.out.println(complexDocument);
    }

    private static String myDocumentTxt() {
        return
            "Hheader        00123\n" +
            "Dstr1000100000000123\n" +
            "Dstr2000200000000456\n" +
            "Dstr3000300000000789\n" +
            "F10042020  255:0:255\n";
    }

    private static MyDocument myDocumentObj() {
        return new MyDocument(
                new HeaderLine("header", 123),
                Arrays.asList(
                        new DetailLine("str1", 10, BigDecimal.valueOf(1.23)),
                        new DetailLine("str2", 20, BigDecimal.valueOf(4.56)),
                        new DetailLine("str3", 30, BigDecimal.valueOf(7.89))),
                new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), Color.MAGENTA));
    }

    private static String myComplexDocumentTxt() {
        return
            "Hheader        00123\n" +
            "Sitem1            10\n" +
            "Dsub1000100000000123\n" +
            "Dsub2000200000000456\n" +
            "Dsub3000300000000789\n" +
            "Sitem2            20\n" +
            "Dsub4000400000000123\n" +
            "Dsub5000500000000456\n" +
            "Dsub6000600000000789\n" +
            "A20200411   123.4560\n" +
            "Iitem1**1000456,7890\n" +
            "A20200412    34.4500\n" +
            "Iitem2**2000056,7800\n" +
            "A20200413   987.6543\n" +
            "Iitem3**3000555,3330\n" +
            "F10042020  255:0:255\n";
    }

    private static MyComplexDocument myComplexDocumentObj() {
        return new MyComplexDocument(
                new HeaderLine("header", 123),
                Arrays.asList(
                        new WithSubItemLine(
                                "item1", 10, Arrays.asList(
                                new DetailLine("sub1", 10, BigDecimal.valueOf(1.23)),
                                new DetailLine("sub2", 20, BigDecimal.valueOf(4.56)),
                                new DetailLine("sub3", 30, BigDecimal.valueOf(7.89)))),
                        new WithSubItemLine(
                                "item2", 20, Arrays.asList(
                                new DetailLine("sub4", 40, BigDecimal.valueOf(1.23)),
                                new DetailLine("sub5", 50, BigDecimal.valueOf(4.56)),
                                new DetailLine("sub6", 60, BigDecimal.valueOf(7.89))))),
                Arrays.asList(
                        new AnotherLine(
                                new GregorianCalendar(2020, Calendar.APRIL, 11).getTime(),
                                BigDecimal.valueOf(123.456),
                                new ItemLine("item1", 100, BigDecimal.valueOf(456.789))),
                        new AnotherLine(
                                new GregorianCalendar(2020, Calendar.APRIL, 12).getTime(),
                                BigDecimal.valueOf(34.45),
                                new ItemLine("item2", 200, BigDecimal.valueOf(56.78))),
                        new AnotherLine(
                                new GregorianCalendar(2020, Calendar.APRIL, 13).getTime(),
                                BigDecimal.valueOf(987.6543),
                                new ItemLine("item3", 300, BigDecimal.valueOf(555.333)))),
                new FooterLine(new GregorianCalendar(2020, Calendar.APRIL, 10).getTime(), Color.MAGENTA));
    }
}
