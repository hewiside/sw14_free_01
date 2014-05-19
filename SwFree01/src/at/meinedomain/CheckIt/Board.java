package at.meinedomain.CheckIt;

import android.util.Log;
import at.meinedomain.CheckIt.Pieces.*;

public class Board {
	private int width;
	private int height;
	private AbstractPiece[][] board;
	Color turn;
	Point enPassant;
	
	public Board(){
		width = 8;
		height = 8;
		turn = Color.WHITE;
		enPassant = null;
		board = new AbstractPiece[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				board[i][j] = null;
			}
		}

		// init pawns
		for(int i=0; i<width; i++){
			board[i][1] = new Pawn(this, Color.WHITE, new Point(i,1));
			board[i][6] = new Pawn(this, Color.BLACK, new Point(i,6));
		}
		// init rooks
		board[0][0] = new Rook(this, Color.WHITE, new Point(0,0));
		board[7][0] = new Rook(this, Color.WHITE, new Point(7,0));
		board[0][7] = new Rook(this, Color.BLACK, new Point(0,7));
		board[7][7] = new Rook(this, Color.BLACK, new Point(7,7));
		//init knights
		board[1][0] = new Knight(this, Color.WHITE, new Point(1,0));
		board[6][0] = new Knight(this, Color.WHITE, new Point(6,0));
		board[1][7] = new Knight(this, Color.BLACK, new Point(1,7));
		board[6][7] = new Knight(this, Color.BLACK, new Point(6,7));
		//init bishops
		board[2][0] = new Bishop(this, Color.WHITE, new Point(2,0));
		board[5][0] = new Bishop(this, Color.WHITE, new Point(5,0));
		board[2][7] = new Bishop(this, Color.BLACK, new Point(2,7));
		board[5][7] = new Bishop(this, Color.BLACK, new Point(5,7));
		//init queen and king
		board[3][0] = new Queen(this, Color.WHITE, new Point(3,0));
		board[4][0] = new King(this, Color.WHITE, new Point(4,0));
		board[3][7] = new Queen(this, Color.BLACK, new Point(3,7));
		board[4][7] = new King(this, Color.BLACK, new Point(4,7));
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public AbstractPiece pieceAt(Point pt){
		return pieceAt(pt.getX(), pt.getY());
	}
	public AbstractPiece pieceAt(int i, int j){
		return board[i][j];
	}
	public void placePiece(AbstractPiece p, Point pt){
		// TODO
	}
	public void move(Point pt1, Point pt2){
		// TODO
	}
	public void move(Point pt1, Point pt2, Point ep){
		// TODO
	}
	public void tryToMove(Point pt1, Point pt2){
		// TODO
	}
	public Point getEnPassant(){
		return enPassant;
	}
}