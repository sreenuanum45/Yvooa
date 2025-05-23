package PraticeProject.ArraysPratice;

public class minIncrementsToEqual {
    public static void main(String[] args) {
        int[]x={1,2,3,4,5};
        int n=x.length;
        int max=x[0];
        int totalelements=0;
        for(int i=0;i<n;i++){
            if(x[i]>max){
                max=x[i];
            }
        }
        for(int i=0;i<n;i++){
            totalelements+=max-x[i];
        }
        System.out.println(totalelements);
    }
}
