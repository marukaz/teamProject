/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

/**
 *
 * @author matsumaru
 */
public class AIsample extends Player {

    @Override
    int[] chooseCard() {
        int[] chosen = new int[20];
        if (TheRichest.isFirst) {
            chosen[0] = (int) (Math.random() * hand.size());
        } else {
            int num = TheRichest.numOfCards;
            chosen = new int[num];
            for (int i = 0; i < num; i++) {
                chosen[i] = (int) (Math.random() * hand.size());
            }
        }
        return chosen;
    }

}
