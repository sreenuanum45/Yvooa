package PraticeProject.ArraysPratice;

public class ReverseCopy {
    public static void main(String[] args) {
        int[] original = {1, 2, 3, 4, 5};
        int n = original.length;
        int[] reversed = new int[n];

//        for (int i = 0; i < n; i++) {
//            reversed[i] = original[n - 1 - i];
//        }
        int j=0;
        for(int i = n - 1; i >= 0; i--) {
          reversed[j++]=original[i];
        }

        System.out.println("Original: " + java.util.Arrays.toString(original));
        System.out.println("Reversed: " + java.util.Arrays.toString(reversed));
    }
}
