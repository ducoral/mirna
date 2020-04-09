package com.github.ducoral.mirna;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MirnaDocumentTest {

    // TODO testar tratamento de valores null em objetos e string vazia em textos.

    @Line(identifier = "H")
    static class HeaderCase {

        @FieldInt(position = 1, length = 5)
        int fieldCase1;

        @FieldStr(position = 2, length = 15)
        String fieldCase2;

        public HeaderCase() {
        }

        public HeaderCase(int fieldCase1, String fieldCase2) {
            this.fieldCase1 = fieldCase1;
            this.fieldCase2 = fieldCase2;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final HeaderCase HEADER_CASE_OBJ_1 = new HeaderCase(10, "text");
    static final HeaderCase HEADER_CASE_OBJ_2 = new HeaderCase(20, "string");
    //                                                1         2
    //                                       123456789012345678901
    static final String HEADER_CASE_STR_1 = "H   10text           \n";
    static final String HEADER_CASE_STR_2 = "H   20string         \n";

    private void assertHeaderCase(HeaderCase expected, HeaderCase tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertEquals(expected.fieldCase2, tested.fieldCase2);
    }

    @Line(identifier = "F")
    static class FooterCase {

        @FieldStr(position = 1, length = 20, align = Align.RIGHT, fill = '*')
        String fieldCase1;

        public FooterCase() {
        }

        public FooterCase(String fieldCase1) {
            this.fieldCase1 = fieldCase1;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final FooterCase FOOTER_CASE_OBJ_1 = new FooterCase("footer case one");
    static final FooterCase FOOTER_CASE_OBJ_2 = new FooterCase("footer case two");
    //                                                1         2
    //                                       123456789012345678901
    static final String FOOTER_CASE_STR_1 = "F*****footer case one\n";
    static final String FOOTER_CASE_STR_2 = "F*****footer case two\n";

    private void assertFooterCase(FooterCase expected, FooterCase tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
    }

    @Line(identifier = "1")
    static class ItemCase1 {

        @FieldInt(position = 1, length = 5)
        int fieldCase1;

        @FieldDtm(position = 2, format = "ddMMyy")
        Date fieldCase2;

        @FieldStr(position = 3, length = 9, fill = '+')
        String fieldCase3;

        public ItemCase1() {
        }

        public ItemCase1(int fieldCase1, Date fieldCase2, String fieldCase3) {
            this.fieldCase1 = fieldCase1;
            this.fieldCase2 = fieldCase2;
            this.fieldCase3 = fieldCase3;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static Date date(int day) {
        return new GregorianCalendar(2020, Calendar.APRIL, day).getTime();
    }

    static final ItemCase1 ITEM_CASE_1_OBJ_1 = new ItemCase1(15, date(6), "text");
    static final ItemCase1 ITEM_CASE_1_OBJ_2 = new ItemCase1(30, date(7), "example");
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_1_STR_1 = "1   15060420text+++++\n";
    static final String ITEM_CASE_1_STR_2 = "1   30070420example++\n";

    private void assertItemCase1(ItemCase1 itemExpected, ItemCase1 itemTested) {
        assertEquals(itemExpected.fieldCase1, itemTested.fieldCase1);
        assertEquals(itemExpected.fieldCase2, itemTested.fieldCase2);
        assertEquals(itemExpected.fieldCase3, itemTested.fieldCase3);
    }

    @Line(identifier = "2")
    static class ItemCase2 {

        @FieldStr(position = 1, length = 15)
        String fieldCase1;

        @FieldDec(position = 2, length = 5)
        BigDecimal fieldCase2;

        public ItemCase2() {
        }

        public ItemCase2(String fieldCase1, BigDecimal fieldCase2) {
            this.fieldCase1 = fieldCase1;
            this.fieldCase2 = fieldCase2;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final ItemCase2 ITEM_CASE_2_OBJ_1 = new ItemCase2("str1", new BigDecimal("12.34"));
    static final ItemCase2 ITEM_CASE_2_OBJ_2 = new ItemCase2("str2", new BigDecimal("56.78"));
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_2_STR_1 = "2str1            1234\n";
    static final String ITEM_CASE_2_STR_2 = "2str2            5678\n";

    private void assertItemCase2(ItemCase2 expected, ItemCase2 tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertEquals(expected.fieldCase2, tested.fieldCase2);
    }

    @Line(identifier = "3")
    static class ItemCase3 {

        @FieldInt(position = 1, length = 5)
        int fieldCase1;

        @FieldStr(position = 2, length = 15, fill = '#')
        String fieldCase2;

        public ItemCase3() {
        }

        public ItemCase3(int fieldCase1, String fieldCase2) {
            this.fieldCase1 = fieldCase1;
            this.fieldCase2 = fieldCase2;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final ItemCase3 ITEM_CASE_3_OBJ_1 = new ItemCase3(100, "text one");
    static final ItemCase3 ITEM_CASE_3_OBJ_2 = new ItemCase3(200, "text two");
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_3_STR_1 = "3  100text one#######\n";
    static final String ITEM_CASE_3_STR_2 = "3  200text two#######\n";

    private void assertItemCase3(ItemCase3 expected, ItemCase3 tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertEquals(expected.fieldCase2, tested.fieldCase2);
    }

    @Line(identifier = "4")
    static class ItemCase4 {

        @FieldStr(position = 1, length = 20, fill = '%')
        String fieldCase1;

        @Item
        ItemCase3 itemCase3;

        public ItemCase4() {
        }

        public ItemCase4(String fieldCase1, ItemCase3 itemCase3) {
            this.fieldCase1 = fieldCase1;
            this.itemCase3 = itemCase3;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final ItemCase4 ITEM_CASE_4_OBJ_1 = new ItemCase4("bla bla bla", ITEM_CASE_3_OBJ_1);
    static final ItemCase4 ITEM_CASE_4_OBJ_2 = new ItemCase4("blu blu blu", ITEM_CASE_3_OBJ_2);
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_4_STR_1 = "4bla bla bla%%%%%%%%%\n";
    static final String ITEM_CASE_4_STR_2 = "4blu blu blu%%%%%%%%%\n";

    void assertItemCase4(ItemCase4 expected, ItemCase4 tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertItemCase3(expected.itemCase3, tested.itemCase3);
    }

    @Line(identifier = "5")
    static class ItemCase5 {

        @FieldStr(position = 1, length = 20, fill = '%')
        String fieldCase1;

        @Item
        List<ItemCase3> itemsCase3;

        public ItemCase5() {
        }

        public ItemCase5(String fieldCase1, List<ItemCase3> itemsCase3) {
            this.fieldCase1 = fieldCase1;
            this.itemsCase3 = itemsCase3;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final ItemCase5 ITEM_CASE_5_OBJ_1 = new ItemCase5("bua bua buu", Arrays.asList(ITEM_CASE_3_OBJ_1, ITEM_CASE_3_OBJ_1));
    static final ItemCase5 ITEM_CASE_5_OBJ_2 = new ItemCase5("blew bron", Arrays.asList(ITEM_CASE_3_OBJ_2, ITEM_CASE_3_OBJ_2));
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_5_STR_1 = "5bua bua buu%%%%%%%%%\n";
    static final String ITEM_CASE_5_STR_2 = "5blew bron%%%%%%%%%%%\n";

    void assertItemCase5(ItemCase5 expected, ItemCase5 tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertEquals(expected.itemsCase3.size(), tested.itemsCase3.size());
        for (int index = 0; index < expected.itemsCase3.size(); index++)
            assertItemCase3(expected.itemsCase3.get(index), tested.itemsCase3.get(index));
    }

    @Line(identifier = "6")
    static class ItemCase6 {

        @FieldStr(position = 1, length = 20, fill = '%')
        String fieldCase1;

        @Item
        ItemCase5 itemCase1;

        public ItemCase6() {
        }

        public ItemCase6(String fieldCase1, ItemCase5 itemCase1) {
            this.fieldCase1 = fieldCase1;
            this.itemCase1 = itemCase1;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    static final ItemCase6 ITEM_CASE_6_OBJ_1 = new ItemCase6("item case 6", ITEM_CASE_5_OBJ_1);
    static final ItemCase6 ITEM_CASE_6_OBJ_2 = new ItemCase6("6 case item", ITEM_CASE_5_OBJ_2);
    //                                                1         2
    //                                       123456789012345678901
    static final String ITEM_CASE_6_STR_1 = "6item case 6%%%%%%%%%\n";
    static final String ITEM_CASE_6_STR_2 = "66 case item%%%%%%%%%\n";

    void assertItemCase6(ItemCase6 expected, ItemCase6 tested) {
        assertEquals(expected.fieldCase1, tested.fieldCase1);
        assertItemCase5(expected.itemCase1, tested.itemCase1);
    }

    @Document
    static class DocumentCase1 {

        @Header
        HeaderCase header;

        @Item
        ItemCase1 itemCase1;

        @Footer
        FooterCase footer;

        public DocumentCase1() {
        }

        public DocumentCase1(HeaderCase header, ItemCase1 itemCase1, FooterCase footer) {
            this.header = header;
            this.itemCase1 = itemCase1;
            this.footer = footer;
        }
    }

    @Test
    void testDocumentCase1ToText() {
        String expected = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                FOOTER_CASE_STR_1;
        DocumentCase1 document = new DocumentCase1(HEADER_CASE_OBJ_1, ITEM_CASE_1_OBJ_1, FOOTER_CASE_OBJ_1);
        assertEquals(expected, Mirna.toText(document));

        expected = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                FOOTER_CASE_STR_2;
        document = new DocumentCase1(HEADER_CASE_OBJ_2, ITEM_CASE_1_OBJ_2, FOOTER_CASE_OBJ_2);
        assertEquals(expected, Mirna.toText(document));
    }

    @Test
    void testDocumentCase1FromText() {
        DocumentCase1 expected = new DocumentCase1(HEADER_CASE_OBJ_1, ITEM_CASE_1_OBJ_1, FOOTER_CASE_OBJ_1);
        String text = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                FOOTER_CASE_STR_1;
        assertDocumentCase1(expected, Mirna.fromText(DocumentCase1.class, text));

        expected = new DocumentCase1(HEADER_CASE_OBJ_2, ITEM_CASE_1_OBJ_2, FOOTER_CASE_OBJ_2);
        text = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                FOOTER_CASE_STR_2;
        assertDocumentCase1(expected, Mirna.fromText(DocumentCase1.class, text));
    }

    void assertDocumentCase1(DocumentCase1 expected, DocumentCase1 tested) {
        assertHeaderCase(expected.header, tested.header);
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertFooterCase(expected.footer, tested.footer);
    }

    @Test
    void testDocumentCase1FromTextException() {
        final String text1 = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                FOOTER_CASE_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase1.class, text1),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));

        final String text2 = HEADER_CASE_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_1_STR_2 +
                FOOTER_CASE_STR_2;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase1.class, text2),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_3_STR_2));
    }

    @Document
    static class DocumentCase2 {

        @Header
        HeaderCase header;

        @Item(order = 3)
        ItemCase3 itemCase3;

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        ItemCase2 itemCase2;

        @Footer
        FooterCase footer;

        public DocumentCase2() {
        }

        public DocumentCase2(HeaderCase header, ItemCase1 item1, ItemCase2 item2, ItemCase3 item3, FooterCase footer) {
            this.header = header;
            this.itemCase1 = item1;
            this.itemCase2 = item2;
            this.itemCase3 = item3;
            this.footer = footer;
        }
    }

    @Test
    void testDocumentCase2ToText() {
        String expected = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_3_STR_1 +
                FOOTER_CASE_STR_1;
        DocumentCase2 document = new DocumentCase2(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_2_OBJ_1,
                ITEM_CASE_3_OBJ_1,
                FOOTER_CASE_OBJ_1
        );
        assertEquals(expected, Mirna.toText(document));

        expected = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        document = new DocumentCase2(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_2_OBJ_2,
                ITEM_CASE_3_OBJ_2,
                FOOTER_CASE_OBJ_2
        );
        assertEquals(expected, Mirna.toText(document));
    }

    @Test
    void testDocumentCase2FromText() {
        DocumentCase2 expected = new DocumentCase2(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_2_OBJ_1,
                ITEM_CASE_3_OBJ_1,
                FOOTER_CASE_OBJ_1
        );
        String text = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_3_STR_1 +
                FOOTER_CASE_STR_1;
        assertDocumentCase2(expected, Mirna.fromText(DocumentCase2.class, text));

        expected = new DocumentCase2(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_2_OBJ_2,
                ITEM_CASE_3_OBJ_2,
                FOOTER_CASE_OBJ_2
        );
        text = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        assertDocumentCase2(expected, Mirna.fromText(DocumentCase2.class, text));
    }

    void assertDocumentCase2(DocumentCase2 expected, DocumentCase2 tested) {
        assertHeaderCase(expected.header, tested.header);
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertItemCase2(expected.itemCase2, tested.itemCase2);
        assertItemCase3(expected.itemCase3, tested.itemCase3);
        assertFooterCase(expected.footer, tested.footer);
    }

    @Test
    void testDocumentCase2FromTextException() {
        final String text1 = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_2_STR_1 +
                FOOTER_CASE_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase2.class, text1),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));

        final String text2 = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase2.class, text2),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_3_STR_2));

    }

    @Document
    static class DocumentCase3 {

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        List<ItemCase2> itemsCase2;

        @Item(order = 3)
        ItemCase3 itemCase3;

        public DocumentCase3() {
        }

        public DocumentCase3(ItemCase1 item1, List<ItemCase2> items2, ItemCase3 item3) {
            this.itemCase1 = item1;
            this.itemsCase2 = items2;
            this.itemCase3 = item3;
        }
    }

    @Test
    void testDocumentCase3ToText() {
        String expected = ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_1;
        DocumentCase3 document = new DocumentCase3(
                ITEM_CASE_1_OBJ_1,
                Arrays.asList(ITEM_CASE_2_OBJ_1, ITEM_CASE_2_OBJ_2, ITEM_CASE_2_OBJ_1, ITEM_CASE_2_OBJ_2),
                ITEM_CASE_3_OBJ_1);
        assertEquals(expected, Mirna.toText(document));
    }

    @Test
    void testDocumentCase3FromText() {
        DocumentCase3 expected = new DocumentCase3(
                ITEM_CASE_1_OBJ_1,
                Arrays.asList(ITEM_CASE_2_OBJ_1, ITEM_CASE_2_OBJ_2, ITEM_CASE_2_OBJ_1, ITEM_CASE_2_OBJ_2),
                ITEM_CASE_3_OBJ_1);
        String text = ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_1;
        DocumentCase3 tested = Mirna.fromText(DocumentCase3.class, text);

        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertItemCase2(expected.itemsCase2.get(0), tested.itemsCase2.get(0));
        assertItemCase2(expected.itemsCase2.get(1), tested.itemsCase2.get(1));
        assertItemCase2(expected.itemsCase2.get(2), tested.itemsCase2.get(2));
        assertItemCase2(expected.itemsCase2.get(3), tested.itemsCase2.get(3));
        assertItemCase3(expected.itemCase3, tested.itemCase3);
    }

    @Test
    void testDocumentCase3FromTextException() {
        final String text1 = ITEM_CASE_1_STR_1 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase3.class, text1),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_4_STR_1));
    }

    @Document
    static class DocumentCase4 {

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        ItemCase4 itemCase4;

        public DocumentCase4() {
        }

        public DocumentCase4(ItemCase1 itemCase1, ItemCase4 itemCase4) {
            this.itemCase1 = itemCase1;
            this.itemCase4 = itemCase4;
        }
    }

    @Test
    void testDocumentCase4ToText() {
        String expected = ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1;
        DocumentCase4 document = new DocumentCase4(
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_4_OBJ_1);
        assertEquals(expected, Mirna.toText(document));

        expected = ITEM_CASE_1_STR_2 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2;
        document = new DocumentCase4(
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_4_OBJ_2);
        assertEquals(expected, Mirna.toText(document));
    }

    @Test
    void testDocumentCase4FromText() {
        DocumentCase4 expected = new DocumentCase4(
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_4_OBJ_1);
        String text = ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1;
        assertDocumentCase4(expected, Mirna.fromText(DocumentCase4.class, text));

        text = ITEM_CASE_1_STR_2 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2;
        expected = new DocumentCase4(
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_4_OBJ_2);
        assertDocumentCase4(expected, Mirna.fromText(DocumentCase4.class, text));
    }

    void assertDocumentCase4(DocumentCase4 expected, DocumentCase4 tested) {
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertItemCase4(expected.itemCase4, tested.itemCase4);
        assertItemCase3(expected.itemCase4.itemCase3, tested.itemCase4.itemCase3);
    }

    @Test
    void testDocumentCase4FromTextException() {
        final String text1 = ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_2_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase4.class, text1),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));

        final String text2 = ITEM_CASE_1_STR_2 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_2_STR_2 +
                ITEM_CASE_3_STR_2;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase4.class, text2),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_2));
    }

    @Document
    static class DocumentCase5 {

        @Header
        HeaderCase header;

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        ItemCase5 itemCase5;

        @Footer
        FooterCase footer;

        public DocumentCase5() {
        }

        public DocumentCase5(HeaderCase header, ItemCase1 itemCase1, ItemCase5 itemCase5, FooterCase footer) {
            this.header = header;
            this.itemCase1 = itemCase1;
            this.itemCase5 = itemCase5;
            this.footer = footer;
        }
    }

    @Test
    void testDocumentCase5ToText() {
        String expected = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                FOOTER_CASE_STR_1;
        DocumentCase5 document = new DocumentCase5(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_5_OBJ_1,
                FOOTER_CASE_OBJ_1);
        assertEquals(expected, Mirna.toText(document));

        expected = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        document = new DocumentCase5(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_5_OBJ_2,
                FOOTER_CASE_OBJ_2);
        assertEquals(expected, Mirna.toText(document));
    }

    @Test
    void testDocumentCase5FromText() {
        String text = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                FOOTER_CASE_STR_1;
        DocumentCase5 expected = new DocumentCase5(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                ITEM_CASE_5_OBJ_1,
                FOOTER_CASE_OBJ_1);
        assertDocumentCase5(expected, Mirna.fromText(DocumentCase5.class, text));

        text = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        expected = new DocumentCase5(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                ITEM_CASE_5_OBJ_2,
                FOOTER_CASE_OBJ_2);
        assertDocumentCase5(expected, Mirna.fromText(DocumentCase5.class, text));
    }

    void assertDocumentCase5(DocumentCase5 expected, DocumentCase5 tested) {
        assertHeaderCase(expected.header, tested.header);
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertItemCase5(expected.itemCase5, tested.itemCase5);
        assertItemCase3(expected.itemCase5.itemsCase3.get(0), tested.itemCase5.itemsCase3.get(0));
        assertItemCase3(expected.itemCase5.itemsCase3.get(1), tested.itemCase5.itemsCase3.get(1));
        assertFooterCase(expected.footer, expected.footer);
    }

    @Test
    void testDocumentCase5FromTextException() {
        final String text1 = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_2_STR_1 +
                FOOTER_CASE_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase5.class, text1),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));

        final String text2 = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase5.class, text2),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_3_STR_2));
    }

    @Document
    static class DocumentCase6 {

        @Header
        HeaderCase header;

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        List<ItemCase4> itemsCase4;

        @Footer
        FooterCase footer;

        public DocumentCase6() {
        }

        public DocumentCase6(HeaderCase header, ItemCase1 itemCase1, List<ItemCase4> itemsCase4, FooterCase footer) {
            this.header = header;
            this.itemCase1 = itemCase1;
            this.itemsCase4 = itemsCase4;
            this.footer = footer;
        }
    }

    @Test
    void testDocumentCase6ToText() {
        String expected = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_1;

        DocumentCase6 tested = new DocumentCase6(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                Arrays.asList(
                        ITEM_CASE_4_OBJ_1,
                        ITEM_CASE_4_OBJ_2,
                        ITEM_CASE_4_OBJ_1,
                        ITEM_CASE_4_OBJ_2),
                FOOTER_CASE_OBJ_1);
        assertEquals(expected, Mirna.toText(tested));
    }

    @Test
    void testDocumentCase6FromText() {
        DocumentCase6 expected = new DocumentCase6(
                HEADER_CASE_OBJ_1,
                ITEM_CASE_1_OBJ_1,
                Arrays.asList(
                        ITEM_CASE_4_OBJ_1,
                        ITEM_CASE_4_OBJ_2,
                        ITEM_CASE_4_OBJ_1,
                        ITEM_CASE_4_OBJ_2),
                FOOTER_CASE_OBJ_1);

        String text = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_1;

        DocumentCase6 tested = Mirna.fromText(DocumentCase6.class, text);

        assertHeaderCase(expected.header, tested.header);
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertEquals(expected.itemsCase4.size(), tested.itemsCase4.size());
        for (int index = 0; index < expected.itemsCase4.size(); index++)
            assertItemCase4(expected.itemsCase4.get(index), tested.itemsCase4.get(index));
        assertFooterCase(expected.footer, tested.footer);
    }

    @Test
    void testDocumentCase6FromTextException() {
        final String text = HEADER_CASE_STR_1 +
                ITEM_CASE_1_STR_1 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_4_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_4_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_2_STR_1 +
                FOOTER_CASE_STR_1;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase6.class, text),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));
    }

    @Document
    static class DocumentCase7 {

        @Header
        HeaderCase header;

        @Item(order = 1)
        ItemCase1 itemCase1;

        @Item(order = 2)
        List<ItemCase5> itemsCase5;

        @Footer
        FooterCase footer;

        public DocumentCase7() {
        }

        public DocumentCase7(HeaderCase header, ItemCase1 itemCase1, List<ItemCase5> itemsCase5, FooterCase footer) {
            this.header = header;
            this.itemCase1 = itemCase1;
            this.itemsCase5 = itemsCase5;
            this.footer = footer;
        }
    }

    @Test
    void testDocumentCase7ToText() {
        String expected = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;

        DocumentCase7 tested = new DocumentCase7(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                Arrays.asList(ITEM_CASE_5_OBJ_1, ITEM_CASE_5_OBJ_2, ITEM_CASE_5_OBJ_1, ITEM_CASE_5_OBJ_2),
                FOOTER_CASE_OBJ_2
        );

        assertEquals(expected, Mirna.toText(tested));
    }

    @Test
    void testDocumentCase7FromText() {
        DocumentCase7 expected = new DocumentCase7(
                HEADER_CASE_OBJ_2,
                ITEM_CASE_1_OBJ_2,
                Arrays.asList(ITEM_CASE_5_OBJ_1, ITEM_CASE_5_OBJ_2, ITEM_CASE_5_OBJ_1, ITEM_CASE_5_OBJ_2),
                FOOTER_CASE_OBJ_2
        );

        String text = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;

        DocumentCase7 tested = Mirna.fromText(DocumentCase7.class, text);

        assertHeaderCase(expected.header, tested.header);
        assertItemCase1(expected.itemCase1, tested.itemCase1);
        assertEquals(expected.itemsCase5.size(), tested.itemsCase5.size());
        for (int index = 0; index < expected.itemsCase5.size(); index++)
            assertItemCase5(expected.itemsCase5.get(index), tested.itemsCase5.get(index));
        assertFooterCase(expected.footer, tested.footer);
    }

    @Test
    void testDocumentCase7FromTextException() {
        final String text = HEADER_CASE_STR_2 +
                ITEM_CASE_1_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_5_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_3_STR_1 +
                ITEM_CASE_5_STR_2 +
                ITEM_CASE_2_STR_1 +
                ITEM_CASE_3_STR_2 +
                ITEM_CASE_3_STR_2 +
                FOOTER_CASE_STR_2;
        assertThrows(
                Oops.class,
                () -> Mirna.fromText(DocumentCase7.class, text),
                Strs.MSG_INVALID_LINE.format(ITEM_CASE_2_STR_1));
    }
}
