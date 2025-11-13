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
    private List<Job> readyJobs = new ArrayList<>();

    public void registerJob(Job job){
        if (hasCycle(job)) {
            System.err.println("Orbit Cannot register job '" + job.getName() + "': DAG cycle detected!");
            return;
        }
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
                readyJobs.clear();
                long now = System.currentTimeMillis();

                for (Job job : jobs){
                    if (!running) break;
                    boolean ready = job.dependenciesSatisfied(jobMap) &&  (job.getLastRunTime() == null || now - job.getLastRunTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() >= job.getIntervalMillis());
                    if (ready) {
                        readyJobs.add(job);
                    }
                }
                readyJobs.sort((job1,job2) -> Integer.compare(job1.getPriority(),job2.getPriority()));
                for (Job job : readyJobs) {
                        try {
                            executor.execute(job);
                        } catch (Exception e) {
                            System.err.println("Orbit couldn't execute  job " + job.getName() + ": " + e.getMessage());
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

    public boolean hasCycle(Job newJob) {
        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Boolean> recStack = new HashMap<>();

        for (Job job : jobs) {
            visited.put(job.getId(), false);
            recStack.put(job.getId(), false);
        }
        visited.put(newJob.getId(), false);
        recStack.put(newJob.getId(), false);

        return dfsCycleCheck(newJob, visited, recStack);
    }

    private boolean dfsCycleCheck(Job job, Map<String, Boolean> visited, Map<String, Boolean> recStack) {
        if (recStack.get(job.getId())) return true;
        if (visited.get(job.getId())) return false;

        visited.put(job.getId(), true);
        recStack.put(job.getId(), true);

        if (job.getDependencyIds() != null) {
            for (String depId : job.getDependencyIds()) {
                Job depJob = jobMap.get(depId);
                if (depJob != null && dfsCycleCheck(depJob, visited, recStack)) {
                    return true;
                }
            }
        }

        recStack.put(job.getId(), false);
        return false;
    }
    public List<Job> getJobs() {
        return List.copyOf(jobs); // return unmodifiable copy
    }
    public boolean runJobNow(String jobId) {
        Job job = jobMap.get(jobId);
        if (job != null) {
            try {
                executor.execute(job);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
