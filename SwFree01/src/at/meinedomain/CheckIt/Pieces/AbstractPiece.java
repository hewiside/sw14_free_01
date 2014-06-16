package at.meinedomain.CheckIt.Pieces;

import java.util.ArrayList;

import android.util.Log;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class AbstractPiece {
	protected Board board;
	protected Color color;
	protected Point location;
	protected Pixmap pixmap;
	protected String TAG; // for logging
	
	protected enum SlideType{
		HORIZONTAL,
		VERTICAL,
		UPWARD,		// diagonal from left down to right up
		DOWNWARD	// diagonal from right up to left down
	}

	public AbstractPiece(Board b, Color col, Point pt, String tag){
		board = b;
		color = col;
		location = pt;
		TAG = tag;
	}
	
	protected MoveType canMove(Point to){
		Log.wtf("AbstractPiece", 
				"Don't call the implementation of the abstract class!");
		return MoveType.ILLEGAL;
	}
	
	public boolean canMoveSomewhere(){
		for(int i=0; i<board.getWidth(); i++){
			for(int j=0; j<board.getHeight(); j++){
				if(canMoveTest(i,j)){
					return true;
				}
			}
		}
		return false;
	}
	
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
	
	// return the MoveType because Overriding methods which check for further
	//			move types don't need to call the expensive CanMove() again.
	public MoveType tryToMove(Point to){
		MoveType mt = canMove(to); 
		
		log(to, mt);
		
		if(mt == MoveType.ILLEGAL){
			// nothing to do
		}
		if(mt == MoveType.NORMAL || mt == MoveType.CAPTURE){
			board.move(location, to, mt);
		}
		return mt;
	}
	
	// UTILITY METHODS:---------------------------------------------------------
	
	// test tiles for emptiness, pieces, or en passant:
	public boolean isEmpty(Point to){
		return board.isEmpty(to);
	}
	
	public boolean isEmpty(int toX, int toY){
		return board.isEmpty(toX, toY);
	}
	
	public boolean isOccupiedByOpponent(Point to){
		if(!isEmpty(to) && board.pieceAt(to).getColor() != color){
			return true;
		}
		return false;
	}
	
	public boolean isOccupiedByMe(Point to){
		if(!isEmpty(to) && board.pieceAt(to).getColor() == color){
			return true;
		}
		return false;
	}
	
	
	// test same file, rank, diagonal
	public boolean isOnSameLine(Point to){
		return isOnSameFile(to) || isOnSameRank(to);
	}
	
	public boolean isOnSameDiag(Point to){
		return isOnSameUpwardDiag(to) || isOnSameDownwardDiag(to);
	}
	
	public boolean isOnSameFile(Point to){
		return to.getX() == location.getX();
	}
	
	public boolean isOnSameRank(Point to){
		return to.getY() == location.getY();
	}
	
	public boolean isOnSameUpwardDiag(Point to){
		return to.getX()-location.getX() == to.getY()-location.getY();
	}
	
	public boolean isOnSameDownwardDiag(Point to){
		return to.getX()-location.getX() == -(to.getY()-location.getY());
	}
	
	
	// horizontal and vertical differences (signed) / distances (positive)
	public int horizontalDiff(Point to){
		return to.getX()-location.getX();
	}
	
	public int verticalDiff(Point to){
		return to.getY()-location.getY();
	}
	
	public int horizontalDist(Point to){
		return Math.abs(horizontalDiff(to));
	}
	
	public int verticalDist(Point to){
		return Math.abs(verticalDiff(to));
	}

	
	
	
	public void log(Point to, MoveType mt){
		Log.i(TAG, "from: "+location.getX()+","+location.getY()+
				   "; to: "+      to.getX()+","+      to.getY()+","+mt);
	}
	
	// this *public* method is intended for testing purposes and for the board
	// to check whether a move leaves us in check or discover check-/stale-mate.
	public boolean canMoveTest(int toX, int toY){
		return canMove(new Point(toX, toY)) == MoveType.ILLEGAL ? false : true;
	}

	public boolean attacks(Point tile){
		return attacks(tile, null, null);
	}
	public abstract boolean attacks(Point tile, Point notConsidering, 
									  Point insteadConsidering);
}
