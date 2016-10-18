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
abstract class Player {
    public List<Card> hand;
    
    public Player(){
        this.hand = new ArrayList<Card>();
    }
    
    public void drawCard(Card card){
        hand.add(card);
    }
    
    public List<Card> leaveCard(int[] arrayNumber){
        List<Card> cards = new ArrayList<Card>();
        for(int i=0; i<arrayNumber.length;i++){
            cards.add(hand.get(arrayNumber[i]));
        }
        //配列の後ろのほうからremoveしないとずれる
        for(int i=arrayNumber.length; i>0;i--){
            Arrays.sort(arrayNumber);
            hand.remove(arrayNumber[i-1]);
        }
        return cards;
    }
    
    public Card seeCard(int n){
        return hand.get(n);
    }
    
    public List<Card> seeCard(int[] n){
        List<Card> cards = new ArrayList<Card>();
        for(int i=0; i<n.length; i++){
            cards.add(hand.get(n[i]));
        }
        return cards;
    }
    
    public List<Card> handCards(){
        return hand;
    }
    
    abstract int[] chooseCard();
}
