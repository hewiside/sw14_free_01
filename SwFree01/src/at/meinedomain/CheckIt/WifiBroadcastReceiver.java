package at.meinedomain.CheckIt;

import java.net.InetAddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import com.badlogic.androidgames.framework.impl.AndroidGame;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager wifiManager;
    private Channel wifiChannel;
    private CheckItGame gameActivity;
    private PeerListListener peerListListener;

    public WifiBroadcastReceiver(WifiP2pManager manager, Channel channel,
            CheckItGame activity, PeerListListener peerListListener) {
        super();
        this.wifiManager = manager;
        this.wifiChannel = channel;
        this.gameActivity = activity;
        this.peerListListener = peerListListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        
    	String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            	gameActivity.setIsWifiP2PEnabled(true);
            } else {
            	gameActivity.setIsWifiP2PEnabled(false);
				// TODO: Tell the activity to clear its list of peers.
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (wifiManager != null) {
                wifiManager.requestPeers(wifiChannel, peerListListener);
            }
            else{
            	Log.wtf("WifiBroadcastReceiver", "member wifiManager is null in WIFI_P2P_PEERS_CHANGED_ACTION");
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            if (wifiManager != null) {
            	NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            	
            	if (networkInfo.isConnected()) {
					// Log...
		        	gameActivity.logConnectionInfo(intent.getParcelableExtra("wifiP2pInfo"));
		        	gameActivity.logConnectionInfo(intent.getParcelableExtra("networkInfo"));

            		
            		// We are connected with the other device, request connection
            		// info to find group owner IP: (the info is then given to
		        	// the 2nd arg (=gameActivity)of the following method.
		        	// (the gameActivity listens for that, since it implements
		        	// OnConnenctionInfoListener.
            		wifiManager.requestConnectionInfo(wifiChannel, gameActivity);
            	}
            	else{
            		// TODO? This probably means that we're no longer connected.
            		// Should any clean-up take place here?
            	}
            }
            else{
            	Log.wtf("WifiBroadcastReceiver", "member wifiManager is null in WIFI_P2P_PEERS_CHANGED_ACTION");
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
