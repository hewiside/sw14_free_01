package at.meinedomain.CheckIt.Pieces;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.MoveType;
import at.meinedomain.CheckIt.Point;

public class King extends AbstractPiece {

	private boolean hasntMoved;
	
	public King(Board b, Color c, Point pt){
		super(b, c, pt, "KING");
		if(c == Color.WHITE){
			pixmap = Assets.wk;
		} else{
			pixmap = Assets.bk;
		}
		hasntMoved = true;
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
		// White Castle
		if(to.getX()==6 && to.getY()==0){
			if(hasntMoved && !board.isInCheck(color) &&
			   isEmpty(to) && isEmpty(5,0) &&
			   board.pieceAt(7,0) instanceof Rook &&
			   ((Rook)board.pieceAt(7,0)).getHasntMoved() &&
			   !board.leavesInCheck(color, location, to) &&
			   !board.leavesInCheck(color, location, new Point(5,0))){
				
				return MoveType.CASTLE_KINGSIDE;
			}
		}
		if(to.getX()==2 && to.getY()==0){
			if(hasntMoved && !board.isInCheck(color) &&
			   isEmpty(to) && isEmpty(3,0) && isEmpty(1,0) &&
			   board.pieceAt(0,0) instanceof Rook &&
			   ((Rook)board.pieceAt(0,0)).getHasntMoved() &&
			   !board.leavesInCheck(color, location, to) &&
			   !board.leavesInCheck(color, location, new Point(3,0))){
				
				return MoveType.CASTLE_QUEENSIDE;
			}
		}
		// Black caslte
		if(to.getX()==6 && to.getY()==7){
			if(hasntMoved && !board.isInCheck(color) &&
			   isEmpty(to) && isEmpty(5,7) &&
			   board.pieceAt(7,7) instanceof Rook &&
			   ((Rook)board.pieceAt(7,7)).getHasntMoved() &&
			   !board.leavesInCheck(color, location, to) &&
			   !board.leavesInCheck(color, location, new Point(5,7))){
				
				return MoveType.CASTLE_KINGSIDE;
			}
		}
		if(to.getX()==2 && to.getY()==7){
			if(hasntMoved && !board.isInCheck(color) &&
			   isEmpty(to) && isEmpty(3,7) && isEmpty(1,7) &&
			   board.pieceAt(0,7) instanceof Rook &&
			   ((Rook)board.pieceAt(0,7)).getHasntMoved() &&
			   !board.leavesInCheck(color, location, to) &&
			   !board.leavesInCheck(color, location, new Point(3,7))){
				
				return MoveType.CASTLE_QUEENSIDE;
			}
		}
		
		return MoveType.ILLEGAL;
	}

	@Override
	public MoveType tryToMove(Point to) {
		// TODO the normal checks + check for en passant (see tryToMove() in Piece in the DD)
		MoveType mt = super.tryToMove(to);
		if(mt == MoveType.ILLEGAL){
			return mt;
		}
		
		if(mt == MoveType.NORMAL  ||
		   mt == MoveType.CAPTURE ){
			
			hasntMoved = false;
			return mt;
		}
		else if(mt == MoveType.CASTLE_KINGSIDE){
			// TODO TODO TODO TODO TODO
			
			if(color == Color.WHITE){
				board.move(location, new Point(6,0), mt);
			}
			if(color == Color.BLACK){
				board.move(location, new Point(6,7), mt);
			}
		}
		else if(mt == MoveType.CASTLE_QUEENSIDE){
			// TODO TODO TODO TODO TODO
			if(color == Color.WHITE){
				board.move(location, new Point(2,0), mt);
			}
			if(color == Color.BLACK){
				board.move(location, new Point(2,7), mt);
			}			
		}
		return mt;
	}

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
