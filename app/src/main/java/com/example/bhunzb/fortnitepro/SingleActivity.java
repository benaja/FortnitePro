package com.example.bhunzb.fortnitepro;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import model.Profile;

public class SingleActivity extends AppCompatActivity {

    private String playerName;
    private String platform;
    private Profile profile;

    int[] statsIds = {
            R.id.wins,
            R.id.win_percent,
            R.id.kills,
            R.id.kd,
            R.id.top10,
            R.id.kills_per_match
    };

    String[] statsname = {
            "WINS",
            "WINS %",
            "KILLS",
            "KD",
            "TOP 10",
            "KILLS / MATCH"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        playerName = intent.getStringExtra("player_name");
        platform = intent.getStringExtra("platform");
        actionBar.setTitle(playerName);
        if(platform.equals("")){
            platform = "pc";
        }
        getPlayer(playerName, platform);

        Button button = (Button) findViewById(R.id.compare_button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("player_name", playerName);
                intent.putExtra("platform", platform);
                startActivity(intent);
            }
        });

        initateContent();
    }


    private void initateContent(){
        for(int i = 0; i < statsIds.length; i++){
            View view = findViewById(statsIds[i]);
            TextView nameView = (TextView)view.findViewById(R.id.property_name);
            nameView.setText(statsname[i]);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getPlayer(String playerName, String platform) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        final String url = "https://api.fortnitetracker.com/v1/profile/"+platform+ "/" + playerName;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            profile = mapper.readValue(response, Profile.class);
                            initiatePropertys();
                            int test = 1;
                            //display function call for displaying stats

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
    }

    private void initiatePropertys(){
        
    }
}
