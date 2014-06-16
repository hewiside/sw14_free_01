package at.meinedomain.CheckIt.Pieces;

import java.util.ArrayList;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.Pieces.AbstractPiece.SlideType;

public class Rook extends SlidingPiece {
	
	private boolean hasntMoved;
	
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
		
		hasntMoved = true;
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
	
	public boolean getHasntMoved(){
		return hasntMoved;
	}

	@Override
	public MoveType tryToMove(Point to) {
		// TODO the normal checks + check for en passant (see tryToMove() in Piece in the DD)
		MoveType mt = super.tryToMove(to);

		if(mt == MoveType.NORMAL  ||
		   mt == MoveType.CAPTURE ){
			
			hasntMoved = false;
		}
		return mt;
	}
}
