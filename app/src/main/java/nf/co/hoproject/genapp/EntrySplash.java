package nf.co.hoproject.genapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nf.co.hoproject.genapp.DataBase.FireConf;
import nf.co.hoproject.genapp.inner.ui.pages.ListActivity;


public class EntrySplash extends AppCompatActivity {

    Firebase fire;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_entry_splash);

        ImageView spl_lm = (ImageView) findViewById(R.id.spl);
        Picasso.with(this).load(R.drawable.icoo).into(spl_lm);

        ctx = this;
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Runnable r=new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ctx, Splash.class);


                if(utl.readData(ctx)!=null)
                {
                    i = new Intent(ctx, ListActivity.class);

                }

                startActivity(i);
                finish();

                // startActivity(new Intent(Splash.this, ListActivity.class));
            }
        };


        Handler h = new Handler();
        h.postDelayed(r, 2000);

        AndroidNetworking.initialize(ctx);
        AndroidNetworking.get(getString(R.string.server2) + "status.php?q=lamp&u=" + URLEncoder.encode(getUser()) + "&t=" + System.currentTimeMillis()).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
            /*    Log.d("re",""+response);
                final Intent i = new Intent(ctx, Splash.class);

                Runnable r=new Runnable() {
                    @Override
                    public void run() {


                    //    startActivity(i);
                    //    finish();

                        // startActivity(new Intent(Splash.this, ListActivity.class));
                    }
                };

                Handler h=new Handler();
                if(response.contains("cool"))
                    h.postDelayed(r,1000);
                else

                    utl.diagExit(EntrySplash.this,ctx,"Error Occurred","Please check if you are connected to internet and restart .");

*/
                    }

                    @Override
                    public void onError(ANError ANError) {
                        utl.diagExit(EntrySplash.this, ctx, "Error Occurred", "Please check if you are connected to internet and restart .");

                        Log.d("re", "" + ANError.toString());
                    }
                });


        Firebase.setAndroidContext(this);
        fire = new Firebase(Constants.fire(ctx)+"/database");
       // fire.child("usage").push().setValue("" + getUser() + " TIME : " + currentDateandTime);

        if (!isNetworkConnected()) {
            utl.diagExit(this, ctx, "Error Occurred", "Please check if you are connected to internet and restart .");

        } else {
            //listenFirebase();

        }

    }

    public String getUser() {
        try {
            AccountManager man = AccountManager.get(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {


                return "";
            }
            Account[] acc = man.getAccountsByType("com.google");
            List<String> pos=new LinkedList<String>();
            for(Account a:acc)
            {
                pos.add(a.name);

            }

            if(!pos.isEmpty()&&pos.get(0)!=null)
            {
                String em=pos.get(0);
                return em;
            }}catch (Exception e)
        {
            e.printStackTrace();
        }
        return "null";

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    ProgressDialog pd;
    public void listenFirebase() {


        fire=new Firebase(Constants.fire(ctx)+"/database"+"status");
        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Boolean ok=false;
                if(pd!=null)
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

/************************************************************/


                    try {
                        Log.d("SNAP",postSnapshot.toString());
                        if(snapshot.getKey().equalsIgnoreCase("status"))
                        ok=(Boolean)postSnapshot.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

/************************************************************/

                }

                Runnable r=new Runnable() {
                    @Override
                    public void run() {
                          Intent i = new Intent(ctx, Splash.class);


                        if(utl.readData(ctx)!=null)
                        {
                            i = new Intent(ctx, ListActivity.class);

                        }

                        startActivity(i);
                        finish();

                        // startActivity(new Intent(Splash.this, ListActivity.class));
                    }
                };

                Handler h=new Handler();
                if(ok)
                h.postDelayed(r,1000);
                else

                    utl.diagExit(EntrySplash.this,ctx,"Error Occurred","Please check if you are connected to internet and restart .");


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



}

