package nf.co.hoproject.genapp;

/**
 * Created by shivesh on 14/6/16.
 */
public class User {

        public String name="dummy";

        public String email="dummy@varrange.com";

        public String facebookID="null";


        public String uid="1";

        public String imageurl="http://placekitten.com.s3.amazonaws.com/homepage-samples/200/138.jpg";

        public String social="null";

        public String googleID="null";

        public String gender="M";

        public String purchases="[]";

        public String lights="0";

        public String password="null";

        public String phone="100";


        public User(String name,String email,String pass,String pho)
        {


                this.name=name;
                this.email=email;
                password=pass;
                phone=pho;
        }




        public User()
        {


        }

}
