package com.zhanghao.ipcdemo.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhanghao.ipcdemo.Constant;
import com.zhanghao.ipcdemo.R;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    private Messenger mMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        bindService(new Intent(this,RemoteService.class),serviceConnection, Context.BIND_AUTO_CREATE);


    }


    private Messenger replayMessenger = new Messenger(new ClientMessengerHandler());


    private  static class ClientMessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_FROM_REMOTE_SERVICE:
                    Log.d(TAG, "handleMessage: receive msg from remote service" + msg.getData().getString("replay"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }









    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            Message message = Message.obtain(null, Constant.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg","this is from client");
            message.setData(bundle);

            message.replyTo = replayMessenger;

            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
