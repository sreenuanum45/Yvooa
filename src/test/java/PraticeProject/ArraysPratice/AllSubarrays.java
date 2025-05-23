package PraticeProject.ArraysPratice;

public class AllSubarrays {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        int n = arr.length;
        for(int start=0;start<n;start++){
            for(int end =start;end<n;end++){
                System.out.print("[");
                for(int i=start;i<=end;i++){
                    System.out.print(arr[i]);
                    if(i<end){
                        System.out.print(",");
                    }
                }
                System.out.println("]");
            }
        }
    }
}
