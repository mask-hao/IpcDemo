package com.zhanghao.ipcdemo.aidlpool;

import android.os.RemoteException;

import com.zhanghao.ipcdemo.ISecurityCenter;

/**
 * Created by zhanghao on 17-7-12.
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub{

    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char [] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^=SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
