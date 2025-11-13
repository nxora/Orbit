package org.dave;

import java.util.ArrayList;
import java.util.List;

public class JobScheduler {
    private final List<Job> jobs = new ArrayList<>();
    private final JobExecutor executor = new JobExecutor();
    private boolean running = false;
    public void registerJob(Job job){
        jobs.add(job);
        System.out.println("Orbit has registered Job: " + job.getName());
    }

    public void start() {
        if (running) return;
        running = true;
        System.out.println("Orbit is running ");

        new Thread(() -> {
            while (running) {
                long now = System.currentTimeMillis();

                for (Job job : jobs){
                    if (job.getLastRunTime() == null || now - job.getLastRunTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() >= job.getIntervalMillis()) {
                        executor.execute(job);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        System.out.println("Orbit Stopped");
    }
}
