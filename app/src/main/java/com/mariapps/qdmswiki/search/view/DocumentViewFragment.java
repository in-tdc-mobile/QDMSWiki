package com.mariapps.qdmswiki.search.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.ArticleModelObj;
import com.mariapps.qdmswiki.ArticleModelObj_;
import com.mariapps.qdmswiki.BuildConfig;
import com.mariapps.qdmswiki.DocumentModelObj;
import com.mariapps.qdmswiki.DocumentModelObj_;
import com.mariapps.qdmswiki.ObjectBox;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.TagModelObj;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.bookmarks.view.BookmarkActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.RecentlyViewedModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.Property;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DocumentViewFragment extends BaseFragment {

    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.btnDown)
    AppCompatImageView btnDown;
    @BindView(R.id.btnUp)
    AppCompatImageView btnUp;
    @BindView(R.id.showMenuFab)
    FloatingActionButton showMenuFab;
    @BindView(R.id.webView)
    WebView webView;
    private String folderName;
    private String folderId;
    private String name;
    private String type;
    private String bookmarkid;
    private String bookmarkall;
    private DocumentModel documentModel;
    private String id;
    private String documentData;
    private String version;
    HashMap<String, ArticleModel> articleMap=new HashMap<String, ArticleModel>();
    private ArticleModel articleModel;
    private HomeDatabase homeDatabase;
    private RecentlyViewedModel recentlyViewedModel;
    private String articleData;
    private boolean flag = false;
    private List<BookmarkEntryModel> bookmarkEntryModelList;
    public static final int REQUEST_CODE = 11;
    public static final String BOOKMARKID = "BOOKMARK_ID";
    public static final int RESULT_CODE = 12;
    private String fileData;
    private FormsModel formsModel;
    private String imageFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QDMSWiki/Images/";
    private ProgressDialog progressDialog;
    String docNum;
    String docDate;
    String docVersion;
    Box<DocumentModelObj> dbox;
    Box<ArticleModelObj> abox;
    AsyncTask loaddoc;
    List<BookmarkEntryModel> bookmarkidlist = new ArrayList<>();
    String bookmarkstring="";
    List<BookmarkEntryModel> checkmodel = new ArrayList<>();




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
            id = args.getString(AppConfig.BUNDLE_ID, "");
            name = args.getString(AppConfig.BUNDLE_NAME, "");
            type = args.getString(AppConfig.BUNDLE_TYPE, "");
            folderId = args.getString(AppConfig.BUNDLE_FOLDER_ID, "");
            folderName = args.getString(AppConfig.BUNDLE_FOLDER_NAME, "");
            version = args.getString(AppConfig.BUNDLE_VERSION, "");
            bookmarkall = args.getString(AppConfig.BOOKMARK_ALL,"");
            bookmarkid = args.getString(AppConfig.BOOKMARK_ID,"");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading");
            dbox = ObjectBox.get().boxFor(DocumentModelObj.class);
            abox = ObjectBox.get().boxFor(ArticleModelObj.class);
            if (type.equals("Document")) {
                recentlyViewedModel = new RecentlyViewedModel(id, name, folderId, folderName, version, DateUtils.getCurrentDate());
                insertRecentlyViewedDocument(recentlyViewedModel);
            }
        } catch (Exception e) {
        }

        homeDatabase = HomeDatabase.getInstance(getActivity());
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                   // webView.findAllAsync(searchET.getText().toString());
                    flag = true;
                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                        m.invoke(webView, true);
                    } catch (Throwable ignored) {
                    }
                } else {
                    webView.findNext(true);
                }
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    //webView.findAllAsync(searchET.getText().toString());
                    flag = true;
                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                        m.invoke(webView, true);
                    } catch (Throwable ignored) {
                    }
                } else {
                    webView.findNext(false);
                }
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                webView.findAllAsync(s.toString());
            }
        });
       highlightbookmark();
        return view;
    }

    public void highlightbookmark(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
             bookmarkidlist = HomeDatabase.getInstance(getActivity().getApplicationContext()).homeDao().getBookmarkEntries(id);
             Log.e("bookmarkidlist",bookmarkidlist.size()+"");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                if(bookmarkidlist.size()>0){
                    for (int i = 0; i < bookmarkidlist.size(); i++) {
                        String id = "#"+bookmarkidlist.get(i).getBookmarkId();
                        bookmarkstring = bookmarkstring+","+id;
                    }
                    bookmarkstring= bookmarkstring.substring(1);
                    Log.e("thisisbookmarkid111",bookmarkstring);
                }
                loadDocument();
            }
            @Override
            public void onError(Throwable e) {
                loadDocument();
                Log.e("artcileiderror",e.getLocalizedMessage()+"   "+articleModel.getId());
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
                if(type.equals("Article"))
                    linBookmark.setVisibility(View.GONE);
                else
                    linBookmark.setVisibility(View.VISIBLE);
                linInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DocumentInfoViewActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                        intent.putExtra(AppConfig.BUNDLE_ID, id);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, folderId);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                });
                linBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BookmarkActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                        intent.putExtra(AppConfig.BUNDLE_ID, id);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            gotoBookmark(data.getStringExtra("BOOKMARK_ID"));
        }
    }

    public void loadDocument() {
     loaddoc=   new AsyncTask<String,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }
            @Override
            protected String doInBackground(String... strings) {
                if (type.equals("Document")) {
                    Log.e("idofdocument",id);
                    try {
                        documentModel = homeDatabase.homeDao().getDocumentData(id);
                        documentData = dbox.query().equal(DocumentModelObj_.id,id).build().find().get(0).documentData;
                    } catch (Exception e) {
//                        Log.e("documentDatacatchid",dbox.getAll().size()+"");
                    }
                } else {
                    List<TagModel> taglist = new ArrayList<>();
                    documentModel = new DocumentModel("", "", "", "",  "",taglist);
                    articleModel = homeDatabase.homeDao().getArticleData(id);
                    documentData = abox.query().equal(ArticleModelObj_.id,id).build().find().get(0).documentData;
                }
                if(documentData!=null){
                    documentData = documentData.replace("<script src=\"/WikiPALApp/Scripts/TemplateSettings/toc-template-settings.js\"></script>", "<script src=\"./Scripts/toc-template-settings.js.download\"></script>");
                    documentData = documentData.replace("src=\"/WikiPALApp/Uploads/Image/","src= \"file://"+imageFolderPath);
                    documentData = documentData.replace("src=\"/WikiPALApp/Scripts/TemplateSettings/","src= \"file:///android_asset/templateHTML/Scripts/");
                    documentData = documentData.replace("href=\"/WikiPALApp/Content/TemplateSettings/","href= \"file:///android_asset/templateHTML/Scripts/");
                }
                documentData = documentData.replace("\n", "");
                documentModel.setDocumentData(documentData);
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                setHTMLContent();
                progressDialog.dismiss();
                super.onPostExecute(s);
            }
        }.execute();
    }

    private void setHTMLContent() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebContentsDebuggingEnabled(true);
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
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            public void onPageFinished(WebView view, String url) {
                // Page is done loading;
                // hide the progress dialog and show the webview
                webView.setEnabled(true);
                if(bookmarkall.equals("yes")){
                    Log.e("onPageFinished","bookmark");
                    gotoBookmark(bookmarkid);
                }
                else{
                    Log.e("onPageFinished","bookmarkelse");
                }
                webView.loadUrl("javascript:highlightbookmarks('"+bookmarkstring +"');");

            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("onReceivedError",error.getDescription()+" "+error.getErrorCode());
              //  Toast.makeText(getActivity(), "Your Internet Connection May not be active Or " + error.getDescription(), Toast.LENGTH_LONG).show();
            }
        });
