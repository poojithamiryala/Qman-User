package com.example.poojithamiryala.userqueue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poojitha miryala on 07-04-2018.
 */

public class Event extends AppCompatActivity {
    TextView categ,location,organisation,service,date,time,token;
    BookUser selectedlist;
    Bundle  b;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
       token=(TextView)findViewById(R.id.token);
        categ=(TextView)findViewById(R.id.cat);
        location=(TextView)findViewById(R.id.loc);
        organisation=(TextView)findViewById(R.id.org);
        date=(TextView)findViewById(R.id.date);
       time=(TextView)findViewById(R.id.time);
        service=(TextView)findViewById(R.id.service);
        Intent i=getIntent();
        selectedlist= (BookUser) i.getSerializableExtra("org");
         token.setText(selectedlist.getAllotedno());
        time.setText(selectedlist.getTime());
        date.setText(selectedlist.getDate());
        service.setText(selectedlist.getService());
        organisation.setText(selectedlist.getOrgname());
        categ.setText(selectedlist.getCategory());
        location.setText(selectedlist.getLocation());


    }
}
