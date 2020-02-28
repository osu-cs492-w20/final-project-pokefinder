package com.example.android.pokefinder.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.pokefinder.R;
import com.example.android.pokefinder.data.Pokemon;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;


public class PokeUtils {

    public static final String EXTRA_POKEMON_ITEM = "com.example.android.pokefinder.utils.Pokemon";

    private final static String POKE_BASE_URL = "https://pokeapi.co/api/v2/";
    private final static String POKE_POKEMON_PATH = "pokemon";
    private final static String POKE_URL_FORMAT_STR = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%s.png";


    /*
     * The below several classes are used only for JSON parsing with Gson.  The main class that's
     * used to represent a single forecast item throughout the rest of the app is the ForecastItem
     * class in the data package.
     */
    static class PokemonResults {
        int id;
        String name;

        int weight;
        int height;
    }

    public static String buildPokemonURL(String pokemonName) {
        return Uri.parse(POKE_BASE_URL).buildUpon()
                .appendPath(POKE_POKEMON_PATH)
                .appendPath(pokemonName)
                .build()
                .toString();
    }

    public static String buildPokemonIconURL(String pokemonID) {
        return String.format(POKE_URL_FORMAT_STR, pokemonID);
    }

    public static Pokemon parsePokemonJSON(String PokemonJSON) {
        Gson gson = new Gson();
        try {
            PokemonResults results = gson.fromJson(PokemonJSON, PokemonResults.class);

            if (results != null) {
                Pokemon mPokemon = new Pokemon();

                mPokemon.id = results.id;
                mPokemon.name = results.name;

                mPokemon.weight = results.weight;
                mPokemon.height = results.height;

                return mPokemon;
            } else {
                return null;
            }
        }
        catch(Exception e){
            return null;
        }
    }
}