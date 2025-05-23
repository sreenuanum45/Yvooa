package PraticeProject.ArraysPratice;

import java.util.Arrays;

public class binarySearch {
    public static void main(String[] args) {
        int target = 5;
        int [] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Arrays.sort(arr);
        int left = 0;
        int right = arr.length - 1;
        boolean found = false;
        while(left<=right){
            int mid=left+(right-left)/2;
            if(arr[mid]==target){
                System.out.println("The target is found at index "+mid);
                found=true;
                break;
            }
            else if(arr[mid]<target){
                left=mid+1;
            }
            else{
                right=mid-1;
            }
        }
        if(!found){
            System.out.println("The target is not found in the array");
        }
    }
}
