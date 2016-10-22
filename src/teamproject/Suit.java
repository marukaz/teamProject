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
public enum Suit {

    SPADE("S"),
    DIAMOND("D"),
    CLUB("C"),
    HEART("H"),
    JOKER("JK");

    private String name;

    Suit(String name) {
        this.name = name;
    }

    ;
        
        public String getSuitMark() {
        return name;
    }

}
