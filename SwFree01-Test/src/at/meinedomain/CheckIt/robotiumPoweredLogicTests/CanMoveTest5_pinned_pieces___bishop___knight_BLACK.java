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

public class CanMoveTest5_pinned_pieces___bishop___knight_BLACK extends
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
	public CanMoveTest5_pinned_pieces___bishop___knight_BLACK() {
		super("at.meinedomain.CheckIt.CheckItGame", CheckItGame.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO 
		player = Color.BLACK;
		
		board = new Board(null, player, player); // with null-board!
		// we need to initialize the board before pieces since they need a
		// board instance in their constructor. But we can't give the board
		// constructor with the AbstractPiece[][]-arg since there are no
		// pieces until the next statement.
		
		// TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
		pieces = new AbstractPiece[] {
				/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	bk(7,7),
				/*   */	/*   */	/*   */	bp(3,6),/*   */	/*   */	bb(6,6),bn(7,6),
				/*   */	bn(1,5),/*   */	/*   */	/*   */	/*///*/	/*   */	/*|||*/
				/*   */	bb(1,4),/*   */	/*   */	wb(4,4),/*   */	/*   */	/*|||*/
				/*   */	/*   */	wp(2,3),/*   */	/*   */	/*   */	/*   */	/*|||*/	
				/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	/*|||*/
				/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	wr(7,1),
				/*   */	/*   */	/*   */	/*   */	/*   */	/*   */	wk(6,0) /*   */	
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
		assertFalse(board.isInCheck(Color.BLACK));
	}
	
	public void testPinnedKnight(){
		assertBoardNotNull();
		Knight knight = (Knight) board.pieceAt(7, 6);
		
		boolBoard = initializeBooleanBoard();
		// no setTrueTile() because the knight is pinned!
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(knight)+" can move to "+i+","+j, 
							   knight.canMoveTest(i,j));
				}
				else{
					assertFalse(knight.canMoveTest(i, j));
				}
			}
		}
	}
	
	public void testPinnedBishop(){
		assertBoardNotNull();
		Bishop bishop = (Bishop) board.pieceAt(6, 6);
		
		boolBoard = initializeBooleanBoard();		
		setTrueTile(5,5); // must stay in the diagonal of checking white bishop!
		setTrueTile(4,4);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(bishop)+" can move to "+i+","+j, 
							   bishop.canMoveTest(i,j));
				}
				else{
					assertFalse("I shall not go to "+i+","+j,
								bishop.canMoveTest(i, j));
				}
			}
		}
	}
	
	public void testNotPinnedBishop(){
		assertBoardNotNull();
		Bishop bishop = (Bishop) board.pieceAt(1, 4);
		
		boolBoard = initializeBooleanBoard();
		setTrueTile(0, 5);
		setTrueTile(0, 3);
		setTrueTile(2, 3);
		setTrueTile(2, 5);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(bishop)+" can move to "+i+","+j, 
							   bishop.canMoveTest(i,j));
				}
				else{
					assertFalse("I shall not go to "+i+","+j,
								bishop.canMoveTest(i, j));
				}
			}
		}
	}
	
	public void testNotPinnedKnight(){
		assertBoardNotNull();
		Knight knight = (Knight) board.pieceAt(1, 5);
		
		boolBoard = initializeBooleanBoard();
		setTrueTile(0, 7);
		setTrueTile(0, 3);
		setTrueTile(2, 3);
		setTrueTile(3, 4);
//		setTrueTile(3, 6); (Occupied by our own pawn)
		setTrueTile(2, 7);
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(boolBoard[i][j] == true){
					assertTrue(pieceToString(knight)+" can move to "+i+","+j, 
							   knight.canMoveTest(i,j));
				}
				else{
					assertFalse(knight.canMoveTest(i, j));
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