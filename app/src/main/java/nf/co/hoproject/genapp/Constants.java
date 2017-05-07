package nf.co.hoproject.genapp;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.UUID;


/**
 * Created by shivesh on 24/6/16.
 */
public class Constants {


    public static boolean isPdCancelable=true;
  //  pd.setCancelable(Constants.isPdCancelable);


    public static String uid(int l)
    {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("uuid Full= " + uuid);
        String ret= uuid.substring(0, Math.min(uuid.length(), l));;
        System.out.println("uuid "+l+" = " + ret);
        return ret;
    }

    public static String HOST="http://thehoproject.co.nf";


    public static String APP_DEVELOPER="Developer : Shivesh Navin : http://thehoproject.co.nf/";



    public static String FCM_AUTH="AAAA1Z5F4YU:APA91bE3R4OIQfdkzJzYhctvfUr9gYVSodLm8OzK1tugKYzkNVI6fmsSw8CcI23nTAbSaIdS4yvQfbZp09mbSsJ5k18WiMGw734m-R6Bm6iQEmEwAi3yuP52pvkXqQ7smx8bUsVTgwkI";
    public static Conf conf;
    public static final String TAG_EMAIL = "email";
    public static final String v = "v1";
    public static final String TAG_LOGIN = "login";
    public static  String folder = "login";
    public static  String datafile = "login";
    private static String FIRE_BASE="https://genricapp.firebaseio.com/";

    public static String RESTRAUNT="";
    public static String fire(Context ctx)
    {
        return Constants.FIRE_BASE+ utl.refineString(ctx.getResources().getString(R.string.app_name),"")+RESTRAUNT;
    }

    public static String getconf(Context ctx,String file)
    {

        FileOperations fo=new FileOperations();
        return fo.read(getFolder(ctx)+"/"+file);

    }


    public static String setconf(Context ctx,String file,String data)
    {

        FileOperations fo=new FileOperations();
        return fo.read(getFolder(ctx)+"/"+file);

    }



    public static String getFolder(Context ctx)
    {
         folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");
        return folder;
    }

    public static String dataFile(Context ctx)
    {
        folder = getFolder(ctx);

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/conf.json";
        return datafile;
    }

    public static String getApp(Context ctx)
    {
        return utl.refineString(ctx.getResources().getString(R.string.app_name),"");
    }
    public static String localData(Context ctx)
    {
        folder =getFolder(ctx);

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/data.json";
        return datafile;
    }
    public static String locFile(Context ctx)
    {
        folder = getFolder(ctx);

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/loc.json";
        return datafile;
    }



    public static String messFile(Context ctx)
    {
        folder = getFolder(ctx);

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/mess.json";
        return datafile;
    }




    public static class Conf{

        public boolean isProductDigital=false;
        public boolean requireAddress=true;

        public String appname;


    }





}
