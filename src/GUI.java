import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public GUI() {
        pressed = "";
        setupStartGUI();
        frame.setVisible(true);
    }

    private void setupStartGUI() {
        frame = new JFrame();
        frame.setSize(270, 190);
        frame.setTitle("Jackpot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        screen = frame.getContentPane();
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));

        screen.add(Box.createVerticalStrut(10));

        JButton title = new JButton("Jackpot");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(title);

        screen.add(Box.createVerticalStrut(40));

        Container centre = new Container();
        centre.setLayout(new FlowLayout());

        centre.add(new JButton("Number of players:"));
        JComboBox<String> numberOfPlayersSelection = new JComboBox<>(new String[]{
                "2", "3", "4",
                "5", "6", "7",
                "8", "9"
        });
        numberOfPlayersSelection.setEditable(false);
        numberOfPlayersSelection.setSelectedItem("2");
        centre.add(numberOfPlayersSelection);
        screen.add(centre);

        JButton confirm = new JButton("Confirm");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(confirm);

        confirm.addActionListener(e -> {
            if (e.getSource().equals(confirm)) {
                numberOfPlayers = Utils.parseInt((String) numberOfPlayersSelection.getSelectedItem());
                pressed = "confirm";
            }
        });

        screen.add(Box.createVerticalStrut(10));

        screen.revalidate();
    }

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    void setupGameGUI() {
        frame.setSize(600, 410);
        screen.removeAll();
        screen.repaint();

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

        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));

        screen.add(Box.createVerticalStrut(10));

        setupUpperGUI();
        screen.add(upperPart);

        feedbackText = new JButton("");
        feedbackText.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(feedbackText);

        screen.add(Box.createVerticalStrut(10));

        setupMiddleGUI();
        screen.add(middlePart);

        setupGameActionListeners();

        screen.revalidate();
    }

    private void setupUpperGUI() {
        upperPart = new Container();
        upperPart.setLayout(new FlowLayout());

        roundNum = new JButton("Round #1");
        roundNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperPart.add(roundNum);

        upperPart.add(Box.createHorizontalStrut(10));

        playerNum = new JButton("Player #1");
        playerNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperPart.add(playerNum);
    }

    private void setupMiddleGUI() {
        middlePart = new Container();
        middlePart.setLayout(new FlowLayout());
        setupLeftGUI();
        middlePart.add(leftSide);
        middlePart.add(Box.createHorizontalStrut(20));
        setupRightGUI();
        middlePart.add(rightSide);
    }

    private void setupLeftGUI() {
        leftSide = new Container();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        Container diceBoxes = new Container();
        diceBoxes.setLayout(new BoxLayout(diceBoxes, BoxLayout.Y_AXIS));
        for (int i = 0; i < 5; i++) {
            dices[i] = new JButton();
            dices[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            diceBoxes.add(dices[i]);
        }
        leftSide.add(diceBoxes);

        leftSide.add(Box.createVerticalStrut(10));

        reroll = new JButton("Reroll Dice");
        reroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftSide.add(reroll);
        rerollsLeft = new JButton("Rerolls Left: 2");
        rerollsLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftSide.add(rerollsLeft);
    }

    private void setupRightGUI() {
        rightSide = new Container();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));

        setupSinglesSection();
        rightSide.add(singlesSection);

        setupCombosSection();
        rightSide.add(combosSection);

        totalMoney = new JButton("Total Money: $0");
        totalMoney.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightSide.add(totalMoney);
    }

    private void setupSinglesSection() {
        singlesSection = new Container();
        singlesSection.setLayout(new BoxLayout(singlesSection, BoxLayout.Y_AXIS));

        JButton singles = new JButton("Singles");
        singles.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlesSection.add(singles);

        Container singlesCategories = new Container();
        singlesCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : singlesCategoryButtons) {
            singlesCategories.add(category);
        }
        singlesSection.add(singlesCategories);

        singlesSection.add(Box.createVerticalStrut(10));
    }

    private void setupCombosSection() {
        combosSection = new Container();
        combosSection.setLayout(new BoxLayout(combosSection, BoxLayout.Y_AXIS));

        JButton singles = new JButton("Combos");
        singles.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlesSection.add(singles);

        Container combosCategories = new Container();
        combosCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : combosCategoryButtons) {
            combosCategories.add(category);
        }
        combosSection.add(combosCategories);

        combosSection.add(Box.createVerticalStrut(10));
    }

    private void setupGameActionListeners() {
        ActionListener buttonListener = e -> {
            Object o = e.getSource();

            for (int i = 0; i < 5; i++) {
                if (dices[i].equals(o)) {
                    pressed = "dice" + (i + 1);
                }
            }

            if (reroll.equals(o)) {
                pressed = "reroll";
            }

            for (int i = 0; i < 6; i++) {
                if (singlesCategoryButtons[i].equals(o)) {
                    pressed = "" + (i + 1);
                }
            }

            for (int i = 0; i < 7; i++) {
                if (combosCategoryButtons[i].equals(o)) {
                    pressed = switch (i) {
                        case 0 -> "triple";
                        case 1 -> "quad";
                        case 2 -> "special";
                        case 3 -> "four";
                        case 4 -> "five";
                        case 5 -> "chance";
                        case 6 -> "jackpot";
                        default -> "";
                    };
                }
            }

            if (feedbackText.equals(o)) {
                pressed = "feedback";
            }
        };

        for (int i = 0; i < 5; i++) {
            dices[i].addActionListener(buttonListener);
        }
        reroll.addActionListener(buttonListener);
        for (int i = 0; i < 6; i++) {
            singlesCategoryButtons[i].addActionListener(buttonListener);
        }
        for (int i = 0; i < 7; i++) {
            combosCategoryButtons[i].addActionListener(buttonListener);
        }
        feedbackText.addActionListener(buttonListener);
    }

    void displayDice(int[] arr) {
        for (int i = 0; i < 5; i++) {
            dices[i].setText("" + arr[i]);
        }
    }

    void changeDisplayedPlayer(Player player) {
        playerNum.setText("Player #" + player.getPlayerNum());

        int[] sectionData = player.getSingles();
        for (int i = 0; i < 6; i++) {
            String currentText = singlesCategoryNames[i];
            if (sectionData[i] == -1) {
                singlesCategoryButtons[i].setText(currentText);
            } else {
                singlesCategoryButtons[i].setText(currentText + ": $" + sectionData[i]);
            }
        }
        if (Utils.arraySum(sectionData) >= 63) {
            singlesCategoryButtons[6].setText("Bonus: ✓");
        }
        singlesCategoryButtons[8].setText("Singles Total: $" + player.getSinglesTotal());

        sectionData = player.getCombos();
        for (int i = 0; i < 7; i++) {
            String currentText = combosCategoryNames[i];
            if (sectionData[i] == -1) {
                combosCategoryButtons[i].setText(currentText);
            } else {
                combosCategoryButtons[i].setText(currentText + ": $" + sectionData[i]);
            }
        }
        combosCategoryButtons[7].setText("Jackpot Bonus: " + player.getJackpotBonus() * 100);
        combosCategoryButtons[8].setText("Combos Total: $" + player.getCombosTotal());

        totalMoney.setText("Total Money: $" + player.getTotalScore());
    }

    void showRerollButtons(int rerolls) {
        leftSide.add(reroll);
        rerollsLeft.setText("Rerolls Left: " + rerolls);
        leftSide.add(rerollsLeft);
    }

    void hideRerollButtons() {
        leftSide.remove(reroll);
        leftSide.remove(rerollsLeft);
    }

    void setRoundNum(int num) {
        roundNum.setText("Round #" + num);
    }

    String getPressed() {
        return pressed;
    }

    void resetPressed() {
        pressed = "";
    }

    void selectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text + ": ✓");
    }

    void unselectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text.replace(": ✓", ""));
    }

    void setFeedbackText(String text) {
        feedbackText.setText(text);
    }

    void setupEndScreen(Player[] players) {
        frame.setSize(250, 150 + players.length * 40);
        screen.removeAll();
        screen.repaint();

        screen.add(Box.createVerticalStrut(10));

        JButton title = new JButton("Scores");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(title);

        screen.add(Box.createVerticalStrut(30));

        for (int i = 0; i < numberOfPlayers; i++) {
            Container playerScore = new Container();
            playerScore.setLayout(new BoxLayout(playerScore, BoxLayout.X_AXIS));

            JButton name = new JButton("Player #" + (i + 1));
            playerScore.add(name);

            playerScore.add(Box.createHorizontalStrut(10));

            JButton score = new JButton("$" + players[i].getTotalScore());
            playerScore.add(score);

            screen.add(playerScore);
            screen.add(Box.createVerticalStrut(10));
        }

        JButton end = new JButton("Press to exit game");
        end.setAlignmentX(Component.CENTER_ALIGNMENT);
        screen.add(end);

        screen.add(Box.createVerticalStrut(10));

        end.addActionListener(e -> {
            if (e.getSource().equals(end)) {
                frame.dispose();
            }
        });

        screen.revalidate();
    }

}
