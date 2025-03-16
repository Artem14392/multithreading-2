package org.example;

import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {

        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.sendTasks(1000);
        System.out.println("активные задачи" + dataProcessor.getActiveTaskCount());
        Optional<Integer> res = dataProcessor.resultByName("task409");
        if (res.isEmpty()){
            System.out.println("нет данных");
        }
        else System.out.println(res.get());

        dataProcessor.generalResultByTask();

    }
}
