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
    private List<Card> allCards;
    private List<Card> nowCards;
    
    public Field(){
        this.allCards = new ArrayList<Card>();
        this.nowCards = new ArrayList<Card>();
    }
    
    public void addCard(List<Card> card){
        nowCards.addAll(card);
        allCards.addAll(card);
    }
    
    public List<Card> allTimeCards(){
        return allCards;
    }
    
    public List<Card> nowCards(){
        return nowCards;
    }
    
    public Card getCard(int num){
        return allCards.get(allCards.size() - 1 - num);
    }
        
    public Suit[] getSuits(int num){
        List<Card> sCards = getLastCard(num);
        Suit[] suits = new Suit[num];
        for(int i=0; i<num; i++){
            suits[i] = sCards.get(i).getSuit();
        }
        return suits;
    }
    
    
    public Card getLastCard(){
        return allCards.get(allCards.size()-1);
    }
    
    public List<Card> getLastCard(int numOfCards){
        int last = allCards.size();
        return new ArrayList<Card>(allCards.subList(last-numOfCards, last));
    }
    
    public void clear(){
        nowCards.clear();
    }
    
    public void allClear(){
        allCards.clear();
        nowCards.clear();
    }
}
