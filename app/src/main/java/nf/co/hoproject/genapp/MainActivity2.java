package nf.co.hoproject.genapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.DataBase.FoodItem;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.SearchDB;
import nf.co.hoproject.genapp.DataBase.SubCat;
import nf.co.hoproject.genapp.adapters.EnrtyAdapterGeneric;

public class MainActivity2 extends AppCompatActivity {


    @Bind(R.id.cat)
    LinearLayout selCat;
    @Bind(R.id.categoryname) TextView category;


    @Bind(R.id.flip)
    ImageView flip;


    ArrayList<String> subcats;

    private TextView title;
    ExpandableHeightListView listview;

    Handler h;
    Runnable r;

    ArrayList<FoodItem> items;
    public String cat="null";

    Context ctx;

    int dr=0;String name,cid,jstr;
    private EnrtyAdapterGeneric listViewBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        cat=getIntent().getStringExtra("category");
        icon=getIntent().getIntExtra("icon", R.drawable.ic_dashboard);
        Log.d("cat", cat);

        ctx=this;
        setupToolbar();;

        ButterKnife.bind(this);

        initListeners();

        items=loadVens(cat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
        durs=subCategories(cat);
       // strip=getCatStrip(cat);


        name=getIntent().getStringExtra("venue");
        if(name!=null)
            setTitle(name);
        cid=getIntent().getStringExtra("cid");
        jstr=getIntent().getStringExtra("guser");

        h=new Handler();

        flip();

        if(!cat.toLowerCase().contains("art"))
        {
          //  utl.toast(ctx,"Talash Jari Hai...");
        }



try {


  /*  TextView title = (TextView) findViewById(R.id.title);
    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf");
    title.setTypeface(font);
    title.setTextColor(Color.WHITE);
*/
    setTitle("");
    ;
    if(!cat.equals("null"))
   ;//   setTitle(cat);



}catch (Exception e) {
    setTitleColor(Color.WHITE);
   //setTitle("Venue");

    e.printStackTrace();

}
        listview = (ExpandableHeightListView)findViewById(R.id.listview);

       // setupList();
        listenFirebase();

    }

public void setupList()
{
    listViewBaseAdapter = new EnrtyAdapterGeneric(ctx, items);
    listview.setAdapter(listViewBaseAdapter);
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent in=new Intent(ctx,Book.class);
            Gson j=new Gson();
            String sss=j.toJson(items.get(position));

            in.putExtra("name",items.get(position).itemName);
            in.putExtra("fid",items.get(position).fid);
            in.putExtra("cat",sss);
            in.putExtra("venue","Euphoria LIVE");
            in.putExtra("guser",jstr);
            startActivity(in);

        }
    });
}

    public void setupList(final ArrayList<FoodItem> items)
    {

        listViewBaseAdapter = new EnrtyAdapterGeneric(ctx, items);
        listview.setAdapter(listViewBaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson j=new Gson();

                String sss=j.toJson(items.get(position));

                Intent in=new Intent(ctx,Book.class);
                in.putExtra("name",items.get(position).itemName);
                in.putExtra("fid",items.get(position).fid);
                in.putExtra("cat",sss);

                in.putExtra("venue","Euphoria LIVE");
                in.putExtra("guser",jstr);
                startActivity(in);

            }
        });
    }


    /***********************FIREBASE SETUP*************************************/

    Firebase fire;
    ListFlowModel db;
    public void listenFirebase()
    {

        Firebase.setAndroidContext(ctx);
       //
        // Firebase.getDefaultConfig().setPersistenceEnabled(true);

        fire= new Firebase(Constants.fire(ctx)+"/database");

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


/************************************************************/

                    try {
                        db = snapshot.getValue(ListFlowModel.class);
                        Log.d("CAT 0", db.categories.get(0).categoryName);
                        ArrayList<FlowModel> f = db.categories;
                        int size = f.size();
                        Log.d("FS", "" + size);


                        FlowModel fm = SearchDB.findCatById(db, cid);

                        subcats = new ArrayList<>();

                        for (int i = 0; i < fm.subcats.size(); i++) {
                            subcats.add(fm.subcats.get(i).subCatName);
                        }

                        durs = new String[subcats.size()];

                        for (int i = 0; i < subcats.size(); i++) {

                            durs[i] = subcats.get(i);

                        }

                       sub = SearchDB.findSubCatByName(fm, subcats.get(sel), false);
                        Log.d("Setup","Setup Cat "+sub.subCatName);
                        setupList(sub.fooditems);

                        for (int m = 0; m < size; m++) {
                            try {


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Log.d("RES", postSnapshot.toString());


/************************************************************/

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    SubCat sub;
    /*************************FIREBASE SETUP***********************************/

    int sel=0;


    public  String[] subCategories(String category)
    {


        category="";
        if(category.contains("Hot"))
        {
            String [] ret = {
                "Awadhi", "Bihari", "Punjabi",
                "Kashmiri", "Bengali", "Chinese",
                "Gujarati","South"

        } ;
            return ret;

        }
        else if(category.contains("Fest"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Main"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Dess"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Drink"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Chap"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Toun"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }
        else if(category.contains("Cond"))
        {
            String [] ret = {
                    "Awadhi", "Bihari", "Punjabi",
                    "Kashmiri", "Bengali", "Chinese",
                    "Gujarati","South"

            } ;
            return ret;

        }

        else {

            String [] ret={"Loading....","Loading....","Loading....","Loading....","Loading....","Loading...."};
            return ret;
        }


    }




    Integer icon;
    String [] durs;//={"Kathhak","Salsa","Western", "Bhangda","Traditional","Garbha"};


    String selectedFrame=null;
    public void dig()
    {

        new AlertDialog.Builder(ctx)
                .setSingleChoiceItems(durs,0,null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int zi) {

                        dialogInterface.dismiss();;
                        sel=((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();



                        Log.d("Selection ", "" + durs[(sel)]);
                        category.setText(durs[sel]);

                        FlowModel fm = SearchDB.findCatById(db, cid);

                        subcats = new ArrayList<>();

                        for (int i = 0; i < fm.subcats.size(); i++) {
                            subcats.add(fm.subcats.get(i).subCatName);
                        }

                        durs = new String[subcats.size()];

                        for (int i = 0; i < subcats.size(); i++) {

                            durs[i] = subcats.get(i);

                        }

                        sub = SearchDB.findSubCatByName(fm, subcats.get(sel), false);
                        Log.d("Setup","Setup Cat "+sub.subCatName);
                        setupList(sub.fooditems);


                    }





                }).show();

    }


public void initListeners()
{
    selCat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            dig();

        }
    });


}

    Integer ii=R.drawable.account;


    Integer [] strip= {R.drawable.place_fd
            ,R.drawable.place_fd };



    public  Integer[] getCatStrip(String category)
    {


        if(category.contains("Hot"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};;
            return ret;

        }


        else if(category.contains("Fest"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }

        else if(category.contains("Main"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }
        else if(category.contains("Dess"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }
        else if(category.contains("Drink"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }
        else if(category.contains("Chap"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }
        else if(category.contains("Toun"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;

        }

        else if(category.contains("Cond"))
        {
            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5
            };
            return ret;

        }

        else {

            Integer [] ret={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};
            return ret;
        }


    }




    public ArrayList<FoodItem> loadVens(String cat)
    {
        ArrayList<FoodItem> ven=new ArrayList<>();



            ven.add(new FoodItem());

       return ven;
    }


    private void setupToolbar() {

        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(icon);
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

                break;

            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void flip()
    {


        h=new Handler();
        r=new Runnable() {
            @Override
            public void run() {

                if(dr==strip.length-1)
                {
                    dr=0;
                }

                Picasso.with(ctx).load(strip[dr++]).into(flip);
                // flip.setImageResource(d);
                YoYo.with(Techniques.SlideInLeft).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                YoYo.with(Techniques.SlideOutRight).withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        h.postDelayed(r, 10);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                }).duration(200).playOn(flip);

                            }
                        },5000);

                        //   h.postDelayed(r,5000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).duration(200).playOn(flip);
            }
        };


        h.postDelayed(r,100);


    }



}
