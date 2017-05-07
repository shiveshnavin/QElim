package nf.co.hoproject.genapp.inner.ui.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.ReviewItem;
import nf.co.hoproject.genapp.ExecuterU;
import nf.co.hoproject.genapp.FileOperations;
import nf.co.hoproject.genapp.GenericUser;
import nf.co.hoproject.genapp.MainActivity2;
import nf.co.hoproject.genapp.adapters.MyListAdapter;
import nf.co.hoproject.genapp.MyWallet;
import nf.co.hoproject.genapp.R;
import nf.co.hoproject.genapp.User;
 import nf.co.hoproject.genapp.inner.ui.base.BaseActivity;
import nf.co.hoproject.genapp.inner.util.LogUtil;
import nf.co.hoproject.genapp.test.WriteFirebase;
import nf.co.hoproject.genapp.utl;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ContactUs extends BaseActivity {
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

    @Bind(R.id.addr)
    TextView addr;

    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.lmnr)
    View mViewr;
    @Bind(R.id.lmn)
    View mView;
    @Bind(R.id.imgm)
    ImageView imgm;
    // @Bind(R.id.flip)
    ImageView flip;

    @OnClick(R.id.book)
    public void book() {

        rate_c();
    }


    String[] gridViewStringZ = {
            "Awadhi", "Bihari", "Punjabi",
            "Kashmiri", "Bengali", "Chinese",
            "Gujarati", "South"

    };
    String[] gridViewString = {
            "Hot & Popular", "Festives", "Main Dishes",
            "Dessert", "Drinks", "Chapaties",
            "ToungueBuster", "Condiments"
    };

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
    Bitmap bmp;
    ;
    private boolean twoPaneMode;
    Context ctx;
    Activity act;
    String folder;

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


        setContentView(R.layout.activity_contactus);

        ctx = this;
        act = this;

        setupToolbar();
        listenFirebase();
        try {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            try {

                View head = navigationView.getHeaderView(0);
                username = (TextView) head.findViewById(R.id.username);
                usernameBack = head.findViewById(R.id.usernameBack);
                Log.d("deb1x", "" + username + " " + usernameBack);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("deb1x", "" + username + " " + usernameBack);
            ButterKnife.bind(this);

        } catch (Exception e) {
            flip = (ImageView) findViewById(R.id.flip);
            e.printStackTrace();
        }
        ctx = this;
        AndroidNetworking.initialize(ctx);
        folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        pd = new ProgressDialog(ctx);
        pd.setMessage("Syncronysing");

        // flip();

        usernameBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(ctx, WriteFirebase.class));
                return false;
            }
        });

        jstr = getIntent().getStringExtra("jstr");
        Log.d("RECIEVED JSTR BY HOME", " " + jstr);
        if (jstr != null) {
            try {

                guser = new GenericUser(jstr);

                Log.d("GUSER", guser.name);
            } catch (Exception e) {
                e.printStackTrace();
            }

            user = new User();
            JSONObject joob;

            try {

                joob = new JSONObject(jstr);

                user.imageurl = joob.getString("image");
                user.name = joob.getString("name");

                username.setText(user.name);
                user.uid = joob.getString("uid");
                user.social = joob.getString("social");

                Log.d("imurl", user.imageurl);


                JSONObject jj = new JSONObject(user.imageurl);
                user.imageurl = jj.getString("link");
                user.imageurl = user.imageurl.replace("sz=50", "sz=200");
                Log.d("imurl link", user.imageurl);


            } catch (Exception e) {
                e.printStackTrace();
            }


            // https://www.googleapis.com/plus/v1/people/101415094392885832530?fields=image&key=%20AIzaSyD5B8SImEMBeymImNxJu5sAhu-dHugrhyE
            if (user.imageurl != "null")
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

                                bmp = bitmap;
//                                bmp=utl.getFbPP(user.facebookID);
                                Drawable d = new BitmapDrawable(getResources(), bmp);
                                usernameBack.setBackground(d);
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });


        }
        setTitle((""));
        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST", "TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }


        addr.setText(Html.fromHtml("<br><p style=\"font-family: times, serif; font-size:14pt; font-style:italic\">" +
                "18 Bakanour Lane\n" +
                "Addresse, LDN" +
                "</p>"));
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlAddress = "http://maps.google.com/maps?q=india " +
                        " &iwloc=A&hl=en";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                startActivity(intent);
            }
        });


        phone.setText(Html.fromHtml("<br><p style=\"font-family: times, serif; font-size:14pt; font-style:italic\">" +
                "<u>01215225445</u>" +
                "</p>"));
        phone.setTextColor(getResources().getColor(R.color.mb_blue));
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0000000000"));

                    if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        utl.toast(ctx,"Please give call permissions to app first");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    utl.toast(ctx,"Please give call permissions to app first");

                }
            }
        });

        Fragment gf = getSupportFragmentManager().findFragmentById(R.id.map);
        if (gf != null)
            try {
              //  mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                try {

                    mMap.getUiSettings().setMapToolbarEnabled(true);

                    try {
                        Calendar n=Calendar.getInstance();
                        fire.child("-KQLF9uOykmuZm9K1bMp").push().setValue("Map On "+n.get(Calendar.HOUR_OF_DAY) );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
               Marker mMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(52.553400, -2.021031))
                        .title("Us")
                        .snippet("Us"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 14));
            }catch (Exception e)
            {
                failsafeMap();
                e.printStackTrace();;
            }
        else{
            failsafeMap();
        }

        if(mMap==null)
        {
            failsafeMap();
        }
