import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Player {

    private final int[] upperSection = new int[6];
    private final int[] lowerSection = new int[7];
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

    public Player() {
        Arrays.fill(lowerSection, -1);
    }

    public boolean isCategoryEmpty(String category) {
        boolean isEmpty;
        switch (category) {
            case "1", "2", "3", "4", "5", "6" -> isEmpty = upperSection[Integer.parseInt(category) - 1] == 0;
            default -> isEmpty = lowerSection[categories.get(category)] == -1;
        }
        return isEmpty;
    }

    public void update(int[] dice, String category, boolean valid) {
        switch (category) {
            case "1", "2", "3", "4", "5", "6" -> {
                int num = Integer.parseInt(category);
                upperSection[num - 1] = num * Collections.frequency(List.of(dice), num);
            }
            default -> {
                int index = categories.get(category);
                if (valid) {
                    switch (index) {
                        case 0, 1, 6 -> lowerSection[index] = IntStream.of(dice).sum();
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
        yahtzeeBonus += 1;
    }

    public int calculateScore() {
        int score = 0;
        score += IntStream.of(upperSection).sum();
        if (score >= 63) {
            score += 35;
        }
        score += IntStream.of(lowerSection).sum() + yahtzeeBonus * 100;
        return score;
    }

}
