// imports
import java.util.Random;

class Game {

    // class variables
    private final GUI gui;
    private int round;
    private int currentPlayer;
    private int[] dice;
    private final Player[] players;

    /**
     * Main class for jackpot game.
     */
    public Game() {
        // setup start screen
        gui = new GUI();
        String start;
        do {
            start = buttonInput();
        } while (!start.equals("confirm"));

        // run start screen
        gui.setupGameGUI();

        // setup game variables
        round = 1;
        currentPlayer = 0;
        dice = new int[5];
        int numberOfPlayers = gui.getNumberOfPlayers();
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(i + 1);
        }

        // run playable game
        run();

        // show end screen
        gui.setupEndScreen(players);
    }

    /**
     * Main method of the playable part of the game. Iterates through each round, and run each turn in each round.
     */
    private void run() {
        // iterate through 13 rounds
        while (round <= 13) {
            // update GUI
            gui.setRoundNum(round);

            // iterate through all players and run each turn
            while (currentPlayer < players.length) {
                // update GUI
                gui.changeDisplayedPlayer(players[currentPlayer]);

                // run player turn
                runDice();
                runCategorySelect();

                // wait for player to start next turn/round
                if (currentPlayer != players.length - 1) {
                    waitNext("Press for next player's turn");
                }
                currentPlayer++;
            }

            // wait for player to start next round
            if (round != 13) {
                waitNext("Press for next round");
                currentPlayer = 0;
            }
            round++;
        }

        // wait for player to end game
        waitNext("Press to finish game");
    }

    /**
     * Method for running the dice part of each turn. Rolls all dice first, then allows user to reroll the dice up to
     * two times. The user can choose to stop rerolling at any time by pressing the feedback button.
     */
    private void runDice() {
        // roll all dice
        rollDice(new boolean[]{true, true, true, true, true});

        // setup reroll functions
        gui.showRerollButtons(2);
        gui.setFeedbackText("Press to finalize dice");

        // setup reroll variables
        String[] diceNames = new String[]{"dice1", "dice2", "dice3", "dice4", "dice5"};
        boolean[] selectedDice = new boolean[5];
        int timesRerolled = 0;

        // loop through until dice is finalized or dice rerolled 2 times
        String pressed;
        do {
            // button input
            pressed = buttonInput();

            // if button clicked is a die select or unselect the die
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

            // if button clicked is the reroll button reroll dice
            if (pressed.equals("reroll")) {
                rollDice(selectedDice);
                selectedDice = new boolean[5];
                timesRerolled++;
                gui.showRerollButtons(2 - timesRerolled);
            }
        } while (!pressed.equals("feedback") && timesRerolled < 2);

        // hide reroll functions
        gui.hideRerollButtons();
    }

    /**
     * Method for running the category select part of each turn. Allows the user to choose a category until an empty
     * category is chosen. Update GUI after category is chosen.
     */
    private void runCategorySelect() {
        // select category
        boolean successful = true;
        do {
            updateCategoryText(successful);
            checkJackpot();
            successful = chooseCategory();
        } while (!successful);

        // update GUI
        gui.changeDisplayedPlayer(players[currentPlayer]);
    }

    /**
     * Changes the text of the feedback text and waits for the feedback button to be pressed.
     *
     * @param text <code>String</code> to set feedback text to
     */
    private void waitNext(String text) {
        // change feedback text
        gui.setFeedbackText(text);

        // wait for feedback button to be pressed
        String pressed;
        do {
            pressed = buttonInput();
        } while (!pressed.equals("feedback"));
    }

    /**
     * Randomizes the dice based on an array of booleans to specify which die to roll. Updates the GUI dice display
     * afterwards.
     *
     * @param selected Given <code>boolean</code> array
     */
    private void rollDice(boolean[] selected) {
        // randomize dice based on boolean array
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            if (selected[i]) {
                dice[i] = 1 + r.nextInt(6);
            }
        }

        // update GUI display for dice
        gui.displayDice(dice);
    }

    /**
     * Waits for a button input from the <code>GUI</code> class. Returns the <code>String</code> value of the button
     * pressed.
     *
     * @return <code>String</code> name of pressed button
     */
    private String buttonInput() {
        // standby until a button is pressed
        while (gui.getPressed().equals("")) {
            System.out.print("");       // necessary lag for the code to work
        }

        // return name of pressed button
        String pressed = gui.getPressed();
        gui.resetPressed();
        return pressed;
    }

    /**
     * Sets the text of the feedback button depending on the parameter <code>repeat</code>
     *
     * @param repeat If the category was already taken
     */
    private void updateCategoryText(boolean repeat) {
        if (repeat) {
            gui.setFeedbackText("Please choose a category");
        } else {
            gui.setFeedbackText("Please choose another category");
        }
    }

    /**
     * Checks if the player is eligible for an extra jackpot.
     */
    private void checkJackpot() {
        if (isJackpot() && !players[currentPlayer].isCategoryEmpty("jackpot")) {
            players[currentPlayer].addExtraJackpot();
        }
    }

    /**
     * Allows the player to choose a category. Checks if the dice is valid for the chosen category and updates the
     * player data. Returns a <code>boolean</code> based on whether the category was already taken or not.
     *
     * @return <code>true</code> if category was already filled <code>false</code>
     */
    private boolean chooseCategory() {
        // names of all valid categories
        String[] categoryNames = new String[]{
                "1", "2", "3", "4", "5", "6",
                "triple", "quad", "special", "four", "five", "chance", "jackpot"
        };

        // choose category, repeat until a category is chosen
        String category;
        do {
            category = buttonInput();
        } while (!Utils.inArray(category, categoryNames));

        // checks if the category chosen is valid for the dice rolled
        boolean isValid;
        isValid = switch (category) {
            case "triple" -> isSameDice(3);
            case "quad" -> isSameDice(4);
            case "special" -> isSpecial();
            case "four" -> isStraight(4);
            case "five" -> isStraight(5);
            case "jackpot" -> isJackpot();
            default -> true;
        };

        // updates player data and returns boolean
        return updatePlayer(category, isValid);
    }

    /**
     * Updates the current player's data based on the category chosen. Do not update if the category is already filled.
     * Returns whether the category was filled or not.
     *
     * @param category Category to update
     * @param isValid Whether the category's requirements was met or not
     * @return <code>true</code> if the category was updated else <code>false</code>
     */
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
     * @return <code>true</code> if there are num of the same dice else <code>false</code>
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
     * Checks if dice has a special. A special is when there is a triple and a pair in the same group of dice.
     *
     * @return <code>true</code> if a special is found else <code>false</code>
     */
    private boolean isSpecial() {
        // count occurrences of each number
        int[] count = new int[6];
        for (int num : dice) {
            count[num - 1]++;
        }

        // return true if the dice is a Jackpot
        if (isJackpot()) {
            return true;
        }

        /*
         * Iterates through the count of each number and checks if there is a pair or a triple. If there is a triple,
         * increment the variable isValid by one. If there is a pair and no other pairs have been found, then increment
         * the variable isValid by one. After all iterations, if the variable isValid is two, then a special has been
         * found.
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
     * @return <code>true</code> if straight exists else <code>false</code>
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
     * Checks if the dice is a jackpot or not. A jackpot occurs when all dice are the same number.
     *
     * @return <code>true</code> if dice is a jackpot else <code>false</code>
     */
    private boolean isJackpot() {
        // return false if any die is not equal to the first die
        for (int num : dice) {
            if (num != dice[0]) {
                return false;
            }
        }
        return true;
    }

}
