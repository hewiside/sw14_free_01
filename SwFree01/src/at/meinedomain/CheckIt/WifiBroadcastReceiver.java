package at.meinedomain.CheckIt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager wifiManager;
    private Channel wifiChannel;
    private AndroidGame gameActivity;

    public WifiBroadcastReceiver(WifiP2pManager manager, Channel channel,
            AndroidGame activity) {
        super();
        this.wifiManager = manager;
        this.wifiChannel = channel;
        this.gameActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        
    	String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // TODO notify activity that Wifi P2P is enabled
            } else {
                // TODO notify activity that Wi-Fi P2P is NOT enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}