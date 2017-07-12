package com.zhanghao.ipcdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhanghao.ipcdemo.a_idl.BookManagerActivity;
import com.zhanghao.ipcdemo.aidlpool.BinderPoolActivity;
import com.zhanghao.ipcdemo.messenger.MessengerActivity;

public class MainActivity extends AppCompatActivity {

    private Button messenger_bt,aidl_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        messenger_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
            }
        });
        aidl_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BookManagerActivity.class));
            }
        });
        findViewById(R.id.binderpool_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BinderPoolActivity.class));
            }
        });
    }

    private void initView() {
        messenger_bt = (Button) findViewById(R.id.messenger_bt);
        aidl_bt = (Button) findViewById(R.id.aidl_bt);
    }
}
