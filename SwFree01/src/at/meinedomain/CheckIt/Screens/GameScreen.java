package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.ClientThread;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.ConnectionThread;
import at.meinedomain.CheckIt.Move;
import at.meinedomain.CheckIt.Point;
import at.meinedomain.CheckIt.R;
import at.meinedomain.CheckIt.SendMoveListener;
import at.meinedomain.CheckIt.ServerThread;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class GameScreen extends AbstractScreen implements SendMoveListener{
	
	private enum GameState{
		READY,
		MY_TURN,
		OPPONENTS_TURN,		
		GAME_OVER
	}
	
	GameState state = GameState.READY;
	
	private CheckItGame game;
	private Board board;
	private Color player;
	
	ConnectionThread connectionThread;
	
	float myTime;
	float opponentsTime;
	
	// members used in present()
	int lightTileOffset; // determines whether A8 is a light or dark tile?	
    int unit;
    int tileSize;
    int firstRankY;
    int boardTop;
    private static final int NUM_WIDTH = 135; // a number's width in the picture Assets.numbers
    private static final int NUM_HEIGHT = 180;
	private static final int COLON_WIDTH = 45; // width of colon in Assets.numbers 
	private final int HEIGHT; // of screen
	private final int WIDTH;
	private final int BOARD_SIZE_PX; // TODO change WIDTH to this int where appropriate
	private final int BUTTON_SIZE;
	private int colorTable;
	private int colorDark; 
	private int colorLight; 
	private int darkOverlay; 
	private int highlightOverlay;
	
    public GameScreen(Game game_) {
        super(game_);
        game = (CheckItGame) game_;
        
        colorTable = game.getResources().getColor(R.color.medium);
        colorDark  = game.getResources().getColor(R.color.dark);
        colorLight = game.getResources().getColor(R.color.light);
        darkOverlay = game.getResources().getColor(R.color.dark_overlay);
        highlightOverlay = game.getResources().getColor(R.color.selector_overlay);

        player = game.getPlayerColor(); // is set because this Screen is only
        		// set if MainMenu Screen recognizes a color in CheckItGame.
        board = new Board(this, player);
        
        myTime = 300; // TODO allow custom times
        opponentsTime = 300;
        
        lightTileOffset = 1;
        Log.d("GameScreen", "player plays with: "+player);
        
        HEIGHT = game.getGraphics().getHeight();
        WIDTH  = game.getGraphics().getWidth();
        BOARD_SIZE_PX = WIDTH;
        BUTTON_SIZE = Assets.buttonPlay.getWidth();
        unit = WIDTH/12;
        tileSize = WIDTH / board.getWidth();
        firstRankY = HEIGHT/2 + (board.getHeight()/2-1)*tileSize;
        boardTop   = HEIGHT/2 - (board.getHeight()/2)  *tileSize;
        
        if(player==Color.WHITE){
        	connectionThread = new ServerThread(board, this);
        }
        else{
        	connectionThread = new ClientThread(game.getWifiP2pInfo(), 
        										board, this);
        }
    	connectionThread.start();
    }   

	// overriden from AbstractScreen--------------------------------------------
    @Override
	public AbstractScreen.ScreenType getScreenType(){
    	return AbstractScreen.ScreenType.GAME_SCREEN;
    }
    
    // overriden from SendMoveListenerAndColorTeller----------------------------
    @Override
	public void sendMove(Move move){
    	connectionThread.setMove(move);
    }
    
    // overriden from Screen----------------------------------------------------
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        if(game.getIsBackPressed()) {
        	game.setIsBackPressed(false);
        	if(Settings.soundEnabled){
                //Assets.click.play(1);
        	}
        	connectionThread.requestStop();
        	game.setPlayerColor(null);
            game.setScreen(new MainMenuScreen(game)); 
            return;
        }
        
        if(state == GameState.READY){ 
            updateReady(deltaTime, touchEvents);
        }
        else if(state == GameState.MY_TURN){
            upateMyTurn(deltaTime, touchEvents);
        }
        else if(state == GameState.OPPONENTS_TURN){
            updateOpponentsTurn(deltaTime, touchEvents);
        }
        else if(state == GameState.GAME_OVER){
            updateGameOver(deltaTime, touchEvents);
        }
    }
    
    private void updateReady(float deltaTime, List<TouchEvent> touchEvents){
    	// for black player - if screen is tapped, trigger the sending of
    	// 					  START_TAG and set the state to OpponentsTurn.
    	if(player == Color.BLACK){
	    	for(int i = 0; i < touchEvents.size(); i++) {
	    		TouchEvent event = touchEvents.get(i);
	    		if(event.type == TouchEvent.TOUCH_UP){
	    			connectionThread.requestStart();
	    			state = GameState.OPPONENTS_TURN;
	    		}
	    	}
    	}
    	// for white player - check if black sent the START_TAG
    	else{
    		if(connectionThread.getStartRequested()){
    			state = GameState.MY_TURN;
    		}
    	}
    }
    private void upateMyTurn(float deltaTime, List<TouchEvent> touchEvents){
    	if(checkForGameOver()){
    		state = GameState.GAME_OVER;
    	}
    	else if(board.getTurn() != player){
    		state = GameState.OPPONENTS_TURN;
    	}
    	else{ // I am expected to move
    		myTime -= deltaTime;
	    	
    		for(int k = 0; k < touchEvents.size(); k++) {
	    		TouchEvent event = touchEvents.get(k);
	    		if(event.type == TouchEvent.TOUCH_UP){
	    			if(inBoard(event)){
	    				Point P = new Point(xToFile(event.x), yToRank(event.y));
	    				
	    				if(board.getMarkedPoint() != null){
	    					if(board.getMarkedPoint().equals(P)){
	    						board.setMarkedPoint(null);
	    					}
	    					else if(board.pieceAt(P) != null &&
	    							board.pieceAt(P).getColor() == player){
	    						board.setMarkedPoint(P);
	    					}
	    					else{
	    						board.tryToMove(board.getMarkedPoint(), P);
	    					}
	    				}
	    				else{ // markedPosition is still null
	    					if(board.pieceAt(P) != null && 
	    					   board.pieceAt(P).getColor() == player){
	    						board.setMarkedPoint(P);
	    					}
	    				}
	    			}
	    		}
	    	}
    	}
    }
    private void updateOpponentsTurn(float deltaTime, List<TouchEvent> touchEvents){
    	if(checkForGameOver()){
    		state = GameState.GAME_OVER;
    	}
    	
    	else if(board.getTurn() == null){
    		Log.wtf("GameScreen","Current color in board is null!");
    	}
    	else if(board.getTurn() == player){
    		state = GameState.MY_TURN;
    		// TODO opponentsTime = connectionThread.getOpponentsTime();
    	}
    	else{
    		opponentsTime -= deltaTime;
    	}
    }
    private void updateGameOver(float deltaTime, List<TouchEvent> touchEvents){
    	// TODO check if some replay-button (or back-button) has been clicked.
    }
    
    private boolean checkForGameOver(){
    	return (board.getMatchState()!=Board.MatchState.RUNNING) ? true : false;
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }
    
    private boolean inBoard(TouchEvent event){
    	return inBounds(event, 0, boardTop, BOARD_SIZE_PX, BOARD_SIZE_PX);
    }
    
    private Point getPoint(TouchEvent event){
    		return new Point(xToFile(event.x), yToRank(event.y));
    }
    
    private int xToFile(float x){
    	return (player == Color.WHITE) ? xToFileWhite(x) : xToFileBlack(x);
    }
    private int yToRank(float y){
    	return (player == Color.WHITE) ? yToRankWhite(y) : yToRankBlack(y);
    }
    private int fileToX(int file){
    	return (player == Color.WHITE) ? fileToXWhite(file) : fileToXBlack(file);
    }
    private int rankToY(int rank){
    	return (player == Color.WHITE) ? rankToYWhite(rank) : rankToYBlack(rank);
    }
    
    private int xToFileWhite(float x){
    	return (int) (x/tileSize);
    }
    private int yToRankWhite(float y){
    	return board.getHeight()-1-(int) ((y-boardTop)/tileSize);
    }
    private int fileToXWhite(int file){
    	return (int) (file*tileSize);
    }
    private int rankToYWhite(int rank){
    	return (int) (firstRankY - rank*tileSize);
    }
    private int xToFileBlack(float x){
		return board.getWidth()-1-(int)(x/tileSize);
	}
	private int yToRankBlack(float y){
		return (int) ((y-boardTop)/tileSize);
	}
	private int fileToXBlack(int file){
		return (int) ((board.getWidth()-1-file)*tileSize);
	}
	private int rankToYBlack(int rank){
		return (int) (boardTop + rank*tileSize);
	}    
	
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        drawBackground(g);
        drawBoard(g);
        drawTimes(g);
        
        if(state == GameState.READY){ 
            drawReadyUI(g);
        }
        else if(state == GameState.MY_TURN){
            drawMyTurnUI(g);
        }
        else if(state == GameState.OPPONENTS_TURN){
            drawOpponentsTurnUI(g);
        }
        else if(state == GameState.GAME_OVER){
            drawGameOverUI(g);
        }
    }
    
    private void drawBackground(Graphics g){
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), colorTable);
    }
    
    private void drawBoard(Graphics g){
        // Dark tiles
        g.drawRect(0, firstRankY - (board.getHeight()-1)*tileSize, 
        		   WIDTH, WIDTH, colorDark);
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Light tiles
        		if((i+j)%2 == lightTileOffset){
        			g.drawRect(i*tileSize, firstRankY - j*tileSize, 
        					   tileSize, tileSize, colorLight);
        		}
        	}
        }

    	// draw pieces
    	if(player == Color.WHITE){
    		drawWhitePiecesAndMarkedPoints(g);
    	}
    	else{
    		drawBlackPiecesAndMarkedPoints(g);
    	}
    }
    
    private void drawWhitePiecesAndMarkedPoints(Graphics g){
        // highlight overlay (markedPoint)
    	if(board.getMarkedPoint() != null){      	
        	g.drawRect(fileToXWhite(board.getMarkedPoint().getX()),
        			   rankToYWhite(board.getMarkedPoint().getY()),
        			   tileSize, tileSize, highlightOverlay);
        }
    	
    	// highlight overlay (markedPointOpponent)
    	if(board.getMarkedPointOpponent() != null){
        	g.drawRect(fileToXWhite(board.getMarkedPointOpponent().getX()), 
        			   rankToYWhite(board.getMarkedPointOpponent().getY()),
        			   tileSize, tileSize, highlightOverlay);
        }
    	
    	// pieces
    	for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Pieces
        		if(board.pieceAt(i,j) != null){
        			g.drawPixmap(board.pieceAt(i,j).getPixmap(), 
        						 fileToXWhite(i), rankToYWhite(j));
        		}
        	}
        }
    }    
    
	private void drawBlackPiecesAndMarkedPoints(Graphics g){
        // highlight overlay (markedPoint) TODO use the utility-methods!
    	if(board.getMarkedPoint() != null){
        	g.drawRect(fileToXBlack(board.getMarkedPoint().getX()),
        			   rankToYBlack(board.getMarkedPoint().getY()),
        			   tileSize, tileSize, highlightOverlay);
        }
    	
        // highlight overlay (markedPointOpponent) TODO use the utility-methods!
    	if(board.getMarkedPointOpponent() != null){
        	g.drawRect(fileToXBlack(board.getMarkedPointOpponent().getX()), 
        			   rankToYBlack(board.getMarkedPointOpponent().getY()),
        			   tileSize, tileSize, highlightOverlay);
        }
    	
    	//pieces
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Pieces
        		if(board.pieceAt(i,j) != null){
        			g.drawPixmap(board.pieceAt(i,j).getPixmap(), 
        					fileToXBlack(i), rankToYBlack(j));
        		}
        	}
        }
    }    
    
    private void drawTimes(Graphics g){
    	int x = WIDTH/2-2*NUM_WIDTH-COLON_WIDTH/2;
    	
    	drawTime(g, (int) opponentsTime, x, 0);
    	drawTime(g, (int) myTime, x, HEIGHT-NUM_HEIGHT);
    }
    
    private void drawTime(Graphics g, int t, int x, int y){
    	int time = (t>0) ? t : 0;
    	int minutes = (int) (time / 60);
    	int minutes10 = (int) (minutes / 10);
    	int minutes01 = minutes - 10*minutes10;
    	
    	int seconds = (int) (time - 60*minutes);
    	int seconds10 = (int) (seconds / 10);
    	int seconds01 = seconds - 10*seconds10;
    	
    	g.drawPixmap(Assets.numbers, x, y, minutes10*NUM_WIDTH, 0, NUM_WIDTH, NUM_HEIGHT);
    	x += NUM_WIDTH;
    	g.drawPixmap(Assets.numbers, x, y, minutes01*NUM_WIDTH, 0, NUM_WIDTH, NUM_HEIGHT);
    	x += NUM_WIDTH;
    	g.drawPixmap(Assets.numbers, x, y, 10*NUM_WIDTH, 0, COLON_WIDTH, NUM_HEIGHT);
    	x += COLON_WIDTH;
    	g.drawPixmap(Assets.numbers, x, y, seconds10*NUM_WIDTH, 0, NUM_WIDTH, NUM_HEIGHT);
    	x += NUM_WIDTH;
    	g.drawPixmap(Assets.numbers, x, y, seconds01*NUM_WIDTH, 0, NUM_WIDTH, NUM_HEIGHT);
    }
    
    private void drawReadyUI(Graphics g){
    	if(player == Color.WHITE){
    		drawDarkOverlay(g, 5, HEIGHT/2);
    	}
    	if(player == Color.BLACK){
    		drawDarkOverlay(g, 5, HEIGHT);
    		g.drawPixmap(Assets.buttonPlay, WIDTH /2 - BUTTON_SIZE/2, 
    										HEIGHT/2 - BUTTON_SIZE/2);
    	}
    }
    private void drawMyTurnUI(Graphics g){
    	// TODO maybe make the inactive clock darker
    	// TODO mark chosen tile (if it contains a piece)
    }
    private void drawOpponentsTurnUI(Graphics g){
    	// TODO maybe make the inactive clock darker
    }
    private void drawGameOverUI(Graphics g){
    	// TODO play-again-button & go-back-button
    	drawDarkOverlay(g, 10, HEIGHT);
    }

    private void drawDarkOverlay(Graphics g, int howOften, int height){
    	for(int i=0; i<howOften; i++){
    		g.drawRect(0, 0, WIDTH, height, darkOverlay);
    	}
    }
    
    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
