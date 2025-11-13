package org.dave;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Job {
    private final String id;
    private final String name;
    private final long intervalMillis;
    private final Runnable task;
    private LocalDateTime lastRunTime;
    private JobStatus status;
    private int retryCount;
    private int maxRetries;
    private List<String> dependencyIds;
    private int priority;

    public Job(  String name, long intervalMillis, Runnable task, List<String> dependencyIds, int priority ) {
        this.status = JobStatus.WAITING;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.lastRunTime = null;
        this.task = task;
        this.intervalMillis = intervalMillis;
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.dependencyIds = dependencyIds;

    }

    public String getId() {return id;}

    public String getName() {return name;}

    public long getIntervalMillis() {return intervalMillis;}

    public Runnable getTask() {return task;}

    public LocalDateTime getLastRunTime() {return lastRunTime;}

    public void setLastRunTime(LocalDateTime lastRunTime) {this.lastRunTime = lastRunTime;}

    public JobStatus getStatus() {return status;}

    public List<String> getDependencyIds() {return dependencyIds;}

    public int getRetryCount() {return retryCount;}
    public void incrementRetryCount () {this.retryCount++;}
    public int getMaxRetries() {return maxRetries;}
    public void setStatus(JobStatus status) {this.status = status;}

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean dependenciesSatisfied (Map<String, Job> allJobs){
        if (dependencyIds == null || dependencyIds.isEmpty())return false;

        for (String depId : dependencyIds){
            Job depjob = allJobs.get(depId);
                if ( depjob == null || depjob.getStatus() != JobStatus.SUCCESS){
                    return false;
                }
        }
        return true;
    }

    @Override
    public String toString(){
        return "Job{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", intervalMillis=" + intervalMillis +
                ", lastRunTime=" + lastRunTime +
                '}';
    }
}
