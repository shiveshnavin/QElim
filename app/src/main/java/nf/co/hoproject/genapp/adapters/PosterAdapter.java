package nf.co.hoproject.genapp.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nf.co.hoproject.genapp.DataBase.Item;
import nf.co.hoproject.genapp.R;


public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.CustomViewHolder> {
    public List<Item> feedItemList;
    private Context ctx;

    public PosterAdapter(Context context, List<Item> feedItemList) {
        this.feedItemList = feedItemList;
        this.ctx = context;
        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
          width = windowManager.getDefaultDisplay().getWidth();

    }

    public void dot(int pos)
    {

    }

    public static int width;
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poster_row,  null, false);
      //  view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));


        CustomViewHolder viewHolder = new CustomViewHolder(view);

        LinearLayout.LayoutParams par=new LinearLayout.LayoutParams(new Double(width).intValue()
                , ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(par);

//        YoYo.with(Techniques.SlideInLeft).duration(500).playOn( view);
        return viewHolder;
    }

    public static class Qant{
        public int quan=1;
    }





    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
               //Setting text view title
      final Item cat=feedItemList.get(i);
        final int id=i;
        final Qant qn=new Qant();
        customViewHolder.item.setText(Html.fromHtml(cat.name));
        customViewHolder.mrp.setText(Html.fromHtml("₹"+cat.mrp));
        //customViewHolder.brand.setText(Html.fromHtml(cat.brand));
//        customViewHolder.id.setText(Html.fromHtml(cat.id));
        customViewHolder.mrp.setPaintFlags( customViewHolder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Double prc=-Float.parseFloat(""+cat.mrp)-Float.parseFloat(""+cat.mrp)*cat.discount;
        customViewHolder.price.setText(Html.fromHtml("₹"+Item.getPrice(cat)));
        customViewHolder.save.setText(Html.fromHtml("<b>FLAT "+cat.discount+" % OFF</b>"));
        customViewHolder.rating.setRating(cat.rating.floatValue());
        customViewHolder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("CLick",""+cat.name+"\n"+qan+"\nID"+id);
                cat.quan=qn.quan;
                add(cat,qn.quan,  id);
            }
        });

        customViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat.quan=qn.quan;
                click(cat,  id);
            }
        });
       final List<String> categories = new ArrayList<String>();
       for(int j=1;j<10;j++)
        categories.add(""+j);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, categories);

        customViewHolder.quan.setAdapter(dataAdapter);
        customViewHolder.quan.setSelection(0);
        customViewHolder.quan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int no=Integer.parseInt(categories.get(i));
                qan=no;
                qn.quan=no;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        try {
            Picasso.with(ctx).load(cat.icon)
                   // .error(R.drawable.cart)
                  //  .placeholder(R.drawable.cart)
                    .into(customViewHolder.itempic);


            Picasso.with(ctx).load(cat.icon)
                    // .error(R.drawable.cart)
                     .placeholder(R.drawable.hm3)
                    .into(customViewHolder.back);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    int qan;
    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void add(Item cat,int quan,int id)
    {

    }


    public void click(Item cat,int id)
    {

    }


public class CustomViewHolder extends RecyclerView.ViewHolder
{
    public TextView item;
    public TextView mrp;
    public TextView price;
    public TextView brand;
    public TextView id;
    public TextView addtocart;
    public TextView save;

    public RatingBar rating;
    public ImageView itempic,back;
    public Spinner quan;
    public View view;



    public CustomViewHolder(View itemView) {
        super(itemView);
        view=itemView;
        item=(TextView)itemView.findViewById(R.id.item);
        mrp=(TextView)itemView.findViewById(R.id.mrp);
        //brand=(TextView)itemView.findViewById(R.id.brand);
        //id=(TextView)itemView.findViewById(R.id.id);
        price=(TextView)itemView.findViewById(R.id.price);
        addtocart =(TextView)itemView.findViewById(R.id.addtocart);
        save=(TextView)itemView.findViewById(R.id.save);


        itempic=(ImageView)itemView.findViewById(R.id.itempic);
        back=(ImageView)itemView.findViewById(R.id.back);
        rating=(RatingBar)itemView.findViewById(R.id.ratings);
        quan=(Spinner)itemView.findViewById(R.id.quan);




    }
}


public class Dummy
{

}






}
