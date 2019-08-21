package com.mariapps.qdmswiki.documents.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
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
import io.reactivex.schedulers.Schedulers;

public class DocumentsFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.searchET)
    CustomEditText searchET;

    private FragmentManager fragmentManager;
    private DocumentsAdapter documentsAdapter;
    private List<DocumentModel> documentsList = new ArrayList<>();
    private HomeDatabase homeDatabase;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        ButterKnife.bind(this, view);
        fragmentManager = getFragmentManager();

        rvDocuments.setHasFixedSize(true);
        rvDocuments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDocuments.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        homeDatabase = HomeDatabase.getInstance(getActivity());

        getDocumentList();
        return view;
    }

    private void setData() {
        documentsAdapter = new DocumentsAdapter(getActivity(),documentsList,"DOCUMENTS");
        rvDocuments.setAdapter(documentsAdapter);

        documentsAdapter.setRowClickListener(new DocumentsAdapter.RowClickListener() {
            @Override
            public void onItemClicked(DocumentModel documentModel) {
                Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,documentModel.getDocumentName());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,documentModel.getId());
                startActivity(intent);

            }
        });
    }

    public void getDocumentList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentsList = homeDatabase.homeDao().getDocuments();
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

            }
        });
    }

    @OnTextChanged(R.id.searchET)
    void onTextChanged() {
        if (documentsAdapter != null) {
            documentsAdapter.getFilter().filter(searchET.getText().toString());
        }

    }
}
