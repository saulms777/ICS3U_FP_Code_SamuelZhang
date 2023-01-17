import java.util.Map;
import static java.util.Map.entry;

public class Player {

    private final int playerNum;
    private int[] upperSection;
    private int[] lowerSection;
    private int yahtzeeBonus;
    private final Map<String, Integer> categories;

    public Player(int num) {
        playerNum = num;
        upperSection = Utils.initialArray(6, -1);
        lowerSection = Utils.initialArray(7, -1);
        yahtzeeBonus = 0;
        categories = Map.ofEntries(
                entry("3same", 0),
                entry("4same", 1),
                entry("full", 2),
                entry("small", 3),
                entry("large", 4),
                entry("chance", 5),
                entry("yahtzee", 6)
        );
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int[] getUpperSection() {
        return upperSection;
    }

    public int getUpperTotal() {
        int score = Utils.arraySum(upperSection);
        if (score >= 63) {
            score += 35;
        }
        return score;
    }

    public int[] getLowerSection() {
        return lowerSection;
    }

    public int getLowerTotal() {
        return Utils.arraySum(lowerSection) + yahtzeeBonus * 100;
    }

    public int getYahtzeeBonus() {
        return yahtzeeBonus;
    }

    public int getTotalScore() {
        return getUpperTotal() + getLowerTotal();
    }

    public boolean isCategoryEmpty(String category) {
        switch (category) {
            case "1", "2", "3", "4", "5", "6" -> {
                return upperSection[Utils.parseInt(category) - 1] == -1;
            }
            default -> {
                return lowerSection[categories.get(category)] == -1;
            }
        }
    }

    public void update(int[] dice, String category, boolean valid) {
        switch (category) {
            case "1", "2", "3", "4", "5", "6" -> {
                int num = Utils.parseInt(category);
                upperSection[num - 1] = num * Utils.frequency(dice, num);
            }
            default -> {
                int index = categories.get(category);
                if (valid) {
                    switch (index) {
                        case 0, 1, 5 -> lowerSection[index] = Utils.arraySum(dice);
                        case 2 -> lowerSection[2] = 25;
                        case 3 -> lowerSection[3] = 30;
                        case 4 -> lowerSection[4] = 40;
                        case 6 -> lowerSection[5] = 50;
                    }
                } else {
                    lowerSection[categories.get(category)] = 0;
                }
            }
        }
    }

    public void extraYahtzee() {
        yahtzeeBonus++;
    }

}
