package nf.co.hoproject.genapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by one on 31/3/16.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :

                SigninFragment signinFragment = new SigninFragment();
                return signinFragment;

            case 1 :

                SignupFragment signupFragment = new SignupFragment();
                return signupFragment;


        }

    return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
