package com.example.amparomed.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.RegistroDose;
import com.example.amparomed.util.Formatos;
import com.example.amparomed.util.InsetsHelper;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Aba Historico (tela 06). */
public class HistoricoFragment extends Fragment {

    private HistoricoAdapter adapter;
    private ChipGroup chips;
    private TextView txtTomados, txtNao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historico, container, false);
        InsetsHelper.aplicarTopo(v.findViewById(R.id.barra_topo));
        ((TextView) v.findViewById(R.id.txt_titulo_topo)).setText("Histórico");
        v.findViewById(R.id.btn_voltar).setOnClickListener(x ->
                ((MainActivity) requireActivity()).irParaAba(R.id.nav_inicio));

        txtTomados = v.findViewById(R.id.txt_resumo_tomados);
        txtNao = v.findViewById(R.id.txt_resumo_nao);
        RecyclerView lista = v.findViewById(R.id.lista_historico);
        adapter = new HistoricoAdapter();
        lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        lista.setAdapter(adapter);

        chips = v.findViewById(R.id.chips_periodo);
        ((com.google.android.material.chip.Chip) v.findViewById(R.id.chip_7dias)).setChecked(true);
        chips.setOnCheckedStateChangeListener((g, ids) -> recarregar());
        return v;
    }

    @Override
    public void onResume() { super.onResume(); recarregar(); }

    private void recarregar() {
        LocalDate hoje = LocalDate.now();
        LocalDate de;
        int marcado = chips.getCheckedChipId();
        if (marcado == R.id.chip_hoje) de = hoje;
        else if (marcado == R.id.chip_30dias) de = hoje.minusDays(29);
        else de = hoje.minusDays(6);

        List<RegistroDose> registros = AmparoRepository.get().registrosEntre(de, hoje);
        int tomados = 0, nao = 0;
        List<HistoricoAdapter.Linha> linhas = new ArrayList<>();
        LocalDate diaAtual = null;
        for (RegistroDose r : registros) {
            if (r.isTomado()) tomados++; else nao++;
            LocalDate d = r.getDataHora().toLocalDate();
            if (!d.equals(diaAtual)) { linhas.add(new HistoricoAdapter.Linha(Formatos.rotuloDia(d))); diaAtual = d; }
            linhas.add(new HistoricoAdapter.Linha(r));
        }
        txtTomados.setText(String.valueOf(tomados));
        txtNao.setText(String.valueOf(nao));
        adapter.setDados(linhas);
    }
}
