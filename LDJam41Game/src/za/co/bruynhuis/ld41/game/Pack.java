/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author NideBruyn
 */
public class Pack {
    
    private List<Card> cards = new ArrayList<>();
    private Stack<Card> currentPack = new Stack<>();
    
    public Pack() {
        
        //CLOVERS
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.SUITE_CLOVERS, i));
        }
        
        //DIAM
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.SUITE_DIAM, i));
        }
        
        //HEARTS
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.SUITE_HEARTS, i));
        }
        
        //PIKES
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(Card.SUITE_PIKES, i));
        }
        
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
        
        currentPack.addAll(cards);
    }
    
    public Card drawCard() {
        if (currentPack.isEmpty()) {
            currentPack.addAll(cards);
        }
        
        return currentPack.pop();
    }
}
