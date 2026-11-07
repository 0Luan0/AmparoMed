package com.example.amparomed.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.model.Paciente;
import com.example.amparomed.model.RegistroDose;

/**
 * Banco SQLite local do app, via Room. Guarda medicamentos, lembretes,
 * historico de doses, cuidadores e o paciente. allowMainThreadQueries:
 * o app e academico e o repositorio e sincrono, entao rodamos na main thread
 * (em producao usaria-se threads/coroutines).
 */
@Database(
        entities = {Medicamento.class, Lembrete.class, RegistroDose.class, Cuidador.class, Paciente.class},
        version = 1,
        exportSchema = false)
@TypeConverters(Conversores.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MedicamentoDao medicamentoDao();
    public abstract LembreteDao lembreteDao();
    public abstract RegistroDoseDao registroDao();
    public abstract CuidadorDao cuidadorDao();
    public abstract PacienteDao pacienteDao();

    private static volatile AppDatabase instancia;

    public static AppDatabase get(Context ctx) {
        if (instancia == null) {
            synchronized (AppDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                                    ctx.getApplicationContext(), AppDatabase.class, "amparomed.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instancia;
    }
}
