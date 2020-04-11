package com.github.ducoral.mirna;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleValidationDocumentTest {

    @Line(identifier = "line1")
    static class ValidLine1 {

        @FieldStr(position = 1, length = 5)
        String validField1;
    }

    @Line(identifier = "line2")
    static class ValidLine2 {

        @FieldInt(position = 1, length = 10)
        Integer validField1;

        @FieldDec(position = 2, length = 20)
        BigDecimal validField2;
    }

    @Line(identifier = "line3")
    static class ValidLine3 {

        @FieldStr(position = 1, length = 10)
        String validField1;
    }

    @Line(identifier = "line4")
    static class ValidLine4 {

        @FieldInt(position = 1, length = 5)
        int validField1;

        @Item
        ValidLine3 validItem1;
    }

    @Line(identifier = "line5")
    static class ValidLine5 {

        @FieldStr(position = 1, length = 10)
        String validField1;

        @Item
        ValidLine4 validItem4;

        @Item
        List<ValidLine3> validItems1;
    }

    static class DocumentAnnotationNotPresentCase {
    }

    @Test
    void validateAnnotationNotPresent() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentAnnotationNotPresentCase.class),
                Strs.MSG_ANNOTATION_NOT_PRESENT.format(
                        Strs.MSG_DOCUMENT_ANNOTATION,
                        DocumentAnnotationNotPresentCase.class.getName()));
    }

    @Document
    static class DocumentHeaderNotAllowedCase {

        @Header
        List<ValidLine1> itemsCase1;
    }

    @Test
    void validateHeaderNotAllowed() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentHeaderNotAllowedCase.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        DocumentHeaderNotAllowedCase.class.getName(),
                        Strs.MSG_HEADER_FOOTER_NOT_ALLOWED));
    }

    @Document
    static class DocumentFooterNotAllowedCase {

        @Footer
        List<ValidLine1> itemsCase1;
    }

    @Test
    void validateFooterNotAllowed() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentFooterNotAllowedCase.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        DocumentFooterNotAllowedCase.class.getName(),
                        Strs.MSG_HEADER_FOOTER_NOT_ALLOWED));
    }

    @Document
    static class DocumentDuplicateHeaderCase {

        @Header
        ValidLine1 itemCase1;

        @Item(order = 1)
        ValidLine3 itemCase3;

        @Item(order = 2)
        ValidLine4 itemCase4;

        @Header
        ValidLine2 itemCase2;
    }

    @Test
    void validateDuplicateHeaderConfig() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateHeaderCase.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        "itemCase2",
                        Strs.MSG_DUPLICATE_HEADER_CONFIG));
    }

    @Document
    static class DocumentDuplicateFooterCase {

        @Footer
        ValidLine1 itemCase1;

        @Item(order = 1)
        ValidLine3 itemCase3;

        @Item(order = 2)
        ValidLine4 itemCase4;

        @Footer
        ValidLine2 itemCase2;
    }

    @Test
    void validateDuplicateFooterConfig() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateFooterCase.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        "itemCase2",
                        Strs.MSG_DUPLICATE_FOOTER_CONFIG));
    }

    @Document
    static class DocumentMissingItemAnnotationCase {

        ValidLine1 itemCase1;
    }

    @Test
    void validateMissingItemAnnotation() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentMissingItemAnnotationCase.class),
                Strs.MSG_ANNOTATION_NOT_PRESENT.format(
                        Strs.MSG_ANY_ITEM_ANNOTATION,
                        "itemCase1"));
    }

    @Document
    static class DocumentDuplicateTypeCase1 {

        @Header
        ValidLine1 itemCase1;

        @Item
        ValidLine1 itemCase2;

    }

    @Test
    void validateDuplicateTypeCase1() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateTypeCase1.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                    ValidLine1.class,
                    Strs.MSG_DUPLICATE_TYPE_CONFIG));
    }

    @Document
    static class DocumentDuplicateTypeCase2 {

        @Header
        ValidLine1 itemCase1;

        @Item
        ValidLine2 itemCase2;

        @Footer
        ValidLine2 itemCase3;
    }

    @Test
    void validateDuplicateTypeCase2() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateTypeCase2.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        ValidLine2.class,
                        Strs.MSG_DUPLICATE_TYPE_CONFIG));
    }

    @Document
    static class DocumentDuplicateTypeCase3 {

        @Item
        ValidLine1 itemCase1;

        @Item
        List<ValidLine1> itemsCase2;
    }

    @Test
    void validateDuplicateTypeCase3() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateTypeCase3.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        ValidLine1.class,
                        Strs.MSG_DUPLICATE_TYPE_CONFIG));
    }

    @Document
    static class DocumentDuplicateTypeCase4 {

        @Item
        ValidLine3 itemCase1;

        @Item
        ValidLine4 itemCase2;
    }

    @Test
    void validateDuplicateTypeCase4() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateTypeCase4.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        ValidLine3.class,
                        Strs.MSG_DUPLICATE_TYPE_CONFIG));
    }

    @Document
    static class DocumentDuplicateTypeCase5 {

        @Item
        ValidLine4 itemCase1;

        @Item
        ValidLine5 itemCase2;
    }

    @Test
    void validateDuplicateTypeCase5() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentDuplicateTypeCase5.class),
                Strs.MSG_INVALID_CONFIGURATION.format(
                        ValidLine3.class,
                        Strs.MSG_DUPLICATE_TYPE_CONFIG));
    }

    @Document
    static class DocumentMissingDefaultConstructor {

        @Item
        final
        ValidLine4 itemCase1;

        public DocumentMissingDefaultConstructor(ValidLine4 itemCase1) {
            this.itemCase1 = itemCase1;
        }
    }

    @Test
    void testMissingDefaultConstrutor() {
        assertThrows(
                Oops.class,
                () -> Rule.validateDocument(DocumentMissingDefaultConstructor.class),
                Strs.MSG_MISSING_DEFAULT_CONSTRUCTOR.format(DocumentMissingDefaultConstructor.class.getName()));
    }

}
