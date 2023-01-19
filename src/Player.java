import java.util.Map;

class Player {

    private final int playerNum;
    private final int[] upperSection;
    private final int[] lowerSection;
    private int yahtzeeBonus;
    private final Map<String, Integer> categories;

    public Player(int num) {
        playerNum = num;
        upperSection = Utils.initialArray(6);
        lowerSection = Utils.initialArray(7);
        yahtzeeBonus = 0;
        categories = Map.ofEntries(
                Map.entry("3same", 0),
                Map.entry("4same", 1),
                Map.entry("full", 2),
                Map.entry("small", 3),
                Map.entry("large", 4),
                Map.entry("chance", 5),
                Map.entry("yahtzee", 6)
        );
    }
    int getPlayerNum() {
        return playerNum;
    }

    int[] getUpperSection() {
        return upperSection;
    }

    int getUpperTotal() {
        int score = Utils.arraySum(upperSection);
        if (score >= 63) {
            score += 35;
        }
        return score;
    }

    int[] getLowerSection() {
        return lowerSection;
    }

    int getLowerTotal() {
        return Utils.arraySum(lowerSection) + yahtzeeBonus * 100;
    }

    int getYahtzeeBonus() {
        return yahtzeeBonus;
    }

    void extraYahtzee() {
        yahtzeeBonus++;
    }

    int getTotalScore() {
        return getUpperTotal() + getLowerTotal();
    }

    boolean isCategoryEmpty(String category) {
        switch (category) {
            case "1", "2", "3", "4", "5", "6" -> {
                return upperSection[Utils.parseInt(category) - 1] == -1;
            }
            default -> {
                return lowerSection[categories.get(category)] == -1;
            }
        }
    }

    void update(int[] dice, String category, boolean valid) {
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

}
