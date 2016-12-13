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
public class MonteCarloAI extends Player {

    @Override
    int[] chooseCard() {
        List<List<Card>> playableCards = TheRichest.playableCalc(hand);
        double rand = Math.random();
        int[] nums;
        if(playableCards.isEmpty() || playableCards.get(0).isEmpty()){
            int[] pass = {-1};
            return pass;
        }
        if (rand > 0.5) {
            List<Card> playCards = playableCards.get(0);
            nums = new int[playCards.size()];
            for (int i = 0; i < playCards.size(); i++) {
                nums[i] = hand.indexOf(playCards.get(i));
            }
        } else {
            List<Card> playCards = playableCards.get(playableCards.size()-1);
            nums = new int[playCards.size()];
            for (int i = 0; i < playCards.size(); i++) {
                nums[i] = hand.indexOf(playCards.get(i));
            }
        }
            return nums;
    }
}