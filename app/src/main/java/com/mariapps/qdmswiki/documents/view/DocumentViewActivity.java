package com.mariapps.qdmswiki.documents.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class DocumentViewActivity extends BaseActivity {

    @BindView(R.id.folderRV)
    CustomRecyclerView folderRV;
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.showMenuFab)
    AppCompatImageView showMenuFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

//        String htmlText = readData("html.txt");
//
//        webView.loadData(htmlText, "text/html", null);
//        searchET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                webView.findAllAsync(s.toString());
//            }
//        });

    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @OnClick({R.id.backBtn, R.id.showMenuFab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.showMenuFab:
                View popupView = View.inflate(DocumentViewActivity.this, R.layout.menu_pop_up, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 50, 50);
                popupWindow.setOutsideTouchable(false);

                View container = (View) popupWindow.getContentView().getParent();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.3f;
                wm.updateViewLayout(container, p);

                LinearLayout linInfo = popupView.findViewById(R.id.linInfo);
                LinearLayout linBookmark = popupView.findViewById(R.id.linBookmark);
                LinearLayout linDownload = popupView.findViewById(R.id.linDownload);

                linInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DocumentViewActivity.this,DocumentInfoViewActivity.class);
                        startActivity(intent);
                    }
                });

                break;
        }
    }

    public String readData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }
}
