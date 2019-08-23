package com.mariapps.qdmswiki.search.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.bookmarks.view.BookmarkActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DocumentViewFragment extends BaseFragment {

    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.showMenuFab)
    AppCompatImageView showMenuFab;
    @BindView(R.id.webView)
    WebView webView;

    private String folderName;
    private String documentData;
    private Integer id;
    private HomeDatabase homeDatabase;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_view, container, false);
        ButterKnife.bind(this, view);

        try {
            Bundle args = getArguments();
            id = args.getInt(AppConfig.BUNDLE_FOLDER_ID, 0);
            folderName = args.getString(AppConfig.BUNDLE_FOLDER_NAME, "");
        } catch (Exception e) {
        }

        homeDatabase = HomeDatabase.getInstance(getActivity());
        loadDocument();
//        setHTMLContent();
        return view;
    }

    public void loadDocument() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentData = homeDatabase.homeDao().getDocumentData();
                documentData=documentData.replace("<script src=\"/WikiPALApp/Scripts/TemplateSettings/toc-template-settings.js\"></script>",  "<script src=\"./Scripts/toc-template-settings.js.download\"></script>");
                documentData=documentData.replace( "\n",  "");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
//                webView.loadData(documentData, "text/html; charset=utf-8", "UTF-8");
                setHTMLContent();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @OnClick({R.id.showMenuFab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showMenuFab:
                View popupView = View.inflate(getActivity(), R.layout.menu_pop_up, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 50);
                popupWindow.setOutsideTouchable(false);

                View container = (View) popupWindow.getContentView().getParent();
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(container, p);

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) popupView.getLayoutParams();

                // Set TextView layout margin 25 pixels to all side
                // Left Top Right Bottom Margin
                lp.setMargins(50, 25, 50, 0);

                // Apply the updated layout parameters to TextView
                popupView.setLayoutParams(lp);

                LinearLayout linInfo = popupView.findViewById(R.id.linInfo);
                LinearLayout linBookmark = popupView.findViewById(R.id.linBookmark);
                LinearLayout linDownload = popupView.findViewById(R.id.linDownload);

                linInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DocumentInfoViewActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, id);
                        startActivity(intent);
                    }
                });

                linBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BookmarkActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, id);
                        startActivity(intent);
                    }
                });

                break;
        }
    }

    private void setHTMLContent() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AppJavaScriptProxy(getActivity()), "androidAppProxy");

        webView.setWebViewClient(new WebViewClient() {

//            public void onLoadResource(WebView view, String url) {
//                // Check to see if there is a progress dialog
//                if (progressDialog == null) {
//                    // If no progress dialog, make one and set message
//                    progressDialog = new ProgressDialog(activity);
//                    progressDialog.setMessage("Loading please wait...");
//                    progressDialog.show();
//
//                    // Hide the webview while loading
//                    webView.setEnabled(false);
//                }
//            }

            public void onPageFinished(WebView view, String url) {
                // Page is done loading;
                // hide the progress dialog and show the webview

                webView.setEnabled(true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(getActivity(), "Your Internet Connection May not be active Or " + error.getDescription(), Toast.LENGTH_LONG).show();
            }
        });

        webView.loadUrl("file:///android_asset/templateHTML/TemplateHTML.html");
//        webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }

    public class AppJavaScriptProxy {

        private Activity activity = null;

        public AppJavaScriptProxy(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public String getDocumentData() {
            return documentData;

        }

        @JavascriptInterface
        public String getArtcileData(String id) {
            return  " verry goood";
        }

        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(getActivity(), "ALERT " + message, Toast.LENGTH_LONG).show();
        }


    }

    class Article{
        private String data = null;
        private String name = null;

        public Article(String data, String name ) {
            this.data = data;
            this.name = name;
        }
    }
}
