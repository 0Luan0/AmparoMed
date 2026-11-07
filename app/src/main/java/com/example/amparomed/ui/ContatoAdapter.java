package com.example.amparomed.ui;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.R;
import com.example.amparomed.model.Cuidador;

import java.util.ArrayList;
import java.util.List;

/** Lista de contatos (aba Pessoas e Emergencia). */
public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.VH> {

    public interface Acao {
        void aoAbrir(Cuidador c);
        void aoLigar(Cuidador c);
    }

    private static final int[] AVATAR_BG = {R.drawable.bg_chip_verde, R.drawable.bg_chip_pessego, R.drawable.bg_chip_roxo};
    private static final int[] AVATAR_TXT = {R.color.verde_principal, R.color.pessego, R.color.roxo};

    private final List<Cuidador> itens = new ArrayList<>();
    private final Acao acao;

    public ContatoAdapter(Acao acao) { this.acao = acao; }

    public void setDados(List<Cuidador> novos) {
        itens.clear();
        itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contato, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Cuidador c = itens.get(position);
        int slot = position % 3;
        h.ini.setBackgroundResource(AVATAR_BG[slot]);
        h.ini.setTextColor(ContextCompat.getColor(h.itemView.getContext(), AVATAR_TXT[slot]));
        h.ini.setText(c.iniciais());

        String suf = " · " + c.rotuloVinculo();
        SpannableStringBuilder sb = new SpannableStringBuilder(c.getNome());
        int inicio = sb.length();
        sb.append(suf);
        sb.setSpan(new StyleSpan(Typeface.NORMAL), inicio, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(h.itemView.getContext(), R.color.texto_secundario)),
                inicio, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        h.nome.setText(sb);

        h.tel.setText(c.getTelefone());
        h.raiz.setOnClickListener(v -> acao.aoAbrir(c));
        h.btnLigar.setOnClickListener(v -> acao.aoLigar(c));
    }

    @Override
    public int getItemCount() { return itens.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final View raiz, btnLigar;
        final TextView ini, nome, tel;
        VH(@NonNull View v) {
            super(v);
            raiz = v.findViewById(R.id.raiz_contato);
            ini = v.findViewById(R.id.txt_contato_ini);
            nome = v.findViewById(R.id.txt_contato_nome);
            tel = v.findViewById(R.id.txt_contato_tel);
            btnLigar = v.findViewById(R.id.btn_ligar);
        }
    }
}
