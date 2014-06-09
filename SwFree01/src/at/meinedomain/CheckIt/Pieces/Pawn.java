package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class Pawn extends AbstractPiece {
	
	private int direction;

	public Pawn(Board b, Color c, Point pt){
		super(b, c, pt);
		if(c == Color.WHITE){
			pixmap = Assets.wp;
			direction = 1;
		} else{
			pixmap = Assets.bp;
			direction = -1;
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
//		// TODO the normal checks + check for en passant (see tryToMove() in Piece in the DD)
//
//	}

}
