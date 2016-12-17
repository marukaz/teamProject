package teamproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matsumaru
 */
public class PlayRichest implements Cloneable {

    // monteは0からはじまるので注意　monte = 0ならばプレイヤー1となる
    private int monte = 4;

    private Deck deck;
    public Player[] players;
    private HashMap<Integer, String> rankMap;
    private Field field;

    int passCount = 0;
    final int JOKER = 0;
    int turn = 0;
    int playerCount = 5;
    int turnPlayerNum = 0;
    int numOfCards = 0;
    int ranking = 1;
    Suit[] bindSuits = new Suit[4];

    boolean isFirst = true;
    boolean isBind = false;
    boolean isSequance = false;
    boolean isGameRev = false;
    boolean is11Rev = false;
    boolean s3check = false;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public PlayRichest() {
        deck = new Deck(JOKER);
        deck.deckMake();
        field = new Field();
        this.rankMap = new HashMap<Integer, String>();
    }

    private void gameStart(int playerCount) {
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new MonteCarloAI(100);
            players[i].giveNumber(i + 1);
        }

        //AIの読み込みは今のところここに実行前に書いておくようにしてください(´・ω・｀)
        // players[2] = new AIsample();
        players[monte] = new MonteCarloSuper();
        players[monte].giveNumber(monte + 1);
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

    public void printCards(List<Card> cards) {
        for (Card c : cards) {
            if (c.getSuit() == Suit.JOKER) {
                System.out.print("JK ");
            } else {
                System.out.print(c.getSuit().getSuitMark() + c.getNum() + " ");
            }
        }
        System.out.println();
    }

