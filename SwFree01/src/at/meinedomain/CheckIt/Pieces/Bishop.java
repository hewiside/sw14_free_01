package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class Bishop extends AbstractPiece {

	public Bishop(Board b, Color c, Point pt){
		super(b, c, pt, "BISHOP");
		if(c == Color.WHITE){
			pixmap = Assets.wb;
		} else{
			pixmap = Assets.bb;
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
