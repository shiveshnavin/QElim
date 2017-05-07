package nf.co.hoproject.genapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.multidex.MultiDex;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.security.MessageDigest;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.inner.ui.pages.Landing;
import nf.co.hoproject.genapp.inner.ui.pages.ListActivity;

public class Splash extends AppCompatActivity {
    private TextView food;
    private ViewPager pager;
    GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;

    Context ctx;
    @Bind(R.id.signinup)
    LinearLayout signinup;
    Handler h;
    @Bind(R.id.main)
    LinearLayout ll;

    Runnable r;
    @Bind(R.id.fb)
    LinearLayout fb;


    @Bind(R.id.splash)
    View splash;;

    @Bind(R.id.go)
    LinearLayout go;
    GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;

    @Bind(R.id.social)
    LinearLayout social;

    LoginButton loginButton;

    String folder;
    FileOperations fop;

    Activity act;
    ProgressDialog progressDialog;
    private CallbackManager callbackManager;





    public void pd()
    {}




    public ProgressDialog pd;
    public   void pd(boolean show)
    {
        utl.log("PD : "+show);
        if(pd==null) {

            pd = new ProgressDialog(ctx,R.style.MyTheme);
            pd.setCancelable(Constants.isPdCancelable);
            //  pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            pd.setIndeterminate(true);
            Drawable dd=getResources().getDrawable(R.drawable.pdb);
            pd.setIndeterminateDrawable(dd);


            pd.setCancelable(false);
        }
        //pd.setMessage("Loading..");
        if(show) {
            if (!pd.isShowing())
                pd.show();
        }else
            pd.dismiss();
    }


    public   void pd(boolean show,Context c)
    {
        utl.log("PD : "+show);
        if(pd==null) {

            pd = new ProgressDialog(ctx,R.style.MyTheme);
            pd.setCancelable(Constants.isPdCancelable);
            //  pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            pd.setIndeterminate(true);
            Drawable dd=getResources().getDrawable(R.drawable.pdb);
            pd.setIndeterminateDrawable(dd);


            pd.setCancelable(false);
        }
        //pd.setMessage("Loading..");
        if(show) {
            if (!pd.isShowing())
                pd.show();
        }else
            pd.dismiss();
    }
    public   void pd(boolean show,String msg)
    {
        utl.log("PD : "+show);
        if(pd==null) {
            pd = new ProgressDialog(ctx);
            // pd.setCancelable(Constants.isPdCancelable);
        }pd.setMessage(""+msg);
        if(show)
            pd.show();
        else
            pd.dismiss();
    }







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
        FacebookSdk.sdkInitialize(getApplicationContext());
        MultiDex.install(this);
        setContentView(R.layout.activity_splash);

       fop=new FileOperations();
        folder= Environment.getExternalStorageDirectory().getPath().toString()+"/varange";

        if(!(new File(folder)).exists())
            (new File(folder)).mkdirs();

        ctx=this;
        act=this;
        ButterKnife.bind(this);

        h=new Handler();

       final Intent i = new Intent(ctx, ListActivity.class);

        FileOperations fop = new FileOperations();
         folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";
if(!new File(folder).exists())
    new File(folder).mkdirs();

        File us = new File(folder + "/user.jnp");
        if (us.exists()) {

            splash.setBackgroundResource(R.drawable.lgn);
            String jstr= fop.read(folder + "/user.jnp");
            i.putExtra("jstr", jstr);

            utl.toast(ctx,"Welcome Back");

            startActivity(i);
            finish();

            r=new Runnable() {
                @Override
                public void run() {

                    utl.toast(ctx,"Welcome Back");

                    startActivity(i);
                    finish();

                    // startActivity(new Intent(Splash.this, Landing.class));
                }
            };

          //  h.postDelayed(r,1);
        }



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        utl.toast(ctx,"Failed..");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        callbackManager=CallbackManager.Factory.create();
        AndroidNetworking.initialize(ctx);

