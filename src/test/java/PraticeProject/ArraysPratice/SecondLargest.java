package PraticeProject.ArraysPratice;

public class SecondLargest {
    public static void main(String[] args) {
        int[] arr= {12, 35, 1, 10, 34, 1};
        int firstLargest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        for(int num: arr){
            if(num>firstLargest){
                secondLargest=firstLargest;
                firstLargest=num;
            } else if (num>secondLargest && num!=firstLargest) {
                secondLargest=num;
            }

        }
        System.out.println("The second largest number is "+secondLargest);
        System.out.println("The first largest number is "+firstLargest);
    }
}
