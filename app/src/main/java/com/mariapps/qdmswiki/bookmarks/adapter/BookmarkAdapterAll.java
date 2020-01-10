package com.mariapps.qdmswiki.bookmarks.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.view.BookMarkActivityAll;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.adapter.BreadCrumbAdapter;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.view.FolderFragment;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class BookmarkAdapterAll extends CustomRecyclerView.Adapter<BookmarkAdapterAll.BookmarkVH>  {

    private Context mContext;
    private BookmarkAdapterAll.RowClickListener rowClickListener;
    private BookmarkAdapterAll.DeleteClickListener deleteClickListener;
    private List<BookmarkEntryModel> bookmarkEntryList;
    //private BreadCrumbBookmarkAdapter breadCrumbAdapter;
    public static final String BOOKMARKID = "BOOKMARK_ID";
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();
    List<BreadCrumbItem> allParents = new ArrayList<>();
    HomeDatabase homeDatabase;
    CategoryModel categoryModel;
    DocumentModel documentModel;
    List<List<BreadCrumbItem>> breadCrumbItemsall;


    public BookmarkAdapterAll(Context context, List<BookmarkEntryModel> bookmarkEntries, List<List<BreadCrumbItem>> breadCrumbItemsall) {
        mContext = context;
        bookmarkEntryList = bookmarkEntries;
        homeDatabase = HomeDatabase.getInstance(context);
        this.breadCrumbItemsall = breadCrumbItemsall;
    }

    @NonNull
    @Override
    public BookmarkAdapterAll.BookmarkVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list_row_withrv, viewGroup, false);
        return new BookmarkAdapterAll.BookmarkVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapterAll.BookmarkVH holder, int i) {
        BookmarkEntryModel bookmarkEntryModel = bookmarkEntryList.get(i);
        holder.bookmarkTV.setText(bookmarkEntryModel.getBookmarkTitle().trim());
        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(bookmarkEntryList.get(holder.getAdapterPosition()));
            }
        });
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickListener.onDeleteClicked(bookmarkEntryList.get(holder.getAdapterPosition()), i);
            }
        });

        try {

            initrv(holder.breadCrumbRV,breadCrumbItemsall.get(holder.getAdapterPosition()));
          /*  Log.e("breadCrumbItemsall",breadCrumbItemsall.size()+"");

            if(breadCrumbItemsall.size()>0)
                Log.e("breadCrumbItemssmall",breadCrumbItemsall.size()+"");
            initrv(holder.breadCrumbRV,breadCrumbItemsall.get(holder.getAdapterPosition()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
       // getdocdet(bookmarkEntryModel.getDocumentId(), holder.breadCrumbRV);
    }


    @Override
    public int getItemCount() {
        return bookmarkEntryList != null ? bookmarkEntryList.size() : 0;
    }




    class BookmarkVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.bookmarkTV)
        CustomTextView bookmarkTV;
        @BindView(R.id.deleteIV)
        AppCompatImageView deleteIV;
        @BindView(R.id.breadCrumbRV)
        RecyclerView breadCrumbRV;

        public BookmarkVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    public void setRowClickListener(BookmarkAdapterAll.RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(BookmarkEntryModel bookmarkEntryModel);
    }

    public void setDeleteClickListener(BookmarkAdapterAll.DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public interface DeleteClickListener {
        void onDeleteClicked(BookmarkEntryModel bookmarkEntryModel, int position);
    }

    public void getBreadCrumbDetails(String categoryId) {
        // getCategoryDetailsOfSelectedDocument(categoryId,);
    }


    public void getCategoryDetailsOfSelectedDocument(String folderId, RecyclerView rv) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                categoryModel = homeDatabase.homeDao().getCategoryDetailsOfSelectedDocument(folderId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                breadCrumbItems.clear();
                if (categoryModel == null) {
                    for (int i = allParents.size() - 1; i >= 0; i--) {
                        breadCrumbItems.add(allParents.get(i));
                    }
                    initrv(rv,breadCrumbItems);
                    allParents.clear();
                } else {
                    allParents.add(new BreadCrumbItem(categoryModel.getName(), categoryModel.getId()));
                    getCategoryDetailsOfSelectedDocument(categoryModel.getParent(), rv);
                }
            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void initrv(RecyclerView breadCrumbRV,List<BreadCrumbItem> breadCrumbItemslist) {
        breadCrumbRV.setHasFixedSize(true);
        breadCrumbRV.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        BreadCrumbBookmarkAdapter  breadCrumbAdapter = new BreadCrumbBookmarkAdapter(breadCrumbItemslist, mContext);
        breadCrumbRV.setAdapter(breadCrumbAdapter);
        breadCrumbAdapter.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void getdocdet(String docid, RecyclerView rv) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModel = HomeDatabase.getInstance(mContext).homeDao().getDocumentDetails(docid);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                if (documentModel != null) {
                    Log.e("documentModel", documentModel.categoryId + "  " + documentModel.documentNumber);
                    getCategoryDetailsOfSelectedDocument(documentModel.categoryId, rv);
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.e("getBookMarkEntryList", e.getLocalizedMessage());

            }
        });
    }

}

