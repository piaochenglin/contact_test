package com.klip.android.contactstest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.klip.android.contactstest.R;
import com.klip.android.contactstest.model.Contacts;

import java.util.ArrayList;


/**
 * Created by park
 * on 2017/11/22.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    public View view;

    public ContactAdapter(ArrayList<Contacts> contacts) {
        this.contactsArrayList = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contacts contacts = contactsArrayList.get(position);
        holder.nameText.setText(contacts.getName());
        holder.numberText.setText(contacts.getNumber());
    }

    @Override
    public int getItemCount() {
        return contactsArrayList.size();
    }
}
