package com.example.poojithamiryala.userqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Token extends AppCompatActivity {
TextView orgn;
    Button b1;
    Button b2;
    TextView time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Bundle b=getIntent().getExtras();
        int alloted=Integer.parseInt(b.getString("Alloted"));
        String etime=b.getString("Time");
        b1=(Button)findViewById(R.id.imageButton2);
        b2=(Button)findViewById(R.id.done);
        time=(TextView)findViewById(R.id.time);
        orgn=(TextView)findViewById(R.id.orgname);
        orgn.setText(Azure.organname);
        time.setText(etime);
        b1.setText((alloted-1)+"\t\t\t\t\t\t\t\t"+alloted+"\t\t\t\t\t\t\t\t"+(alloted+1));
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Home.class);
                startActivity(i);
                Azure.category="";
                Azure.city="";
                Azure.location="";
                Azure.branch="";
                Azure.service="";
                Azure.organname="";
            }
        });

    }
}
