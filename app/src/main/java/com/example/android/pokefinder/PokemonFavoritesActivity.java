package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.utils.PokeUtils;

import java.util.List;

public class PokemonFavoritesActivity extends AppCompatActivity implements PokemonAdapter.OnPokemonClickListener {

    private static final String TAG = PokemonFavoritesActivity.class.getSimpleName();
    private RecyclerView mPokemonItemsRV;

    private PokemonAdapter mPokemonAdapter;
    private SavedPokemonViewModel mSavedPokemonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mPokemonItemsRV = findViewById(R.id.rv_pokemon_items);

        mPokemonAdapter = new PokemonAdapter(this);
        mPokemonItemsRV.setAdapter(mPokemonAdapter);
        mPokemonItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mPokemonItemsRV.setHasFixedSize(true);


        /*
         * This version of the app code uses the new ViewModel architecture to manage data for
         * the activity.  See the classes in the data package for more about how the ViewModel
         * is set up.  Here, we simply grab the forecast data ViewModel.
         */
        mSavedPokemonViewModel = new ViewModelProvider(this).get(SavedPokemonViewModel.class);

        /*
         * Attach an Observer to the forecast data.  Whenever the forecast data changes, this
         * Observer will send the new data into our RecyclerView's adapter.
         */
        mSavedPokemonViewModel.getAllPokemon().observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemonList) {
                mPokemonAdapter.updatePokemon(pokemonList);
            }
        });

        mSavedPokemonViewModel.loadPokemonResults();
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        if (pokemon != null) {
            Intent intent = new Intent(this, PokemonDetailActivity.class);
            intent.putExtra(PokemonDetailActivity.EXTRA_POKEMON, pokemon);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mSavedPokemonViewModel.loadPokemonResults();
    }
}
