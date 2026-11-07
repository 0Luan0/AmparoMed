package com.example.amparomed.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.amparomed.util.Formatos;

/** Um medicamento do estoque (tabela "medicamento"). */
@Entity(tableName = "medicamento")
public class Medicamento {

    @PrimaryKey
    private long id;
    private String nome;
    private String dosagem;
    private FormaFarmaceutica forma;
    private String tipo;
    private double quantidadeEstoque;

    public Medicamento(long id, String nome, String dosagem, FormaFarmaceutica forma,
                       String tipo, double quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.dosagem = dosagem;
        this.forma = forma;
        this.tipo = tipo;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String unidadeCurta() {
        return forma == FormaFarmaceutica.LIQUIDA ? "ml" : "cps";
    }

    public String unidadeDose(double q) {
        if (forma == FormaFarmaceutica.LIQUIDA) {
            return Formatos.numero(q) + " ml";
        }
        String base = tipo.toLowerCase(Formatos.PT);
        String plural = (q > 1) ? base + "s" : base;
        return Formatos.numero(q) + " " + plural;
    }

    public String nomeCompleto() {
        if (dosagem == null || dosagem.trim().isEmpty()) {
            return nome;
        }
        return nome + " " + dosagem;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDosagem() { return dosagem; }
    public void setDosagem(String dosagem) { this.dosagem = dosagem; }
    public FormaFarmaceutica getForma() { return forma; }
    public void setForma(FormaFarmaceutica forma) { this.forma = forma; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(double q) { this.quantidadeEstoque = q; }
    public void setId(long id) { this.id = id; }
}
