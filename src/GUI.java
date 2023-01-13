import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    // GUI elements
    private JFrame frame;
    private Container screen;

    // left side of screen
    private Container leftSide;
    private JButton playerNum;
    private Container diceBoxes;
    private JButton[] dices;

    // right side of screen
    private Container rightSide;
    private Container upperSection;
    private Container upperCategories;
    private JButton[] upperCategoryNames;
    private Container lowerSection;
    private Container lowerCategories;
    private JButton[] lowerCategoryNames;
    private JButton totalPoints;

    public GUI() {
        setupGUI();
        setUpActionListeners();

        frame.setVisible(true);
    }

    private void setupGUI() {
        dices = new JButton[5];
        upperCategoryNames = new JButton[]{
                new JButton("Aces"),
                new JButton("Twos"),
                new JButton("Threes"),
                new JButton("Fours"),
                new JButton("Fives"),
                new JButton("Sixes"),
                new JButton("Bonus: x"),
                new JButton(""),
                new JButton("Upper Total: 0")
        };
        lowerCategoryNames = new JButton[]{
                new JButton("3 of a Kind"),
                new JButton("4 of a Kind"),
                new JButton("Full House"),
                new JButton("Small Straight"),
                new JButton("Large Straight"),
                new JButton("Chance"),
                new JButton("Yahtzee"),
                new JButton("Yahtzee Bonus: 0"),
                new JButton("Lower Total: 0")
        };

        frame = new JFrame();
        frame.setSize(600, 300);
        frame.setTitle("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen = frame.getContentPane();
        screen.setLayout(new FlowLayout());
        setupLeftGUI();
        screen.add(leftSide);
        setupRightGUI();
        screen.add(rightSide);
    }

    private void setupLeftGUI() {
        leftSide = new Container();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        playerNum = new JButton("Player #1");
        leftSide.add(playerNum);

        diceBoxes = new Container();
        diceBoxes.setLayout(new BoxLayout(diceBoxes, BoxLayout.Y_AXIS));
        for (int i = 0; i < 5; i++) {
            dices[i] = new JButton("Dice #" + (i + 1));
            diceBoxes.add(dices[i]);
        }
        leftSide.add(diceBoxes);
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

    public void setUpActionListeners() {
        ActionListener buttonListener = e -> {
            Object o = e.getSource();
            if (o == playerNum) {
                System.out.println("player num");
            }
        };

        playerNum.addActionListener(buttonListener);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
