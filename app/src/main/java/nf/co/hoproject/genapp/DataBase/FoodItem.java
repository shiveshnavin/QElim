package nf.co.hoproject.genapp.DataBase;

import java.util.ArrayList;

/**
 * Created by shivesh on 25/6/16.
 */  public class FoodItem
{

    public String fid="ACH1";

    public String itemName="Paneer Tikka";
    public String desc="Paneer Tikka";

    public String icon="http://placehold.it/200x200";

    public String price="59";
    public Integer priceInt=59;
    public String est="39 Min";

    public String chef="Manav Ahar";


    public Double rating=4.5;

    public ArrayList<Review> reviews=null;

    public FoodItem(String n,String d,String ch,String id,String pr,String es,String ic,Double rat)
    {
        fid=id;
        itemName=n;
        chef=ch;
        desc=d;
        est=es;
        price=pr;
        rating=rat;
        icon=ic;
        reviews=new ArrayList<>();
        try{

            priceInt=Integer.parseInt(pr);

        }catch (Exception e)
        {
            priceInt=100;
            e.printStackTrace();
        }


    }
    public FoodItem()
    {

    }

}
