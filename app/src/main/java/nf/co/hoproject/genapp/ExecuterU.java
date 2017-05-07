package nf.co.hoproject.genapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shivesh on 6/6/16.
 */
public class ExecuterU extends AsyncTask<Void,Void,Void> {


    interface callBack{

        void doInBackground(int progress);
        void doPostExecute();

    }

    callBack b;
    int prog;
    ProgressDialog pd;
    Context ctx;
    String message,folder;
    Runnable r;

    public ExecuterU( Context ctx, String message)
    {
        this.ctx=ctx;
        this.r=r;
        this.message=message;
    }

   @Override
   protected void onPreExecute()
   {
       super.onPreExecute();
       folder= Environment.getExternalStorageDirectory().getPath().toString()+"/vidmo";

       pd=new ProgressDialog(ctx);
       pd.setMessage(message);
       pd.show();
   }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        pd.dismiss();
        doNe();

    }

    public void doIt()
    {

    }



    public void doNe()
    {

    }


    @Override
    protected Void doInBackground(Void... voids) {


try {
    doIt();
}catch (Exception e)
{
    Log.d("Exce","While loaovelayingding bitmaps");
    e.printStackTrace();
}
        return null;
    }












    /******************************************/

}
