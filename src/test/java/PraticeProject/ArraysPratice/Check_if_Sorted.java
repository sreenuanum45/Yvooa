package PraticeProject.ArraysPratice;

public class Check_if_Sorted {
    public static void main(String[] args) {
        int[] arr = {7,5,4,3,2,1};
        boolean isSorted = true;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                isSorted = false;
                break;
            }
        }
        if (isSorted) {
            System.out.println("The array is sorted");
        } else {
            System.out.println("The array is not sorted");
        }
    }
}
