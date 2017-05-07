package nf.co.hoproject.genapp.DataBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivesh on 2/4/17.
 */

public class PayUResp {



        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("curr")
        @Expose
        public String curr;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("txnid")
        @Expose
        public String txnid;
        @SerializedName("userid")
        @Expose
        public String userid;
        @SerializedName("catid")
        @Expose
        public String catid;
        @SerializedName("levelid")
        @Expose
        public String levelid;
        @SerializedName("info")
        @Expose
        public String info;
        @SerializedName("slot1")
        @Expose
        public String slot1;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("categ")
        @Expose
        public String categ;
        @SerializedName("status")
        @Expose
        public String status;

}
