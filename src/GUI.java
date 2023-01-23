// imports
import javax.swing.*;
import java.awt.*;

class GUI {

    // GUI elements
    private JFrame frame;
    private Container screen;
    private Container middlePart;
    private JButton feedbackText;
    private String pressed;
    private int numberOfPlayers;

    // upper part of screen
    private Container upperPart;
    private JButton roundNum;
    private JButton playerNum;

    // left side of middle part
    private Container leftSide;
    private JButton[] dices;
    private JButton reroll;
    private JButton rerollsLeft;

    // right side of middle
    private Container rightSide;
    private Container singlesSection;
    private String[] singlesCategoryNames;
    private JButton[] singlesCategoryButtons;
    private Container combosSection;
    private String[] combosCategoryNames;
    private JButton[] combosCategoryButtons;
    private JButton totalMoney;

    /**
     * Main class for the GUI of the game.
     */
    public GUI() {
        // sets up starting screen
        pressed = "";
        setupStartGUI();
        frame.setVisible(true);
    }

    /**
     * Sets up GUI for start screen.
     */
    private void setupStartGUI() {
        // sets up the GUI frame
        frame = new JFrame();
        frame.setSize(800, 780);
        frame.setTitle("Jackpot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // sets up content pane of frame
        screen = frame.getContentPane();
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // sets up jackpot title
        JButton title = new JButton("Jackpot");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(title);

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(40));

        // sets up instructions for the game
        JButton instructions = new JButton(
                        """
                        <html><pre>
                        Jackpot is a dice game played with five dice. The goal is to earn the most money by rolling
                        certain combinations of the dice.
                        
                        To play, each player takes turns rolling the dice. On the first roll, the player will roll all
                        five dice. After the first roll, the player can choose to roll some or all of the dice again in
                        order to try and get a better combination. The player has a total of three rolls per turn.
                        
                        After the third roll, the player must choose one of the thirteen categories to calculate the
                        amount of money earned. The categories include ones, twos, threes, fours, fives, sixes, triple,
                        quad, special, four-line, five-line, chance, and Jackpot.
                        
                        The game ends after thirteen rounds. The player with the most money wins.
                        
                        The following are the rules for each category:
                         Category                    | Requirements             | Money earned
                        -----------------------------|--------------------------|----------------------
                         All singles categories      | None                     | Sum of matching dice
                         Triple                      | Three of a kind          | Sum of all dice
                         Quad                        | Four of a kind           | Sum of all dice
                         Special                     | A triple and a pair      | $25
                         Four-line                   | Four consecutive dice    | $30
                         Five-line                   | Five consecutive dice    | $40
                         Chance                      | None                     | Sum of all dice
                         Jackpot                     | Five of a kind           | $50
                        
                        Extra scoring rules:
                         - If the player has more than $50 from the singles categories, they will recieve another $25.
                         - If a Jackpot is rolled while the Jackpot category is already used, the player will be awarded
                           an extra $100. Category selection continues as normal.
                        
                        Select a number of players and press "Confirm" to start.
                        </pre></html>
                        """
        );
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setMaximumSize(new Dimension(700, 600));
        screen.add(instructions);

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(40));

        // sets up player number selector container
        Container selectPlayerNum = new Container();
        selectPlayerNum.setLayout(new FlowLayout());

        // sets up player selection with combo box
        selectPlayerNum.add(new JButton("Number of players:"));
        JComboBox<String> numberOfPlayersSelection = new JComboBox<>(new String[]{
                "2", "3", "4",
                "5", "6", "7",
                "8", "9"
        });
        numberOfPlayersSelection.setEditable(false);
        selectPlayerNum.add(numberOfPlayersSelection);
        screen.add(selectPlayerNum);

        // sets up confirm button to start game
        JButton confirm = new JButton("Confirm");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(confirm);

