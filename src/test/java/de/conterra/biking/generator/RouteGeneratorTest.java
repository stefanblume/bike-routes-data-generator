package de.conterra.biking.generator;


import java.text.ParseException;

import org.junit.Test;

public class RouteGeneratorTest {

    @Test
    public void testDataGeneration(){
        RouteGenerator r = new RouteGenerator();
        try {
            r.generate(7, 1000, "01.01.2021", "01.09.2021", "bike");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}