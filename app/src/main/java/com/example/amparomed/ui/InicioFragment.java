package com.example.amparomed.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.data.EstoqueCalculadora;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.model.Ocorrencia;
import com.example.amparomed.model.StatusDose;
import com.example.amparomed.util.Constantes;
import com.example.amparomed.util.Formatos;
import com.example.amparomed.util.InsetsHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Aba Inicio (tela 01). */
public class InicioFragment extends Fragment
        implements LembreteAdapter.Acao, AdicionarLembreteSheet.OnSalvoListener {

    private TextView txtSaudacao, txtNome, txtRotuloDia, txtContador, txtVazio, txtAlerta;
    private RecyclerView lista;
    private View cardEstoque;
    private LembreteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        InsetsHelper.aplicarTopo(v.findViewById(R.id.cabecalho));

        txtSaudacao = v.findViewById(R.id.txt_saudacao);
        txtNome = v.findViewById(R.id.txt_nome);
        txtRotuloDia = v.findViewById(R.id.txt_rotulo_dia);
        txtContador = v.findViewById(R.id.txt_contador);
        txtVazio = v.findViewById(R.id.txt_vazio);
        txtAlerta = v.findViewById(R.id.txt_alerta);
        lista = v.findViewById(R.id.lista_hoje);
        cardEstoque = v.findViewById(R.id.card_estoque_baixo);

        adapter = new LembreteAdapter(this);
        lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        lista.setAdapter(adapter);

        v.findViewById(R.id.btn_sino).setOnClickListener(x -> abrirAlarmeProximo());
        v.findViewById(R.id.btn_adicionar).setOnClickListener(x -> abrirAdicionar());
        v.findViewById(R.id.btn_emergencia).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), EmergenciaActivity.class)));
        v.findViewById(R.id.btn_repor_alerta).setOnClickListener(x ->
                ((MainActivity) requireActivity()).irParaAba(R.id.nav_estoque));
        return v;
    }

    @Override
    public void onResume() { super.onResume(); recarregar(); }

    private void recarregar() {
        AmparoRepository repo = AmparoRepository.get();
        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        if (repo.getPaciente() != null) txtNome.setText(doisPrimeirosNomes(repo.getPaciente().getNome()));
        txtSaudacao.setText(Formatos.saudacao());
        txtRotuloDia.setText(Formatos.rotuloDia(hoje));

        List<Ocorrencia> doDia = repo.ocorrenciasDoDia(hoje);
        int tomados = 0;
        Ocorrencia proximo = null;
        for (Ocorrencia o : doDia) {
            if (o.getStatus() == StatusDose.TOMADO) tomados++;
            if (proximo == null && o.getStatus() == StatusDose.PENDENTE && !o.getDataHora().isBefore(agora)) proximo = o;
        }
        txtContador.setText(tomados + " de " + doDia.size() + " tomados");

        boolean vazio = doDia.isEmpty();
        lista.setVisibility(vazio ? View.GONE : View.VISIBLE);
        txtVazio.setVisibility(vazio ? View.VISIBLE : View.GONE);
        adapter.setDados(doDia, proximo != null ? proximo.getDataHora() : null);

        atualizarAlertaEstoque(repo);
    }

    private void atualizarAlertaEstoque(AmparoRepository repo) {
        List<Medicamento> baixos = new ArrayList<>();
        for (Medicamento m : repo.getMedicamentos()) if (EstoqueCalculadora.estoqueBaixo(m)) baixos.add(m);
        if (baixos.isEmpty()) { cardEstoque.setVisibility(View.GONE); return; }
        cardEstoque.setVisibility(View.VISIBLE);
        Medicamento p = baixos.get(0);
        if (baixos.size() == 1)
            txtAlerta.setText(p.getNome() + " acabando: " + (int) p.getQuantidadeEstoque() + " " + p.unidadeCurta());
        else
            txtAlerta.setText(p.getNome() + " e mais " + (baixos.size() - 1) + " acabando");
    }

    private void abrirAlarmeProximo() {
        Ocorrencia o = AmparoRepository.get().proximaPendente();
        if (o == null) { Toast.makeText(requireContext(), "Nenhum remédio pendente", Toast.LENGTH_SHORT).show(); return; }
        abrirAlarme(o);
    }

    private void abrirAlarme(Ocorrencia o) {
        Intent i = new Intent(requireContext(), AlarmeActivity.class);
        i.putExtra(Constantes.EXTRA_LEMBRETE_ID, o.getLembrete().getId());
        i.putExtra(Constantes.EXTRA_DATA_HORA, o.getDataHora().toString());
        startActivity(i);
    }

    private void abrirAdicionar() {
        if (AmparoRepository.get().getMedicamentos().isEmpty()) {
            Toast.makeText(requireContext(), "Cadastre um medicamento no Estoque primeiro.", Toast.LENGTH_SHORT).show();
            return;
        }
        AdicionarLembreteSheet sheet = new AdicionarLembreteSheet();
        sheet.setListener(this);
        sheet.show(getChildFragmentManager(), "add_lembrete");
    }

    @Override
    public void aoTocarPendente(Ocorrencia o) { abrirAlarme(o); }

    @Override
    public void aoRemover(Ocorrencia o) {
        View v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_remover_lembrete, null);
        ((TextView) v.findViewById(R.id.txt_dlg_nome)).setText(o.getMedicamento().nomeCompleto());
        String dose = o.getMedicamento().unidadeDose(o.getLembrete().getQuantidadePorDose());
        String hora = Formatos.hora(o.getDataHora().toLocalTime());
        String rep = o.getLembrete().getIntervaloHoras() == 24 ? "todo dia" : "a cada " + o.getLembrete().getIntervaloHoras() + "h";
        ((TextView) v.findViewById(R.id.txt_dlg_sub)).setText(dose + " · " + hora + " · " + rep);

        AlertDialog d = new MaterialAlertDialogBuilder(requireContext()).setView(v).create();
        if (d.getWindow() != null) d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        v.findViewById(R.id.btn_cancelar).setOnClickListener(x -> d.dismiss());
        v.findViewById(R.id.btn_remover).setOnClickListener(x -> {
            AmparoRepository.get().removerLembrete(o.getLembrete().getId());
            d.dismiss();
            recarregar();
            Toast.makeText(requireContext(), "Lembrete removido", Toast.LENGTH_SHORT).show();
        });
        d.show();
    }

    @Override
    public void aoSalvar() { recarregar(); }

    private String doisPrimeirosNomes(String nome) {
        if (nome == null) return "";
        String[] partes = nome.trim().split("\\s+");
        if (partes.length <= 2) return nome;
        return partes[0] + " " + partes[1];
    }
}
