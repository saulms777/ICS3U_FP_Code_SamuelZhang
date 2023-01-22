// imports
import java.util.Map;

class Player {

    // player variables
    private final int playerNum;
    private final int[] singles;
    private final int[] combos;
    private int jackpotBonus;
    private final Map<String, Integer> categories;

    /**
     * Player object for storing data of each player.
     *
     * @param num Player's number
     */
    public Player(int num) {
        playerNum = num;
        singles = Utils.initialArray(6);
        combos = Utils.initialArray(7);
        jackpotBonus = 0;
        categories = Map.ofEntries(
                Map.entry("triple", 0),
                Map.entry("quad", 1),
                Map.entry("special", 2),
                Map.entry("four", 3),
                Map.entry("five", 4),
                Map.entry("chance", 5),
                Map.entry("jackpot", 6)
        );
    }

    /**
     * Gets the player's number.
     *
     * @return Player's number
     */
    int getPlayerNum() {
        return playerNum;
    }

    /**
     * Gets the player's money in the singles section.
     *
     * @return <code>int</code> array of singles section
     */
    int[] getSingles() {
        return singles;
    }

    /**
     * Calculates and returns the total score for the singles section.
     *
     * @return Total money in the singles section
     */
    int getSinglesTotal() {
        int score = Utils.arraySum(singles);
        if (score >= 50) {
            score += 25;
        }
        return score;
    }

    /**
     * Gets the player's money in the combos section.
     *
     * @return <code>int</code> array of combos section
     */
    int[] getCombos() {
        return combos;
    }

    /**
     * Calculates and returns the total score for the combos section.
     *
     * @return Total money in the combos section
     */
    int getCombosTotal() {
        return Utils.arraySum(combos) + jackpotBonus * 100;
    }

    /**
     * Gets the amount of jackpot bonuses the player has.
     *
     * @return Amount of jackpot bonuses
     */
    int getJackpotBonus() {
        return jackpotBonus;
    }

    /**
     * Adds one to the jackpot bonus.
     */
    void addExtraJackpot() {
        jackpotBonus++;
    }

    /**
     * Calculates and returns the player's total score.
     *
     * @return Total score
     */
    int getTotalScore() {
        return getSinglesTotal() + getCombosTotal();
    }

    /**
     * Checks if a category is empty or not. If a category has the value -1 it is empty.
     *
     * @param category Category to check
     * @return <code>true</code> if category is empty else <code>false</code>
     */
    boolean isCategoryEmpty(String category) {
        switch (category) {
            // singles section category check
            case "1", "2", "3", "4", "5", "6" -> {
                return singles[Utils.parseInt(category) - 1] == -1;
            }
            // combos section category check
            default -> {
                return combos[categories.get(category)] == -1;
            }
        }
    }

    /**
     * Updates the player's data for a specified category.
     *
     * @param dice Current dice roll
     * @param category Category to update
     * @param valid If the category's requirements were met
     */
    void update(int[] dice, String category, boolean valid) {
        switch (category) {
            // singles section category update
            case "1", "2", "3", "4", "5", "6" -> {
                int num = Utils.parseInt(category);
                singles[num - 1] = num * Utils.frequency(dice, num);
            }

            // combos section category update
            default -> {
                int index = categories.get(category);
                if (valid) {
                    // update score if category requirements were met
                    switch (index) {
                        case 0, 1, 5 -> combos[index] = Utils.arraySum(dice);
                        case 2 -> combos[2] = 25;
                        case 3 -> combos[3] = 30;
                        case 4 -> combos[4] = 40;
                        case 6 -> combos[5] = 50;
                    }
                } else {
                    // set score to 0 if category requirements were not met
                    combos[categories.get(category)] = 0;
                }
            }
        }
    }

}
