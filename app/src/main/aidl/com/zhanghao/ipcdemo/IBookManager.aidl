// IBookManager.aidl
package com.zhanghao.ipcdemo;

import com.zhanghao.ipcdemo.Book;
import com.zhanghao.ipcdemo.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
