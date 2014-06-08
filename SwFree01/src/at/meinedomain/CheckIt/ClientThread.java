package at.meinedomain.CheckIt;

import java.io.IOException;
import java.net.Socket;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

public class ClientThread extends ConnectionThread{
	private WifiP2pInfo info;
	
	public ClientThread(WifiP2pInfo info, Board board){
		super(board);
		this.info = info;
	}
	
	@Override
	public void run(){
		
		try{
			client = new Socket(info.groupOwnerAddress.getHostAddress(),
									   SERVER_PORT);
			Log.d("CheckItGame", "Client: "+client.getLocalAddress()+
								 " connected to "+client.getInetAddress());
			
			in = client.getInputStream();
			out = client.getOutputStream();    			
			client.setSoTimeout(SOCKET_TIMEOUT);
			
			byte[] b = new byte[BUFFER_SIZE];
			
			while(!startRequested){
				if(stopRequested){
					Log.wtf("ClientThread", "Stop requested before start");
					return;
				}
			}
			
			out.write(START_TAG.getBytes("UTF-8"));
			Log.wtf("Client Thread", "Please start!");
			while(true){
				while(!stopRequested){
					processIncommingMove(in, b);
				}
				while(!stopRequested){
					sendMoveWhenMade(out, b);
					CheckForExitingOpponent(in, b);
				}
				if(stopRequested){
					break;
				}
			}
			sendExitTag(out, b);
		}
		catch(IOException e){
			Log.wtf("CheckItGame", e.getMessage());
		}
		finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(client != null){
				if(client.isConnected()){
					try{
						client.close();
					}
					catch(IOException e){
						// maybe we could do more to save the day...
						e.printStackTrace();
					}
				}
			}
		}
	}
}