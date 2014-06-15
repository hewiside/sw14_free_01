package at.meinedomain.CheckIt.GuiTest;

import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import at.meinedomain.CheckIt.CheckItGame;
import at.meinedomain.CheckIt.Settings;
import at.meinedomain.CheckIt.Screens.MainMenuScreen;
import at.meinedomain.CheckIt.Screens.SettingsScreen;

public class GuiTestSettings extends
		ActivityInstrumentationTestCase2<CheckItGame> {

	private Solo solo;
	
	public GuiTestSettings() {
		super("at.meinedomain.CheckIt.CheckItGame", CheckItGame.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	// IMPORTANT! THIS TEST HAS TO BE DONE ON A DEVICE WITH FULL-HD SCREEN.
	public void testToggleSound() {
		
		// IMPORTANT! THIS TEST HAS TO BE DONE ON A DEVICE WITH FULL-HD SCREEN.
		int width  = 1080;
		int height = 1920;
		float eps = width/6;
		//----------------------------------------------------------------------
		
		
		
		// wait for assets to be loaded and MainMenuScreen to appear------------
		solo.sleep(1000);
		
		boolean currentSoundStatus = Settings.soundEnabled;
		AndroidGame gameActivity = ((AndroidGame) solo.getCurrentActivity());
		
		assertTrue("Is MainMenuScreen the current screen?", 
				   gameActivity.getCurrentScreen() instanceof MainMenuScreen);
		//----------------------------------------------------------------------
		
		
		
		// click on settings-button and wait for the settings screen to appear--
		solo.clickOnScreen(width/3+eps,   height*2/3-width/6+eps);
		
		solo.sleep(3000);
		
		assertTrue("Is SettingsScreen the current screen?", 
				   gameActivity.getCurrentScreen() instanceof SettingsScreen);
		//----------------------------------------------------------------------
		
		
		
		// Click on the sound-toggle-button and check if the setting changed----
		solo.clickOnScreen(width/3+eps,   height/3-width/6+eps);
		
		solo.sleep(1000); // wait for changes to take place
		
		assertTrue("Has the sound status toggled?", 
				   Settings.soundEnabled == !currentSoundStatus);
		//----------------------------------------------------------------------
		
		
		
		// Go back to MainMenuScreen and check if sound status is still the same
		solo.clickOnScreen(width/3+eps,   height*2/3-width/6+eps);
		
		solo.sleep(500);
		
		assertTrue("Is MainMenuScreen the current screen?", 
				   gameActivity.getCurrentScreen() instanceof MainMenuScreen);
		assertTrue("Is the sound status still toggled?", 
				   Settings.soundEnabled == !currentSoundStatus);
		//----------------------------------------------------------------------
	}

}
