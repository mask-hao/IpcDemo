package com.zhanghao.ipcdemo.a_idl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhanghao.ipcdemo.Book;
import com.zhanghao.ipcdemo.Constant;
import com.zhanghao.ipcdemo.IBookManager;
import com.zhanghao.ipcdemo.IOnNewBookArrivedListener;
import com.zhanghao.ipcdemo.R;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {


    private static final String TAG = "BookManagerActivity";

    private IBookManager mRemoteBookManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        bindService(new Intent(this,BookManagerService.class),serviceConnection, Context.BIND_AUTO_CREATE);


        findViewById(R.id.anr_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRemoteBookManager.getBookList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "handleMessage: "+msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub(){
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(Constant.MSG_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Log.d(TAG, "onServiceConnected: "+list.getClass().getCanonicalName());
                Log.d(TAG, "onServiceConnected: "+list.toString());

                Book newBook = new Book(10,"Android");

                bookManager.addBook(newBook);

                List<Book> newList = bookManager.getBookList();
                Log.d(TAG, "onServiceConnected: "+newList.toString());

                bookManager.registerListener(mOnNewBookArrivedListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onServiceConnected: name" +Thread.currentThread().getName());

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
                mRemoteBookManager = null;
            Log.d(TAG, "onServiceDisconnected: binder died");
            Log.d(TAG, "onServiceDisconnected: name "+Thread.currentThread().getName());
        }
    };


    @Override
    protected void onDestroy() {

        if (mRemoteBookManager!=null && mRemoteBookManager.asBinder().isBinderAlive()){
            try {
                Log.d(TAG, "onDestroy: unregister listener "+mOnNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
