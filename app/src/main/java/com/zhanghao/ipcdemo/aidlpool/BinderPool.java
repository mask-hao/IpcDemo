package com.zhanghao.ipcdemo.aidlpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zhanghao.ipcdemo.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhanghao on 17-7-12.
 */

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NODE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;
    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPool binderIntanse;
    private CountDownLatch mConnectionCountDownLatch;

    private BinderPool(Context context){
        this.mContext = context;
        connectionBinderPoolService();
    }


    public static BinderPool getInstance(Context context){
        if (binderIntanse==null){
            synchronized (BinderPool.class){
                if (binderIntanse==null)
                    binderIntanse = new BinderPool(context);
            }
        }
        return binderIntanse;
    }


    private  synchronized void connectionBinderPoolService() {
        mConnectionCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);
        try {
            mConnectionCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        if (mBinderPool!=null){
            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }



    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);

            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            }catch (RemoteException e){
                e.printStackTrace();
            }
            mConnectionCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient(){
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied: binder is died");
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool = null;
            connectionBinderPoolService();
        }
    };





    public static class BinderPoolImpl extends IBinderPool.Stub{


        public BinderPoolImpl(){
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_SECURITY_CENTER:{
                  binder = new SecurityCenterImpl();
                    break;
                }
                case BINDER_COMPUTE:{
                   binder = new ComputeImpl();
                    break;
                }
                default:
                    break;
            }
            return binder;
        }
    }


}
