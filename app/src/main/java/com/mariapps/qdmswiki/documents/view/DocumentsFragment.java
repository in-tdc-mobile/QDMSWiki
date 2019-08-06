package com.mariapps.qdmswiki.documents.view;

import android.content.Intent;
import android.nfc.Tag;
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
import com.mariapps.qdmswiki.documents.model.DepartmentModel;
import com.mariapps.qdmswiki.documents.model.DocumentsModel;
import com.mariapps.qdmswiki.documents.model.TagModel;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class DocumentsFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.searchET)
    CustomEditText searchET;

    private FragmentManager fragmentManager;
    private DocumentsAdapter documentsAdapter;
    private ArrayList<TagModel> department1List = new ArrayList<>();
    private ArrayList<TagModel> department2List = new ArrayList<>();
    private ArrayList<DocumentsModel> documentsList = new ArrayList<>();

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

        setData();
        return view;
    }

    private void setData() {
        department1List.add(new TagModel(0, 1,"Safety", "Y"));
        department1List.add(new TagModel(1,1,"LPSQ Department", "Y"));

        documentsList.add(new DocumentsModel(0,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(1,"Passenger ship safety management","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(2,"SMC DE HR Manual","General Data Protection Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(3,"Incident investigation Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(4,"Safety management Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));

        department2List.add(new TagModel(0,1,"Safety", "Y"));
        department2List.add(new TagModel(1,1,"LPSQ Department", "Y"));
        department2List.add(new TagModel(2,1,"Safety", "Y"));
        department2List.add(new TagModel(3,1,"LPSQ Department", "Y"));

        documentsList.add(new DocumentsModel(5,"Passenger ship safety management","GDPR Manual","25/02/2019","11:08:35 AM",department2List));
        documentsList.add(new DocumentsModel(6,"Safety management Manual","GDPR Manual","25/02/2019","11:08:35 AM",department2List));

        documentsAdapter = new DocumentsAdapter(getActivity(),documentsList,"DOCUMENTS");
        rvDocuments.setAdapter(documentsAdapter);

        documentsAdapter.setRowClickListener(new DocumentsAdapter.RowClickListener() {
            @Override
            public void onItemClicked(DocumentsModel documentsModel) {
                Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,documentsModel.getDocumentName());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,documentsModel.getId());
                startActivity(intent);

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
