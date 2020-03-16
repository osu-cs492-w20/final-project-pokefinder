package com.example.android.pokefinder.utils;

import android.graphics.Color;
import android.net.Uri;

import com.example.android.pokefinder.data.Pokemon;
import com.google.gson.Gson;

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

    static class PokemonVariety{
        PokemonVarietyDescription pokemon;
        Boolean is_default;
    }

    static class PokemonVarietyDescription{
        String name;
        String url;
    }

    static class PokemonSpeciesResults{
        PokemonSpecies evolves_from_species;
        PokeURL evolution_chain;
        List<PokemonVariety> varieties;
        String name;
    }

    static class PokeURL {
        String url;
    }

    static class PokemonSpecies{
        String name;
    }

    static class PokemonEvolutionResults{
        PokemonChain chain;
    }

    static class PokemonChain{
        ArrayList<PokemonChain> evolves_to;
        PokemonSpecies species;
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

    public static String parseForVarieties(String PokemonSpeciesJSON){
        Gson gson = new Gson();
        PokemonSpeciesResults results = gson.fromJson(PokemonSpeciesJSON, PokemonSpeciesResults.class);

        if (results != null) {
            for (PokemonVariety varietyItem : results.varieties) {
                if (varietyItem.is_default) {
                    return varietyItem.pokemon.url;
                }
            }
        }
        return null;
    }

    public static Pokemon parsePokemonEvolutionJSON(String EvolutionJSON, String pokemonName, Pokemon pokemon_to_modify){
        Gson gson = new Gson();
        PokemonEvolutionResults results = gson.fromJson(EvolutionJSON, PokemonEvolutionResults.class);

        String prevName = null;
        if(results != null){
            PokemonChain myChainLink = results.chain;
            Boolean end_of_chain = false;

            while (!end_of_chain) {
                if (pokemonName.toLowerCase().equals(myChainLink.species.name.toLowerCase())
                        || pokemon_to_modify.name.toLowerCase().equals(myChainLink.species.name.toLowerCase())) {
                    pokemon_to_modify.evolves_from = prevName;
                    pokemon_to_modify.evolves_to = null;
                    if (myChainLink.evolves_to.size() == 1) {
                        pokemon_to_modify.evolves_to = myChainLink.evolves_to.get(0).species.name;
                    }
                }
                end_of_chain = myChainLink.evolves_to.size() == 0;

                if (!end_of_chain){
                    prevName = myChainLink.species.name;
                    myChainLink = myChainLink.evolves_to.get(0);
                }


            }

        }

        return pokemon_to_modify;
    }


    public static Pokemon parsePokemonJSON(String PokemonJSON, String PokemonSpeciesJSON) {
        if(PokemonJSON.equals("Not Found")){
            return null;
        }
        Gson gson = new Gson();
        PokemonResults results = gson.fromJson(PokemonJSON, PokemonResults.class);
        PokemonSpeciesResults speciesResults = null;
        if (PokemonSpeciesJSON != null) {
            speciesResults = gson.fromJson(PokemonSpeciesJSON, PokemonSpeciesResults.class);
        }

        if (results != null) {
            Pokemon pokemon = new Pokemon();

            pokemon.id = results.id;

            pokemon.weight = results.weight;
            pokemon.height = results.height;

            pokemon.types = new ArrayList<>();

            for(PokemonType typeItem: results.types){
                pokemon.types.add(typeItem.type.name);
            }

            if (speciesResults != null){
                pokemon.name = speciesResults.name;
                pokemon.evolution_chain_url = speciesResults.evolution_chain.url;
            }

            return pokemon;
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