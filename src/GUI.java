import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GUI {

    // GUI elements
    private JFrame frame;
    private Container screen;
    private JButton feedbackText;
    private String pressed;

    // start screen
    private int numberOfPlayers;
    private JComboBox<String> numberOfPlayersSelection;
    private JButton confirm;

    // left side of screen
    private Container leftSide;
    private JButton roundNum;
    private JButton playerNum;
    private JButton[] dices;
    private JButton reroll;
    private JButton rerollsLeft;

    // right side of screen
    private Container rightSide;
    private Container singlesSection;
    private String[] singlesCategoryInitialNames;
    private JButton[] singlesCategoryNames;
    private Container combosSection;
    private String[] combosCategoryInitialNames;
    private JButton[] combosCategoryNames;
    private JButton totalMoney;

    public GUI() {
        pressed = "";
        setupStartGUI();
        frame.setVisible(true);
    }

    private void setupStartGUI() {
        frame = new JFrame();
        frame.setSize(600, 350);
        frame.setTitle("Jackpot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen = frame.getContentPane();
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));

        screen.add(new JButton("Jackpot"));

        numberOfPlayersSelection = new JComboBox<>(new String[]{
                "2", "3", "4",
                "5", "6", "7",
                "8", "9", "10"
        });
        numberOfPlayersSelection.setEditable(false);
        numberOfPlayersSelection.setSelectedItem("2");
        screen.add(numberOfPlayersSelection);

        confirm = new JButton("Confirm");
        screen.add(confirm);

        setupStartActionListeners();
    }

    private void setupStartActionListeners() {
        ActionListener buttonListener = ae -> {
            numberOfPlayers = Utils.parseInt((String) numberOfPlayersSelection.getSelectedItem());

            if (ae.getSource().equals(confirm)) {
                pressed = "confirm";
            }
        };

        numberOfPlayersSelection.addActionListener(buttonListener);
        confirm.addActionListener(buttonListener);
    }

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    void setupGameGUI() {
        screen.removeAll();
        screen.repaint();

        dices = new JButton[5];

        singlesCategoryInitialNames = new String[]{
                "Ones", "Twos", "Threes",
                "Fours", "Fives", "Sixes",
                "Bonus: x", "", "Singles Total: 0"
        };
        singlesCategoryNames = new JButton[9];
        for (int i = 0; i < 9; i++) {
            singlesCategoryNames[i] = new JButton(singlesCategoryInitialNames[i]);
        }

        combosCategoryInitialNames = new String[]{
                "Triple", "Quad", "Special",
                "Four-Line", "Five-Line", "Chance",
                "Jackpot", "Jackpot Bonus: 0", "Combos Total: 0"
        };
        combosCategoryNames = new JButton[9];
        for (int i = 0; i < 9; i++) {
            combosCategoryNames[i] = new JButton(combosCategoryInitialNames[i]);
        }

        screen.setLayout(new FlowLayout());
        setupLeftGUI();
        screen.add(leftSide);
        setupRightGUI();
        screen.add(rightSide);

        feedbackText = new JButton("");
        screen.add(feedbackText);

        setupGameActionListeners();
    }

    private void setupLeftGUI() {
        leftSide = new Container();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        roundNum = new JButton("Round #1");
        leftSide.add(roundNum);

        playerNum = new JButton("Player #1");
        leftSide.add(playerNum);

        Container diceBoxes = new Container();
        diceBoxes.setLayout(new BoxLayout(diceBoxes, BoxLayout.Y_AXIS));
        for (int i = 0; i < 5; i++) {
            dices[i] = new JButton();
            diceBoxes.add(dices[i]);
        }
        leftSide.add(diceBoxes);

        reroll = new JButton("Reroll Dice");
        leftSide.add(reroll);
        rerollsLeft = new JButton("Rerolls Left: 2");
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
        rightSide.add(totalMoney);
    }

    private void setupSinglesSection() {
        singlesSection = new Container();
        singlesSection.setLayout(new BoxLayout(singlesSection, BoxLayout.Y_AXIS));
        singlesSection.add(new JButton("Singles"));

        Container singlesCategories = new Container();
        singlesCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : singlesCategoryNames) {
            singlesCategories.add(category);
        }
        singlesSection.add(singlesCategories);
    }

    private void setupCombosSection() {
        combosSection = new Container();
        combosSection.setLayout(new BoxLayout(combosSection, BoxLayout.Y_AXIS));
        combosSection.add(new JButton("Combos"));

        Container combosCategories = new Container();
        combosCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : combosCategoryNames) {
            combosCategories.add(category);
        }
        combosSection.add(combosCategories);
    }

    private void setupGameActionListeners() {
        ActionListener buttonListener = ae -> {
            Object o = ae.getSource();

            for (int i = 0; i < 5; i++) {
                if (dices[i].equals(o)) {
                    pressed = "dice" + (i + 1);
                }
            }

            if (reroll.equals(o)) {
                pressed = "reroll";
            }

            for (int i = 0; i < 6; i++) {
                if (singlesCategoryNames[i].equals(o)) {
                    pressed = "" + (i + 1);
                }
            }

            for (int i = 0; i < 7; i++) {
                if (combosCategoryNames[i].equals(o)) {
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
            singlesCategoryNames[i].addActionListener(buttonListener);
        }
        for (int i = 0; i < 7; i++) {
            combosCategoryNames[i].addActionListener(buttonListener);
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
            String currentText = singlesCategoryInitialNames[i];
            if (sectionData[i] == -1) {
                singlesCategoryNames[i].setText(currentText);
            } else {
                singlesCategoryNames[i].setText(currentText + ": $" + sectionData[i]);
            }
        }
        if (Utils.arraySum(sectionData) >= 63) {
            singlesCategoryNames[6].setText("Bonus: ✓");
        }
        singlesCategoryNames[8].setText("Singles Total: " + player.getSinglesTotal());

        sectionData = player.getCombos();
        for (int i = 0; i < 7; i++) {
            String currentText = combosCategoryInitialNames[i];
            if (sectionData[i] == -1) {
                combosCategoryNames[i].setText(currentText);
            } else {
                combosCategoryNames[i].setText(currentText + ": $" + sectionData[i]);
            }
        }
        combosCategoryNames[7].setText("Jackpot Bonus: " + player.getJackpotBonus() * 100);
        combosCategoryNames[8].setText("Combos Total: " + player.getCombosTotal());

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

}
