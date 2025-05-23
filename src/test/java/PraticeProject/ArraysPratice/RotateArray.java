package PraticeProject.ArraysPratice;

public class RotateArray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        int k = 3; // Number of positions to rotate
        int n = arr.length;

        // Normalize k to avoid unnecessary rotations
        k = k % n;

        // Rotate the array
        reverse(arr, 0, n - 1);
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);

        // Print the rotated array
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
    public static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}
