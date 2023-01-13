import java.util.HashMap;

public class Player {

    private final int[] upperSection = Utils.initialArray(6, -1);
    private final int[] lowerSection = Utils.initialArray(7, -1);
    private int yahtzeeBonus = 0;
    private final HashMap<String, Integer> categories = new HashMap<>() {{
        put("3same", 0);
        put("4same", 1);
        put("full", 2);
        put("small", 3);
        put("large", 4);
        put("yahtzee", 5);
        put("chance", 6);
    }};

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
                        case 0, 1, 6 -> lowerSection[index] = Utils.arraySum(dice);
                        case 2 -> lowerSection[2] = 25;
                        case 3 -> lowerSection[3] = 30;
                        case 4 -> lowerSection[4] = 40;
                        case 5 -> lowerSection[5] = 50;
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

    public int calculateScore() {
        int score = 0;
        score += Utils.arraySum(upperSection);
        if (score >= 63) {
            score += 35;
        }
        score += Utils.arraySum(lowerSection) + yahtzeeBonus * 100;
        return score;
    }

}
