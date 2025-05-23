package PraticeProject.ArraysPratice;

public class MinIncrements {

public static void main(String[] args) {
    int[] test1 = {1, 2, 3};       // 0+1+0 = 1
    int[] test2 = {3, 3, 3};        // 0
    int i[] = {5, 2, 8, 4};
    int n = i.length;
    int max = Integer.MIN_VALUE;
    for(int j=0;j<n;j++){
        if(i[j]>max){
            max=i[j];
        }
    }
    int sum = 0;
    for(int j=0;j<n;j++){
        sum+=i[j];
    }
    int ans = (max * n) - sum;
    System.out.println(ans);
}
}
