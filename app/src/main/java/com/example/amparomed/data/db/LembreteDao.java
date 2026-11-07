package com.example.amparomed.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.amparomed.model.Lembrete;

import java.util.List;

@Dao
public interface LembreteDao {
    @Query("SELECT * FROM lembrete")
    List<Lembrete> listar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Lembrete l);

    @Query("DELETE FROM lembrete WHERE id = :id")
    void deletar(long id);

    @Query("DELETE FROM lembrete WHERE medicamentoId = :medId")
    void deletarPorMedicamento(long medId);
}