/*
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


        //setUpGrid();
       // listenFirebase();

           }



    GoogleMap mMap;
public void failsafeMap()
{

    mView.setVisibility(View.GONE);
    mViewr.setVisibility(View.VISIBLE);
    Picasso.with(ctx).load("http://maps.googleapis.com/maps/api/staticmap?" +
            "center=52.553400,-2.021031&zoom=18&scale=false&size=600x600&" +
            "maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%" +
            "7Ccolor:0xff0000%7Clabel:L%7C52.553400,-2.021031").placeholder(android.R.drawable.ic_dialog_map).into(imgm);


    try {String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        fire.child("-KQLF9uOykmuZm9K1bMp").push().setValue("Image On "+currentDateandTime );
    } catch (Exception e) {
        e.printStackTrace();
    }
    imgm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Double myLatitude = 52.553400;
            Double myLongitude = -2.021031;
            String labelLocation = "The Lamp Res. & Bar";
            String urlAddress = "http://maps.google.com/maps?q=18 Upper High Street " +
                    "Wednesbury, WS10 7HQ &iwloc=A&hl=en";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
            startActivity(intent);
            /*String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 52.553400, -2.021031);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            ctx.startActivity(intent);*/
        }
    });
}




    RatingBar ratingBar;
    AlertDialog alert;
    View promptView;
    ImageView image;
    String curr;
    ReviewItem curReview;
    public void rate_c() {

        curReview=new ReviewItem();
/*
	d=new Dialog(ctx);
	d.setContentView(R.layout.rev_diag);
	//d.setTitle("Review Chef");
	d.show();

*/

        String rev, revir;

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);

        final EditText en = (EditText) promptView.findViewById(R.id.name);
        final EditText er = (EditText) promptView.findViewById(R.id.title);
        final EditText re = (EditText) promptView.findViewById(R.id.review);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
        ;

        alert = alertDialogBuilder.create();

        alert.show();


        image = (ImageView) promptView.findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                (new FileOperations()).write(Environment.getExternalStorageDirectory().getPath().toString() + "/time.flag", "true");
                File ff = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/time.flag");
                curr = Environment.getExternalStorageDirectory().getPath().toString() + "/DCIM/" + ff.lastModified() + ".jpg";
                File f = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/DCIM/" + ff.lastModified() + ".jpg");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));


                startActivityForResult(photoPickerIntent, 120);
            }
        });
        ratingBar = (RatingBar) promptView.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float ratin,
                                        boolean fromUser) {

              /*  rat = String.valueOf(ratin);
                curReview.rating=""+ratin;
                Double d=new Double(ratin);
                curReview.ratingInt = d;
                Log.d("deb16 rat",""+ratin);*/
                curReview.rating=""+ratin;
                curReview.ratingInt = (new Double(ratin).doubleValue());
            }
        });
        //addListenerOnRatingBar();
        //if click on me, then display the current rating value.

        Button rate = (Button) promptView.findViewById(R.id.go);

        rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText myname = (EditText) promptView.findViewById(R.id.nameR);
                EditText myrev = (EditText) promptView.findViewById(R.id.review);
                EditText titl = (EditText) promptView.findViewById(R.id.title);

                String nam = myname.getText().toString();
                String rev = myrev.getText().toString();
                String tit = titl.getText().toString();
                if(nam!=null&&rev!=null&&tit!=null)
                {
                    if(nam.length()>1&&rev.length()>1&&tit.length()>1)
                    {


                        Log.d("deb16",""+nam+" tit "+tit+" quo "+rev);
                        curReview.name=nam;
                        curReview.review=rev;
                        curReview.reviewTitle =tit;
                        // curReview.place=curP;

                        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        curReview.dateTimeOfReview =currentDateandTime;

                        fire.push().setValue(curReview);
                        alert.dismiss();
                        pd=new ProgressDialog(ctx);
                        pd.setMessage("Processing...");
                        pd.show();
                      /*  if(toup!=null)
                            upload(toup);
                        else {
                            boolean ok=false;
                            curReview.uploadedImageURL ="https://placeholdit.imgix.net/~text?txtsize=33&txt=NA&w=150&h=150";
                            if(db!=null)
                            {

                                if(db.reviews!=null)
                                {


                                    db.reviews.add(curReview);
                                    ok=true;
                                }
                            }
                            if(!ok) {
                                db=new ReviewList();
                                db.reviews=new ArrayList<>();
                                db.reviews.add(curReview);
                            }


                        }
*/
                    } else {
                        utl.toast(ctx,"Please Fill NAME/TITLE/REVIEW");
                    }
                }
                else {
                    utl.toast(ctx,"Please Fill NAME/TITLE/REVIEW");
                }
            }


        });


        Button bac = (Button) promptView.findViewById(R.id.ca);

        bac.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alert.dismiss();

            }
        });


    }















    public void setUpGrid()
    {
        MyListAdapter adapterViewAndroid = new MyListAdapter(ContactUs.this, db.categories);

        Log.d("DATA",""+db.categories.size());
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

    Handler h;
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
    public void listenFirebase() {

        Firebase.setAndroidContext(ctx);
        fire = new Firebase(Constants.fire(ctx)+"/database"+"review");


        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int i = 0;
                if(pd!=null)
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                        utl.toast(ctx,"Review Published , Wait for reply");
                    }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

/************************************************************/

                    Log.d("",postSnapshot.toString());

/************************************************************/

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
        return R.id.contactus;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
