package org.example;

import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {

        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.sendTasks(30);
        Thread.sleep(2000);
        Optional<Integer> res = dataProcessor.resultByName("task4");
        if (res.isEmpty()){
            System.out.println("нет данных");
        }
        else System.out.println(res.get());

        dataProcessor.generalResultByTask();

    }
}
