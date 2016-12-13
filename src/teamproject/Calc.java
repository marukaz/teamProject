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

    // 3,4や13,1といった組み合わせを返す。大富豪ではシークエンスで使うのみなので同マークであることのチェックもしている。
    private static List<List<Card>> sequance2(List<Card> cards) {
        List<List<Card>> rCards = new ArrayList<List<Card>>();
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                Card next = cards.get(i);
                if ((c.getPow() + 1 == next.getPow()) && (c.getSuit() == cards.get(i).getSuit())) {
                    List<Card> step = new ArrayList<Card>(2);
                    step.add(c);
                    step.add(cards.get(i));
                    rCards.add(step);
                }
            }
            k++;
        }
        return rCards;
    }

    // 5,7や13,1といった組み合わせを返す。大富豪ではシークエンスで使うのみなので同マークであることのチェックもしている。
    private static List<List<Card>> skipCards2(List<Card> cards) {
        List<List<Card>> mCards = new ArrayList<List<Card>>();
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                Card next = cards.get(i);
                if ((c.getPow() + 2 == next.getPow()) && (c.getSuit() == cards.get(i).getSuit())) {
                    List<Card> skip = new ArrayList<Card>(2);
                    skip.add(c);
                    skip.add(next);
                    mCards.add(skip);
                }
            }
            k++;
        }
        return mCards;
    }

    private static List<List<Card>> skipCards1and2(List<Card> cards) {
        List<List<Card>> mCards = new ArrayList<List<Card>>();
        int k = 1;
        int size = cards.size();
        for (Card c : cards) {
            int cPow = c.getPow();
            Suit cSuit = c.getSuit();
            for (int i = k; i < size; i++) {
                Card next = cards.get(i);
                int nextPow = next.getPow();
                Suit nextSuit = next.getSuit();
                if ((cPow + 1 == nextPow) && (cSuit == nextSuit)) {
                    for (int j = i; j < size; j++) {
                        Card next2 = cards.get(j);
                        if ((cPow + 3 == next2.getPow()) && (cSuit == next2.getSuit())) {
                            List<Card> skip = new ArrayList<Card>(3);
                            skip.add(c);
                            skip.add(next);
                            skip.add(next2);
                            mCards.add(skip);
                        }
                    }
                } else if ((cPow + 2 == nextPow) && (cSuit == nextSuit)) {
                    for (int j = i; j < size; j++) {
                        Card next2 = cards.get(j);
                        if ((cPow + 3 == next2.getPow()) && (cSuit == next2.getSuit())) {
                            List<Card> skip = new ArrayList<Card>(3);
                            skip.add(c);
                            skip.add(next);
                            skip.add(next2);
                            mCards.add(skip);
                        }
                    }
                }
            }
            k++;
        }
        return mCards;
    }

    // シークエンスのしばりは難しくはないが今のところ未実装
    public static List<List<Card>> sequance3(List<Card> cards) {
        List<List<Card>> mCards = new ArrayList<List<Card>>();
        if (cards.size() < 3) {
            return mCards;
        }
        int k = 1;
        for (Card c : cards) {
            for (int i = k; i < cards.size(); i++) {
                if ((c.getPow() + 1 == cards.get(i).getPow()) && (c.getSuit() == cards.get(i).getSuit())) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if ((c.getPow() + 2 == cards.get(j).getPow()) && (c.getSuit() == cards.get(j).getSuit())) {
                            List<Card> sequance3 = new ArrayList<Card>(3);
                            sequance3.add(c);
                            sequance3.add(cards.get(i));
                            sequance3.add(cards.get(j));
                            mCards.add(sequance3);
                        }
                    }
                }
            }
            k++;
        }

        Card wonderJK = cards.get(cards.size() - 1);
        if (wonderJK.getSuit() == Suit.JOKER) {
            List<List<Card>> sequ2Cards = sequance2(cards.subList(0, cards.size() - 1));
            for (int i = 0; i < sequ2Cards.size(); i++) {
                List<Card> sequ3CardA = new ArrayList<Card>(3);
                sequ3CardA.addAll(sequ2Cards.get(i));
                sequ3CardA.add(wonderJK);
                mCards.add(sequ3CardA);
                List<Card> sequ3CardB = new ArrayList<Card>(3);
                sequ3CardB.add(wonderJK);
                sequ3CardB.addAll(sequ2Cards.get(i));
                mCards.add(sequ3CardB);
            }
            List<List<Card>> skipCards = skipCards2(cards.subList(0, cards.size() - 1));
            for (int i = 0; i < skipCards.size(); i++) {
                List<Card> sequ3CardA = new ArrayList<Card>(3);
                sequ3CardA.addAll(skipCards.get(i));
                sequ3CardA.add(1, wonderJK);
                mCards.add(sequ3CardA);
            }

        }
        return mCards;
    }

    public static List<List<Card>> sequance4(List<Card> cards) {
        List<List<Card>> mCards = new ArrayList<List<Card>>();
        if (cards.size() < 4) {
            return mCards;
        }
        int k = 1;
        for (Card c : cards) {
            int cPow = c.getPow();
            Suit cSuit = c.getSuit();
            for (int i = k; i < cards.size(); i++) {
                if ((cPow + 1 == cards.get(i).getPow()) && (cSuit == cards.get(i).getSuit())) {
                    for (int j = i + 1; j < cards.size(); j++) {
                        if ((cPow + 2 == cards.get(j).getPow()) && (cSuit == cards.get(j).getSuit())) {
                            for (int n = j + 1; n < cards.size(); n++) {
                                if ((cPow + 3 == cards.get(n).getPow()) && (cSuit == cards.get(n).getSuit())) {
                                    List<Card> sequance4 = new ArrayList<Card>(4);
                                    sequance4.add(c);
                                    sequance4.add(cards.get(i));
                                    sequance4.add(cards.get(j));
                                    sequance4.add(cards.get(n));
                                    mCards.add(sequance4);
                                }
                            }
                        }
                    }
                    k++;
                }
            }

            Card wonderJK = cards.get(cards.size() - 1);
            if (wonderJK.getSuit() == Suit.JOKER) {
                List<List<Card>> sequance3 = sequance3(cards.subList(0, cards.size() - 1));
                for (int i = 0; i < sequance3.size(); i++) {
                    List<Card> sequ4CardA = new ArrayList<Card>(4);
                    sequ4CardA.addAll(sequance3.get(i));
                    sequ4CardA.add(wonderJK);
                    mCards.add(sequ4CardA);
                    List<Card> sequ4CardB = new ArrayList<Card>(4);
                    sequ4CardB.add(wonderJK);
                    sequ4CardB.addAll(sequance3.get(i));
                    mCards.add(sequ4CardB);
                }
                List<List<Card>> skipCards = skipCards1and2(cards.subList(0, cards.size() - 1));
                for (int i = 0; i < skipCards.size(); i++) {
                    List<Card> skipCard = skipCards.get(i);
                    List<Card> sequ4Card = new ArrayList<Card>(4);
                    if (skipCard.get(0).getPow() + 1 == skipCard.get(1).getPow()) {
                        sequ4Card.addAll(skipCard);
                        sequ4Card.add(2, wonderJK);
                        mCards.add(sequ4Card);
                    } else {
                        sequ4Card.addAll(skipCard);
                        sequ4Card.add(1, wonderJK);
                        mCards.add(sequ4Card);
                    }
                }
            }
        }
        return mCards;
    }
}
