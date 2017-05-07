package nf.co.hoproject.genapp.DataBase;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shivesh on 25/6/16.
 */  public class Item
{

    public String fid ="ACH1";
    public String name ="Paneer Tikka";
    public String itemName ="Paneer Tikka";
    public String desc="Paneer Tikka";
    public String img="http://placehold.it/200x200";
    public String mrp="59";
    public String price="59";
    public String chef="59";
    public String est="59";
    public Double priceInt=5.9;
    @Nullable
    public String poster="";
    public String icon="";
    public Double discount=5.0;
    public String stock ="IN STOCK";
    public String brand="Paneer Tikka";
    public Double rating=4.5;
    public int quan=1;


    public ArrayList<Review> reviews=null;

    public Item(String n, String d, String ch, String id, String pr, String es, String du, String ic, Double rat)
    {
        this.fid =id;
        name =n;
      //  place=ch;
        desc=d;
      //  dur=du;
        price=pr;
        rating=rat;
        img=ic;
        reviews=new ArrayList<>();
        try{

            priceInt=Double.parseDouble(pr);

        }catch (Exception e)
        {
            priceInt=100.0;
            e.printStackTrace();
        }


    }
    public Item build()
    {
         Double mr=Double.parseDouble(mrp);
         priceInt=mr-(discount/100)*mr;
         price=""+priceInt;

        return  this;
    }
    public Item()
    {

    }


    public static Double getPrice(Item cat)
    {
        Double mrp=Double.parseDouble(cat.mrp);

         Double prc=mrp-mrp*(cat.discount/100);
        return prc;
    }

}
