package com.example.ksfgh.pokedex.pokemon;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ksfgh.pokedex.MainActivity;
import com.example.ksfgh.pokedex.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PokemonFragment extends Fragment {

    @BindView(R.id.avatar)ImageView avatar;
    @BindView(R.id.pokeId)TextView id;
    @BindView(R.id.pokeName)TextView name;
    @BindView(R.id.type1)Button type1;
    @BindView(R.id.type2)Button type2;
    @BindView(R.id.description)TextView desc;

    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<PokemonModel> pokeList = new ArrayList<>();
    MainActivity main;
    int idz;
    String namez,img,dezc;
    ArrayList<String>typez = new ArrayList<>();



    public PokemonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pokemon_fragment_layout,container, false);
        ButterKnife.bind(this, view);
        main = (MainActivity) getActivity();
        Log.i("kfsama", pokeList.size() + "");

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PokemonRecycler(main, pokeList);
        recyclerView.setAdapter(adapter);

//        recyclerView.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
//                        for(int i = 0; i < recyclerView.getChildCount(); i++){
//
//
//                            View view = recyclerView.getChildAt(i);
//                            view.setAlpha(0.0f);
//                            view.animate().alpha(1.0f)
//                                    .setDuration(300)
//                                    .setStartDelay(i*50)
//                                    .start();
//                        }
//
//                        return true;
//                    }
//                }
//        );

        Glide.with(main)
                .load(img)
                .into(avatar);

        this.id.setText(idz + "");
        this.name.setText(namez);
        this.desc.setText(dezc);


        if(typez.size() < 2){
            type2.setVisibility(View.GONE);
            String color = determineTypeColor(typez.get(0));
            type1.setBackgroundColor(Color.parseColor(color));
            type1.setText(typez.get(0));
        }
        else{
            String color = determineTypeColor(typez.get(0));
            Log.i("kfsama", color);
//            type1.setBackgroundColor(Color.parseColor(color));
            type1.setText(typez.get(0));

            String color2 = determineTypeColor(typez.get(1));
            Log.i("kfsama", color2);
           // type1.setBackgroundColor(Color.parseColor(color2));
            type2.setText(typez.get(1));
        }

        return view;
    }

    public void setList(ArrayList<PokemonModel> pokeList){

        this.pokeList = pokeList;
    }

    public void addItems(PokemonModel pokemonModel){
        //adapter = new PokemonRecycler(main, pokeList);
        pokeList.add(pokemonModel);
        adapter.notifyDataSetChanged();
    }

    public void setHeader(String img, int Id, String name, ArrayList<String> types, String description){

        for(int i = 0; i< types.size();i++){
            Log.i("kfsama", types.get(i));
        }
        this.img = img;
        idz = Id;
        namez = name;
        typez = types;
        dezc = description;
    }

    private String determineTypeColor(String whatKind) {

        String coolor = "";
        ArrayList<String> types = new ArrayList<>();
        types.add("normal");types.add("grass");types.add("fire");types.add("water");types.add("fighting");types.add("flying");
        types.add("poison");types.add("ground");types.add("rock");types.add("bug");types.add("ghost");types.add("electric");
        types.add("psychic");types.add("ice");types.add("dragon");types.add("dark");types.add("steel");types.add("fairy");

        ArrayList<String> color = new ArrayList<>();
        color.add("#BEBDB2");color.add("#8BD550");color.add("#F95343");color.add("#57AFFF");color.add("#A25544");color.add("#79A3FF");
        color.add("#A75B9D");color.add("#EDCD5B");color.add("#CEBD72");color.add("#C2D21F");color.add("#7975D7");color.add("#FDE53C");
        color.add("#FA64B5");color.add("#96F1FF");color.add("#8A75FF");color.add("#8E6956");color.add("#C4C2DB");color.add("#FAADFF");

        for(int i = 0;i < types.size(); i++){

            if(whatKind == types.get(i))
                coolor = color.get(i);
        }

        return  coolor;
    }

}
