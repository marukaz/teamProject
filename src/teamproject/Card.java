/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

import java.util.Objects;

/**
 *
 * @author matsumaru
 */
public class Card {

    public static final int SUITKIND = 4;
    public static final int NUMBERKIND = 13;

    private Suit suit;
    protected int number;
    private int power;
    
    public Card(Suit suit, int number) {
        this.suit = suit;
        this.number = number;
        this.power = number;
    }
    
    public Card(Suit suit, int number, int power) {
        this.suit = suit;
        this.number = number;
        this.power = power;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getNum() {
        return number;
    }

    public int getPow(){
        return power;
    }
    
    public void changePow(int pow){
        power = pow;
    }
    
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Card c = (Card) obj;
        Suit s = c.suit;
        int n = c.number;

        return suit == s && number == n;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.suit);
        hash = 29 * hash + this.number;
        return hash;
    }
}
