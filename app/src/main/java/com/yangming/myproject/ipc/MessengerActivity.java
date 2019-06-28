package com.yangming.myproject.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yangming.myproject.Constants;
import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yangming
 * 使用Messenger跨进程通信
 */
public class MessengerActivity extends AppCompatActivity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private Messenger mService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVICE:
                    tvMsg.setText(msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        ButterKnife.bind(this);

        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
