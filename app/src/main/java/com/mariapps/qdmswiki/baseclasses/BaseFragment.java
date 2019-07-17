package com.mariapps.qdmswiki.baseclasses;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mariapps.qdmswiki.R;

/**
 * Created by aruna.ramakrishnan on 26-June-2019
 */
public abstract class BaseFragment extends Fragment {

    protected abstract void setUpPresenter();

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.str_loading));
        progressDialog.setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpPresenter();
    }


    @Override
    public void onStart() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void setUpDialog(Context context) {

    }

    public void showCustomToast(String message, int toastLength) {
        Toast.makeText(getActivity(), message, toastLength).show();
    }







}