        callbackManager = CallbackManager.Factory.create();
        loginButton= (LoginButton)findViewById(R.id.login_button);

        //loginButton = (LoginButton) view.findViewById(R.id.usersettings_fragment_login_button);
        loginButton.registerCallback(callbackManager,mCallBack);

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        showSignup();





        r=new Runnable() {
            @Override
            public void run() {

                utl.toast(ctx,"Welcome Back");
                Intent i = new Intent(ctx, Landing.class);

                FileOperations fop = new FileOperations();
                String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr= fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);
                }

                startActivity(i);
                finish();

                // startActivity(new Intent(Splash.this, Landing.class));
            }
        };


        // h.postDelayed(r,2000);




        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File us = new File(folder + "/user.jnp");

                if (!us.exists() || us.exists()) {
                    progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("Loading...");
                    //progressDialog.show();

                    loginButton.performClick();


                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
                } else
                    h.postDelayed(r, 1);
            }
        });






        getHash();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // utl.toast(ctx,"Sign In using Google");


                signIn();


            }
        });

        go.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                utl.toast(ctx,"Signed Out");
                signOut();
                return  true;
            }
        });


            pager=(ViewPager)

            findViewById(R.id.pager);


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

            tabLayout.addTab(tabLayout.newTab().

            setText("SIGN IN")

            );
            tabLayout.addTab(tabLayout.newTab().

            setText("SIGN UP")

            );


            MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            pager.setAdapter(myPagerAdapter);
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

                                               {
                                                   @Override
                                                   public void onTabSelected(TabLayout.Tab tab) {
                                                       pager.setCurrentItem(tab.getPosition());
                                                   }

                                                   @Override
                                                   public void onTabUnselected(TabLayout.Tab tab) {

                                                   }

                                                   @Override
                                                   public void onTabReselected(TabLayout.Tab tab) {

                                                   }
                                               }

            );


        }


    public void getHash()
    {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {

            e.printStackTrace();


        }
        //- See more at: http://www.theappguruz.com/blog/android-facebook-integration-tutorial#sthash.BD94NXCy.dpuf
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //tv_username.setText("");
                    }
                });
    }

/////////////////////////////////////////?GOGOOOOOOOOOOOOOOOOOOOOOOOOGLEEEEEEEEEEEE///////////////////////////////
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


          //  Log.d("acct",acct.toString());

            try {
                js=new Gson();

                user = new GenericUser();
                user.googleID = acct.getId();
                user.email = acct.getEmail();
                user.name = acct.getDisplayName();
                user.social="google";
                try {

                   /// user.imageurl = acct.getPhotoUrl().toString();
                }catch (Exception e)
                {

                   e.printStackTrace();
                }
                user.password="google";
                user.phone="null";

                progressDialog=new ProgressDialog(ctx);
                progressDialog.setMessage("A min...Gathering your profile");
                progressDialog.show();

                String imur="https://www.googleapis.com/plus/v1/people/"+user.googleID+"?fields=image&key=%20AIzaSyD5B8SImEMBeymImNxJu5sAhu-dHugrhyE";

                Log.d("Imgur",""+imur);


                AndroidNetworking.get(imur).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Log.d("Image API"," "+response);


                        try{
                            JSONObject k=new JSONObject(response);
                            JSONObject i=k.getJSONObject("image");
                            user.imageurl=i.getString("url");//.replace("sz=50","sz=200");


                            Log.d("USre GOOGLE", js.toJson(user).toString());
                            sighup();


                        }catch (Exception e)
                        { progressDialog.dismiss();

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError ANError) {

                        Log.d("Imgur",""+ANError.getErrorDetail());
                        user.imageurl="https://cdn4.iconfinder.com/data/icons/48-bubbles/48/30.User-128.png";//.replace("sz=50","sz=200");


                        Log.d("USre GOOGLE", js.toJson(user).toString());
                        sighup();

                    }
                });
               // registerViaSo(false);;


            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(ctx,"welcome "+user.name, Toast.LENGTH_LONG).show();



        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    Gson js;



    /**********************************ONACT rESULT************************************/




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    GenericUser user;
////////////////////////////////////////////////////////FACEBOOOOOOOKKKKKK//////////////////////////////////////////////
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

           // progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", "RES "+response + "");
                            try {
                               js=new Gson();

                                user = new GenericUser();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();

                                user.imageurl= ("https://graph.facebook.com/" + object.getString("id").toString() + "/picture?type=large");

                                user.password="facebook";
                                user.social="facebook";
                                user.phone = "null";


                                Log.d("USre FACEBOk",js.toJson(user).toString());

                                sighup();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(ctx,"welcome "+user.name, Toast.LENGTH_LONG).show();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {



            Log.d("CANCLE", "CANCLE");

            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            utl.log("" + "EXCEPRI");
            progressDialog.dismiss();
            e.printStackTrace();
        }
    };






