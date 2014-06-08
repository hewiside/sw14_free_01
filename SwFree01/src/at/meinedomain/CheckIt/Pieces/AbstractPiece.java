package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class AbstractPiece {
	private Board board;
	private Color color;
	private Point location;
	protected Pixmap pixmap;


	public AbstractPiece(Board b, Color col, Point pt){
		board = b;
		color = col;
		location = pt;
	}
	
	protected abstract MoveType CanMove(Point pt);
	
	public Color getColor(){
		return color;
	}
	
	public Point getLocation(){
		return location;
	}
	
	public Pixmap getPixmap(){
		return pixmap;
	}
	
	public void setLocation(Point pt){
		location = pt;
	}
	
	public void setLocation(int i, int j){
		location.setX(i);
		location.setY(j);
	}
	
	public abstract void tryToMove(Point pt);
}
