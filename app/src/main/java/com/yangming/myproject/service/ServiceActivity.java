package com.yangming.myproject.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangming 2019/10/15
 * service 和 activity 的通讯方式
 */
public class ServiceActivity extends AppCompatActivity {
    private Intent intent;
    private Intent intentForeground;
    private ServiceConnection connection;
    private MyService myService;

    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);

        intent = new Intent(this, MyService.class);
        intentForeground = new Intent(this, ForegroundService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
                myService.registerCallBack(callBack);
                myService.test();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

    }

    private MyService.CallBack callBack = new MyService.CallBack() {
        @Override
        public void postMessage(String message) {
            tv.setText(message);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_start, R.id.btn_bind, R.id.btn_unbind, R.id.btn_stop, R.id.btn_start_foreground, R.id.btn_stop_foreground})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startService(intent);
                break;
            case R.id.btn_bind:
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                unbindService(connection);
                break;
            case R.id.btn_stop:
                stopService(intent);
                break;
            case R.id.btn_start_foreground:
                startService(intentForeground);
                break;
            case R.id.btn_stop_foreground:
                stopService(intentForeground);
                break;
            default:
                break;
        }
    }
}
