package PraticeProject.ArraysPratice;

public class LeadersInArray {
    public static void main(String[] args) {
        int[] arr = {2,25, 17, 4, 3, 5, 2};
        int n=arr.length;
        for(int i=n-1;i>0;i--){
            if(arr[i]>arr[i-1]){
                System.out.println(arr[i]+" is a leader");
            }
            else if(i==n-1){
                System.out.println(arr[i]+" is a leader");
            }
        }

    }
}
