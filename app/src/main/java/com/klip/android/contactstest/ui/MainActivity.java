package com.klip.android.contactstest.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.klip.android.contactstest.R;
import com.klip.android.contactstest.adapter.ContactAdapter;
import com.klip.android.contactstest.model.Contacts;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;

    private ContactAdapter adapter;
    private ArrayList<Contacts> contactses;
    private RecyclerView recyclerView;
    private Button read_contacts;
    private Button add_data;
    private Button update_data;
    private Button delete_data;
    private Button query_result;
    private Button web_page;
    private TextView query_result_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        read_contacts = (Button) findViewById(R.id.read_contacts);
        read_contacts.setOnClickListener(readContactsListener);
        add_data = (Button) findViewById(R.id.add_data);
        add_data.setOnClickListener(addDataListener);
        query_result = (Button) findViewById(R.id.query_data);
        query_result.setOnClickListener(queryDataListener);
        update_data = (Button) findViewById(R.id.update_data);
        update_data.setOnClickListener(updateDataListener);
        delete_data = (Button) findViewById(R.id.delete_data);
        delete_data.setOnClickListener(deleteDataListener);
        web_page = (Button) findViewById(R.id.web_page);
        web_page.setOnClickListener(webPageListener);
        query_result_text = (TextView) findViewById(R.id.query_result_text);
        contactses = new ArrayList<>();
        adapter = new ContactAdapter(contactses);
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener readContactsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
            } else {
                readContacts();
            }
        }
    };

    private void readContacts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contact_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String contact_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contacts contacts = new Contacts();
                    contacts.setName(contact_name);
                    contacts.setNumber(contact_number);
                    contactses.add(contacts);
                }
                cursor.close();
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.need_update_system), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.denied_permission), Toast.LENGTH_LONG).show();
                }
        }
    }

    private View.OnClickListener addDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("content://com.klip.android.broadcastbestpractice.provider/books");
            ContentValues values = new ContentValues();
            values.put("name", "A Clash of Kings");
            values.put("author", "George Martin");
            values.put("pages", "1040");
            values.put("price", "22.85");
            Uri newUri = getContentResolver().insert(uri, values);
            String newId = newUri.getPathSegments().get(1);
        }
    };
    private View.OnClickListener updateDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("content://com.klip.android.broadcastbestpractice.provider/books");
            ContentValues values = new ContentValues();
            values.put("author", "Cheng Lin Park");
            values.put("price", "100.00");
            getContentResolver().update(uri, values, "name = ?", new String[]{"The Lost Symbol"});
        }
    };
    private View.OnClickListener deleteDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("content://com.klip.android.broadcastbestpractice.provider/books");
            getContentResolver().delete(uri, null, null);
        }
    };
    private View.OnClickListener webPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener queryDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("content://com.klip.android.broadcastbestpractice.provider/books");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                StringBuilder content = new StringBuilder();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String pages = cursor.getString(cursor.getColumnIndex("pages"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    content.append(id);
                    content.append(author);
                    content.append(price);
                    content.append(pages);
                    content.append(name);
                    content.append("\n");
                }
                query_result_text.setText(content.toString());
                cursor.close();
            }
        }
    };
}
