package com.example.amparomed.model;

import java.time.LocalDateTime;

/**
 * "O lembrete X no dia/hora Y". NAO e salva no repositorio: e calculada a
 * partir dos lembretes, e o status vem do RegistroDose correspondente.
 */
public class Ocorrencia {

    private final Lembrete lembrete;
    private final Medicamento medicamento;
    private final LocalDateTime dataHora;
    private StatusDose status;

    public Ocorrencia(Lembrete lembrete, Medicamento medicamento,
                      LocalDateTime dataHora, StatusDose status) {
        this.lembrete = lembrete;
        this.medicamento = medicamento;
        this.dataHora = dataHora;
        this.status = status;
    }

    public Lembrete getLembrete() { return lembrete; }
    public Medicamento getMedicamento() { return medicamento; }
    public LocalDateTime getDataHora() { return dataHora; }
    public StatusDose getStatus() { return status; }
    public void setStatus(StatusDose status) { this.status = status; }
}
