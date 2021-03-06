package at.meinedomain.CheckIt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PeerListFragment extends DialogFragment{
	
	private ArrayList<WifiP2pDevice> peers;
	private ArrayAdapter<WifiP2pDevice> peerAdapter;
	private CheckItGame gameActivity;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.gameActivity = (CheckItGame) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		peers = gameActivity.getPeers();
		
		// Inflate the layout for this fragment
		View fragLayout = inflater.inflate(R.layout.peer_list_fragment, container, false);
		ListView listView = (ListView) fragLayout.findViewById(R.id.peer_list);
		if(gameActivity.getWifiCheckPossible() == false){
			listView.setEmptyView(fragLayout.findViewById(R.id.no_wifi));
		}
		else{
			listView.setEmptyView(fragLayout.findViewById(R.id.empty));
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewInAdapter,
						int positionOfViewInAdapter, long rowIdOfClickedItem) {
				WifiP2pDevice device = peers.get(positionOfViewInAdapter);
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				
				gameActivity.getWifiManager().connect(
						gameActivity.getWifiChannel(), config, 
						new ActionListener() {
							@Override
							public void onSuccess() {
								// BroadcastReceiver will notify us. Ignore for now.
								Log.d("PeerListFragment", "connect() SUCCESS!");
							}
							
							@Override
							public void onFailure(int reason) {
								// TODO Auto-generated method stub
								Log.e("PeerListFragment", "connect() FAILED!");
							}
						});
			}
		});
		
		peerAdapter = new WifiDeviceArrayAdapter(gameActivity, 
		        R.layout.peer_list_entry, R.id.text_view_peer, peers);
		listView.setAdapter(peerAdapter);
		       
		return fragLayout;
	}
	
	public ArrayAdapter<WifiP2pDevice> getPeerAdapter(){
		return peerAdapter;
	}

}