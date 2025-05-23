package PraticeProject.ArraysPratice;

public class findLargest {
    public static void main(String[] args) {
        int [] x = {1, 3, 4, 4, 4, 2, 6, 9, 5, 7, 1, 2, 3, 5, 8, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int max=x[0];
        int min=x[0];
        for (int i=0;i<x.length;i++){
            if(x[i]>max){
                max=x[i];
            }
            if (x[i]<min){
                min=x[i];

            }
        }
        System.out.println("The largest number is "+max);
        System.out.println("The smallest number is "+min);}
}
