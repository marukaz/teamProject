/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

/**
 *
 * @author matsumaru
 */
public class Card {
    
    
    public static final int SUITKIND = 4;
    public static final int NUMBERKIND = 13;
    
    private Suit suit;
    protected int number;
    
    public Card(Suit suit, int number){
        this.suit = suit;
        this.number = number;
    }
    
    public Suit getSuit(){
        return suit;
    }
    
    public int getNum(){
        return number;
    }
    
    
}
