package com.example.amparomed.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Regra de quando tomar um medicamento (tabela "lembrete"). */
@Entity(tableName = "lembrete")
public class Lembrete {

    @PrimaryKey
    private long id;
    private long medicamentoId;
    private double quantidadePorDose;
    private int intervaloHoras;
    private LocalTime horarioInicial;
    private LocalDate dataInicio;
    private Integer duracaoDias; // null = "Nao definido" (repete para sempre)

    public Lembrete(long id, long medicamentoId, double quantidadePorDose, int intervaloHoras,
                    LocalTime horarioInicial, LocalDate dataInicio, Integer duracaoDias) {
        this.id = id;
        this.medicamentoId = medicamentoId;
        this.quantidadePorDose = quantidadePorDose;
        this.intervaloHoras = intervaloHoras;
        this.horarioInicial = horarioInicial;
        this.dataInicio = dataInicio;
        this.duracaoDias = duracaoDias;
    }

    public boolean ativoEm(LocalDate dia) {
        if (dia.isBefore(dataInicio)) return false;
        if (duracaoDias == null) return true;
        return dia.isBefore(dataInicio.plusDays(duracaoDias));
    }

    /** Horarios de um dia (aritmetica de minutos para nao virar o dia). */
    public List<LocalTime> horariosDoDia() {
        List<LocalTime> horarios = new ArrayList<>();
        int minutos = horarioInicial.getHour() * 60 + horarioInicial.getMinute();
        int passo = intervaloHoras * 60;
        while (minutos < 24 * 60) {
            horarios.add(LocalTime.of(minutos / 60, minutos % 60));
            if (passo <= 0) break;
            minutos += passo;
        }
        return horarios;
    }

    public long getId() { return id; }
    public long getMedicamentoId() { return medicamentoId; }
    public double getQuantidadePorDose() { return quantidadePorDose; }
    public int getIntervaloHoras() { return intervaloHoras; }
    public LocalTime getHorarioInicial() { return horarioInicial; }
    public LocalDate getDataInicio() { return dataInicio; }
    public Integer getDuracaoDias() { return duracaoDias; }
    public void setId(long id) { this.id = id; }
}
