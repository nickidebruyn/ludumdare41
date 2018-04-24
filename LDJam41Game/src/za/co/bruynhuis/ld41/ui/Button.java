/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.bruynhuis.ld41.ui;

import com.bruynhuis.galago.ui.button.TouchButton;
import com.bruynhuis.galago.ui.effect.TouchEffect;
import com.bruynhuis.galago.ui.panel.Panel;
import com.jme3.math.ColorRGBA;

/**
 * An implementation of a button.
 
 * @author NideBruyn
 */
public class Button extends TouchButton {
	
    public Button(Panel panel, String id, String text, float scale) {
        super(panel, id, "Interface/button.png", 264*scale, 84*scale, true);
        this.setText(text);
        this.setFontSize(28*scale);
        this.setTextColor(ColorRGBA.White);
        this.setBackgroundColor(ColorRGBA.White);
        this.addEffect(new TouchEffect(this));
    }	
    
    
}
