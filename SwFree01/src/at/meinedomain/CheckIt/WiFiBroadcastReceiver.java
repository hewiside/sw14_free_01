package at.meinedomain.CheckIt;


import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.content.Context;
import android.content.Intent;

import com.badlogic.androidgames.framework.impl.AndroidGame;


public class WiFiBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel mChannel;
    private AndroidGame gameActivity;

    public WiFiBroadcastReceiver(WifiP2pManager manager, Channel channel,
            AndroidGame activity) {
        super();
        this.manager = manager;
        this.mChannel = channel;
        this.gameActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
        return;
    }
}