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
 *
 */
public class TheRichest {

    private static Deck deck;
    private static Player[] players;
    private static Field field;

    static int passCount = 0;
    static final int JOKER = 1;
    static int turn = 0;
    static int numOfCards = 0;
    static int playerCount = 0;
    static int ranking = 1;
    static Suit[] bindSuits = new Suit[4];

    static boolean isFirst = true;
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
            players[i] = new MonteCarloAI();
            players[i].giveNumber(i + 1);
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
        // 手札のソート
        for (Player p : players) {
            Collections.sort(p.handCards(), new Comparator<Card>() {
                @Override
                public int compare(Card a, Card b) {
                    return a.getPow() - b.getPow();
                }
            });
        }
    }

    private static void printCards(List<Card> cards) {
        for (Card c : cards) {
            if (c.getSuit() == Suit.JOKER) {
                System.out.print("JK ");
            } else {
                System.out.print(c.getSuit().getSuitMark() + c.getNum() + " ");
            }
        }
        System.out.println();
    }

    private static void changeJKR(List<Card> cards) {
        int cardPow = 0;

        if (isSequance) {
            int position = 0;
            for (Card c : cards) {
                if (c.getSuit() != Suit.JOKER) {
                    cardPow = c.getPow();
                    position = cards.indexOf(c);
                    break;
                }
            }
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getSuit() == Suit.JOKER) {
                    cards.get(i).changePow(cardPow + i - position);

                }
            }

        } else {
            for (Card c : cards) {
                if (c.getSuit() != Suit.JOKER) {
                    cardPow = c.getPow();
                    break;
                }
            }
            for (Card c : cards) {
                if (c.getSuit() == Suit.JOKER) {
                    c.changePow(cardPow);
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
            if (nowc.getSuit() == Suit.JOKER) {
                if (((card.getPow() + 2 != cards.get(i+1).getPow()) || card.getSuit() != cards.get(i+1).getSuit())&& (i != cards.size() - 1)) {
                    return false;
                }
            } else if (card.getSuit() == Suit.JOKER) {

            } else {
                if (card.getSuit() != nowc.getSuit()) {
                    return false;
                }
                if (card.getPow() + 1 != nowc.getPow()) {
                    return false;
                }
            }
            card = nowc;
        }

        return true;
    }

    private static boolean cardP_Checker(Card card) {
        if (numOfCards == 1 && card.getSuit() == Suit.JOKER) {
            return true;
        }

        if (numOfCards == 1 && field.getLastCard().getSuit() == Suit.JOKER) {
            return false;
        }

        if (isGameRev ^ is11Rev) {
            return card.getPow() < field.getLastCard().getPow();
        } else {
            return card.getPow() > field.getLastCard().getPow();
        }
    }

    private static void efChecker() {
        int num = field.getLastCard().getNum();
        switch (num) {
            case 8:
                clearField();
                System.out.println("\'Eight Cut\'\n");
                System.out.println("now the field consists of\n");
                turn--;
                break;
            case 11:
                is11Rev = true;
                System.out.println("\'Eleven Reverse\'\n");
        }
    }

    private static boolean s3checker(Card card) {
        if (field.getLastCard().getSuit() != Suit.JOKER) {
            return false;
        }
        return card.getNum() == 3 && card.getSuit() == Suit.SPADE;
    }

    public static List<List<Card>> playableCalc(List<Card> hand) {
        List<List<Card>> pacards = new ArrayList<List<Card>>();
        List<Card> single = new ArrayList<Card>(1);
        if (isFirst) {
            pacards.addAll(Calc.single(hand));
            pacards.addAll(Calc.multi(hand));
            pacards.addAll(Calc.triple(hand));
            pacards.addAll(Calc.quartet(hand));
            pacards.addAll(Calc.sequance3(hand));
            pacards.addAll(Calc.sequance4(hand));
        } else {
            List<Card> subHand = new ArrayList<Card>(hand.size());
            for (Card c : hand) {
                if (cardP_Checker(c)) {
                    subHand.add(c);
                }
            }
            switch (numOfCards) {
                case 1:
                    if (field.getLastCard().getSuit() == Suit.JOKER) {
                        Card spade3 = new Card(Suit.SPADE, 3);
                        if (hand.contains(spade3)) {
                            single.add(spade3);
                            pacards.add(single);
                        }
                    } else if (isBind) {
                        pacards.addAll(Calc.single(subHand, field.getLastCard().getSuit()));
                    } else {
                        pacards.addAll(Calc.single(subHand));
                    }
                    break;

                case 2:
                    if (isBind) {
                        pacards.addAll(Calc.multi(subHand, field.getSuits(2)));
                    } else {
                        pacards.addAll(Calc.multi(subHand));
                    }
                    break;

                case 3:
                    if (isBind) {
                        pacards.addAll(Calc.triple(subHand, field.getSuits(3)));
                    } else if (isSequance) {
                        pacards.addAll(Calc.sequance3(subHand));
                    } else {
                        pacards.addAll(Calc.triple(subHand));
                    }
                    break;
                case 4:
                    if (isBind) {
                        pacards.addAll(Calc.multi(subHand, field.getSuits(4)));
                    } else if (isSequance) {
                        pacards.addAll(Calc.sequance4(subHand));
                    } else {
                        pacards.addAll(Calc.quartet(subHand));
                    }
                    break;
            }
        }
        return pacards;
    }

    private static void clearField() {
        field.clear();
        isBind = false;
        is11Rev = false;
        isSequance = false;
        isFirst = true;
    }

    public static void main(String args[]) {
        System.out.println("input the number of players");
        try {
            playerCount = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameStart(playerCount, JOKER);
        System.out.println("\n***Game Start***\n");
        System.out.println("note : input 0 if you pass the turn.\n\n");

        while (true) {

            int tPlayerNum = turn % playerCount;
            Player tPlayer = players[tPlayerNum];

            System.out.println("Turn of Player" + (tPlayer.playerNum()));
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

                    List<Card> play = tPlayer.seeCard(nums);
                    if (isFirst) {
                        numOfCards = nums.length;
                        isSequance = checkSQ(play);
                        printCards(play);
                    }

                    if (numOfCards != 1) {
                        changeJKR(play);
                    }
                    if (numOfCards == 1 && !isFirst) {
                        s3check = s3checker(tPlayer.seeCard(nums[0]));
                        if (s3check) {
                            passCount = 0;
                            break;
                        }
                    }

                    if (isSequance) {
                        if (!checkSQ(play)) {
                            System.out.println("You must put Sequance cards.");
                        } else if (!isFirst && !cardP_Checker(play.get(0))) {
                            System.out.println("you must put the stronger card than last put card.");
                        } else {
                            passCount = 0;
                            isFirst = false;
                            break;
                        }
                    } else if (!isFirst && !cardP_Checker(play.get(0))) {
                        System.out.println("you must put the stronger card than last put card.");
                    } else if (nums.length != numOfCards) {
                        System.out.println("you must put the same card count of the first put cards.");
                    } else if (!isCardSame(tPlayer, nums)) {
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

            //カードを出す
            if (passCount == 0) {
                field.addCard(tPlayer.leaveCard(nums));
                if (numOfCards >= 4) {
                    isGameRev = !isGameRev;
                    System.out.println("\n\'Revolution\'\n");
                }
            }

            if (tPlayer.handCards().isEmpty()) {
                System.out.println("The Player wins.");
                tPlayer.giveRank(ranking);
                ranking++;
                Player[] restPlayers = new Player[players.length - 1];
                int r = 0;
                for (int i = 0; i < players.length; i++) {
                    if (i != tPlayerNum) {
                        restPlayers[r] = players[i];
                        r++;
                    }
                }
                players = restPlayers;
                turn = tPlayerNum;
                playerCount--;
                if (playerCount == 1) {
                    System.out.println("\n *** Game Set ***\n");
                    break;
                }
            }

            System.out.println("OK, now the field consists of");
            printCards(field.nowCards());
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

    }
}
