package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class Rook extends AbstractPiece {

	public Rook(Board b, Color c, Point pt){
		super(b, c, pt);
		if(c == Color.WHITE){
			pixmap = Assets.wr;
		} else{
			pixmap = Assets.br;
		}
	}
	
	@Override
	protected MoveType CanMove(Point pt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tryToMove(Point pt) {
		// TODO Auto-generated method stub

	}

}
