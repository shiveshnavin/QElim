package nf.co.hoproject.genapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nf.co.hoproject.genapp.DataBase.FoodItem;
import nf.co.hoproject.genapp.R;

/**
 * Created by shivesh on 15/6/16.
 */

public class EnrtyAdapterGeneric extends BaseAdapter {


    Context context;
    ArrayList<FoodItem> food;


    public EnrtyAdapterGeneric(Context context, ArrayList<FoodItem> food) {
        this.context = context;
        this.food = food;
    }


    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return food.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder= null;

        if (convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.genric_entry,null);


            viewHolder.image= (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title= (TextView)convertView.findViewById(R.id.title);
            viewHolder.time= (TextView)convertView.findViewById(R.id.time);
            viewHolder.dist= (TextView)convertView.findViewById(R.id.dist);
            viewHolder.likes= (TextView)convertView.findViewById(R.id.likes);
            viewHolder.ratings= (RatingBar)convertView.findViewById(R.id.ratings);

            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
            viewHolder.title.setTypeface(font);

/*
            viewHolder.title.setTextColor(Color.BLACK);
            viewHolder.dist.setTextColor(Color.BLACK);
            viewHolder.likes.setTextColor(Color.BLACK);
*/

            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();

        }


        FoodItem food = (FoodItem)getItem(position);

        //viewHolder.image.setImageResource(food.image);
        //Picasso.with(context).load(food.images.get(0)).into(viewHolder.image);
       // Picasso.with(context).load(R.drawable.def).into(viewHolder.image);

        Log.d("Picasso Load Start", food.icon);
        if((food.icon.length()>10))
        Picasso.with(context).load(food.icon).placeholder(R.drawable.placeholder_food_item_2).into(viewHolder.image);

        viewHolder.title.setText(food.itemName);

        viewHolder.time.setText(food.est+" Min");
        viewHolder.dist.setText("Chef "+food.chef);
        viewHolder.likes.setText(food.price);
        viewHolder.ratings.setRating((food.rating).floatValue());

        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                (viewHolder.likes).setText(Integer.parseInt((viewHolder.likes).getText().toString())+1);
            }
        });



        return convertView;
    }

    private class ViewHolder{

        ImageView image;
        TextView title;
        TextView time;
        TextView dist;
        TextView likes;
        RatingBar ratings;


    }

}
