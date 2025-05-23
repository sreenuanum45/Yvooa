package PraticeProject.ArraysPratice;

import java.util.*;
import java.util.stream.Collectors;

public class DistinctElements {
    public static void main(String[] args) {
        List<Integer>  list= List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer>ll=list.stream().distinct().collect(Collectors.toList());
        Set<Integer> ss=list.stream().collect(Collectors.toSet());
        Set<Integer> ss1=new HashSet<>(list);
        Set<Integer>ss2=new LinkedHashSet<>(list);
        System.out.println(ss2);
        List<Integer>distinctList=new ArrayList<>();
        for(Integer num :list){
            if (!distinctList.contains(num)) {
                distinctList.add(num);
            }
        }

    }
}
