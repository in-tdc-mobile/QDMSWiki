//package com.mariapps.qdmswiki.documents.view;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.AppCompatImageView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.Toast;
//
//import com.mariapps.qdmswiki.R;
//import com.mariapps.qdmswiki.baseclasses.BaseActivity;
//import com.mariapps.qdmswiki.bookmarks.view.BookmarkActivity;
//import com.mariapps.qdmswiki.custom.CustomEditText;
//import com.mariapps.qdmswiki.custom.CustomRecyclerView;
//import com.mariapps.qdmswiki.custom.CustomTextView;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//public class DocumentViewActivity extends BaseActivity {
//
//    @BindView(R.id.headingTV)
//    CustomTextView headingTV;
//    @BindView(R.id.searchET)
//    CustomEditText searchET;
//    @BindView(R.id.showMenuFab)
//    AppCompatImageView showMenuFab;
//    @BindView(R.id.webView)
//    WebView webView;
//
//    @SuppressLint("JavascriptInterface")
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_document_view);
//
////
////        webView.loadUrl("file:///android_asset/QDMS PAL 1.0.0.1.html");
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.addJavascriptInterface(new JsInterface(), "android");
//
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
//
//    }
//
//    public class JsInterface{
//        @JavascriptInterface
//        public void showToast()
//        {
//           Toast.makeText(getApplicationContext(),"Call clicked",Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//    @Override
//    protected void setUpPresenter() {
//
//    }
//
//    @Override
//    protected void isNetworkAvailable(boolean isConnected) {
//
//    }
//
//    @OnClick({R.id.backBtn, R.id.showMenuFab})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.backBtn:
//                onBackPressed();
//                break;
//            case R.id.showMenuFab:
//                View popupView = View.inflate(DocumentViewActivity.this, R.layout.menu_pop_up, null);
//                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 50);
//                popupWindow.setOutsideTouchable(false);
//
//                View container = (View) popupWindow.getContentView().getParent();
//                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
//                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                p.dimAmount = 0.5f;
//                wm.updateViewLayout(container, p);
//
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) popupView.getLayoutParams();
//
//                // Set TextView layout margin 25 pixels to all side
//                // Left Top Right Bottom Margin
//                lp.setMargins(50,25,50,0);
//
//                // Apply the updated layout parameters to TextView
//                popupView.setLayoutParams(lp);
//
//                LinearLayout linInfo = popupView.findViewById(R.id.linInfo);
//                LinearLayout linBookmark = popupView.findViewById(R.id.linBookmark);
//                LinearLayout linDownload = popupView.findViewById(R.id.linDownload);
//
//                linInfo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(DocumentViewActivity.this,DocumentInfoViewActivity.class);
//                        startActivity(intent);
//                    }
//                });
//
//                linBookmark.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(DocumentViewActivity.this, BookmarkActivity.class);
//                        startActivity(intent);
//                    }
//                });
//
//                break;
//        }
//    }
//
//    public String readData(String inFile) {
//        String tContents = "";
//
//        try {
//            InputStream stream = getAssets().open(inFile);
//
//            int size = stream.available();
//            byte[] buffer = new byte[size];
//            stream.read(buffer);
//            stream.close();
//            tContents = new String(buffer);
//        } catch (IOException e) {
//            // Handle exceptions here
//        }
//
//        return tContents;
//
//    }
//}
