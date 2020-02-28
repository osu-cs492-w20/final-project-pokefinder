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
            Log.d(TAG, "fetching MORE pokemon data with this URL: " + mURL);
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
            mPokemon = PokeUtils.parsePokemonJSON(s);
        }
        mCallback.onPokemonLoadFinished(mPokemon);
    }
}