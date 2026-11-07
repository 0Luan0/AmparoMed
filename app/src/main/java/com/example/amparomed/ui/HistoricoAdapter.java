package com.example.amparomed.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.R;
import com.example.amparomed.model.RegistroDose;
import com.example.amparomed.util.Formatos;

import java.util.ArrayList;
import java.util.List;

/** Historico com duas linhas: cabecalho de dia e item de dose. */
public class HistoricoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_CABECALHO = 0;
    private static final int TIPO_ITEM = 1;

    public static class Linha {
        final boolean cabecalho;
        final String rotulo;
        final RegistroDose registro;
        public Linha(String rotulo) { this.cabecalho = true; this.rotulo = rotulo; this.registro = null; }
        public Linha(RegistroDose registro) { this.cabecalho = false; this.rotulo = null; this.registro = registro; }
    }

    private final List<Linha> linhas = new ArrayList<>();

    public void setDados(List<Linha> novos) {
        linhas.clear();
        linhas.addAll(novos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) { return linhas.get(position).cabecalho ? TIPO_CABECALHO : TIPO_ITEM; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TIPO_CABECALHO)
            return new CabecalhoVH(inf.inflate(R.layout.item_historico_cabecalho, parent, false));
        return new ItemVH(inf.inflate(R.layout.item_historico, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Linha linha = linhas.get(position);
        if (linha.cabecalho) ((CabecalhoVH) holder).txt.setText(linha.rotulo);
        else ((ItemVH) holder).bind(linha.registro);
    }

    @Override
    public int getItemCount() { return linhas.size(); }

    static class CabecalhoVH extends RecyclerView.ViewHolder {
        final TextView txt;
        CabecalhoVH(@NonNull View v) { super(v); txt = v.findViewById(R.id.txt_cabecalho_dia); }
    }

    static class ItemVH extends RecyclerView.ViewHolder {
        final View raiz, frameSelo;
        final ImageView imgRelogio, imgSelo;
        final TextView txtNome, txtSub, txtStatus;
        ItemVH(@NonNull View v) {
            super(v);
            raiz = v.findViewById(R.id.raiz_hist);
            imgRelogio = v.findViewById(R.id.img_relogio);
            frameSelo = v.findViewById(R.id.frame_selo);
            imgSelo = v.findViewById(R.id.img_selo);
            txtNome = v.findViewById(R.id.txt_hist_nome);
            txtSub = v.findViewById(R.id.txt_hist_sub);
            txtStatus = v.findViewById(R.id.txt_hist_status);
        }
        void bind(RegistroDose r) {
            boolean tomado = r.isTomado();
            int corStatus = ContextCompat.getColor(itemView.getContext(),
                    tomado ? R.color.verde_principal : R.color.vermelho);
            raiz.setBackgroundResource(tomado ? R.drawable.bg_card_tomado : R.drawable.bg_card_nao_tomado);
            imgRelogio.setImageTintList(ColorStateList.valueOf(corStatus));
            frameSelo.setBackgroundResource(tomado ? R.drawable.bg_selo_ok : R.drawable.bg_selo_nao);
            imgSelo.setImageResource(tomado ? R.drawable.ic_check : R.drawable.ic_x);
            txtNome.setText(r.getMedicamentoNome());
            txtSub.setText(r.getDoseTexto() + " · " + Formatos.hora(r.getDataHora().toLocalTime()));
            txtStatus.setText(tomado ? "Tomado" : "Não tomado");
            txtStatus.setTextColor(corStatus);
        }
    }
}
