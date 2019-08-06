package com.yangming.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yangming.myproject.alipayhome.AlipayHomeActivity;
import com.yangming.myproject.chat.ChatActivity;
import com.yangming.myproject.design.LoginActivity;
import com.yangming.myproject.imageloader.PhotoWallActivity;
import com.yangming.myproject.thread.AsyncTaskActivity;
import com.yangming.myproject.view.ViewActivity;
import com.yangming.myproject.databinding.BindingActivity;
import com.yangming.myproject.imageselector.PictureGridviewActivity;
import com.yangming.myproject.ipc.MessengerActivity;
import com.yangming.myproject.ipc.aidl.BookManagerActivity;
import com.yangming.myproject.linkman.LinkManActivity;
import com.yangming.myproject.notification.NotificationActivity;
import com.yangming.myproject.snaphelper.SnapHelperActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yang
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_alipay, R.id.btn_image, R.id.btn_chat, R.id.btn_snapHelper, R.id.btn_view,
            R.id.btn_link, R.id.btn_notification, R.id.btn_data_binding, R.id.btn_messenger, R.id.btn_aidl,
            R.id.btn_thread, R.id.btn_photo_wall, R.id.btn_design})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alipay:
                startActivity(new Intent(this, AlipayHomeActivity.class));
                break;
            case R.id.btn_image:
                startActivity(new Intent(this, PictureGridviewActivity.class));
                break;
            case R.id.btn_chat:
                startActivity(new Intent(this, ChatActivity.class));
                break;
            case R.id.btn_snapHelper:
                startActivity(new Intent(this, SnapHelperActivity.class));
                break;
            case R.id.btn_view:
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case R.id.btn_link:
                startActivity(new Intent(this, LinkManActivity.class));
                break;
            case R.id.btn_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.btn_data_binding:
                startActivity(new Intent(this, BindingActivity.class));
                break;
            case R.id.btn_messenger:
                startActivity(new Intent(this, MessengerActivity.class));
                break;
            case R.id.btn_aidl:
                startActivity(new Intent(this, BookManagerActivity.class));
                break;
            case R.id.btn_thread:
                startActivity(new Intent(this, AsyncTaskActivity.class));
                break;
            case R.id.btn_photo_wall:
                startActivity(new Intent(this, PhotoWallActivity.class));
                break;
            case R.id.btn_design:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }


}