/*
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("MyApplicationQDMSa", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });*/
       // webView.loadDataWithBaseURL("file:///android_asset/templateHTML/TemplateHTML.html", "<style>img{display: inline;height: auto;max-width: 100%;}</style>", "text/html", "UTF-8", null);
        webView.loadUrl("file:///android_asset/templateHTML/TemplateHTML.html");
//        webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }


    public void loadArticle(String id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleModel = ((ArticleModel) homeDatabase.homeDao().getArticleData(id));
                String articleData =documentData = abox.query().equal(ArticleModelObj_.id,id).build().find().get(0).documentData;;
                articleData = articleData.replace("<script src=\"/WikiPALApp/Scripts/TemplateSettings/toc-template-settings.js\"></script>", "<script src=\"./Scripts/toc-template-settings.js.download\"></script>");
                articleData = articleData.replace("src=\"/WikiPALApp/Uploads/Image/", "src= \"file://"+imageFolderPath);
                articleData = articleData.replace("src=\"/WikiPALApp/Scripts/TemplateSettings/","src= \"file:///android_asset/templateHTML/Scripts/");
                articleData = articleData.replace("href=\"/WikiPALApp/Content/TemplateSettings/","href= \"file:///android_asset/templateHTML/Scripts/");
                articleData = articleData.replace("\n", "");
                articleModel.setDocumentData(articleData);
                articleMap.put(id,articleModel);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onComplete() {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webView.loadUrl("javascript:setArticleDataFromViewController('" + articleModel.getId() + "');");
                            if(bookmarkall.equals("yes")){
                                Log.e("onPageFinished","bookmark");
                                gotoBookmark(bookmarkid);
                            }
                            else{
                                Log.e("onPageFinished","bookmarkelse");
                            }
                           // webView.loadUrl("javascript:highlightbookmarks('"+bookmarkstring +"');");
                          //  webView.loadUrl("javascript:highlightbookmarks();");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //webView.loadUrl("javascript:setArticleDataFromViewController('" + articleModel.getDocumentData() + "','" + articleModel.getId() + "');$(`.article-table`).removeClass(`hide-state`); $(`.article-table>tbody>tr`).removeClass(`hide-article`);$(`span.toggle-article`).addClass(`opentoggle`)");
                    }
                });
            }
            @Override
            public void onError(Throwable e) {
                Log.e("artcileiderror",e.getLocalizedMessage()+"   "+articleModel.getId());
            }
        });

        /*webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("MyApplicationQDMSa", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });*/
    }

    public void loadFooter() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (type.equals("Document")) {
                    docNum = documentModel.getDocumentNumber();
                    docDate = DateUtils.getFormattedDateinDDMMYYYY(documentModel.getDate());
                    docVersion = documentModel.getVersion();
                } else {
                    docNum = String.valueOf(articleModel.getArticleNumber());
                    docDate = DateUtils.getFormattedDateinDDMMYYYY(articleModel.getDate());
                    docVersion = String.valueOf(articleModel.getVersion());
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:setFooterDataAfterRender($('#tableDragger'),'" + docNum + "','" + docDate + "','" + docVersion+"')");
                    }
                });
            }
            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void gotoBookmark(String id){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("gotoBookmark",id);
                        webView.loadUrl("javascript:gotoElement('" + id + "')");
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void openDocumentAttachment(String id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModel = new DocumentModel("", "", "", "", "", new ArrayList<TagModel>());
                documentModel = ((DocumentModel) homeDatabase.homeDao().getDocumentDetails(id));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_PAGE,"DocumentView");
                        intent.putExtra(AppConfig.BUNDLE_TYPE, documentModel.getType());
                        intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getDocumentName());
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                        intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCategoryId());
                        intent.putExtra(AppConfig.BUNDLE_VERSION, documentModel.getVersion());
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void openArticleAttachment(String id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleModel = new ArticleModel("", "", "", new ArrayList<String>());
                articleModel = ((ArticleModel) homeDatabase.homeDao().getArticleDetails(id));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_PAGE,"DocumentView");
                        intent.putExtra(AppConfig.BUNDLE_TYPE, "Article");
                        intent.putExtra(AppConfig.BUNDLE_NAME, articleModel.getArticleName());
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, "");
                        intent.putExtra(AppConfig.BUNDLE_ID, articleModel.getId());
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,"");
                        intent.putExtra(AppConfig.BUNDLE_VERSION, articleModel.getVersion());
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void openFileAttachment(String id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                formsModel = homeDatabase.homeDao().getFileId(id);
                fileData = homeDatabase.homeDao().getFileData(formsModel.getFileID());

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                decodeFile(fileData,formsModel.getFileName(),formsModel.getFileExtention());
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }
    public void decodeFile(String strFile, String filename, String fileExtention) {
        String fullpath = "";
        try {
            byte[] data = Base64.decode(strFile, Base64.DEFAULT);
            File ext = Environment.getExternalStorageDirectory();
            File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }
            if(fileExtention.equals(".jfif")){
                filename = filename+".jpeg";
            }
            fullpath = ext.getAbsolutePath() + "/QDMSWiki/" + filename;
            File file = new File(mydir, filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            FileOpen(fullpath,fileExtention);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void FileOpen(String FilePath, String fileExtention) {
        if (!FilePath.equals("")) {
            try {
                Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", new File(FilePath));
                String type = FilePath.substring(FilePath.lastIndexOf('.'));
                String extension = MimeTypeMap.getFileExtensionFromUrl(type);
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(uri, type);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No handler for this type of file.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void insertRecentlyViewedDocument(RecentlyViewedModel recentlyViewedModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().insertRecentlyViewedDocument(recentlyViewedModel);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
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
        public void getArtcileData(String id) {
            loadArticle(id);
        }

        @JavascriptInterface
        public void logdata(String id) {
         Log.e("errorofromjs",id);
        }

        @JavascriptInterface
        public void getDocumentAttachment(String fileId) {
            openDocumentAttachment(fileId);
        }

        @JavascriptInterface
        public void getArticleAttachment(String fileId) {
            openArticleAttachment(fileId);
        }

        @JavascriptInterface
        public void getFileAttachment(String fileId) {
            openFileAttachment(fileId);
        }

        @JavascriptInterface
        public void getFooterData() {
            loadFooter();
        }

        @JavascriptInterface
        public void saveBookmark(String bookmarkEntity) {
            String[] bookmarkEntityArray = bookmarkEntity.split("##");
            BookmarkEntryModel bookmarkEntryModel = new BookmarkEntryModel(id,bookmarkEntityArray[0],bookmarkEntityArray[1]);
            checkbookmark(id,bookmarkEntryModel);
        }

        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(getActivity(), "ALERT " + message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public String getArticleData(String id) {
            return (articleMap.get(id)).getDocumentData();
        }
    }
    public void checkbookmark(String documentId, BookmarkEntryModel bookmarkEntryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                checkmodel.addAll(HomeDatabase.getInstance(getActivity().getApplicationContext()).homeDao().checkBookmarkEntriesall(bookmarkEntryModel.getBookmarkId()));
                Log.e("querycheck",checkmodel.size()+"  "+checkmodel.size());

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
           //     progressDialog.show();
            }
            @Override
            public void onComplete() {
             //   progressDialog.dismiss();
                if(checkmodel.size()==0){
                    getBookmarkEntries( documentId,  bookmarkEntryModel);
                }
                else {
                    AlertDialog.Builder bu = new AlertDialog.Builder(getActivity());
                    bu.setTitle("Bookmark");
                    bu.setMessage("Do you want to remove the bookmark?");
                    bu.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteBookmarkEntries(documentId,bookmarkEntryModel);
                        }
                    });
                    bu.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                        }
                    });
                    bu.show();
                }
                checkmodel.clear();
                //adapterAll.notifyDataSetChanged();
                // progressDialog.dismiss();
            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                // adapterAll.notifyDataSetChanged();
                Log.e("getBookMarkEntryList", e.getLocalizedMessage());
            }
        });
    }



    public void getBookmarkEntries(String documentId, BookmarkEntryModel bookmarkEntryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentId != null) {
                    homeDatabase.homeDao().deleteBookmarkEntry(bookmarkEntryModel.getDocumentId(),bookmarkEntryModel.getBookmarkId());
                    homeDatabase.homeDao().insertBookmarkEntry(bookmarkEntryModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                webView.loadUrl("javascript:highlightbookmarks('"+"#"+bookmarkEntryModel.getBookmarkId() +"');");
                showCustomToast("Bookmark Saved",Toast.LENGTH_SHORT);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteBookmarkEntries(String documentId, BookmarkEntryModel bookmarkEntryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentId != null) {
                    homeDatabase.homeDao().deleteBookmarkEntry(bookmarkEntryModel.getDocumentId(),bookmarkEntryModel.getBookmarkId());

                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                String bookid = "#"+bookmarkEntryModel.getBookmarkId();
                webView.loadUrl("javascript:removehighlightedbookmarks('"+bookid+"');");
                showCustomToast("Bookmark Removed",Toast.LENGTH_SHORT);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    class Article {
        private String data = null;
        private String name = null;

        public Article(String data, String name) {
            this.data = data;
            this.name = name;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(loaddoc!=null&&loaddoc.getStatus()== AsyncTask.Status.RUNNING){
          loaddoc.cancel(true);
        }
        progressDialog.cancel();
        webView.onPause();
    }
}
