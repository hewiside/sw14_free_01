package at.meinedomain.CheckIt.Pieces;

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
	
	protected MoveType CanMove(Point pt){
		return MoveType.NORMAL;
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
		MoveType mt = CanMove(to); 
		
		if(mt == MoveType.ILLEGAL){
			// nothing to do
		}
		if(mt == MoveType.NORMAL){
			board.move(location, to);
		}
		return mt;
	}
	
	// UTILITY METHODS:---------------------------------------------------------
	
	// test tiles for pieces:
	public boolean isEmpty(Point to){
		return (board.pieceAt(to) == null) ? true : false;
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
	public boolean isOnSameFile(Point to){
		return to.getX() == location.getX();
	}
	
	public boolean isOnSameRank(Point to){
		return to.getY() == location.getY();
	}
	
	public boolean isOnSameUpwardDiagonal(Point to){
		return to.getX()-location.getX() == to.getY()-location.getY();
	}
	
	public boolean isOnSameDownwardDiagonal(Point to){
		return to.getX()-location.getX() == -(to.getY()-location.getY());
	}
	
	
	// TODO TODO TODO
//	public boolean canSlide(Point from, Point to){
//		int x = from.getX();
//		int y = from.getY();
//		
//		if(x == to.getX()){
//			if(y == to.getY()){
//				return false;	// from==to
//			}
//			// VERTICAL ||||||||||||||||||||||||||||||||||||||||||||||||||||||||
//			else{
//				// TODO test between
////				int diff = to.getY()-y;
////				int offset = Math.abs(diff);
////				int dir = diff/offset;
////				
////				// test, if tiles between from and to are empty
////				for(int j=1; j<offset; j++){
////					if(board.pieceAt(x, y+j) != null){
////						return false;
////					}
////				}
//				// TODO test, if goal is occupied by our own piece
//				
//			}
//		}
//		else{
//			
//		}
//		
//		return false;
//	}
	
	
	// TODO TODO TODO use the utility functions from above (isEmpty(),...)
	public boolean noPiecesBetween(Point from, Point to, SlideType st){
		if(st == SlideType.HORIZONTAL){
			int diff = to.getX()-from.getX();
			int offset = Math.abs(diff);
			int dir = diff/offset;
			for(int i=1; i<offset; i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.VERTICAL){
			int diff = to.getY()-from.getY();
			int offset = Math.abs(diff);
			int dir = diff/offset;	
			for(int j=1; j<offset; j++){
				if(board.pieceAt(from.getX(), from.getY()+j*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.UPWARD){
			int diff = to.getX()-from.getX(); // we use x (y=x when upward)
			int offset = Math.abs(diff);
			int dir = diff/offset;
			for(int i=1; i<offset; i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()+i*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.DOWNWARD){
			int diff = to.getX()-from.getX(); // we use x (x=-y when downward)
			int offset = Math.abs(diff);
			int dir = diff/offset;
			for(int i=1; i<offset; i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()-i*dir) != null){ // note the minus!
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void log(Point to, MoveType mt){
		Log.i(TAG, "from: "+location.getX()+","+location.getY()+
				   "; to: "+      to.getX()+","+      to.getY()+","+mt);
	}
}
