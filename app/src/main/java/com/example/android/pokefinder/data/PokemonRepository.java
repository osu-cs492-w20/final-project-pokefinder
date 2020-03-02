package com.example.android.pokefinder.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.pokefinder.utils.PokeUtils;

import java.util.List;


public class PokemonRepository implements LoadPokemonTask.AsyncCallback {

    private static final String TAG = PokemonRepository.class.getSimpleName();

    private MutableLiveData<Pokemon> mPokemon;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentPokemonName;
    private int mCompletedTargets;

    public PokemonRepository() {
        mPokemon = new MutableLiveData<>();
        mPokemon.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCompletedTargets = 0;
        mCurrentPokemonName = null;
    }


    public void loadPokemon(String pokemonName) {
        mCompletedTargets = 0;
        mLoadingStatus.setValue(Status.LOADING);
        if (shouldFetchPokemon(pokemonName)) {
            mCurrentPokemonName = pokemonName;
            mPokemon.setValue(null);
            String url = PokeUtils.buildPokemonURL(pokemonName);
            Log.d(TAG, "fetching new pokemon data with this URL: " + url);
            new LoadPokemonTask(url, "pokemon",  this).execute();
            String species_url = PokeUtils.buildPokemonSpeciesURL(mCurrentPokemonName);
            Log.d(TAG, "fetching new pokemon species data with this URL: " + species_url);
            new LoadPokemonTask(species_url, "pokemon-species", this).execute();
        } else {
            mLoadingStatus.setValue(Status.SUCCESS);
            Log.d(TAG, "using cached pokemon data");
        }
    }

    /*
     * Returns the LiveData object containing the forecast data.  An observer can be hooked to this
     * to react to changes in the forecast.
     */
    public LiveData<Pokemon> getPokemon() {
        return mPokemon;
    }

    /*
     * Returns the LiveData object containing the Repository's loading status.  An observer can be
     * hooked to this, e.g. to display a progress bar or error message when appropriate.
     */
    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    private boolean shouldFetchPokemon(String pokemonName) {
        if (!TextUtils.equals(pokemonName, mCurrentPokemonName) || mPokemon.getValue() == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPokemonLoadFinished(Pokemon pokemon) {
        mPokemon.setValue(pokemon);
        if (pokemon == null) {
            mLoadingStatus.setValue(Status.ERROR);
        } else {
            mCompletedTargets += 1;
            if(mCompletedTargets == 2){
                mLoadingStatus.setValue(Status.SUCCESS);
            }
        }
    }

    @Override
    public void onPokemonEvolveLoadFinished(Pokemon pokemon) {
        Pokemon tempPokemon = mPokemon.getValue();
        if (pokemon == null) {
            mLoadingStatus.setValue(Status.ERROR);
        } else {
            if (tempPokemon == null){tempPokemon = new Pokemon();}
            tempPokemon.evolves_from_id  = pokemon.id;
            tempPokemon.evolves_from = pokemon.name;
            mCompletedTargets += 1;
            if(mCompletedTargets == 2){
                mLoadingStatus.setValue(Status.SUCCESS);
            }
        }
    }

    @Override
    public void onPokemonSpeciesLoadFinished(Pokemon pokemon) {
        if (pokemon == null) {
            mLoadingStatus.setValue(Status.ERROR);
        } else {
            if(pokemon.evolves_from != null) {
                String url = PokeUtils.buildPokemonURL(pokemon.evolves_from);
                Log.d(TAG, "fetching new pokemon evolution data with this URL: " + url);
                new LoadPokemonTask(url, "pokemon-evolve", this).execute();
            }
            else{
                mCompletedTargets += 1;
                if(mCompletedTargets == 2) {
                    mLoadingStatus.setValue(Status.SUCCESS);
                }
            }
        }
    }
}