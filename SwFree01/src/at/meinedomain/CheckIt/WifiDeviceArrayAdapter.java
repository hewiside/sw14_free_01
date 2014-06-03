package at.meinedomain.CheckIt;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WifiDeviceArrayAdapter extends ArrayAdapter<WifiP2pDevice> {
	
	private int resource_;
	private int textViewResourceId_;
	private Activity activity_;
	
	WifiDeviceArrayAdapter(Activity activity, int peerListEntry, 
			int textViewPeer, ArrayList<WifiP2pDevice> peers){
		
		super(activity, peerListEntry, textViewPeer, peers);
		resource_ = peerListEntry;
		textViewResourceId_ = textViewPeer;
		activity_= activity;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = ((LayoutInflater)activity_.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource_, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (textViewResourceId_ == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(textViewResourceId_);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        WifiP2pDevice item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence)item);
        } else {
            text.setText(item.deviceName); //TODO: change to desired string.
        }

        return view;
    }
}