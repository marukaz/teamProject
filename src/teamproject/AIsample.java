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
public class AIsample extends Player{

    @Override
    int[] chooseCard() {
        int num = TheRichest.numOfCards;
        int[] chosen = new int[num];
        for(int i=0;i<num;i++){
            chosen[i] = (int) (Math.random() * hand.size())-1;
        }
        return chosen;
    }
        
}
