package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.utils.PokeUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class PokemonDetailActivity extends AppCompatActivity{

    private static final String TAG = PokemonDetailActivity.class.getSimpleName();

    public static final String EXTRA_POKEMON = "Pikachu";

    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mNameTV;
    private ImageView mPokemonIconIV;

    private SavedPokemonViewModel mViewModel;

    private Toast mToast;

    private Pokemon mPokemon;
    private boolean mIsSaved = false;

    Menu mOptionsMenu = null;
    private RecyclerView mPokemonTypesRV;
    private PokemonTypeAdapter mPokemonTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_item_detail);

        mPokemonTypesRV = findViewById(R.id.rv_pokemon_types);

        mViewModel = new ViewModelProvider(this).get(SavedPokemonViewModel.class);

        mPokemonTypeAdapter = new PokemonTypeAdapter();
        mPokemonTypesRV.setAdapter(mPokemonTypeAdapter);

        LinearLayoutManager horizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mPokemonTypesRV.setLayoutManager(horizontalLayout);
        //mPokemonTypesRV.setLayoutManager(layoutManager);
        mPokemonTypesRV.setHasFixedSize(true);

        mToast = null;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_POKEMON)) {
            mPokemon = (Pokemon) intent.getSerializableExtra(EXTRA_POKEMON);

            mNameTV = findViewById(R.id.name);
            mNameTV.setText(PokeUtils.capitalizeFirstLetter(mPokemon.name));

            mWeightTV = findViewById(R.id.weight);
            mWeightTV.setText(String.format("Weight: %s kg", Float.toString((float) mPokemon.weight / 10)));

            mHeightTV = findViewById(R.id.height);
            mHeightTV.setText(String.format("Height: %s m", Float.toString((float) mPokemon.height / 10)));

            String iconURL = PokeUtils.buildPokemonIconURL(Integer.toString(mPokemon.id));

            mPokemonTypeAdapter.updatePokemonTypes(mPokemon.types);

            mPokemonIconIV = findViewById(R.id.pokemon_image);
            Glide.with(this).load(iconURL).into(mPokemonIconIV);
        }

        mViewModel.loadPokemonResults();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pokemon_item_detail, menu);

        mOptionsMenu = menu;

        mViewModel.getAllPokemon().observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemonList) {
                mIsSaved = pokemonList != null && pokemonList.contains(mPokemon);
                MenuItem item = mOptionsMenu.findItem(R.id.action_favorite);
                if (mIsSaved) {
                    item.setIcon(R.drawable.ic_bookmark_black);
                } else {
                    item.setIcon(R.drawable.ic_bookmark_border);
                }

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (mPokemon != null) {
                    mIsSaved = !mIsSaved;
                    String output = PokeUtils.capitalizeFirstLetter(mPokemon.name);
                    if (mIsSaved) {
                        mViewModel.insert(mPokemon);
                        makeToast(output + " is saved");
                        item.setIcon(R.drawable.ic_bookmark_black);
                    } else {
                        mViewModel.delete(mPokemon);
                        makeToast(output + " is no longer saved");
                        item.setIcon(R.drawable.ic_bookmark_border);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeToast(String message){
        if (mToast != null ){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
