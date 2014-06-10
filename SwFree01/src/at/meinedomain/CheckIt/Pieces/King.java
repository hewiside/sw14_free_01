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
	
//	@Override
//	protected MoveType CanMove(Point pt) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void tryToMove(Point pt) {
//		// TODO Auto-generated method stub
//
//	}

}
