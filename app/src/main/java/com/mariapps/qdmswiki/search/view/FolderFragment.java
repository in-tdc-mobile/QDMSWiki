package com.mariapps.qdmswiki.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.search.adapter.SearchFilterAdapter;
import com.mariapps.qdmswiki.search.adapter.SearchResultAdapter;
import com.mariapps.qdmswiki.search.model.FilterBooleanItem;
import com.mariapps.qdmswiki.search.model.SearchFilterModel;
import com.mariapps.qdmswiki.search.model.SearchModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class FolderFragment extends BaseFragment {

    @BindView(R.id.linLayout)
    LinearLayout linLayout;
    @BindView(R.id.searchResultRV)
    CustomRecyclerView searchResultRV;
    @BindView(R.id.searchFilterRV)
    CustomRecyclerView searchFilterRV;
    @BindView(R.id.searchET)
    CustomEditText searchET;

    private ArrayList<SearchFilterModel> searchType = new ArrayList<>();
    private ArrayList<SearchModel> searchList = new ArrayList<>();
    private SearchFilterAdapter searchFilterAdapter;
    private SearchResultAdapter searchResultAdapter;
    private boolean isFolderSelected = false;
    private boolean isDocumentSelected = false;
    private boolean isArticleSelected = false;
    private boolean isFormSelected = false;
    private String folderName;
    private Integer id;
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
            id= args.getInt(AppConfig.BUNDLE_FOLDER_ID, 0);
        }
        catch (Exception e){}

        searchFilterRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        searchFilterRV.setHasFixedSize(true);

        searchResultRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        searchResultRV.setHasFixedSize(true);

        setSearchList();

        return view;
    }

    private void setSearchTypeData() {
        searchType = new ArrayList<>();
        searchType.add(new SearchFilterModel(1, "Folder", false));
        searchType.add(new SearchFilterModel(2, "Document", false));
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
                        onSearch();
                    } else {
                        item.setSelected(true);
                        isFolderSelected = true;
                        onSearch();
                    }

                } else if (item.getSearchType().equals("Document")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isDocumentSelected = false;
                        onSearch();
                    } else {
                        item.setSelected(true);
                        isDocumentSelected = true;
                        onSearch();
                    }

                } else if (item.getSearchType().equals("Article")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isArticleSelected = false;
                        onSearch();
                    } else {
                        item.setSelected(true);
                        isArticleSelected = true;
                        onSearch();
                    }
                } else if (item.getSearchType().equals("Forms")) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        isFormSelected = false;
                        onSearch();
                    } else {
                        item.setSelected(true);
                        isFormSelected = true;
                        onSearch();
                    }

                }
                searchFilterAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setSearchList() {

        setSearchTypeData();
        searchList = new ArrayList<>();

        searchList.add(new SearchModel(1, -1,"Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(2, 1,"Document", "Information Technology", "ECDIS Manual"));
        searchList.add(new SearchModel(3, -1,"Folder", "Ethical Ship Operations Policy", "ISO Ethical Ship Operations Policy"));
        searchList.add(new SearchModel(4, 3,"Forms", "Safety Management Manual", "Safety Management Manual Appendix"));
        searchList.add(new SearchModel(5, 1,"Document", "LPG Carrier Manual", "LPG Carrier Manual"));
        searchList.add(new SearchModel(6, -1,"Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(7, 1,"Document", "Information Technology", "ECDIS Manual"));
        searchList.add(new SearchModel(8, 6,"Document", "Safety Management Manual", "Safety Management Manual Appendix"));
        searchList.add(new SearchModel(9, 3,"Document", "LPG Carrier Manual", "LPG Carrier Manual"));
        searchList.add(new SearchModel(10, 1,"Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(11, 10,"Document", "Information Technology", "ECDIS Manual"));

        ArrayList<SearchModel> selectedList = new ArrayList<>();

        for(int i=0;i<searchList.size();i++){
            if(searchList.get(i).getFolderId() == id){
                selectedList.add(searchList.get(i));
            }
        }


        searchResultAdapter = new SearchResultAdapter(getActivity(), selectedList);
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
                    args.putInt(AppConfig.BUNDLE_FOLDER_ID, id);
                    folderFragment.setArguments(args);
                    ((FolderStructureActivity) getActivity()).replaceFragments(folderFragment);
                }
                else{
                    ((FolderStructureActivity) getActivity()).replaceFragments(new DocumentViewFragment());
                }
                ((FolderStructureActivity)getActivity()).initBreadCrumb(folderName,id);
            }
        });
    }



    @OnTextChanged(R.id.searchET)
    void onSearch() {
        if (searchResultAdapter != null) {
            searchResultAdapter.getFilter().filter(gson.toJson(new FilterBooleanItem(isFolderSelected, isDocumentSelected, isArticleSelected, isFormSelected, searchET.getText().toString())));
        }
    }
}
