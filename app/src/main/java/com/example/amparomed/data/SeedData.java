package com.example.amparomed.data;

import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.model.Ocorrencia;
import com.example.amparomed.model.Paciente;
import com.example.amparomed.model.Vinculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/** Dados de exemplo com datas relativas a hoje (a data da apresentacao e desconhecida). */
public final class SeedData {

    private SeedData() { }

    public static void popular(AmparoRepository repo) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusDays(30);

        repo.addMedicamento(new Medicamento(1, "Metformina", "500mg", FormaFarmaceutica.SOLIDA, "Comprimido", 34));
        repo.addMedicamento(new Medicamento(2, "Losartana", "50mg", FormaFarmaceutica.SOLIDA, "Comprimido", 3));
        repo.addMedicamento(new Medicamento(3, "Atorvastatina", "20mg", FormaFarmaceutica.SOLIDA, "Comprimido", 28));
        repo.addMedicamento(new Medicamento(4, "AAS", "100mg", FormaFarmaceutica.SOLIDA, "Comprimido", 15));
        repo.addMedicamento(new Medicamento(5, "Lactulose xarope", "", FormaFarmaceutica.LIQUIDA, "Xarope", 90));

        repo.addLembrete(new Lembrete(0, 1, 1, 24, LocalTime.of(8, 0), inicio, null));
        repo.addLembrete(new Lembrete(0, 2, 1, 24, LocalTime.of(12, 0), inicio, null));
        repo.addLembrete(new Lembrete(0, 3, 1, 24, LocalTime.of(20, 0), inicio, null));
        repo.addLembrete(new Lembrete(0, 4, 1, 24, LocalTime.of(20, 0), inicio, null));
        repo.addLembrete(new Lembrete(0, 5, 15, 24, LocalTime.of(21, 0), inicio, null));

        repo.setPaciente(new Paciente("Maria Clara Silva", LocalDate.of(1954, 3, 14), "123.456.789-00",
                Arrays.asList("Dipirona", "Penicilina"),
                Arrays.asList("Diabetes tipo 2", "Hipertensão", "Colesterol alto")));

        repo.addCuidador(new Cuidador(0, "João Silva", 45, Vinculo.FAMILIAR, "Filho",
                "joao.silva@email.com", "(11) 99812-4410", "321.654.987-00"));
        repo.addCuidador(new Cuidador(0, "Ana Silva", 42, Vinculo.FAMILIAR, "Filha",
                "ana.silva@email.com", "(11) 98455-0921", "456.789.123-00"));
        repo.addCuidador(new Cuidador(0, "Rosa Souza", 45, Vinculo.CUIDADOR, "",
                "rosa.souza@email.com", "(11) 98765-4321", "987.654.321-00"));

        LocalDateTime agora = LocalDateTime.now();
        for (int offset = 30; offset >= 0; offset--) {
            LocalDate dia = hoje.minusDays(offset);
            for (Ocorrencia o : repo.ocorrenciasDoDia(dia)) {
                if (offset == 0) {
                    if (o.getDataHora().isBefore(agora)) gravar(repo, o, true);
                } else {
                    gravar(repo, o, definirTomado(offset, o));
                }
            }
        }
    }

    private static boolean definirTomado(int offset, Ocorrencia o) {
        String nome = o.getMedicamento().getNome();
        LocalTime h = o.getDataHora().toLocalTime();
        if (offset == 1) {
            return !(nome.equals("Atorvastatina") && h.equals(LocalTime.of(20, 0)));
        }
        if (offset >= 2 && offset <= 6) {
            if (offset == 3 && nome.equals("AAS") && h.equals(LocalTime.of(20, 0))) return false;
            if (offset == 5 && nome.equals("Metformina") && h.equals(LocalTime.of(8, 0))) return false;
            return true;
        }
        return true;
    }

    private static void gravar(AmparoRepository repo, Ocorrencia o, boolean tomado) {
        repo.gravarRegistro(o.getLembrete().getId(), o.getMedicamento().nomeCompleto(),
                o.getMedicamento().unidadeDose(o.getLembrete().getQuantidadePorDose()),
                o.getDataHora(), tomado);
    }
}
