package com.example.android.pokefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.text.TextUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.Status;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mErrorMessageTV;

    private PokemonViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.pokemon_name_search_box);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTV = findViewById(R.id.tv_error_message);

        mViewModel = new ViewModelProvider(this).get(PokemonViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<Pokemon>() {
            @Override
            public void onChanged(Pokemon pokemon) {
                onPokemonSearched(pokemon);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "There was an error getting that Pokemon");
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        Button searchButton = findViewById(R.id.btn_pokemon_name_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });


    }

    public void onPokemonSearched(Pokemon pokemon) {
        if(pokemon != null) {
            Intent intent = new Intent(this, PokemonDetailActivity.class);
            intent.putExtra(PokemonDetailActivity.EXTRA_POKEMON, pokemon);
            startActivity(intent);
        }
    }



    private void doGitHubSearch(String searchQuery) {

        mViewModel.loadSearchResults(searchQuery);
    }
}
