package PraticeProject.ArraysPratice;

public class selectionSort {
    public static void main(String[] args) {
        int [] arr = {64, 25, 12, 22, 11};
        int a=arr.length;
        for(int i=0;i<a;i++){
            int minid=i;
            for(int j=i+1;j<a;j++){
                if(arr[j]<arr[minid]){
                    minid=j;
                }
            }
            int temp=arr[minid];
            arr[minid]=arr[i];
            arr[i]=temp;
        }
     for (int i=0;i<a;i++){
         System.out.print(arr[i]+" ");
     }

    }
}
