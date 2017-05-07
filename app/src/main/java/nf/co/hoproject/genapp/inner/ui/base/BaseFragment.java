package nf.co.hoproject.genapp.inner.ui.base;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import nf.co.hoproject.genapp.inner.util.LogUtil;

public class BaseFragment extends Fragment {

    private static final String TAG = "TAG";//makeLogTag(BaseFragment.class);

    public View inflateAndBind(LayoutInflater inflater, ViewGroup container, int layout) {
        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);

        LogUtil.logD(TAG, ">>> view inflated");
        return view;
    }
}
