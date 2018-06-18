package com.example.bhunzb.fortnitepro;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
