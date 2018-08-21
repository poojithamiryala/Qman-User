package com.example.poojithamiryala.userqueue;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import static com.example.poojithamiryala.userqueue.Azure.mClient;
import static com.example.poojithamiryala.userqueue.Azure.mOrgan;

public class Bookslot4 extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    private ListView lv;

    // Search EditText
    EditText inputSearch;
    Button but;
    String s;
    String orgname;
    String city;
    String branch;
    List<Organization_details> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookslot4);
        Bundle b = getIntent().getExtras();
        s = Azure.category;
        orgname=Azure.organname;
      /*  String loc=Azure.location;
        int y=loc.indexOf(',');
        branch=loc.substring(0,y);*/
      city=Azure.city;
        branch=Azure.branch;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(adapter);
        getAll();
        but = (Button) findViewById(R.id.next);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputSearch.setText(lv.getItemAtPosition(i).toString());
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Book.class);
                Azure.service=inputSearch.getText().toString();
                startActivity(i);
            }
        });
    }

    void getAll() {
        final int[] index = {0};
        if (mClient == null) {
            return;
        }
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    result = mOrgan.where().field("category").eq(s).execute().get();
                    Log.e("hospital", result.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            adapter.clear();
                            for (Organization_details i : result)
                            {
                                if(i.getName().equals(orgname) && i.getCity().equals(city) && i.getBranches().equals(branch)) {
                                    Log.e("hospitalsssssssssssss", i.getCategory().toString());
                                    adapter.addAll(Lists.newArrayList(Splitter.on(",").split(i.getServices())));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
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
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    /**
     * Run an ASync task on the corresponding executor
     *
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
