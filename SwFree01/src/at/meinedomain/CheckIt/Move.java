package at.meinedomain.CheckIt;

public class Move {
	private Point from;
	private Point to;
	private MoveType mt;
	
	public Move(Point from, Point to, MoveType mt){
		this.from = from;
		this.to = to;
		this.mt = mt;
	}
	
	public Move(int fromX, int fromY, int toX, int toY, MoveType mt){
		this.from = new Point(fromX, fromY);
		this.to   = new Point(toX,   toY  );
		this.mt   = mt;
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
	
	public MoveType getMoveType(){
		return mt;
	}
}
