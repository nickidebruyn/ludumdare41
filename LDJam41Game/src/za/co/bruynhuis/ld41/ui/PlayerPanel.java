/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.ui;

import com.bruynhuis.galago.ui.Image;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.listener.TouchButtonListener;
import com.bruynhuis.galago.ui.panel.Panel;
import com.bruynhuis.galago.util.Timer;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import za.co.bruynhuis.ld41.game.Card;

/**
 *
 * @author NideBruyn
 */
public class PlayerPanel extends Panel {

    private Label title;
    private Label stayTitle;
    private Button drawButton;
    private Button stayButton;
    private Image[] cards = new Image[5];
    private List<Card> hand = new ArrayList<>();
    private float cardScale = 0.38f;
    private float spacing = 12;
    private boolean stay = false;
    private boolean player = false;
    private Label timerLabel;
    private int counter = 0;
    private Timer timer = new Timer(100);
    private TimeoutCallback timeoutCallback;
    private Label lapCount;

    public PlayerPanel(Panel parent, String name, ColorRGBA colorRGBA, boolean player) {
        super(parent, "Interface/panel-card.png", 460, 200, true);
        this.player = player;

        setTransparency(0.65f);

        title = new Label(this, name);
        title.leftTop(15, 0);
        title.setTextColor(colorRGBA);
        title.setAlignment(TextAlign.LEFT);
        
        lapCount = new Label(this, "Lap Count: 0", 16, 300, 50);
        lapCount.leftTop(5, -50);
        lapCount.setTextColor(ColorRGBA.White);
        lapCount.setAlignment(TextAlign.LEFT);

        stayTitle = new Label(this, "Standing", 14);
        stayTitle.leftTop(15, 22);
        stayTitle.setTextColor(ColorRGBA.White);
        stayTitle.setAlignment(TextAlign.LEFT);
        stayTitle.setVisible(false);

        if (player) {
            drawButton = new Button(this, "draw button", "Hit", 0.55f);
            drawButton.rightTop(8, 8);

            stayButton = new Button(this, "stayButton", "Stand", 0.55f);
            stayButton.rightTop(150, 8);

            timerLabel = new Label(this, "0", 38, 60, 60);
            timerLabel.setTextColor(ColorRGBA.White);
            timerLabel.setAlignment(TextAlign.CENTER);
            timerLabel.setVerticalAlignment(TextAlign.CENTER);

            widgetNode.addControl(new AbstractControl() {
                @Override
                protected void controlUpdate(float tpf) {

                    if (counter > 0) {
                        timer.update(tpf);

                        if (timer.finished()) {
                            counter--;
                            if (counter == 0) {
                                timeoutCallback.done();
                                stopCountDown();

                            } else {
                                timerLabel.setText(counter + "");
                                timerLabel.setScale(1.1f);
                                timer.reset();

                            }
                        }
                    }

                }

                @Override
                protected void controlRender(RenderManager rm, ViewPort vp) {

                }
            });

        }

        for (int i = 0; i < 5; i++) {
            cards[i] = new Image(this, "Interface/cards/blank.png", 220 * cardScale, 340 * cardScale, true);
            cards[i].leftBottom(spacing + (i * (220 + spacing) * cardScale), 10);
        }

        parent.add(this);

    }
    
    public void setStats(int count, int speed) {
        lapCount.setText("Lap Count: " + count + "/10" +
                "\nSpeed: " + speed + " mph");
    }

    public void addTimeoutCallback(TimeoutCallback timeoutCallback) {
        this.timeoutCallback = timeoutCallback;
    }

    public Vector3f getNextSlotPosition() {
        return this.getPosition().clone().add(cards[hand.size()].getPosition());
    }

    public void addCard(Card card) {
        hand.add(card);

        for (int i = 0; i < hand.size(); i++) {
            Card c = (Card) hand.get(i);

            if (player || i == 0) {
                cards[i].updatePicture(c.getCardPath());
            } else {
                cards[i].updatePicture("Interface/cards/back.png");
            }

        }

    }

    public void revealCards() {
        for (int i = 0; i < hand.size(); i++) {
            Card c = (Card) hand.get(i);
            cards[i].updatePicture(c.getCardPath());

        }
    }

    public void addDrawButtonListener(TouchButtonListener listener) {
        drawButton.addTouchButtonListener(listener);
    }

    public void addStayButtonListener(TouchButtonListener listener) {
        stayButton.addTouchButtonListener(listener);
    }

    public void setEnabled(boolean enabled) {
        if (player) {
            drawButton.setVisible(enabled);
            stayButton.setVisible(enabled);
            timerLabel.setVisible(enabled);

            if (enabled && hand.size() < cards.length) {
                Vector3f pos = cards[hand.size()].getPosition();
                timerLabel.setPosition(pos.x, pos.y);

            }
        }

    }
    
    public boolean isDeckFull() {
        return hand.size() == cards.length && !isBusted();
    }
    
    public boolean isDeck21() {
        int total = getTotalHigherCardScore();
        return hand.size() == 2 && !isBusted() && total == 21;
    }

    public void doCountDown() {
        counter = 9;
        timerLabel.setText(counter + "");
        timer.start();
    }

    public void stopCountDown() {
        counter = 0;
        timerLabel.setVisible(false);
        timer.stop();
    }

    public int getTotalHigherCardScore() {
        int total = 0;
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            total += card.getHigherValue();
        }
        return total;
    }

    public int getTotalLowerCardScore() {
        int total = 0;
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            total += card.getValue();
        }
        return total;
    }

    public boolean isBusted() {
        int total = getTotalHigherCardScore();
        if (total > 21) {
            total = getTotalLowerCardScore();
            if (total > 21) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isStay() {
        return stay;
    }

    public void setStay(boolean stay) {
        this.stay = stay;
        this.stayTitle.setVisible(stay);
    }

    public void reset() {
        stay = false;
        stayTitle.setVisible(false);
        hand.clear();
        for (int i = 0; i < cards.length; i++) {
            Image c = cards[i];
            c.updatePicture("Interface/cards/blank.png");

        }

    }

}
