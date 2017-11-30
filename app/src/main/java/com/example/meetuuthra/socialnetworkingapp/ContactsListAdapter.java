package com.example.meetuuthra.socialnetworkingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meetuuthra on 11/20/17.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.MyViewHolder>{
    public List<ContactsModel> ContactList;
    public Context mContext;

    public ContactsListAdapter(Context context, ArrayList<ContactsModel> ContactList) {
        this.ContactList = ContactList;
        this.mContext = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contactlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactsListAdapter.MyViewHolder holder, int position) {
        final ContactsModel user = ContactList.get(position);

        holder.textCLName.setText(user.getContactName());
        holder.textCLEmail.setText(user.getContactEmail());
        holder.textCLPhone.setText(user.getContactPhone());

    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView textCLName, textCLEmail, textCLPhone;
        ImageView imageCLView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textCLName = (TextView) itemView.findViewById(R.id.tVName);
            textCLEmail = (TextView) itemView.findViewById(R.id.tVEmail);
            textCLPhone = (TextView) itemView.findViewById(R.id.tVPhone);
            imageCLView = (ImageView) itemView.findViewById(R.id.userImage);


            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

    }
}
