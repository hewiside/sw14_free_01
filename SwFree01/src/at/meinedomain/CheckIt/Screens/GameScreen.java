package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.R;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class GameScreen extends AbstractScreen {
	
	enum GameState{
		Ready,
		MyTurn,
		OpponentsTurn,		
		GameOver
	}
	
	GameState state = GameState.Ready;
	
	CheckItGame game;
	Board board;
	Color player;
	
	float myTime;
	float opponentsTime;
	
	int offset; // determines whether A8 is a light or dark tile?	
    int unit;
    int tileSize;
    int firstRankY;
    private static final int NUM_WIDTH = 135; // a number's width in the picture Assets.numbers
    private static final int NUM_HEIGHT = 180;
	private static final int COLON_WIDTH = 45; // width of colon in Assets.numbers 
	private int colorTable;
	private int colorDark; 
	private int colorLight; 
	private int darkOverlay; 
	
    public GameScreen(Game game) {
        super(game);
        colorTable = ((AndroidGame)game).getResources().getColor(R.color.medium);
        colorDark  = ((AndroidGame)game).getResources().getColor(R.color.dark);
        colorLight = ((AndroidGame)game).getResources().getColor(R.color.light);
        darkOverlay = ((AndroidGame)game).getResources().getColor(R.color.dark_overlay);
        
        Log.e("GameScreen", "VOR");
        this.game = (CheckItGame) game;
        Log.e("GameScreen", "DANACH");
        board = new Board();
        player = this.game.getPlayerColor();

        myTime = 300;
        opponentsTime = 300;
        
        offset = (player==Color.WHITE ? 1 : 0);
        unit = game.getGraphics().getWidth()/12;
        tileSize = game.getGraphics().getWidth() / board.getWidth();
        firstRankY = game.getGraphics().getHeight()/2 +
        									(board.getHeight()/2-1)*tileSize;
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
        
        if(game.isBackPressed) {
        	game.isBackPressed = false;
        	if(Settings.soundEnabled){
                //Assets.click.play(1);
        	}
            game.setScreen(new MainMenuScreen(game)); 
            return;
        }
        
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
        
        if(state == GameState.Ready){ 
            drawReadyUI(g);
        }
        if(state == GameState.MyTurn){
            drawMyTurnUI(g);
        }
        if(state == GameState.OpponentsTurn){
            drawOpponentsTurnUI(g);
        }
        if(state == GameState.GameOver){
            drawGameOverUI(g);
        }
    }
    
    private void drawBackground(Graphics g){
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), colorTable);
    }
    
    private void drawBoard(Graphics g){
        // Dark tiles
        g.drawRect(0, firstRankY - 7*tileSize, 
        		   g.getWidth(), g.getWidth(), colorDark);
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Light tiles
        		if((i+j)%2 == offset){
        			g.drawRect(i*tileSize, firstRankY - j*tileSize, 
        					   tileSize, tileSize, colorLight);
        		}
        		// Pieces
        		if(board.pieceAt(i,j) != null){
        			g.drawPixmap(board.pieceAt(i,j).getPixmap(), 
        						 i*tileSize, firstRankY - j*tileSize);
        		}
        	}
        }
    }
    
    private void drawTimes(Graphics g){
    	int x = g.getWidth()/2-2*NUM_WIDTH-COLON_WIDTH/2;
    	
    	drawTime(g, (int) opponentsTime, x, 0);
    	drawTime(g, (int) myTime, x, g.getHeight()-NUM_HEIGHT);
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
    		// TODO show that we wait for black to start the countdown
    	}
    	if(player == Color.BLACK){
    		// TODO show a picture that makes player start the countdown
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
    	g.drawRect(0, 0, g.getWidth(), g.getHeight(), darkOverlay);
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
