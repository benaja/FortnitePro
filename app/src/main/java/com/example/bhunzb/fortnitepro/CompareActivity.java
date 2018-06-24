package com.example.bhunzb.fortnitepro;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Profile;
import model.StatProperty;

public class CompareActivity extends AppCompatActivity {

    int[] statsIdsProfile = {
            R.id.percent_Wins,
            R.id.percent_WinPerc,
            R.id.percent_TOP10,
            R.id.percent_Kills,
            R.id.percent_KD,
            R.id.percent_KPM,
    };

    int[] statsIdsProfile2 = {
            R.id.percent_Wins2,
            R.id.percent_WinPerc2,
            R.id.percent_TOP10_2,
            R.id.percent_Kills2,
            R.id.percent_KD2,
            R.id.percent_KPM2,
    };

    Profile profile2;
    Profile profile;

    String[] statsname = {
            "WINS",
            "WINS %",
            "TOP 10",
            "KILLS",
            "KD",
            "KILLS / MATCH",
    };

    private String playerName;
    private String playerName2;
    private String platform;
    private String platform2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        initateContent();
        Intent intent = getIntent();
        playerName = intent.getStringExtra("player_name");
        playerName2 = intent.getStringExtra("player_name_to_compare");
        platform = intent.getStringExtra("platform");
        platform2 = intent.getStringExtra("platform_to_compare");

        convertPlatforms();

        getPlayer(playerName, platform);
    }

    private void convertPlatforms(){
        if(platform.equals("") || platform.equals("PC")){
            platform = "pc";
        }else if(platform.equals("XBOX")){
            platform = "xbl";
        }else{
            platform = "psn";
        }

        if(platform2.equals("") || platform2.equals("PC")){
            platform2 = "pc";
        }else if(platform2.equals("XBOX")){
            platform2 = "xbl";
        }else{
            platform2 = "psn";
        }
    }

    private void initateContent() {
        for (int i = 0; i < statsIdsProfile.length; i++) {
            View view = findViewById(statsIdsProfile[i]);
            TextView nameView = (TextView) view.findViewById(R.id.property_name);
            nameView.setText(statsname[i]);

            view = findViewById(statsIdsProfile2[i]);
            nameView = (TextView) view.findViewById(R.id.property_name);
            nameView.setText(statsname[i]);
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
                        parseProfile(response);
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
        queue.add(stringRequest);
    }

    private void parseProfile(String response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(profile == null){
                profile = mapper.readValue(response, Profile.class);
                getPlayer(playerName2, platform2);
            }else{
                profile2 = mapper.readValue(response, Profile.class);
                initiatePropertys();
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private void initiatePropertys() {
        TextView titleName = findViewById(R.id.playerName1);
        TextView titleName2 = findViewById(R.id.playerName2);
        titleName.setText(profile.epicUserHandle);
        titleName2.setText(profile2.epicUserHandle);
        updatePropertys(0);
    }

    private void updatePropertys(int gameType){
        List<StatProperty> values = new ArrayList<>();
        List<StatProperty> values2 = new ArrayList<>();
        if(gameType == 0){
            values = getValuesOfSolo(profile, R.id.percent_TOP10);
            values2 = getValuesOfSolo(profile2, R.id.percent_TOP10_2);
        }else if(gameType == 1){
            values = getValuesOfDuo(profile, R.id.percent_TOP10);
            values2 = getValuesOfDuo(profile2, R.id.percent_TOP10_2);
        }else{
            values = getValuesOfSquad(profile, R.id.percent_TOP10);
            values2 = getValuesOfSquad(profile2, R.id.percent_TOP10_2);
        }

        for(int i = 0; i < values.size(); i++){
            setPlayerStats(values.get(i), statsIdsProfile[i]);
            setPlayerStats(values2.get(i), statsIdsProfile2[i]);
        }
    }

    private void setPlayerStats(StatProperty value, int id){
        View view = findViewById(id);
        View percentilBar = view.findViewById(R.id.property_foreground);
        View backgroundBar = view.findViewById(R.id.property_background);
        TextView textView = (TextView)view.findViewById(R.id.property_value);
        int height = backgroundBar.getLayoutParams().height;
        double width = backgroundBar.getLayoutParams().width;

        double currentWidth;
        if(value.percentile == 0.0){
            currentWidth = 1;
        }else{
            currentWidth = width / 100 * (100 - value.percentile);
        }

        int newWidth = (int)Math.round(currentWidth);

        percentilBar.setLayoutParams(new ConstraintLayout.LayoutParams(newWidth, height));
        textView.setText(value.displayValue);
    }

    private List getValuesOfSolo(Profile profile, int viewId){
        View view = findViewById(viewId);
        TextView nameView = (TextView)view.findViewById(R.id.property_name);
        nameView.setText("TOP 10");

        List<StatProperty> values = new ArrayList<>();
        values.add(profile.stats.p2.top1);
        values.add(profile.stats.p2.winRatio);
        values.add(profile.stats.p2.top10);
        values.add(profile.stats.p2.kills);
        values.add(profile.stats.p2.kd);
        values.add(profile.stats.p2.kpg);
        return values;
    }

    private List getValuesOfDuo(Profile profile, int viewId){
        View view = findViewById(viewId);
        TextView nameView = (TextView)view.findViewById(R.id.property_name);
        nameView.setText("TOP 5");

        List<StatProperty> values = new ArrayList<>();
        values.add(profile.stats.p10.top1);
        values.add(profile.stats.p10.winRatio);
        values.add(profile.stats.p10.top5);
        values.add(profile.stats.p10.kills);
        values.add(profile.stats.p10.kd);
        values.add(profile.stats.p10.kpg);
        return values;
    }

    private List getValuesOfSquad(Profile profile, int viewId){
        View view = findViewById(viewId);
        TextView nameView = (TextView)view.findViewById(R.id.property_name);
        nameView.setText("TOP 3");

        List<StatProperty> values = new ArrayList<>();
        values.add(profile.stats.p9.top1);
        values.add(profile.stats.p9.winRatio);
        values.add(profile.stats.p9.top3);
        values.add(profile.stats.p9.kills);
        values.add(profile.stats.p9.kd);
        values.add(profile.stats.p9.kpg);
        return values;
    }
}
