package com.example.amparomed.data;

import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Regra de negocio central do estoque — tudo deriva dos lembretes ativos. */
public final class EstoqueCalculadora {

    private EstoqueCalculadora() { }

    public static double consumoDiario(Medicamento m) {
        AmparoRepository repo = AmparoRepository.get();
        LocalDate hoje = LocalDate.now();
        double total = 0;
        for (Lembrete l : repo.lembretesDoMedicamento(m.getId()))
            if (l.ativoEm(hoje)) total += l.getQuantidadePorDose() * l.horariosDoDia().size();
        return total;
    }

    public static Double diasRestantes(Medicamento m) {
        double consumo = consumoDiario(m);
        if (consumo <= 0) return null;
        return m.getQuantidadeEstoque() / consumo;
    }

    /** Pega as PROXIMAS 3 doses futuras e soma; estoque <= soma -> baixo. */
    public static boolean estoqueBaixo(Medicamento m) {
        List<Double> proximas = proximasDoses(m, 3);
        if (proximas.isEmpty()) return false;
        double soma = 0;
        for (double d : proximas) soma += d;
        return m.getQuantidadeEstoque() <= soma;
    }

    public static int percentualBarra(Medicamento m) {
        Double dias = diasRestantes(m);
        if (dias == null) return 100;
        return (int) Math.min(100, dias / 30 * 100);
    }

    public static String textoDuracao(Medicamento m) {
        Double dias = diasRestantes(m);
        if (dias == null) return "Sem lembrete ativo";
        int n = (int) Math.floor(dias);
        if (estoqueBaixo(m)) {
            if (n <= 0) return "Acaba hoje";
            return "Acaba em " + n + (n == 1 ? " dia" : " dias");
        }
        return "Dura " + n + (n == 1 ? " dia" : " dias");
    }

    private static List<Double> proximasDoses(Medicamento m, int n) {
        AmparoRepository repo = AmparoRepository.get();
        LocalDateTime agora = LocalDateTime.now();
        List<Lembrete> lembretes = repo.lembretesDoMedicamento(m.getId());
        List<Double> doses = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        for (int i = 0; i <= 60 && doses.size() < n; i++) {
            LocalDate d = hoje.plusDays(i);
            List<Object[]> itensDoDia = new ArrayList<>();
            for (Lembrete l : lembretes) {
                if (!l.ativoEm(d)) continue;
                for (LocalTime h : l.horariosDoDia()) {
                    LocalDateTime dt = LocalDateTime.of(d, h);
                    if (dt.isAfter(agora)) itensDoDia.add(new Object[]{dt, l.getQuantidadePorDose()});
                }
            }
            itensDoDia.sort(Comparator.comparing(a -> (LocalDateTime) a[0]));
            for (Object[] item : itensDoDia) {
                if (doses.size() >= n) break;
                doses.add((Double) item[1]);
            }
        }
        return doses;
    }
}
