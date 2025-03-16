package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class DataProcessor {
    ExecutorService service = Executors.newCachedThreadPool();
    private final Map<String, Integer> taskResult = new HashMap<>();
    private final AtomicInteger taskCounter = new AtomicInteger(0);
    private final AtomicInteger activeTasks = new AtomicInteger(0);

    public void sendTasks(int number) {
        for (int i = 1; i <= number; i++) {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                Integer res = 0;
                try {res = new CalculateSumTask(listRandom(), "task" + taskCounter).call();} catch (Exception _) {}
                return res;
            }, service);
            taskCounter.incrementAndGet();
            activeTasks.incrementAndGet();
            completableFuture.thenAccept(result -> {
                synchronized (taskResult){
                    taskResult.put("task" + taskCounter, result);
                }
                activeTasks.decrementAndGet();
            });

        }
        shutdown();

    }

    public int getActiveTaskCount() {
        return activeTasks.get();
    }

    public Optional<Integer> resultByName(String task) {
        synchronized (taskResult) {
            return Optional.ofNullable(taskResult.get(task));
        }

    }

    public void generalResultByTask() {
        synchronized (taskResult) {
            for (Map.Entry<String, Integer> item : taskResult.entrySet()) {
                System.out.printf("Key: %s  Value: %d \n", item.getKey(), item.getValue());
            }
        }

    }

    private List<Integer> listRandom() {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        int size = random.nextInt(3, 40);
        for (int i = 0; i < size; i++) {
            list.add(i, random.nextInt(1, 100));
        }
        return list;
    }

    public void shutdown() {
        service.shutdown();
        try {
            boolean isTerminated = service.awaitTermination(10, TimeUnit.MILLISECONDS);
            if (!isTerminated) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
            throw new RuntimeException(e);
        }
    }

}
