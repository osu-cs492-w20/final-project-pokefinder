package com.example.android.pokefinder.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.pokefinder.utils.NetworkUtils;
import com.example.android.pokefinder.utils.PokeUtils;

import java.io.IOException;

class LoadPokemonTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = LoadPokemonTask.class.getSimpleName();

    public interface AsyncCallback {
        void onPokemonLoadFinished(Pokemon mPokemon);
    }

    private String mURL;
    private AsyncCallback mCallback;

    LoadPokemonTask(String url, AsyncCallback callback) {
        mURL = url;
        mCallback = callback;
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
        Pokemon mPokemon = null;
        if (s != null) {
            try {
                mPokemon = PokeUtils.parsePokemonJSON(s);
                mCallback.onPokemonLoadFinished(mPokemon);
            }
            catch(Exception e){
                e.printStackTrace();
                mCallback.onPokemonLoadFinished(null);
            }
        }
    }
}