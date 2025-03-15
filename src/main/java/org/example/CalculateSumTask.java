package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class CalculateSumTask implements Callable<Integer> {
    private List<Integer> list = new ArrayList<>();
    private final String task;

    public CalculateSumTask(List<Integer> list, String task){
        this.list = list;
        this.task = task;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("имя текущего потока: " + Thread.currentThread().getName() + " задача: " + task);
        Thread.sleep(200);
        return list.stream().parallel()
                .mapToInt(i -> i)
                .sum();
    }

}
