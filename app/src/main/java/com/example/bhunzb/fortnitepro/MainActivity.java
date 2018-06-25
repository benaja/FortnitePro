package com.example.bhunzb.fortnitepro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import android.widget.AdapterView.OnItemClickListener;

import model.Profile;


public class MainActivity extends AppCompatActivity {
    ListView simpleListView;
    Boolean isComparing = false;
    String player_name;
    String platform;

    int[] pictures = {
            R.drawable.character_4,
            R.drawable.character_5,
            R.drawable.character_6,
            R.drawable.character_7,
            R.drawable.character_8,
            R.drawable.character_9,
            R.drawable.character_10,
            R.drawable.character_11,
            R.drawable.character_12,
            R.drawable.character_13,
            R.drawable.character_14,
            R.drawable.character_15
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if(intent.hasExtra("player_name")){
            isComparing = true;
            player_name = intent.getStringExtra("player_name");
            platform = intent.getStringExtra("platform");

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Spieler auswählen");
            }
        }else{
            isComparing = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPlayers();
    }

    private void loadPlayers() {
        Set<String> stringSet = new HashSet<String>();

        SharedPreferences favourites = getSharedPreferences("Favourite", 0);
        Set<String> favouritesStringSet = favourites.getStringSet("element", stringSet);

        List<String> names = new ArrayList<>();
        List<String> descriptiones = new ArrayList<>();
        List<Integer> imageIds = new ArrayList<>();
        for (String favourite : favouritesStringSet) {
            String[] favouriteArray = favourite.split(",");
            names.add(favouriteArray[0]);
            descriptiones.add(favouriteArray[1]);
            int imagePosition = Integer.parseInt(favouriteArray[2]);
            imageIds.add(pictures[imagePosition]);
        }
        FavouriteListAdapter adapter = new FavouriteListAdapter(this, names, descriptiones, imageIds);

        simpleListView = (ListView) findViewById(R.id.list_view);
        simpleListView.setAdapter(adapter);

        simpleListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView textView = (TextView)view.findViewById(R.id.Itemname);
                String newPlayer = textView.getText().toString();
                String newPlatform = ((TextView) view.findViewById(R.id.Itemdescription)).getText().toString();

                passToApiRequestPlayer(newPlayer, newPlatform);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        if (searchViewMenuItem != null) {
            SearchView sv = (SearchView) searchViewMenuItem.getActionView();
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    //Nod needed
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String searchQuery) {
                    if (searchQuery != null) {
                        passToApiRequestPlayer(searchQuery, "");
                    }
                    return true;
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Such Objekt nicht definiert!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void passToApiRequestPlayer(String newProfile, String newPlatform) {
        Intent intent;
        if(isComparing){
            intent = new Intent(getApplicationContext(), CompareActivity.class);
            intent.putExtra("player_name_to_compare", newProfile);
            intent.putExtra("platform_to_compare", newPlatform);
            intent.putExtra("player_name", player_name);
            intent.putExtra("platform", platform);
        }else{
            intent = new Intent(getApplicationContext(), SingleActivity.class);
            intent.putExtra("player_name", newProfile);
            intent.putExtra("platform", newPlatform);
        }

        startActivity(intent);
    }
}
