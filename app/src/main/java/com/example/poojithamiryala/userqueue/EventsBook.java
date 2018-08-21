package com.example.poojithamiryala.userqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class EventsBook extends AppCompatActivity {
    TextView categ1,location1,organisation1,service1,date1,time1,token1;
    BookUser selectedlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_book);
        token1=(TextView)findViewById(R.id.token);
        categ1=(TextView)findViewById(R.id.cat);
        location1=(TextView)findViewById(R.id.loc);
        organisation1=(TextView)findViewById(R.id.org);
        date1=(TextView)findViewById(R.id.date);
        time1=(TextView)findViewById(R.id.time);
        service1=(TextView)findViewById(R.id.service);
        Intent i=getIntent();
        selectedlist= (BookUser)i.getSerializableExtra("org");
        token1.setText("Token:"+selectedlist.getAllotedno());
        time1.setText("Time:"+convertDate(selectedlist.getTime(),"hh:mm:ss"));
        date1.setText("Date:"+selectedlist.getDate());
        service1.setText("Service:"+selectedlist.getService());
        organisation1.setText("Organization:"+selectedlist.getOrgname());
        categ1.setText("Category:"+selectedlist.getCategory());
    }
    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
