package nf.co.hoproject.genapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.inner.ui.base.BaseActivity;

public class MyWallet extends BaseActivity {


    @Bind(R.id.trans)
    LinearLayout trans;
    Context ctx;
    ProgressDialog pd;

    @Bind(R.id.redeem)
    TextView redeem;

    @Bind(R.id.lights)
    TextView lights;



    @Bind(R.id.pp)
    ImageView pp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        ctx=this;
        setupToolbar();
        ButterKnife.bind(this);
        AndroidNetworking.initialize(this);

        setTitle("");

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.toast(ctx,"Not Available in Pilot Run");

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        pd=new ProgressDialog(this);
        final  LayoutInflater lf=getLayoutInflater();

        FileOperations op=new FileOperations();
        String jstr=op.read(Environment.getExternalStorageDirectory().getPath().toString()+
        "/varange/user.jnp");
        String  email;
        try{

            JSONObject jo=new JSONObject(jstr.replace("nn","").replace("}n","}"));
            email=jo.getString("email");
            pd.setMessage("Working...");
            pd.show();;
            AndroidNetworking.get(getResources().getString(R.string.server)+"/check_user.php?email="+
                    utl.getD(email)).build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {

                    Log.d("get user response", response);
                    pd.dismiss();
                    try {
                        JSONObject joo = new JSONObject(response);
                        JSONObject urlj = joo.getJSONObject("image");
                        String url = urlj.getString("link").replace("sz=50", "sz=250");
                        Log.d("Image", url);


                        String light = joo.getString("lights");

                        lights.setText(light + " Coins");

                        Log.d("SetUsePicUrl",url);
                        AndroidNetworking.get(url)
                                .setTag("imageRequestTag")
                                .setPriority(Priority.MEDIUM)
                                .setBitmapMaxHeight(250)
                                .setBitmapMaxWidth(250)
                                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                                .build()
                                .getAsBitmap(new BitmapRequestListener() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        // do anything with bitmap

                                        pp.setImageBitmap(bitmap);
                                        Log.d("SetUsePicUrl", "Complete");

                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                    }
                                });


                        JSONArray jarr = joo.optJSONArray("previous_purchases");
                        for (int i = 0; i < jarr.length(); i++) {
                            try {

                                JSONObject j = new JSONObject();
                                j = jarr.getJSONObject(i);

                                LinearLayout ll = (LinearLayout) lf.inflate(R.layout.list_item_article, trans, false);
                                TextView tit = (TextView) ll.findViewById(R.id.article_title);
                                TextView sub = (TextView) ll.findViewById(R.id.article_subtitle);

                                try {
                                    tit.setText("Event : " + j.getString("ename") + "\nBooking on : " + j.getString("date"));
                                } catch (Exception e) {
                                    tit.setText("Event : " + j.getString("ename") + "\nBooking on : NA");
                                    e.printStackTrace();
                                }
                                try {
                                    sub.setText("EVENT DATE:" + j.getString("edate")+ "\nCost : " + j.getString("cost"));
                                } catch (Exception e) {
                                    sub.setText("EVENT DATE:" + "JUN 19 , 7:30 PM" + "\nCost : Rs. 699/Person");

                                    e.printStackTrace();
                                }

                                trans.addView(ll);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError ANError) {

                    pd.dismiss();
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }



        LinearLayout ll=(LinearLayout)lf.inflate(R.layout.list_item_article,trans,false);
        ( (TextView) ll.findViewById(R.id.article_title)).setText(" ");
        ( (TextView) ll.findViewById(R.id.article_subtitle)).setText(" ");




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




            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
