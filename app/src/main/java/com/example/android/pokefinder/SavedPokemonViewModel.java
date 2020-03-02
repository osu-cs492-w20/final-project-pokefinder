package com.example.android.pokefinder;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.SavedPokemonRepository;

import java.util.List;

public class SavedPokemonViewModel extends AndroidViewModel {

    private SavedPokemonRepository mRepository;

    private LiveData<List<Pokemon>> mAllPokemon;

    public SavedPokemonViewModel (Application application) {
        super(application);
        mRepository = new SavedPokemonRepository(application);
        mAllPokemon = mRepository.getAllPokemon();
    }

    LiveData<List<Pokemon>> getAllPokemon() { return mAllPokemon; }

    public void loadPokemonResults() {
        mRepository.queryAllPokemon();
    }

    public void delete(Pokemon pokemon) {mRepository.deletePokemon(pokemon);}

    public void insert(Pokemon pokemon) { mRepository.insertPokemon(pokemon); }
}
