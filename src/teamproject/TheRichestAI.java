/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamproject;

import java.io.*;
import java.util.*;

public class TheRichestAI {
    static int a = 0;
    static List<Card> b = new ArrayList<Card>();
    static Card e;    
    static Card d;
    static Card f;
    static Card g;
    static Card h;
    private static Deck deck;
    private static Player[] players;
    private static Field field;

    static int passCount = 0;
    static final int JOKER = 1;
    static int turn = 0;
    static int numOfCards = 0;
    static int playerCount = 5;
    static int ranking = 1;
    static int AIranking = 0;
    static Suit[] bindSuits = new Suit[4];

    static boolean isFirst = true;
    static boolean isBind = false;
    static boolean isSequance = false;
    static boolean isGameRev = false;
    static boolean is11Rev = false;
    static boolean s3check = false;

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static void gameStart(int playerCount, int joker,List<Card> AIhand) {

        deck = new Deck(joker);
        deck.deckMake();

        field = new Field();

        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new AIsample();
            players[i].giveNumber(i+1);
        }

        //AIの読み込みは今のところここに実行前に書いておくようにしてください(´・ω・｀)
        // players[2] = new AIsample();
        players[0].hand = AIhand;
        outside:
            while (true) {
            for (int i = 1; i < playerCount; i++) {
                players[i].drawCard(deck.dealCard());
                if (deck.isEmpty()) {
                    break outside;
                }
            }
        }
        // 手札のソート
        for (Player p : players) {
            p.handCards().sort(new Comparator<Card>() {
                @Override
                public int compare(Card a, Card b) {
                    return cardP(a) - cardP(b);
                }
            });
        }
    }

    private static void printCards(List<Card> cards) {
        int i = 1;
        for (Card card : cards) {

            if (card.getSuit() == Suit.JOKER) {
                System.out.print("JK ");
            } else {
                System.out.print(card.getSuit().getSuitMark() + card.getNum() + " ");
            }
            i++;
        }
        System.out.println();
    }

    private static void changeJKR(List<Card> cards) {

        int cardNum = 0;

        if (isSequance) {
            int position = 0;
            for (Card c : cards) {
                if (c.getSuit() != Suit.JOKER) {
                    cardNum = c.getNum();
                    position = cards.indexOf(c);
                    break;
                }
            }
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getSuit() == Suit.JOKER) {
                    cards.get(i).number = cardNum + i - position;

                }
            }

        } else {
            for (Card c : cards) {
                if (c.getSuit() != Suit.JOKER) {
                    cardNum = c.getNum();
                    break;
                }
            }
            for (Card c : cards) {
                if (c.getSuit() == Suit.JOKER) {
                    c.number = cardNum;
                }
            }
        }
    }

    private static boolean isCardSame(Player player, int[] arrayNum) {
        int cNum = player.seeCard(arrayNum[0]).getNum();
        for (int i = 1; i < arrayNum.length; i++) {
            if (player.seeCard(arrayNum[i]).getNum() != cNum) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkBind(List<Card> cards) {
        boolean check = false;
        if (!isBind) {
            List<Card> fCards = field.getLastCard(numOfCards);
            for (int i = 0; i < numOfCards; i++) {
                bindSuits[i] = fCards.get(i).getSuit();
            }
        }
        for (Card c : cards) {
            Suit suit = c.getSuit();
            for (Suit s : bindSuits) {
                if (suit == s || suit == Suit.JOKER) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                return false;
            }
            check = false;
        }
        return true;
    }

    private static boolean checkSQ(List<Card> cards) {
        if (numOfCards < 3) {
            return false;
        }
        Card card = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            Card nowc = cards.get(i);
            if (nowc.getSuit() != Suit.JOKER && card.getSuit() != Suit.JOKER) {
                if (card.getSuit() != nowc.getSuit()) {
                    return false;
                }
                if (card.getNum() + 1 != nowc.getNum()) {
                    return false;
                }
            }
            card = nowc;
        }
        return true;
    }

    private static int cardP(Card card) {
        int num = card.getNum();
        if (num > 2) {
            return num;
        } else if (num == 1) {
            return 14;
        } else if (num == 2) {
            return 15;
        }
        return 16;
    }

    private static boolean cardP_Checker(Card card) {
        if (numOfCards == 1 && card.getSuit() == Suit.JOKER) {
            return true;
        }
        if (isGameRev ^ is11Rev) {
            return cardP(card) < cardP(field.getLastCard());
        } else {
            return cardP(card) > cardP(field.getLastCard());
        }
    }

    private static boolean isAllJoker(List<Card> cards) {
        for (Card c : cards) {
            if (c.getSuit() != Suit.JOKER) {
                return false;
            }
        }
        return true;
    }

    private static void efChecker() {
        int num = field.getLastCard().getNum();
        switch (num) {
            case 8:
                clearField();
                turn--;
                break;
            case 11:
                is11Rev = true;
        }
    }

    private static boolean s3checker(Card card) {
        if (field.getLastCard().getSuit() != Suit.JOKER) {
            return false;
        }
        return card.getNum() == 3 && card.getSuit() == Suit.SPADE;
    }
//
//    public static List<Card[]> playableCalc(List<Card> hand) {
//        List<Card[]> pacards = new ArrayList<Card[]>();
//        if (isFirst) {
//            for (int i=0; i<hand.size();i++) {
//                Card[] single = {hand.get(i)};
//                pacards.add(single);
//            }
//            pacards.addAll(Calc.multi(hand));
//        } else if (numOfCards == 1) {
//            for(int i=0; i<hand.size();i++){
//                if(cardP(hand.get(i))>cardP(field.getLastCard())){
//                    
//                }
//            }
//        }
//        return pacards;
//    }

    private static void clearField() {
        field.clear();

        isBind = false;
        is11Rev = false;
        isSequance = false;
        isFirst = true;
    }
    
    public static int play (List<Card> AIhand) {

        gameStart(playerCount, JOKER,AIhand);

        while (true) {

            int tPlayerNum = turn % playerCount;
            Player tPlayer = players[tPlayerNum];
            printCards(tPlayer.handCards());
            // numsにはプログラムとして処理しやすいよう入力-1の値を入れる
            int[] nums;
            String[] sNums;
            while (true) {

                try {
                    if (tPlayer instanceof RealPlayer) {
                        sNums = br.readLine().split(" ");
                        nums = new int[sNums.length];
                        for (int i = 0; i < sNums.length; i++) {
                            nums[i] = (Integer.parseInt(sNums[i]) - 1);
                        }
                    } else {
                        nums = tPlayer.chooseCard();
                    }

                    if (nums[0] == -1) {
                        passCount++;
                        break;
                    }

                    List<Card> play = tPlayer.seeCard(nums);
                    if (isFirst) {
                        numOfCards = nums.length;
                        isSequance = checkSQ(play);
                    }

                    if (numOfCards != 1) {
                        changeJKR(play);
                    }
                    if (numOfCards == 1 && !isFirst) {
                        s3check = s3checker(tPlayer.seeCard(nums[0]));
                        if (s3check) {
                            break;
                        }
                    }

                    if (isSequance) {
                        if (!checkSQ(play)) {
                            System.out.println("You must put Sequance cards.");
                        } else if (!isFirst && !cardP_Checker(play.get(0))) {
                            System.out.println("you must put the stronger card than last put card.");
                        } else {
                            break;
                        }
                    } else if (!isFirst && !cardP_Checker(play.get(0))) {
                        System.out.println("you must put the stronger card than last put card.");
                    } else if (nums.length != numOfCards) {
                        System.out.println("you must put the same card count of the first put cards.");
                    } else if (!isFirst && !isCardSame(tPlayer, nums)) {
                        System.out.println("you must put the same number or JOKER cards.");
                    } else if (isBind && !checkBind(play)) {
                        System.out.println("The field is binded.");
                    } else {
                        passCount = 0;
                        if (!isFirst && !isBind) {
                            isBind = checkBind(play);
                        }
                        isFirst = false;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (passCount == 0) {
                field.addCard(tPlayer.leaveCard(nums));
                if (numOfCards >= 4) {
                    isGameRev = !isGameRev;
                    System.out.println("\n\'Revolution\'\n");
                }
            }

            if (tPlayer.handCards().isEmpty()) {
                if(tPlayerNum == 0){
                    AIranking = ranking;
                    break;
                }
                tPlayer.giveRank(ranking);
                if(ranking == 4 && AIranking == 0){
                    AIranking = 5;
                    break;
                }
                ranking++;
                Player[] restPlayers = new Player[players.length-1];
                int r = 0;
                for(int i=0;i<players.length;i++){
                    if(i != tPlayerNum){
                        restPlayers[r] = players[i];
                        r++;
                    }
                }
                players = restPlayers;
                turn = tPlayerNum;
                playerCount--;
              if(playerCount ==1){
                    break;
              }
            }

            System.out.println("OK, now the field consists of");
            printCards(field.cards());
            System.out.println();

            efChecker();
            if (s3check) {
                System.out.println("\n\'Spade Three Counter\'\n");
                clearField();
                s3check = false;
                turn--;
            }

            turn++;

            if (passCount == players.length - 1) {
                System.out.println("***All players passed, so the field has reseted.***\n");
                clearField();
            }

        }
        return AIranking;
    }
     public static void main(String args[]) {

        try {
            a = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        d = new Card(Suit.JOKER,4);
        e = new Card(Suit.DIAMOND,5);
        f = new Card(Suit.DIAMOND,6);
        g = new Card(Suit.HEART,7);
        h = new Card(Suit.CLUB,3);
        b.add(d);
        b.add(e);
        b.add(f);
        b.add(g);
        b.add(h);
        int c = play(b);
        System.out.println(c);
     }   
}
