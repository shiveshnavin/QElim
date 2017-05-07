package nf.co.hoproject.genapp;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.Profile;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.DataBase.PayUResp;
import nf.co.hoproject.genapp.DataBase.Request;
import nf.co.hoproject.genapp.inner.ui.pages.ListActivity;
import nf.co.hoproject.genapp.inner.ui.pages.Orders;
import nf.co.hoproject.genapp.test.NotificationExtras;


public class Pay extends AppCompatActivity {
    Request req;
    Gson js;
    public static String TAG="STEP 4 PAY";
    public static Context ctx;
    public static Activity act;




    Firebase ref;

    @Bind(R.id.webview)
    AdvancedWebView webView;




    Firebase debug,instance;
    public long SESSION_ID;
    public void initReport()
    {

        Firebase.setAndroidContext(ctx);
        GenericUser user= utl.readData(ctx);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String time=sdf.format(Calendar.getInstance().getTime());
        SESSION_ID= SystemClock.currentThreadTimeMillis();
        debug=new Firebase(Constants.fire(ctx)+"/debug");
      //  instance=debug.child("v"+utl.getApkVerison(ctx)).child(""+utl.refineString(user.email,"_")+"_"+user.uid).child(""+time);

    }
    public void report(String value)
    {

        //long time= SystemClock.currentThreadTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String time=sdf.format(Calendar.getInstance().getTime());
        instance.child(""+time).setValue(value);

    }




    GenericUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
/*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        */

        ctx=this;
        act=this;
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        ref=new Firebase(Constants.fire(ctx)+"/orders");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                utl.log("FB connected");
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        initReport();

        js=new Gson();
        String json=getIntent().getStringExtra("cat");
        req =js.fromJson(json,Request.class);

        try {
//            FirebaseApp.initializeApp(ctx, FirebaseOptions.fromResource(ctx));
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i("TOKEN", "FCM Registration Token: " + token);

            if (token != null) {
                req.token=token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //step3Date(1,false, req);
        utl.e(TAG,json);
 //http://feelinglone.com/pay/payu_pay.php?
// name=xyz
// &email=xyz%40domain.com
// &phone=9000000001
// &info=TEST
// &slot1=23+March+2017+-+08%3A25+am
// &slot2=02+March+2017+-+03%3A10+am
// &slot3=10+March+2017+-+09%3A30+am
// &price=1000
// &more=TEST
// &catid=5
// &levelid=3
// &user_id=5902
// &firstname=xyz

        String url;
        url=Constants.HOST+"/pay/payu_pay.php?";
     /*   if(req.more.toLowerCase().contains("usd")||!req.curr.toLowerCase().equals("inr"))
        {
            url =Constants.HOST+"/pay/payp_pay.php?";
        }

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
*/

        user=utl.readData(ctx);

        utl.log("USER IS : "+(new Gson()).toJson(utl.readData(ctx)));


        req.oid=""+SystemClock.currentThreadTimeMillis();
        req.email=user.email;
        req.name=user.name;


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        req.datetime=formattedDate;


        url+="email="+ URLEncoder.encode(""+user.email);
        url+="&name="+URLEncoder.encode(""+user.name);
        url+="&phone="+URLEncoder.encode("");
        url+="&slot="+URLEncoder.encode(""+req.slot1);
        url+="&first_name="+URLEncoder.encode(""+user.name);



        url+="&food_name="+URLEncoder.encode(""+req.item.itemName);;
        url+="&fid="+URLEncoder.encode(""+req.item.fid);;
        url+="&food_price="+URLEncoder.encode(""+req.item.price);;
        url+="&food_price_int="+URLEncoder.encode(""+req.item.priceInt);;




        utl.e(TAG,"Pay url : "+url);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webView.loadUrl(url);

        //report("Start Pay : "+req.price+" "+req.curr);





    }




    public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the ctx */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
           // toast=toast.replace("_11_","\"");
       //     Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
          //  utl.e(TAG,"RESPONSE FROM PAYG : "+toast);

           // finish();
        }


