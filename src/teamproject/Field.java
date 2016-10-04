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
    
    public void addCard(Card card){
        cards.add(0,card);
    }
    
    public List<Card> fieldCards(){
        return cards;
    }
    
    public void clearField(){
        cards.clear();
    }
}
