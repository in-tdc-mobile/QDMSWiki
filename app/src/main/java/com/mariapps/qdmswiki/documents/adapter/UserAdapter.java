package com.mariapps.qdmswiki.documents.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.model.UserModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends CustomRecyclerView.Adapter<UserAdapter.UsersVH> {

    private Context mContext;
    private ArrayList<UserModel> userList;

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        mContext = context;
        userList = list;
    }

    @NonNull
    @Override
    public UserAdapter.UsersVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_row, viewGroup, false);
        return new UserAdapter.UsersVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UsersVH holder, int i) {
        holder.tvUser.setText(userList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    static class UsersVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.tvUser)
        CustomTextView tvUser;

        public UsersVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

