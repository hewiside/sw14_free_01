package at.meinedomain.CheckIt;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import at.meinedomain.CheckIt.PeerListFragment.OnOpponentSelectedListener;
import at.meinedomain.CheckIt.Screens.AbstractScreen;
import at.meinedomain.CheckIt.Screens.LoadingScreen;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class CheckItGame extends AndroidGame 
						 implements OnOpponentSelectedListener {
	
	private Color playerColor; // TODO: ensure reset of variable after a game.
	
	private FragmentManager fragManager;
    private WifiP2pManager  wifiManager;
    private Channel wifiChannel;
    BroadcastReceiver wifiReceiver;
    private final IntentFilter wifiIntentFilter = new IntentFilter();
	private boolean isWifiP2PEnabled;
	public boolean isBackPressed = false;
	private boolean wifiCheckPossible = false;	// used in MainMenuScreen
	private ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            // Out with the old, in with the new.
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            if(fragManager.findFragmentByTag("PeerList") != null){
            	((PeerListFragment) fragManager.
            			findFragmentByTag("PeerList")).getPeerAdapter().
            			notifyDataSetChanged();
            }
        }
    };

	
	public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playerColor = null;
		
		fragManager = getFragmentManager();

		isWifiP2PEnabled = false;
        
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
        wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiManager.initialize(this, getMainLooper(), null);
        
//        peers.add(0,"1. peer (onCreate of CheckItGame)");	// TODO delete this static code
//        peers.add(1,"2. peer");								// TODO delete this static code
	}
	
	@Override
	public void onResume(){
		super.onResume();
		fragManager = getFragmentManager();
		// TODO determine whether it's safe to register broadcast receiver after super.onResume() which sets the screen.
		wifiReceiver = new WifiBroadcastReceiver(wifiManager, wifiChannel, 
				 								 this, peerListListener);
		registerReceiver(wifiReceiver, wifiIntentFilter);
        
		discoverPeers();
//        wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                // Code for when the discovery initiation is successful goes here.
//                // No services have actually been discovered yet, so this method
//                // can often be left blank.  Code for peer discovery goes in the
//                // onReceive method of the broadcast receiver.
//            	setWifiCheckPossible(true);
//            }
//
//            @Override
//            public void onFailure(int reasonCode) {
//            	// TODO
//                // Code for when the discovery initiation fails goes here.
//                // Alert the user that something went wrong.
//            	setWifiCheckPossible(false);
//            	Log.w("CheckItGame", "discoverPeers FAILS!");
//            }
//        });
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
    
    
    public void discoverPeers(){
        wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method of the broadcast receiver.
            	setWifiCheckPossible(true);
            }

            @Override
            public void onFailure(int reasonCode) {
            	// TODO
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            	setWifiCheckPossible(false);
            	Log.w("CheckItGame", "discoverPeers FAILS!");
            }
        });
    }
    public WifiP2pManager getWifiManager(){
    	return wifiManager;
    }
    public Channel getWifiChannel(){
    	return wifiChannel;
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
    public void setWifiCheckPossible(boolean b){
    	this.wifiCheckPossible = b;
    }
    public ArrayList<WifiP2pDevice> getPeers(){
    	return peers;
    }
    public void showPeerList(){
    	PeerListFragment fragment = new PeerListFragment();
    	fragment.show(fragManager, "PeerList");
    }
    @Override
    public void onOpponentSelected(Color color){
    	if(fragManager.findFragmentByTag("PeerList")==null){
    		Log.e("CheckItGame", "OH NO!!!");
    	}
    	fragManager.beginTransaction().
    						remove(fragManager.findFragmentByTag("PeerList"));
    	Log.e("CheckItGame", "Fragment gone.");
    	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	playerColor = Color.WHITE;
    }
    public Color getPlayerColor(){
    	return playerColor;
    }
    public void setPlayerColor(Color c){
    	playerColor = c;
    	Log.e("CheckItGame", "playerColor: "+playerColor);
    }
} 

