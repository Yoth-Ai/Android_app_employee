package com.mardikh.crudusermobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mardikh.crudusermobileapp.R;
import com.mardikh.crudusermobileapp.models.Role;

import java.util.List;

public class CustomRoleAdapter extends BaseAdapter {
    private final Context context;
    private final List<Role> roleList;
    public CustomRoleAdapter(Context context, List<Role> roleList){
        this.context = context;
        this.roleList = roleList;
    }

    @Override
    public int getCount() {
        return roleList.size();
    }

    @Override
    public Object getItem(int position) {
        return roleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return roleList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.role_spinner_card_layout,null, false);
        Role role = roleList.get(position);
        TextView title = convertView.findViewById(R.id.tvTitle);
        if (role.getName()!=null){
            title.setText(role.getName());
        }
        return convertView;
    }
}