        // adds action listener to confirm button
        confirm.addActionListener(e -> {
            numberOfPlayers = Utils.parseInt((String) numberOfPlayersSelection.getSelectedItem());
            pressed = "confirm";
        });

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // revalidates the GUI
        screen.revalidate();
    }

    /**
     * Gets the number of players selected by the player.
     *
     * @return Number of players
     */
    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Sets up GUI for the main part of the game.
     */
    void setupGameGUI() {
        // resets GUI and resizes frame
        frame.setSize(600, 410);
        screen.removeAll();
        screen.repaint();

        // setup GUI variables
        dices = new JButton[5];
        singlesCategoryNames = new String[]{
                "Ones", "Twos", "Threes",
                "Fours", "Fives", "Sixes",
                "Bonus: x", "", "Singles Total: 0"
        };
        singlesCategoryButtons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            singlesCategoryButtons[i] = new JButton(singlesCategoryNames[i]);
        }
        combosCategoryNames = new String[]{
                "Triple", "Quad", "Special",
                "Four-Line", "Five-Line", "Chance",
                "Jackpot", "Jackpot Bonus: 0", "Combos Total: 0"
        };
        combosCategoryButtons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            combosCategoryButtons[i] = new JButton(combosCategoryNames[i]);
        }

        // changes layout of screen
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // sets up upper part of GUI
        setupUpperGUI();
        screen.add(upperPart);

        // sets up feedback button
        feedbackText = new JButton("");
        feedbackText.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(feedbackText);

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // sets up middle part of GUI
        setupMiddleGUI();
        screen.add(middlePart);

        // sets up button functionalities
        setupGameActionListeners();

        // revalidates the GUI
        screen.revalidate();
    }

    /**
     * Sets up top part of GUI.
     */
    private void setupUpperGUI() {
        // sets up top part container
        upperPart = new Container();
        upperPart.setLayout(new FlowLayout());

        // sets up round number display
        roundNum = new JButton("Round #1");
        roundNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperPart.add(roundNum);

        // spacing for aesthetics
        upperPart.add(Box.createHorizontalStrut(10));

        // sets up player number display
        playerNum = new JButton("Player #1");
        playerNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperPart.add(playerNum);
    }

    /**
     * Sets up middle part of GUI.
     */
    private void setupMiddleGUI() {
        // sets up middle part container
        middlePart = new Container();
        middlePart.setLayout(new FlowLayout());

        // sets up left side
        setupLeftGUI();
        middlePart.add(leftSide);

        // spacing for aesthetics
        middlePart.add(Box.createHorizontalStrut(20));

        // sets up right side
        setupRightGUI();
        middlePart.add(rightSide);
    }

    /**
     * Sets up left side of middle part.
     */
    private void setupLeftGUI() {
        // sets up left side container
        leftSide = new Container();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        // sets up and adds dice boxes buttons
        Container diceBoxes = new Container();
        diceBoxes.setLayout(new BoxLayout(diceBoxes, BoxLayout.Y_AXIS));
        for (int i = 0; i < 5; i++) {
            dices[i] = new JButton();
            dices[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            diceBoxes.add(dices[i]);
        }
        leftSide.add(diceBoxes);

        // spacing for aesthetics
        leftSide.add(Box.createVerticalStrut(10));

        // sets up reroll buttons
        reroll = new JButton("Reroll Dice");
        reroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftSide.add(reroll);
        rerollsLeft = new JButton("Rerolls Left: 2");
        rerollsLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftSide.add(rerollsLeft);
    }

    /**
     * Sets up right side of middle part.
     */
    private void setupRightGUI() {
        // sets up right side container
        rightSide = new Container();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));

        // sets up and adds singles section
        setupSinglesSection();
        rightSide.add(singlesSection);

        // sets up and adds combos section
        setupCombosSection();
        rightSide.add(combosSection);

        // adds total money button
        totalMoney = new JButton("Total Money: $0");
        totalMoney.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSide.add(totalMoney);
    }

    /**
     * Sets up GUI for singles section.
     */
    private void setupSinglesSection() {
        // sets up singles section container
        singlesSection = new Container();
        singlesSection.setLayout(new BoxLayout(singlesSection, BoxLayout.Y_AXIS));

        // adds singles title to section
        JButton singles = new JButton("Singles");
        singles.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlesSection.add(singles);

        // adds singles categories to section
        Container singlesCategories = new Container();
        singlesCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : singlesCategoryButtons) {
            singlesCategories.add(category);
        }
        singlesSection.add(singlesCategories);

        // spacing for aesthetics
        singlesSection.add(Box.createVerticalStrut(10));
    }

    /**
     * Sets up GUI for combos section.
     */
    private void setupCombosSection() {
        // sets up combo section container
        combosSection = new Container();
        combosSection.setLayout(new BoxLayout(combosSection, BoxLayout.Y_AXIS));

        // adds combos title to section
        JButton singles = new JButton("Combos");
        singles.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlesSection.add(singles);

        // adds combos categories to section
        Container combosCategories = new Container();
        combosCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : combosCategoryButtons) {
            combosCategories.add(category);
        }
        combosSection.add(combosCategories);

        // spacing for aesthetics
        combosSection.add(Box.createVerticalStrut(10));
    }

    /**
     * Creates and adds action listeners to each functional button. Updates pressed based on the button pressed.
     */
    private void setupGameActionListeners() {
        // adds action listeners to dice buttons
        for (int i = 0; i < 5; i++) {
            int index = i;
            dices[i].addActionListener(e -> pressed = "dice" + (index + 1));
        }

        // adds action listeners to reroll button
        reroll.addActionListener(e -> pressed = "reroll");

        // adds action listeners to singles category buttons
        for (int i = 0; i < 6; i++) {
            int index = i;
            singlesCategoryButtons[i].addActionListener(e -> pressed = "" + (index + 1));
        }

        // adds action listeners to combos category buttons
        for (int i = 0; i < 7; i++) {
            int index = i;
            combosCategoryButtons[i].addActionListener(e -> pressed = switch (index) {
                case 0 -> "triple";
                case 1 -> "quad";
                case 2 -> "special";
                case 3 -> "four";
                case 4 -> "five";
                case 5 -> "chance";
                case 6 -> "jackpot";
                default -> "";
            });
        }

        // adds action listeners to feedback button
        feedbackText.addActionListener(e -> pressed = "feedback");
    }

    /**
     * Updates displayed dice.
     *
     * @param dice dice <code>int</code> array
     */
    void displayDice(int[] dice) {
        for (int i = 0; i < 5; i++) {
            dices[i].setText("" + dice[i]);
        }
    }

    /**
     * Updates GUI to change the currently displayed player.
     *
     * @param player Player data to change to
     */
    void changeDisplayedPlayer(Player player) {
        // updates player number
        playerNum.setText("Player #" + player.getPlayerNum());

        // updates singles section
        int[] sectionData = player.getSingles();
        for (int i = 0; i < 6; i++) {
            String currentText = singlesCategoryNames[i];
            if (sectionData[i] == -1) {
                singlesCategoryButtons[i].setText(currentText);
            } else {
                singlesCategoryButtons[i].setText(currentText + ": $" + sectionData[i]);
            }
        }

        // updates singles section bonus
        if (Utils.arraySum(sectionData) >= 63) {
            singlesCategoryButtons[6].setText("Bonus: ✓");
        }

        // updates singles total score
        singlesCategoryButtons[8].setText("Singles Total: $" + player.getSinglesTotal());

        // updates combos section
        sectionData = player.getCombos();
        for (int i = 0; i < 7; i++) {
            String currentText = combosCategoryNames[i];
            if (sectionData[i] == -1) {
                combosCategoryButtons[i].setText(currentText);
            } else {
                combosCategoryButtons[i].setText(currentText + ": $" + sectionData[i]);
            }
        }

        // updates jackpot bonus
        combosCategoryButtons[7].setText("Jackpot Bonus: " + player.getJackpotBonus() * 100);

        // updates combos total score
        combosCategoryButtons[8].setText("Combos Total: $" + player.getCombosTotal());

        // updates total score
        totalMoney.setText("Total Money: $" + player.getTotalScore());
    }

    /**
     * Shows the reroll buttons and displays the amount of rerolls left.
     *
     * @param rerolls <code>int</code> rerolls left
     */
    void showRerollButtons(int rerolls) {
        leftSide.add(reroll);
        rerollsLeft.setText("Rerolls Left: " + rerolls);
        leftSide.add(rerollsLeft);
    }

    /**
     * Hides the reroll buttons.
     */
    void hideRerollButtons() {
        leftSide.remove(reroll);
        leftSide.remove(rerollsLeft);
    }

    /**
     * Change the round number to a specified number.
     *
     * @param num <code>int</code> num
     */
    void setRoundNum(int num) {
        roundNum.setText("Round #" + num);
    }

    /**
     * Gets the current pressed button as a <code>String</code>.
     *
     * @return Currently pressed button
     */
    String getPressed() {
        return pressed;
    }

    /**
     * Resets the current pressed button.
     */
    void resetPressed() {
        pressed = "";
    }

    /**
     * Selects a specified die for rerolling.
     *
     * @param num Dice number to select
     */
    void selectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text + ": ✓");
    }

    /**
     * Unselects a specified die for rerolling.
     *
     * @param num Dice number to unselect
     */
    void unselectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text.replace(": ✓", ""));
    }

    /**
     * Sets the feedback button to a specified <code>String</code>.
     *
     * @param text Text to display
     */
    void setFeedbackText(String text) {
        feedbackText.setText(text);
    }

    /**
     * Sets up end screen of game.
     *
     * @param players <code>Player</code> array for score display
     */
    void setupEndScreen(Player[] players) {
        // resets GUI and resizes frame
        frame.setSize(250, 150 + players.length * 40);
        screen.removeAll();
        screen.repaint();

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // adds scores title
        JButton title = new JButton("Scores");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(title);

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(30));

        // sets up and displays players' score
        setupEndScores(players);

        // sets up end game button
        JButton end = new JButton("Press to exit game");
        end.setAlignmentX(Component.CENTER_ALIGNMENT);
        end.addActionListener(e -> frame.dispose());
        screen.add(end);

        // spacing for aesthetics
        screen.add(Box.createVerticalStrut(10));

        // revalidates the GUI
        screen.revalidate();
    }

    /**
     * Displays end scores of players.
     *
     * @param players <code>Player</code> array for score display
     */
    private void setupEndScores(Player[] players) {
        // iterate through each player and display score
        for (int i = 0; i < numberOfPlayers; i++) {
            // sets up player score container
            Container playerScore = new Container();
            playerScore.setLayout(new BoxLayout(playerScore, BoxLayout.X_AXIS));

            // sets up player score display
            playerScore.add(new JButton("Player #" + (i + 1)));
            playerScore.add(Box.createHorizontalStrut(10));
            playerScore.add(new JButton("$" + players[i].getTotalScore()));
            screen.add(playerScore);

            // spacing for aesthetics
            screen.add(Box.createVerticalStrut(10));
        }
    }

}
