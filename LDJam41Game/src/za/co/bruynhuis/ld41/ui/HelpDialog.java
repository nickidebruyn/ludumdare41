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
public class HelpDialog extends PopupDialog {

    private Label info;
    private Button startButton;

    public HelpDialog(Window window) {
        super(window, "Interface/dialog.png", 660, 660, true);

        setTitle("Help");
        setTitleColor(ColorRGBA.White);
        setTitleSize(38);
        title.centerTop(0, 6);

        info = new Label(this, "Info about the game", 16, 500, 500);
        info.setAlignment(TextAlign.LEFT);
        info.setVerticalAlignment(TextAlign.CENTER);
        info.setWrapMode(LineWrapMode.Word);
        info.setTextColor(ColorRGBA.Black);
        info.centerAt(0, -50);
        info.setText("Rules:"
                + "\n-----------------------------"
                + "\n- In black Jack Racing you race against the Opponent"
                + "\n  by playing Black Jack."
                + "\n- Each round you win will increase your cars speed."
                + "\n- Try to get infront of the Opponent by winning card"
                + "\n  games."
                + "\n- Ask another card by clicking Hit button."
                + "\n- If happy with hand click Stand."
                + "\n- Don't exceed 21 points with your cards."
                + "\n\nPoints:"
                + "\n------------------------------"
                + "\n- Ace: 1 or 11"
                + "\n- Jack, Queen or King: 10"
                + "\n- Other: Their numbers");

        startButton = new Button(this, "startgame", "Let's Play", 0.8f);
        startButton.centerBottom(0, 20);
    }

    public void addStartButtonListener(TouchButtonListener listener) {
        startButton.addTouchButtonListener(listener);
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
                        HelpDialog.super.hide();
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
