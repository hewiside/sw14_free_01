package at.meinedomain.CheckIt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import android.util.Log;
import at.meinedomain.CheckIt.Screens.GameScreen;

public class ConnectionThread extends Thread {
	protected InputStream in;
	protected OutputStream out;
	protected Socket client;
	protected Board board;
	protected GameScreen gs;
	
	protected boolean startRequested;
	protected boolean stopRequested;
	
	protected Move myMove;
	protected boolean moveMade;
	protected float opponentsTime;
	
	protected static final int SERVER_PORT = 8864;
	protected static final int INITIAL_SOCKET_TIMEOUT = 30000;
	protected static final int SOCKET_TIMEOUT = 2000; // TODO TODO set to 50
	protected static final String START_TAG = "STARTNOW"; // IMPORTANT: all tags shall have length BUFFER_SIZE --> easy comparison between byte[] and String.
	protected static final String EXIT_TAG  = "EXIT....";
	protected static final int BUFFER_SIZE = 8;
	public static final float DUMMY_OPPONENTS_TIME = Float.MAX_VALUE;
	
	public ConnectionThread(Board board, GameScreen gs) {
		super();
		in = null;
		out = null;
		client = null;
		this.board = board;
		this.gs    = gs;
		startRequested = false;
		stopRequested  = false;
		myMove = null;
		moveMade = false;
	}
	
	// Getters/Setters ---------------------------------------------------------
	public boolean getStartRequested(){
		return startRequested;
	}
	
	public void requestStart(){
		startRequested = true;
	}
	
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
					else{
						compareToExitTagAndProcess(b);
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
	
	protected void sendExitTag(OutputStream out, byte[] b){
		for(int i=0; i<EXIT_TAG.length(); i++){
			b[i] = (byte) EXIT_TAG.charAt(i);
		}
		try {
			Log.i("ConnectionThread", "I'm leaving. Bye, bye!");
			out.write(b);
			Log.i("ConnectionThread", "Yes, I said bye, bye!");
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void CheckForExitingOpponent(InputStream in, byte[] b){
		int length;
		try {
			if((length = in.read(b)) == -1) {}
			else if(length == EXIT_TAG.length()){
				compareToExitTagAndProcess(b);			
			}
			else{
				Log.wtf("ConnectionThread","We got "+b+" and not EXIT_TAG!");
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void compareToExitTagAndProcess(byte[] b){
		try {
			if(EXIT_TAG.equals(new String(b, "UTF-8"))){
				board.setMatchState(Board.MatchState.WON);
				Log.i("ConnectionThread", "Opponent left.");
				// TODO also call requestStop() ?
			}
		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
