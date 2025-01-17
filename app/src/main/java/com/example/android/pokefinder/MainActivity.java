package com.example.android.pokefinder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.data.Status;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mErrorMessageTV;
    private VideoView mVideoView;

    Toast mToast;

    private PokemonViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToast = null;

        mSearchBoxET = findViewById(R.id.pokemon_name_search_box);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTV = findViewById(R.id.tv_error_message);

        mViewModel = new ViewModelProvider(this).get(PokemonViewModel.class);

        mViewModel.resetStatus();
        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    Pokemon pokemon = mViewModel.getSearchResults().getValue();
                    onPokemonSearched(pokemon);
                } else if (status == Status.ERROR) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mErrorMessageTV.setVisibility(View.VISIBLE);
                    mViewModel.resetStatus();
                }
            }
        });

        final Button searchButton = findViewById(R.id.btn_pokemon_name_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    if(mViewModel.getLoadingStatus().getValue() == Status.INITIAL) {
                        doPokemonSearch(searchQuery);
                    }
                }
                else{
                    makeToast("Please enter a Pokemon first.");
                }
            }
        });

        loadVideo();

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

        mViewModel.resetStatus();

    }

    private void makeToast(String message){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                Intent favoritesIntent = new Intent(this, PokemonFavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doPokemonSearch(String searchQuery) {
        mViewModel.loadSearchResults(searchQuery);
    }

    private void loadVideo() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.pikachu);
        mVideoView.setVideoURI(video);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });
    }

}
