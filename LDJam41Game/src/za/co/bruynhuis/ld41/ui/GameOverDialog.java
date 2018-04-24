/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.listener.TouchButtonListener;
import com.bruynhuis.galago.ui.panel.PopupDialog;
import com.bruynhuis.galago.ui.tween.WidgetAccessor;
import com.bruynhuis.galago.ui.window.Window;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author NideBruyn
 */
public class GameOverDialog extends PopupDialog {

    private Label info;
    private Button retryButton;
    private Button exitButton;

    public GameOverDialog(Window window) {
        super(window, "Interface/dialog.png", 660, 660, true);

        setTitle("Finish");
        setTitleColor(ColorRGBA.White);
        setTitleSize(38);
        title.centerTop(0, 6);

        info = new Label(this, "Info about the game", 24, 500, 500);
        info.setAlignment(TextAlign.CENTER);
        info.setVerticalAlignment(TextAlign.CENTER);
        info.setWrapMode(LineWrapMode.Word);
        info.setTextColor(ColorRGBA.Black);
        info.centerAt(0, 0);

        retryButton = new Button(this, "retrygame", "Retry", 0.8f);
        retryButton.centerBottom(130, 20);
        
        exitButton = new Button(this, "exit", "Exit", 0.8f);
        exitButton.centerBottom(-130, 20);
    }
    
    public void setText(String text) {
        info.setText(text);
    }

    public void addRetryButtonListener(TouchButtonListener listener) {
        retryButton.addTouchButtonListener(listener);
    }
    
    public void addExitButtonListener(TouchButtonListener listener) {
        exitButton.addTouchButtonListener(listener);
    }

    @Override
    public void hide() {
        centerBottom(0, -800);
        Vector3f pos = getPosition().clone();
        center();
        Tween.to(this, WidgetAccessor.POS_XY, 1.2f)
                .target(pos.x, pos.y)
                .ease(Bounce.OUT)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> bt) {
                        GameOverDialog.super.hide();
                    }
                })
                .start(window.getApplication().getTweenManager());

    }

    @Override
    public void show() {
        super.show();
        center();
        Vector3f pos = getPosition().clone();
        centerTop(0, -700);
        Tween.to(this, WidgetAccessor.POS_XY, 1f)
                .target(pos.x, pos.y)
                .ease(Bounce.OUT)
                .start(window.getApplication().getTweenManager());

    }

}
