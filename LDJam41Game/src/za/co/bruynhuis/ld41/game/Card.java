/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.game;

/**
 *
 * @author NideBruyn
 */
public class Card {
    
    public static final String SUITE_PIKES = "pikes";
    public static final String SUITE_CLOVERS = "clovers";
    public static final String SUITE_HEARTS = "hearts";
    public static final String SUITE_DIAM = "diam";
    
    private String suite;
    private int number;
    
    public Card(String suite, int number) {
        this.suite = suite;
        this.number = number;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getValue() {
        int value = 10;
        
        if (number >= 10) {
            value = 10;            
        } else {
            value = number;
        }
        
        return value;
    }
    
    public int getHigherValue() {
        int value = 10;
        
        if (number >= 10) {
            value = 10;            
        } else if (number == 1) {
            return 11;
        } else {
            value = number;
        }
        
        return value;
    }

    public String getCardPath() {
        if (number == 1) {
            return "Interface/cards/Ace_" + suite + ".png";
        } else if (number == 11) {
            return "Interface/cards/Jack_" + suite + ".png";
        } else if (number == 12) {
            return "Interface/cards/Queen_" + suite + ".png";
        } else if (number == 13) {
            return "Interface/cards/King_" + suite + ".png";
        }
        return "Interface/cards/" + number + "_" + suite + ".png";
    }
    
}
