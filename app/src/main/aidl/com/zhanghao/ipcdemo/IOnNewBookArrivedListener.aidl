// IOnNewBookArrivedListener.aidl
package com.zhanghao.ipcdemo;

import com.zhanghao.ipcdemo.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
