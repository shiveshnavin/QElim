package nf.co.hoproject.genapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import nf.co.hoproject.genapp.DataBase.ListFlowModel;


/**
 * Created by shivesh on 8/4/16.
 */
public class utl {


    public static void e(String t,String tt)
    {
        Log.e(""+t,""+tt);

    }



    public static void e(String t )
    {
        Log.e("TAG",""+t);

    }





    public static ListFlowModel db;
    public static void logout(Activity th)
    {
        FacebookSdk.sdkInitialize(th);
        LoginManager.getInstance().logOut();
        File g=new File(folder+"/user.jnp");
        g.delete();
        th.startActivity(new Intent(th, Splash.class));
        th.finish();
    }

    public void um()
    {

        Context ctx=null;




        String msg="MSG";
        ExecuterU ex=new ExecuterU(ctx,msg)
        {
            @Override
        public void doIt()
            {




            }
            @Override
            public void doNe()
            {



            }

        };


    }
   public static Bitmap convertBitmap(String path)   {
                Bitmap bitmap=null;
                BitmapFactory.Options bfOptions=new BitmapFactory.Options();
                bfOptions.inDither=false;                     //Disable Dithering mode
                bfOptions.inPurgeable=true;                   //Tell to gc that whether itneeds free memory, the Bitmap can be cleared
                bfOptions.inInputShareable=true;              //Which kind of reference will be                used to recover the Bitmap data after being clear, when it will be used in the future
                bfOptions.inTempStorage=new byte[32 * 1024];
                File file=new File(path);
                FileInputStream fs=null;
                try {
                    fs = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if(fs!=null)
                    {
                        bitmap=BitmapFactory.decodeFileDescriptor(fs.
                                getFD(), null, bfOptions);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if(fs!=null) {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bitmap;
            }




    public static Bitmap convertBitmap(Context ctx,  String  path,int q)   {


        Log.d("path",path);

        Bitmap bitmap=null;
        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                     //Disable Dithering mode
        bfOptions.inPurgeable=true;                   //Tell to gc that whether itneeds free memory, the Bitmap can be cleared
        bfOptions.inInputShareable=true;              //Which kind of reference will be                used to recover the Bitmap data after being clear, when it will be used in the future
        bfOptions.inTempStorage=new byte[32 * 1024];
        File file=new File(path);
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null)
            {
                bitmap=BitmapFactory.decodeFileDescriptor(fs.
                        getFD(), null, bfOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Bitmap bitmap1=Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*(q/100)),
                (int)(bitmap.getHeight()*(q/100)),true);

        try{

            bitmap.recycle();;
            bitmap=null;

        }catch (Exception e)
        {
            Log.d("Recycle fail","Never Ming at convertBitmap()")
;            e.printStackTrace();
        }
        return bitmap1;
    }






    public static  void saveScaledBitmap(Bitmap bmp,int h,int w,String dest){

        Bitmap bmm=getResizedBitmap(bmp,h,w);


        File de=new File(dest);

       /*     try {  if(!de.exists())
                de.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        FileOutputStream out= null;
        try {
            out = new FileOutputStream(de);
        bmp.compress(Bitmap.CompressFormat.PNG,1,out);
        out.flush();;
        out.close();;
            Log.d("SAVED",dest);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Bitmap overlay(Bitmap b1,Bitmap b2)
    {

        b2=utl.getResizedBitmap(b2,b1.getHeight(),b1.getWidth());
        Bitmap ov=Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), b1.getConfig());
        Canvas cv=new Canvas(ov);
        cv.drawBitmap(b1,new Matrix(),null);
        cv.drawBitmap(b2,new Matrix(),null);
        return ov;

    }


    public static void bitmapToFile(Bitmap bmp,String dest)
    {

        File f=new File(dest);
        try {
            FileOutputStream os;

            os=new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
            os.close();
            bmp.recycle();


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public static String resizePng(Context ctx,String src,String dest, int quality)
    {

        try {
            Bitmap bmp = convertBitmap(ctx,src,quality);
            if (bmp.getWidth()<512) {

                File de=new File(dest);
                FileOutputStream out=new FileOutputStream(de);
                bmp.compress(Bitmap.CompressFormat.PNG,90,out);
                out.flush();;
                out.close();;

                return dest;
            }


            File de=new File(dest);
            FileOutputStream out=new FileOutputStream(de);
            bmp.compress(Bitmap.CompressFormat.PNG,quality,out);
            out.flush();;
            out.close();;







        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return dest;
    }





    public static void toast(Context c,String t)
    {
        Toast.makeText(c, t, Toast.LENGTH_LONG).show();

    }




    public static void log(String t)
    {

        Log.d("TAG", t);
    }


    public static void copyFile(File src,File dst)
    {
        try{

        InputStream in=new FileInputStream(src);
        OutputStream os=new FileOutputStream(dst);

            byte []buf=new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                os.write(buf,0,len);
            }
            in.close();
            os.close();




        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }




    public static  String folder=Environment.getExternalStorageDirectory().getPath();;
    public  utl(){

        folder= Environment.getExternalStorageDirectory().getPath();


    }
    public static void diag(Context c,String title,String desc)
    {
        final AlertDialog.Builder
                alertDialogBuilder = new AlertDialog.Builder
                (c);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(desc);
        alertDialogBuilder.setNeutralButton("Close", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface
                                                dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog
                = alertDialogBuilder.create();


        alertDialog.show();
    }
    public static void diagExit(final Activity c,Context ctx,String title,String desc)
    {
        final AlertDialog.Builder
                alertDialogBuilder = new AlertDialog.Builder
                (ctx);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(desc);
        alertDialogBuilder.setNeutralButton("Exit", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface
                                                dialog, int id) {
                       c.finish();
                    }
                });


        AlertDialog alertDialog
                = alertDialogBuilder.create();


        alertDialog.show();
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap
                (bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth,int i) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) ;
        float scaleHeight = ((float) newHeight) ;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap
                (bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
public Bitmap circle(Bitmap bmp)
{
    Bitmap out=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas cv=new Canvas(out);
    int color= Color.RED;
    Paint paint=new Paint();
    Rect rect=new Rect(0,0,bmp.getWidth(),bmp.getHeight());
    RectF rectF=new RectF(rect);
    paint.setAntiAlias(true);
    cv.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    cv.drawOval(rectF, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    cv.drawBitmap(bmp,rect,rect,paint);


    return out;




}




    //Generic
   static String ret;
    public static String showChangeLangDialog(Activity ctx,String title,String message,String hint) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = ctx.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.generic_promt_inp, null);
        dialogBuilder.setView(dialogView);

         final EditText edt = (EditText) dialogView.findViewById(R.id.input);

        edt.setHint(hint);

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ret=edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               ret =null;
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        return  ret;
    }

    public static String refineString(String red,String rep)
    {
        red = red.replaceAll("[^a-zA-Z0-9]", rep);
        return  red;
    }


    public static  boolean writeData(GenericUser guser,Context ctx)
    {
        String data= Constants.dataFile(ctx);
        FileOperations fop=new FileOperations();
        Gson g=new Gson();
        fop.write(data,g.toJson(guser));
        Log.d("DATA WROTE","");
        //Log.d("DATA WROTE",""+fop.read(data));
        return  true;
    }


    @Override
    public String toString()
    {
        return (new Gson()).toJson(this);
    }

    public static GenericUser readData( Context ctx)
    {
        String data= Constants.dataFile(ctx);
        if(!new File(data).exists())
            return null;
        FileOperations fop=new FileOperations();
        Gson g=new Gson();
        try {
            Log.d("DATA READ","");
            //Log.d("DATA READ",""+fop.read(data));
            GenericUser guser=g.fromJson(fop.read(data),GenericUser.class);
            return  guser;


        } catch (JsonSyntaxException e) {

            e.printStackTrace();

            return  null;
        }
    }



/*

    public static User getCuruserGeneric()
    {
        User user=new User();


        String jstr=new FileOperations().read(folder + "/user.jnp");
        jstr=jstr.replace("nn{","").replace("}n","}");
        try{


            JSONObject j=new JSONObject(jstr);
            user.name=j.getString("name");
            user.phone=j.getString("phone");
            user.email=j.getString("email");



        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return user;
    }






    public static User getCuruser()
    {
        User user=new User();


       String jstr=new FileOperations().read(folder+"/user.jnp");
        jstr=jstr.replace("nn{","").replace("}n","}");
        try{


            JSONObject j=new JSONObject(jstr);




        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return user;
    }
*/

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }




    public static GenericUser getGUserFromJstr(String response)
    {


        GenericUser guser=new GenericUser();
        try {
            Gson j = new Gson();
            response = response.replace("[", "\"[");
            response = response.replace("]", "]\"");
            response = response.replace("{", "\"{");
            response = response.replace("}", "}\"");
            Log.d("GUSER Q RESPONSE", response);
            guser = j.fromJson(response, GenericUser.class);

            Log.d("GUSER NAME", guser.name);

        }catch (Exception e)
        {

            Log.d("ERR in gu ","");
            e.printStackTrace();
        }



return guser;
    }



/*
    public static String getRegisterUrlGeneric(User user,Context ctx)
    {
        final String url=ctx.getResources().getString(R.string.server).toString() +"/newuser.php?name="+utl.getD(user.name)+"&email" +
                "="+utl.getD(user.email)+"&" +
                "username="+ user.name.replace(" ", "")+"&" +
                "phone="+utl.getD(user.phone)+"&" +
                "password="+utl.getD(user.password)+
                "&facebookId="+user.facebookID+
                "&googleuserId="+user.googleID+
                "&social="+user.social
                ;
        return url;
    }


*/




    public static Bitmap getFbPP(String uid) {
        try {
            URL imageUrl = new URL("https://graph.facebook.com/" + uid + "/picture?type=large");

            Bitmap bmp=BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            return bmp;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String getD(EditText ed){

        String ret;
        ret=ed.getText().toString();
        ret= URLEncoder.encode(ret);


        return  ret;


    }



    public static String getD(String ed){

        String ret;
       ret=ed;
        ret= URLEncoder.encode(ret);


        return  ret;


    }




}
