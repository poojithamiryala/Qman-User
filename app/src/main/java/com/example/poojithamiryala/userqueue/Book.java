package com.example.poojithamiryala.userqueue;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.poojithamiryala.userqueue.Azure.Diag;
import static com.example.poojithamiryala.userqueue.Azure.Hospital;
import static com.example.poojithamiryala.userqueue.Azure.category;
import static com.example.poojithamiryala.userqueue.Azure.college;
import static com.example.poojithamiryala.userqueue.Azure.mClient;
import static com.example.poojithamiryala.userqueue.Azure.mOrgan;
import static com.example.poojithamiryala.userqueue.Azure.madmin;
import static com.example.poojithamiryala.userqueue.Azure.mbook;
import static com.example.poojithamiryala.userqueue.Azure.others;
import static com.example.poojithamiryala.userqueue.Azure.restu;
import static java.sql.Types.TIME;
import static java.sql.Types.TIMESTAMP;

public class Book extends AppCompatActivity{
    EditText orgname1;
    public EditText cat;
    EditText location;
    EditText service;
    EditText ETA;
    Long t;
    Button save;
    MobileServiceList<BookUser> result;
    BookUser item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        cat=(EditText)findViewById(R.id.category);
        orgname1=(EditText)findViewById(R.id.orgname);
        location=(EditText)findViewById(R.id.loc);
        service=(EditText)findViewById(R.id.service);
        ETA=(EditText)findViewById(R.id.eta);
        save=(Button)findViewById(R.id.save);
        if(!Azure.category.equals(""))
        {
            cat.setText(Azure.category);
        }
        if(!Azure.organname.equals(""))
        {
            orgname1.setText(Azure.organname);
        }
        if(!Azure.location.equals(""))
        {
            location.setText(Azure.location);
        }
        if(!Azure.service.equals(""))
        {
            service.setText(Azure.service);
        }
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),BookSlot.class);
                startActivity(i);
            }
        });
        orgname1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Azure.category.equals(""))
                {
                    Intent i = new Intent(getApplicationContext(), Bookslot2.class);
                    Log.e("cat",Azure.category);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select category",Toast.LENGTH_LONG).show();
                }
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Azure.category.equals("") && !Azure.organname.equals("")) {
                    Intent i = new Intent(getApplicationContext(), Bookslot3.class);
                    startActivity(i);
                }
                else if(Azure.category.equals("") && Azure.organname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Select category and organization name",Toast.LENGTH_LONG).show();
                }
                else if(Azure.organname.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Select organization name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select category",Toast.LENGTH_LONG).show();
                }
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Azure.category.equals("") && !Azure.organname.equals("") && !Azure.location.equals("")) {
                    Intent i = new Intent(getApplicationContext(), Bookslot4.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select category,organization name,location",Toast.LENGTH_LONG).show();
                }
            }
        });
        ETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Azure.category.equals("") && !Azure.organname.equals("") && !Azure.location.equals("") && !Azure.service.equals("")){
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Book.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            ETA.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Select category,organization name,location,service",Toast.LENGTH_LONG).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Azure.service.equals("") && !Azure.location.equals("") && !Azure.organname.equals("") && !Azure.category.equals("") && !ETA.getText().toString().equals(""))
                {
                    //java.util.Date today = new java.util.Date();
                  //  System.out.println(new java.sql.Timestamp(today.getTime()));
                  //  t=tsToSec8601(new java.sql.Timestamp(today.getTime()).toString());
                    t=Calendar.getInstance().getTime().getTime();
                  addItem(view);
                         //additem1();
                    //Toast.makeText(getApplicationContext(),"done"+tsToSec8601(new java.sql.Timestamp(today.getTime()).toString()),Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter all fields",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    public void addItem(View v)
    {
        if (mClient == null) {
            return;
        }
        item = new BookUser();
        item.setCategory(Azure.category);
        item.setContact(Azure.contact);
        item.setOrgname(Azure.organname);
        item.setEta(ETA.getText().toString());
        item.setLocation(Azure.branch+","+Azure.city);
        item.setService(Azure.service);
        item.setAllotedno(0);
        item.setTime("");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        item.setDate(df.format(Calendar.getInstance().getTime()));
        item.setTimestamp(t+"");
        final String[] etime = new String[1];
        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    addItemInTable(item);
                    int count=0;
                    List<BookUser> result1=mbook.where().field("category").eq(Azure.category).and().field("location").eq(Azure.location).and().field("service").eq(Azure.service).and().field("orgname").eq(Azure.organname).orderBy("timestamp",QueryOrder.Ascending).execute().get();
                    List<AdminQ> y=madmin.where().
                            field("service").eq(Azure.service).
                            and().field("orgname").eq(Azure.organname).
                            execute().get();
                    for(BookUser a:result1)
                    {
                        if(!(a.getTimestamp().equals(t)) && !(a.getContact().equals(Azure.contact)))
                        {
                            count++;
                        }
                        else if(!(a.getTimestamp().equals(t)) && (a.getContact().equals(Azure.contact)))
                        {
                            count++;
                            a.setAllotedno(count);
                            long time= (int) y.get(0).getTime();
                            etime[0] =String.valueOf(Calendar.getInstance().getTime().getTime()+((count-1)*time));
                            a.setTime(etime[0]);
                            updateItemInTable(a);
                            break;
                        }
                    }
                    Intent i=new Intent(getApplicationContext(),Token.class);
                    Bundle b=new Bundle();
                    b.putString("Alloted",count+"");
                    b.putString("Time",convertDate(etime[0],"dd/MM/yyyy hh:mm:ss"));
                    i.putExtras(b);
                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {

                        }
                    });
                }
                catch (final Exception e)
                {
                    createAndShowDialogFromTask(e, "Error");
                    //Toast.makeText(getApplicationContext(),"Username already in use!!",Toast.LENGTH_LONG).show();
                }
                return null;
            }
        };
        runAsyncTask(task);
        cat.setText("");
        orgname1.setText("");
        location.setText("");
        service.setText("");
        ETA.setText("");
    }
    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
    /**
     * Add an item to the Mobile Service Table
     *
     * @param item
     *            The item to Add
     */
    public BookUser addItemInTable(BookUser item) throws ExecutionException, InterruptedException {
        BookUser entity = Azure.mbook.insert(item).get();
        return entity;
    }
    public void deleteItemInTable(BookUser item) throws ExecutionException, InterruptedException {
        Azure.mbook.delete(item);
    }
    public BookUser updateItemInTable(BookUser item) throws ExecutionException, InterruptedException {
        BookUser entity = Azure.mbook.update(item).get();
        return entity;
    }
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    /**
     * Run an ASync task on the corresponding executor
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public static Long tsToSec8601(String timestamp) {
        if (timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (epoch);
        } catch (ParseException e) {
            return null;
        }
    }
}
