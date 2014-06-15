package at.meinedomain.CheckIt.Pieces;

import java.util.ArrayList;

import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.Pieces.AbstractPiece.SlideType;

public class SlidingPiece extends AbstractPiece {

	protected ArrayList<SlideType> slideTypes;
	
	protected SlidingPiece(Board b, Color c, Point pt, String tag){
		super(b, c, pt, tag);
	}
	
	@Override
	public boolean attacks(Point tile, Point notConsidering,
			Point insteadConsidering) {
		return canSlide(tile, notConsidering, insteadConsidering);
	}
	
	// get slide type
	public SlideType slideType(Point to){
		if(to.equals(location)){ // DO NOT DELETE!
			return null;
		}
		else if(isOnSameRank(to)){
			return SlideType.HORIZONTAL;
		}
		else if(isOnSameFile(to)){
			return SlideType.VERTICAL;
		}
		else if(isOnSameUpwardDiag(to)){
			return SlideType.UPWARD;
		}
		else if(isOnSameDownwardDiag(to)){
			return SlideType.DOWNWARD;
		}
		else{
			return null;
		}
	}
	
	public boolean canSlide(Point to){
		return canSlide(to, null, null);
	}
	
	
	public boolean canSlide(Point to, 
							Point oppFrom,  Point oppTo){
		SlideType st = slideType(to);
		
		if(!slideTypes.contains(st)){
			return false;
		}
		else if(isOccupiedByMe(to) && !to.equals(oppTo)){
			return false;
		}
		
		// move legal if no pieces between from and to
		else if(st == SlideType.HORIZONTAL){
			if(!noPiecesBetween(location, to, SlideType.HORIZONTAL,
								oppFrom, oppTo)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.VERTICAL){
			if(!noPiecesBetween(location, to, SlideType.VERTICAL,
								oppFrom, oppTo)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.UPWARD){
			if(!noPiecesBetween(location, to, SlideType.UPWARD,
								oppFrom, oppTo)){
				return false;
			}
			return true;
		}
		else{ // st==SlideType.DOWNWARD
			if(!noPiecesBetween(location, to, SlideType.DOWNWARD,
								oppFrom, oppTo)){
				return false;
			}
			return true;
		}
	}

	// oppFrom (oppTo) is the point the opponent moves from (to). So we 
	// ignore oppFrom and consider oppTo in addition to the rest of the board.
	public boolean noPiecesBetween(Point from, Point to, SlideType st,
								   Point oppFrom, Point oppTo){ // TODO TODO TODO
		if(st == SlideType.HORIZONTAL){
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(!board.emptyAfterOppMove(
									new Point(from.getX()+i*dir, from.getY()), 
									oppFrom, oppTo)){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.VERTICAL){
			int dir = verticalDiff(to)/verticalDist(to);	
			for(int j=1; j<verticalDist(to); j++){
				if(!board.emptyAfterOppMove(
						new Point(from.getX(), from.getY()+j*dir), 
						oppFrom, oppTo)){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.UPWARD){
			// we consider only x difference because y=x when upward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(!board.emptyAfterOppMove(
						new Point(from.getX()+i*dir, from.getY()+i*dir), 
						oppFrom, oppTo)){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.DOWNWARD){
			// we consider only x because x=-y when downward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(!board.emptyAfterOppMove(
						new Point(from.getX()+i*dir, from.getY()-i*dir), 
						oppFrom, oppTo)){
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
