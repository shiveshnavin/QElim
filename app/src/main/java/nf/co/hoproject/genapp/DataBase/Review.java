package nf.co.hoproject.genapp.DataBase;

/**
 * Created by shivesh on 25/6/16.
 */
public class Review
{

    public String username="RajatR";
    public String userId="12";
    public String email="rajat@gmail.com";

    public String pic="http://placehold.it/200x200";
    public String review="Nice !";

    public Double rating=4.5;


    public Review(String us,String id,String emai,String pi,String revie,Double  rat)
    {
        username=us;
        userId=id;
        email=emai;
        pic=pi;
        rating=rat;

    }

    public Review()
    {

    }

}