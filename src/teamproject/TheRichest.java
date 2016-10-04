/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;
import java.io.*;
import java.util.*;
/**
 *
 * @author matsumaru
 */
public class TheRichest {
    private static Deck deck;
    private static Player[] players;
    
    public static void gameStart(int playerCount, int joker){
        
        deck = new Deck(joker);
        deck.deckMake();
        
        players = new Player[playerCount];
        for(int i=0;i<playerCount;i++){
            players[i] = new Player();
        }
        
        outside: while(true){
            for(Player p : players){
                p.getCard(deck.dealCard());
                if(deck.isEmpty()) break outside;
            }
        }
    }
    
    public static void main(String args[]){
        int playerCount = 0;
        int joker = 0;
        
        System.out.println("input the number of players");
        try{
            
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        playerCount = Integer.parseInt(br.readLine());
        System.out.println("input the number of jokers");
        joker = Integer.parseInt(br.readLine());
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
        gameStart(playerCount, joker);
        System.out.println("Game Start");
        
        int turn = 0;
        while(true){
            int turnPlayer = turn % playerCount;
            
            System.out.println("turn of Player" + (turnPlayer+1));
            
        }
    }
}
