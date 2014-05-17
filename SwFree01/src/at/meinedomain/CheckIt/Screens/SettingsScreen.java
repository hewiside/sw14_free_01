package at.meinedomain.CheckIt.Screens;

import android.util.Log;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Settings;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class SettingsScreen extends AbstractScreen {
	
    public SettingsScreen(Game game) {
        super(game);               
    }   

	// overriden from AbstractScreen--------------------------------------------
    @Override
	public AbstractScreen.ScreenType getScreenType(){
    	return AbstractScreen.ScreenType.SETTINGS_SCREEN;
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
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                // if "Sound" clicked
            	if(inBounds(event, 4*unit, 2*unit, 4*unit, 4*unit)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled){
//                        Assets.click.play(1);
                    }
                }
            	// if "Back" clicked
                if(inBounds(event, 0, g.getHeight() - 3*unit, 3*unit, 3*unit)) {
                    game.setScreen(new MainMenuScreen(game));
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
        
        // background
        g.drawRect(0, 0, g.getWidth(), g.getHeight(), 0xffffce9e);
        
        // Sound button
        int unit = g.getWidth()/12;
        if(Settings.soundEnabled){
        	g.drawRect(4*unit, 2*unit, 4*unit, 4*unit, 0xffa18934); // green icon
        }
        else {
        	g.drawRect(4*unit, 2*unit, 4*unit, 4*unit, 0xffb55034);	// red icon
        }
        // Back button
        g.drawRect(0, g.getHeight() - 3*unit, 3*unit, 3*unit, 0xffb57554);
        
//        g.drawPixmap(Assets.background, 0, 0);
//        g.drawPixmap(Assets.logo, 32, 20);
//        g.drawPixmap(Assets.mainMenu, 64, 220);
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
