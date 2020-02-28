package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.pokefinder.data.Pokemon;

public class PokemonDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON = "Pikachu";

    private Pokemon mPokemon;
    private boolean mIsSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_item_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_POKEMON)) {
            mPokemon = (Pokemon) intent.getSerializableExtra(EXTRA_POKEMON);

            TextView pokemonNameTV = findViewById(R.id.name);
            pokemonNameTV.setText(mPokemon.name);

            TextView weightTV = findViewById(R.id.weight);
            weightTV.setText(String.format("Weight: " + Integer.toString(mPokemon.weight)));

            TextView heightTV = findViewById(R.id.height);
            heightTV.setText(String.format("Height: " + Integer.toString(mPokemon.height)));
        }
    }

}
