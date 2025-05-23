package PraticeProject.ArraysPratice;

import java.util.Arrays;

public class LargestThree {
    public static void main(String[] args) {
        int[] arr = {10, 20, 4, 45, 99, 9, 99};
        Arrays.sort(arr);
        int n = arr.length;
        System.out.println("The largest three numbers are " + arr[n - 1] + ", " + arr[n - 2] + " and " + arr[n - 3]);
    }
}
