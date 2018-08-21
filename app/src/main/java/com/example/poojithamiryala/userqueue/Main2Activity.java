package com.example.poojithamiryala.userqueue;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.example.poojithamiryala.userqueue.Azure.mClient;
import static com.example.poojithamiryala.userqueue.Azure.madmin;
import static com.example.poojithamiryala.userqueue.Azure.mbook;

public class Main2Activity extends AppCompatActivity{
    List<BookUser> result;
    ArrayAdapter<String> adapter;
    ListView events;
    List<BookUser> result1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        result=new ArrayList<BookUser>();
        result1=new ArrayList<BookUser>();
       // adapter=new ArrayAdapter<BookUser>(this,R.layout.s);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        events=(ListView)findViewById(R.id.event);
        events.setAdapter(adapter);
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             BookUser data = (BookUser) result.get(i);
             Intent intent = new Intent(Main2Activity.this,EventsBook.class );
             intent.putExtra("org",data);
             startActivity(intent);
         }
     });
        addItem();
    }
    void func() {
        if (mClient == null) {
            return;
        }
            final int[] currno = new int[1];
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        for (BookUser e : result) {
                            List<BookUser> find_len = mbook.where().field("category").eq(e.getCategory())
                                    .and().field("service").eq(e.getService()).and().
                                            field("location").eq(e.getLocation()).and().
                                            field("orgname").eq(e.getOrgname()).execute().get();
                            final List<BookUser> y = mbook.where().field("category").eq(e.getCategory())
                                    .and().field("contact").eq(e.getContact()).and().field("service").eq(e.getService()).and().
                                            field("location").eq(e.getLocation()).and().field("allotedno").eq(e.getAllotedno()).and().
                                            field("orgname").eq(e.getOrgname()).and().field("eta").eq(e.getEta()).and().
                                            field("date").eq(e.getDate()).and().field("timestamp").eq(e.getTimestamp()).execute().get();
                            List<AdminQ> ad = madmin.
                                    where().field("service").eq(e.getService()).
                                    and().field("orgname").eq(e.getOrgname()).
                                    execute().get();
                            for (AdminQ r : ad)
                            {
                                currno[0] = r.getAdminQ();
                                break;
                            }
                            if (e.getAllotedno() >= currno[0])
                            {
                                y.get(0).setTime(String.valueOf(Calendar.getInstance().getTime().getTime()+ (ad.get(0).getTime()*(find_len.size() - ad.get(0).getAdminQ()))));
                                updateTime(y.get(0));
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result1.clear();
                                adapter.clear();
                                addItem();
                            }
                        });
                    } catch (final Exception e) {
                        //createAndShowDialogFromTask(e, "Error");
                        //Toast.makeText(getApplicationContext(),"Username already in use!!",Toast.LENGTH_LONG).show();
                    }
                    return null;
                }
            };
            runAsyncTask(task);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.action_refresh:
                func();
                break;
        }
        return true;
    }
    public void updateTime(BookUser y)
    {
        mbook.update(y);
    }
    public void addItem()
    {
        if (mClient == null) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String date=(df.format(Calendar.getInstance().getTime()));
        final int[] currno = new int[1];
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    result = mbook.where().field("contact").eq(Azure.contact).execute().get();
                    for(BookUser e:result)
                    {
                        if((new SimpleDateFormat("dd-MMM-yyyy").parse(e.getDate())).
                                compareTo(new SimpleDateFormat("dd-MMM-yyyy").parse(date))>0)
                        {
                            result1.add(e);
                            adapter.add("Category:"+e.getCategory()+"\nOrganization Name:"+e.getOrgname());
                        }
                        else if((new SimpleDateFormat("dd-MMM-yyyy").parse(e.getDate())).
                                compareTo(new SimpleDateFormat("dd-MMM-yyyy").parse(date))==0)
                        {
                          List<AdminQ> y=madmin.
                                  where().field("service").eq(e.getService()).
                                  and().field("orgname").eq(e.getOrgname()).
                                  execute().get();
                            for(AdminQ r:y)
                            {
                                currno[0] =r.getAdminQ();
                                break;
                            }
                            if(e.getAllotedno()>currno[0])
                            {
                                result1.add(e);
                                adapter.add("Category:"+e.getCategory()+"\nOrganization Name:"+e.getOrgname());
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                catch (final Exception e)
                {
                    //createAndShowDialogFromTask(e, "Error");
                    //Toast.makeText(getApplicationContext(),"Username already in use!!",Toast.LENGTH_LONG).show();
                }
                return null;
            }
        };
        runAsyncTask(task);

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



}
