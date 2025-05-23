package PraticeProject.ArraysPratice;

import java.util.Arrays;

public class RemoveDuplicates {
    public static void main(String[] args) {
//        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5};
//        int n = arr.length;
//        int[] temp = new int[n];
//        int j = 0;
//
//        for (int i = 0; i < n; i++) {
//            boolean isDuplicate = false;
//            for (int k = 0; k < j; k++) {
//                if (arr[i] == temp[k]) {
//                    isDuplicate = true;
//                    break;
//                }
//            }
//            if (!isDuplicate) {
//                temp[j++] = arr[i];
//            }
//        }
//
//        // Print the array without duplicates
//        for (int i = 0; i < j; i++) {
//            System.out.print(temp[i] + " ");
//        }
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5};
        int n = arr.length;

        if (n == 0) {
            System.out.println("[]");
            return;
        }

        Arrays.sort(arr); // Important: sort the array first

        int[] temp = new int[n];
        temp[0] = arr[0];
        int j = 0;

        for (int i = 1; i < n; i++) {
            if (arr[i] != temp[j]) {
                j++;
                temp[j] = arr[i];
            }
        }

        // Print unique elements
        System.out.print("Array without duplicates: ");
        for (int i = 0; i <= j; i++) {
            System.out.print(temp[i] + " ");
        }

    }
}
