package at.meinedomain.CheckIt;

public class Move {
	private Point from;
	private Point to;
	
	public Move(Point from, Point to){
		this.from = from;
		this.to = to;
	}
	
	public Point getFrom(){
		return from;
	}
	
	public Point getTo(){
		return to;
	}
	
	public int getFromX(){
		return from.getX();
	}
	public int getFromY(){
		return from.getY();
	}
	public int getToX(){
		return to.getX();
	}
	public int getToY(){
		return to.getY();
	}	
	
}
