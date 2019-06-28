// IBookManager.aidl
package com.yangming.myproject.ipc.aidl;

// Declare any non-default types here with import statements
// 客户端AIDL
import com.yangming.myproject.ipc.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
