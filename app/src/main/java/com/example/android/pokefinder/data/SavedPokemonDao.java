package com.example.android.pokefinder.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedPokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pokemon pokemon);

    @Query("SELECT * FROM pokemon")
    List<Pokemon> getAll();

    @Delete
    void delete(Pokemon pokemon);
}
