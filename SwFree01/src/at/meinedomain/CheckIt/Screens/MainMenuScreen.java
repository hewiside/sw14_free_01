package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class MainMenuScreen extends AbstractScreen {
	
    private static final int NUM_WIDTH = 135; // a number's width in the picture Assets.numbers
    private static final int NUM_HEIGHT = 180;
	
    public MainMenuScreen(Game game) {
        super(game);               
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
        
        int len = touchEvents.size();
        int unit = g.getWidth()/12;
        int height = g.getHeight();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
//                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
//                    Settings.soundEnabled = !Settings.soundEnabled;
//                    if(Settings.soundEnabled){
//                        Assets.click.play(1);
//                    }
//                }
            	
            	// if "Play" clicked
                if(inBounds(event, 4*unit, height/3-2*unit, 4*unit, 4*unit)) {
                    game.setScreen(new GameScreen(game));
                    if(Settings.soundEnabled)
//                        Assets.click.play(1);
                    return;
                }
            	
            	// if "Settings" clicked
                if(inBounds(event, 4*unit, 2*height/3-2*unit, 4*unit, 4*unit)) {
                    game.setScreen(new SettingsScreen(game));
                    if(Settings.soundEnabled)
//                        Assets.click.play(1);
                    return;
                }
//                if(inBounds(event, 64, 220 + 42, 192, 42) ) {
//                    game.setScreen(new HighscoreScreen(game));
//                    if(Settings.soundEnabled)
//                        Assets.click.play(1);
//                    return;
//                }
//                if(inBounds(event, 64, 220 + 84, 192, 42) ) {
//                    game.setScreen(new HelpScreen(game));
//                    if(Settings.soundEnabled)
//                        Assets.click.play(1);
//                    return;
//                }
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
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), 0xffffce9e);

        // bars at top and bottom
        g.drawRect(0, 0, 12*unit, 2*unit, 0xffb57554);
        g.drawRect(0, g.getHeight()-2*unit, 12*unit, 2*unit, 0xffb57554);

        
        // Play button
        g.drawPixmap(Assets.buttonPlay, 4*unit, height/3-2*unit);
        if(((CheckItGame)game).getWifiCheckPossible() == false){
        	g.drawCirc(6*unit, height/3, 2*unit, 0x40ff0000);
        } else{
        	// TODO Test if this method works as intended
        	drawNumberOfPeers(g);
        }

//        here would be more abstract art ;)
//        g.drawRect(4*unit, 2*unit, 4*unit, 4*unit, 0xffb57554);
//        g.drawRect(5*unit, 3*unit, unit, 2*unit, 0xffffce9e);
//        g.drawRect(6*unit, 7*unit/2, unit, unit, 0xffffce9e);
        
        
        // Settings button
        g.drawPixmap(Assets.buttonSettings, 4*unit, 2*height/3-2*unit);
        
//        here would be more abstract art ;)
//        g.drawRect(4*unit, 8*unit, 4*unit, 4*unit, 0xffb57554);
//        g.drawRect(11*unit/2, 19*unit/2, unit, unit, 0xffffce9e);
//        
//        g.drawRect(23*unit/4, 37*unit/4, unit/2, unit/2, 0xffffce9e);
//        g.drawRect(21*unit/4, 39*unit/4, unit/2, unit/2, 0xffffce9e);
//        g.drawRect(25*unit/4, 39*unit/4, unit/2, unit/2, 0xffffce9e);
//        g.drawRect(23*unit/4, 41*unit/4, unit/2, unit/2, 0xffffce9e);
//        
//        g.drawRect(23*unit/4, 39*unit/4, unit/2, unit/2, 0xffb57554);

        
        
//        g.drawPixmap(Assets.background, 0, 0);
//        g.drawPixmap(Assets.logo, 32, 20);
//        g.drawPixmap(Assets.mainMenu, 64, 220);
//        if(Settings.soundEnabled)
//            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
//        else
//            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
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
