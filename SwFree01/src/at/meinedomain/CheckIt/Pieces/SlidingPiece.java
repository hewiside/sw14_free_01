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
		// TODO Auto-generated method stub
		return false;
	}
	
	// get slide type
	public SlideType slideType(Point to){
		if(to.equals(location)){
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
	
	public boolean canSlide(Point from, Point to, ArrayList<SlideType> stList){
		return canSlide(from, to, null, null, stList);
	}
	
	
	public boolean canSlide(Point from, Point to, 
							Point not,  Point instead,
							ArrayList<SlideType> stList){
		SlideType st = slideType(to);
		
		if(!stList.contains(st)){
			return false;
		}
		else if(isOccupiedByMe(to)){
			return false;
		}
		
		// move legal if no pieces between from and to
		else if(st == SlideType.HORIZONTAL){
			if(!noPiecesBetween(from, to, SlideType.HORIZONTAL)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.VERTICAL){
			if(!noPiecesBetween(from, to, SlideType.VERTICAL)){
				return false;
			}
			return true;
		}
		else if(st == SlideType.UPWARD){
			if(!noPiecesBetween(from, to, SlideType.UPWARD)){
				return false;
			}
			return true;
		}
		else{ // st==SlideType.DOWNWARD
			if(!noPiecesBetween(from, to, SlideType.DOWNWARD)){
				return false;
			}
			return true;
		}
	}

	
	public boolean noPiecesBetween(Point from, Point to, SlideType st){
		if(st == SlideType.HORIZONTAL){
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.VERTICAL){
			int dir = verticalDiff(to)/verticalDist(to);	
			for(int j=1; j<verticalDist(to); j++){
				if(board.pieceAt(from.getX(), from.getY()+j*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.UPWARD){
			// we consider only x difference because y=x when upward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()+i*dir) != null){
					return false;
				}
			}
			return true;
		}
		
		else if(st == SlideType.DOWNWARD){
			// we consider only x because x=-y when downward
			int dir = horizontalDiff(to)/horizontalDist(to);
			for(int i=1; i<horizontalDist(to); i++){
				if(board.pieceAt(from.getX()+i*dir, from.getY()-i*dir) != null){ // note the minus!
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
