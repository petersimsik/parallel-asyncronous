package com.learnjava.paralellstreams;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;

public class ArrayListSplitteratorExample {

    public List<Integer> multiplyEachValue(ArrayList<Integer> inputList, int multiplyer, boolean isParallel) {
        stopWatchReset();
        startTimer();
        Stream<Integer> integerStream = inputList
                .stream();

        if (isParallel)
            integerStream.parallel();

        List<Integer> resultList = integerStream
                .map(integer -> integer * multiplyer)
                .collect(Collectors.toList());
        timeTaken();

        return  resultList;
    }
}
