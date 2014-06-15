package at.meinedomain.CheckIt.Screens;

import android.R;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Color;
import at.meinedomain.CheckIt.PeerListFragment;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class MainMenuScreen extends AbstractScreen {
	
    private static final int NUM_WIDTH = 135; // a number's width in the picture Assets.numbers
    private static final int NUM_HEIGHT = 180;
    
    int colorLight;
    int colorDark;
    int colorForbidden;
	
    public MainMenuScreen(Game game) {
        super(game);
        colorLight = ((CheckItGame)game).getResources().
    			getColor(at.meinedomain.CheckIt.R.color.light);
        colorDark  = ((CheckItGame)game).getResources().
    			getColor(at.meinedomain.CheckIt.R.color.dark);
        colorForbidden = ((CheckItGame)game).getResources().
        		getColor(at.meinedomain.CheckIt.R.color.forbidden_overlay);
    }   

	// overriden from AbstractScreen--------------------------------------------
    @Override
	public AbstractScreen.ScreenType getScreenType(){
    	return AbstractScreen.ScreenType.MAIN_MENU_SCREEN;
    }
    
    // overriden from Screen----------------------------------------------------
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        if(((CheckItGame)game).getPlayerColor() != null){
//        	((CheckItGame)game).setPlayerColor(null);
        	game.setScreen(new GameScreen(game));
        }
        
        int len = touchEvents.size();
        int unit = g.getWidth()/12;
        int height = g.getHeight();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	
            	// if "Play" clicked
                if(inBounds(event, 4*unit, height/3-2*unit, 4*unit, 4*unit)) {
                	if(((CheckItGame)game).getWifiCheckPossible() == false){
                		((CheckItGame)game).discoverPeers(); // TODO: I can't expect that discoverPeers() (async) leads to an updated List peers before the next statement (showPeerList()) is reached!!!
                	}                	
                    if(Settings.soundEnabled)
                        Assets.menu.play(1);                	
                	((CheckItGame)game).showPeerList();
                    return;
                }
            	
            	// if "Settings" clicked
                if(inBounds(event, 4*unit, 2*height/3-2*unit, 4*unit, 4*unit)) {
                    game.setScreen(new SettingsScreen(game));
                    if(Settings.soundEnabled)
                        Assets.menu.play(1);
                    return;
                }
            }
        }
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
        
        int unit = g.getWidth()/12;
        int height = g.getHeight();
        // background
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), colorLight);

        // bars at top and bottom
        g.drawRect(0, 0, 12*unit, 2*unit, colorDark);
        g.drawRect(0, g.getHeight()-2*unit, 12*unit, 2*unit, colorDark);

        
        // Play button
        g.drawPixmap(Assets.buttonPlay, 4*unit, height/3-2*unit);
        if((((CheckItGame)game).getWifiCheckPossible() ||
        	((CheckItGame)game).getIsWifiP2PEnabled() )== false){
        	g.drawCirc(6*unit, height/3, 2*unit, colorForbidden);
        } else{
        	drawNumberOfPeers(g);
        }       
        
        // Settings button
        g.drawPixmap(Assets.buttonSettings, 4*unit, 2*height/3-2*unit);
    }
    
    // this method draws the number of peers in the center of the bottom bar. (it is only called if wifi-check successful)
    private void drawNumberOfPeers(Graphics g){
    	int number = ((CheckItGame)game).getPeers().size();
    	int length = String.valueOf(number).length();
    	int x = (int) (g.getWidth()/2 + (length/2.0-1) * NUM_WIDTH);
    	int y = g.getHeight() - NUM_HEIGHT;
    	for(int i=0; i<length; i++){
        	g.drawPixmap(Assets.numbers, x, y, (number%10)*NUM_WIDTH, 0, NUM_WIDTH, NUM_HEIGHT);
        	number /= 10;
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
