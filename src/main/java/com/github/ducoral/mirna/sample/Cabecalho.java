package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.FieldDtm;
import com.github.ducoral.mirna.FieldStr;
import com.github.ducoral.mirna.Line;

import java.util.Date;

@Line(identifier = "1")
public class Cabecalho {

    @FieldDtm(position = 1, format = "ddMMyy")
    private Date dataCriacao;

    @FieldStr(position = 2, length = 13)
    private String descricao;

    public Cabecalho() { }

    public Cabecalho(Date dataCriacao, String descricao) {
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
