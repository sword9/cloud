package com.hochan.sqlite.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 查看网络是否连通
 * Created by Administrator on 2016/7/22.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static boolean NetworkConnected = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (int i =0; i<networkInfos.length; i++) {
                NetworkInfo.State state = networkInfos[i].getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    NetworkConnected = true;
                    return;
                }
            }
        }

        //没有执行return,则说明当前无网络连接
        NetworkConnected = false;
    }
}
