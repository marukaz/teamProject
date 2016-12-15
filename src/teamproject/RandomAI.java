/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

import java.util.List;

/**
 *
 * @author matsumaru
 */
public class RandomAI extends Player {

    RandomAI() {
        super();
    }
    
    RandomAI(List<Card> handCards) {
        super(handCards);
    }

    @Override
    int[] chooseCard(PlayRichest game) {
        List<List<Card>> playableCards = game.playableCalc(hand);
        double rand = Math.random();
        int[] nums = {-1};
        if (playableCards.isEmpty() || playableCards.get(0).isEmpty()) {
            return nums;
        }

        if (!game.isFirst && rand > 0.9) {
            return nums;
        } else if (rand > 0.5) {
            List<Card> playCards = playableCards.get(0);
            nums = new int[playCards.size()];
            for (int i = 0; i < playCards.size(); i++) {
                nums[i] = hand.indexOf(playCards.get(i));
            }
        } else {
            List<Card> playCards = playableCards.get(playableCards.size() - 1);
            nums = new int[playCards.size()];
            for (int i = 0; i < playCards.size(); i++) {
                nums[i] = hand.indexOf(playCards.get(i));
            }
        }
        return nums;
    }
}
