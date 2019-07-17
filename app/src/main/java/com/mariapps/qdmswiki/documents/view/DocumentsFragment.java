package com.mariapps.qdmswiki.documents.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
import com.mariapps.qdmswiki.documents.model.DepartmentModel;
import com.mariapps.qdmswiki.documents.model.DocumentsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentsFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    RecyclerView rvDocuments;

    private FragmentManager fragmentManager;
    private DocumentsAdapter documentsAdapter;
    private ArrayList<DepartmentModel> department1List = new ArrayList<>();
    private ArrayList<DepartmentModel> department2List = new ArrayList<>();
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
        department1List.add(new DepartmentModel(0,"Safety"));
        department1List.add(new DepartmentModel(1,"LPSQ Department"));

        documentsList.add(new DocumentsModel(0,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(1,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(2,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(3,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));
        documentsList.add(new DocumentsModel(4,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department1List));

        department2List.add(new DepartmentModel(0,"Safety"));
        department2List.add(new DepartmentModel(1,"LPSQ Department"));
        department2List.add(new DepartmentModel(2,"hhhhh"));
        department2List.add(new DepartmentModel(3,"rrrrr"));

        documentsList.add(new DocumentsModel(5,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department2List));
        documentsList.add(new DocumentsModel(6,"General Data Protection Manual","GDPR Manual","25/02/2019","11:08:35 AM",department2List));

        documentsAdapter = new DocumentsAdapter(getActivity(),documentsList);
        rvDocuments.setAdapter(documentsAdapter);
    }
}
