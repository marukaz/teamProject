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
    
    public void getCard(Card card){
        hand.add(card);
    }
    
    public Card leaveCard(int arrayNumber){
        return hand.remove(arrayNumber);
    }
    
    public List<Card> handCards(){
        return hand;
    }
}
