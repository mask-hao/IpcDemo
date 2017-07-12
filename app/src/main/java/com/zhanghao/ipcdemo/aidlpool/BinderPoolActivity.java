package com.zhanghao.ipcdemo.aidlpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhanghao.ipcdemo.ICompute;
import com.zhanghao.ipcdemo.ISecurityCenter;
import com.zhanghao.ipcdemo.R;

public class BinderPoolActivity extends AppCompatActivity {


    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;
    private static final String TAG = "BinderPoolActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();

    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder sercurity  = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(sercurity);
        Log.d(TAG, "doWork: visit ISecurityCenter");
        String msg = "hello Android";
        try {
            String password = mSecurityCenter.encrypt(msg);
            Log.d(TAG, "entrypt: "+password);
            Log.d(TAG, "detrypt: "+mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "doWork: visit ICompute");
        IBinder compute = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(compute);
        try {
            Log.d(TAG, " 1+5 "+mCompute.add(1,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
