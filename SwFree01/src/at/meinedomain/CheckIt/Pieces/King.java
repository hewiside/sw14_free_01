package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class King extends AbstractPiece {

	public King(Board b, Color c, Point pt){
		super(b, c, pt, "KING");
		if(c == Color.WHITE){
			pixmap = Assets.wk;
		} else{
			pixmap = Assets.bk;
		}
	}
	
	@Override
	protected MoveType canMove(Point to) {
		
		
		if(!to.equals(location) &&
		   horizontalDist(to)<=1 &&
		   verticalDist(to)  <=1 ){
			
			return MoveType.NORMAL;
		}
		return MoveType.ILLEGAL;
	}
//
//	@Override
//	public void tryToMove(Point pt) {
//		// TODO Auto-generated method stub
//
//	}

}
