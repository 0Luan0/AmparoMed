package com.example.amparomed.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.amparomed.model.Medicamento;

import java.util.List;

@Dao
public interface MedicamentoDao {
    @Query("SELECT * FROM medicamento")
    List<Medicamento> listar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Medicamento m);

    @Query("DELETE FROM medicamento WHERE id = :id")
    void deletar(long id);
}
