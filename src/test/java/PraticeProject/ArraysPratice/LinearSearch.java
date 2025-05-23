package PraticeProject.ArraysPratice;

public class LinearSearch {
    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 4, 4, 2, 6, 9, 5, 7, 1, 2, 3, 5, 8, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int k = 6;
        int count = 0;
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] == k) {
                count++;
            }
        }
        if (count > 0) {
            System.out.println("The element is present " + count + " times in the array");
        }
    }
}
