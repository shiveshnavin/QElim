package nf.co.hoproject.genapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.DataBase.FireConf;
import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.DataBase.FoodItem;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.OrderFormat;
import nf.co.hoproject.genapp.DataBase.Request;
import nf.co.hoproject.genapp.DataBase.SubCat;
import nf.co.hoproject.genapp.inner.ui.pages.Booking;

public class Book extends AppCompatActivity {

    @Bind(R.id.description)
    TextView desc;

    @Bind(R.id.order)
    View order;

    @Bind(R.id.mainpic)
    ImageView pic;

    GenericUser guser;
    @Bind(R.id.coupon)
    EditText coupon;

    @Bind(R.id.quan)
    TextView quan;
    @Bind(R.id.name)
    TextView mainname;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.outlet)
    TextView outlet;
    @Bind(R.id.est)
    TextView esttime;

    @Bind(R.id.ratings)
    RatingBar ratings;

    String tPrice;
    String  jstr;
    String fid;
    Context ctx;
    String vid;
    String name;

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

        setContentView(R.layout.activity_book);
        setupToolbar();

        ctx=this;
        ButterKnife.bind(this);

        name=getIntent().getStringExtra("name");
        fid=getIntent().getStringExtra("fid");
        if(fid==null)
        {
            finish();
        }
        if(name!=null)
        try {
            mainname.setText(name);
        }catch (Exception e)
        {
            mainname.setText("Dal Makhani");
            e.printStackTrace();
        }
        vid=getIntent().getStringExtra("fid");
        jstr=getIntent().getStringExtra("guser");



        guser=utl.readData(ctx);
        Log.d("GUSER",guser.name);


        getVidDet(vid);
        Log.d("vid", " " + vid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dig();

            }
        });



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utl.toast(ctx,"Placing Order...");

               // User u=utl.getCuruserGeneric();
                JSONObject jo=new JSONObject();
                Request order=new Request();
                String exp="";

                try {

                    jo=new JSONObject();
                    String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    order.slot=currentDateandTime;
                    order.quan=quanitiy[sel];
                    order.user=guser.name;
                    order.email=guser.email;
                    order.phone=guser.phone;
                    order.est=food.est;

                    order.uid=guser.uid;
                    order.price=tPrice;
                    order.itemname=food.itemName;
                    order.outlet=food.chef;
                    order.fid=food.fid;
                    order.item=food;



                    Gson js=new Gson();
                    exp=js.toJson(order,Request.class);
                    Log.d("EXPORT",exp.toString());




                Intent i=new Intent(ctx,Pay.class);
                i.putExtra("jstr",exp);
                i.putExtra("exp",exp);
                i.putExtra("cat",exp);
                FileOperations fop = new FileOperations();
                String folder = Constants.getFolder(ctx);

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr = fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);

                }

                startActivity(i);

              /*  Intent i=new Intent(ctx,BookingResultGeneric.class);
                i.putExtra("jstr",exp);
                i.putExtra("exp",exp);
                startActivity(i);*/

                }catch (Exception e)
                {
                    e.printStackTrace();
                    utl.toast(ctx,"Please fill all fields first .");
                }

            }
        });


        setTitle("");

        listenFirebase();
    }


    public void getVidDet(String vid)
    {
      //return image of vid and shit
    }

    String [] nop={"1","2","3","4","5","6","7","8","9","10"};


    int sel=0;


    String [] quanitiy={"1","2","3","4","5","6","7","8","9","10",};//={"Kathhak","Salsa","Western", "Bhangda","Traditional","Garbha"};


    String selectedFrame=null;
    public void dig()
    {

        new AlertDialog.Builder(ctx)
                .setSingleChoiceItems(quanitiy,0,null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();;
                        sel=((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();



                        Log.d("Selection ", "" + quanitiy[(sel)]);
                        quan.setText(quanitiy[sel] + "");
                        tPrice=""+(Integer.parseInt(quanitiy[sel])*food.priceInt);
                        price.setText("Rs "+tPrice);



                    }





                }).show();

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



    /***********************FIREBASE SETUP*************************************/

    Firebase fire;
    ListFlowModel db;
    public void listenFirebase()
    {

       // Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(ctx);

        fire= new Firebase(Constants.fire(ctx)+"/database");


        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int i = 0;

/************************************************************/

                    try {
                        db = snapshot.getValue(ListFlowModel.class);
                        Log.d("CAT 0", db.categories.get(0).categoryName);

                        ArrayList<FlowModel> f=db.categories;

                        String foodStr=getIntent().getStringExtra("cat");

                        food=(new Gson()).fromJson(foodStr,FoodItem.class);

                        desc.setText(food.desc);
                        mainname.setText(food.itemName);
                        price.setText("Rs. "+food.price);
                        tPrice=food.price;
                        outlet.setText(food.chef);
                        ratings.setRating(food.rating.floatValue());
                        esttime.setText(food.est + " Min");
                        Picasso.with(ctx).load(food.icon).placeholder(R.drawable.plcf).into(pic);




                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



/************************************************************/

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    break;    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    /*************************FIREBASE SETUP***********************************/

FoodItem food;
public FoodItem findItem(String fid)
{
    FoodItem foodItem=null;
    ArrayList<FlowModel> cats=db.categories;
    for(int i=0;i<cats.size();i++)
    {
        ArrayList<SubCat> subs=cats.get(i).subcats;
        for(int j=0;j<subs.size();j++)
        {

            ArrayList<FoodItem> items=subs.get(j).fooditems;
            for(int k=0;k<items.size();k++)
            {

                Log.d("Looking I "+i+" J "+j+" K "+k,items.get(k).itemName);
                if(items.get(k).fid.toLowerCase().equals(fid.toLowerCase()))
                {
                    foodItem=items.get(k);
                    Log.d("FOUND", items.get(k).toString());
                    return foodItem;
                }
            }

        }



    }



    return foodItem;
}

}
