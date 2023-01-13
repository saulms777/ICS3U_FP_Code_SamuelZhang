public class Utils {

    public static int[] initialArray(int length, int value) {
        int[] array = new int[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
        return array;
    }

    public static int parseInt(String str) {
        if (str.length() != 1) {
            return -1;
        }
        return (int) str.charAt(0) - 48;
    }

    public static int frequency(int[] array, int num) {
        int count = 0;
        for (int n : array) {
            if (n == num) {
                count++;
            }
        }
        return count;
    }

    public static int arraySum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }

    public static int[] sortDice(int[] array) {
        int[] count = new int[6];
        for (int num : array) {
            count[num - 1]++;
        }
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

}
