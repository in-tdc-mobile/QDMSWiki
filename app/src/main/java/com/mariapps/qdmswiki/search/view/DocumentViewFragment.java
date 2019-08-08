package com.mariapps.qdmswiki.search.view;

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
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.bookmarks.view.BookmarkActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.home.database.HomeDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DocumentViewFragment extends BaseFragment {

    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.showMenuFab)
    AppCompatImageView showMenuFab;
    @BindView(R.id.webView)
    WebView webView;

    private String folderName;
    private Integer id;
    private HomeDao homeDao;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_view, container, false);
        ButterKnife.bind(this, view);

        try{
            Bundle args = getArguments();
            id= args.getInt(AppConfig.BUNDLE_FOLDER_ID, 0);
            folderName= args.getString(AppConfig.BUNDLE_FOLDER_NAME, "");
        }
        catch (Exception e){}

        homeDao = new HomeDao(getActivity());
        loadDocument();

        return view;
    }

    private void loadDocument() {
        String document = homeDao.fetchDocumentData();
        webView.loadData(document, "text/html; charset=utf-8", "UTF-8");
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
                lp.setMargins(50,25,50,0);

                // Apply the updated layout parameters to TextView
                popupView.setLayoutParams(lp);

                LinearLayout linInfo = popupView.findViewById(R.id.linInfo);
                LinearLayout linBookmark = popupView.findViewById(R.id.linBookmark);
                LinearLayout linDownload = popupView.findViewById(R.id.linDownload);

                linInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DocumentInfoViewActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,folderName);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,id);
                        startActivity(intent);
                    }
                });

                linBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BookmarkActivity.class);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,folderName);
                        intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,id);
                        startActivity(intent);
                    }
                });

                break;
        }
    }
}
