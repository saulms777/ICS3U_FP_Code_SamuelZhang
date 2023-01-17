import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import static java.util.Map.entry;

public class GUI {

    // GUI elements
    private JFrame frame;
    private Container screen;
    private JButton feedbackText;
    private String pressed;

    // left side of screen
    private Container leftSide;
    private JButton roundNum;
    private JButton playerNum;
    private Container diceBoxes;
    private JButton[] dices;
    private JButton reroll;

    // right side of screen
    private Container rightSide;
    private Container upperSection;
    private Container upperCategories;
    private final String[] upperCategoryInitialNames;
    private JButton[] upperCategoryNames;
    private Container lowerSection;
    private Container lowerCategories;
    private final String[] lowerCategoryInitialNames;
    private JButton[] lowerCategoryNames;
    private JButton totalPoints;

    public GUI() {
        pressed = "";
        dices = new JButton[5];

        upperCategoryInitialNames = new String[]{
                "Aces", "Twos", "Threes",
                "Fours", "Fives", "Sixes",
                "Bonus: x", "", "Upper Total: 0"
        };
        upperCategoryNames = new JButton[9];
        for (int i = 0; i < 9; i++) {
            upperCategoryNames[i] = new JButton(upperCategoryInitialNames[i]);
        }

        lowerCategoryInitialNames = new String[]{
                "3 of a Kind", "4 of a Kind", "Full House",
                "Small Straight", "Large Straight", "Chance",
                "Yahtzee", "Yahtzee Bonus: 0", "Lower Total: 0"
        };
        lowerCategoryNames = new JButton[9];
        for (int i = 0; i < 9; i++) {
            lowerCategoryNames[i] = new JButton(lowerCategoryInitialNames[i]);
        }

        setupGUI();
        setupActionListeners();

        frame.setVisible(true);
    }

    private void setupGUI() {
        frame = new JFrame();
        frame.setSize(600, 350);
        frame.setTitle("Yahtzee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen = frame.getContentPane();
        screen.setLayout(new FlowLayout());
        setupLeftGUI();
        screen.add(leftSide);
        setupRightGUI();
        screen.add(rightSide);

        feedbackText = new JButton("");
        screen.add(feedbackText);
    }

    private void setupLeftGUI() {
        leftSide = new Container();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        roundNum = new JButton("Round #1");
        leftSide.add(roundNum);

        playerNum = new JButton("Player #1");
        leftSide.add(playerNum);

        diceBoxes = new Container();
        diceBoxes.setLayout(new BoxLayout(diceBoxes, BoxLayout.Y_AXIS));
        for (int i = 0; i < 5; i++) {
            dices[i] = new JButton();
            diceBoxes.add(dices[i]);
        }
        leftSide.add(diceBoxes);

        reroll = new JButton("Reroll Dice");
        leftSide.add(reroll);
    }

    private void setupRightGUI() {
        rightSide = new Container();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));

        setupUpperSection();
        rightSide.add(upperSection);

        setupLowerSection();
        rightSide.add(lowerSection);

        totalPoints = new JButton("Total points: 0");
        rightSide.add(totalPoints);
    }

    private void setupUpperSection() {
        upperSection = new Container();
        upperSection.setLayout(new BoxLayout(upperSection, BoxLayout.Y_AXIS));
        upperSection.add(new JButton("Upper Section"));

        upperCategories = new Container();
        upperCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : upperCategoryNames) {
            upperCategories.add(category);
        }
        upperSection.add(upperCategories);
    }

    private void setupLowerSection() {
        lowerSection = new Container();
        lowerSection.setLayout(new BoxLayout(lowerSection, BoxLayout.Y_AXIS));
        lowerSection.add(new JButton("Lower Section"));

        lowerCategories = new Container();
        lowerCategories.setLayout(new GridLayout(3, 3));
        for (JButton category : lowerCategoryNames) {
            lowerCategories.add(category);
        }
        lowerSection.add(lowerCategories);
    }

    private void setupActionListeners() {
        ActionListener buttonListener = ae -> {
            Map<Integer, String> pressedNames = Map.ofEntries(
                    entry(0, "3same"),
                    entry(1, "4same"),
                    entry(2, "full"),
                    entry(3, "small"),
                    entry(4, "large"),
                    entry(5, "chance"),
                    entry(6, "yahtzee")
            );

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
                if (upperCategoryNames[i].equals(o)) {
                    pressed = "" + (i + 1);
                }
            }

            for (int i = 0; i < 7; i++) {
                if (lowerCategoryNames[i].equals(o)) {
                    pressed = pressedNames.get(i);
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
            upperCategoryNames[i].addActionListener(buttonListener);
        }
        for (int i = 0; i < 7; i++) {
            lowerCategoryNames[i].addActionListener(buttonListener);
        }
        feedbackText.addActionListener(buttonListener);
    }

    public String getPressed() {
        return pressed;
    }

    public void resetPressed() {
        pressed = "";
    }

    public void displayDice(int[] arr) {
        for (int i = 0; i < 5; i++) {
            dices[i].setText("" + arr[i]);
        }
    }

    public void changeDisplayedPlayer(Player player) {
        playerNum.setText("Player #" + player.getPlayerNum());

        int[] sectionData = player.getUpperSection();
        for (int i = 0; i < 6; i++) {
            String currentText = upperCategoryInitialNames[i];
            if (sectionData[i] == -1) {
                upperCategoryNames[i].setText(currentText);
            } else {
                upperCategoryNames[i].setText(currentText + ": " + sectionData[i]);
            }
        }
        if (Utils.arraySum(sectionData) >= 63) {
            upperCategoryNames[6].setText("Bonus: ✓");
        }
        upperCategoryNames[8].setText("Upper Total: " + player.getUpperTotal());

        sectionData = player.getLowerSection();
        for (int i = 0; i < 7; i++) {
            String currentText = lowerCategoryInitialNames[i];
            if (sectionData[i] == -1) {
                lowerCategoryNames[i].setText(currentText);
            } else {
                lowerCategoryNames[i].setText(currentText + ": " + sectionData[i]);
            }
        }
        lowerCategoryNames[7].setText("Yahtzee Bonus: " + player.getYahtzeeBonus() * 100);
        lowerCategoryNames[8].setText("Lower Total: " + player.getLowerTotal());

        totalPoints.setText("Total Points: " + player.getTotalScore());
    }

    public void setRoundNum(int num) {
        roundNum.setText("Round #" + num);
    }

    public void selectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text + ": ✓");
    }

    public void unselectDice(int num) {
        String text = dices[num].getText();
        dices[num].setText(text.replace(": ✓", ""));
    }

    public void setFeedbackText(String text) {
        feedbackText.setText(text);
    }

}
