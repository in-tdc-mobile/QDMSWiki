package com.mariapps.qdmswiki.articles.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.articles.adapter.ArticlesAdapter;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
import com.mariapps.qdmswiki.documents.view.DocumentsFragment;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class ArticlesFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;
    @BindView(R.id.loading_spinner)
    ProgressBar loadingSpinner;

    private FragmentManager fragmentManager;
    private ArticlesAdapter articlesAdapter;
    private List<ArticleModel> articleList = new ArrayList<>();
    List<String> categoryIds = new ArrayList<>();
    List<String> categoryNames = new ArrayList<>();
    private HomeDatabase homeDatabase;
    private String categoryName;
    String folderName;
//    ArticleListener articleListener;

    @Override
    protected void setUpPresenter() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        ButterKnife.bind(this, view);
        RxJavaPlugins.setErrorHandler(throwable -> {}); // nothing or some logging

        fragmentManager = getFragmentManager();

        rvDocuments.setHasFixedSize(true);
        rvDocuments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDocuments.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        homeDatabase = HomeDatabase.getInstance(getActivity());

        getArticlesList();
        return view;
    }

    private void setData() {
        loadingSpinner.setVisibility(View.GONE);
        if (articleList != null && articleList.size() > 0) {
            setRecyclerView();
            noDataRL.setVisibility(View.GONE);
        }
        else{
            rvDocuments.setAdapter(null);
            noDataRL.setVisibility(View.VISIBLE);
        }

    }

    public void getArticlesList() {
        loadingSpinner.setVisibility(View.VISIBLE);
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleList = homeDatabase.homeDao().getArticles();
                for(int i=0;i<articleList.size();i++){
                    categoryNames = new ArrayList<>();
                    for(int j=0;j<articleList.get(i).getCategoryIds().size();j++){
                        categoryName = homeDatabase.homeDao().getCategoryName(articleList.get(i).getCategoryIds().get(j));
                        if(!categoryNames.contains(categoryName))
                            categoryNames.add(categoryName);
                    }
                    articleList.get(i).setCategoryNames(categoryNames);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                setData();
            }


            @Override
            public void onError(Throwable e) {
                loadingSpinner.setVisibility(View.GONE);
            }
        });
    }

    private void setCategoryNames(List<ArticleModel> articleList) {
        System.out.println("article list size "+articleList.size());
        for(int i=0;i<articleList.size();i++){
            categoryNames = new ArrayList<>();
            for(int j=0;j<articleList.get(i).getCategoryIds().size();j++){
                categoryName = homeDatabase.homeDao().getCategoryName(articleList.get(i).getCategoryIds().get(j));
                if(!categoryNames.contains(categoryName))
                    categoryNames.add(categoryName);
            }
            articleList.get(i).setCategoryNames(categoryNames);
        }
        setData();
    }


    @OnTextChanged(R.id.searchET)
    void onTextChanged() {
        if (articlesAdapter != null) {
            articlesAdapter.getFilter().filter(searchET.getText().toString());
        }

    }

    private void setRecyclerView() {
        articlesAdapter = new ArticlesAdapter(getActivity(), articleList);
        rvDocuments.setAdapter(articlesAdapter);
        articlesAdapter.setRowClickListener(new ArticlesAdapter.RowClickListener() {
            @Override
            public void onItemClicked(ArticleModel articleModel) {
                Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_PAGE,"Article");
                intent.putExtra(AppConfig.BUNDLE_TYPE, "Article");
                intent.putExtra(AppConfig.BUNDLE_NAME, articleModel.getArticleName());
                intent.putExtra(AppConfig.BUNDLE_ID, articleModel.getId());
                intent.putExtra(AppConfig.BUNDLE_VERSION,articleModel.getVersion());
                if(articleModel.getCategoryIds().size() > 0)
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, articleModel.getCategoryIds().get(0));
                startActivity(intent);

            }
        });
    }

    public void updateArticleList(List<ArticleModel> articleList) {
        setCategoryNames(articleList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getActivity() != null) {
            getArticlesList();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
    }

//
//
//    public interface ArticleListener {
//        void onArticlesLoaded();
//
//    }
//
//    public void setArticleListener(ArticleListener articleListener) {
//        this.articleListener = articleListener;
//    }

}
