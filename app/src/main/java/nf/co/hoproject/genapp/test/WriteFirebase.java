package nf.co.hoproject.genapp.test;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.DataBase.FireConf;
import nf.co.hoproject.genapp.DataBase.FlowModel;
import nf.co.hoproject.genapp.DataBase.FoodItem;
import nf.co.hoproject.genapp.DataBase.ListFlowModel;
import nf.co.hoproject.genapp.DataBase.Review;
import nf.co.hoproject.genapp.DataBase.SubCat;
import nf.co.hoproject.genapp.DataBase.WFoodItem;
import nf.co.hoproject.genapp.R;

public class WriteFirebase extends AppCompatActivity {

    @Bind(R.id.name)
    EditText name;

    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.pass)
    EditText pass;

    @Bind(R.id.phone)
    EditText phone;

    @Bind(R.id.write)
    Button write;

    @Bind(R.id.result)
    TextView result;
    String[] gridViewString = {
            "Hot & Popular", "Festives", "Main Dishes",
            "Dessert", "Drinks", "Chapaties",
            "ToungueBuster","Condiments"

    } ;

    String [] ret = {
            "Awadhi", "Bihari", "Punjabi",
            "Kashmiri", "Bengali", "Chinese",
            "Gujarati","South"

    } ;

    Context ctx;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_firebase);
        ButterKnife.bind(this);

        ctx=this;
        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database");


        listenFirebase();

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }

/************************************************************/

                ListFlowModel lfw=new ListFlowModel();
                lfw.categories=new ArrayList<FlowModel>();
                Character cha='A';
                /************************************************************/
                for(int n=0;n<8;n++) {



                    FlowModel flowModel = new FlowModel(gridViewString[n], "" + cha, "httl",null);
                    flowModel.subcats = new ArrayList<SubCat>();

                    for (int k = 0; k < 8; k++) {
                        ;
                        SubCat sub = new SubCat(ret[k], "" + cha + "" + ret[k].charAt(0) + "" + ret[k].toUpperCase().charAt(1), "httP;//mysomeal.in/" + ret[k] + ".png");
                        sub.fooditems = new ArrayList<FoodItem>();

                        for (int m = 0; m < 8; m++) {

                            FoodItem f = new FoodItem("Banana", "Nice", "Manav Ahar", "" + cha + "" + ret[k].charAt(0) + "" + ret[k].toUpperCase().charAt(1) + ""
                                    + m, "50", "39", "http", 3.0);

                            f.reviews = new ArrayList<Review>();
                            for (int j = 0; j < 5; j++) {
                                Review r = WFoodItem.createReview("shivesh", "1", "shiveshnavin@gmail.com", "90", "Nice One", 3.0);
                                f.reviews.add(r);
                            }
                            sub.fooditems.add(f);
                        }

                        flowModel.subcats.add(sub);

                    }
                    lfw.categories.add(flowModel);
                    cha++;
                }

                fire.child("database").setValue(lfw);

/************************************************************/


            }
        });


    }

    Firebase fire;
    public void listenFirebase()
    {

        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database");


        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

/************************************************************/
                /*    pr = postSnapshot.getValue(PersonArray.class);

                    for (int j = 0; j < pr.prs.size(); j++) {
                        Person person = pr.prs.get(j);

                        //Adding it to a string
                        String string = "Name: " + person.name + "\nEmail: " + person.email
                                + "\nPhone: " + person.phone + "\nPass: " + person.pass + "\n\n";

                        Log.d("Entry" + j, string);
                        }*/

                        result.setText(postSnapshot.toString());

/************************************************************/




                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
