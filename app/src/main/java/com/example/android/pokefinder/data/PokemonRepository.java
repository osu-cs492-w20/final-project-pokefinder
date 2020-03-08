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
        mLoadingStatus.setValue(Status.INITIAL);

        mCompletedTargets = 0;
        mCurrentPokemonName = null;
    }

    public void resetStatus(){
        mLoadingStatus.setValue(Status.INITIAL);
    }

    public void loadPokemon(String pokemonName) {
        mCompletedTargets = 0;
        mLoadingStatus.setValue(Status.LOADING);
        if (shouldFetchPokemon(pokemonName)) {
            mCurrentPokemonName = pokemonName;
            mPokemon.setValue(null);
            new LoadPokemonTask(pokemonName, this).execute();
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
            mLoadingStatus.setValue(Status.SUCCESS);
        }
    }

}