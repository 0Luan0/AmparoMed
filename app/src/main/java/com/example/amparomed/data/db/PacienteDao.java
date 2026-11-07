package com.example.amparomed.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.amparomed.model.Paciente;

@Dao
public interface PacienteDao {
    @Query("SELECT * FROM paciente WHERE id = 1 LIMIT 1")
    Paciente obter();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Paciente p);
}
