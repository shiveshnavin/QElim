package nf.co.hoproject.genapp.DataBase;

import java.util.ArrayList;

/**
 * Created by shivesh on 25/6/16.
 */
public class WFoodItem {





    public static FoodItem lf;
    public WFoodItem(FoodItem l)
    {
        lf=l;
    }




    static  public Review createReview(String us,String id,String emai,String pi,String revie,Double  rat)

    {
        Review r=new Review(us,id,emai,pi,revie,rat);
        return r;

    }



}
