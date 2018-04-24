/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.screens;

import com.bruynhuis.galago.control.RotationControl;
import com.bruynhuis.galago.filters.FXAAFilter;
import com.bruynhuis.galago.screen.AbstractScreen;
import com.bruynhuis.galago.sprite.Sprite;
import com.bruynhuis.galago.ui.Image;
import com.bruynhuis.galago.ui.effect.WobbleEffect;
import com.bruynhuis.galago.ui.listener.TouchButtonAdapter;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Spatial;
import za.co.bruynhuis.ld41.ui.Button;

/**
 *
 * @author NideBruyn
 */
public class MenuScreen extends AbstractScreen {

    public static final String NAME = "MenuScreen";
    private Image title;
    private Button playButton;
    private Sprite background;
    private float backgroundScale = 0.021f;
    private Spatial model;
    private FilterPostProcessor fpp;
    private PointLight light;

    @Override
    protected void init() {
        title = new Image(hudPanel, "Interface/title.png", 800, 300, true);
        title.centerTop(0, 0);
        
        playButton = new Button(hudPanel, "playbutton", "Play", 1f);
        playButton.centerBottom(0, 10);
        playButton.addEffect(new WobbleEffect(playButton, 1.05f, 0.1f));
        playButton.addTouchButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(PlayScreen.NAME);
                }
            }
            
        });

    }

    @Override
    protected void load() {
        
        background = new Sprite("background", 1280*backgroundScale, 800*backgroundScale);
        background.setImage("Textures/background.jpg");
        background.move(0, 0, -10);
        rootNode.attachChild(background);
        
        model = assetManager.loadModel("Scenes/menu.j3o");
        model.move(0, -3, 0);
        model.rotate(0, 45f*FastMath.DEG_TO_RAD, 0);
        model.addControl(new RotationControl(30));
        rootNode.attachChild(model);
        
        light = new PointLight(new Vector3f(0, 5, 5));
        rootNode.addLight(light);
        
        fpp = new FilterPostProcessor(assetManager);
        baseApplication.getViewPort().addProcessor(fpp);
        
        SSAOFilter ssaof = new SSAOFilter();
        ssaof.setIntensity(2.5f);
        ssaof.setSampleRadius(1.5f);
        fpp.addFilter(ssaof);
        
        FXAAFilter fXAAFilter = new FXAAFilter();
        fpp.addFilter(fXAAFilter);
        
        camera.setLocation(new Vector3f(0, 0, 10));
        camera.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
        
    }

    @Override
    protected void show() {
        setPreviousScreen(null);
        baseApplication.getSoundManager().playMusic("garage");
    }

    @Override
    protected void exit() {
        baseApplication.getSoundManager().stopMusic("garage");
        rootNode.removeLight(light);
        rootNode.detachAllChildren();
        baseApplication.getViewPort().removeProcessor(fpp);
    }

    @Override
    protected void pause() {
    }

}
