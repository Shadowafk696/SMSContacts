package com.school.broadcast.broadcast;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
public class MainActivity extends Activity{
    private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity activity;
    private ArrayList<String> smsList = new ArrayList<String>();
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS =1;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS =1;
    public static MainActivity instance() {
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        mListView = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(MyItemClickListener);
        readSMS();
    }
    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }
    public void readSMS() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse(INBOX_URI), null, null, null, null);
        int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        adapter.clear();
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
            String.format(getResources().getString(R.string.sms_message), sender, message);
            String formattedText = message;
            adapter.add(Html.fromHtml(formattedText).toString());
        } while (smsInboxCursor.moveToNext());
    }
    public void updateList(final String newSms) {
        adapter.insert(newSms, 0);
        adapter.notifyDataSetChanged();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com")));
    }
    private OnItemClickListener MyItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            try {
                Toast.makeText(getApplicationContext(), adapter.getItem(pos), Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", adapter.getItem(pos));
                                String message = adapter.getItem(pos);
                                String[] contentSms = message.split(":");
                                String typeSms = contentSms[0];
                                Uri uri = Uri.parse(contentSms[1]);
                                Log.d("DEBUG", typeSms);
                                Log.d("DEBUG", String.valueOf(uri));
                              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=yJL5SE1i0u4")));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}