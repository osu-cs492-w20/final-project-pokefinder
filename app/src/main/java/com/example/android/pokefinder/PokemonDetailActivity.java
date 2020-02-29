package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.utils.PokeUtils;

public class PokemonDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON = "Pikachu";

    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mNameTV;
    private ImageView mPokemonIconIV;



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
            String str = mPokemon.name;
            String output = str.substring(0, 1).toUpperCase() + str.substring(1);
            pokemonNameTV.setText(output);

            mWeightTV = findViewById(R.id.weight);
            mWeightTV.setText(String.format("Weight: " + Float.toString((float)mPokemon.weight / 10) + " kg"));

            mHeightTV = findViewById(R.id.height);
            mHeightTV.setText(String.format("Height: " + Float.toString((float)mPokemon.height / 10) + " m"));

            String iconURL = PokeUtils.buildPokemonIconURL(Integer.toString(mPokemon.id));

            mPokemonIconIV = findViewById(R.id.pokemon_image);
            Glide.with(this).load(iconURL).into(mPokemonIconIV);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pokemon_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
