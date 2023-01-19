final class Utils {

    /**
     * Creates and returns an integer array of specified length with initial value -1.
     *
     * @param length Length of array
     * @return Created array
     */
    static int[] initialArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
        return array;
    }

    /**
     * Converts the first character of a given String to an integer value.
     *
     * @param str Given String
     * @return Converted integer
     */
    static int parseInt(String str) {
        return (int) str.charAt(0) - 48;
    }

    /**
     * Finds the number of times a given integer appears in a given array.
     *
     * @param array Given array
     * @param num Number to find
     * @return Occurences of number in array
     */
    static int frequency(int[] array, int num) {
        int count = 0;
        for (int n : array) {
            if (n == num) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the sum of all elements in a given array, counting values of -1 as 0.
     *
     * @param array Given array
     * @return Sum of array
     */
    static int arraySum(int[] array) {
        int sum = 0;
        for (int num : array) {
            if (num != -1) {
                sum += num;
            }
        }
        return sum;
    }

    /**
     * Sorts and returns an array using counting sort algorithm
     *
     * @param array Given array
     * @return Sorted array
     */
    static int[] sortDice(int[] array) {
        // count occurrences of each number
        int[] count = new int[6];
        for (int num : array) {
            count[num - 1]++;
        }

        /*
         * Iterate through each count in the count array. For each number add that number to the sorted list n times,
         * where n is the count of that number. Return the sorted list after iterating through each count.
         */
        int[] sorted = new int[array.length];
        int position = 0;
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[i]; j++) {
                sorted[position] = i - 1;
                position++;
            }
        }
        return sorted;
    }

    /**
     * Checks if a given String is in a given array.
     *
     * @param str Given String
     * @param arr Given array
     * @return If the String is found in array
     */
    static boolean inArray(String str, String[] arr) {
        for (String s : arr) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }

}
