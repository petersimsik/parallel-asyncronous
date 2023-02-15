package com.learnjava.paralellstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSplitteratorExampleTest {

    ArrayListSplitteratorExample arrayListSplitteratorExample = new ArrayListSplitteratorExample();

    @RepeatedTest(5)
    void multiplyEachValueSequential() {
        //given
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        //when
        List<Integer> resultList = arrayListSplitteratorExample.multiplyEachValue(inputList, 2, false);

        //then
        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValueParallel() {
        //given
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        //when
        List<Integer> resultList = arrayListSplitteratorExample.multiplyEachValue(inputList, 2, true);

        //then
        assertEquals(size, resultList.size());
    }
}