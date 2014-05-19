package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Board;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class GameScreen extends AbstractScreen {
	
	enum GameState{
		Ready,
		Running,	// maybe 1 state for white's turn and 1 state for black' turn.
		GameOver
	}
	
	GameState state = GameState.Ready;
	Board board;
	Color player;
	int offset; // determines whether A8 is a light or dark tile?
	
    public GameScreen(Game game) {
        super(game);
        board = new Board();
        player = Color.WHITE;
        offset = (player==Color.WHITE ? 1 : 0);
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
        
        if(((CheckItGame)game).backPressed) {
        	((CheckItGame)game).backPressed = false;
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
        
        // background
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), 0xffdaa179);
        
        int unit = g.getWidth()/12;
        int tileSize = g.getWidth()/8;
        int firstRankY = g.getHeight()/2+3*tileSize;
        // Dark tiles
        g.drawRect(0, firstRankY - 7*tileSize, 
        		   g.getWidth(), g.getWidth(), 0xffb57554);
        for(int i=0; i<board.getWidth(); i++){
        	for(int j=0; j<board.getHeight(); j++){
        		// Light tiles
        		if((i+j)%2 == offset){
        			g.drawRect(i*tileSize, firstRankY - j*tileSize, 
        					   tileSize, tileSize, 0xffffce9e);
        		}
        		// Pieces
        		if(board.pieceAt(i,j) != null){
        			g.drawPixmap(board.pieceAt(i,j).getPixmap(), 
        						 i*tileSize, firstRankY - j*tileSize);
        		}
        	}
        }
	//	        g.drawPixmap(Assets.br, 0, 0);
	//	        g.drawPixmap(Assets.bn, tileSize, 0);
	//	        g.drawPixmap(Assets.bb, 2*tileSize, 0);
	//	        g.drawPixmap(Assets.bq, 3*tileSize, 0);
	//	        g.drawPixmap(Assets.bk, 4*tileSize, 0);
	//	        g.drawPixmap(Assets.bb, 5*tileSize, 0);
	//	        g.drawPixmap(Assets.bn, 6*tileSize, 0);
	//	        g.drawPixmap(Assets.br, 7*tileSize, 0);
	//	        g.drawPixmap(Assets.bp, 0, 2*tileSize);
	//	
	//	        g.drawPixmap(Assets.wr, 0, 0);
	//	        g.drawPixmap(Assets.wn, tileSize, 0);
	//	        g.drawPixmap(Assets.wb, 2*tileSize, 0);
	//	        g.drawPixmap(Assets.wq, 3*tileSize, 0);
	//	        g.drawPixmap(Assets.wk, 4*tileSize, 0);
	//	        g.drawPixmap(Assets.wb, 5*tileSize, 0);
	//	        g.drawPixmap(Assets.wn, 6*tileSize, 0);
	//	        g.drawPixmap(Assets.wr, 7*tileSize, 0);
	//	        g.drawPixmap(Assets.wp, 0, 2*tileSize);
	//	        
	//	        g.drawPixmap(Assets.wr, 0, tileSize);
	//	        g.drawPixmap(Assets.wn, tileSize, tileSize);
	//	        g.drawPixmap(Assets.wb, 2*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wq, 3*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wk, 4*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wb, 5*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wn, 6*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wr, 7*tileSize, tileSize);
	//	        g.drawPixmap(Assets.wp, tileSize, 2*tileSize);
	//	        
	//	        g.drawPixmap(Assets.br, 0, tileSize);
	//	        g.drawPixmap(Assets.bn, tileSize, tileSize);
	//	        g.drawPixmap(Assets.bb, 2*tileSize, tileSize);
	//	        g.drawPixmap(Assets.bq, 3*tileSize, tileSize);
	//	        g.drawPixmap(Assets.bk, 4*tileSize, tileSize);
	//	        g.drawPixmap(Assets.bb, 5*tileSize, tileSize);
	//	        g.drawPixmap(Assets.bn, 6*tileSize, tileSize);
	//	        g.drawPixmap(Assets.br, 7*tileSize, tileSize);
	//	        g.drawPixmap(Assets.bp, tileSize, 2*tileSize);
//        g.drawRect(0, 0, 12*unit, 2*unit, 0xffb57554);
//
//        g.drawRect(0, g.getHeight()-2*unit, 12*unit, 2*unit, 0xffb57554);
        
        
//        if(Settings.soundEnabled)
//            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
//        else
//            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
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
