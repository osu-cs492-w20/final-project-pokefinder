package com.example.android.pokefinder.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.example.android.pokefinder.R;
import com.example.android.pokefinder.data.Pokemon;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class PokeUtils {

    private final static String POKE_BASE_URL = "https://pokeapi.co/api/v2/";
    private final static String POKE_POKEMON_PATH = "pokemon";
    private final static String POKE_POKEMON_SPECIES_PATH = "pokemon-species";
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

        ArrayList<PokemonType> types;
    }

    static class PokemonEvolutionResults{
        PokemonSpecies evolves_from_species;
    }

    static class PokemonSpecies{
        String name;
    }

    static class PokemonType {
        PokemonTypeDetail type;
    }

    static class PokemonTypeDetail{
        String name;
    }

    public static String buildPokemonURL(String pokemonName) {
        return Uri.parse(POKE_BASE_URL).buildUpon()
                .appendPath(POKE_POKEMON_PATH)
                .appendPath(pokemonName.toLowerCase())
                .build()
                .toString();
    }

    public static String buildPokemonSpeciesURL(String pokemonName) {
        return Uri.parse(POKE_BASE_URL).buildUpon()
                .appendPath(POKE_POKEMON_SPECIES_PATH)
                .appendPath(pokemonName.toLowerCase())
                .build()
                .toString();
    }

    public static String buildPokemonIconURL(String pokemonID) {
        return String.format(POKE_URL_FORMAT_STR, pokemonID);
    }

    public static String capitalizeFirstLetter(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static Pokemon parsePokemonEvolutionJSON(String PokemonEvolutionJSON){
        Gson gson = new Gson();
        PokemonEvolutionResults results = gson.fromJson(PokemonEvolutionJSON, PokemonEvolutionResults.class);

        if (results != null) {
            Pokemon pokemon = new Pokemon();
            if (results.evolves_from_species != null) {
                pokemon.evolves_from = results.evolves_from_species.name;
            } else{
                pokemon.evolves_from = null;
            }
            return pokemon;
        } else {
            return null;
        }
    }

    public static Pokemon parsePokemonJSON(String PokemonJSON) {
        Gson gson = new Gson();
            PokemonResults results = gson.fromJson(PokemonJSON, PokemonResults.class);

            if (results != null) {
                Pokemon mPokemon = new Pokemon();

                mPokemon.id = results.id;
                mPokemon.name = results.name;

                mPokemon.weight = results.weight;
                mPokemon.height = results.height;

                mPokemon.types = new ArrayList<>();

                for(PokemonType typeItem: results.types){
                    mPokemon.types.add(typeItem.type.name);
                }

                return mPokemon;
            } else {
                return null;
            }
    }

    /*
     * This code was found from this Github Repo: https://github.com/eddydn/PokemonCommon
     * This utility styles how the different types of Pokemon Chips are colored.
     */

    public static int getColorByType(String type) {
        switch(type)
        {

            case "Normal":
                return Color.parseColor("#A4A27A");


            case "Dragon":
                return Color.parseColor("#743BFB");



            case "Psychic":
                return Color.parseColor("#F15B85");


            case "Electric":
                return Color.parseColor("#E9CA3C");


            case "Ground":
                return Color.parseColor("#D9BF6C");


            case "Grass":
                return Color.parseColor("#81C85B");

            case "Poison":
                return Color.parseColor("#A441A3");

            case "Steel":
                return Color.parseColor("#BAB7D2");


            case "Fairy":
                return Color.parseColor("#DDA2DF");


            case "Fire":
                return Color.parseColor("#F48130");


            case "Fight":
                return Color.parseColor("#BE3027");


            case "Bug":
                return Color.parseColor("#A8B822");


            case "Ghost":
                return Color.parseColor("#705693");


            case "Dark":
                return Color.parseColor("#745945");


            case "Ice":
                return Color.parseColor("#9BD8D8");


            case "Water":
                return Color.parseColor("#658FF1");
            default:
                return Color.parseColor("#658FA0");
        }
    }
}