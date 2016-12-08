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

    static List<List<Card>> multi(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> multi = new ArrayList<Card>(3);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.number == cards.get(i).getNum()) {
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

    static List<List<Card>> triple(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        List<Card> triple = new ArrayList<Card>(4);
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if (c.number == cards.get(i).getNum()) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if (c.number == cards.get(j).getNum()) {
                            triple.add(c);
                            triple.add(cards.get(i));
                            triple.add(cards.get(j));
                            rCards.add(triple);
                            triple.clear();
                        }else{
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

            }
        }

        return rCards;
    }

    List<Card[]> quartet(List<Card> cards) {
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for (Card c : cards) {
            Card[] yon = new Card[4];
            yon[0] = c;
            for (int i = k; i < cards.size(); i++) {
                if (c.number == cards.get(i).getNum()) {
                    yon[1] = cards.get(i);
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.number == cards.get(j).getNum()) {
                            yon[2] = cards.get(j);
                            for (int n = j + 1; n < cards.size(); n++) {
                                if (c.number == cards.get(n).getNum()) {
                                    yon[3] = cards.get(n);
                                    mCards.add(yon);
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

    List<Card[]> sequence3(List<Card> cards) {
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for (Card c : cards) {
            Card[] skd = new Card[3];
            skd[0] = c;
            for (int i = k; i < cards.size(); i++) {
                if (c.number + 1 == cards.get(i).getNum()) {
                    skd[1] = cards.get(i);
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.number + 2 == cards.get(j).getNum()) {
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

    List<Card[]> sequence4(List<Card> cards) {
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for (Card c : cards) {
            Card[] ykd = new Card[4];
            ykd[0] = c;
            for (int i = k; i < cards.size(); i++) {
                if (c.number + 1 == cards.get(i).getNum()) {
                    ykd[1] = cards.get(i);
                    for (int j = i + 1; i < cards.size(); j++) {
                        if (c.number + 2 == cards.get(j).getNum()) {
                            ykd[2] = cards.get(j);
                            for (int n = j + 1; n < cards.size(); n++) {
                                if (c.number + 3 == cards.get(n).getNum()) {
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
}
