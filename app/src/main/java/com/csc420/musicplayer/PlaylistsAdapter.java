package com.csc420.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistsAdapter extends ArrayAdapter<String>{

    private Context context;
    private String[] playlistNames;

    PlaylistsAdapter(Context c, String[] names){
        super(c, R.layout.service_layout, R.id.textView, names);
        this.context = c;
        playlistNames = names;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.playlist_first_cell, parent, false);
//            ImageView image = row.findViewById(R.id.imageView);
//            TextView title = row.findViewById(R.id.textView);
//
//            image.setImageResource(images[position]);
//            title.setText(titles[position]);
//            title.setTextColor(Color.WHITE);

            final EditText playlistName = row.findViewById(R.id.editText);
            Button btnAddPlaylist = row.findViewById(R.id.btnAddPlaylist);

            btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = playlistName.getText().toString();
                    if(name.length() > 0) {
                        //playListsList.add(name);
                    }
                }
            });

            return row;
        }else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.service_layout, parent, false);
            ImageView image = row.findViewById(R.id.imageView);
            TextView title = row.findViewById(R.id.textView);

            image.setImageResource(R.drawable.local_logo);
            title.setText(playlistNames[position]);
            title.setTextColor(Color.WHITE);

            return row;
        }
    }

}
