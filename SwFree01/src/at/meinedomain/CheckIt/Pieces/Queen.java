package at.meinedomain.CheckIt.Pieces;

import java.util.ArrayList;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.Pieces.AbstractPiece.SlideType;

public class Queen extends SlidingPiece {
	
	public Queen(Board b, Color c, Point pt){
		super(b, c, pt, "QUEEN");
		if(c == Color.WHITE){
			pixmap = Assets.wq;
		} else{
			pixmap = Assets.bq;
		}
		slideTypes = new ArrayList<>();
		slideTypes.add(SlideType.HORIZONTAL);
		slideTypes.add(SlideType.VERTICAL);
		slideTypes.add(SlideType.UPWARD);
		slideTypes.add(SlideType.DOWNWARD);
	}
	
	@Override
	protected MoveType canMove(Point to) {
		
		if(board.leavesInCheck(color, location, to)){
			return MoveType.ILLEGAL;
		}
		
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
