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
		
		if(board.leavesInCheck(color, location, to)){
			return MoveType.ILLEGAL;
		}
		
		if(attacks(to)){
			
			if(isEmpty(to)){
				return MoveType.NORMAL;
			}
			if(isOccupiedByOpponent(to)){
				return MoveType.CAPTURE;
			}
		}
		return MoveType.ILLEGAL;
	}
//
//	@Override
//	public void tryToMove(Point pt) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public boolean attacks(Point tile, Point not, Point instead){
		if(!tile.equals(location) &&
		   horizontalDist(tile)<=1 &&
		   verticalDist(tile)  <=1 ){
			
			return true;
		}
		return false;
	}
}
