package at.meinedomain.CheckIt;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import at.meinedomain.CheckIt.Screens.AbstractScreen;
import at.meinedomain.CheckIt.Screens.LoadingScreen;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class CheckItGame extends AndroidGame {
    
    WifiP2pManager wifiManager;
    Channel wifiChannel;
    BroadcastReceiver wifiReceiver;
    private final IntentFilter wifiIntentFilter = new IntentFilter();
	private boolean isWifiP2PEnabled;
	public boolean isBackPressed = false;
	private boolean wifiCheckPossible = false;	
	private List peers = new ArrayList();
    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            // Out with the old, in with the new.
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            // TODO:
//            ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
//            if (peers.size() == 0) {
//                Log.d(WiFiDirectActivity.TAG, "No devices found");
//                return;
//            }
        }
    };

	
	public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isWifiP2PEnabled = false;
		
        wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiManager.initialize(this, getMainLooper(), null);
        wifiReceiver = new WifiBroadcastReceiver(wifiManager, wifiChannel, 
        										 this, peerListListener);
        
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		// TODO determine whether it's safe to register broadcast receiver after super.onResume() which sets the screen.
        registerReceiver(wifiReceiver, wifiIntentFilter);
        
        wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method of the broadcast receiver.
            	wifiCheckPossible = true;
            }

            @Override
            public void onFailure(int reasonCode) {
            	// TODO
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            	wifiCheckPossible = false;
            }
        });
	}
	@Override
	public void onPause(){
		super.onPause();
        unregisterReceiver(wifiReceiver);
	}
	
    @Override
    public void onBackPressed() {
    	AbstractScreen.ScreenType st = ((AbstractScreen)getCurrentScreen()).
    															getScreenType();
    	if(st == AbstractScreen.ScreenType.MAIN_MENU_SCREEN ||
    	   st == AbstractScreen.ScreenType.LOADING_SCREEN){
    		super.onBackPressed();
    	}
    		
    	isBackPressed = true;	
    }
    
    
    public boolean getIsWifiP2PEnabled(){
    	return isWifiP2PEnabled;
    }
    public void setIsWifiP2PEnabled(boolean wifiStatus){
    	this.isWifiP2PEnabled = wifiStatus;
    }
    public boolean getWifiCheckPossible(){
    	return wifiCheckPossible;
    }
    public List getPeers(){
    	return peers;
    }
} 

