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
    static Suit[] bindSuits = new Suit[4];

    static boolean isBind = false;
    static boolean isSequance = false;
    static boolean isGameRev = false;
    static boolean is11Rev = false;
    static boolean s3check = false;

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static void gameStart(int playerCount, int joker) {

        deck = new Deck(joker);
        deck.deckMake();

        field = new Field();

        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new RealPlayer();
        }

        //AIの読み込みは今のところここに実行前に書いておくようにしてください(´・ω・｀)
        // players[2] = new AIsample();
        outside:
        while (true) {
            for (Player p : players) {
                p.drawCard(deck.dealCard());
                if (deck.isEmpty()) {
                    break outside;
                }
            }
        }
    }

    private static void printCards(List<Card> cards) {
        for (Card card : cards) {
            if (card.getSuit() == Suit.JOKER) {
                System.out.print("JOKER ");
            } else {
                System.out.print(card.getSuit().getSuitMark() + card.getNum() + " ");
            }
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

    public static boolean cardP_Checker(Card card) {
        if (numOfCards == 1 && card.getSuit() == Suit.JOKER) {
            return true;
        }
        if (isAllJoker(field.getLastCard(numOfCards))) {
            if (numOfCards == 1 && card.getSuit() == Suit.SPADE && card.getNum() == 3) {
                s3check = true;
                return true;
            }
            return false;
        }
        if (isGameRev ^ is11Rev) {
            return cardP(card) < cardP(field.getLastCard());
        } else {
            return cardP(card) > cardP(field.getLastCard());
        }
    }

    public static boolean isAllJoker(List<Card> cards) {
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
                field.clear();
                is11Rev = false;
                System.out.println("\'Eight Cut\'\n");
                System.out.println("now the field consists of\n");
                turn--;
                break;
            case 11:
                is11Rev = true;
                System.out.println("\'Eleven Reverse\'\n");
        }
    }

    public static void main(String args[]) {

        System.out.println("input the number of players");
        try {

            playerCount = Integer.parseInt(br.readLine());
            System.out.println("input the number of jokers");
            joker = Integer.parseInt(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }

        gameStart(playerCount, joker);
        System.out.println("\n***Game Start***\n");
        System.out.println("note : input 0 if you pass the turn.\n\n");

        while (true) {

            boolean isFirst = field.cards().isEmpty();
            int tPlayerNum = turn % playerCount;
            Player tPlayer = players[tPlayerNum];

            System.out.println("turn of Player" + (tPlayerNum + 1));
            printCards(tPlayer.handCards());
            System.out.println("input the number you want to put counting from the left of your hand.");
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
                    passCount = 0;
                    List<Card> play = tPlayer.seeCard(nums);
                    if (isFirst) {
                        numOfCards = nums.length;
                        isSequance = checkSQ(play);
                    } else if (!isBind) {
                        isBind = checkBind(play);
                    }

                    if (numOfCards != 1) {
                        changeJKR(play);
                    }

                    if (isSequance) {
                        if (!checkSQ(play)) {
                            System.out.println("You must put Sequance cards.");
                        } else if (!isFirst && !cardP_Checker(play.get(0))) {
                            System.out.println("you must put the stronger card than last put card.");
                        } else {
                            break;
                        }
                    } else if (isBind && !checkBind(play)) {
                        System.out.println("The field is binded.");
                    } else if (nums.length != numOfCards) {
                        System.out.println("you must put the same card count of the first put cards.");
                    } else if (!isFirst && !cardP_Checker(play.get(0))) {
                        System.out.println("you must put the stronger card than last put card.");
                    } else if (!isCardSame(tPlayer, nums)) {
                        System.out.println("you must put the same number or JOKER cards.");
                    } else {
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
                System.out.println("The Player wins.");
            }

            System.out.println("OK, now the field consists of");
            printCards(field.cards());
            System.out.println();

            efChecker();
            if (s3check) {
                System.out.println("\n\'Spade Three Counter\'\n");
                field.clear();
                s3check = false;
                turn--;
            }
            turn++;
            if (passCount == players.length - 1) {
                System.out.println("***All players passed, so the field has reseted.***\n");
                field.clear();
                isBind = false;
                is11Rev = false;
                isSequance = false;
            }

        }

    }
}
