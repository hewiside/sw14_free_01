package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.ClientThread;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.ConnectionThread;
import at.meinedomain.CheckIt.R;
import at.meinedomain.CheckIt.ServerThread;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class GameScreen extends AbstractScreen {
	
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
    private static final int NUM_WIDTH = 135; // a number's width in the picture Assets.numbers
    private static final int NUM_HEIGHT = 180;
	private static final int COLON_WIDTH = 45; // width of colon in Assets.numbers 
	private final int HEIGHT; // of screen
	private final int WIDTH;
	private final int BUTTON_SIZE;
	private int colorTable;
	private int colorDark; 
	private int colorLight; 
	private int darkOverlay; 
	
    public GameScreen(Game game_) {
        super(game_);
        game = (CheckItGame) game_;
        
        colorTable = game.getResources().getColor(R.color.medium);
        colorDark  = game.getResources().getColor(R.color.dark);
        colorLight = game.getResources().getColor(R.color.light);
        darkOverlay = game.getResources().getColor(R.color.dark_overlay);
        

        board = new Board();
        player = game.getPlayerColor(); // is set because this Screen is only
        		// set if MainMenu Screen recognizes a color in CheckItGame.

        myTime = 300; // TODO allow custom times
        opponentsTime = 300;
        
        lightTileOffset = 1;
        Log.d("GameScreen", "player plays with: "+player);
        
        HEIGHT = game.getGraphics().getHeight();
        WIDTH  = game.getGraphics().getWidth();
        BUTTON_SIZE = Assets.buttonPlay.getWidth();
        unit = WIDTH/12;
        tileSize = WIDTH / board.getWidth();
        firstRankY = HEIGHT/2 + (board.getHeight()/2-1)*tileSize;
        
        if(player==Color.WHITE){
        	connectionThread = new ServerThread(board);
        }
        else{
        	connectionThread = new ClientThread(game.getWifiP2pInfo(), board);
        }
    	connectionThread.start();
    }   

	// overriden from AbstractScreen--------------------------------------------
    @Override
	public AbstractScreen.ScreenType getScreenType(){
    	return AbstractScreen.ScreenType.GAME_SCREEN;
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
        
//        if(state == GameState.MyTurn)
        
        int len = touchEvents.size();
        int unit = g.getWidth()/12;
//        for(int i = 0; i < len; i++) {
//            TouchEvent event = touchEvents.get(i);
//            if(event.type == TouchEvent.TOUCH_UP) {
////                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
////                    Settings.soundEnabled = !Settings.soundEnabled;
////                    if(Settings.soundEnabled){
////                        Assets.click.play(1);
////                    }
////                }
//            	
//            	// if "Settings" clicked
//                if(inBounds(event, 4*unit, 8*unit, 4*unit, 4*unit)) {
//                    game.setScreen(new SettingsScreen(game));
//                    if(Settings.soundEnabled)
////                        Assets.click.play(1);
//                    return;
//                }
////                if(inBounds(event, 64, 220 + 42, 192, 42) ) {
////                    game.setScreen(new HighscoreScreen(game));
////                    if(Settings.soundEnabled)
////                        Assets.click.play(1);
////                    return;
////                }
////                if(inBounds(event, 64, 220 + 84, 192, 42) ) {
////                    game.setScreen(new HelpScreen(game));
////                    if(Settings.soundEnabled)
////                        Assets.click.play(1);
////                    return;
////                }
//            }
//        }
    }
    
    private void updateReady(float deltaTime, List<TouchEvent> touchEvents){
    	// TODO FOR BLACK PLAYER: if screen is tapped, trigger the sending of START_TAG in the socket and set the state to OpponentsTurn.
    	// for black player
    	if(player == Color.BLACK){
	    	for(int i = 0; i < touchEvents.size(); i++) {
	    		TouchEvent event = touchEvents.get(i);
	    		if(event.type == TouchEvent.TOUCH_UP){
	    			connectionThread.requestStart();
	    			state = GameState.OPPONENTS_TURN;
	    		}
	    	}
    	}
    	// for white player
    	else{
    		if(connectionThread.getStartRequested()){
    			state = GameState.MY_TURN;
    		}
    	}
    	
    	// TODO FOR WHITE PLAYER: check if we have received the START_TAG. If so, set state to MyTurn.
    }
    private void upateMyTurn(float deltaTime, List<TouchEvent> touchEvents){
    	if(checkForGameOver()){
    		return;
    	}
//    	if(board.getTurn() == )
    }
    private void updateOpponentsTurn(float deltaTime, List<TouchEvent> touchEvents){
    	if(checkForGameOver()){
    		return;
    	}
    	
    	if(board.getTurn() == null){
    		Log.wtf("GameScreen","Current color in board is null!");
    	}
    	if(board.getTurn() != player){
    		state = GameState.MY_TURN;
    		// TODO opponentsTime = connectionThread.getOpponentsTime();
    	}
    }
    private void updateGameOver(float deltaTime, List<TouchEvent> touchEvents){
    	// TODO check if some replay-button (or back-button) has been clicked.
    }
    
    private boolean checkForGameOver(){
    	if(board.getMatchState() != Board.MatchState.RUNNING){
    		state = GameState.GAME_OVER;
    		return true;
    	}
    	return false;
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
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
    		drawWhitePieces(g);
    	}
    	else{
    		drawBlackPieces(g);
    	}
    }
    
    private void drawWhitePieces(Graphics g){
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Pieces
        		if(board.pieceAt(i,j) != null){
        			g.drawPixmap(board.pieceAt(i,j).getPixmap(), 
        						 i*tileSize, firstRankY - j*tileSize);
        		}
        	}
        }
    }
    
    private void drawBlackPieces(Graphics g){
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Pieces
        		int k = board.getWidth() -1-i;
        		int l = board.getHeight()-1-j;
        		if(board.pieceAt(k,l) != null){
        			g.drawPixmap(board.pieceAt(k,l).getPixmap(), 
        					i*tileSize, firstRankY - j*tileSize);
        		}
        	}
        }
    }    
    
    private void drawTimes(Graphics g){
    	int x = WIDTH/2-2*NUM_WIDTH-COLON_WIDTH/2;
    	
    	drawTime(g, (int) opponentsTime, x, 0);
    	drawTime(g, (int) myTime, x, HEIGHT-NUM_HEIGHT);
    }
    
    private void drawTime(Graphics g, int time, int x, int y){
    	int minutes = (int) time / 60;
    	int minutes10 = (int) minutes / 10;
    	int minutes01 = (int) minutes - minutes10;
    	
    	int seconds = (int) time - 60*minutes;
    	int seconds10 = (int) seconds / 10;
    	int seconds01 = (int) seconds - seconds/10;
    	
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
