package com.example.android.pokefinder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PokemonTypeAdapter extends RecyclerView.Adapter<PokemonTypeAdapter.PokemonTypeViewHolder> {

    private static final String TAG = PokemonTypeAdapter.class.getSimpleName();

    private List<String> mTypes;

    void updatePokemonTypes(List<String> pokemonTypes) {
        mTypes = pokemonTypes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTypes != null) {
            return mTypes.size();
        } else {
            return 0;
        }
    }

    @Override
    @NonNull
    public PokemonTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.pokemon_types, parent, false);
        return new PokemonTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokemonTypeViewHolder holder, int position) {
        holder.bind(mTypes.get(position));
    }

    class PokemonTypeViewHolder extends RecyclerView.ViewHolder {
        private TextView mTypesNameTV;

        PokemonTypeViewHolder(View itemView) {
            super(itemView);
            mTypesNameTV = itemView.findViewById(R.id.tv_pokemon_type);
        }

        void bind(String pokemonType) {
            mTypesNameTV.setText(pokemonType);
        }
    }
}