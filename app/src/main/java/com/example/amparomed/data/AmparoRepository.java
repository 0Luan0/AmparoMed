package com.example.amparomed.data;

import android.content.Context;

import com.example.amparomed.data.db.AppDatabase;
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.model.Ocorrencia;
import com.example.amparomed.model.Paciente;
import com.example.amparomed.model.RegistroDose;
import com.example.amparomed.model.StatusDose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Repositorio unico (singleton). Mantem os dados em memoria (cache) para a UI
 * continuar sincrona, mas agora PERSISTE tudo num banco SQLite (Room):
 *  - ao abrir, carrega do banco; se o banco estiver vazio, semeia (SeedData);
 *  - cada alteracao e gravada no banco na hora (write-through).
 * Assim os dados sobrevivem ao fechar o app. Se nenhum Context for fornecido
 * (init), cai no modo so-memoria (seed a cada abertura) como fallback seguro.
 */
public class AmparoRepository {

    private static AmparoRepository instancia;

    /** Inicializa com banco (chamado uma vez pela Application). */
    public static synchronized void init(Context ctx) {
        if (instancia == null) {
            instancia = new AmparoRepository();
            instancia.db = AppDatabase.get(ctx);
            instancia.carregar();
        }
    }

    /** Acesso ao repositorio. Se init() nao rodou, usa so memoria (fallback). */
    public static synchronized AmparoRepository get() {
        if (instancia == null) {
            instancia = new AmparoRepository();
            instancia.carregar();
        }
        return instancia;
    }

    private AppDatabase db; // null = modo so-memoria

    private final List<Medicamento> medicamentos = new ArrayList<>();
    private final List<Lembrete> lembretes = new ArrayList<>();
    private final List<RegistroDose> registros = new ArrayList<>();
    private final List<Cuidador> cuidadores = new ArrayList<>();
    private Paciente paciente;

    private long proximoIdMedicamento = 1;
    private long proximoIdLembrete = 1;
    private long proximoIdCuidador = 1;
    private long proximoIdRegistro = 1;

    private AmparoRepository() { }

    /** Carrega do banco; semeia se vazio. Sem banco, semeia so na memoria. */
    private void carregar() {
        if (db == null) {
            SeedData.popular(this);
            return;
        }
        boolean vazio = db.medicamentoDao().listar().isEmpty();
        if (vazio) {
            SeedData.popular(this); // add*/gravarRegistro ja gravam no banco
        } else {
            medicamentos.addAll(db.medicamentoDao().listar());
            lembretes.addAll(db.lembreteDao().listar());
            registros.addAll(db.registroDao().listar());
            cuidadores.addAll(db.cuidadorDao().listar());
            paciente = db.pacienteDao().obter();
            recalcularContadores();
        }
    }

    // Garante que novos ids nao colidam com os ja existentes no banco.
    private void recalcularContadores() {
        for (Medicamento m : medicamentos) proximoIdMedicamento = Math.max(proximoIdMedicamento, m.getId() + 1);
        for (Lembrete l : lembretes) proximoIdLembrete = Math.max(proximoIdLembrete, l.getId() + 1);
        for (Cuidador c : cuidadores) proximoIdCuidador = Math.max(proximoIdCuidador, c.getId() + 1);
        for (RegistroDose r : registros) proximoIdRegistro = Math.max(proximoIdRegistro, r.getId() + 1);
    }

    // ---------- Medicamentos ----------
    public List<Medicamento> getMedicamentos() { return medicamentos; }

    public Medicamento getMedicamento(long id) {
        for (Medicamento m : medicamentos) if (m.getId() == id) return m;
        return null;
    }

    public Medicamento addMedicamento(Medicamento m) {
        if (m.getId() <= 0) m.setId(proximoIdMedicamento++);
        else proximoIdMedicamento = Math.max(proximoIdMedicamento, m.getId() + 1);
        medicamentos.add(m);
        if (db != null) db.medicamentoDao().inserir(m);
        return m;
    }

    public void removerMedicamento(long id) {
        medicamentos.removeIf(m -> m.getId() == id);
        lembretes.removeIf(l -> l.getMedicamentoId() == id);
        if (db != null) {
            db.medicamentoDao().deletar(id);
            db.lembreteDao().deletarPorMedicamento(id);
        }
    }

    public void reporEstoque(long medicamentoId, double quantidade) {
        Medicamento m = getMedicamento(medicamentoId);
        if (m != null) {
            m.setQuantidadeEstoque(m.getQuantidadeEstoque() + quantidade);
            if (db != null) db.medicamentoDao().inserir(m);
        }
    }

    // ---------- Lembretes ----------
    public List<Lembrete> getLembretes() { return lembretes; }

    public Lembrete getLembrete(long id) {
        for (Lembrete l : lembretes) if (l.getId() == id) return l;
        return null;
    }

    public List<Lembrete> lembretesDoMedicamento(long medicamentoId) {
        List<Lembrete> res = new ArrayList<>();
        for (Lembrete l : lembretes) if (l.getMedicamentoId() == medicamentoId) res.add(l);
        return res;
    }

    public Lembrete addLembrete(Lembrete l) {
        if (l.getId() <= 0) l.setId(proximoIdLembrete++);
        else proximoIdLembrete = Math.max(proximoIdLembrete, l.getId() + 1);
        lembretes.add(l);
        if (db != null) db.lembreteDao().inserir(l);
        return l;
    }

    public void removerLembrete(long id) {
        lembretes.removeIf(l -> l.getId() == id);
        if (db != null) db.lembreteDao().deletar(id);
    }

