package com.example.android.pokefinder;

import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.PokemonRepository;
import com.example.android.pokefinder.data.Status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class PokemonViewModel extends ViewModel {
    private PokemonRepository mRepository;
    private LiveData<Pokemon> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public PokemonViewModel() {
        mRepository = new PokemonRepository();
        mSearchResults = mRepository.getPokemon();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void resetStatus(){mRepository.resetStatus();}

    public void loadSearchResults(String query) {
        mRepository.loadPokemon(query);
    }

    public LiveData<Pokemon> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}