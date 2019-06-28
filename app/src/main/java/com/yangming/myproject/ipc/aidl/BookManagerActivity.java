package com.yangming.myproject.ipc.aidl;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.yangming.myproject.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;

    @BindView(R.id.tv_aidl)
    TextView tvAidl;
    @BindView(R.id.tv1)
    TextView tv1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "receive new book : " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
//            // 手写实现Binder
//            com.yangming.myproject.ipc.binder.IBookManager bookManager = BookManagerImpl.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "query book list, list type：" + list.getClass().getCanonicalName());
                Log.e(TAG, "query book list: " + list.toString());
                Book newBook = new Book(3, "Android开发艺术探索");
                bookManager.addBook(newBook);
                Log.e(TAG, "add book: " + newBook);
                List<Book> newList = bookManager.getBookList();
                bookManager.registerListener(mOnNewBookArrivedListener);

                tvAidl.setText(list.toString());
                tv1.setText(newList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.e(TAG, "binder died. ");
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.e(TAG, "unregister listener: " + mOnNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
}
