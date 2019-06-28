package com.yangming.myproject.ipc.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.yangming.myproject.ipc.aidl.Book;
import com.yangming.myproject.ipc.aidl.IOnNewBookArrivedListener;

import java.util.List;

/**
 * @author yangming on 2019/4/24
 * 手动实现一个Binder，不需要AIDL文件
 */
public interface IBookManager extends IInterface {

    static final String DESCRIPTOR = "com.yangming.myproject.ipc.binder.IBookManager";
    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
    static final int TRANSACTION_registerListener = IBinder.FIRST_CALL_TRANSACTION + 2;
    static final int TRANSACTION_unregisterListener = IBinder.FIRST_CALL_TRANSACTION + 3;


    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;

    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException;

    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException;


}
