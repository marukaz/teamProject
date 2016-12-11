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
        boolean jokerFlag = false;
        for (Card c : cards) {
            for (Suit s : suits) {
                if (c.getSuit() == s) {
                    flag = true;
                    break;
                }
            }
            if(!flag && c.getSuit() != Suit.JOKER){
                return false;
            }
            flag = false;
        }
        return true;
    }

    static List<List<Card>> single(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> single = new ArrayList<Card>(1);
        for (Card c : cards) {
            single.add(c);
            rCards.add(single);
            single.clear();
        }
        return rCards;
    }

    static List<List<Card>> single(List<Card> cards, Suit suit) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> match = new ArrayList<Card>();
        for (Card c : cards) {
            if (c.getSuit() == suit) {
                match.add(c);
                rCards.add(match);
                match.clear();
            }
        }

        return rCards;
    }

    static List<List<Card>> multi(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> multi = new ArrayList<Card>(3);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    multi.add(c);
                    multi.add(cards.get(i));
                    rCards.add(multi);
                    multi.clear();
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
                multi.add(cards.get(i));
                multi.add(wonderJK);
                rCards.add(multi);
                multi.clear();
            }
        }
        return rCards;
    }

    static List<List<Card>> multi(List<Card> cards, Suit suit1, Suit suit2) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> multi = new ArrayList<Card>(3);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                Card next = cards.get(i);
                if (c.getNum() == next.getNum()) {
                    if ((c.getSuit() == suit1 && next.getSuit() == suit2) || (c.getSuit() == suit2 && next.getSuit() == suit1)) {
                        multi.add(c);
                        multi.add(cards.get(i));
                        rCards.add(multi);
                        multi.clear();
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
                if (c.getSuit() == suit1 || c.getSuit() == suit2) {
                    multi.add(c);
                    multi.add(wonderJK);
                    rCards.add(multi);
                    multi.clear();
                }
            }
        }
        return rCards;
    }

    static List<List<Card>> triple(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> triple = new ArrayList<Card>(4);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if (c.getNum() == cards.get(j).getNum()) {
                            triple.add(c);
                            triple.add(cards.get(i));
                            triple.add(cards.get(j));
                            rCards.add(triple);
                            triple.clear();
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
            List<Card> multiCard = new ArrayList<Card>(3);
            for (int i = 0; i < multiCards.size(); i++) {
                multiCard = multiCards.get(i);
                multiCard.add(wonderJK);
                rCards.add(multiCard);
                multiCard.clear();
            }
        }
        return rCards;
    }

    static List<List<Card>> quartet(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> quartet = new ArrayList<Card>(5);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.getNum() == cards.get(i).getNum()) {
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.getNum() == cards.get(j).getNum()) {
                            for (int n = j + 1; n < cards.size(); n++) {
                                if (c.getNum() == cards.get(n).getNum()) {
                                    quartet.add(c);
                                    quartet.add(cards.get(i));
                                    quartet.add(cards.get(j));
                                    quartet.add(cards.get(n));
                                    rCards.add(quartet);
                                    quartet.clear();
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
            List<Card> tripleCard = new ArrayList<Card>(3);
            for (int i = 0; i < tripleCards.size(); i++) {
                tripleCard = tripleCards.get(i);
                tripleCard.add(wonderJK);
                rCards.add(tripleCard);
                tripleCard.clear();
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
