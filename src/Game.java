import java.util.Random;

public class Game {

    private GUI gui;
    private int round;
    private int currentPlayer;
    private int[] dice;
    private Player[] players;
    private final String[] categoryNames;

    public Game(int numberOfPlayers) {
        gui = new GUI();
        round = 1;
        currentPlayer = 0;
        dice = new int[5];
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(i + 1);
        }
        categoryNames = new String[]{
                "1", "2", "3", "4", "5", "6",
                "3same", "4same", "full", "small", "large", "chance", "yahtzee"
        };
    }

    public void run() {
        while (round <= 13) {
            gui.setRoundNum(round);
            while (currentPlayer < players.length) {
                gui.changeDisplayedPlayer(players[currentPlayer]);

                gui.setFeedbackText("Press to finalize dice");
                rollDice(new boolean[]{true, true, true, true, true});
                String[] diceNames = new String[]{"dice1", "dice2", "dice3", "dice4", "dice5"};
                int timesRerolled = 0;
                boolean[] selectedDice = new boolean[5];
                String pressed;
                do {
                    pressed = buttonInput();
                    for (int i = 0; i < 5; i++) {
                        if (pressed.equals(diceNames[i])) {
                            if (selectedDice[i]) {
                                gui.unselectDice(i);
                                selectedDice[i] = false;
                            } else {
                                gui.selectDice(i);
                                selectedDice[i] = true;
                            }
                        }
                    }
                    if (pressed.equals("reroll")) {
                        rollDice(selectedDice);
                        selectedDice = new boolean[5];
                        timesRerolled++;
                    }
                } while (!pressed.equals("feedback") && timesRerolled < 2);

                boolean successful = true;
                do {
                    successful = chooseCategory(successful);
                } while (!successful);
                gui.changeDisplayedPlayer(players[currentPlayer]);

                if (currentPlayer != players.length - 1) {
                    gui.setFeedbackText("Press for next player's turn");
                    do {
                        pressed = buttonInput();
                    } while (!pressed.equals("feedback"));
                }
                currentPlayer++;
            }
            gui.setFeedbackText("Press for next round");
            String pressed;
            do {
                pressed = buttonInput();
            } while (!pressed.equals("feedback"));
            currentPlayer = 0;
            round++;
        }
    }

    private void rollDice(boolean[] selected) {
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            if (selected[i]) {
                dice[i] = 1 + r.nextInt(6);
            }
        }
        gui.displayDice(dice);
    }

    private String buttonInput() {
        while (gui.getPressed().equals("")) {
            System.out.print("");       // necessary lag for the code to work
        }
        String pressed = gui.getPressed();
        gui.resetPressed();
        return pressed;
    }

    private boolean chooseCategory(boolean repeat) {
        if (repeat) {
            gui.setFeedbackText("Please choose a category");
        } else {
            gui.setFeedbackText("Please choose another category");
        }

        if (isYahtzee() && !players[currentPlayer].isCategoryEmpty("yahtzee")) {
            players[currentPlayer].extraYahtzee();
        }

        String category;
        do {
            category = buttonInput();
        } while (!Utils.inArray(category, categoryNames));
        boolean isValid;
        switch (category) {
            case "3same" -> isValid = isSameDice(3);
            case "4same" -> isValid = isSameDice(4);
            case "full" -> isValid = fullHouse();
            case "small" -> isValid = isStraight(4);
            case "large" -> isValid = isStraight(5);
            case "yahtzee" -> isValid = isYahtzee();
            default -> isValid = true;
        }
        return updatePlayer(category, isValid);
    }

    private boolean updatePlayer(String category, boolean isValid) {
        if (players[currentPlayer].isCategoryEmpty(category)) {
            players[currentPlayer].update(dice, category, isValid);
            return true;
        }
        return false;
    }

    /**
     * Checks if dice has num of the same dice.
     *
     * @param num Number of the same dice
     * @return If there are num of the same dice
     */
    private boolean isSameDice(int num) {
        // count occurrences of each number
        int[] count = new int[6];
        for (int n : dice) {
            count[n - 1]++;
        }

        // return true if any count is greater than or equal to num
        for (int i = 0; i < 6; i++) {
            if (count[i] >= num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if dice has a full house. A full house is when there is a triple and a pair in the same group of dice.
     *
     * @return If there is a full house
     */
    private boolean fullHouse() {
        // count occurrences of each number
        int[] count = new int[6];
        for (int num : dice) {
            count[num - 1]++;
        }

        // return true if the dice is a yahtzee
        if (isYahtzee()) {
            return true;
        }

        /*
         * Iterates through the count of each number and checks if there is a pair or a triple. If there is a triple,
         * increment the variable isValid by one. If there is a pair and no other pairs have been found, then increment
         * the variable isValid by one. After all iterations, if the variable isValid is two, then a full house has
         * been found.
         */
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
        return isValid == 2;
    }

    /**
     * Checks if the dice has a straight of a specified length. A straight is a sequence of consecutive numbers.
     *
     * @param length Length of straight
     * @return If straight exists
     */
    private boolean isStraight(int length) {
        // sort the dice
        dice = Utils.sortDice(dice);

        /*
         * Iterates through each pair of dice that are adjacent to each other. For each iteration, find the difference
         * between the two dice. If the difference is 0, continue iterating as it is a duplicate die. If the difference
         * is 1, increase the length of the current sequence. If the difference is anything else, the current sequence
         * ends and record the length of the sequence. If the maximum value between the sequence count and the longest
         * sequence is greater than or equal to the specified length then the straight exists.
         */
        int longest = 1;
        int sequence = 1;
        for (int i = 1; i < dice.length; i++) {
            switch (dice[i] - dice[i - 1]) {
                case 0 -> {}
                case 1 -> sequence += 1;
                default -> {
                    if (sequence > longest) {
                        longest = sequence;
                    }
                    sequence = 1;
                }
            }
        }
        return Math.max(longest, sequence) >= length;
    }

    /**
     * Checks if the dice is a yahtzee or not. A yahtzee occurs when all dice are the same number.
     *
     * @return If dice is a yahtzee
     */
    private boolean isYahtzee() {
        // return false if any die is not equal to the first die
        for (int num : dice) {
            if (num != dice[0]) {
                return false;
            }
        }
        return true;
    }

}
