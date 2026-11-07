package com.example.amparomed.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.amparomed.util.Formatos;

/** Um familiar ou cuidador / contato (tabela "cuidador"). */
@Entity(tableName = "cuidador")
public class Cuidador {

    @PrimaryKey
    private long id;
    private String nome;
    private int idade;
    private Vinculo vinculo;
    private String parentesco;
    private String email;
    private String telefone;
    private String cpf;

    public Cuidador(long id, String nome, int idade, Vinculo vinculo, String parentesco,
                    String email, String telefone, String cpf) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.vinculo = vinculo;
        this.parentesco = parentesco;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public String rotuloVinculo() {
        if (vinculo == Vinculo.FAMILIAR) {
            return (parentesco == null || parentesco.trim().isEmpty()) ? "Familiar" : parentesco;
        }
        return "Cuidador(a)";
    }

    public String iniciais() {
        if (nome == null || nome.trim().isEmpty()) return "";
        String[] partes = nome.trim().split("\\s+");
        String ini = partes[0].substring(0, 1);
        if (partes.length > 1) ini += partes[partes.length - 1].substring(0, 1);
        return ini.toUpperCase(Formatos.PT);
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public Vinculo getVinculo() { return vinculo; }
    public void setVinculo(Vinculo v) { this.vinculo = v; }
    public String getParentesco() { return parentesco; }
    public void setParentesco(String p) { this.parentesco = p; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String t) { this.telefone = t; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setId(long id) { this.id = id; }
}
