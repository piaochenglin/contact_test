package com.klip.android.contactstest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.klip.android.contactstest.R;

/**
 * Created by park
 * on 2017/11/22.
 */

public class ContactViewHolder extends RecyclerView.ViewHolder {
    public TextView nameText;
    public TextView numberText;

    public ContactViewHolder(View itemView) {

        super(itemView);
        nameText = (TextView) itemView.findViewById(R.id.contact_name);
        numberText = (TextView) itemView.findViewById(R.id.contact_number);
    }
}
