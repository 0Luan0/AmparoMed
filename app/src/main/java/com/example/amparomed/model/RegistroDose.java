package com.example.amparomed.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

/** Uma entrada do historico (tabela "registro_dose"). */
@Entity(tableName = "registro_dose")
public class RegistroDose {

    @PrimaryKey
    private long id;
    private long lembreteId;
    private String medicamentoNome;
    private String doseTexto;
    private LocalDateTime dataHora;
    private boolean tomado;

    public RegistroDose(long id, long lembreteId, String medicamentoNome, String doseTexto,
                        LocalDateTime dataHora, boolean tomado) {
        this.id = id;
        this.lembreteId = lembreteId;
        this.medicamentoNome = medicamentoNome;
        this.doseTexto = doseTexto;
        this.dataHora = dataHora;
        this.tomado = tomado;
    }

    public long getId() { return id; }
    public long getLembreteId() { return lembreteId; }
    public String getMedicamentoNome() { return medicamentoNome; }
    public String getDoseTexto() { return doseTexto; }
    public LocalDateTime getDataHora() { return dataHora; }
    public boolean isTomado() { return tomado; }
    public void setId(long id) { this.id = id; }
}
