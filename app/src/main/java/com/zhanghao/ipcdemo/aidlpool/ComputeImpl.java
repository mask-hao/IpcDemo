package com.zhanghao.ipcdemo.aidlpool;

import android.os.RemoteException;

import com.zhanghao.ipcdemo.ICompute;

/**
 * Created by zhanghao on 17-7-12.
 */

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }

}
