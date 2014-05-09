package at.meinedomain.CheckIt;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class CheckItGame extends AndroidGame {

	public boolean backPressed = false;
	
	public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }

    @Override
    public void onBackPressed() {
    	AbstractScreen.ScreenType st = ((AbstractScreen)getCurrentScreen()).
    															getScreenType();
    	if(st == AbstractScreen.ScreenType.MAIN_MENU_SCREEN ||
    	   st == AbstractScreen.ScreenType.LOADING_SCREEN){
    		super.onBackPressed();
    	}
    		
    	backPressed = true;
    	
    }
} 

