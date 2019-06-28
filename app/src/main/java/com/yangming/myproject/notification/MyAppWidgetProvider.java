package com.yangming.myproject.notification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yangming.myproject.R;

/**
 * @author yangming on 2019/4/28
 * 桌面小部件实现类
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String TAG = "MyAppWidgetProvider";

    public static final String CLICK_ACTION = "com.yangming.myproject.notification.CLICK";

    public MyAppWidgetProvider() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG, "onReceive: action = " + intent.getAction());
        // 这里判断是自己的action，做自己的事情，比如小部件被单击了要干什么，这里是做一个动画效果
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcbBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.big_scan);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.iv_home, rotateBitmap(context, srcbBitmap, degree));
                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        intentClick.setClass(context, MyAppWidgetProvider.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
                        remoteViews.setOnClickPendingIntent(R.id.iv_home, pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }

    /**
     * 每次桌面小部件更新时都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e(TAG, "onUpdate: ");

        final int counter = appWidgetIds.length;
        Log.e(TAG, "counter =  " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }

    }

    /**
     * 桌面小部件更新
     */
    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.e(TAG, "appWidgetId =  " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        // "桌面小部件"单击事件发送的Intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        // android 8.0以上大多数隐式广播通过静态注册的方式无法接收
        intentClick.setClass(context, MyAppWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_home, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    private Bitmap rotateBitmap(Context context, Bitmap srcbBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcbBitmap, 0, 0, srcbBitmap.getWidth(), srcbBitmap.getHeight(), matrix, true);
        return tmpBitmap;
    }
}
