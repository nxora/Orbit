package org.dave;


import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JobExecutor {

    private final ExecutorService pool = Executors.newFixedThreadPool(5);



    public void execute(Job job){
        pool.submit(() -> {
            System.out.println("Orbit is Running job: " + job.getName());
            try {
                job.getTask().run();
                job.setLastRunTime(LocalDateTime.now());
                System.out.println("Orbit Says Job " + job.getName() + " finished running at " + job.getLastRunTime());

            } catch (Exception e) {
                System.err.println("Orbit says Job: " + job.getName() + " failed. " + e.getMessage());
                e.printStackTrace();
        }

        });
    }

    public void shutDown(){
        System.out.println("Orbit shutting Down executor....");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)){
                System.out.println("Orbit Forcing shutdown....");
                pool.shutdown();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
