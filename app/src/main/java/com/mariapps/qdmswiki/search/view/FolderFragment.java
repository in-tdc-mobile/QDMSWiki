package com.mariapps.qdmswiki.search.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.search.adapter.SearchFilterAdapter;
import com.mariapps.qdmswiki.search.adapter.SearchResultAdapter;
import com.mariapps.qdmswiki.search.model.FilterBooleanItem;
import com.mariapps.qdmswiki.search.model.SearchFilterModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;

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
import io.reactivex.schedulers.Schedulers;

public class FolderFragment extends BaseFragment{

    @BindView(R.id.linLayout)
    LinearLayout linLayout;
    @BindView(R.id.searchResultRV)
    CustomRecyclerView searchResultRV;
    @BindView(R.id.searchFilterRV)
    CustomRecyclerView searchFilterRV;
    @BindView(R.id.searchET)
    CustomEditText searchET;

    private HomeDatabase homeDatabase;
    private ArrayList<SearchFilterModel> searchType = new ArrayList<>();
    private List<SearchModel> searchList = new ArrayList<>();
    private SearchFilterAdapter searchFilterAdapter;
    private SearchResultAdapter searchResultAdapter;
    private boolean isFolderSelected = false;
    private boolean isDocumentSelected = true;
    private boolean isArticleSelected = false;
    private boolean isFormSelected = false;
    private String folderName;
    private String id;
    Gson gson = new Gson();

    @Override
    protected void setUpPresenter() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder_structure, container, false);
        ButterKnife.bind(this, view);

        try{
            Bundle args = getArguments();
            id= args.getString(AppConfig.BUNDLE_FOLDER_ID, "");
        }
        catch (Exception e){}

        homeDatabase = HomeDatabase.getInstance(getActivity());
        searchFilterRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        searchFilterRV.setHasFixedSize(true);

        searchResultRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        searchResultRV.setHasFixedSize(true);


        getSearchList();
        setSearchTypeData();

        return view;
    }

    private void setSearchTypeData() {
        searchType = new ArrayList<>();
        searchType.add(new SearchFilterModel(1, "Folder", false));
        searchType.add(new SearchFilterModel(2, "Document", true));
        searchType.add(new SearchFilterModel(3, "Article", false));
        searchType.add(new SearchFilterModel(4, "Forms", false));
        searchFilterAdapter = new SearchFilterAdapter(getActivity(), searchType);
        searchFilterRV.setAdapter(searchFilterAdapter);


        searchFilterAdapter.setItemClickListener(new SearchFilterAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchFilterModel item) {
                if (item.getSearchType().equals("Folder")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isFolderSelected = false;
                        getSearchList();
                    } else {
                        item.setSelected(true);
                        isFolderSelected = true;
                        getSearchList();
                    }

                } else if (item.getSearchType().equals("Document")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isDocumentSelected = false;
                        getSearchList();
                    } else {
                        item.setSelected(true);
                        isDocumentSelected = true;
                        getSearchList();
                    }

                } else if (item.getSearchType().equals("Article")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isArticleSelected = false;
                        getSearchList();
                    } else {
                        item.setSelected(true);
                        isArticleSelected = true;
                        getSearchList();
                    }
                } else if (item.getSearchType().equals("Forms")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isFormSelected = false;
                        getSearchList();
                    } else {
                        item.setSelected(true);
                        isFormSelected = true;
                        getSearchList();
                    }

                }
                searchFilterAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setSearchList() {

        searchResultAdapter = new SearchResultAdapter(getActivity(), searchList,"Folder");
        searchResultRV.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();

        searchResultAdapter.setItemClickListener(new SearchResultAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchModel item) {
                folderName = item.getName();
                id = item.getId();
                if(item.getType().equals("Folder")) {
                    FolderFragment folderFragment = new FolderFragment();
                    Bundle args = new Bundle();
                    args.putString(AppConfig.BUNDLE_FOLDER_ID, id);
                    args.putString(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                    folderFragment.setArguments(args);
                    ((FolderStructureActivity) getActivity()).replaceFragments(folderFragment,id,folderName);
                    ((FolderStructureActivity)getActivity()).initBreadCrumb(folderName,id);
                }
                else{
                    DocumentViewFragment documentViewFragment = new DocumentViewFragment();
                    Bundle args = new Bundle();
                    args.putString(AppConfig.BUNDLE_FOLDER_NAME, folderName);
                    documentViewFragment.setArguments(args);
                    ((FolderStructureActivity) getActivity()).replaceFragments(documentViewFragment,id,folderName);
                }

            }
        });
    }



    @OnTextChanged(R.id.searchET)
    void onSearch() {
        if (searchResultAdapter != null) {
            searchResultAdapter.getFilter().filter(gson.toJson(new FilterBooleanItem(isFolderSelected, isDocumentSelected, isArticleSelected, isFormSelected, searchET.getText().toString())));
        }
    }

    public void getSearchList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if(isDocumentSelected && isArticleSelected && isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsArticlesAndFoldersInsideFolder(id);
                else if(isDocumentSelected && isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsAndArticlesInsideFolder();
                else if(isDocumentSelected && isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsAndFoldersInsideFolder(id);
                else if(isArticleSelected && isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllArticlesAndFoldersInsideFolder(id);
                else if(isDocumentSelected && !isArticleSelected && !isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsInsideFolder();
                else if(!isDocumentSelected && isArticleSelected && !isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllArticlesInsideFolder();
                else if(isFolderSelected && !isDocumentSelected && !isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllCategoriesInsideFolder(id);


            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                setSearchList();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
