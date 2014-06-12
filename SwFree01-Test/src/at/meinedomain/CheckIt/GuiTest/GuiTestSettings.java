package at.meinedomain.CheckIt.GuiTest;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import at.meinedomain.CheckIt.CheckItGame;

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
	
	public void testToggleSound() {
		// TODO write the actual test
	}

}
