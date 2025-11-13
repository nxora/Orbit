package org.dave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobScheduler {
    private final List<Job> jobs = new ArrayList<>();
    private final JobExecutor executor = new JobExecutor();
    private boolean running = false;
    private Map<String, Job > jobMap = new HashMap<>();

    public void registerJob(Job job){
        jobs.add(job);
        jobMap.put(job.getId(), job);
        System.out.println("Orbit has registered Job: " + job.getName());
        JobManager.saveJobs(jobs);
    }

    public void start() {
        if (running) return;
        running = true;
        System.out.println("Orbit is running ");

        Thread loopThread = new Thread(() -> {
            while (running) {
                long now = System.currentTimeMillis();

                for (Job job : jobs){
                    if (!running) break;
                    boolean ready = job.dependenciesSatisfied(jobMap) &&  job.getLastRunTime() == null || now - job.getLastRunTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() >= job.getIntervalMillis();
                    if (ready) {
                        try {
                            executor.execute(job);
                        } catch (Exception e) {
                            System.err.println("Orbit couldn't execute  job " + job.getName() + ": " + e.getMessage());
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("[Orbit] Scheduler loop exited.");
        });
        loopThread.setDaemon(true);
        loopThread.start();
    }

    public void stop() {
        System.out.println("Orbit Stopping scheduler...");
        running = false;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
         }
        executor.shutDown();
        JobManager.saveJobs(jobs);
        System.out.println("Orbit says Scheduler stopped gracefully.");
    }
}
