package com.example.amparomed.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Paciente;
import com.example.amparomed.util.Constantes;
import com.example.amparomed.util.InsetsHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.format.DateTimeFormatter;

/** Aba Pessoas (tela 10). */
public class PessoasFragment extends Fragment implements ContatoAdapter.Acao {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private TextView segPaciente, segCuidadores;
    private View grupoPaciente, grupoCuidadores;
    private TextView txtIni, txtNome, txtIdade, txtNasc, txtCpf;
    private ChipGroup chipsAlergias, chipsDoencas;
    private ContatoAdapter adapter;
    private boolean segmentoPaciente = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pessoas, container, false);
        InsetsHelper.aplicarTopo(v.findViewById(R.id.barra_topo));
        ((TextView) v.findViewById(R.id.txt_titulo_topo)).setText("Pessoas");
        v.findViewById(R.id.btn_voltar).setOnClickListener(x ->
                ((MainActivity) requireActivity()).irParaAba(R.id.nav_inicio));

        segPaciente = v.findViewById(R.id.seg_paciente);
        segCuidadores = v.findViewById(R.id.seg_cuidadores);
        grupoPaciente = v.findViewById(R.id.grupo_paciente);
        grupoCuidadores = v.findViewById(R.id.grupo_cuidadores);
        txtIni = v.findViewById(R.id.txt_iniciais_pac);
        txtNome = v.findViewById(R.id.txt_nome_pac);
        txtIdade = v.findViewById(R.id.txt_idade_pac);
        txtNasc = v.findViewById(R.id.txt_nascimento);
        txtCpf = v.findViewById(R.id.txt_cpf_pac);
        chipsAlergias = v.findViewById(R.id.chips_alergias);
        chipsDoencas = v.findViewById(R.id.chips_doencas);

        RecyclerView lista = v.findViewById(R.id.lista_cuidadores);
        adapter = new ContatoAdapter(this);
        lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        lista.setAdapter(adapter);

        segPaciente.setOnClickListener(x -> mostrarSegmento(true));
        segCuidadores.setOnClickListener(x -> mostrarSegmento(false));
        v.findViewById(R.id.btn_editar_pac).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), FormPacienteActivity.class)));
        v.findViewById(R.id.btn_add_cuidador).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), FormCuidadorActivity.class)));

        mostrarSegmento(segmentoPaciente);
        return v;
    }

    @Override
    public void onResume() { super.onResume(); recarregar(); }

    private void mostrarSegmento(boolean paciente) {
        segmentoPaciente = paciente;
        segPaciente.setSelected(paciente);
        segCuidadores.setSelected(!paciente);
        grupoPaciente.setVisibility(paciente ? View.VISIBLE : View.GONE);
        grupoCuidadores.setVisibility(paciente ? View.GONE : View.VISIBLE);
    }

    private void recarregar() {
        AmparoRepository repo = AmparoRepository.get();
        Paciente p = repo.getPaciente();
        if (p != null) {
            txtIni.setText(iniciais(p.getNome()));
            txtNome.setText(p.getNome());
            txtIdade.setText(p.idade() + " anos");
            txtNasc.setText(p.getDataNascimento().format(DATA));
            txtCpf.setText(p.getCpf());
            preencherTags(chipsAlergias, p.getAlergias(), R.color.vermelho_claro, R.color.vermelho);
            preencherTags(chipsDoencas, p.getDoencas(), R.color.pessego_claro, R.color.pessego);
        }
        adapter.setDados(repo.getCuidadores());
    }

    private void preencherTags(ChipGroup grupo, java.util.List<String> textos, int bgRes, int txtRes) {
        grupo.removeAllViews();
        for (String t : textos) {
            Chip chip = new Chip(requireContext());
            chip.setText(t);
            chip.setClickable(false);
            chip.setCheckable(false);
            chip.setChipStrokeWidth(0);
            chip.setEnsureMinTouchTargetSize(false);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), bgRes)));
            chip.setTextColor(ContextCompat.getColor(requireContext(), txtRes));
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            grupo.addView(chip);
        }
    }

    private String iniciais(String nome) {
        if (nome == null || nome.trim().isEmpty()) return "";
        String[] partes = nome.trim().split("\\s+");
        String ini = partes[0].substring(0, 1);
        if (partes.length > 1) ini += partes[partes.length - 1].substring(0, 1);
        return ini.toUpperCase();
    }

    @Override
    public void aoAbrir(Cuidador c) {
        Intent i = new Intent(requireContext(), PerfilCuidadorActivity.class);
        i.putExtra(Constantes.EXTRA_CUIDADOR_ID, c.getId());
        startActivity(i);
    }

    @Override
    public void aoLigar(Cuidador c) { Dialogos.mostrarLigacao(requireContext(), c.getNome(), c.getTelefone(), false); }
}
