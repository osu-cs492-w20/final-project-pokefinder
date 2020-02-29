package com.example.android.pokefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.Status;
import com.example.android.pokefinder.utils.PokeUtils;

public class PokemonDetailActivity extends AppCompatActivity{

    public static final String EXTRA_POKEMON = "Pikachu";

    private TextView mHeightTV;
    private TextView mWeightTV;
    private TextView mNameTV;
    private ImageView mPokemonIconIV;

    private SavedPokemonViewModel mViewModel;

    private Toast mToast;

    private Pokemon mPokemon;
    private boolean mIsSaved = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_item_detail);

        mViewModel = new ViewModelProvider(this).get(SavedPokemonViewModel.class);

        mToast = null;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_POKEMON)) {
            mPokemon = (Pokemon) intent.getSerializableExtra(EXTRA_POKEMON);

            mNameTV = findViewById(R.id.name);
            String str = mPokemon.name;
            String output = str.substring(0, 1).toUpperCase() + str.substring(1);
            mNameTV.setText(output);

            mWeightTV = findViewById(R.id.weight);
            mWeightTV.setText(String.format("Weight: %s kg", Float.toString((float) mPokemon.weight / 10)));

            mHeightTV = findViewById(R.id.height);
            mHeightTV.setText(String.format("Height: %s m", Float.toString((float) mPokemon.height / 10)));

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
                if (mPokemon != null) {
                    mIsSaved = !mIsSaved;
                    if (mIsSaved) {
                        mViewModel.insert(mPokemon);
                        makeToast(mPokemon.name + " is saved");
                        item.setIcon(R.drawable.ic_bookmark_black);
                    } else {
                        mViewModel.delete(mPokemon);
                        makeToast(mPokemon.name +  " is no longer saved");
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
