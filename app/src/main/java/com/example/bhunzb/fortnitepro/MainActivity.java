package com.example.bhunzb.fortnitepro;

<<<<<<< HEAD
import android.arch.persistence.room.Database;
=======
<<<<<<< HEAD
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private Menu menu;
=======
import android.app.Application;
>>>>>>> 76ec06db30efe817cc404036924ea62ed747d080
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.List;
import java.util.Map;

import data.AppDatabase;
import data.Favourite;
import model.*;

public class MainActivity extends AppCompatActivity {
    ListView simpleListView;

    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    String[] description = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

<<<<<<< HEAD
    private AppDatabase database;

=======
>>>>>>> 9ba5b6b81490ed88092c9b03b0482d72ab9eb350
>>>>>>> 76ec06db30efe817cc404036924ea62ed747d080
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
=======

        loadPlayers();

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "fortnite-pro").build();

        List<Favourite> products = App.get().getDB().productDao().getAll();

        //getPlayer();


    }


    private void loadPlayers(){
        simpleListView = (ListView)findViewById(R.id.list_view);


        List<HashMap<String, String>> list = new ArrayList();
        for(int i = 0; i < itemname.length; i++) {
            HashMap<String,String> hashMap =  new HashMap();
            hashMap.put("name", itemname[i]);
            hashMap.put("description", description[i]);
            list.add(hashMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.favourite_list, new String[] { "name", "description" }, new int[] { R.id.Itemname, R.id.Itemdescription });

        simpleListView.setAdapter(adapter);

        //perform listView item click event
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void getPlayer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.fortnitetracker.com/v1/profile/psn/chillmau";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper mapper = new ObjectMapper();

                        try{
                            Profile student = mapper.readValue(response, Profile.class);

                            //response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);

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
>>>>>>> 9ba5b6b81490ed88092c9b03b0482d72ab9eb350
    }





/*
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
