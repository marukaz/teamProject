/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

import java.util.*;

/**
 *
 * @author matsumaru カードの集合から任意の組み合わせを算出するメソッドを集めたユーティリティクラス
 * 本当はよくないことですが処理の高速化のためソートされている前提で探索します。。。
 */
public class Calc {

//    static boolean matchSuit(List<Card> cards, Suit... suits) {
//        matchFlag = false;
//        List<Card> newCards = new ArrayList<Card>();
//        newCards.addAll(cards);
//        int len = suits.length;
//        for (Card c : cards) {
//            if (c.getSuit() == suits[len - 1]) {
//                if (len == 1) {
//                    matchFlag = true;
//                    return matchFlag;
//                }
//                newCards.remove(c);
//                matchSuit(newCards, Arrays.copyOf(suits, len - 1));
//            }else if(c.getSuit() == Suit.JOKER){
//                newCards.remove(c);
//                
//            }
//        }
//        
//        
//        return matchFlag;
//    }
    static boolean matchSuit(List<Card> cards, Suit... suits) {
        boolean flag = false;
        for (Card c : cards) {
            for (Suit s : suits) {
                if (c.getSuit() == s) {
                    flag = true;
                    break;
                }
            }
            if (!flag && c.getSuit() != Suit.JOKER) {
                return false;
            }
            flag = false;
        }
        return true;
    }

    static List<List<Card>> single(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        for (Card c : cards) {
            List<Card> single = new ArrayList<Card>(1);
            single.add(c);
            rCards.add(single);
        }
        return rCards;
    }

    static List<List<Card>> single(List<Card> cards, Suit suit) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        for (Card c : cards) {
            if (c.getSuit() == suit) {
                List<Card> match = new ArrayList<Card>(1);
                match.add(c);
                rCards.add(match);
            }
        }

        return rCards;
    }

    static List<List<Card>> multi(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 2) {
            return rCards;
        }
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    List<Card> multi = new ArrayList<Card>(2);
                    multi.add(c);
                    multi.add(cards.get(i));
                    rCards.add(multi);
                } else {
                    break;
                }
            }
            k++;
        }
        //ソート前提の実装
        Card wonderJK = cards.get(cards.size() - 1);
        if (wonderJK.getSuit() == Suit.JOKER) {
            for (int i = 0; i < cards.size() - 1; i++) {
                List<Card> multi = new ArrayList<Card>(2);
                multi.add(cards.get(i));
                multi.add(wonderJK);
                rCards.add(multi);
            }
        }
        return rCards;
    }

    static List<List<Card>> multi(List<Card> cards, Suit... suits) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 2) {
            return rCards;
        }
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                Card next = cards.get(i);
                if (c.getNum() == next.getNum()) {
                    if ((c.getSuit() == suits[0] && next.getSuit() == suits[1]) || (c.getSuit() == suits[1] && next.getSuit() == suits[0])) {
                        List<Card> multi = new ArrayList<Card>(2);
                        multi.add(c);
                        multi.add(cards.get(i));
                        rCards.add(multi);
                    }
                } else {
                    break;
                }
            }
            k++;
        }
        //ソート前提の実装
        Card wonderJK = cards.get(cards.size() - 1);
        if (wonderJK.getSuit() == Suit.JOKER) {
            for (Card c : cards) {
                if (c.getSuit() == suits[0] || c.getSuit() == suits[1]) {
                    List<Card> multi = new ArrayList<Card>(2);
                    multi.add(c);
                    multi.add(wonderJK);
                    rCards.add(multi);
                }
            }
        }
        return rCards;
    }

    static List<List<Card>> triple(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 3) {
            return rCards;
        }
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if (c.getNum() == cards.get(j).getNum()) {
                            List<Card> triple = new ArrayList<Card>(3);
                            triple.add(c);
                            triple.add(cards.get(i));
                            triple.add(cards.get(j));
                            rCards.add(triple);
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            k++;
        }
        Card wonderJK = cards.get(cards.size() - 1);
        if (wonderJK.getSuit() == Suit.JOKER) {
            List<List<Card>> multiCards = multi(cards.subList(0, cards.size() - 1));
            for (int i = 0; i < multiCards.size(); i++) {
                List<Card> multiCard = new ArrayList<Card>(3);
                multiCard = multiCards.get(i);
                multiCard.add(wonderJK);
                rCards.add(multiCard);
            }
        }
        return rCards;
    }

    static List<List<Card>> triple(List<Card> cards, Suit... suits) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 3) {
            return rCards;
        }
        List<List<Card>> cardses = triple(cards);
        for (List<Card> cs : cardses) {
            if (matchSuit(cs, suits)) {
                rCards.add(cs);
            }
        }
        return rCards;
    }

    static List<List<Card>> quartet(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 4) {
            return rCards;
        }
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if (c.getNum() == cards.get(j).getNum()) {
                            for (int n = j + 1; n < cards.size(); n++) {
                                if (c.getNum() == cards.get(n).getNum()) {
                                    List<Card> quartet = new ArrayList<Card>(4);
                                    quartet.add(c);
                                    quartet.add(cards.get(i));
                                    quartet.add(cards.get(j));
                                    quartet.add(cards.get(n));
                                    rCards.add(quartet);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            k++;
        }
        Card wonderJK = cards.get(cards.size() - 1);
        if (wonderJK.getSuit() == Suit.JOKER) {
            List<List<Card>> tripleCards = triple(cards.subList(0, cards.size() - 1));
            for (int i = 0; i < tripleCards.size(); i++) {
                List<Card> tripleCard = new ArrayList<Card>(3);
                tripleCard = tripleCards.get(i);
                tripleCard.add(wonderJK);
                rCards.add(tripleCard);
                tripleCard.clear();
            }
        }
        return rCards;
    }

    static List<List<Card>> quartet(List<Card> cards, Suit... suits) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        if (cards.size() < 4) {
            return rCards;
        }
        List<List<Card>> cardses = quartet(cards);
        for (List<Card> cs : cardses) {
            if (matchSuit(cs, suits)) {
                rCards.add(cs);
            }
        }
        return rCards;
    }

    /*
    List<List<Card>> sequence3(List<Card> cards) {
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for (Card c : cards) {
            Card[] skd = new Card[3];
            skd[0] = c;
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() + 1 == cards.get(i).getNum()) {
                    skd[1] = cards.get(i);
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.getNum() + 2 == cards.get(j).getNum()) {
                            skd[2] = cards.get(j);
                            mCards.add(skd);
                        }
                    }
                }
            }
            k++;
        }
        return mCards;
    }

    List<List<Card>> sequence4(List<Card> cards) {
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for (Card c : cards) {
            Card[] ykd = new Card[4];
            ykd[0] = c;
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() + 1 == cards.get(i).getNum()) {
                    ykd[1] = cards.get(i);
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.getNum() + 2 == cards.get(j).getNum()) {
                            ykd[2] = cards.get(j);
                            for (int n = j + 1; n < cards.size(); n++) {
                                if (c.getNum() + 3 == cards.get(n).getNum()) {
                                    ykd[3] = cards.get(n);
                                    mCards.add(ykd);
                                }
                            }
                        }
                    }
                }
            }
            k++;
        }
        return mCards;
    }
     */
}
