package com.example.amparomed.data.db;

import androidx.room.TypeConverter;

import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Vinculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Conversores do Room: tipos que o SQLite nao guarda direto (datas, enums,
 * listas) viram String e voltam. Datas como ISO; listas unidas por "|".
 */
public class Conversores {

    @TypeConverter public static String fromLocalDate(LocalDate d) { return d == null ? null : d.toString(); }
    @TypeConverter public static LocalDate toLocalDate(String s) { return s == null ? null : LocalDate.parse(s); }

    @TypeConverter public static String fromLocalTime(LocalTime t) { return t == null ? null : t.toString(); }
    @TypeConverter public static LocalTime toLocalTime(String s) { return s == null ? null : LocalTime.parse(s); }

    @TypeConverter public static String fromLocalDateTime(LocalDateTime dt) { return dt == null ? null : dt.toString(); }
    @TypeConverter public static LocalDateTime toLocalDateTime(String s) { return s == null ? null : LocalDateTime.parse(s); }

    @TypeConverter public static String fromForma(FormaFarmaceutica f) { return f == null ? null : f.name(); }
    @TypeConverter public static FormaFarmaceutica toForma(String s) { return s == null ? null : FormaFarmaceutica.valueOf(s); }

    @TypeConverter public static String fromVinculo(Vinculo v) { return v == null ? null : v.name(); }
    @TypeConverter public static Vinculo toVinculo(String s) { return s == null ? null : Vinculo.valueOf(s); }

    @TypeConverter
    public static String fromLista(List<String> lista) {
        return (lista == null) ? null : String.join("|", lista);
    }

    @TypeConverter
    public static List<String> toLista(String s) {
        if (s == null || s.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(s.split("\\|")));
    }
}
