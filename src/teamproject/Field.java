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
public class Field {
    private List<Card> cards;
    
    public Field(){
        this.cards = new ArrayList<>();
    }
    
    public void addCard(List<Card> card){
        cards.addAll(card);
    }
    
    public List<Card> cards(){
        return cards;
    }
    
    public Card getLastCard(){
        return cards.get(cards.size()-1);
    }
    
    public void clear(){
        cards.clear();
    }
}
