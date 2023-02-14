package com.learnjava.paralellstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.learnjava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamExampleTest {

    ParallelStreamExample parallelStreamExample = new ParallelStreamExample();

    @Test
    void stringTransform() {
        //given
        List<String> inputList = DataSet.namesList();

        //when
        List<String> resultList = parallelStreamExample.stringTransform(inputList);

        //then
        assertEquals(4, resultList.size(), "Result list has wrong size");
        resultList.forEach(name -> assertTrue(name.contains("-"), "Name: " + name + " does not contain '-' "));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void stringTransform1(boolean isParallel) {
        //given
        List<String> inputList = DataSet.namesList();

        //when
        startTimer();
        List<String> resultList = parallelStreamExample.stringTransform1(inputList, isParallel);
        timeTaken();
        stopWatchReset();

        //then
        assertEquals(4, resultList.size(), "Result list has wrong size");
        resultList.forEach(name -> assertTrue(name.contains("-"), "Name: " + name + " does not contain '-' "));
    }
}