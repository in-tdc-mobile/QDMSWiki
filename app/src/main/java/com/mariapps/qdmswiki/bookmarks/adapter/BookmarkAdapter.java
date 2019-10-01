package com.mariapps.qdmswiki.bookmarks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkAdapter extends CustomRecyclerView.Adapter<BookmarkAdapter.BookmarkVH> {

    private Context mContext;
    private BookmarkAdapter.RowClickListener rowClickListener;
    private BookmarkAdapter.DeleteClickListener deleteClickListener;
    private List<BookmarkEntryModel> bookmarkEntryList;

    public BookmarkAdapter(Context context, List<BookmarkEntryModel> bookmarkEntries) {
        mContext = context;
        bookmarkEntryList = bookmarkEntries;
    }

    @NonNull
    @Override
    public BookmarkAdapter.BookmarkVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list_row, viewGroup, false);
        return new BookmarkAdapter.BookmarkVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapter.BookmarkVH holder, int i) {

        BookmarkEntryModel bookmarkEntryModel = bookmarkEntryList.get(i);
        holder.bookmarkTV.setText(bookmarkEntryModel.getBookmarkTitle());

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(bookmarkEntryList.get(holder.getAdapterPosition()));
            }
        });

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickListener.onDeleteClicked(bookmarkEntryList.get(holder.getAdapterPosition()),i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookmarkEntryList != null ? bookmarkEntryList.size() : 0;
    }

    static class BookmarkVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.bookmarkTV)
        CustomTextView bookmarkTV;
        @BindView(R.id.deleteIV)
        AppCompatImageView deleteIV;

        public BookmarkVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setRowClickListener(BookmarkAdapter.RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(BookmarkEntryModel bookmarkEntryModel) ;
    }

    public void setDeleteClickListener(BookmarkAdapter.DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public interface DeleteClickListener {
        void onDeleteClicked(BookmarkEntryModel bookmarkEntryModel, int position) ;
    }
}

