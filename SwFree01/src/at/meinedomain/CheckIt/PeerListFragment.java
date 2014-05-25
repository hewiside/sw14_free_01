package at.meinedomain.CheckIt;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PeerListFragment extends DialogFragment{
	
	ArrayList<String> peers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		CheckItGame gameActivity = (CheckItGame) getActivity();
		peers = gameActivity.getPeers();  // PROBLEM, BECAUSE peers-ELEMENTS ARE NOT STRINGS???????????????????????????
		
		// Inflate the layout for this fragment
		View fragLayout = inflater.inflate(R.layout.peer_list_fragment, container, false);
		ListView listView = (ListView) fragLayout.findViewById(R.id.peer_list);
		listView.setEmptyView(fragLayout.findViewById(R.id.empty));
			
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(gameActivity, 
		        R.layout.peer_list_entry, R.id.text_view_peer, peers);
		listView.setAdapter(adapter);
		       
		return fragLayout;
	}

}