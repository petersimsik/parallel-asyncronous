package com.learnjava.paralellstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamExample {
    public static void main(String[] args) {
        List<String> namesList = DataSet.namesList();
        ParallelStreamExample paralellStreamExample = new ParallelStreamExample();
        startTimer();
        List<String> resultList = paralellStreamExample.stringTransform(namesList);
        timeTaken();
        log("resultList: " + resultList.toString());
    }

    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    public List<String> stringTransform(List<String> namesList) {
        return namesList
                .parallelStream()
                .map(name -> addNameLengthTransform(name))
                .collect(Collectors.toList());
    }

    public List<String> stringTransform1(List<String> namesList, boolean isParallel) {
        Stream<String> namesListStream = namesList.stream();
        if (isParallel)
            namesListStream.parallel();

        return namesListStream
                .map(name -> addNameLengthTransform(name))
                .collect(Collectors.toList());
    }
}
