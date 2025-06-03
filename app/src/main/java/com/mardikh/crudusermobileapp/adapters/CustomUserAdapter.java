package com.mardikh.crudusermobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mardikh.crudusermobileapp.R;
import com.mardikh.crudusermobileapp.models.User;

import java.util.List;

public class CustomUserAdapter extends BaseAdapter {
    private final Context context;
    private final List<User> userList;
    private OnClickListener onClickListener;

    public CustomUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public CustomUserAdapter(Context context, List<User> userList, OnClickListener onClickListener) {
        this.context = context;
        this.userList = userList;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view== null){
           view = LayoutInflater.from(context).inflate(R.layout.user_card_list_layout, null, false);
           User user = userList.get(position);
           ImageView edit = view.findViewById(R.id.ivEdit);
           TextView username = view.findViewById(R.id.tvUsername);
           TextView gender = view.findViewById(R.id.tvGender);
           TextView email = view.findViewById(R.id.tvEmail);
           TextView role = view.findViewById(R.id.tvUserRole);
           if(!user.getName().isEmpty()){
               username.setText(user.getName());
           }
            if(!user.getEmail().isEmpty()){
                email.setText(user.getEmail());
            }
            if(!user.getGender().isEmpty()){
                gender.setText(user.getGender());
            }
            if(user.getRole() != null){
                role.setText(user.getRole().getName());
            }
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onEdit(v, user);
                }
            });

        }
        return view;
    }
    public  interface OnClickListener{
        void onEdit(View view, User user);
    }
}
