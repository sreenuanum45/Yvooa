package PraticeProject.ArraysPratice;

public class Zeroes_to_End {
    public static void main(String[] args) {
        int [] x={1,0,2,0,3,0,4,0,5};
        int nonzero=0;
        int n= x.length;
        for(int i=0;i<x.length;i++){
            if(x[i]!=0)
            {
                x[nonzero]=x[i];
                nonzero++;
            }
        }
        for(int i=nonzero;i<n;i++){
            x[i]=0;
        }
    }
}
