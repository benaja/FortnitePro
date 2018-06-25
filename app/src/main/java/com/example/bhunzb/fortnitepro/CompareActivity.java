package com.example.bhunzb.fortnitepro;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    private int checkIfGameModeHasChange = 0;

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
        updateRadioButtons();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Spieler vergleichen");
        }

        Spinner dropdown = findViewById(R.id.select_game_type);
        //create a list of items for the spinner.

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(++checkIfGameModeHasChange > 1){
                    updatePropertys(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        RadioGroup radioGroup = findViewById(R.id.platform_radio_buttons);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                profile = null;
                if(checkedId == R.id.radio_button_pc){
                    platform = "pc";
                    getPlayer(playerName, platform);
                }else if(checkedId == R.id.radio_button_ps){
                    platform = "psn";
                    getPlayer(playerName, platform);
                }else if(checkedId == R.id.radio_button_xbox) {
                    platform = "xbl";
                    getPlayer(playerName, platform);
                }
            }
        });

        RadioGroup radioGroup2 = findViewById(R.id.platfomr_radio_buttons_2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                profile2 = null;
                if(checkedId == R.id.radio_button_pc){
                    platform2 = "pc";
                    getPlayer(playerName2, platform2);
                }else if(checkedId == R.id.radio_button_ps){
                    platform2 = "psn";
                    getPlayer(playerName2, platform2);
                }else if(checkedId == R.id.radio_button_xbox){
                    platform2 = "xbl";
                    getPlayer(playerName2, platform2);
                }
            }
        });

        getPlayer(playerName, platform);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
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
        findViewById(R.id.compare_content).setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "https://api.fortnitetracker.com/v1/profile/"+platform+ "/" + playerName;
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
        findViewById(R.id.compare_content).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        try {
            if(profile == null && profile2 == null){
                profile = mapper.readValue(response, Profile.class);
                getPlayer(playerName2, platform2);
            }else if(profile != null && profile2 == null){
                profile2 = mapper.readValue(response, Profile.class);
                initiatePropertys();
            }else{
                profile = mapper.readValue(response, Profile.class);
                initiatePropertys();
            }
            findViewById(R.id.stats_player_1).setVisibility(View.VISIBLE);
            findViewById(R.id.stats_player_2).setVisibility(View.VISIBLE);
            findViewById(R.id.error_player_1).setVisibility(View.GONE);
            findViewById(R.id.error_player_2).setVisibility(View.GONE);
        } catch (Exception e) {
            if(profile == null && profile2 == null){
                findViewById(R.id.stats_player_1).setVisibility(View.GONE);
                findViewById(R.id.stats_player_2).setVisibility(View.GONE);
                findViewById(R.id.error_player_1).setVisibility(View.VISIBLE);
                findViewById(R.id.error_player_2).setVisibility(View.VISIBLE);
            }else if(profile != null && profile2 == null){
                findViewById(R.id.stats_player_2).setVisibility(View.GONE);
                findViewById(R.id.error_player_2).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.stats_player_1).setVisibility(View.GONE);
                findViewById(R.id.error_player_1).setVisibility(View.VISIBLE);
            }
        }
    }

    private void initiatePropertys() {
        TextView titleName = findViewById(R.id.playerName1);
        TextView titleName2 = findViewById(R.id.playerName2);
        titleName.setText(profile.epicUserHandle);
        titleName2.setText(profile2.epicUserHandle);
        Spinner dropdown = findViewById(R.id.select_game_type);
        int selectedRadioButtonId = dropdown.getSelectedItemPosition();
        updatePropertys(selectedRadioButtonId);
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
    private void updateRadioButtons(){
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.platform_radio_buttons);
        if(platform.equals("pc")){
            radioGroup.check(R.id.radio_button_pc);
        }else if(platform.equals("psn")){
            radioGroup.check(R.id.radio_button_ps);
        }else if(platform.equals("xbl")){
            radioGroup.check(R.id.radio_button_xbox);
        }

        radioGroup = (RadioGroup)findViewById(R.id.platfomr_radio_buttons_2);
        if(platform2.equals("pc")){
            radioGroup.check(R.id.radio_button_pc);
        }else if(platform2.equals("psn")){
            radioGroup.check(R.id.radio_button_ps);
        }else if(platform2.equals("xbl")){
            radioGroup.check(R.id.radio_button_xbox);
        }
    }

}
