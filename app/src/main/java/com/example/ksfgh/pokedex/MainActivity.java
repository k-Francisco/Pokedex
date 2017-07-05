package com.example.ksfgh.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.example.ksfgh.pokedex.pokemon.PokemonFragment;
import com.example.ksfgh.pokedex.pokemon.PokemonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bottombar) AHBottomNavigation pokeBar;
    @BindView(R.id.fragment_container) FrameLayout container;
    PokemonFragment pokemonFragment = new PokemonFragment();
    LoadingFragment loader = new LoadingFragment();

    //ArrayLists
    ArrayList<String> pokedexUrls = new ArrayList<>();

    public String next;
    public String previous;

    String url = "https://pokeapi.co";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPokeBar();
        getPokedexList();
        switchFragment(loader);
    }

    Fragment oldFragment = null;
    private void switchFragment(Fragment fragment) {

        if(fragment == oldFragment)
            return;

        oldFragment = fragment;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void initPokeBar() {
        pokeBar.addItem(
                new AHBottomNavigationItem("Pokemon",R.drawable.zzz_duck)
        );

        pokeBar.addItem(
                new AHBottomNavigationItem("Types", R.drawable.zzz_flash)
        );

        pokeBar.addItem(
                new AHBottomNavigationItem("Berries", R.drawable.zzz_apple)
        );

        pokeBar.addItem(
                new AHBottomNavigationItem("Abilities", R.drawable.zzz_vpn)
        );

        pokeBar.setDefaultBackgroundColor(Color.parseColor("#e74c3c"));
        pokeBar.setAccentColor(Color.parseColor("#ffffff"));
        pokeBar.setInactiveColor(Color.parseColor("#bdc3c7"));
        pokeBar.setForceTint(true);
        pokeBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        pokeBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position){

                    case 0:
                        switchFragment(pokemonFragment);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "TYPES", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "MOVES", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "ABILITIES", Toast.LENGTH_SHORT).show();
                        break;

                }
                return  true;
            }
        });

    }


    public void getPokedexList(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "/api/v2/pokemon", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            next = response.getString("next");
                            JSONArray results = response.getJSONArray("results");
                            for(int i = 0; i < results.length(); i++)
                            {
                                JSONObject url = results.getJSONObject(i);
                                pokedexUrls.add(new String(url.getString("url")));

                                if(i == (results.length() - 1))
                                    fillPokemonFragment(pokedexUrls);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                        Log.i("kfsama", "ERROR");
                    }
                });
        MySingleton.getmInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    public  void fillPokemonFragment(final ArrayList<String> urls){

        final ArrayList<PokemonModel> pokemonModels = new ArrayList<>();
        final ArrayList<String> pokeTypes = new ArrayList<>();
        Log.i("kfsama", urls.size() + "");
        for(int i = 0; i < urls.size(); i++){
            Log.i("kfsama", i + "");
            final int finalI = i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urls.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                int firstPokeId = -1;
                                String firstPokeName = "";
                                String firstPokeImg = "";


                                JSONObject sprites = response.getJSONObject("sprites");
                                int id = response.getInt("id");
                                String name = response.getString("name");
                                String img = sprites.getString("front_default");
                                pokemonModels.add(new PokemonModel(id, name, img));

                                String specie = response.getJSONObject("species").getString("url");

                                if(finalI == 0){

                                    firstPokeId = id;
                                    firstPokeName = name;
                                    firstPokeImg = img;


                                    JSONArray types = response.getJSONArray("types");
                                    for(int j = 0; j < types.length(); j++){
                                        Log.i("kfsama", types.getJSONObject(j).getJSONObject("type").getString("name"));
                                        pokeTypes.add(types.getJSONObject(j).getJSONObject("type").getString("name"));
                                    }
                                    pokemonFragment.setHeader(firstPokeImg, firstPokeId, firstPokeName, pokeTypes, "" );
                                }

                                if(finalI == (urls.size() -1) ){
                                    pokemonFragment.setList(pokemonModels);
                                    switchFragment(pokemonFragment);

                                    //getDescription(pokemonModels, pokeTypes, firstPokeId, firstPokeName, firstPokeImg, specie);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("kfsama", "ERROR at filling kay "+ error.getMessage());
                        }
                    });
            MySingleton.getmInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }
    }

    public void getDescription(final ArrayList<PokemonModel> pokemonModels, final ArrayList<String> types, final int id, final String name, final String img, String url){


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray flavor = response.getJSONArray("flavor_text_entries");
                            for(int i = 0; i < flavor.length(); i++){
                                String version = flavor.getJSONObject(i).getJSONObject("version").getString("name");
                                String language = flavor.getJSONObject(i).getJSONObject("language").getString("name");

                                if(version == "alpha-sapphire" && language == "en"){
                                    String description = flavor.getJSONObject(i).getString("flavor_text");
                                    Log.i("kfsama", "im here");
                                    pokemonFragment.setList(pokemonModels);
                                    //pokemonFragment.setHeader(img, id, name, types, description );
                                    switchFragment(pokemonFragment);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("kfsama", "ERROR at last");
                    }
                });
        MySingleton.getmInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
