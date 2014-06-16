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
	protected TimeGetterSetter timeGetterSetter;
	
	protected boolean startRequested;
	protected boolean stopRequested;
	
	protected Move myMove;
	protected boolean myMoveMade;
	protected boolean myMoveSent;
	protected boolean opponentsMoveMade;
	protected float opponentsTime;
	
	protected static final int SERVER_PORT = 8864;
	protected static final int INITIAL_SOCKET_TIMEOUT = 30000;
	protected static final int SOCKET_TIMEOUT = 2000; // TODO TODO set to 50
	protected static final String START_TAG = "STARTNOW"; // IMPORTANT: all tags shall have length BUFFER_SIZE --> easy comparison between byte[] and String.
	protected static final String EXIT_TAG  = "EXIT....";
	protected static final int BUFFER_SIZE = 8;
	public static final float DUMMY_OPPONENTS_TIME = Float.MAX_VALUE;
	
	public ConnectionThread(Board board, TimeGetterSetter tgs) {
		super();
		in = null;
		out = null;
		client = null;
		this.board = board;
		timeGetterSetter= tgs;
		startRequested = false;
		stopRequested  = false;
		myMove = null;
		myMoveMade = false;
		opponentsMoveMade = false;
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
		myMoveMade = true;
	}
	
	// Sending/Receiving -------------------------------------------------------
	protected void processIncommingMove(InputStream in, byte[] b){
		int length;
		try {
			if((length = in.read(b)) != -1){
				if(length == 7){
					Point from = new Point(b[0], b[1]);
					Point to   = new Point(b[2], b[3]);
					MoveType mt = decodeMoveType(b[4]);
					board.move(from, to, mt);
					timeGetterSetter.setTime(b[5]*60 + b[6]);
					opponentsMoveMade = true;
				}
				else{
					compareToExitTagAndProcess(b);
				}
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private MoveType decodeMoveType(byte b){
		if(b == "N".getBytes()[0]){
			return MoveType.NORMAL;
		}
		if(b == "C".getBytes()[0]){
			return MoveType.CAPTURE;
		}
		if(b == "K".getBytes()[0]){
			return MoveType.CASTLE_KINGSIDE;
		}
		if(b == "Q".getBytes()[0]){
			return MoveType.CASTLE_QUEENSIDE;
		}
		if(b == "D".getBytes()[0]){
			return MoveType.DOUBLE_STEP;
		}
		if(b == "E".getBytes()[0]){
			return MoveType.EN_PASSANT;
		}
		else{
			return MoveType.ILLEGAL;
		}
	}
	
	protected void sendMoveWhenMade(OutputStream out, byte[] b){
//		Log.wtf("sendMoveWhenMade()", "moveMade? --> "+myMoveMade);
		if(myMoveMade){
			myMoveMade = false;
			if(myMove==null){
				Log.wtf("ConnectionThread", "Oh no! myMove is null!!!");
			}
			b[0] = (byte)myMove.getFromX();
			b[1] = (byte)myMove.getFromY();
			b[2] = (byte)myMove.getToX();
			b[3] = (byte)myMove.getToY();
			b[4] = encodeMoveType(myMove.getMoveType());
			b[5] = (byte)timeGetterSetter.getMinutes();
			b[6] = (byte)timeGetterSetter.getSeconds();
			try {
				out.write(b, 0, 7);
				myMoveSent = true;
				Log.d("ConnectionThread","Move sent.");
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private byte encodeMoveType(MoveType mt){
		if(mt==MoveType.NORMAL){
			return "N".getBytes()[0];
		}
		if(mt==MoveType.CAPTURE){
			return "C".getBytes()[0];
		}
		if(mt==MoveType.CASTLE_KINGSIDE){
			return "K".getBytes()[0];
		}
		if(mt==MoveType.CASTLE_QUEENSIDE){
			return "Q".getBytes()[0];
		}
		if(mt==MoveType.DOUBLE_STEP){
			return "D".getBytes()[0];
		}		
		if(mt==MoveType.EN_PASSANT){
			return "E".getBytes()[0];
		}
		else{
			return "!".getBytes()[0];
		}
	}
	
	protected void sendExitTag(OutputStream out, byte[] b){
		for(int i=0; i<EXIT_TAG.length(); i++){
			b[i] = (byte) EXIT_TAG.charAt(i);
		}
		try {
			Log.d("ConnectionThread", "I'm leaving. Bye, bye!");
			out.write(b);
			Log.d("ConnectionThread", "Yes, I said bye, bye!");
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
				Log.d("ConnectionThread", "Opponent left.");
				// TODO also call requestStop() ?
			}
		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
