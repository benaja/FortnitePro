package com.example.bhunzb.fortnitepro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import model.Profile;
import model.StatProperty;

public class SingleActivity extends AppCompatActivity {

    private String playerName;
    private String platform;
    private Profile profile;
    private Menu menu;
    private boolean isFavourite = false;
    private int checkIfGameModeHasChange = 0;
    private int checkIfRadioButtonhasChanged = 0;

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
        if(platform.equals("") || platform.equals("PC")){
            platform = "pc";
        }else if(platform.equals("XBOX")){
            platform = "xbl";
        }else{
            platform = "psn";
        }

        getPlayer();

        Button button = (Button) findViewById(R.id.compare_button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("player_name", playerName);
                intent.putExtra("platform", profile.platformNameLong);
                startActivity(intent);
            }
        });

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.select_game_type);
        //create a list of items for the spinner.
        String[] items = new String[]{"Solo", "Duo", "Squad"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
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

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(++checkIfRadioButtonhasChanged > 2){
                    if(checkedId == R.id.radio_button_pc){
                        platform = "pc";
                        getPlayer();
                    }else if(checkedId == R.id.radio_button_ps){
                        platform = "psn";
                        getPlayer();
                    }else if(checkedId == R.id.radio_button_xbox){
                        platform = "xbl";
                        getPlayer();
                    }
                }
            }
        });

        initateContent();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourite_icon, menu);
        this.menu = menu;
        return true;
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

            case R.id.favourite:
                if(isFavourite){
                    isFavourite = false;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.add_favourite));

                    Set<String> stringSet = new HashSet<String>();

                    SharedPreferences favourites = getSharedPreferences("Favourite", 0);
                    Set<String> oldFavouritesStringSet = favourites.getStringSet("element", stringSet);

                    Set<String> newFavouritesStringSet = new HashSet<>();

                    for (String favourite: oldFavouritesStringSet) {
                        String[] favouriteArray = favourite.split(",");
                        String name = favouriteArray[0].toLowerCase();
                        if(!name.equals(profile.epicUserHandle.toLowerCase())){
                            newFavouritesStringSet.add(favourite);
                        }
                    }

                    SharedPreferences.Editor editor = favourites.edit();
                    editor.clear();
                    editor.putStringSet("element", newFavouritesStringSet);
                    editor.commit();

                }else{
                    isFavourite = true;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.is_favourite));
                    Set<String> stringSet = new HashSet<String>();

                    SharedPreferences favourites = getSharedPreferences("Favourite", 0);
                    Set<String> favouritesStringSet = favourites.getStringSet("element", stringSet);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.profile.epicUserHandle);
                    stringBuilder.append(",");
                    stringBuilder.append(this.profile.platformNameLong);
                    stringBuilder.append(",");
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 6);
                    stringBuilder.append(randomNum);
                    favouritesStringSet.add(stringBuilder.toString());

                    SharedPreferences.Editor editor = favourites.edit();
                    editor.clear();
                    editor.putStringSet("element", favouritesStringSet);
                    editor.commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getPlayer() {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        View content = findViewById(R.id.single_view_content);
        content.setVisibility(View.GONE);

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
                            updatePropertys(0);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.single_view_content).setVisibility(View.VISIBLE);
                            findViewById(R.id.single_view_properties).setVisibility(View.VISIBLE);
                            findViewById(R.id.error_text).setVisibility(View.GONE);
                            findViewById(R.id.favourite).setVisibility(View.VISIBLE);

                            checkIfFavourite();
                            //display function call for displaying stats

                        } catch (Exception e) {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.single_view_content).setVisibility(View.VISIBLE);
                            findViewById(R.id.single_view_properties).setVisibility(View.GONE);
                            findViewById(R.id.error_text).setVisibility(View.VISIBLE);
                            findViewById(R.id.favourite).setVisibility(View.GONE);
                        }
                        updateRadioButtons();

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

    private void checkIfFavourite(){
        Set<String> stringSet = new HashSet<String>();

        SharedPreferences favourites = getSharedPreferences("Favourite", 0);
        Set<String> favouritesStringSet = favourites.getStringSet("element", stringSet);

        for (String favourite : favouritesStringSet) {
            String[] favouriteArray = favourite.split(",");
            String name = favouriteArray[0].toLowerCase();
            if(name.equals(profile.epicUserHandle.toLowerCase())){
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.is_favourite));
                isFavourite = true;
            }
        }
    }

    private void updateRadioButtons(){
        if(platform.equals("pc")){
            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
            radioGroup.check(R.id.radio_button_pc);
        }else if(platform.equals("psn")){
            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
            radioGroup.check(R.id.radio_button_ps);
        }else if(platform.equals("xbl")){
            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
            radioGroup.check(R.id.radio_button_xbox);
        }
    }

    private void updatePropertys(int gameType){
        List<StatProperty> values = new ArrayList<>();
        if(gameType == 0){
            values.add(profile.stats.p2.top1);
            values.add(profile.stats.p2.winRatio);
            values.add(profile.stats.p2.kills);
            values.add(profile.stats.p2.kd);
            values.add(profile.stats.p2.top10);
            values.add(profile.stats.p2.kpg);
            View view = findViewById(R.id.top10);
            TextView nameView = (TextView)view.findViewById(R.id.property_name);
            nameView.setText("TOP 10");
        }else if(gameType == 1){
            values.add(profile.stats.p10.top1);
            values.add(profile.stats.p10.winRatio);
            values.add(profile.stats.p10.kills);
            values.add(profile.stats.p10.kd);
            values.add(profile.stats.p10.top5);
            values.add(profile.stats.p10.kpg);
            View view = findViewById(R.id.top10);
            TextView nameView = (TextView)view.findViewById(R.id.property_name);
            nameView.setText("TOP 5");
        }else{
            values.add(profile.stats.p9.top1);
            values.add(profile.stats.p9.winRatio);
            values.add(profile.stats.p9.kills);
            values.add(profile.stats.p9.kd);
            values.add(profile.stats.p9.top3);
            values.add(profile.stats.p9.kpg);
            View view = findViewById(R.id.top10);
            TextView nameView = (TextView)view.findViewById(R.id.property_name);
            nameView.setText("TOP 3");
        }

        for(int i = 0; i < statsIds.length; i++){
            View view = findViewById(statsIds[i]);
            View percentilBar = view.findViewById(R.id.property_foreground);
            View backgroundBar = view.findViewById(R.id.property_background);
            TextView textView = (TextView)view.findViewById(R.id.property_value);
            int height = backgroundBar.getLayoutParams().height;
            double width = backgroundBar.getLayoutParams().width;

            double currentWidth;
            if(values.get(i).percentile == 0.0){
                currentWidth = 1;
            }else{
                currentWidth = width / 100 * (100 - values.get(i).percentile);

            }

            int newWidth = (int)Math.round(currentWidth);


            percentilBar.setLayoutParams(new ConstraintLayout.LayoutParams(newWidth, height));
            textView.setText(values.get(i).displayValue);

        }

    }
}
