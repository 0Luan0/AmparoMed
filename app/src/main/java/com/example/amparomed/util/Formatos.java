package com.example.amparomed.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Formatacoes de texto do app (datas, horas, numeros), sempre em pt-BR. */
public final class Formatos {

    private Formatos() { }

    public static final Locale PT = Locale.forLanguageTag("pt-BR");

    private static final DateTimeFormatter DIA_SEMANA = DateTimeFormatter.ofPattern("EEE", PT);
    private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("MMM", PT);
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm", PT);

    public static String rotuloDia(LocalDate d) {
        LocalDate hoje = LocalDate.now();
        String prefixo;
        if (d.isEqual(hoje)) prefixo = "HOJE";
        else if (d.isEqual(hoje.minusDays(1))) prefixo = "ONTEM";
        else prefixo = maiusculaSemPonto(d.format(DIA_SEMANA));
        return prefixo + " · " + d.getDayOfMonth() + " " + maiusculaSemPonto(d.format(MES));
    }

    public static String hora(LocalTime t) { return t.format(HORA); }

    public static String saudacao() {
        int h = LocalTime.now().getHour();
        if (h >= 5 && h <= 11) return "Bom dia";
        if (h >= 12 && h <= 17) return "Boa tarde";
        return "Boa noite";
    }

    public static String numero(double v) {
        if (!Double.isInfinite(v) && v == Math.rint(v)) return Long.toString((long) v);
        return String.valueOf(v);
    }

    private static String maiusculaSemPonto(String s) {
        return s.replace(".", "").toUpperCase(PT);
    }
}