void showSignup()
{

    signinup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (social.getVisibility() == View.VISIBLE) {
                                            ll.setVisibility(View.VISIBLE);
                                            social.setVisibility(View.GONE);

                                        } else {
                                            ll.setVisibility(View.INVISIBLE);
                                            social.setVisibility(View.VISIBLE);
                                        }


                                    }
                                }

    );

}



/*
    public void registerViaSo(boolean gotit)
    {

      //  String jstr=js.toJson(user,User.class);
      //  Log.d("FROM USEr TO GSON", jstr);
        // fop.write(folder+"/user.jnp",jstr);


        if(!gotit)
        showChangeLangDialog("Enter Your Phone"
                , "Please enter your mobile no to continue "

                , "Mobile No.");



        if (gotit) {


          final  String check=getResources().getString(R.string.server).toString() +"/check_user.php?email="+utl.getD(user.email);


            String imageurl=user.imageurl;
            JSONObject jo=new JSONObject();
            try {
                jo.put("link",imageurl);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            try {

                JSONObject jzo=new JSONObject();
                jzo.put("link",user.imageurl);
                user.imageurl=jzo.toString();


            }catch (Exception e)
            {e.printStackTrace();}




            final String url=getResources().getString(R.string.server).toString() +"/newuser.php?name="+utl.getD(user.name)+"&email" +
                    "="+utl.getD(user.email)+"&" +
                    "username="+ user.name.replace(" ", "")+"&" +
                    "phone="+utl.getD(user.phone)+"&" +
                    "password="+utl.getD(user.password)+
                   "&facebookId="+user.facebookID+
                   "&googleuserId="+user.googleID+
                   "&gender="+user.gender+
                    "&social="+user.social+
                   "&image="+ URLEncoder.encode(user.imageurl);
                   ;

            utl.log(url);
            progressDialog=new ProgressDialog(ctx);
            progressDialog.setMessage("Checking...");

            progressDialog.show();


            AndroidNetworking.get(check).build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {

                    utl.log(response);


                    if(!response.toLowerCase().contains("not found")) {
                        js = new Gson();
                        String jstr = js.toJson(user, User.class);

                        Intent i = new Intent(ctx, ListActivity.class);
                        i.putExtra("jstr", jstr);
                        utl.log(jstr);
                        FileOperations fop = new FileOperations();
                        String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                        File us = new File(folder + "/user.jnp");
                        if (us.exists())
                            us.delete();
                        fop.write(folder + "/user.jnp", jstr);

                        progressDialog.dismiss();

                        startActivity(i);
                        finish();
                    }
                    else {

                        progressDialog.setMessage("Registering...");

                        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {

                                utl.log(response);


                                Intent i = new Intent(ctx, ListActivity.class);
                                i.putExtra("jstr", response);
                                FileOperations fop = new FileOperations();
                                String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                                File us = new File(folder + "/user.jnp");
                                if (us.exists())
                                    us.delete();
                                fop.write(folder + "/user.jnp", response);

                                progressDialog.dismiss();

                                startActivity(i);
                                finish();
                                ;


                            }

                            @Override
                            public void onError(ANError ANError) {

                                progressDialog.dismiss();
                                utl.log(ANError.toString());
                            }
                        });
                    }


                    ;


                }

                @Override
                public void onError(ANError ANError) {

                    progressDialog.dismiss();
                    utl.log(ANError.toString());
                }
            });

    }else {
           // utl.toast(ctx, "Try Again");
        }

    }

    String ret=null;
    public  String showChangeLangDialog(String title,String message,String hint) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.generic_promt_inp, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.input);


        edt.setHint(hint);

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ret=edt.getText().toString();


                user.phone=ret;
                Log.d("phone",user.phone);


                registerViaSo(true);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ret = null;
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        return  ret;
    }


public void checkIfAlready()
{

    final  String check=getResources().getString(R.string.server).toString() +"/check_user.php?email="+utl.getD(user.email);

    if(progressDialog!=null)
        progressDialog.dismiss();;
    progressDialog=new ProgressDialog(ctx);
    progressDialog.setMessage("Checking...");
    progressDialog.show();

    AndroidNetworking.get(check).build().getAsString(new StringRequestListener() {
        @Override
        public void onResponse(String response) {

            progressDialog.dismiss();

            if (response.toLowerCase().contains("not found")) {
                registerViaSo(false);
                ;

            }
            else
            {

                String jstr=response;
                Intent i = new Intent(ctx, Landing.class);
                i.putExtra("jstr", jstr);
                utl.log(jstr);
                FileOperations fop = new FileOperations();
                String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                File us = new File(folder + "/user.jnp");
                if (us.exists())
                    us.delete();
                fop.write(folder + "/user.jnp", jstr);

                progressDialog.dismiss();

                startActivity(i);
                finish();
            }
        }

        @Override
        public void onError(ANError ANError) {

            progressDialog.dismiss();
        }
    });

}*/

    public void sendSms(String st)
    {

    }



    public void getPhone()
    {

        newUser();
    }

    Firebase fireUsers;Firebase  fireUser;
    public void newUser()
    {


        if(user!=null)
        {

            final Firebase sms=new Firebase(Constants.fire(ctx)+"/sms");
            sms.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        String ms=dataSnapshot.getValue(String.class);
                        ms=ms.replace("<name>",user.name);
                        utl.log("SENDING SMS TO "+ms+"\n"+user.name);
                        sendSms(ms);
                        sms.removeEventListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    sms.removeEventListener(this);
                }
            });



        }



        fireUser  =new Firebase(Constants.fire(this)+"/userdb");
        fireUser.child(utl.refineString(user.email,"")).setValue(user);
        fireUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                utl.log("SUCESS IN CREATING NEW USER");
                fireUsers.child(utl.refineString(user.email,"")).setValue(user.password);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    public void sighup()
    {
        fireUsers =new Firebase(Constants.fire(this)+"/userlist");


        pd(true);

        fireUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                boolean notfound=true;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    try {
                        if(postSnapshot.getKey().equalsIgnoreCase(utl.refineString(user.email,"")))
                        {
                            notfound=false;


                            pd(false);

                            utl.log("ALREADY PRESENT "+postSnapshot.getValue());
                            Intent iz=new Intent(Splash.this, ListActivity.class);
                            iz.putExtra("email",postSnapshot.getKey());
                            if(
                                    utl.writeData(user,ctx)){
                                if(getIntent().getBooleanExtra("comeback",false)==false)
                                    startActivity(iz);
                                finish();}
                            break;

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("RES",postSnapshot.toString());



                }
                if(notfound)
                {
                    getPhone();;
                    // fireUser.child(utl.refineString(user.email,"")).setValue(user);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



    }






}
