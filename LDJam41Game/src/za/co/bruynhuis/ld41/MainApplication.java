package za.co.bruynhuis.ld41;

import com.bruynhuis.galago.app.Base3DApplication;
import com.bruynhuis.galago.resource.EffectManager;
import com.bruynhuis.galago.resource.FontManager;
import com.bruynhuis.galago.resource.ModelManager;
import com.bruynhuis.galago.resource.ScreenManager;
import com.bruynhuis.galago.resource.SoundManager;
import com.bruynhuis.galago.resource.TextureManager;
import za.co.bruynhuis.ld41.screens.MenuScreen;
import za.co.bruynhuis.ld41.screens.PlayScreen;

/**
 * A game for the Ludum Dare 41 jam.
 *
 *
 * @author nickidebruyn
 */
public class MainApplication extends Base3DApplication {
    
    private int gamesRetried = 0;

    public MainApplication() {
        super("Ludum Dare 41", 1280, 800, "ldjam41.save", null, null, false);
//        JmeDesktopSystem
    }

    public static void main(String[] args) {
        new MainApplication();
    }

    @Override
    protected void preInitApp() {
    }

    @Override
    protected void postInitApp() {
        showScreen(MenuScreen.NAME);
    }

    @Override
    protected boolean isPhysicsEnabled() {
        return false;
    }

    @Override
    protected void initScreens(ScreenManager screenManager) {
        screenManager.loadScreen(MenuScreen.NAME, new MenuScreen());
        screenManager.loadScreen(PlayScreen.NAME, new PlayScreen());

    }

    @Override
    public void initModelManager(ModelManager modelManager) {
    }

    @Override
    protected void initSound(SoundManager soundManager) {
        soundManager.loadSoundFx("start", "Sounds/start.ogg");
        soundManager.loadSoundFx("endrace", "Sounds/endrace.ogg");
        soundManager.loadSoundFx("gameover", "Sounds/gameover.ogg");
        soundManager.loadSoundFx("deal", "Sounds/deal.ogg");
        
        soundManager.loadMusic("racing", "Sounds/racing.ogg");
        soundManager.setMusicVolume("racing", 0.4f);
        
        soundManager.loadMusic("garage", "Sounds/garage.ogg");
        soundManager.setMusicVolume("garage", 0.3f);
        
    }

    @Override
    protected void initEffect(EffectManager effectManager) {
    }

    @Override
    protected void initTextures(TextureManager textureManager) {
    }

    @Override
    protected void initFonts(FontManager fontManager) {
    }

    public int getGamesRetried() {
        return gamesRetried;
    }

    public void addGamesRetried() {
        this.gamesRetried ++;
    }

}
