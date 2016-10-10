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
    
    static int passCount = 0;
    static int joker = 0;
    static int turn = 0;
    static int numOfCards = 0;
    static int playerCount = 0;
    
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    private static void gameStart(int playerCount, int joker){
        
        deck = new Deck(joker);
        deck.deckMake();
        
        field = new Field();
        
        players = new Player[playerCount];
        for(int i=0;i<playerCount;i++){
            players[i] = new RealPlayer();
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
    
    private static void changeJKR(List<Card> cards){
        int cardNum = 0;
        for (Card c : cards) {
            if(c.getSuit() != Suit.JOKER){
                cardNum = c.getNum();
                break;
            }
        }
        for(Card c : cards){
            if(c.getSuit() == Suit.JOKER){
                c.number = cardNum;
            }
        }
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
    
    private static void efChecker(Card card){
        if(card.getNum() == 8){
            field.clear();
            System.out.println("\'Eight Cut\'\n");
            System.out.println("now the field consists of\n");
        }else{
            turn++;
        }
    } 
    
    public static void main(String args[]){
        
        System.out.println("input the number of players");
        try{
         
        playerCount = Integer.parseInt(br.readLine());
        System.out.println("input the number of jokers");
        joker = Integer.parseInt(br.readLine());
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
        gameStart(playerCount, joker);
        System.out.println("\n***Game Start***\n");
        
        
        while(true){
       
            boolean isFirst = field.cards().isEmpty();
            int tPlayerNum = turn % playerCount;
            Player tPlayer = players[tPlayerNum];
            
            System.out.println("now, turn of Player" + (tPlayerNum+1));
            printCards(tPlayer.handCards());
            System.out.println("input the number you want to put counting from the left of your hand.");
            
            int[] nums = new int[6];
            String[] sNums;
            while(true){
              
                try{
                    if(tPlayer instanceof RealPlayer){
                        sNums = br.readLine().split(" "); 
                    }else{
                        sNums = tPlayer.chooseCard();
                    }
                    if(sNums[0].equals("p")){
                        passCount++;
                        break;
                    }
                    passCount = 0;
                    nums = Arrays.stream(sNums)
                            .mapToInt((String s) -> Integer.parseInt(s) -1)
                            .toArray();
                    
                    changeJKR(tPlayer.seeCard(nums));
                    
                    if(isFirst){
                        numOfCards = nums.length;
                    }
                    
                    if(nums.length != numOfCards){
                        System.out.println("you must put the same card count of the first put cards.");
                    }else if((isFirst != true) && cardP(tPlayer.seeCard(nums[0])) <= cardP(field.getLastCard())){
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
            if(passCount == 0) field.addCard(tPlayer.leaveCard(nums));
            
            
            System.out.println("OK, now the field consists of");
            printCards(field.cards());
            System.out.println();
            
            efChecker(field.getLastCard());
            
            if(passCount == players.length -1){
                System.out.println("***All players passed, so the field has reseted.***\n");
                field.clear();
                
            }
               
        }
           
    }
}
