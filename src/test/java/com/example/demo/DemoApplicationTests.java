package com.example.demo;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DemoApplicationTests {

    @Test
    public void test() {
        int num = 2;
        List<String> list = Arrays.asList("1", "2");
        Consumer<String> runnable1 = System.out::println;
        Consumer<String> runnable2 = System.err::println;
        Function<Integer, String> intConvert = (item) -> String.valueOf(item * num);
        list.forEach(runnable1.andThen(runnable2));
        System.out.println(intConvert.apply(2));
    }

}
