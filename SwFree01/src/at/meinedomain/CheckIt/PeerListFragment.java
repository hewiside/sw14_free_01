package at.meinedomain.CheckIt;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeerListFragment extends DialogFragment{
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.peer_list_fragment, container, false);
//		List
		
		return v;
	}

}