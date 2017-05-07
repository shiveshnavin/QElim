package nf.co.hoproject.genapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.MainActivity2;
import nf.co.hoproject.genapp.R;
import nf.co.hoproject.genapp.utl;

/**
 * Created by shivesh on 29/6/16.
 */ public class MyListAdapter extends ArrayAdapter<FlowModel> {
    private final Context context;;
    public ArrayList<FlowModel> categMain;

    public MyListAdapter(Context context, ArrayList<FlowModel> model) {
        super(context, -1, model);
        this.context = context;
        this.categMain = model;
        mContext=context;


        Log.d("DATA LIST ",""+categMain.size());


    }

    Context mContext;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.row_ent_ltd, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            TextView desc = (TextView) gridViewAndroid.findViewById(R.id.descG);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);

        desc.setVisibility(View.GONE);
            //imageViewAndroid
        // .setImageResource(gridViewImageId[i]);

            Log.d("DATA "+position,""+categMain.get(position).categoryName);
            try{
                textViewAndroid.setText(categMain.get(position).categoryName);
               // desc.setText(categMain.get(position).desc);
                if(categMain.get(position).iconLink.length()>10)
                Picasso.with(mContext).load(categMain.get(position).iconLink).into(imageViewAndroid);
                else {
                    //imageViewAndroid.setImageResource(R.drawable.lamp);
                    Picasso.with(mContext).load(R.drawable.milk).into(imageViewAndroid);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            try{

             View vz=gridViewAndroid.findViewById(R.id.android_custom_gridview_layout);
                vz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent iz=(new Intent(context, MainActivity2.class));
                        iz.putExtra("category",categMain.get(position).categoryName);
                        iz.putExtra("icon",categMain.get(position).iconLink);
                        iz.putExtra("guser", utl.readData(context).toString());
                        iz.putExtra("venue","Euphoria LIVE");

                        iz.putExtra("cid",categMain.get(position).CID);



                        //  if(gridViewString[i].toLowerCase().contains("events"))
                        context.startActivity(iz);
                    }
                });

                if(!categMain.get(position).color.toLowerCase().contains(("default")))
                vz.setBackgroundColor(Color.parseColor(categMain.get(position).color));
            }catch (Exception e)
            {
                e.printStackTrace();
            }


//            View v=gridViewAndroid.findViewById(R.id.android_custom_gridview_layout);




        // YoYo.with(Techniques.RotateIn).duration(900).playOn(gridViewAndroid);

        return gridViewAndroid;



    }
}

