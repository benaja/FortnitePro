package com.example.bhunzb.fortnitepro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class CompareActivity extends AppCompatActivity {

    int[] statsIds = {
            R.id.percent_Wins,
            R.id.percent_Wins2,
            R.id.percent_Kills,
            R.id.percent_Kills2,
            R.id.percent_WinPerc,
            R.id.percent_WinPerc2,
            R.id.percent_KD,
            R.id.percent_KD2,
            R.id.percent_KPM,
            R.id.percent_KPM2,
            R.id.percent_TOP10,
            R.id.percent_TOP10_2
    };

    Profile profile2;
    Profile profile;

    String[] statsname = {
            "WINS",
            "WINS",
            "KILLS",
            "KILLS",
            "WINS %",
            "WINS %",
            "KD",
            "KD",
            "KILLS / MATCH",
            "KILLS / MATCH",
            "TOP 10",
            "TOP 10"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        initateContent();
        Intent intent = getIntent();
        String playerName = intent.getStringExtra("player_name");
        String playerName2 = intent.getStringExtra("player_name_to_compare");
        String platform = intent.getStringExtra("platform");
        String platform2 = intent.getStringExtra("platform_to_compare");
        getBothPlayers(playerName, playerName2, platform, platform2);
        initiatePropertys();
    }


    private void initateContent() {
        for (int i = 0; i < statsIds.length; i++) {
            View view = findViewById(statsIds[i]);
            TextView nameView = (TextView) view.findViewById(R.id.property_name);
            nameView.setText(statsname[i]);
        }
    }


    private void getBothPlayers(String player1, String player2, String platform1, String platform2) {
        getPlayer(player1, platform1);
        getPlayer(player2, platform2);
    }

    public void getPlayer(String playerName, String platform) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        final String url = "https://api.fortnitetracker.com/v1/profile/" + platform + "/" + playerName;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            if (profile == null) {
                                profile = mapper.readValue(response, Profile.class);
                            } else {
                                profile2 = mapper.readValue(response, Profile.class);

                            }
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

    public void initiatePropertys() {
        TextView titleName = findViewById(R.id.playerName1);
        TextView titleName2 = findViewById(R.id.playerName2);
        titleName.setText(profile.epicUserHandle);
        titleName2.setText(profile2.epicUserHandle);
    }
}
