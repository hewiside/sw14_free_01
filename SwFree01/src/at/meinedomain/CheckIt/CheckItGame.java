package at.meinedomain.CheckIt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.R.bool;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import at.meinedomain.CheckIt.Screens.AbstractScreen;
import at.meinedomain.CheckIt.Screens.LoadingScreen;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class CheckItGame extends AndroidGame 
						 implements WifiP2pManager.ConnectionInfoListener{
	
	private static final int SERVER_PORT = 8864;
	private Color playerColor; // TODO: ensure reset of variable after a game.
	
	private FragmentManager fragManager;
    private WifiP2pManager  wifiManager;
    private Channel wifiChannel;
    BroadcastReceiver wifiReceiver;
    private final IntentFilter wifiIntentFilter = new IntentFilter();
	private boolean isWifiP2PEnabled; // used (set) in WifiBroadcastReceiver TODO: needed?
	private boolean isBackPressed = false;
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
    
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
	    InetAddress groupOwnerAddress = info.groupOwnerAddress;
	    // After the group negotiation, we can determine the group owner.
	    
	    if (info.groupFormed && info.isGroupOwner){
	        // Do whatever tasks are specific to the group owner.
	        // One common case is creating a server thread and accepting
	        // incoming connections. (TODO)
	    	Log.d("WifiBroadCastReceiver", "I am the group owner.");
	    	Thread serverThread = new ServerThread();
	    	serverThread.start();
			onOpponentSelected(Color.WHITE);
	    } else if (info.groupFormed){
	        // The other device acts as the client. In this case,
	        // you'll want to create a client thread that connects to the group
	        // owner. (TODO)
	    	
	    	// ...
	    	Log.d("WifiBroadCastReceiver", "I am the client.");
			Thread clientThread = new ClientThread(info);
			clientThread.start();
	    	onOpponentSelected(Color.BLACK);
	    }
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
    public boolean getIsBackPressed(){
    	return isBackPressed;
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
    public void logConnectionInfo(Object o){
    	Log.d("CONNECTION CHANGED TO:", ""+o);
    }
    public void setIsBackPressed(boolean backStatus){
    	isBackPressed = backStatus;
    }
    public void setIsWifiP2PEnabled(boolean wifiStatus){
    	this.isWifiP2PEnabled = wifiStatus;
    	if(wifiStatus==false){
    		peers.clear();
    	}
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

    public void onOpponentSelected(Color color){
    	if(fragManager.findFragmentByTag("PeerList")==null){
    		Log.d("CheckItGame", "Fragment not findable (This is often the case for invited devices.)");
    	}
    	else{
    		fragManager.beginTransaction().
    						remove(fragManager.findFragmentByTag("PeerList")).
    						commitAllowingStateLoss();
    	}
    	playerColor = color;
    	Log.d("CheckItGame", "onOpponentSelected() -> I play with "+color);
    }
    public Color getPlayerColor(){
    	return playerColor;
    }
    public void setPlayerColor(Color c){
    	playerColor = c;
    	Log.d("CheckItGame", "playerColor: "+playerColor);
    }

    
//------------------------------------------------------------------------------
    private class ServerThread extends Thread{
    	private boolean stopRequested;
    	
    	public ServerThread(){
    		super();
    		stopRequested = false;
    	}
    	
    	@Override
    	public void run(){
    		ServerSocket serverSocket = null;
    		Socket client = null;
    		InputStream in = null;
    		OutputStream out = null;
    		
    		try{
    			serverSocket = new ServerSocket(SERVER_PORT);
    			Log.d("ServerThread", "Server: ServerSocket opened.");
    			client = serverSocket.accept();
    			Log.d("ServerThread", "Server: "+client.getLocalAddress()+
    								 " connected to "+client.getInetAddress());
    			
    			in = client.getInputStream();
//    			out = new BufferedOutputStream(client.getOutputStream(), 8);   			
    			client.setSoTimeout(50);
    			
    			byte[] b = new byte[8];
    			int length;
    			
				try{
					if((length = in.read(b)) != -1){
						System.out.write(b,0,length);
						Log.wtf("ServerThread", "length:"+length+", "+new String(b, "UTF-8"));
//						Log.wtf("ServerThread", IOUtils.toString(b, "UTF-8"));
						Log.wtf("ServerThread", "end reached");
						stopRequested = true;
					}
//					else{
//						Log.wtf("ServerThread", "end not reached");
//						if(b[0]=="S".getBytes()[0] &&
//						   b[1]=="T".getBytes()[1]){
//							Log.wtf("ServerThread", "OK, I will start.");
//						}
//					}
				}
				catch(InterruptedIOException e){
					// try again
				}
    			
    			while(!stopRequested){
    				requestStop(); //TODO
    			}
    		}
    		catch(IOException e){
    			Log.wtf("CheckItGame", e.getMessage());
    		}		
    		finally{
    			if(in != null){
    				try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			if(out != null){
    				try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			if(client != null){
    				if(client.isConnected()){
    					try {
							client.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    			if(serverSocket != null){
    				try {
						serverSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}	
    	}
    	
    	public synchronized void requestStop(){
    		stopRequested = true;
    	}
    }
    
    
//------------------------------------------------------------------------------
    private class ClientThread extends Thread{
    	private WifiP2pInfo info;
    	private boolean stopRequested;
    	
    	public ClientThread(WifiP2pInfo info){
    		super();
    		this.info = info;
    		stopRequested = false;
    	}
    	
    	@Override
    	public void run(){
    		Socket client = null;
    		InputStream in = null;
    		OutputStream out = null;
    		
    		try{
    			client = new Socket(info.groupOwnerAddress.getHostAddress(),
    									   SERVER_PORT);
    			Log.d("CheckItGame", "Client: "+client.getLocalAddress()+
    								 " connected to "+client.getInetAddress());
    			
//    			in = new BufferedInputStream(client.getInputStream(), 8);
    			out = client.getOutputStream();    			
    			client.setSoTimeout(50);
    			
    			out.write("START".getBytes("UTF-8"));
    			Log.wtf("Client Thread", "Please start!");
    			while(!stopRequested){
    				stopRequested = true;
    			}
    		}
    		catch(IOException e){
    			Log.wtf("CheckItGame", e.getMessage());
    		}
    		finally{
    			if(in != null){
    				try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			if(out != null){
    				try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			if(client != null){
    				if(client.isConnected()){
    					try{
    						client.close();
    					}
    					catch(IOException e){
    						// TODO: maybe we could do more to save the day...
    						e.printStackTrace();
    					}
    				}
    			}
    		}
    	}
    	
    	public synchronized void requestStop(){
    		stopRequested = true;
    	}
    }    
} 

