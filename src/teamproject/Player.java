/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;
import java.util.*;
/**
 *
 * @author matsumaru
 */
public class Player {
    private List<Card> hand;
    
    public Player(){
        this.hand = new ArrayList<>();
    }
    
    public void drawCard(Card card){
        hand.add(card);
    }
    
    public List<Card> leaveCard(int... arrayNumber){
        List<Card> cards = new ArrayList<>();
        for(int i=0; i<arrayNumber.length;i++){
            cards.add(hand.get(arrayNumber[i]));
        }
        for(int i=arrayNumber.length; i>0;i--){
            Arrays.sort(arrayNumber);
            hand.remove(arrayNumber[i-1]);
        }
        return cards;
    }
    
    public Card seeCard(int n){
        return hand.get(n);
    }
    public List<Card> handCards(){
        return hand;
    }
}
