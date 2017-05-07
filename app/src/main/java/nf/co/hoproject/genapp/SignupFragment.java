package nf.co.hoproject.genapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.inner.ui.pages.ListActivity;

/**
 * Created by wolfsoft on 10/11/2015.
 */
public class SignupFragment extends Fragment {

    private View view;


    private TextView book;

    Context ctx;
    @Bind(R.id.name)
    EditText name;

    ProgressDialog pd;

    @Bind(R.id.email)
    EditText email;




    @Bind(R.id.mobile)
    EditText phone;


    Gson js;
    User user;


    @Bind(R.id.pass)
    EditText pass;


    @Bind(R.id.create)
    View create;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_signup, container, false);


        ButterKnife.bind(this,view);
        ctx=view.getContext();
//        client= new AsyncHttpClient();
        pd=new ProgressDialog(ctx);

        AndroidNetworking.initialize(ctx);





        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                user=new User();



                try {

                    JSONObject jzo=new JSONObject();
                    jzo.put("link",user.imageurl);
                    user.imageurl=jzo.toString();


                }catch (Exception e)
                {e.printStackTrace();}




                final String url=getResources().getString(R.string.server).toString() +"/newuser.php?" +
                        "name="+name.getText().toString()+
                        "&email="+getD(email)+"&" +
                        "username="+ name.getText().toString().replace(" ", "")+"&" +
                        "phone="+getD(phone)+"&" +
                        "password="+getD(pass)+
                        "&facebookId="+user.facebookID+
                        "&googleuserId="+user.googleID+
                        "&gender="+user.gender+
                        "&social="+user.social+
                        "&image="+ URLEncoder.encode(user.imageurl);
                ;

                /*
                final String url=ctx.getResources().getString(R.string.server).toString() +"/newuser.php?" +
                        "name="+getD(name)+
                        "&email" + "="+getD(email)+"&" +
                        "username="+name.getText().toString().replace(" ", "") + "&" +
                        "phone="+getD(phone)+"&" +
                        "password=" +getD(pass)+
                        "&gender="+user.gender+
                        "&facebookId="+user.facebookID+
                        "&googleuserId="+user.googleID+
                        "&social="+user.social+
                        "&image="+ URLEncoder.encode(user.imageurl);
                        ;*/



                Log.d("SIGNUOP URL", url);


                pd.setMessage("Registering...");

                pd.show();
                AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        Log.d("SIGNUOP RESPONSE", response);

                        if (response.contains("{")) {

                            user = new User(getD(name), getD(email), getD(pass), getD(phone));
                            js = new Gson();
                            String jstr = response;

                            Intent i = new Intent(ctx, ListActivity.class);
                            i.putExtra("jstr", jstr);
                            utl.log(jstr);
                            FileOperations fop = new FileOperations();
                            String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                            File us = new File(folder + "/user.jnp");
                            if (us.exists())
                                us.delete();
                            fop.write(folder + "/user.jnp", jstr);


                            startActivity(i);
                            getActivity().finish();
                            ;

                        } else {
                            utl.toast(ctx, "SighUp failed . EMail already registered ");
                        }


                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.log(ANError.toString());
                    }
                });


/*

                client.get(url,  new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        pd.setMessage("Registering...");
                        pd.show();;
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"


                        pd.setMessage("Registered...");
                        pd.dismiss();;
                        String res=response.toString();
                        utl.log(res);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
        */    }
        });





        return view;

    }

    public String getD(EditText ed){

        String ret;
        ret=ed.getText().toString();
        ret= URLEncoder.encode(ret);


        return  ret;


    }




}
