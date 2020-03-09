package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.Status;
import com.example.android.pokefinder.utils.PokeUtils;

import java.util.List;

public class PokemonDetailActivity extends AppCompatActivity{

    private static final String TAG = PokemonDetailActivity.class.getSimpleName();

    public static final String EXTRA_POKEMON = "Pikachu";

    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mNameTV;
    private TextView mEvolvesFromTV;
    private TextView mEvolvesToTV;
    private ImageView mPokemonIconIV;
    private ImageView mPokemonEvolvesFromIconIV;
    private ImageView mPokemonEvolvesToIconIV;

    private SavedPokemonViewModel mViewModel;
    private PokemonViewModel mViewModelForSearch;

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

        LinearLayoutManager horizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mPokemonTypesRV.setLayoutManager(horizontalLayout);
        //mPokemonTypesRV.setLayoutManager(layoutManager);
        mPokemonTypesRV.setHasFixedSize(true);

        mPokemonTypeAdapter = new PokemonTypeAdapter();
        mPokemonTypesRV.setAdapter(mPokemonTypeAdapter);

        mToast = null;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_POKEMON)) {

            mViewModelForSearch = new ViewModelProvider(this).get(PokemonViewModel.class);

            mViewModelForSearch.getLoadingStatus().observe(this, new Observer<Status>() {
                @Override
                public void onChanged(Status status) {
                    if (status == Status.SUCCESS) {
                        Pokemon pokemon = mViewModelForSearch.getSearchResults().getValue();
                        onPokemonSearched(pokemon);
                    }
                }
            });
            mViewModelForSearch.resetStatus();


            mPokemon = (Pokemon) intent.getSerializableExtra(EXTRA_POKEMON);

            mEvolvesFromTV = findViewById(R.id.tv_evolves_from);
            mPokemonEvolvesFromIconIV = findViewById(R.id.pokemon_evolve_from_image);

            mEvolvesToTV = findViewById(R.id.tv_evolves_to);
            mPokemonEvolvesToIconIV = findViewById(R.id.pokemon_evolve_to_image);
            /*
             * This pokemon evolves from another pokemon
             */
            if(mPokemon.evolves_from != null) {
                mEvolvesFromTV.setText(PokeUtils.capitalizeFirstLetter(mPokemon.evolves_from));
                mPokemonEvolvesFromIconIV = findViewById(R.id.pokemon_evolve_from_image);

                String iconEvolveFromURL = PokeUtils.buildPokemonIconURL(Integer.toString(mPokemon.evolves_from_id));
                Glide.with(this).load(iconEvolveFromURL).into(mPokemonEvolvesFromIconIV);

                mPokemonEvolvesFromIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPokemonSearch(mPokemon.evolves_from);
                    }
                });
                mEvolvesFromTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPokemonSearch(mPokemon.evolves_from);
                    }
                });
            }
            else{
                /*
                 * This pokemon is at the base of the evolution tree.
                 */
                mEvolvesFromTV.setText(R.string.no_evolution_form);
                mPokemonEvolvesFromIconIV.setVisibility(View.GONE);
            }

            if(mPokemon.evolves_to != null) {
                mEvolvesToTV.setText(PokeUtils.capitalizeFirstLetter(mPokemon.evolves_to));
                mPokemonEvolvesToIconIV = findViewById(R.id.pokemon_evolve_to_image);

                String iconEvolveToURL = PokeUtils.buildPokemonIconURL(Integer.toString(mPokemon.evolves_to_id));
                Glide.with(this).load(iconEvolveToURL).into(mPokemonEvolvesToIconIV);

                mPokemonEvolvesToIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPokemonSearch(mPokemon.evolves_to);
                    }
                });
                mEvolvesToTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPokemonSearch(mPokemon.evolves_to);
                    }
                });
            }
            else{
                /*
                 * This pokemon is at the top of the evolution tree.
                 */
                mEvolvesToTV.setText(R.string.no_evolution_form);
                mPokemonEvolvesToIconIV.setVisibility(View.GONE);
            }



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


    public void onPokemonSearched(Pokemon pokemon) {
        if(pokemon != null) {
            Intent intent = new Intent(this, PokemonDetailActivity.class);
            intent.putExtra(PokemonDetailActivity.EXTRA_POKEMON, pokemon);
            startActivity(intent);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        mViewModelForSearch.resetStatus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pokemon_item_detail, menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_home);

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
            case android.R.id.home:
                Intent i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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

    private void doPokemonSearch(String searchQuery) {
        if(Status.INITIAL == mViewModelForSearch.getLoadingStatus().getValue()) {
            mViewModelForSearch.loadSearchResults(searchQuery);
        }
    }
}
