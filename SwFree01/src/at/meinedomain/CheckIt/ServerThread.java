package at.meinedomain.CheckIt;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;

import android.util.Log;
import at.meinedomain.CheckIt.Screens.GameScreen;

public class ServerThread extends ConnectionThread{    	
	
	private ServerSocket serverSocket;
	
	public ServerThread(Board board, GameScreen gs){
		super(board, gs);
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
						startRequested = true;
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
					Log.i("ServerThread", "entering send-Loop.");
					while(!stopRequested && !myMoveSent){
						sendMoveWhenMade(out, b);
						if(myMoveSent){
							break;
						}
						CheckForExitingOpponent(in, b);
					}
					myMoveSent = false;
					Log.i("ServerThread", "left send-Loop.");
					Log.i("ClientThread", "entering receive-Loop.");
					while(!stopRequested && !opponentsMoveMade){
						processIncommingMove(in, b);
					}
					opponentsMoveMade = false;
					if(stopRequested){
						break;
					}
				}
				sendExitTag(out, b);
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