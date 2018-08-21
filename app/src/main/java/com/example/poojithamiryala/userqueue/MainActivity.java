package com.example.poojithamiryala.userqueue;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.poojithamiryala.userqueue.Azure.Diag;
import static com.example.poojithamiryala.userqueue.Azure.Hospital;
import static com.example.poojithamiryala.userqueue.Azure.mClient;
import static com.example.poojithamiryala.userqueue.Azure.mOrgan;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    Button b;
    MobileServiceClient mclient;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed=(EditText)findViewById(R.id.phoneno);
        b=(Button)findViewById(R.id.submit);
        try {
            mclient = new MobileServiceClient("https://queueman.azurewebsites.net", this);
            Azure.mClient = mclient;
            mclient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Azure.mOrgan=Azure.mClient.getTable(Organization_details.class);
        Azure.mbook=Azure.mClient.getTable(BookUser.class);
        Azure.madmin=Azure.mClient.getTable(AdminQ.class);
        getAll();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar = new ProgressDialog(view.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Wait for few secs ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                progressBarStatus = 0;
                new Thread(new Runnable() {
                    public void run() {
                        while (progressBarStatus < 100000) {
                            progressBarStatus = 150;

                            try {
                                Thread.sleep(10000000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            progressBarbHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        }
                    }
                }).start();
                if(ed.getText().toString().trim()!="" && ed.getText().toString().trim().length()==10)
                {
                    Intent i=new Intent(getApplicationContext(),Home.class);
                    //Bundle bundle=new Bundle();
                    //bundle.putString("phoneno",(ed.getText().toString()));
                    //i.putExtras(bundle);
                    Azure.contact=ed.getText().toString();
                    startActivity(i);
                }
            }
        });
    }
    void getAll()
    {
        if (mClient == null) {
            return;
        }
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Azure.college = mOrgan.where().field("category").eq("College").execute().get();
                    Azure.Hospital = mOrgan.where().field("category").eq("Hospital").execute().get();
                    Azure.others = mOrgan.where().field("category").eq("Other").execute().get();
                    Azure.restu = mOrgan.where().field("category").eq("Restuarant").execute().get();
                    Azure.Diag = mOrgan.where().field("category").eq("Diagnostic center").execute().get();
                    Log.e("College", Diag.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
    /*public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Are you sure want to exit?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }*/
}
