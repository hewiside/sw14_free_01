package at.meinedomain.CheckIt.Pieces;

import java.util.ArrayList;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.Pieces.AbstractPiece.SlideType;

public class Rook extends SlidingPiece {
	
	public Rook(Board b, Color c, Point pt){
		super(b, c, pt, "ROOK");
		if(c == Color.WHITE){
			pixmap = Assets.wr;
		} else{
			pixmap = Assets.br;
		}
		slideTypes = new ArrayList<>();
		slideTypes.add(SlideType.HORIZONTAL);
		slideTypes.add(SlideType.VERTICAL);
	}
	
	@Override
	protected MoveType canMove(Point to) {
		
		if(canSlide(to)){
			if(isOccupiedByOpponent(to)){
				return MoveType.CAPTURE;
			}
			else{
				return MoveType.NORMAL;
			}
		}
		else{
			return MoveType.ILLEGAL;
		}
	}
//
//	@Override
//	public MoveType tryToMove(Point to) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
