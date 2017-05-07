package nf.co.hoproject.genapp.inner.ui.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.Book;
import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.DataBase.FireConf;
import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.Order;
import nf.co.hoproject.genapp.DataBase.Request;
import nf.co.hoproject.genapp.ExecuterU;
import nf.co.hoproject.genapp.GenericUser;
import nf.co.hoproject.genapp.MainActivity2;
import nf.co.hoproject.genapp.MyWallet;
import nf.co.hoproject.genapp.R;
import nf.co.hoproject.genapp.User;
import nf.co.hoproject.genapp.adapters.MyOrderAdapter;
import nf.co.hoproject.genapp.inner.ui.base.BaseActivity;
import nf.co.hoproject.genapp.inner.util.LogUtil;
import nf.co.hoproject.genapp.test.WriteFirebase;
import nf.co.hoproject.genapp.utl;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class Orders extends BaseActivity {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    //GridView gridView;

    ListView gridView;
    GenericUser guser;
    ExecuterU ex;
  //  @Bind(R.id.username)
    TextView username;

   // @Bind(R.id.usernameBack)
    View usernameBack;
    ProgressDialog pd;

    @Bind(R.id.flip)
    ImageView flip;


    String[] gridViewString = {
            "Hot & Popular", "Festives", "Main Dishes",
            "Dessert", "Drinks", "Chapaties",
            "ToungueBuster","Condiments"
    } ;

    User user;

    int[] gridViewImageId = {
            R.drawable.hot,
            R.drawable.fest,
            R.drawable.main,
            R.drawable.dess,
            R.drawable.drin,
            R.drawable.roti,
            R.drawable.chill,
            R.drawable.cond
    };
     String jstr;
    Bitmap bmp;;
    private boolean twoPaneMode;
    Context ctx;
    Activity act;
    String  folder;
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


        setContentView(R.layout.activity_orders);

        ctx=this;
        act=this;

        setupToolbar();
        try {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            try {

                View head=navigationView.getHeaderView(0);
                username=(TextView)head.findViewById(R.id.username);
                usernameBack= head.findViewById(R.id.usernameBack);
                Log.d("deb1x",""+username+" "+usernameBack);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("deb1x",""+username+" "+usernameBack);
            ButterKnife.bind(this);

        } catch (Exception e) {
             flip=(ImageView)findViewById(R.id.flip);
            e.printStackTrace();
        }
        ctx=this;
        AndroidNetworking.initialize(ctx);
        folder= Environment.getExternalStorageDirectory().getPath().toString()+"/varange";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        pd=new ProgressDialog(ctx);
        pd.setMessage("Syncronysing");
        pd.show();
        flip();

        usernameBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(ctx, WriteFirebase.class));
                return false;
            }
        });

        jstr =getIntent().getStringExtra("jstr");
        Log.d("RECIEVED JSTR BY HOME"," "+jstr);
        if(jstr!=null){
            try{

                guser=new GenericUser(jstr);

                Log.d("GUSER",guser.name);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            user=new User();
            JSONObject joob;

            try{

                joob=new JSONObject(jstr);

                user.imageurl=joob.getString("image");
                user.name=joob.getString("name");

                username.setText(user.name);
                user.uid=joob.getString("uid");
                user.social=joob.getString("social");

                Log.d("imurl", user.imageurl);


    JSONObject jj = new JSONObject(user.imageurl);
    user.imageurl = jj.getString("link");
    user.imageurl = user.imageurl.replace("sz=50", "sz=200");
    Log.d("imurl link", user.imageurl);






            }catch (Exception e)
            {
                e.printStackTrace();
            }



           // https://www.googleapis.com/plus/v1/people/101415094392885832530?fields=image&key=%20AIzaSyD5B8SImEMBeymImNxJu5sAhu-dHugrhyE
            if(user.imageurl!="null")
                AndroidNetworking.get(user.imageurl)
                        .setTag("imageRequestTag")
                        .setPriority(Priority.MEDIUM)
                        .setBitmapMaxHeight(250)
                        .setBitmapMaxWidth(250)
                        .setBitmapConfig(Bitmap.Config.ARGB_8888)
                        .build()
                        .getAsBitmap(new BitmapRequestListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                // do anything with bitmap

                                bmp=bitmap;
//                                bmp=utl.getFbPP(user.facebookID);
                                Drawable d=new BitmapDrawable(getResources(),bmp);
                                usernameBack.setBackground(d);
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });



        }
        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST", "TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }


        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(ctx,Book.class);
                in.putExtra("venue","Euphoria LIVE");

                in.putExtra("vid", "1");

                in.putExtra("guser", jstr);



          //      startActivity(in);

            }
        });
