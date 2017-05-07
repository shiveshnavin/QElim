package nf.co.hoproject.genapp.inner.ui.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nf.co.hoproject.genapp.BookingFmt;
import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.ExecuterU;
import nf.co.hoproject.genapp.GenericUser;
import nf.co.hoproject.genapp.adapters.MyListAdapter;
import nf.co.hoproject.genapp.MyWallet;
import nf.co.hoproject.genapp.DataBase.OrderFormat;
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
public class Booking extends BaseActivity {
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
   // @Bind(R.id.flip)
    ImageView flip;






    @Bind(R.id.name)
    EditText name;

    ProgressDialog pd;

    @Bind(R.id.nop)
    EditText nop;

    @Bind(R.id.mobile)
    EditText phone;

    @Bind(R.id.date)
            TextView date;

    @Bind(R.id.time)
    TextView time;
    @OnClick(R.id.time)
    public void time()
    {

        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        String timee = ""+hourOfDay+":"+minute;
                        time.setText(timee);
                        bookingFmt.time=timee;
                    }
                },
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );
        dpd.show(getFragmentManager(), "Timepickerdialog");

    }

    @OnClick(R.id.book)
    public void book()
    {



        String  exp=getIntent().getStringExtra("exp");

        Log.d("EXPORT IMPORt",""+exp);
        if(exp!=null)
        {
            Gson g=new Gson();
            OrderFormat order=g.fromJson(exp,OrderFormat.class);

            bookingFmt.item=order.itemname;
            bookingFmt.itemID=order.fid;
            bookingFmt.quan=order.quan;





        }
        bookingFmt.name=name.getText().toString();
        bookingFmt.nop=nop.getText().toString();
        bookingFmt.phone=phone.getText().toString();
        Log.d("TAG",""+bookingFmt.toString());
        if(bookingFmt.date!=null&&bookingFmt.name!=null&&bookingFmt.nop!=null&&bookingFmt.time!=null&&bookingFmt.phone!=null)
        {
            if(bookingFmt.date.length()>1&&bookingFmt.name.length()>1
                    &&bookingFmt.nop.length()>0&&bookingFmt.time.length()>1&&bookingFmt.phone.length()>1)
            {

                if((bookingFmt.phone.length())>13||(bookingFmt.phone.length())<5)
                {
                    phone.setError("Invalid No");
                    phone.requestFocus();
                }
                else {
                    flagB = true;

                    fire.push().setValue(bookingFmt);


                    utl.toast(ctx, "Requesting Reservation...");

                }




            }else {
                utl.toast(ctx,"Please Re-Check all details Once.");
            }
        }
        else {
            utl.toast(ctx,"Fill in all details first.");
        }
    }


    @OnClick(R.id.date)
    public void date()
    {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        String datee = " "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        bookingFmt.date=datee;
                        date.setText(datee);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }



    BookingFmt bookingFmt;/*
    @OnClick(R.id.name)
            public void name()
    {

    }


    @OnClick(R.id.nop)
    public void nop()
    {

    }

    @OnClick(R.id.mobile)
    public void mobile()
    {

    }

*/




    Gson js;




    String[] gridViewStringZ = {
            "Awadhi", "Bihari", "Punjabi",
            "Kashmiri", "Bengali", "Chinese",
            "Gujarati","South"

    } ;
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
bookingFmt =new BookingFmt();
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_booking);

        ctx=this;
        act=this;
        ButterKnife.bind(this);
        listenFirebase();
        setupToolbar();
        setTitle("");
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

       // flip();

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





























           }
boolean flagB=false;
    public void setUpGrid()
    {
        MyListAdapter adapterViewAndroid = new MyListAdapter(Booking.this, db.categories);

        Log.d("DATA",""+db.categories.size());
        gridView.setAdapter(adapterViewAndroid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Log.d("Clicked", "Cleickd " + (i)+" len "+gridViewString.length+" arr "+db.categories.size());
                    Intent iz=(new Intent(ctx, Booking.class));
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





    public void notifyN()
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager  manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
;
        Intent intent = new Intent("nf.co.hoproject.genapp.androidtemplate.ui.quote.Landing");

        PendingIntent pendingIntent = PendingIntent.getActivity(Booking.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(Booking.this);

        builder.setAutoCancel(false);
        builder.setTicker("Success !");
        builder.setContentTitle("Your Booking is recorded !");
        builder.setContentText("Hi, Your booking "+bookingFmt.item+" is recorded ." +
                " Stay put our staff will reach out to you via contact you provided !".replace("null",""));
        builder.setSmallIcon(R.drawable.lampsm);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
       // builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

       Notification myNotication = builder.getNotification();
        manager.notify(11, myNotication);
    }


  public  int NOTIFICATION_ID=1212;
    public void notifyN2()
    {


        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        mBuilder.setSmallIcon(R.drawable.lampsm);
        mBuilder.setContentTitle("Your Booking is recorded !");
        mBuilder.setContentText("Hi, Your booking "+bookingFmt.item+" is recorded ." +
                " Stay put our staff will reach out to you via contact you provided !".replace("null",""));
        Intent resultIntent = new Intent(ctx, Landing.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Landing.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

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


        h.postDelayed(r, 100);


    }



    /***********************FIREBASE SETUP*************************************/

    Firebase fire;
    ListFlowModel db;
    public void listenFirebase()
    {

//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database"+"/orders");


        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(pd!=null)
                if(pd.isShowing())
                {
                    pd.dismiss();
                }
                int i = 0;
                if(flagB)
                {
                    utl.diag(ctx,"Booking Successful","Your booking "
                            +bookingFmt.item+" request is registered and will be entertained soon".replace("null",""));
                    try {
                        notifyN();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flagB=false;
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {





/************************************************************/
/*
try {
    db = postSnapshot.getValue(ListFlowModel.class);
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
    setUpGrid();
}catch (Exception e)
{
    e.printStackTrace();
}*/
    Log.d("RES",postSnapshot.toString());
/************************************************************/

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
        return R.id.book;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
