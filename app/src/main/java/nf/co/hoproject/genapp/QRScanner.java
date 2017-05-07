package nf.co.hoproject.genapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanner extends  AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        utl.log("SSDSDSD");

        Toast.makeText(this,"Scan the QR for the Restraunt ! ",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(
                "com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);



    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {


                String contents = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                Constants.RESTRAUNT="/"+utl.refineString(contents,"");

                Intent it=new Intent(QRScanner.this, nf.co.hoproject.genapp.inner.ui.pages.ListActivity.class);
                startActivity(it);


                finish();



            }
        }
    }


    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                Constants.RESTRAUNT="/"+utl.refineString(rawResult.getText(),"");

                Intent it=new Intent(QRScanner.this, ListActivity.class);
                startActivity(it);

            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();


        // If you would like to resume scanning, call this method below:
      //  mScannerView.resumeCameraPreview(this);
    }




}
