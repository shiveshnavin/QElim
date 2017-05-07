package nf.co.hoproject.genapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.DataBase.FireConf;
import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.OrderFormat;

public class BookingResultGeneric extends AppCompatActivity {



    @Bind(R.id.parent)
    View parent;



    @Bind(R.id.share)
    ImageView share;


    @Bind(R.id.cancel)
    ImageView cancel;


    @Bind(R.id.map)
    ImageView map;



    @Bind(R.id.item)
    TextView item;
    @Bind(R.id.user)
    TextView user;
    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.quan)
    TextView quan;
    @Bind(R.id.reward)
    TextView reward;
    @Bind(R.id.chef)
    TextView chef;
    @Bind(R.id.est)
    TextView est;


    String  exp;

    OrderFormat order;

    ProgressDialog progressDialog;
    String jstr;
    JSONObject job;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_result_generic);

        ctx=this;
        ButterKnife.bind(this);

        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database");

        listenFirebase();


        parent.setVisibility(View.INVISIBLE);
        jstr=getIntent().getStringExtra("jstr");
        AndroidNetworking.initialize(ctx);
        setupToolbar();
        exp=getIntent().getStringExtra("exp");

        activatePallet();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if(jstr!=null)
        {
            Gson g=new Gson();
            order=g.fromJson(jstr,OrderFormat.class);

            item.setText(order.itemname);
            user.setText(order.user);
            total.setText("£ "+order.price);
            quan.setText(order.quan);
            reward.setText("0 Coins");
            chef.setText(order.outlet);
            est.setText(order.est + " Mins");


            try {
                fire.child("orders").push().setValue(order);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            parent.setVisibility(View.VISIBLE);

        }






    }


    public void set()
    {
        {
            try {
                job=new JSONObject(jstr);

                progressDialog=new ProgressDialog(ctx);
                progressDialog.setMessage("Requesting...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Log.d("EXP RECIEVED",exp);
                Gson jj=new Gson();
                order=jj.fromJson(exp,OrderFormat.class);



                Log.d("URL",getResources().getString(R.string.server)+"/place.php?json="+ (exp)+
                        "&email="+(order.email)+"&pid="+order.fid);


                String url=getResources().getString(R.string.server)+"/place.php?json="+ URLEncoder.encode(exp)+
                        "&email="+utl.getD(order.email)+"&pid="+order.fid;

                Log.d("URL",url);


                AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

/*
                        Log.d("RESPONSE OF ORDER"," "+response);
                        if(response.contains("{"))
                        {


                            parent.setVisibility(View.VISIBLE);
                            try {
                                JSONObject job=new JSONObject(response);
                                event.setText(job.getString("ename"));
                                guest.setText(job.getString("pguest"));


                                try {

                                    date.setText(job.getString("date_book"));


                                }catch (Exception e)
                                { String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                                    date.setText(currentDateandTime);

                                    e.printStackTrace();
                                }

                                try {
                                    cost.setText(job.getString("cost"));



                                }catch (Exception e)
                                {
                                    cost.setText("0");


                                    e.printStackTrace();
                                }


                                try {

                                    reward.setText(job.getString("reward"));


                                }catch (Exception e)
                                {
                                    reward.setText("0");

                                    e.printStackTrace();
                                }


                                try {

                                    loc.setText(job.getString("location"));


                                }catch (Exception e)
                                {
                                    loc.setText("Hard Rock Cafe, New Delhi");

                                    e.printStackTrace();
                                }


                                try {


                                    edate.setText(job.getString("date_event"));


                                }catch (Exception e)
                                {
                                    edate.setText("June 19 , 7:30 PM Onwards");

                                    e.printStackTrace();
                                }

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }
                        else
                        {
                            utl.toast(ctx, "Order Failed : The server denied your request or you are not conncted to Internet.");
                            finish();
                        }
*/
                    }

                    @Override
                    public void onError(ANError ANError) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void setupToolbar() {

        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_settings:

                startActivity(new Intent(ctx,MyWallet.class));
                ;


            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void activatePallet()
    {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String shareBody = "Hi ! I just ordered food from this awesome app 'Myso Meal' . Download it now here : "+getResources().getString(R.string.website);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,("Share App")));




            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.diag(ctx,"Cancel Order","The Orders for the item in Pilot Run are for Promotions " +
                        ". These Orders cannot be cancelled . \n");


            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", "28.5272", "77.2171");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);*/

                double latitude = 28.5272;
                double longitude = 77.2171;
                String coordinates = String.format("geo:0,0?q=" + latitude + "," + longitude);
                Intent intentMap = new Intent( Intent.ACTION_VIEW, Uri.parse(coordinates) );
              //  startActivity( intentMap );

                utl.diag(ctx,"Track","This feature gives live tracking of your deal on your phone , from Frying Pan to your Plate .");

            }
        });



    }


    /***********************FIREBASE SETUP*************************************/

    Context ctx;
    Firebase fire;
    ListFlowModel db;
    public void listenFirebase()
    {



        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

/************************************************************/

                    try {
                        db = postSnapshot.getValue(ListFlowModel.class);
                        Log.d("CAT 0", db.categories.get(0).categoryName);
                        ArrayList<FlowModel> f = db.categories;
                        int size = f.size();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("RES", postSnapshot.toString());


/************************************************************/


                    break;  }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    /*************************FIREBASE SETUP***********************************/



}
