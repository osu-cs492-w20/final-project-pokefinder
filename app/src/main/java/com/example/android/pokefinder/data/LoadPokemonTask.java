package com.example.android.pokefinder.data;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.android.pokefinder.utils.NetworkUtils;
import com.example.android.pokefinder.utils.PokeUtils;

import java.io.IOException;

class LoadPokemonTask extends AsyncTask<Void, Void, Pokemon> {
    private static final String TAG = LoadPokemonTask.class.getSimpleName();

    public interface AsyncCallback {
        void onPokemonLoadFinished(Pokemon pokemon);
    }

    private String mPokemonName;
    private AsyncCallback mCallback;

    LoadPokemonTask(String name, AsyncCallback callback) {
        mPokemonName = name;
        mCallback = callback;
    }

    @Override
    protected Pokemon doInBackground(Void... voids) {
        Pokemon new_pokemon;
        try {
            String my_species_url = PokeUtils.buildPokemonSpeciesURL(mPokemonName);
            String pokemonSpeciesJSON = NetworkUtils.doHTTPGet(my_species_url);

            String my_url = PokeUtils.parseForVarieties(pokemonSpeciesJSON);

            String pokemonJSON = NetworkUtils.doHTTPGet(my_url);

            new_pokemon = PokeUtils.parsePokemonJSON(pokemonJSON, pokemonSpeciesJSON);

            String pokemonEvolutionJSON = NetworkUtils.doHTTPGet(new_pokemon.evolution_chain_url);

            new_pokemon = PokeUtils.parsePokemonEvolutionJSON(pokemonEvolutionJSON, new_pokemon.name, new_pokemon);

            if (new_pokemon.evolves_from != null){
                String evolves_from_pokemon_url = PokeUtils.buildPokemonURL(new_pokemon.evolves_from);
                String evolves_fromJSON = NetworkUtils.doHTTPGet(evolves_from_pokemon_url);
                Pokemon evolves_from_pokemon = PokeUtils.parsePokemonJSON(evolves_fromJSON, null);
                new_pokemon.evolves_from_id = evolves_from_pokemon.id;
            }
            if (new_pokemon.evolves_to != null){
                String evolves_to_pokemon_url = PokeUtils.buildPokemonURL(new_pokemon.evolves_to);
                String evolves_toJSON = NetworkUtils.doHTTPGet(evolves_to_pokemon_url);
                Pokemon evolves_to_pokemon = PokeUtils.parsePokemonJSON(evolves_toJSON, null);
                new_pokemon.evolves_to_id = evolves_to_pokemon.id;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new_pokemon;
    }

    @Override
    protected void onPostExecute(Pokemon poke) {
        mCallback.onPokemonLoadFinished(poke);
    }
}