    // ---------- Cuidadores ----------
    public List<Cuidador> getCuidadores() { return cuidadores; }

    public Cuidador getCuidador(long id) {
        for (Cuidador c : cuidadores) if (c.getId() == id) return c;
        return null;
    }

    public Cuidador addCuidador(Cuidador c) {
        if (c.getId() <= 0) c.setId(proximoIdCuidador++);
        else proximoIdCuidador = Math.max(proximoIdCuidador, c.getId() + 1);
        cuidadores.add(c);
        if (db != null) db.cuidadorDao().inserir(c);
        return c;
    }

    public void atualizarCuidador(Cuidador c) {
        for (int i = 0; i < cuidadores.size(); i++) {
            if (cuidadores.get(i).getId() == c.getId()) { cuidadores.set(i, c); break; }
        }
        if (db != null) db.cuidadorDao().inserir(c);
    }

    public void removerCuidador(long id) {
        cuidadores.removeIf(c -> c.getId() == id);
        if (db != null) db.cuidadorDao().deletar(id);
    }

    // ---------- Paciente ----------
    public Paciente getPaciente() { return paciente; }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        if (paciente != null) {
            paciente.setId(1); // singleton
            if (db != null) db.pacienteDao().inserir(paciente);
        }
    }

    // ---------- Ocorrencias (calculadas) ----------
    public List<Ocorrencia> ocorrenciasDoDia(LocalDate dia) {
        List<Ocorrencia> res = new ArrayList<>();
        for (Lembrete l : lembretes) {
            if (!l.ativoEm(dia)) continue;
            Medicamento m = getMedicamento(l.getMedicamentoId());
            if (m == null) continue;
            for (LocalTime h : l.horariosDoDia()) {
                LocalDateTime dt = LocalDateTime.of(dia, h);
                res.add(new Ocorrencia(l, m, dt, statusDe(l.getId(), dt)));
            }
        }
        res.sort(Comparator.comparing(Ocorrencia::getDataHora));
        return res;
    }

    private StatusDose statusDe(long lembreteId, LocalDateTime dt) {
        RegistroDose r = registroDe(lembreteId, dt);
        if (r == null) return StatusDose.PENDENTE;
        return r.isTomado() ? StatusDose.TOMADO : StatusDose.NAO_TOMADO;
    }

    private RegistroDose registroDe(long lembreteId, LocalDateTime dt) {
        for (RegistroDose r : registros)
            if (r.getLembreteId() == lembreteId && r.getDataHora().isEqual(dt)) return r;
        return null;
    }

    public Ocorrencia proximaPendente() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();
        List<Ocorrencia> deHoje = ocorrenciasDoDia(hoje);
        for (Ocorrencia o : deHoje)
            if (o.getStatus() == StatusDose.PENDENTE && !o.getDataHora().isBefore(agora)) return o;
        for (Ocorrencia o : deHoje)
            if (o.getStatus() == StatusDose.PENDENTE) return o;
        List<Ocorrencia> amanha = ocorrenciasDoDia(hoje.plusDays(1));
        return amanha.isEmpty() ? null : amanha.get(0);
    }

    public LocalDateTime proximoHorarioDepois(Lembrete l, LocalDateTime t) {
        LocalDate base = t.toLocalDate();
        for (int i = 0; i <= 60; i++) {
            LocalDate d = base.plusDays(i);
            if (!l.ativoEm(d)) continue;
            for (LocalTime h : l.horariosDoDia()) {
                LocalDateTime dt = LocalDateTime.of(d, h);
                if (dt.isAfter(t)) return dt;
            }
        }
        return null;
    }

    public boolean confirmarDose(Ocorrencia o) {
        Medicamento m = o.getMedicamento();
        double novo = Math.max(0, m.getQuantidadeEstoque() - o.getLembrete().getQuantidadePorDose());
        m.setQuantidadeEstoque(novo);
        if (db != null) db.medicamentoDao().inserir(m); // persiste a baixa de estoque
        gravarRegistro(o.getLembrete().getId(), m.nomeCompleto(),
                m.unidadeDose(o.getLembrete().getQuantidadePorDose()), o.getDataHora(), true);
        return novo == 0;
    }

    public void rejeitarDose(Ocorrencia o) {
        Medicamento m = o.getMedicamento();
        gravarRegistro(o.getLembrete().getId(), m.nomeCompleto(),
                m.unidadeDose(o.getLembrete().getQuantidadePorDose()), o.getDataHora(), false);
    }

    void gravarRegistro(long lembreteId, String medicamentoNome, String doseTexto,
                        LocalDateTime dataHora, boolean tomado) {
        Iterator<RegistroDose> it = registros.iterator();
        while (it.hasNext()) {
            RegistroDose r = it.next();
            if (r.getLembreteId() == lembreteId && r.getDataHora().isEqual(dataHora)) it.remove();
        }
        if (db != null) db.registroDao().deletarPorLembreteEData(lembreteId, dataHora.toString());
        RegistroDose novo = new RegistroDose(proximoIdRegistro++, lembreteId, medicamentoNome, doseTexto, dataHora, tomado);
        registros.add(novo);
        if (db != null) db.registroDao().inserir(novo);
    }

    public List<RegistroDose> registrosEntre(LocalDate de, LocalDate ate) {
        List<RegistroDose> res = new ArrayList<>();
        for (RegistroDose r : registros) {
            LocalDate d = r.getDataHora().toLocalDate();
            if (!d.isBefore(de) && !d.isAfter(ate)) res.add(r);
        }
        res.sort((a, b) -> b.getDataHora().compareTo(a.getDataHora()));
        return res;
    }
}
