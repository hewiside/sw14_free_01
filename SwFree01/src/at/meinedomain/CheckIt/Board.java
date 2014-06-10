package at.meinedomain.CheckIt;

import android.util.Log;
import at.meinedomain.CheckIt.Pieces.*;

public class Board {
	public enum MatchState{
		RUNNING,
		WON,
		LOST,
		DRAW
	}
	
	private SendMoveListener sendMoveListener;
	private Color player;
	private MatchState matchState;
	private int width;
	private int height;
	private AbstractPiece[][] board;
	private Color turn;
	private Point markedPoint;
	private Point markedPointOpponent;
	private Point enPassant;
	
	public Board(SendMoveListener sml, Color player){
		this.sendMoveListener = sml;
		this.player = player;
		matchState = MatchState.RUNNING;
		width = 8;
		height = 8;
		turn = Color.WHITE;
		markedPoint = null;
		markedPointOpponent = null;
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
	
	// for rook-placing (castling) and piece-placing (pawn reaches last rank)
	public void placePiece(Point from, Point to){
		pieceAt(from).setLocation(to);
		
		board[  to.getX()][  to.getY()] = pieceAt(from); 
		board[from.getX()][from.getY()] = null;
	}
	
	// move without testing for correctness of the move.
	public void move(Point from, Point to){
		move(from, to, null);
	}
	public void move(Point from, Point to, Point ep){
		enPassant = ep;
		if(turn.equals(player)){
			sendMoveListener.sendMove(new Move(from, to));
			markedPointOpponent = null;
		}
		else{
			markedPointOpponent = to;
		}
		Log.d("Board", "now placePiece() with from.x="+from.getX()+", from.y="+from.getY());
		placePiece(from, to);
		markedPoint = null;
		
		turn = (turn.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
	}
	
	public void tryToMove(Point from, Point to){
		AbstractPiece tempPiece = pieceAt(from);
		if(tempPiece != null){
			tempPiece.tryToMove(to);
		}
		else{
			Log.wtf("Board", "Trying to move null!");
		}
	}
	
	public Point getEnPassant(){
		return enPassant;
	}
	
	public Point getMarkedPoint(){
		return markedPoint;
	}
	
	public Point getMarkedPointOpponent(){
		return markedPointOpponent;
	}
	
	public MatchState getMatchState(){
		return matchState;
	}
	
	public Color getTurn(){
		return turn;
	}
	
	public void setMarkedPoint(Point P){
		markedPoint = P;
	}
	
	public void setMatchState(MatchState ms){
		matchState = ms;
	}
	public void toggleTurn(){
		turn = (turn==Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
}
