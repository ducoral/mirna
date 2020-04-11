package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Document;
import com.github.ducoral.mirna.Footer;
import com.github.ducoral.mirna.Header;
import com.github.ducoral.mirna.Item;

import java.util.List;

@Document
public class MyComplexDocument {

    @Header
    private HeaderLine header;

    @Item(order = 1)
    private List<WithSubItemLine> itemsWithDetails;

    @Item(order = 2)
    private List<AnotherLine> anotherLines;

    @Footer
    private FooterLine footer;

    public MyComplexDocument() { }

    public MyComplexDocument(
            HeaderLine header,
            List<WithSubItemLine> itemsWithDetails,
            List<AnotherLine> anotherLines,
            FooterLine footer) {
        this.header = header;
        this.itemsWithDetails = itemsWithDetails;
        this.anotherLines = anotherLines;
        this.footer = footer;
    }

    public HeaderLine getHeader() {
        return header;
    }

    public void setHeader(HeaderLine header) {
        this.header = header;
    }

    public List<WithSubItemLine> getItemsWithDetails() {
        return itemsWithDetails;
    }

    public void setItemsWithDetails(List<WithSubItemLine> itemsWithDetails) {
        this.itemsWithDetails = itemsWithDetails;
    }

    public FooterLine getFooter() {
        return footer;
    }

    public void setFooter(FooterLine footer) {
        this.footer = footer;
    }

    public List<AnotherLine> getAnotherLines() {
        return anotherLines;
    }

    public void setAnotherLines(List<AnotherLine> anotherLines) {
        this.anotherLines = anotherLines;
    }

    @Override
    public String toString() {
        StringBuilder docStr = new StringBuilder(getClass().getSimpleName())
                .append('\n')
                .append(header.toString())
                .append('\n');
        for (WithSubItemLine item : itemsWithDetails)
            docStr.append(item.toString()).append('\n');
        for (AnotherLine item : anotherLines)
            docStr.append(item.toString()).append('\n');
        return docStr.append(footer.toString())
                .append('\n')
                .toString();
    }
}
