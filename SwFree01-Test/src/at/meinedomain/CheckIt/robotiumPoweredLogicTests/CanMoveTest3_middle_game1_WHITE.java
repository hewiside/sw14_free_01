package at.meinedomain.CheckIt.robotiumPoweredLogicTests;

import static org.junit.Assert.fail;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.Pieces.AbstractPiece;
import at.meinedomain.CheckIt.Pieces.Bishop;
import at.meinedomain.CheckIt.Pieces.King;
import at.meinedomain.CheckIt.Pieces.Knight;
import at.meinedomain.CheckIt.Pieces.Pawn;
import at.meinedomain.CheckIt.Pieces.Queen;
import at.meinedomain.CheckIt.Pieces.Rook;

public class CanMoveTest3_middle_game1_WHITE extends
		ActivityInstrumentationTestCase2<CheckItGame> {

//	private Solo solo;
	public int width  = 8;
	public int height = 8;
	
	protected AbstractPiece[][] b;
	protected boolean[][] boolBoard;
	protected Board board;
	protected AbstractPiece[] pieces;
	protected Color player;
	
	// SEE DRAWABLE-FOLDER TO SEE THE BOARD-CONFIGURATION
	public CanMoveTest3_middle_game1_WHITE() {
		super("at.meinedomain.CheckIt.CheckItGame", CheckItGame.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO 
		player = Color.WHITE;
		
		board = new Board(null, player, player); // with null-board!
		// we need to initialize the board before pieces since they need a
		// board instance in their constructor. But we can't give the board
		// constructor with the AbstractPiece[][]-arg since there are no
		// pieces until the next statement.
		
		// TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
		pieces = new AbstractPiece[] {
														br(5,7),		bk(7,7),
						bp(1,6),								bp(6,6),bp(7,6),
				bp(0,5),
						bq(1,4),		wq(3,4),wn(4,4),
				
						wp(1,2),
				wp(0,1),						bp(4,1),/*---*/	wp(6,1),wp(7,1),
												wr(4,0),/*---*/	wk(6,0)
				};
		
		b = initializeBoard();
		fillBoardWithPieces(b, pieces);
		Log.d("AbstractCanMoveTest", "Board matrix initialized:"+"\n"+
									 boardMatrixToString(b));
		
		board.setBoard(b); // now the board is not null anymore.
	}
	
	// =========================================================================
	// TESTS begin =============================================================
	// =========================================================================
	// TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO	
	public void testIfNotCheck(){
		assertFalse(board.isInCheck(Color.WHITE));
	}
	
	public void testKing(){
		assertBoardNotNull();
		King king = (King) board.pieceAt(6, 0);
		
		boolBoard = initializeBooleanBoard();
		setTrueTile(7, 0);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(king)+" can move to "+i+","+j, 
							   king.canMoveTest(i,j));
				}
				else{
					assertFalse(king.canMoveTest(i, j));
				}
			}
		}
	}
	
	public void testRookE1(){
		assertBoardNotNull();
		Rook rook = (Rook) board.pieceAt(4, 0);
		
		boolBoard = initializeBooleanBoard();		
		setTrueTile(0, 0);
		setTrueTile(1, 0);
		setTrueTile(2, 0);
		setTrueTile(3, 0);
		setTrueTile(5, 0);
		setTrueTile(4, 1);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(rook)+" can move to "+i+","+j, 
							   rook.canMoveTest(i,j));
				}
				else{
					assertFalse(rook.canMoveTest(i, j));
				}
			}
		}
	}
	
	public void testQueen(){
		assertBoardNotNull();
		Queen queen = (Queen) board.pieceAt(3, 4);
		
		boolBoard = initializeBooleanBoard();
		setTrueTile(3, 0);
		setTrueTile(3, 1);
		setTrueTile(3, 2);
		setTrueTile(3, 3);
		setTrueTile(3, 5);
		setTrueTile(3, 6);
		setTrueTile(3, 7);
		setTrueTile(2, 4);
		setTrueTile(1, 4);
		setTrueTile(2, 3);
		setTrueTile(4, 3);
		setTrueTile(5, 2);
		setTrueTile(4, 5);
		setTrueTile(5, 6);
		setTrueTile(6, 7);
		setTrueTile(2, 5);
		setTrueTile(1, 6);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(queen)+" can move to "+i+","+j, 
							   queen.canMoveTest(i,j));
				}
				else{
					assertFalse(queen.canMoveTest(i, j));
				}
			}
		}
	}
	
	// =========================================================================
	// TESTS end ===============================================================
	// =========================================================================
	
	public void assertBoardNotNull(){
		if(board==null){
			fail("No board object initialised in setUp()!!!");
		}
		else if(b==null){
			fail("No board member initialised in setUp()!!!");
		}
	}
	
	public boolean[][] initializeBooleanBoard(){
		boolean[][] b = new boolean[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				b[i][j] = false;
			}
		}
		return b;
	}
	
	public void setTrueTile(int x, int y){
		boolBoard[x][y] = true;
	}
	
	public AbstractPiece[][] initializeBoard(){
		AbstractPiece[][] b = new AbstractPiece[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				b[i][j] = null;
			}
		}
		return b;
	}
	
	public void fillBoardWithPieces(AbstractPiece[][] b, AbstractPiece[] pieces){
		for(AbstractPiece p : pieces){
			b[p.getLocation().getX()][p.getLocation().getY()] = p;
		}
	}
	
	public String boardMatrixToString(AbstractPiece[][] b){
		StringBuilder message = new StringBuilder();
		for(int j=height-1; j>=0; j--){
			for(int i=0; i<width; i++){
				message.append(pieceToString(b[i][j]));
			}
			message.append("\n");
		}
		return message.toString();
	}
	
	public String boardToString(Board board){
		StringBuilder message = new StringBuilder();
		for(int j=height-1; j>=0; j--){
			for(int i=0; i<width; i++){
				message.append(pieceToString(board.pieceAt(i,j)));
			}
			message.append("\n");
		}
		return message.toString();
	}
	
	public String pieceToString(AbstractPiece p){
		if(p==null)
			return "-";
		else if(p instanceof Pawn){
			return p.getColor()==Color.WHITE ? "P" : "p";
		}
		else if(p instanceof Rook){
			return p.getColor()==Color.WHITE ? "R" : "r";
		}
		else if(p instanceof Knight){
			return p.getColor()==Color.WHITE ? "N" : "n";
		}
		else if(p instanceof Bishop){
			return p.getColor()==Color.WHITE ? "B" : "b";
		}
		else if(p instanceof Queen){
			return p.getColor()==Color.WHITE ? "Q" : "q";
		}
		else if(p instanceof King){
			return p.getColor()==Color.WHITE ? "K" : "k";
		}
		else return "???";
	}
	
	// Abbreviations============================================================
	public Pawn wp(int x, int y){
		return new Pawn(board, Color.WHITE, new Point(x,y));
	}
	public Pawn bp(int x, int y){
		return new Pawn(board, Color.BLACK, new Point(x,y));
	}
	public Rook wr(int x, int y){
		return new Rook(board, Color.WHITE, new Point(x,y));
	}
	public Rook br(int x, int y){
		return new Rook(board, Color.BLACK, new Point(x,y));
	}	
	public Knight wn(int x, int y){
		return new Knight(board, Color.WHITE, new Point(x,y));
	}
	public Knight bn(int x, int y){
		return new Knight(board, Color.BLACK, new Point(x,y));
	}		
	public Bishop wb(int x, int y){
		return new Bishop(board, Color.WHITE, new Point(x,y));
	}
	public Bishop bb(int x, int y){
		return new Bishop(board, Color.BLACK, new Point(x,y));
	}	
	public Queen wq(int x, int y){
		return new Queen(board, Color.WHITE, new Point(x,y));
	}
	public Queen bq(int x, int y){
		return new Queen(board, Color.BLACK, new Point(x,y));
	}
	public King wk(int x, int y){
		return new King(board, Color.WHITE, new Point(x,y));
	}
	public King bk(int x, int y){
		return new King(board, Color.BLACK, new Point(x,y));
	}
}