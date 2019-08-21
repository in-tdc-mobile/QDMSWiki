package com.mariapps.qdmswiki.bookmarks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkAdapter extends CustomRecyclerView.Adapter<BookmarkAdapter.BookmarkVH> {

    private Context mContext;
    private BookmarkAdapter.RowClickListener rowClickListener;
    private ArrayList<BookmarkModel> bookmarkList;

    public BookmarkAdapter(Context context, ArrayList<BookmarkModel> list) {
        mContext = context;
        bookmarkList = list;
    }

    @NonNull
    @Override
    public BookmarkAdapter.BookmarkVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list_row, viewGroup, false);
        return new BookmarkAdapter.BookmarkVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapter.BookmarkVH holder, int i) {
        //holder.bookmarkTV.setText(bookmarkList.get(i).getBookMark());
        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(bookmarkList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmarkList != null ? bookmarkList.size() : 0;
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
        void onItemClicked(BookmarkModel bookmarkModel) ;
    }
}

