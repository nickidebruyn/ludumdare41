/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.bruynhuis.galago.ui.Image;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.panel.Panel;
import com.bruynhuis.galago.ui.tween.WidgetAccessor;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author NideBruyn
 */
public class DeckPanel extends Panel {

    private Label title;
    private Image[] cards = new Image[5];
    private float cardScale = 0.4f;

    public DeckPanel(Panel parent) {
        super(parent, "Interface/panel-card.png", 320, 200, true);
        
        setTransparency(0.65f);

        title = new Label(this, "Deck of Cards");
        title.centerTop(0, 0);
        title.setTextColor(ColorRGBA.DarkGray);
        title.setAlignment(TextAlign.CENTER);

        for (int i = 0; i < 5; i++) {
            cards[i] = new Image(this, "Interface/cards/backshadow.png", 220 * cardScale, 340 * cardScale, true);
            cards[i].centerBottom(-20 + (i * 20 * cardScale), 15 + (i * 5 * cardScale));
        }

        parent.add(this);

    }
    
    public void drawCard(float xPos, float yPos, TweenCallback callback) {
        
        Tween.to(cards[4], WidgetAccessor.POS_XY, 0.6f)
                .target(xPos, yPos)
                .setCallback(callback)
                .start(window.getApplication().getTweenManager());
        
//        Tween.to(cards[4], WidgetAccessor.ROTATION_Y, 1f)
//                .target(90)
//                .start(window.getApplication().getTweenManager());
        
    }
    
    public void reset() {
        for (int i = 0; i < 5; i++) {
            cards[i].centerBottom(-20 + (i * 20 * cardScale), 15 + (i * 5 * cardScale));
//            cards[i].setRotationY(0);
        }
    }

}
