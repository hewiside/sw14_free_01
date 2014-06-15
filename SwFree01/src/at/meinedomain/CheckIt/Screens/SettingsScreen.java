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

public class SettingsScreen extends AbstractScreen {
	
    int colorLight;
    int colorDark;
	
    public SettingsScreen(Game game) {
        super(game);
        colorLight = ((CheckItGame)game).getResources().
    			getColor(at.meinedomain.CheckIt.R.color.light);
        colorDark  = ((CheckItGame)game).getResources().
    			getColor(at.meinedomain.CheckIt.R.color.dark);
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
        
        if(((CheckItGame)game).getIsBackPressed()) {
        	((CheckItGame)game).setIsBackPressed(false);
        	if(Settings.soundEnabled){
                Assets.menu.play(1);
        	}
            game.setScreen(new MainMenuScreen(game)); 
            return;
        }
        
        int len = touchEvents.size();
        int unit = g.getWidth()/12;
        int height = g.getHeight();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                // if Mute ON/OFF clicked
            	if(inBounds(event, 4*unit, height/3-2*unit, 4*unit, 4*unit)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled){
                        Assets.menu.play(1);
                    }
                }
            	// if "Back" clicked
                if(inBounds(event, 4*unit, 2*height/3-2*unit, 4*unit, 4*unit)) {
                    game.setScreen(new MainMenuScreen(game));
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
       
        // Mute ON/OFF button
        if(Settings.soundEnabled){
        	g.drawPixmap(Assets.buttonSound, 4*unit, height/3-2*unit);
        }
        else{
        	g.drawPixmap(Assets.buttonMute, 4*unit, height/3-2*unit);
        }

        // Back button
        g.drawPixmap(Assets.buttonBack, 4*unit, 2*height/3-2*unit);
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
