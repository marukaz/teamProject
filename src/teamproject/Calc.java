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
 * カードの集合から任意の組み合わせを算出するメソッドを集めたユーティリティクラス
 * 本当はよくないことですが処理の高速化のためソートされている前提で探索します。。。
 */
public class Calc {
    static List<Card[]> multi(List<Card> cards){
        List<Card[]> mCards = new ArrayList<Card[]>();
        int k = 1;
        for(Card c : cards){
            for(int i=k;i<cards.size();i++){
                if(c.number == cards.get(i).getNum()){
                    Card[] multi = new Card[2];
                    multi[0] = c;
                    multi[1] = cards.get(i);
                    mCards.add(multi);
                }
            }
            k++;
        }
        return mCards;
    }
    
    
}
