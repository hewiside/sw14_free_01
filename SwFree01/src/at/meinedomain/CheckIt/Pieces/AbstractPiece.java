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
		if(mt == MoveType.NORMAL){
			board.move(location, to);
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
	
	public boolean isEnPassant(Point to){
		return board.getEnPassant() != null && to.equals(board.getEnPassant());
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
	
	// get slide type
	public SlideType slideType(Point to){
		if(to.equals(location)){
			return null;
		}
		else if(isOnSameRank(to)){
			return SlideType.HORIZONTAL;
		}
		else if(isOnSameFile(to)){
			return SlideType.VERTICAL;
		}
		else if(isOnSameUpwardDiag(to)){
			return SlideType.UPWARD;
		}
		else if(isOnSameDownwardDiag(to)){
			return SlideType.DOWNWARD;
		}
		else{
			return null;
		}
	}
	
	
	// TODO TODO TODO
	public boolean canSlide(Point from, Point to, ArrayList<SlideType> stList){
		SlideType st = slideType(to);
		
		if(!stList.contains(st)){
			return false;
		}
		else if(isOccupiedByMe(to)){
			return false;
		}
		
		// move legal if no pieces between from and to
		else if(st == SlideType.HORIZONTAL){
			if(!noPiecesBetween(from, to, SlideType.HORIZONTAL)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.VERTICAL){
			if(!noPiecesBetween(from, to, SlideType.VERTICAL)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.UPWARD){
			if(!noPiecesBetween(from, to, SlideType.UPWARD)){
				return false;
			}
			return true;
		}
		else{ // st==SlideType.DOWNWARD
			if(!noPiecesBetween(from, to, SlideType.DOWNWARD)){
				return false;
			}
			return true;
		}
	}

	
	
	// TODO TODO TODO use the utility functions from above (isEmpty(),...)
	public boolean noPiecesBetween(Point from, Point to, SlideType st){
		if(st == SlideType.HORIZONTAL){
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.VERTICAL){
			int dir = verticalDiff(to)/verticalDist(to);	
			for(int j=1; j<verticalDist(to); j++){
				if(board.pieceAt(from.getX(), from.getY()+j*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.UPWARD){
			// we consider only x difference because y=x when upward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()+i*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.DOWNWARD){
			// we consider only x because x=-y when downward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()-i*dir) != null){ // note the minus!
					return false;
				}
			}
			Log.wtf("DOWNWARD to", ""+to.getX()+","+to.getY());
			return true;
		}
		return false;
	}
	
	public void log(Point to, MoveType mt){
		Log.i(TAG, "from: "+location.getX()+","+location.getY()+
				   "; to: "+      to.getX()+","+      to.getY()+","+mt);
	}
	
	// this *public* method is intended for testing purposes and for the board
	// to check wether a move leaves us in check or discover check-/stale-mate.
	public boolean canMoveTest(int toX, int toY){
		return canMove(new Point(toX, toY)) == MoveType.ILLEGAL ? false : true;
	}
}
