package Helpers;

import sample.Hardware;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HandyUtils {

    private void funct() {

        int[] numbers = {1, 2, 3};
        int min = IntStream.of(numbers).min().getAsInt();
        IntSummaryStatistics statistics = IntStream.of(numbers).summaryStatistics();
        double average = statistics.getAverage();
        IntStream.of(numbers).
                distinct().
                sorted().
                limit(3).
                skip(2).
                filter(n -> n % 2 == 0).
                map(nu -> nu * nu).
                boxed().
                forEach(System.out::println);

//      allmatch  anyMatch(n->n%2==1) // boolean


        List<Hardware> hw = new ArrayList<>();
        List<String> collect = hw.stream().sorted(Comparator.comparing(Hardware::getUniqueName).reversed())
                .limit(2)
                .map(Hardware::getUniqueName)
                .collect(Collectors.toList());

        List<Hardware> hw2 = new ArrayList<>();
        Map<String, String> collect1 = hw2.stream().sorted(Comparator.comparing(Hardware::getUniqueName).reversed())
                .limit(2)
                .collect(Collectors.toMap(Hardware::getTxString, Hardware::getUniqueName));


        List<Hardware> hw3 = new ArrayList<>();
        String collect2 = hw3.stream()
                .map(Hardware::getRfString)
                .collect(Collectors.joining(" "));

        List<Hardware> hw4 = new ArrayList<>();
        Map<String, List<Hardware>> collect3 = hw4.stream()
                .collect(Collectors.groupingBy(Hardware::getRfString));

        List<Hardware> hw5 = new ArrayList<>();
//        Map<HashMap<String, Long>, Long> collect4 = hw5.stream()
//                .collect(Collectors.groupingBy(Hardware::getModules, Collectors.counting()));


    }
}
