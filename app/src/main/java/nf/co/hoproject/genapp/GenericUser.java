package nf.co.hoproject.genapp;

import org.json.JSONObject;

/**
 * Created by shivesh on 16/6/16.
 */


/*



 {public String uid=11;
 public String name=shivesh navin;
 public String username=shiveshnavin;
 public String gender=M;
 public String email=shiveshnavin@gmail.com;
 public String password=google;
 public String lights=0;
 public String previous_purchasespublic String :[],public String phone=8527483275;
 public String notificationspublic String :[],
 public String imagepublic String :{public String link=https://lh3.googleusercontent.com/-Wf45krcPhOo/AAAAAAAAAAI/AAAAAAAAADc/MxU_U1Y8_E8/photo.jpg?sz=50public String },
 public String social=google;
 public String facebookId=null;
 public String googleuserId=10141509439288583253public String }


 */
public class GenericUser {




            public String uid="11";
            public String name="dummy man";
            public String username="dummy";
    public String facebookID="dummy";
    public String imageurl="dummy";
    public String googleID="dummy";
            public String gender="M";
            public String email="dummy@gmail.com";
            public String password="password";
            public String lights="0";
            public String previous_purchases="[]";
            public String phone="8527483275";
            public String notifications ="[]";

    public String image ="{\"link\"=\"http://placekitten.com.s3.amazonaws.com/homepage-samples/200/138.jpg\" }";
    //public String imagepublic String ="{\"link\"=\"https://lh3.googleusercontent.com/-Wf45krcPhOo/AAAAAAAAAAI/AAAAAAAAADc/MxU_U1Y8_E8/photo.jpg?sz=50public String\" }";
            public String social="null";
            public String facebookId="null";
            public String googleuserId="10141509439288583253";

public  GenericUser(String jstr)
{
    try {
        JSONObject jo = new JSONObject(jstr);

        uid=jo.getString("uid");
        name=jo.getString("name");
        username=jo.getString("username");
        gender=jo.getString("gender");
        email=jo.getString("email");
        password=jo.getString("password");

        lights=jo.getString("lights");
        previous_purchases=jo.getString("previous_purchases");
        phone=jo.getString("phone");
        notifications=jo.getString("notifications");


    }catch (Exception e)
    {
        e.printStackTrace();
    }


}


    public GenericUser()
    {

    }





}
