package nf.co.hoproject.genapp.DataBase;

import com.google.gson.Gson;

/**
 * Created by shivesh on 2/4/17.
 */

public class Request {

    public String  name="",more="",country="",curr="";
    public String slot1="",slot2="",slot3="",slot;
    public String catId,levelId;
    public String catName="",levelName="";
    public String priceInr,priceUsd ;
    public String user_id,username;

     public String orderStatus =" Delivered";
    public String oid ="33";
     public String paymentStatus="Paid" ;



    public String token;

    public String itemname="Eporia Live";
    public String outlet="Hard Rock Cafe";
    public String user="varrange";
    public String uid="1";
    public String quan="2";
    public String email="dummymain12@dumdum.com";
    public String fid="1";
    public String phone="S Navin";
    public String price="50";
    public String datetime="March 2017 ";
    public String est="DumDum";


    public  FoodItem item;



    @Override
    public  String toString()
    {
        Gson js=new Gson();
        return js.toJson(this);
    }



}
