/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matsumaru
 */
public class MonteCarloAI extends Player {

    @Override
    int[] chooseCard(PlayRichest game) {
        final int PLAYOUTTIMES = 100;
        List<List<Card>> playableCards = game.playableCalc(hand);
        int rankSumMin = 10000;
        int[] chosen = {-1};

        if (playableCards.isEmpty() || playableCards.get(0).isEmpty()) {
            return chosen;
        }

        for (int i = 0; i < playableCards.size(); i++) {
            try {
                List<Card> cards = playableCards.get(i);
                int rankSum = 0;
                int size = cards.size();
                int[] nums = new int[size];

                for (int k = 0; k < size; k++) {
                    nums[k] = hand.indexOf(cards.get(k));
                }
                for (int k = 0; k < PLAYOUTTIMES; k++) {
                    PlayRichest copyGame = game.clone();
                    rankSum += copyGame.playOut(nums);
                }
                if (rankSum < rankSumMin) {
                    rankSumMin = rankSum;
                    chosen = nums;
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MonteCarloAI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //パスについてのプレイアウト
        if (!game.isFirst) {
            try {
                int[] nums = {-1};
                int rankSum = 0;

                for (int k = 0; k < PLAYOUTTIMES; k++) {
                    PlayRichest copyGame = game.clone();
                    rankSum += copyGame.playOut(nums);
                }
                if (rankSum < rankSumMin) {
                    chosen = nums;
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MonteCarloAI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return chosen;
    }
}
