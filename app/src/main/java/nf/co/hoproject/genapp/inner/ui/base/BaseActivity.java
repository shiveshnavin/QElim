package nf.co.hoproject.genapp.inner.ui.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.squareup.picasso.Picasso;

import java.io.File;

import nf.co.hoproject.genapp.Constants;
import nf.co.hoproject.genapp.DataBase.Order;
import nf.co.hoproject.genapp.FileOperations;
import nf.co.hoproject.genapp.GenericUser;
import nf.co.hoproject.genapp.MyWallet;
import nf.co.hoproject.genapp.R;
import nf.co.hoproject.genapp.Splash;
import nf.co.hoproject.genapp.inner.ui.pages.AboutUs;
import nf.co.hoproject.genapp.inner.ui.pages.Booking;
import nf.co.hoproject.genapp.inner.ui.pages.ContactUs;
import nf.co.hoproject.genapp.inner.ui.pages.Landing;
import nf.co.hoproject.genapp.inner.ui.pages.ListActivity;
import nf.co.hoproject.genapp.inner.ui.pages.Orders;
import nf.co.hoproject.genapp.utl;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "TAG";//makeLogTag(BaseActivity.class);

    protected static final int NAV_DRAWER_ITEM_INVALID = -1;

    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;


    public static TextView username;

    public static  View usernameBack;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
    }

    GenericUser user;
    /**
     * Sets up the navigation drawer.
     */
    private void setupNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawerLayout == null) {
            // current activity does not have a drawer.
            return;
        }

        user= utl.readData(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        try {
            LayoutInflater inf=getLayoutInflater();
          //  View head=inf.inflate(R.layout.navigation_header,navigationView);
            View head=navigationView.getHeaderView(0);

             username=(TextView)head.findViewById(R.id.username);
            usernameBack= head.findViewById(R.id.usernameBack);
            Log.d("deb1x",""+username+" "+usernameBack);

            username.setText(""+user.name);
            AndroidNetworking.get(user.imageurl).build().getAsBitmap(new BitmapRequestListener() {
                @Override
                public void onResponse(Bitmap response) {

                    BitmapDrawable ss= new BitmapDrawable(response) ;
                    usernameBack.setBackground(ss);


                }

                @Override
                public void onError(ANError ANError) {

                }
            });


            usernameBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Log.d("clckd","Clicked mm");
                    Intent i = new Intent(BaseActivity.this, Landing.class);
                    FileOperations fop = new FileOperations();
                    String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                    File us = new File(folder + "/user.jnp");
                    if (us.exists()) {

                        String jstr = fop.read(folder + "/user.jnp");
                        i.putExtra("jstr", jstr);
                    }
                    finish();*/
                }
            });username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Log.d("clckd","Clicked mm");
                    Intent i = new Intent(BaseActivity.this, Landing.class);
                    FileOperations fop = new FileOperations();
                    String folder = Environment.getExternalStorageDirectory().getPath().toString() + "/varange";

                    File us = new File(folder + "/user.jnp");
                    if (us.exists()) {

                        String jstr = fop.read(folder + "/user.jnp");
                        i.putExtra("jstr", jstr);
                    }
                    finish();*/
                }
            });
        } catch (Exception e) {
        e.printStackTrace();
    } if (navigationView != null) {
            setupDrawerSelectListener(navigationView);
            setSelectedItem(navigationView);
            Log.d(TAG, "navigation drawer setup finished");
        }


    }

    /**
     * Updated the checked item in the navigation drawer
     * @param navigationView the navigation view
     */
    private void setSelectedItem(NavigationView navigationView) {
        // Which navigation item should be selected?
        int selectedItem = getSelfNavDrawerItem(); // subclass has to override this method
       // navigationView.setCheckedItem(selectedItem);
    }

    /**
     * Creates the item click listener.
     * @param navigationView the navigation view
     */
    private void setupDrawerSelectListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onNavigationItemClicked(menuItem.getItemId());
                        return true;
                    }
                });
    }

    /**
     * Handles the navigation item click.
     * @param itemId the clicked item
     */
    private void onNavigationItemClicked(final int itemId) {
        if(itemId == getSelfNavDrawerItem()) {
            // Already selected
            closeDrawer();
            return;
        }

        goToNavDrawerItem(itemId);
    }

    /**
     * Handles the navigation item click and starts the corresponding activity.
     * @param item the selected navigation item
     */
    private void goToNavDrawerItem(int item) {
        folder= Environment.getExternalStorageDirectory().getPath().toString()+"/varange";
;
        switch (item) {

            case R.id.mymenu: {
                Log.d("clckd","Clicked Menu");
                Intent i = new Intent(getApplicationContext(), ListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                FileOperations fop = new FileOperations();
                String folder = Constants.getFolder(BaseActivity.this);

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr = fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);
                }
                startActivity(i);
             }
                break;
            case R.id.contactus:
            {


                Intent i=new Intent(getApplicationContext(),ContactUs.class);   FileOperations fop = new FileOperations();
                String folder =Constants.getFolder(BaseActivity.this);

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr= fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);
                }

                startActivity(i);
               /* String url = getResources().getString(R.string.website);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            };
                 break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(),MyWallet.class));
                break;
            case R.id.about:{

                Intent i=new Intent(getApplicationContext(),AboutUs.class);   FileOperations fop = new FileOperations();
                String folder =Constants.getFolder(BaseActivity.this);

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr= fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);
                }

                startActivity(i);}
                break;
            case R.id.book: {
                Intent i = new Intent(getApplicationContext(), Orders.class);
                FileOperations fop = new FileOperations();
                String folder =Constants.getFolder(BaseActivity.this);

                File us = new File(folder + "/user.jnp");
                if (us.exists()) {

                    String jstr = fop.read(folder + "/user.jnp");
                    i.putExtra("jstr", jstr);
                }

                startActivity(i);
            }   break;

            case R.id.rate:
            {
                File g=new File(folder+"/user.jnp");
                g.delete();
                this.startActivity(new Intent(getApplicationContext(), Splash.class));
                this.finish();
                utl.logout(this);
            }


        }
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            utl.log("BACK PRESSED");


            final AlertDialog.Builder
                    alertDialogBuilder = new AlertDialog.Builder
                    (this);
            alertDialogBuilder.setMessage("Exit ?");
            alertDialogBuilder.setPositiveButton("No", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface
                                                    dialog, int id) {

                            dialog.dismiss();;
                            //  ListActivity.this.moveTaskToBack(true);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });


            AlertDialog alertDialog
                    = alertDialogBuilder.create();


            alertDialog.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    String folder;
    /**
     * Provides the action bar instance.
     * @return the action bar.
     */
    protected ActionBar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        return getSupportActionBar();
    }


    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * have to override this method.
     */
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_ITEM_INVALID;
    }

    protected void openDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public abstract boolean providesActivityToolbar();

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
