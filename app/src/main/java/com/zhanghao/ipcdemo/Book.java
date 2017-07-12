package com.zhanghao.ipcdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhanghao on 2017/6/26.
 */

public class Book implements Parcelable{
    private int bookId;
    private String bookName;


    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }

    public Book(int bookId, String bookName){
        this.bookId = bookId;
        this.bookName = bookName;
    }


    private  Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bookId);
        parcel.writeString(bookName);
    }
}
