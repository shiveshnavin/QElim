package nf.co.hoproject.genapp.DataBase;

/**
 * Created by shivesh on 25/6/16.
 */
public class Order {

//delivered  = green, ready to pick = yell, process = red ;
    public String order ="Hot Dog";
    public String orderStatus =" Delivered";
    public String payment="10000" ;
    public String paymentStatus="Paid" ;
    public String timee = "2:40";
    public String timeStatus = "Ok" ;






    public Order(String order, String orderStatus, String payment, String paymentStatus,  String timee, String timeStatus )
    {
        this.order =order;
        this.orderStatus=orderStatus;
        this.payment=payment;
        this.paymentStatus=paymentStatus;
        this.timee=timee;
        this.timeStatus = timeStatus;


    }

    public Order()
    {


    }



}
