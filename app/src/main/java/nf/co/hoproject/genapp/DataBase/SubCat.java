package nf.co.hoproject.genapp.DataBase;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shivesh on 25/6/16.
 */ public class SubCat
{
    public String subCatName="Chinese";
    public String SCID="ACH";
    public String iconLink="http://placehold.it/200x200";

    public ArrayList<FoodItem> fooditems;

    public SubCat(String  nam,String  id,String li)
    {
        subCatName=nam
        ;
        SCID=id;
        fooditems=new ArrayList<>();
        iconLink=li;

    }

    public SubCat()
    {

    }

}
