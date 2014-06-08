package at.meinedomain.CheckIt;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;

import android.util.Log;

public class ServerThread extends ConnectionThread{    	
	
	private ServerSocket serverSocket;
	
	public ServerThread(Board board){
		super(board);
		serverSocket = null;
	}
	
	@Override
	public void run(){    		
		try{
			serverSocket = new ServerSocket(SERVER_PORT);
			Log.d("ServerThread", "Server: ServerSocket opened.");
			client = serverSocket.accept();
			Log.d("ServerThread", "Server: "+client.getLocalAddress()+
								 " connected to "+client.getInetAddress());
			
			in = client.getInputStream();
			out = client.getOutputStream();
			client.setSoTimeout(INITIAL_SOCKET_TIMEOUT);
			
			byte[] b = new byte[BUFFER_SIZE];
			int length;
			
			try{
				if((length = in.read(b)) != -1){
					if(START_TAG.equals(new String(b, "UTF-8"))){
						Log.d("ServerThread", "START_TAG received.");
					}
					else{
						Log.wtf("ServerThread", "Wrong start tag!!!");
						return;
					}
				}
				else{
					Log.wtf("ServerThread", "No start tag received!!!");
					return; 
				}
				
				client.setSoTimeout(SOCKET_TIMEOUT);
				while(true){
					while(!stopRequested){
						sendMoveWhenMade(out, b);
						// TODO maybe listen for opponent giving up
					}
					while(!stopRequested){
						processIncommingMove(in, b);
					}
					if(stopRequested){
						break;
					}
				}
			}
			catch(InterruptedIOException e){
				// try again
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
					try {
						client.close();
					} catch (IOException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
}