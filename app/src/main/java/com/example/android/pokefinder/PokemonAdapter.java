package com.example.android.pokefinder;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.pokefinder.data.Pokemon;
import com.example.android.pokefinder.utils.PokeUtils;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private static final String TAG = PokemonAdapter.class.getSimpleName();

    private List<Pokemon> mPokemon;
    private OnPokemonClickListener mPokemonClickListener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokemonAdapter(OnPokemonClickListener clickListener) {
        mPokemonClickListener = clickListener;
    }

    public void updatePokemon(List<Pokemon> pokemonList) {
        mPokemon = pokemonList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPokemon != null) {
            return mPokemon.size();
        } else {
            return 0;
        }
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.pokemon_favorite_card, parent, false);
        return new PokemonViewHolder(itemView);
    }

    @Override
        public void onBindViewHolder(PokemonViewHolder holder, int position) {
        holder.bind(mPokemon.get(position));
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPokemonNameTV;
        private ImageView mPokemonIconIV;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            mPokemonNameTV = itemView.findViewById(R.id.tv_pokemon_name);
            mPokemonIconIV = itemView.findViewById(R.id.iv_pokemon_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(Pokemon pokemon) {

            String iconURL = PokeUtils.buildPokemonIconURL(Integer.toString(pokemon.id));
            Glide.with(mPokemonIconIV.getContext()).load(iconURL).into(mPokemonIconIV);

            mPokemonNameTV.setText(pokemon.name);
        }

        @Override
        public void onClick(View v) {
            Pokemon pokemon = mPokemon.get(getAdapterPosition());
            mPokemonClickListener.onPokemonClick(pokemon);
        }
    }
}