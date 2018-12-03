package com.csc420.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceActivity extends Activity {

    ListView servicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_activity);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .8));

        servicesList = findViewById(R.id.servicesList);
//
//        ArrayAdapter<String> arrayAdapter =
//                new ArrayAdapter<>(this,R.layout.service_layout, R.id.textView, Constants.listOfServices);
//
//        servicesList.setAdapter(arrayAdapter);

        ServiceAdapter sd = new ServiceAdapter(this,Constants.listOfServices, Constants.serviceLogos);
        servicesList.setAdapter(sd);

        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("ServiceID", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}

class ServiceAdapter extends ArrayAdapter<String>{

    private Context context;
    private int[] images;
    private String[] titles;

    ServiceAdapter(Context c, String[] titles, int[] images){
        super(c, R.layout.service_layout, R.id.textView, titles);
        this.context = c;
        this.images = images;
        this.titles = titles;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.service_layout, parent, false);
        ImageView image = row.findViewById(R.id.imageView);
        TextView title = row.findViewById(R.id.textView);

        image.setImageResource(images[position]);
        title.setText(titles[position]);
        title.setTextColor(Color.WHITE);

        return row;
    }
}