    private void changeJKR(List<Card> cards) {
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

    private boolean isCardsSameNum(List<Card> cards) {
        int num = -1;
        for (Card c : cards) {
            if (c.getSuit() != Suit.JOKER) {
                num = c.getNum();
                break;
            }
        }
        for (Card c : cards) {
            if (c.getNum() != num && c.getSuit() != Suit.JOKER) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBind(List<Card> cards) {
        boolean check = false;
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

    private boolean checkSQ(List<Card> cards) {
        if (numOfCards < 3) {
            return false;
        }
        Card card = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            Card nowc = cards.get(i);
            if (nowc.getSuit() == Suit.JOKER) {
                if (((card.getPow() + 2 != cards.get(i + 1).getPow()) || card.getSuit() != cards.get(i + 1).getSuit()) && (i != cards.size() - 1)) {
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

    private boolean cardP_Checker(Card card) {
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

    private void efChecker() {
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

    private void efCheckerNonPrint() {
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

    private boolean s3checker(Card card) {
        if (field.getLastCard().getSuit() != Suit.JOKER) {
            return false;
        }
        return card.getNum() == 3 && card.getSuit() == Suit.SPADE;
    }

    public List<List<Card>> playableCalc(List<Card> hand) {
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

    public List<List<Card>> factPlayCalc(List<Card> hand) {
        List<List<Card>> pacards = new ArrayList<List<Card>>();
        List<Card> single = new ArrayList<Card>(1);
        if (isFirst) {
            List<Card> fpcards = hand;
            //    printCards(fpcards);
            List<List<Card>> calced = Calc.quartet(fpcards);
            pacards.addAll(calced);
            fpcards.removeAll(calced);
            //       printCards(fpcards);
            calced = Calc.triple(fpcards);
            pacards.addAll(calced);
            fpcards.removeAll(calced);
            //        printCards(fpcards);
            calced = Calc.multi(fpcards);
            pacards.addAll(calced);
            fpcards.removeAll(calced);
            //        printCards(fpcards);
            calced = Calc.single(fpcards);
            pacards.addAll(calced);
            fpcards.removeAll(calced);
            fpcards = hand;
            calced = Calc.sequance4(fpcards);
            pacards.addAll(calced);
            fpcards.removeAll(calced);
            calced = Calc.sequance3(fpcards);
            pacards.addAll(calced);
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

    private void clearField() {
        field.clear();
        isBind = false;
        is11Rev = false;
        isSequance = false;
        isFirst = true;
    }

    @Override
    public PlayRichest clone() throws CloneNotSupportedException {
        try {
            PlayRichest copy = (PlayRichest) super.clone();
            copy.field = new Field(field.allTimeCards(), field.nowCards());
            Deck restCards = new Deck(JOKER);
            restCards.deckMake();

            restCards.remove(field.allTimeCards());
            //プレイアウトのためのクローンなのでデッキからターンプレイヤーの手札を除いている
            restCards.remove(players[turnPlayerNum].handCards());

            //プレイアウトのためのクローンなのでプレイヤーを全員RandomAI系にしている
            copy.players = new Player[players.length];
            if (players[turnPlayerNum] instanceof MonteCarloSuper) {
                for (int i = 0; i < players.length; i++) {
                    Player tPlayer = players[i];
                    if(i == turnPlayerNum){
                    copy.players[i] = new MonteCarloAI(tPlayer.handCards());
                    }else{
                    copy.players[i] = new MonteCarloAI(restCards.dealCard(tPlayer.handCards().size()));
                    }
                    copy.players[i].giveNumber(i + 1);
                }
            } else if (players[turnPlayerNum] instanceof MonteCarloAI) {
                for (int i = 0; i < players.length; i++) {
                    Player tPlayer = players[i];
                    if(i == turnPlayerNum){
                    copy.players[i] = new RandomAI(tPlayer.handCards());
                    } else{
                    copy.players[i] = new RandomAI(restCards.dealCard(tPlayer.handCards().size()));
                    //   copy.players[i] = new TwoWaysAI(players[i].handCards());
                    }
                    copy.players[i].giveNumber(i + 1);
                }
            } else {
                for (int i = 0; i < players.length; i++) {
                    // copy.players[i] = new RandomAI(restCards.dealCard(players[i].handCards().size()));
                    copy.players[i] = new RandomAI(restCards.dealCard(players[i].handCards().size()));
                }
            }

            copy.bindSuits = Arrays.copyOf(bindSuits, bindSuits.length);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public int playOut(int[] firstChoose) {
        boolean notyet = true;
        while (true) {
            turnPlayerNum = turn % playerCount;
            Player turnPlayer = players[turnPlayerNum];
            int[] nums = new int[1];
            while (true) {
                if (notyet) {
                    nums = firstChoose;
                    notyet = false;
                } else {
                    nums = turnPlayer.chooseCard(this);
                }

                if (nums[0] == -1) {
                    passCount++;
                    break;
                }

                List<Card> play = turnPlayer.seeCard(nums);
                if (isFirst) {
                    numOfCards = nums.length;
                    isSequance = checkSQ(play);
                }

                if (numOfCards != 1) {
                    changeJKR(play);
                }
                if (numOfCards == 1 && !isFirst) {
                    s3check = s3checker(turnPlayer.seeCard(nums[0]));
                    if (s3check) {
                        passCount = 0;
                        break;
                    }
                }

                if (isSequance) {
                    if (!checkSQ(play)) {
                    } else if (!isFirst && !cardP_Checker(play.get(0))) {
                    } else {
                        passCount = 0;
                        isFirst = false;
                        break;
                    }
                } else if (!isFirst && !cardP_Checker(play.get(0))) {
                } else if (nums.length != numOfCards) {
                } else if (!isCardsSameNum(play)) {
                } else if (isBind && !checkBind(play)) {
                } else {
                    passCount = 0;
                    if (!isFirst && !isBind) {
                        isBind = true;
                        boolean check = false;
                        for (Card c : play) {
                            Suit suit = c.getSuit();
                            for (Suit s : bindSuits) {
                                if (suit == s || suit == Suit.JOKER) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                isBind = false;
                                break;
                            }
                            check = false;
                        }
                    }
                    if (!isBind) {
                        for (int i = 0; i < play.size(); i++) {
                            bindSuits[i] = play.get(i).getSuit();
                        }
                    }
                    isFirst = false;
                    break;
                }
            }
            //カードを出す
            if (passCount == 0) {
                field.addCard(turnPlayer.leaveCard(nums));
                if (numOfCards >= 4) {
                    isGameRev = !isGameRev;
                }
            }

            //プレイヤーがあがった時の処理
            if (turnPlayer.handCards().isEmpty()) {
                ranking++;
                if (turnPlayer.playerNum() == monte + 1) {
                    return ranking;
                }
                Player[] restPlayers = new Player[players.length - 1];
                int r = 0;
                for (int i = 0; i < players.length; i++) {
                    if (i != turnPlayerNum) {
                        restPlayers[r] = players[i];
                        r++;
                    }
                }
                players = restPlayers;
                turn = turnPlayerNum;
                playerCount--;
                if (players.length == 1) {
                    return ranking;
                }
            }
            if (players.length == 1) {
                return ranking;
            }

            efCheckerNonPrint();
            if (s3check) {
                clearField();
                s3check = false;
                turn--;
            }

            turn++;

            if (passCount == players.length - 1) {
                clearField();
            }
        }
    }

    public int playOutSuper(int[] firstChoose) {
        for (Player p : players) {
            List<Card> hand = new ArrayList<Card>();
            hand.addAll(p.handCards());
            p = new MonteCarloAI(hand);
        }
        boolean notyet = true;
        while (true) {
            turnPlayerNum = turn % playerCount;
            Player turnPlayer = players[turnPlayerNum];
            int[] nums = new int[1];
            while (true) {
                if (notyet) {
                    nums = firstChoose;
                    notyet = false;
                } else {
                    nums = turnPlayer.chooseCard(this);
                }

                if (nums[0] == -1) {
                    passCount++;
                    break;
                }

                List<Card> play = turnPlayer.seeCard(nums);
                if (isFirst) {
                    numOfCards = nums.length;
                    isSequance = checkSQ(play);
                }

                if (numOfCards != 1) {
                    changeJKR(play);
                }
                if (numOfCards == 1 && !isFirst) {
                    s3check = s3checker(turnPlayer.seeCard(nums[0]));
                    if (s3check) {
                        passCount = 0;
                        break;
                    }
                }

                if (isSequance) {
                    if (!checkSQ(play)) {
                    } else if (!isFirst && !cardP_Checker(play.get(0))) {
                    } else {
                        passCount = 0;
                        isFirst = false;
                        break;
                    }
                } else if (!isFirst && !cardP_Checker(play.get(0))) {
                } else if (nums.length != numOfCards) {
                } else if (!isCardsSameNum(play)) {
                } else if (isBind && !checkBind(play)) {
                } else {
                    passCount = 0;
                    if (!isFirst && !isBind) {
                        isBind = true;
                        boolean check = false;
                        for (Card c : play) {
                            Suit suit = c.getSuit();
                            for (Suit s : bindSuits) {
                                if (suit == s || suit == Suit.JOKER) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                isBind = false;
                                break;
                            }
                            check = false;
                        }
                    }
                    if (!isBind) {
                        for (int i = 0; i < play.size(); i++) {
                            bindSuits[i] = play.get(i).getSuit();
                        }
                    }
                    isFirst = false;
                    break;
                }
            }
            //カードを出す
            if (passCount == 0) {
                field.addCard(turnPlayer.leaveCard(nums));
                if (numOfCards >= 4) {
                    isGameRev = !isGameRev;
                }
            }

            //プレイヤーがあがった時の処理
            if (turnPlayer.handCards().isEmpty()) {
                ranking++;
                if (turnPlayer.playerNum() == monte + 1) {
                    return ranking;
                }
                Player[] restPlayers = new Player[players.length - 1];
                int r = 0;
                for (int i = 0; i < players.length; i++) {
                    if (i != turnPlayerNum) {
                        restPlayers[r] = players[i];
                        r++;
                    }
                }
                players = restPlayers;
                turn = turnPlayerNum;
                playerCount--;
                if (players.length == 1) {
                    return ranking;
                }
            }
            if (players.length == 1) {
                return ranking;
            }

            efCheckerNonPrint();
            if (s3check) {
                clearField();
                s3check = false;
                turn--;
            }

            turn++;

            if (passCount == players.length - 1) {
                clearField();
            }
        }
    }

    public void playGame() {

//        System.out.println("input the number of players");
//        try {
//            playerCount = Integer.parseInt(br.readLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        gameStart(playerCount);
        System.out.println("\n***Game Start***\n");
        System.out.println("note : input 0 if you pass the turn.\n\n");

        while (true) {

            turnPlayerNum = turn % playerCount;
            Player turnPlayer = players[turnPlayerNum];

            System.out.println("Turn of Player" + (turnPlayer.playerNum()));
            printCards(turnPlayer.handCards());
            System.out.println("input the number you want to put counting from the left of your hand.");

            // numsにはプログラムとして処理しやすいよう入力-1の値を入れる
            int[] nums = new int[1];
            String[] sNums;
            while (true) {

                try {
                    if (turnPlayer instanceof RealPlayer) {
                        sNums = br.readLine().split(" ");
                        nums = new int[sNums.length];
                        for (int i = 0; i < sNums.length; i++) {
                            nums[i] = (Integer.parseInt(sNums[i]) - 1);
                        }
                    } else {
                        nums = turnPlayer.chooseCard(this);
                    }

                    if (nums[0] == -1) {
                        passCount++;
                        break;
                    }

                    List<Card> play = turnPlayer.seeCard(nums);
                    if (isFirst) {
                        numOfCards = nums.length;
                        isSequance = checkSQ(play);
                        printCards(play);
                    }

                    if (numOfCards != 1) {
                        changeJKR(play);
                    }
                    if (numOfCards == 1 && !isFirst) {
                        s3check = s3checker(turnPlayer.seeCard(nums[0]));
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
                    } else if (!isCardsSameNum(play)) {
                        System.out.println("you must put the same number or JOKER cards.");
                    } else if (isBind && !checkBind(play)) {
                        System.out.println("The field is binded.");
                    } else {
                        passCount = 0;
                        if (!isFirst && !isBind) {
                            isBind = true;
                            boolean check = false;
                            for (Card c : play) {
                                Suit suit = c.getSuit();
                                for (Suit s : bindSuits) {
                                    if (suit == s || suit == Suit.JOKER) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (!check) {
                                    isBind = false;
                                    break;
                                }
                                check = false;
                            }
                        }
                        if (!isBind) {
                            for (int i = 0; i < play.size(); i++) {
                                bindSuits[i] = play.get(i).getSuit();
                            }
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
                field.addCard(turnPlayer.leaveCard(nums));
                if (numOfCards >= 4) {
                    isGameRev = !isGameRev;
                    System.out.println("\n\'Revolution\'\n");
                }
            }

            System.out.println("OK, now the field consists of");
            printCards(field.nowCards());
            System.out.println();

            efChecker();

            //プレイヤーがあがった時の処理
            if (turnPlayer.handCards().isEmpty()) {
                System.out.println("Player" + turnPlayer.playerNum() + " wins.\n");
                turnPlayer.giveRank(ranking);
                rankMap.put(ranking, "Player" + turnPlayer.playerNum());
                ranking++;
                Player[] restPlayers = new Player[players.length - 1];
                int r = 0;
                for (int i = 0; i < players.length; i++) {
                    if (i != turnPlayerNum) {
                        restPlayers[r] = players[i];
                        r++;
                    }
                }
                players = restPlayers;

                turn = turnPlayerNum - 1;
                playerCount--;
                if (playerCount == 1) {
                    System.out.println("\n *** Game Set ***\n");
                    for (int i = 1; i < rankMap.size() + 1; i++) {
                        String playerName = rankMap.get(i);
                        System.out.println(playerName + " is rank " + i);
                    }
                    System.out.println("Player" + players[0].playerNum() + " is rank 5");
                    for (Player p : players) {
                        System.out.println("");
                    }
                    break;
                }
            }

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
