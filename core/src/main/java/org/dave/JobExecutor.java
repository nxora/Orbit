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
            job.setStatus(JobStatus.RUNNING);
            System.out.println("Orbit is Running job: " + job.getName());
            try {
                job.getTask().run();
                job.setLastRunTime(LocalDateTime.now());
                job.setStatus(JobStatus.SUCCESS);
                System.out.println("Orbit Says Job " + job.getName() + " finished running at " + job.getLastRunTime());
                JobHistoryLogger.log(job,"SUCCESS");

            } catch (Exception e) {
                job.incrementRetryCount();
                job.setStatus(JobStatus.FAILED);
                JobHistoryLogger.log(job,"FAILED" + e.getMessage());

                if (job.getRetryCount() < job.getMaxRetries()) {
                    System.err.println("Orbit says Job " + job.getName() + " failed. Retrying (" +
                            job.getRetryCount() + "/" + job.getMaxRetries() + ")...");
                    execute(job);
                } else {
                    System.err.println("[Orbit] 🚫 Job " + job.getName() + " failed permanently after max retries.");
                }
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
