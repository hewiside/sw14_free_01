package at.meinedomain.CheckIt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class ConnectionThread extends Thread {
	protected InputStream in;
	protected OutputStream out;
	protected Socket client;
	protected Board board;
	protected boolean stopRequested;
	protected Move myMove;
	protected boolean moveMade;
	protected static final int SERVER_PORT = 8864;
	protected static final int INITIAL_SOCKET_TIMEOUT = 10000;
	protected static final int SOCKET_TIMEOUT = 2000; // TODO TODO set to 50
	protected static final String START_TAG = "STARTNOW";
	protected static final int BUFFER_SIZE = 8;
	
	
	public ConnectionThread(Board board) {
		super();
		in = null;
		out = null;
		client = null;
		this.board = board;
		stopRequested = false;
		myMove = null;
		moveMade = false;
	}
	
	// Setters -----------------------------------------------------------------
	public synchronized void requestStop(){
		stopRequested = true;
	}
	
	public void setMove(Move move){
		this.myMove = move;
		moveMade = true;
	}
	
	// Sending/Receiving -------------------------------------------------------
	protected void processIncommingMove(InputStream in, byte[] b){
		int length;
		try {
			while(true){
				if((length = in.read(b)) != -1){
					if(length == 4){
						Point from = new Point(b[0], b[1]);
						Point to   = new Point(b[2], b[3]);
						board.move(from, to);
						break;
					}
				}
				if(stopRequested){
					break;
				}
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void sendMoveWhenMade(OutputStream out, byte[] b){
		if(moveMade){
			moveMade = false;
			if(myMove==null){
				Log.wtf("ConnectionThread", "Oh no! myMove is null!!!");
			}
			b[0] = (byte)myMove.getFromX();
			b[1] = (byte)myMove.getFromY();
			b[2] = (byte)myMove.getToX();
			b[3] = (byte)myMove.getToY();
			try {
				out.write(b, 0, 4);
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
