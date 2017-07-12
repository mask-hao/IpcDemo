package com.zhanghao.ipcdemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.zhanghao.ipcdemo.Constant;

public class RemoteService extends Service {


    private static final String TAG = "RemoteService";

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_FROM_CLIENT:
                    Log.d(TAG, "handleMessage: from client " +msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;
                    Message replayMessage = Message.obtain(null,Constant.MSG_FROM_REMOTE_SERVICE);
                    Bundle  bundle = new Bundle();
                    bundle.putString("replay","I got your message,this is a replay!");
                    replayMessage.setData(bundle);

                    try {
                        client.send(replayMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case Constant.MSG_FROM_REMOTE_SERVICE:
                    break;
                default:super.handleMessage(msg);
            }
        }
    }

    public RemoteService() {
    }

    private final Messenger messenger  = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
