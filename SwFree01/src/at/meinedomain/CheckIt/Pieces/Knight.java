package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class Knight extends AbstractPiece {

	public Knight(Board b, Color c, Point pt){
		super(b, c, pt, "KNIGHT");
		if(c == Color.WHITE){
			pixmap = Assets.wn;
		} else{
			pixmap = Assets.bn;
		}
	}
	
	@Override
	protected MoveType canMove(Point to) {
		// TODO test if move leaves us in check
		
		if(attacks(to)) {
			
			if(isEmpty(to)){
				return MoveType.NORMAL;
			}
			if(isOccupiedByOpponent(to)){
				return MoveType.CAPTURE;
			}
		}
		return MoveType.ILLEGAL;
	}
	
	@Override
	public boolean attacks(Point tile, Point not, Point instead){
		
		int distX = horizontalDist(tile);
		int distY = verticalDist(tile);
		
		if(Math.max(distX, distY) == 2 && 
		   Math.min(distX, distY) == 1 ){
			
			   return true;
		}
		return false;
	}

	
//  // tryToMove() not needed. super-implementation sufices.
//	@Override
//	public void tryToMove(Point pt) {
//		// TODO Auto-generated method stub
//
//	}

}
