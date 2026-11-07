package com.example.amparomed.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/** O paciente (unico no app; tabela "paciente" com uma linha, id=1). */
@Entity(tableName = "paciente")
public class Paciente {

    // Paciente e singleton: id fixo = 1. Fora do construtor; o Room seta via setId.
    @PrimaryKey
    private int id = 1;

    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private List<String> alergias;
    private List<String> doencas;

    public Paciente(String nome, LocalDate dataNascimento, String cpf,
                    List<String> alergias, List<String> doencas) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.alergias = (alergias != null) ? alergias : new ArrayList<>();
        this.doencas = (doencas != null) ? doencas : new ArrayList<>();
    }

    /** Idade CALCULADA a partir da data de nascimento — nunca digitada. */
    public int idade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate d) { this.dataNascimento = d; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public List<String> getAlergias() { return alergias; }
    public void setAlergias(List<String> a) { this.alergias = a; }
    public List<String> getDoencas() { return doencas; }
    public void setDoencas(List<String> d) { this.doencas = d; }
}
