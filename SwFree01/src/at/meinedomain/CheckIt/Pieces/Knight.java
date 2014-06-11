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
	protected MoveType CanMove(Point to) {
		int distX = horizontalDist(to);
		int distY = verticalDist(to);
		
		if(Math.max(distX, distY) == 2 && 
		   Math.min(distX, distY) == 1){
			
			return MoveType.NORMAL;
		}
		return MoveType.ILLEGAL;
	}

	
//  // tryToMove() not needed. super-implementation sufices.
//	@Override
//	public void tryToMove(Point pt) {
//		// TODO Auto-generated method stub
//
//	}

}
