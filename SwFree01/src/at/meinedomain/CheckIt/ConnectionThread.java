package at.meinedomain.CheckIt;

public class ConnectionThread extends Thread {
	protected boolean stopRequested;
	protected Move move;
	protected boolean moveMade;
	
	public ConnectionThread() {
		super();
		stopRequested = false;
		move = null;
		moveMade = false;
	}
	
	public synchronized void requestStop(){
		stopRequested = true;
	}
	
	public void moveMade(){
		moveMade = true;
	}
	
	public void setMove(Move move){
		this.move = move;
	}
}
