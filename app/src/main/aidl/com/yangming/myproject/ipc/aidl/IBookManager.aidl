// IBookManager.aidl
package com.yangming.myproject.ipc.aidl;

// Declare any non-default types here with import statements
// 服务端AIDL
import com.yangming.myproject.ipc.aidl.Book;
import com.yangming.myproject.ipc.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
