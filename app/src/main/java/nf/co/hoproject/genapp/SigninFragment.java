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
public class SigninFragment extends Fragment {

    private View view;
Context ctx;

@Bind(R.id.uname)
    EditText uname;



    @Bind(R.id.pass)
    EditText pass;



    @Bind(R.id.signin)
    View signin;

    Gson js;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);


        ButterKnife.bind(this, view);
        ctx = inflater.getContext();

        pd = new ProgressDialog(ctx);
        AndroidNetworking.initialize(ctx);

        js = new Gson();
        FileOperations fop = new FileOperations();
        String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

        File us = new File(folder + "/user.jnp");
        if (us.exists()) {
            String cfg = fop.read(folder + "/user.jnp");
            try{
                Log.d("cfg", cfg);
                String ccfg=cfg.replace("nn","").replace("}n","}");
                Log.d("cfg", ccfg);

                JSONObject jsonObject = new JSONObject(ccfg);
                uname.setText(jsonObject.getString("name").replace(" ",""));
                pass.setText(jsonObject.getString("password"));
        }catch (Exception e)
            {
                e.printStackTrace();
            }
    }




        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=getResources().getString(R.string.server).toString()+"/login_user.php?email="+getD(uname)+"&password="+getD(pass);


                pd.setMessage("Signing in...");
                pd.show();

                AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        FileOperations fop=new FileOperations();



                        utl.log(response);

                        if(!response.toLowerCase().contains("invalid")) {
                            js = new Gson();
                            String jstr = response;

                            Intent i = new Intent(ctx, ListActivity.class);
                           // i.putExtra("jstr", jstr);
                            i.putExtra("jstr", response);

                            utl.log(jstr);
                            String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                            File us = new File(folder + "/user.jnp");
                            if (us.exists())
                                us.delete();
                           // fop.write(folder + "/user.jnp", jstr);
                            fop.write(folder + "/user.jnp", response);


                            startActivity(i);
                            getActivity().finish();
                            ;
                        }
                        else {

                            utl.toast(ctx,"Invalid Creditials");
                        }

                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.toast(ctx, "Check your Internet Connection");
                    }
                });


            }
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
