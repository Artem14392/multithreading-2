package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class DataProcessor {
    ExecutorService service = Executors.newCachedThreadPool();
    private final Map<String, Integer> taskResult = new HashMap<>();

    public void sendTasks(int number) {
        AtomicInteger activeTasks = new AtomicInteger(1);
        for (int i = 1; i <= number; i++) {
            int taskNumber = activeTasks.getAndIncrement();
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return new CalculateSumTask(listRandom(), "task" + taskNumber).call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, service);

            completableFuture.thenAccept(result -> {
                synchronized (taskResult) {
                    taskResult.put("task" + taskNumber, result);
                }
                activeTasks.decrementAndGet();
            });

        }
        shutdown();

    }

    public Optional<Integer> resultByName(String task) {
        synchronized (taskResult) {
            if (taskResult.containsKey(task)) {
                return Optional.of(taskResult.get(task));
            } else return Optional.empty();
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
