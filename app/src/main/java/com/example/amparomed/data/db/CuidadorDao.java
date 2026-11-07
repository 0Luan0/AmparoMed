package com.example.amparomed.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.amparomed.model.Cuidador;

import java.util.List;

@Dao
public interface CuidadorDao {
    @Query("SELECT * FROM cuidador")
    List<Cuidador> listar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Cuidador c);

    @Query("DELETE FROM cuidador WHERE id = :id")
    void deletar(long id);
}
