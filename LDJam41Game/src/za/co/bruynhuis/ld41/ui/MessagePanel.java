/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.panel.Panel;
import com.bruynhuis.galago.ui.tween.WidgetAccessor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Stack;

/**
 *
 * @author NideBruyn
 */
public class MessagePanel extends Panel {

    private ShowCallback showCallback;
    private Label label;
    private Stack<String> messageStack = new Stack<>();
    private Vector3f targetPosition;

    public MessagePanel(Panel parent) {
        super(parent, null, parent.getWindow().getWidth(), 100, false);

        label = new Label(this, "Message", 42, 1000, 100);
        label.setAlignment(TextAlign.CENTER);
        label.setTextColor(ColorRGBA.White);

        parent.add(this);
    }

    public void addMessage(String text) {
        messageStack.push(text);
    }

    public void addShowCallback(ShowCallback showCallback) {
        this.showCallback = showCallback;
    }

    @Override
    public void show() {
        super.show();
        
        centerAt(0, 150);

        this.targetPosition = this.getPosition().clone();

        doCounter();

    }

    private void doCounter() {

        this.centerBottom(0, -100);

        label.setText(messageStack.pop());
        this.setScale(0);

        Tween.to(this, WidgetAccessor.POS_XY, 1f)
                .target(targetPosition.x, targetPosition.y)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> bt) {

                        if (messageStack.isEmpty()) {
                            if (showCallback != null) {
                                showCallback.shown();
                            }
                            hide();

                        } else {
                            doCounter();
                        }

                    }
                })
                .start(window.getApplication().getTweenManager());

        Tween.to(this, WidgetAccessor.SCALE_XY, 0.65f)
                .target(1, 1)
                .start(window.getApplication().getTweenManager());

    }

}
