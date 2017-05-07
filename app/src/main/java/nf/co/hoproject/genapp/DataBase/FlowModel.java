package nf.co.hoproject.genapp.DataBase;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shivesh on 25/6/16.
 */
public class FlowModel {


    public String categoryName="Dessert";
    public String CID="A";
    public String iconLink="http://placehold.it/200x200";
    public String desc="Description";
    public String color="#ffab40";
    public ArrayList<Flips> flips;
    public ArrayList<SubCat> subcats;





    public FlowModel(String cat,String ci,String ic,ArrayList<SubCat> subcats)
    {
        categoryName=cat;
        CID=ci;
        iconLink=ic;
        flips=new ArrayList<>();
        this.subcats=subcats;


    }

    public FlowModel()
    {


    }



}
