import java.util.Random;

public class Game {

    private int round = 0;
    private int currentPlayer = 0;
    private final Player[] players;
    private int[] dice = new int[5];

    public Game(int num) {
        players = new Player[num];
    }

    public int[] rollDice(int[] dice) {
        Random r = new Random();
        for (int i = 0; i < dice.length; i++) {
            dice[i] = 1 + r.nextInt(6);
        }
        return dice;
    }

    public boolean chooseCategory(String category) {
        switch (category) {
            case "3same" -> {
                return nSame(3);
            }
            case "4same" -> {
                return nSame(4);
            }
            case "full" -> {
                return fullHouse();
            }
            case "small" -> {
                return straight(4);
            }
            case "large" -> {
                return straight(5);
            }
            case "yahtzee" -> {
                return yahtzee();
            }
            case "chance" -> {
                return chance();
            }
            default -> {
                return upper(category);
            }
        }
    }

    private boolean upper(String same) {
        if (players[currentPlayer].isCategoryEmpty(same)) {
            players[currentPlayer].update(dice, same, true);
            return true;
        }
        return false;
    }

    private boolean nSame(int n) {
        String category = n + "same";
        int[] count = new int[6];
        for (int num : dice) {
            count[num - 1]++;
        }
        boolean isValid = false;
        for (int i = 0; i < 6; i++) {
            if (count[i] >= 3) {
                isValid = true;
                break;
            }
        }
        if (isValid && players[currentPlayer].isCategoryEmpty(category)) {
            players[currentPlayer].update(dice, category, true);
            return true;
        }
        return false;
    }

    private boolean fullHouse() {
        int[] count = new int[6];
        for (int num : dice) {
            count[num - 1] += 1;
        }
        int isValid = 0;
        boolean pair = true;
        for (int i = 0; i < 6; i++) {
            if (count[i] == 2 && pair) {
                pair = false;
                isValid++;
            } else if (count[i] == 3) {
                isValid++;
            }
        }
        if (isValid == 2 && players[currentPlayer].isCategoryEmpty("full")) {
            players[currentPlayer].update(dice, "full", true);
            return true;
        }
        return false;
    }

    private boolean straight(int length) {
        dice = Utils.sortDice(dice);
        String category = "";
        boolean isValid = true;
        if (length == 4) {
            category = "small";
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3 || isValid; j++) {        // test later
                    if (dice[i + j] + 1 != dice[i + j + 1]) {
                        isValid = false;
                        break;
                    }
                }
            }
        } else if (length == 5) {
            category = "large";
            for (int i = 0; i < 4; i++) {
                if (dice[i] + 1 != dice[i + 1]) {
                    isValid = false;
                    break;
                }
            }
        }
        if (isValid && players[currentPlayer].isCategoryEmpty(category)) {
            players[currentPlayer].update(dice, category, true);
            return true;
        }
        return false;
    }

    private boolean yahtzee() {
        boolean isValid = true;
        for (int num : dice) {
            if (num != dice[0]) {
                isValid = false;
                break;
            }
        }
        if (isValid) {
            if (players[currentPlayer].isCategoryEmpty("chance")) {
                players[currentPlayer].update(dice, "chance", true);
            } else {
                players[currentPlayer].extraYahtzee();
                // joker code here
            }
            return true;
        }
        return false;
    }

    private boolean chance() {
        if (players[currentPlayer].isCategoryEmpty("chance")) {
            players[currentPlayer].update(dice, "chance", true);
            return true;
        }
        return false;
    }

}
