package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Document;
import com.github.ducoral.mirna.Footer;
import com.github.ducoral.mirna.Header;
import com.github.ducoral.mirna.Item;

import java.util.List;

@Document
public class MyDocument {

    @Header
    private HeaderLine header;

    @Item
    private List<DetailLine> details;

    @Footer
    private FooterLine footer;

    public MyDocument() { }

    public MyDocument(HeaderLine header, List<DetailLine> details, FooterLine footer) {
        this.header = header;
        this.details = details;
        this.footer = footer;
    }

    public HeaderLine getHeader() {
        return header;
    }

    public void setHeader(HeaderLine header) {
        this.header = header;
    }

    public List<DetailLine> getDetails() {
        return details;
    }

    public void setDetails(List<DetailLine> details) {
        this.details = details;
    }

    public FooterLine getFooter() {
        return footer;
    }

    public void setFooter(FooterLine footer) {
        this.footer = footer;
    }
}
