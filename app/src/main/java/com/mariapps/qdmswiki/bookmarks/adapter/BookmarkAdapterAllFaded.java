package com.mariapps.qdmswiki.bookmarks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;

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

public class BookmarkAdapterAllFaded extends CustomRecyclerView.Adapter<BookmarkAdapterAllFaded.BookmarkVH>{
    private Context mContext;
    private BookmarkAdapterAllFaded.RowClickListener rowClickListener;
    private BookmarkAdapterAllFaded.RowLongClickListner rowLongClickListener;
    private BookmarkAdapterAllFaded.DeleteClickListener deleteClickListener;
    private List<BookmarkEntryModel> bookmarkEntryList;
    //private BreadCrumbBookmarkAdapter breadCrumbAdapter;
    public static final String BOOKMARKID = "BOOKMARK_ID";
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();
    List<BreadCrumbItem> allParents = new ArrayList<>();
    List<List<BreadCrumbItem>> breadCrumbItemsall;


    public BookmarkAdapterAllFaded(Context context, List<BookmarkEntryModel> bookmarkEntries, List<List<BreadCrumbItem>> breadCrumbItemsall) {
        mContext = context;
        bookmarkEntryList = bookmarkEntries;
        this.breadCrumbItemsall = breadCrumbItemsall;
    }

    @NonNull
    @Override
    public BookmarkAdapterAllFaded.BookmarkVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list_row_withtv, viewGroup, false);
        return new BookmarkAdapterAllFaded.BookmarkVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapterAllFaded.BookmarkVH holder, int i) {
        BookmarkEntryModel bookmarkEntryModel = bookmarkEntryList.get(i);
        holder.bookmarkTV.setText(bookmarkEntryModel.getBookmarkTitle().trim());
        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(bookmarkEntryList.get(holder.getAdapterPosition()));
            }
        });
        holder.rowLL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                rowLongClickListener.onItemClicked(bookmarkEntryList.get(holder.getAdapterPosition()),holder.getAdapterPosition());
                return true;
            }
        });
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickListener.onDeleteClicked(bookmarkEntryList.get(holder.getAdapterPosition()), i);
            }
        });

        try {
            List<BreadCrumbItem> itemList  = breadCrumbItemsall.get(holder.getAdapterPosition());
            String crumbtxt="";
            for (int i1 = 0; i1 < itemList.size(); i1++) {
                if(itemList.get(i1)!=null)
                crumbtxt= crumbtxt.trim()+"/"+itemList.get(i1).getHeading().trim();
            }
            holder.breadCrumbTV.setText(crumbtxt.trim());
            holder.breadCrumbTV.setSelected(true);
            holder.breadCrumbTV.setMovementMethod(new ScrollingMovementMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        @BindView(R.id.breadCrumbTV)
        CustomTextView breadCrumbTV;

        public BookmarkVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    public void setRowClickListener(BookmarkAdapterAllFaded.RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public void setRowLongClickListener(BookmarkAdapterAllFaded.RowLongClickListner rowClickListener) {
        this.rowLongClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(BookmarkEntryModel bookmarkEntryModel);
    }

    public interface RowLongClickListner {
        void onItemClicked(BookmarkEntryModel bookmarkEntryModel, int position);
    }

    public void setDeleteClickListener(BookmarkAdapterAllFaded.DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public interface DeleteClickListener {
        void onDeleteClicked(BookmarkEntryModel bookmarkEntryModel, int position);
    }

    public void getBreadCrumbDetails(String categoryId) {
        // getCategoryDetailsOfSelectedDocument(categoryId,);
    }
}

