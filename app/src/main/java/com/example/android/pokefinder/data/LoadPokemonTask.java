package com.example.android.pokefinder.data;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.android.pokefinder.utils.NetworkUtils;
import com.example.android.pokefinder.utils.PokeUtils;

import java.io.IOException;

class LoadPokemonTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = LoadPokemonTask.class.getSimpleName();

    public interface AsyncCallback {
        void onPokemonLoadFinished(Pokemon mPokemon);
        void onPokemonSpeciesLoadFinished(Pokemon mPokemon);
    }

    private Pokemon mPokemon;
    private String mURL;
    private String mRequestType;
    private AsyncCallback mCallback;

    LoadPokemonTask(String url, String requestType, Pokemon pokemon, AsyncCallback callback) {
        mRequestType = requestType;
        mURL = url;
        mCallback = callback;
        mPokemon = pokemon;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String pokemonJSON = null;
        try {
            pokemonJSON = NetworkUtils.doHTTPGet(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pokemonJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            try {
                Pokemon pokemon;
                if (mRequestType.equals("pokemon")) {
                    pokemon = PokeUtils.parsePokemonJSON(s);
                    mCallback.onPokemonLoadFinished(pokemon);
                }
                else if(mRequestType.equals("pokemon-species")){
                    pokemon = PokeUtils.parsePokemonEvolutionJSON(s, mPokemon);
                    mCallback.onPokemonSpeciesLoadFinished(pokemon);
                }
            }
            catch(Exception e){
                e.printStackTrace();
                mCallback.onPokemonLoadFinished(null);
            }
        }
    }
}