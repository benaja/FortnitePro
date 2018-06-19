package com.example.bhunzb.fortnitepro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavouriteListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final List<String> title;
    private final List<Integer> imgid;
    private final List<String> description;

    public FavouriteListAdapter(Activity context, List<String> title, List<String> description, List<Integer> imgid) {
        super(context, R.layout.favourite_list, title);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.title=title;
        this.imgid=imgid;
        this.description=description;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.favourite_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ItemIcon);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.Itemdescription);

        txtTitle.setText(title.get(position));
        imageView.setImageResource(imgid.get(position));
        txtDescription.setText(description.get(position));
        return rowView;

    };
}
