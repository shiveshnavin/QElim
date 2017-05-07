package nf.co.hoproject.genapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nf.co.hoproject.genapp.DataBase.Order;
import nf.co.hoproject.genapp.DataBase.Request;
import nf.co.hoproject.genapp.R;

/**
 * Created by shivesh on 29/6/16.
 */ public class MyOrderAdapter extends ArrayAdapter<Request> {
    private final Context context;;
    public ArrayList<Request> requestArrayList;

    public MyOrderAdapter(Context context, ArrayList<Request> model) {
        super(context, -1, model);
        this.context = context;
        requestArrayList = model;
        mContext=context;


        Log.d("DATA LIST ",""+ requestArrayList.size());


    }

    Context mContext;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        MyViewHolder viewHolder = null ;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {

            viewHolder = new MyViewHolder() ;

            convertView = inflater.inflate(R.layout.order_row, null);

            viewHolder.order_status = (TextView) convertView.findViewById(R.id.order_status);
            viewHolder.order_id = (TextView) convertView.findViewById(R.id.order_id);
            viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
            viewHolder.payment = (TextView) convertView.findViewById(R.id.payment);
            viewHolder.payemnt_status = (TextView) convertView.findViewById(R.id.payment_status);
            //  viewHolder.time_status = (TextView) convertView.findViewById(R.id.time_status);
            viewHolder.time = (TextView) convertView.findViewById(R.id.timee);
            convertView.setTag(viewHolder);

        }else {
            //if the convetView has accessed 1st item and now it is not null then
            viewHolder = (MyViewHolder)convertView.getTag();

        }

        viewHolder.order_status.setText(requestArrayList.get(position).orderStatus);

        if(requestArrayList.get(position).orderStatus.equalsIgnoreCase("pending"))
        {
            viewHolder.order_status.setTextColor(context.getResources().getColor(R.color.red_200));
        }
        else
        {
            viewHolder.order_status.setTextColor(context.getResources().getColor(R.color.green_200));

        }
        viewHolder.order_id.setText("OID #"+requestArrayList.get(position).oid);
        viewHolder.order_title.setText(requestArrayList.get(position).itemname);
        viewHolder.payemnt_status.setText(requestArrayList.get(position).paymentStatus);
        viewHolder.payment.setText("");//requestArrayList.get(position).price
        // viewHolder.time_status.setText(requestArrayList.get(position).timeStatus);
        viewHolder.time.setText(requestArrayList.get(position).datetime);
//            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
//            TextView desc = (TextView) gridViewAndroid.findViewById(R.id.descG);
//            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
//
//            //imageViewAndroid.setImageResource(gridViewImageId[i]);
//
//            Log.d("DATA "+position,""+categMain.get(position).categoryName);
//            try{
//                textViewAndroid.setText(categMain.get(position).categoryName);
//                desc.setText(categMain.get(position).desc);
//                if(categMain.get(position).iconLink.length()>10)
//                Picasso.with(mContext).load(categMain.get(position).iconLink).into(imageViewAndroid);
//                else {
//                    //imageViewAndroid.setImageResource(R.drawable.lamp);
//                    Picasso.with(mContext).load(R.drawable.lampsm).into(imageViewAndroid);
//                }
//
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            try{
//
//             View vz=gridViewAndroid.findViewById(R.id.android_custom_gridview_layout);
//
//                if(!categMain.get(position).color.toLowerCase().contains(("default")))
//                vz.setBackgroundColor(Color.parseColor(categMain.get(position).color));
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//
//            View v=gridViewAndroid.findViewById(R.id.android_custom_gridview_layout);
//
//
//

        // YoYo.with(Techniques.RotateIn).duration(900).playOn(gridViewAndroid);



        return convertView;



    }

    class MyViewHolder {

        // private ImageView imageIcon;
        private TextView order_status;
        private TextView order_title;
        private TextView order_id;
        private TextView payment;
        private TextView payemnt_status;
        //  private TextView time_status;
        // private RatingBar rating;
        private TextView time;
        //private TextView story;

    }
}

