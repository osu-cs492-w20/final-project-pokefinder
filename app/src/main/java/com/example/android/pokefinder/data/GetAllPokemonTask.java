package com.example.android.pokefinder.data;

import android.os.AsyncTask;

import java.util.List;

class GetAllPokemonTask extends AsyncTask<Void, Void, List<Pokemon>> {
    private SavedPokemonDao mAsyncTaskDao;
    private static final String TAG = GetAllPokemonTask.class.getSimpleName();

    public interface AsyncCallback {
        void onPokemonGetAllFinished(List<Pokemon> pokeList);
    }

    private AsyncCallback mCallback;

    GetAllPokemonTask(SavedPokemonDao dao, AsyncCallback callback) {
        mAsyncTaskDao = dao;
        mCallback = callback;
    }

    @Override
    protected List<Pokemon> doInBackground(Void... voids) {
        return mAsyncTaskDao.getAll();
    }

    @Override
    protected void onPostExecute(List<Pokemon> myPokemonList) {
        mCallback.onPokemonGetAllFinished(myPokemonList);
    }

}
