// IBookManager.aidl
package com.zhanghao.ipcdemo;

import com.zhanghao.ipcdemo.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
