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
    public void onReceive(Context context, final Intent intent) {
        
    	String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            	gameActivity.setIsWifiP2PEnabled(true);
            } else {
            	gameActivity.setIsWifiP2PEnabled(false);
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
            		// We are connected with the other device, request connection
            		// info to find group owner IP:
            		wifiManager.requestConnectionInfo(wifiChannel, 
            				new WifiP2pManager.ConnectionInfoListener() {
								
								@Override
								public void onConnectionInfoAvailable(WifiP2pInfo info) {
									// Log...
						        	gameActivity.logConnectionInfo(intent.getParcelableExtra("wifiP2pInfo"));
						        	gameActivity.logConnectionInfo(intent.getParcelableExtra("networkInfo"));
								    
								    InetAddress groupOwnerAddress = info.groupOwnerAddress;

								    // After the group negotiation, we can determine the group owner.
								    if (info.groupFormed && info.isGroupOwner){
								        // Do whatever tasks are specific to the group owner.
								        // One common case is creating a server thread and accepting
								        // incoming connections. (TODO)
								    	
								    	// ...
								    	Log.d("WifiBroadCastReceiver", "I am the group owner.");
										gameActivity.onOpponentSelected(Color.WHITE);
								    } else if (info.groupFormed){
								        // The other device acts as the client. In this case,
								        // you'll want to create a client thread that connects to the group
								        // owner. (TODO)
								    	
								    	// ...
								    	Log.d("WifiBroadCastReceiver", "I am the client.");
										gameActivity.onOpponentSelected(Color.BLACK);
								    }

								}
							});
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