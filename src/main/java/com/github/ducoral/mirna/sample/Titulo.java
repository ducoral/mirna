package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDec;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

import java.math.BigDecimal;

@Line(identifier = "2")
public class Titulo {

    @FieldStr(position = 1, length = 14)
    private String cpfCnpj;

    @FieldDec(position = 2, length = 5)
    private BigDecimal valor;

    public Titulo() { }

    public Titulo(String cpfCnpj, BigDecimal valor) {
        this.cpfCnpj = cpfCnpj;
        this.valor = valor;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
