package at.meinedomain.CheckIt.Screens;

import at.meinedomain.CheckIt.Assets;
import at.meinedomain.CheckIt.Settings;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends AbstractScreen {

    public LoadingScreen(Game game) {
        super(game);
    }
    
	// overriden from AbstractScreen--------------------------------------------
    @Override
	public AbstractScreen.ScreenType getScreenType(){
    	return AbstractScreen.ScreenType.LOADING_SCREEN;
    }
    
    // overriden from Screen----------------------------------------------------
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        
        Assets.bb = g.newPixmap("bb.png", PixmapFormat.ARGB4444);
        Assets.bk = g.newPixmap("bk.png", PixmapFormat.ARGB4444);
        Assets.bn = g.newPixmap("bn.png", PixmapFormat.ARGB4444);
        Assets.bp = g.newPixmap("bp.png", PixmapFormat.ARGB4444);
        Assets.bq = g.newPixmap("bq.png", PixmapFormat.ARGB4444);
        Assets.br = g.newPixmap("br.png", PixmapFormat.ARGB4444);
        
        Assets.wb = g.newPixmap("wb.png", PixmapFormat.ARGB4444);
        Assets.wk = g.newPixmap("wk.png", PixmapFormat.ARGB4444);
        Assets.wn = g.newPixmap("wn.png", PixmapFormat.ARGB4444);
        Assets.wp = g.newPixmap("wp.png", PixmapFormat.ARGB4444);
        Assets.wq = g.newPixmap("wq.png", PixmapFormat.ARGB4444);
        Assets.wr = g.newPixmap("wr.png", PixmapFormat.ARGB4444);
        
        Assets.buttonPlay = g.newPixmap("buttonPlay.png", 
        								PixmapFormat.ARGB4444);
        Assets.buttonSettings = g.newPixmap("buttonSettings.png", 
        								PixmapFormat.ARGB4444);
        
        Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB4444);
        
        Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
    }
    
    @Override
    public void present(float deltaTime) {

    }
    
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}