package com.yangming.myproject.ipc.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yangming.myproject.ipc.aidl.Book;
import com.yangming.myproject.ipc.aidl.IOnNewBookArrivedListener;

import java.util.List;

/**
 * @author yangming on 2019/4/24
 * 实现Stub类和Stub类中的Proxy代理类
 */
public class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManager) {
            return (IBookManager) iin;
        }
        return new BookManagerImpl.Proxt(obj);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_registerListener: {
                data.enforceInterface(DESCRIPTOR);
                IOnNewBookArrivedListener arg0;
                arg0 = IOnNewBookArrivedListener.Stub.asInterface(data.readStrongBinder());
                this.registerListener(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_unregisterListener: {
                data.enforceInterface(DESCRIPTOR);
                IOnNewBookArrivedListener arg0;
                arg0 = IOnNewBookArrivedListener.Stub.asInterface(data.readStrongBinder());
                this.unregisterListener(arg0);
                return true;
            }
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        // TODO 待实现
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        // TODO 待实现
    }

    @Override
    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
        // TODO 待实现
    }

    @Override
    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
        // TODO 待实现
    }

    private static class Proxt implements IBookManager {
        private IBinder mRemote;

        public Proxt(IBinder mRemote) {
            this.mRemote = mRemote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                mRemote.transact(TRANSACTION_registerListener, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                mRemote.transact(TRANSACTION_unregisterListener, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public java.lang.String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }


    }

}
