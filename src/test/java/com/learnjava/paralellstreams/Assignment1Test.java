package com.learnjava.paralellstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Assignment1Test {

    Assignment1 assignment1 = new Assignment1();

    @Test
    void string_toLowerCase() {
        //given
        List<String> namesList = DataSet.namesList();

        //when
        List<String> resultList = assignment1.string_toLowerCase(namesList);

        //then
        assertEquals(4, resultList.size(), "Wrong result list size");
        resultList.forEach(name -> assertTrue(name.toLowerCase().equals(name), "Name " + name + " is not lowerCase"));
    }
}