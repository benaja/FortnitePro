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

<<<<<<< HEAD
    String[] itemname = {
=======
    String[] titles ={
>>>>>>> 8e60277d48e2574b7e6a936e6ded1c8299547b81
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    String[] descriptions = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    int[] pictures = {
            R.drawable.character_0,
            R.drawable.character_1,
            R.drawable.character_2,
            R.drawable.character_3,
            R.drawable.character_4,
            R.drawable.character_5,
            R.drawable.character_6,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        storeFavourites();
        loadPlayers();

        //getPlayer();
    }

    private void storeFavourites(){
        Set<String> stringSet = new HashSet<String>();
        for(int i = 0; i < titles.length; i++){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(titles[i]);
            stringBuilder.append(",");
            stringBuilder.append(descriptions[i]);
            stringBuilder.append(",");
            int randomNum = ThreadLocalRandom.current().nextInt(0, 6);
            stringBuilder.append(randomNum);
            stringSet.add(stringBuilder.toString());
        }

        SharedPreferences favourites = getSharedPreferences("Favourite", 0);
        SharedPreferences.Editor editor = favourites.edit();
        editor.putStringSet("element", stringSet);
        editor.commit();
    }


<<<<<<< HEAD
    private void loadPlayers() {
        simpleListView = (ListView) findViewById(R.id.list_view);


        List<HashMap<String, String>> list = new ArrayList();
        for (int i = 0; i < itemname.length; i++) {
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("name", itemname[i]);
            hashMap.put("description", description[i]);
            list.add(hashMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.favourite_list, new String[]{"name", "description"}, new int[]{R.id.Itemname, R.id.Itemdescription});
=======
    private void loadPlayers(){
        Set<String> stringSet = new HashSet<String>();

        SharedPreferences favourites = getSharedPreferences("Favourite", 0);
        Set<String> favouritesStringSet = favourites.getStringSet("element", stringSet);

        List<String> names = new ArrayList<>();
        List<String> descriptiones = new ArrayList<>();
        List<Integer> imageIds = new ArrayList<>();
        for(String favourite : favouritesStringSet){
            String[] favouriteArray = favourite.split(",");
            names.add(favouriteArray[0]);
            descriptiones.add(favouriteArray[1]);
            int imagePosition = Integer.parseInt(favouriteArray[2]);
            imageIds.add(pictures[imagePosition]);
        }
        FavouriteListAdapter adapter = new FavouriteListAdapter(this, names, descriptiones, imageIds);
>>>>>>> 8e60277d48e2574b7e6a936e6ded1c8299547b81

        simpleListView = (ListView)findViewById(R.id.list_view);
        simpleListView.setAdapter(adapter);

        simpleListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= titles[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

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
                    //No needed
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    getPlayer(query);
                    Toast.makeText(getApplicationContext(),
                            query, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "search object null", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private void getPlayer(String playerName){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.fortnitetracker.com/v1/profile/" + playerName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper mapper = new ObjectMapper();

                        try{
                            Profile playerProfile = mapper.readValue(response, Profile.class);

                            //response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
                            Intent intent = new Intent(getBaseContext(), SingleActivity.class);
                            //intent.putExtra("player_data", playerProfile);
                            startActivity(intent);

                        }catch (Exception e){
                            System.out.print(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TRN-Api-Key", "3c9aa93d-ee97-4154-bab9-281acbb3c549");
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /*private void getPlayer() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.fortnitetracker.com/v1/profile/psn/chillmau";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            Profile student = mapper.readValue(response, Profile.class);

                            //response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);

                        } catch (Exception e) {
                            System.out.print(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TRN-Api-Key", "3c9aa93d-ee97-4154-bab9-281acbb3c549");
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.);
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
