package com.example.android.pokefinder.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class SavedPokemonRepository implements GetAllPokemonTask.AsyncCallback{
    private static final String TAG = SavedPokemonRepository.class.getSimpleName();

    private SavedPokemonDao mSavedPokemonDao;
    private MutableLiveData<List<Pokemon>> mPokemonList;

    public SavedPokemonRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mSavedPokemonDao = db.savedPokemonDao();
        mPokemonList = new MutableLiveData<>();
    }


    private static class InsertPokemonAsyncTask
            extends AsyncTask<Pokemon, Void, Void> {
        private SavedPokemonDao mAsyncTaskDao;

        InsertPokemonAsyncTask(SavedPokemonDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Pokemon... pokemon) {
            mAsyncTaskDao.insert(pokemon[0]);
            return null;
        }
    }

    public void insertPokemon(Pokemon pokemon) {
        new InsertPokemonAsyncTask(mSavedPokemonDao).execute(pokemon);
    }

    private static class DeletePokemonAsyncTask
            extends AsyncTask<Pokemon, Void, Void> {
        private SavedPokemonDao mAsyncTaskDao;

        DeletePokemonAsyncTask(SavedPokemonDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Pokemon... pokemon) {
            mAsyncTaskDao.delete(pokemon[0]);
            return null;
        }
    }

    public void deletePokemon(Pokemon pokemon) {
        new DeletePokemonAsyncTask(mSavedPokemonDao).execute(pokemon);
    }

    public void queryAllPokemon() {
        new GetAllPokemonTask(mSavedPokemonDao, this).execute();
    }

    @Override
    public void onPokemonGetAllFinished(List<Pokemon> myPokemonList){
            mPokemonList.setValue(myPokemonList);
    }

    public LiveData<List<Pokemon>> getAllPokemon(){ return mPokemonList; }


}
