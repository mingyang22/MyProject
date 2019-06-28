package com.yangming.myproject.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import com.yangming.myproject.MainActivity;
import com.yangming.myproject.R;
import com.yangming.myproject.view.ViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通知
 */
public class NotificationActivity extends AppCompatActivity {

    private String notifyChannelId = "test";
    private String notifyChannelName = "通知";

    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        initNotification();
    }

    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 适配8.0
            NotificationChannel channel = new NotificationChannel(notifyChannelId, notifyChannelName, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notifyChannelId);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.small_money);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.small_money));
        builder.setAutoCancel(true);
        builder.setContentTitle("普通通知");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(1, builder.build());
    }

    private void notification2() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notifyChannelId);
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_msg, "自定义通知");
        remoteViews.setTextViewText(R.id.tv_open, "打开ViewActivity");
        remoteViews.setImageViewResource(R.id.iv, R.mipmap.small_charge);

        PendingIntent openActivity = PendingIntent.getActivity(this, 0, new Intent(this, ViewActivity.class), 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_open, openActivity);
        builder.setContent(remoteViews);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.small_charge);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(2, builder.build());
    }

    @OnClick({R.id.btn_notification, R.id.btn_notification2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_notification:
                notification();
                break;
            case R.id.btn_notification2:
                notification2();
                break;
            default:
                break;
        }
    }
}
