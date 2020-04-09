package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldInt;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

@Line(identifier = "3")
public class Rodape {

    @FieldStr(position = 1, length = 14)
    private String texto;

    @FieldInt(position = 2, length = 5)
    private Integer qtdeItens;

    public Rodape() { }

    public Rodape(String texto, Integer qtdeItens) {
        this.texto = texto;
        this.qtdeItens = qtdeItens;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getQtdeItens() {
        return qtdeItens;
    }

    public void setQtdeItens(Integer qtdeItens) {
        this.qtdeItens = qtdeItens;
    }
}
