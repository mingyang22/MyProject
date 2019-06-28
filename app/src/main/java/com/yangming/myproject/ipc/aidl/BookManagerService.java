package com.yangming.myproject.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.yangming.myproject.ipc.binder.BookManagerImpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yangming 2019/04/18
 * AIDL 服务端
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    /**
     * 支持并发读/写
     */
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();


    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
            Log.e(TAG, "registerListener, current size: " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            Log.e(TAG, "unregister success. ");
            Log.e(TAG, "unregisterListener, current size: " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }
    };

    private Binder binder = new BookManagerImpl(){
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
            Log.e(TAG, "registerListener, current size: " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            Log.e(TAG, "unregister success. ");
            Log.e(TAG, "unregisterListener, current size: " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
