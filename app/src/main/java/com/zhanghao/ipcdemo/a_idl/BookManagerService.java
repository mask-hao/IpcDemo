package com.zhanghao.ipcdemo.a_idl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.zhanghao.ipcdemo.Book;
import com.zhanghao.ipcdemo.IBookManager;
import com.zhanghao.ipcdemo.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners =new RemoteCallbackList<>();
    private AtomicBoolean isServiceDestroyed  = new AtomicBoolean(false);


    private Binder mBinder = new IBookManager.Stub(){
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return books;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
                listeners.register(listener);
            Log.d(TAG, "registerListener:");
//            Log.d(TAG, "registerListener: " + listeners.beginBroadcast());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
            Log.d(TAG, "unregisterListener: ");
//            Log.d(TAG, "unregisterListener: "+listeners.beginBroadcast());
        }
    };






    public BookManagerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        books.add(new Book(1,"android"));
        books.add(new Book(2,"ios"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        books.add(book);
        final int N = listeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = listeners.getBroadcastItem(i);
            if (l!=null){
                l.onNewBookArrived(book);
            }
        }
        listeners.finishBroadcast();
    }



    private class ServiceWorker implements Runnable{
        @Override
        public void run() {
            while (!isServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = books.size()+1;
                Book newBook = new Book(bookId,"new Book"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