        @JavascriptInterface
        public void success(String toast) {
            toast=toast.replace("_11_","\"");


            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i("TOKEN", "FCM Registration Token: " + token);

            if (token != null) {
               req.token=token;
            }






            req.orderStatus="PENDING";
            req.paymentStatus="Paid ₹ "+req.price;
            ref.push().setValue(req);
;
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    Intent intent=new Intent(ctx, Orders.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    PendingIntent resultIntent = PendingIntent.getActivity( ctx , 0, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    String dd="";

                    boolean isLoggedIn=(utl.readData(ctx)!=null);
                    Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Notification.Builder b = new Notification.Builder(ctx)
                            .setSmallIcon(R.drawable.ic_done)
                            .setContentTitle("Order Placed !")
                            //.setColor(getResources().getColor(R.color.green_400))
                            .setContentText("Your Order Status is : "+req.orderStatus)
                            .setAutoCancel( true )
                            .setSound(notificationSoundURI)
                            .setContentInfo("")
                            .setContentIntent(isLoggedIn?resultIntent: PendingIntent.getActivity( ctx , 0, new Intent(ctx, Orders.class),
                                    PendingIntent.FLAG_ONE_SHOT));


                    Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, getResources().getColor(R.color.green_400));
                    // Notification n = NotificationExtras.buildWithBackgroundResource(this, b, R.drawable.bg_notification);
                    NotificationManagerCompat.from(ctx).notify(1, n);



                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


//            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            showStatus(toast);


        }


        @JavascriptInterface
        public void failure(String toast) {
            toast=toast.replace("_11_","\"");
            utl.e(TAG,"RESPONSE FROM PAYG : "+toast);
            showStatus(toast);

            Intent intent=new Intent(ctx, Orders.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent resultIntent = PendingIntent.getActivity( ctx , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String dd="";

            boolean isLoggedIn=(utl.readData(ctx)!=null);
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder b = new Notification.Builder(ctx)
                    .setSmallIcon(R.drawable.ic_done)
                    .setContentTitle("Order Failed !")
                    //.setColor(getResources().getColor(R.color.green_400))
                    .setContentText("Your Order Status is : "+req.orderStatus)
                    .setAutoCancel( true )
                    .setSound(notificationSoundURI)
                    .setContentInfo("")
                    .setContentIntent(isLoggedIn?resultIntent: PendingIntent.getActivity( ctx , 0, new Intent(ctx, Orders.class),
                            PendingIntent.FLAG_ONE_SHOT));


            Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, getResources().getColor(R.color.green_400));
            // Notification n = NotificationExtras.buildWithBackgroundResource(this, b, R.drawable.bg_notification);
            NotificationManagerCompat.from(ctx).notify(1, n);





        }



    }



    AlertDialog dig;Profile profile;
    public String noOnce=null;
    /*************************EMAIL LOGIN**************************************/
    View v=null;



        public void showStatus(String json)
    {

       final PayUResp payUResp=js.fromJson(json,PayUResp.class);

        final AlertDialog.Builder di;

        v=act.getLayoutInflater().inflate(R.layout.pay_status,null);
        di = new AlertDialog.Builder(ctx);
        di.setView(v);
        dig=di.create();
        dig.show();


        if(v!=null) {
            final TextView txnid = (TextView) v.findViewById(R.id.txnid);
            final TextView amount = (TextView) v.findViewById(R.id.amount);
            final TextView status = (TextView) v.findViewById(R.id.status);
            final ImageView img = (ImageView) v.findViewById(R.id.img);

            final Button login = (Button) v.findViewById(R.id.login);

            txnid.setText("TXN ID : "+payUResp.txnid);
            amount.setText("Amount : "+payUResp.amount);
            status.setText("Status : "+payUResp.status);

            if(payUResp.status.toLowerCase().equals("failure")||payUResp.status.toLowerCase().contains("fail")) {

                img.setImageResource(R.drawable.order_failed);
              //  Picasso.with(ctx).load(R.drawable.order_failed).into(img);
            }  else {


                img.setImageResource(R.drawable.order_placed);

                //Picasso.with(ctx).load(R.drawable.order_placed).into(img);
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dig.dismiss();;

                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finish();
                }
            });


        }


    }


    @Override
    public void onBackPressed()
    {

        String t="Are you sure you want to Cancel ?";

        View rootView = act.getWindow().getDecorView().getRootView();
        //Snackbar snackbar = Snackbar.make(rootView, Html.fromHtml("<font color=\"#fff\">"+t+"</font>" ), Snackbar.LENGTH_LONG);
        Snackbar snackbar = Snackbar.make(rootView, ""+t , Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(act.getResources().getColor(R.color.blue_100));

        snackbar.setAction("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));

// change snackbar text color
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(act.getResources().getColor(R.color.white));


            snackbar.show();


     //   utl.snack(this,"You cant go back at this stage !");




    }


    public void sendFCM(String userToken,Request cm)
    {
        js=new Gson();
        utl.log("Sending FCM to : "+userToken);
        ANRequest.PostRequestBuilder post=new ANRequest.PostRequestBuilder("https://fcm.googleapis.com/fcm/send");
        post.addHeaders("Content-Type","application/json");
        post.addHeaders("Authorization","key="+Constants.FCM_AUTH);
        String body="{ \"data\": {" +
                "\"click_action\": \"ChatActivity\"," +
                "    \"message\": \"STRING_CM\"}," +
                "   \"notification\": {" +
                "    \"title\": \"STRING_TI\"," +
                "    \"text\": \"STRING_TX\"" +
                "  }," +
                "  \"to\" : \"STRING_TO\"" +
                "}}";


        body="{ \"data\": {" +
                "\"click_action\": \"ChatActivity\"," +
                "    \"message\": \"STRING_CM\"," +
                "\"title\": \"STRING_TI\"," +
                "\"text\": \"STRING_TX\"}," +
                "" +
                "  \"to\" : \"STRING_TO\"" +
                "}";





        body= body.replace("STRING_CM",js.toJson(cm).replace("\"","\\\""));
        body= body.replace("STRING_TI","Order Ready For Pickup!");
        body= body.replace("STRING_TX","Your Order \"+cm.itemname+\" is ready for PickUP !");
        body= body.replace("STRING_TO",userToken);
        utl.log("FCM BODY "+body);

        JSONObject jo=new JSONObject();
        try {
            jo=new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.addJSONObjectBody(jo);

        post.build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

                // utl.toast(ctx,"FCM Sent");
                utl.log("respo : "+response);
            }

            @Override
            public void onError(ANError ANError) {
                utl.log("respo : "+ANError.getErrorDetail());
            }
        });
    }





}
