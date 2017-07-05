package com.example.ksfgh.pokedex.pokemon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ksfgh.pokedex.MainActivity;
import com.example.ksfgh.pokedex.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ksfgh on 05/07/2017.
 */

public class PokemonRecycler extends RecyclerView.Adapter<PokemonRecycler.ViewHolder>{


    private ArrayList<PokemonModel> pokemonList = new ArrayList<>();
    private MainActivity main;

    public PokemonRecycler(MainActivity main, ArrayList<PokemonModel> pokemonList) {
        this.main = main;
        this.pokemonList = pokemonList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_list_layout, parent, false);
        PokemonRecycler.ViewHolder vh = new PokemonRecycler.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mPokeId.setText(Integer.toString(pokemonList.get(position).getId()));
        holder.mPokeName.setText(pokemonList.get(position).getName());
        Glide.with(main)
                .load(pokemonList.get(position).getImgUrl())
                .into(holder.mPokeAvatar);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mPokeId;
        TextView mPokeName;
        ImageView mPokeAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            mPokeId = (TextView) itemView.findViewById(R.id.tvPokeId);
            mPokeName = (TextView) itemView.findViewById(R.id.tvPokeName);
            mPokeAvatar = (ImageView) itemView.findViewById(R.id.ivPokeAvatar);
        }
    }
}
