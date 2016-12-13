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
public class Deck {

    private List<Card> deck;
    private int joker;

    public Deck(int joker) {
        this.deck = new ArrayList<Card>();
        this.joker = joker;
    }

    public void deckMake() {
        for (Suit s : Suit.values()) {
            for (int k = 1; k <= Card.NUMBERKIND; k++) {
                if (s == Suit.JOKER) {
                    break;
                } else if (k == 1) {
                    deck.add(new Card(s, k, 14));
                } else if (k == 2) {
                    deck.add(new Card(s, k, 15));
                } else {
                    deck.add(new Card(s, k));
                }
            }
        }

        for (int i = 0; i < joker; i++) {

            deck.add(new Card(Suit.JOKER, 99));
        }

        Collections.shuffle(deck);
    }

    public Card dealCard() {
        return deck.remove(0);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public int size() {
        return deck.size();
    }
    
    public void remove(List<Card> cards){
        deck.removeAll(cards);
    }
}
