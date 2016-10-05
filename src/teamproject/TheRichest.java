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
    private static Field field;
    
    private static BufferedReader br;
    
    private static void gameStart(int playerCount, int joker){
        
        deck = new Deck(joker);
        deck.deckMake();
        
        field = new Field();
        
        players = new Player[playerCount];
        for(int i=0;i<playerCount;i++){
            players[i] = new Player();
        }
        
        outside: while(true){
            for(Player p : players){
                p.drawCard(deck.dealCard());
                if(deck.isEmpty()) break outside;
            }
        }
    }
    
    private static void printCards(List<Card> cards){
        cards.forEach((card) -> {
                if(card.getSuit() == Suit.JOKER){
                    System.out.print("JOKER ");
                }else{
                System.out.print(card.getSuit().getSuitMark() + card.getNum() +" ");
                }
            });
            System.out.println();
    }
    
    private static boolean isCardSame(Player player, int[] arrayNum){
        int cNum = player.seeCard(arrayNum[0]).getNum();
        for(int i=1;i<arrayNum.length;i++){
            if(player.seeCard(arrayNum[i]).getNum() != cNum ) return false;
        }
        return true;
    }
    
    private static int cardP(Card card){
        int num = card.getNum();
        if(num > 2){
            return num;
        }else if(num == 1){
            return 14;
        }else if(num == 2){
            return 15;
        }
        return 16;
    }
    public static void main(String args[]){
        int playerCount = 0;
        int joker = 0;
        br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("input the number of players");
        try{
         
        playerCount = Integer.parseInt(br.readLine());
        System.out.println("input the number of jokers");
        joker = Integer.parseInt(br.readLine());
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
        gameStart(playerCount, joker);
        System.out.println("Game Start");
        
        int turn = 0;
        int numOfCards = 0;
        int passCount = 0;
        while(true){
       
            boolean isFirst = field.cards().isEmpty();
            int tPlayerNum = turn % playerCount;
            Player tPlayer = players[tPlayerNum];
            System.out.println("now, turn of Player" + (tPlayerNum+1));
            printCards(tPlayer.handCards());
            System.out.println("input the number you want to put counting from the left of your hand.");
            int[] nums = new int[6];
            
            while(true){
              
                try{
                    String[] sNums = br.readLine().split(" "); 
                    if(sNums[0].equals("p")){
                        passCount++;
                        turn++;
                        break;
                    }
                    passCount = 0;
                    nums = Arrays.stream(sNums)
                            .mapToInt(s -> Integer.parseInt(s) -1)
                            .toArray();
                    
                    if(isFirst){
                        numOfCards = nums.length;
                    }
                    
                    
                    if(nums.length != numOfCards){
                        System.out.println("you must put the same card count of the first put cards.");
                    }else if(isFirst != true && cardP(field.getLastCard()) >= cardP(tPlayer.seeCard(nums[0]))){
                        System.out.println("you must put a stronger card than last put card.");
                    }else if(!isCardSame(tPlayer, nums)){
                        System.out.println("you must put the same number cards.");
                    }else
                    {
                        break;
                    }
               
                    
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(passCount == 0){
                field.addCard(tPlayer.leaveCard(nums));
                turn++;
            }
            
            System.out.println("OK, now the field consists of");
            printCards(field.cards());
            System.out.println();
            
            if(passCount == players.length -1){
                System.out.println("***All players passed, so reset the field.***\n");
                field.clear();
                
            }
                
                
        }
           
    }
}
