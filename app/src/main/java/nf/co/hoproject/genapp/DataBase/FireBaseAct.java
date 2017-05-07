package nf.co.hoproject.genapp.DataBase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.R;

public class FireBaseAct extends AppCompatActivity {

    @Bind(R.id.button)
    View but;
    @Bind(R.id.res)
    TextView res;
    Firebase fire;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);
        ButterKnife.bind(this);

        ctx=this;
        Firebase.setAndroidContext(ctx);
        fire= new Firebase(Constants.fire(ctx)+"/database");



        listenFirebase();
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    res.setText("Working...");

                    /************************************************************/

                    fire.child("database").setValue(new Du());

                    /************************************************************/

                }catch (Exception e)
                {
                    e.printStackTrace();
                }catch (OutOfMemoryError e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

public class Du{

    String a="a";
    String b="b";

}

    public void listenFirebase()
    {



        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

/************************************************************/

                    res.setText(postSnapshot.toString());

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
