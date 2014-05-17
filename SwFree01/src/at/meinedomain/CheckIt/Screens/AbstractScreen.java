package at.meinedomain.CheckIt.Screens;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Game;

public abstract class AbstractScreen extends Screen{
	public enum ScreenType{
		GAME_SCREEN,
		LOADING_SCREEN,
		MAIN_MENU_SCREEN,
		SETTINGS_SCREEN
	}
	
	AbstractScreen(Game game){
		super(game);
	}
	
	public abstract ScreenType getScreenType();
	
}