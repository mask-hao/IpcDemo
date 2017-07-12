// IBinderPool.aidl
package com.zhanghao.ipcdemo;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
