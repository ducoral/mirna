package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Document;
import com.github.ducoral.mirna.Footer;
import com.github.ducoral.mirna.Header;
import com.github.ducoral.mirna.Item;

import java.util.List;

@Document
public class Remessa {

    @Header
    private Cabecalho cabecalho;

    @Item
    private List<Titulo> titulos;

    @Footer
    private Rodape rodape;

    public Remessa() {
    }

    public Remessa(Cabecalho cabecalho, List<Titulo> titulos, Rodape rodape) {
        this.cabecalho = cabecalho;
        this.titulos = titulos;
        this.rodape = rodape;
    }

    public Cabecalho getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(Cabecalho cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<Titulo> getTitulos() {
        return titulos;
    }

    public void setTitulos(List<Titulo> titulos) {
        this.titulos = titulos;
    }

    public Rodape getRodape() {
        return rodape;
    }

    public void setRodape(Rodape rodape) {
        this.rodape = rodape;
    }
}
