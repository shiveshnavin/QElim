package nf.co.hoproject.genapp.DataBase;

import android.util.Log;

/**
 * Created by shivesh on 25/6/16.
 */
public class SearchDB {



    static  public Review createReview(String us,String id,String emai,String pi,String revie,Double  rat)

    {
        Review r=new Review(us,id,emai,pi,revie,rat);
        return r;

    }


    static   public SubCat findSubCatById(FlowModel lf,String scid)
    {

        SubCat food=null;

        if(lf.subcats!=null)
        {
            for(int i=0;i<lf.subcats.size();i++)
            {
                if(lf.subcats.get(i).SCID.toLowerCase().equals(scid.toLowerCase()))
                {
                    return lf.subcats.get(i);
                }
            }

            Log.d("FindFoodById", "No Category Match Found");
        }
        else {

            Log.d("FindFoodById","No Category Items");

        }
        return food;
    }


    static   public SubCat findSubCatByName(FlowModel lf,String scid,boolean matchWhole)
    {
        // FlowModel f=null;
        //  ArrayList<SubCat> lf.subcats=f.lf.subcats;

        SubCat food=null;

        if(lf.subcats!=null)
        {
            for(int i=0;i<lf.subcats.size();i++)
            {
                if(lf.subcats.get(i).subCatName.toLowerCase().contains(scid.toLowerCase())&&!matchWhole)
                {
                    return lf.subcats.get(i);
                }
                else if(lf.subcats.get(i).subCatName.toLowerCase().equals(scid.toLowerCase())&&matchWhole)
                {
                    return lf.subcats.get(i);
                }
            }

            if(food==null)
            Log.d("FindFoodById","No Category Match Found");
        }
        else {

            Log.d("FindFoodById", "No Category Items");

        }
        return food;
    }












    static public FlowModel findCatById(ListFlowModel lf,String cid)
    {
        FlowModel food=null;

        if(lf.categories!=null)
        {
            for(int i=0;i<lf.categories.size();i++)
            {
                Log.d("Searching ",cid+" in "+lf.categories.get(i).CID);
                if(lf.categories.get(i).CID.toLowerCase().equals(cid.toLowerCase()))
                {

                    food= lf.categories.get(i);
                }
            }

            if(food==null)
            Log.d("FindFoodById", "No Category Match Found");
        }
        else {

            Log.d("FindFoodById","No Category Items");

        }
        return food;
    }

    ////////////////////////////////***************************************/////////////////////////////////
    static public FlowModel findCatByName(ListFlowModel lf,String cid,boolean matchWhole)
    {
        FlowModel food=null;

        if(lf.categories!=null)
        {
            for(int i=0;i<lf.categories.size();i++)
            {
                if(lf.categories.get(i).categoryName.toLowerCase().contains(cid.toLowerCase())&&!matchWhole)
                {
                    return lf.categories.get(i);
                }
                else if(lf.categories.get(i).categoryName.toLowerCase().equals(cid.toLowerCase())&&matchWhole)
                {
                    return lf.categories.get(i);
                }
            }

            Log.d("FindFoodById","No Category Match Found");
        }
        else {

            Log.d("FindFoodById","No Category Items");

        }
        return food;
    }



    static  public FoodItem findFoodById(SubCat lf,String FID)
    {

        FoodItem food=null;

        if(lf.fooditems!=null)
        {
            for(int i=0;i<lf.fooditems.size();i++)
            {
                if(lf.fooditems.get(i).fid.toLowerCase().equals(FID.toLowerCase()))
                {
                    return lf.fooditems.get(i);
                }
            }

            Log.d("FindFoodById", "No Food Items Match Found");
        }
        else {

            Log.d("FindFoodById","No Food Items");

        }
        return food;
    }


    static  public FoodItem findFoodByName(SubCat lf,String FID,boolean matchWhole)
    {
        FoodItem food=null;

        if(lf.fooditems!=null)
        {
            for(int i=0;i<lf.fooditems.size();i++)
            {
                if(lf.fooditems.get(i).itemName.toLowerCase().contains(FID.toLowerCase())&&!matchWhole)
                {
                    return lf.fooditems.get(i);
                }
                else if(lf.fooditems.get(i).itemName.toLowerCase().equals(FID.toLowerCase())&&matchWhole)
                {
                    return lf.fooditems.get(i);
                }
            }

            Log.d("FindFoodById","No Food Items Match Found");
        }
        else {

            Log.d("FindFoodById","No Food Items");

        }
        return food;
    }



}
