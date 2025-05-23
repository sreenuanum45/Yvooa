package PraticeProject.ArraysPratice;

public class Test1 {
    public static void main(String[] args) {
        int[] x={1,3,4,4,4,2,6,9,5,7,1,2,3,5,8,8,9,1,2,3,4,5,6,7,8,9};
        int k=6;
        int a;
        int count =0;
       a=x.length/k;
        System.out.println("The value of a is "+a);
        for (int j=0;j<x.length;j++)
        {
            for(int b=0;b<x.length;b++){
                if(x[j]==x[b])
                {
                    count++;
                }
            }
            if(count>=a){
                System.out.println(x[j]+" is repeated "+count+" times");
            }
            count=0;
        }
    }
}