/*
        usernameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.toast(ctx, "Beta:Manage Account");
                File g=new File(folder+"/user.jnp");
                g.delete();
                startActivity(new Intent(ctx, Splash.class));
                finish();
            }
        });*/

        gridView = (ListView) findViewById(R.id.gridview);


        //setUpGrid();
        listenFirebase();

           }

    public void setUpGrid(  ArrayList<Request> ordersList )
    {


        MyOrderAdapter adapterViewAndroid = new MyOrderAdapter(Orders.this, ordersList);

        Log.d("DATA",""+ ordersList.size());
        gridView.setAdapter(adapterViewAndroid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Log.d("Clicked", "Cleickd " + (i)+" len "+gridViewString.length+" arr "+db.categories.size());
                    Intent iz=(new Intent(ctx, MainActivity2.class));
                    iz.putExtra("category", gridViewString[i]);
                    iz.putExtra("icon",gridViewImageId[0]);
                    iz.putExtra("guser",jstr);
                    iz.putExtra("venue","Euphoria LIVE");

                    iz.putExtra("cid",db.categories.get(i).CID);

                    iz.putExtra("guser",jstr);


                    //  if(gridViewString[i].toLowerCase().contains("events"))
                    startActivity(iz);
                    //else
                    //utl.toast(ctx,"Talash jari hai...");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
       // gridView.removeViewAt(db.categories.size());
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {/*
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            utl.log("BACK PRESSED");


            final AlertDialog.Builder
                    alertDialogBuilder = new AlertDialog.Builder
                    (ctx);
            alertDialogBuilder.setMessage("Exit ?");
            alertDialogBuilder.setPositiveButton("NO", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface
                                                    dialog, int id) {

                            dialog.dismiss();;
                          //  ListActivity.this.moveTaskToBack(true);
                        }
                    });
            alertDialogBuilder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Orders.this.finish();
                }
            });


            AlertDialog alertDialog
                    = alertDialogBuilder.create();


            alertDialog.show();

            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }

    private void setupDetailFragment() {

    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        //ArticleListFragment fragmentById = (ArticleListFragment) getFragmentManager().findFragmentById(R.id.article_list);
       // fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
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
                openDrawer();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler  h;
    Runnable r;
    int dr=0;


    Integer [] strip={R.drawable.lp1
            ,R.drawable.lp2
            ,R.drawable.lp3
            ,R.drawable.lp4
            ,R.drawable.lp5};


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


        h.postDelayed(r, 100);


    }



    /***********************FIREBASE SETUP*************************************/

    Firebase fire;
    ListFlowModel db;
    public void listenFirebase()
    {

        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database");


        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(pd!=null)
                if(pd.isShowing())
                {
                    pd.dismiss();
                }
                int i = 0;

/************************************************************/

try {
    db = snapshot.getValue(ListFlowModel.class);
    Log.d("CAT 0", db.categories.get(0).categoryName);
    ArrayList<FlowModel> f=db.categories;
    int size=f.size();
    gridViewString=new String[size];;
    Log.d("FS",""+size);

    for(int m=0;m<size;m++)
    {
        try {
           gridViewString[m] = f.get(m).categoryName;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    Firebase or=new Firebase(Constants.fire(ctx)+"/orders");
    or.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            ArrayList<Request> ordersList  = new ArrayList<Request>() ;
            GenericUser user=utl.readData(ctx);

            for(DataSnapshot ds:dataSnapshot.getChildren()){
                Request req=ds.getValue(Request.class);


                utl.log(""+ ds.toString());
                if( ds.child("email").toString().contains(user.email))
                ordersList.add(ds.getValue(Request.class));
                else{
                    utl.log("NOTFOUND : "+user.email+"\n"+ ds.child("email"));

                }

            }
            setUpGrid(ordersList );



        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });
}catch (Exception e)
{
    e.printStackTrace();
}
/************************************************************/
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Log.d("RES",postSnapshot.toString());
              break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    /*************************FIREBASE SETUP***********************************/

    @Override
    protected void onPause()
    {
        if(h!=null)
        {
            try{
            h.removeCallbacks(r);}catch (Exception e)
            {
                e.printStackTrace();;
            }
        }
        super.onPause();
    }
    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.mymenu;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


}
