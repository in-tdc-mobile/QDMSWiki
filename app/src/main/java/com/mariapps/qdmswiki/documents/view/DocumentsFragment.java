package com.mariapps.qdmswiki.documents.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
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
import io.reactivex.schedulers.Schedulers;

public class DocumentsFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;
    @BindView(R.id.loading_spinner)
    ProgressBar loadingSpinner;

    private FragmentManager fragmentManager;
    private DocumentsAdapter documentsAdapter;
    private List<DocumentModel> documentsList = new ArrayList<>();
    private HomeDatabase homeDatabase;
    private SessionManager sessionManager;
//    DocumentListener documentListener;

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
        sessionManager = new SessionManager(getActivity());

        getDocumentList();
        Log.e("DocumentsFragment",documentsList.size()+"");
        return view;
    }

    private void setData(List<DocumentModel> documentsList) {
        loadingSpinner.setVisibility(View.GONE);
        System.out.println("document list size " + documentsList.size());
        if (documentsList != null && documentsList.size() > 0) {
            noDataRL.setVisibility(View.GONE);
            documentsAdapter = new DocumentsAdapter(getActivity(), documentsList, "DOCUMENTS");
            rvDocuments.setAdapter(documentsAdapter);
            documentsAdapter.setRowClickListener(new DocumentsAdapter.RowClickListener() {
                @Override
                public void onItemClicked(DocumentModel documentModel) {
                    Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                    intent.putExtra(AppConfig.BUNDLE_PAGE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_TYPE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getDocumentName());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                    intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCategoryId());
                    intent.putExtra(AppConfig.BUNDLE_VERSION, documentModel.getVersion());
                    startActivity(intent);
                }
            });
        } else {
            rvDocuments.setAdapter(null);
            noDataRL.setVisibility(View.VISIBLE);
        }
    }

    public void getDocumentList() {
        loadingSpinner.setVisibility(View.VISIBLE);
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentsList = homeDatabase.homeDao().getDocuments();
                /*if(sessionManager.getKeyIsSeafarerLogin().equals("True")){
                    List<DocumentModel> seafarerdoclist = homeDatabase.homeDao().getDocumentsForSeafarer();
                    for(int i =0; i<seafarerdoclist.size();i++){
                        if((seafarerdoclist.get(i).getVesselIds() != null &&
                                seafarerdoclist.get(i).getVesselIds().isEmpty()) ||
                                (seafarerdoclist.get(i).getPassengersVesselIds()!= null) &&
                                        seafarerdoclist.get(i).getPassengersVesselIds().isEmpty()){
                            documentsList.add(seafarerdoclist.get(i));
                        }
                    }

                }
                else {
                    documentsList = homeDatabase.homeDao().getDocuments();
                }*/

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.e("sizeofdocis  ",documentsList.size()+"");
                setData(documentsList);
            }


            @Override
            public void onError(Throwable e) {
                loadingSpinner.setVisibility(View.GONE);
                Log.e("doclistThrowable",e.getLocalizedMessage());
            }
        });
    }

    @OnTextChanged(R.id.searchET)
    void onTextChanged() {
        if (documentsAdapter != null) {
            documentsAdapter.getFilter().filter(searchET.getText().toString());
        }

    }


    public void updateDocumentList(List<DocumentModel> documentsList) {
        System.out.println("document2");
        setData(documentsList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getActivity() != null) {
                getDocumentList();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        if (!getUserVisibleHint()) {
            return;
        }
    }

//
//    public interface DocumentListener {
//        void onDocumentsLoaded();
//
//    }
//
//    public void setDocumentListener(DocumentListener documentListener) {
//        this.documentListener = documentListener;
//    }


}
