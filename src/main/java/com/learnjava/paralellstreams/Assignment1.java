package com.learnjava.paralellstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.LoggerUtil.log;

public class Assignment1 {
    public static void main(String[] args) {
        List<String> inputList = DataSet.namesList();
        Assignment1 assignment1 = new Assignment1();
        List<String> resultList = assignment1.string_toLowerCase(inputList);
        log("resultList: " + resultList);
    }

    public List<String> string_toLowerCase(List<String> inputList){
        return inputList
                .parallelStream()
                .map(name -> name.toLowerCase())
                .collect(Collectors.toList());
    }
}
