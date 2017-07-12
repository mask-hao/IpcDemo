package com.zhanghao.ipcdemo.aidlpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zhanghao on 17-7-12.
 */

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: onBind");
        return mBinderPool;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
