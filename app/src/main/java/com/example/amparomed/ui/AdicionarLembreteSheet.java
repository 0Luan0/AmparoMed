package com.example.amparomed.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.util.Formatos;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Bottom sheet "Novo lembrete" (tela 02). */
public class AdicionarLembreteSheet extends BottomSheetDialogFragment {

    public interface OnSalvoListener { void aoSalvar(); }

    private OnSalvoListener listener;
    private List<Medicamento> medicamentos;
    private Spinner spinner;
    private TextView txtQtd, txtHorario;
    private EditText editOutro;
    private double quantidade = 1;
    private LocalTime horario = LocalTime.of(20, 0);

    public void setListener(OnSalvoListener listener) { this.listener = listener; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_adicionar_lembrete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        spinner = v.findViewById(R.id.spinner_medicamento);
        txtQtd = v.findViewById(R.id.txt_qtd);
        txtHorario = v.findViewById(R.id.txt_horario);
        editOutro = v.findViewById(R.id.edit_outro);

        medicamentos = AmparoRepository.get().getMedicamentos();
        List<String> nomes = new ArrayList<>();
        for (Medicamento m : medicamentos) nomes.add(m.nomeCompleto());
        ArrayAdapter<String> ad = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nomes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        atualizarQuantidadeInicial();
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> p, View vw, int pos, long id) { atualizarQuantidadeInicial(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> p) { }
        });

        ImageButton menos = v.findViewById(R.id.btn_qtd_menos);
        ImageButton mais = v.findViewById(R.id.btn_qtd_mais);
        menos.setOnClickListener(x -> { double passo = passoAtual(); quantidade = Math.max(passo, quantidade - passo); txtQtd.setText(Formatos.numero(quantidade)); });
        mais.setOnClickListener(x -> { quantidade += passoAtual(); txtQtd.setText(Formatos.numero(quantidade)); });

        txtHorario.setText(Formatos.hora(horario));
        v.findViewById(R.id.campo_horario).setOnClickListener(x ->
                new TimePickerDialog(requireContext(), (tp, h, m) -> {
                    horario = LocalTime.of(h, m);
                    txtHorario.setText(Formatos.hora(horario));
                }, horario.getHour(), horario.getMinute(), true).show());

        ((com.google.android.material.chip.Chip) v.findViewById(R.id.chip_24h)).setChecked(true);
        ((com.google.android.material.chip.Chip) v.findViewById(R.id.chip_indef)).setChecked(true);

        com.google.android.material.chip.ChipGroup chipsDias = v.findViewById(R.id.chips_dias);
        chipsDias.setOnCheckedStateChangeListener((group, ids) ->
                editOutro.setVisibility(group.getCheckedChipId() == R.id.chip_outro ? View.VISIBLE : View.GONE));

        v.findViewById(R.id.btn_cancelar_sheet).setOnClickListener(x -> dismiss());
        v.findViewById(R.id.btn_salvar_sheet).setOnClickListener(x -> salvar(v));
    }

    private void atualizarQuantidadeInicial() { quantidade = passoAtual(); txtQtd.setText(Formatos.numero(quantidade)); }

    private double passoAtual() {
        Medicamento m = medicamentoSelecionado();
        return (m != null && m.getForma() == FormaFarmaceutica.LIQUIDA) ? 5 : 1;
    }

    private Medicamento medicamentoSelecionado() {
        int pos = spinner.getSelectedItemPosition();
        if (pos < 0 || pos >= medicamentos.size()) return null;
        return medicamentos.get(pos);
    }

    private int intervaloSelecionado(View v) {
        int id = ((com.google.android.material.chip.ChipGroup) v.findViewById(R.id.chips_intervalo)).getCheckedChipId();
        if (id == R.id.chip_4h) return 4;
        if (id == R.id.chip_6h) return 6;
        if (id == R.id.chip_8h) return 8;
        if (id == R.id.chip_12h) return 12;
        return 24;
    }

    private void salvar(View v) {
        Medicamento m = medicamentoSelecionado();
        if (m == null) { dismiss(); return; }
        int intervalo = intervaloSelecionado(v);

        Integer duracao;
        int idDias = ((com.google.android.material.chip.ChipGroup) v.findViewById(R.id.chips_dias)).getCheckedChipId();
        if (idDias == R.id.chip_7d) duracao = 7;
        else if (idDias == R.id.chip_15d) duracao = 15;
        else if (idDias == R.id.chip_30d) duracao = 30;
        else if (idDias == R.id.chip_outro) {
            String txt = editOutro.getText().toString().trim();
            int n = txt.isEmpty() ? 0 : Integer.parseInt(txt);
            if (n <= 0) { editOutro.setError("Informe um número de dias"); return; }
            duracao = n;
        } else duracao = null;

        AmparoRepository.get().addLembrete(new Lembrete(0, m.getId(), quantidade, intervalo, horario, LocalDate.now(), duracao));
        if (listener != null) listener.aoSalvar();
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog d = (BottomSheetDialog) getDialog();
        if (d != null) {
            View bs = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bs != null) {
                BottomSheetBehavior<View> b = BottomSheetBehavior.from(bs);
                b.setState(BottomSheetBehavior.STATE_EXPANDED);
                b.setSkipCollapsed(true);
            }
        }
    }
}
