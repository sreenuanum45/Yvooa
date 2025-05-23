package PraticeProject.ArraysPratice;

public class SmallestPosstiveNumberIndex {
    public static void main(String[] args) {
        int[] arr={-1,0,-4,-5,3,4,5,6,7,8,9};
        int smallestvalue=Integer.MAX_VALUE;
        int index=0;
        int n=arr.length;
        for(int i=0;i<n;i++){
            if(arr[i]>0){
                if (arr[i]<=smallestvalue){
                    smallestvalue=arr[i];
                    index=i;

                }
            }
        }
        System.out.println("The smallest positive number is "+smallestvalue+" and its index is "+index);
    }
}
