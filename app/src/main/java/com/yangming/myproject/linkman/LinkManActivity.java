package com.yangming.myproject.linkman;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 联系人
 */
public class LinkManActivity extends AppCompatActivity {

    public String[] PERMISSION_SDCARD = new String[]{
            Manifest.permission.READ_CONTACTS};

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_man);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_choose)
    public void onViewClicked() {
        ActivityCompat.requestPermissions(this, PERMISSION_SDCARD, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                tvName.setText(username);
                tvPhone.setText(usernumber);
            }
        }
    }
}
