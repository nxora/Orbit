package org.dave;

import java.time.LocalDateTime;
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

    public Job(  String name, long intervalMillis, Runnable task ) {
        this.status = JobStatus.WAITING;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.lastRunTime = null;
        this.task = task;
        this.intervalMillis = intervalMillis;
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public long getIntervalMillis() {return intervalMillis;}

    public Runnable getTask() {return task;}

    public LocalDateTime getLastRunTime() {return lastRunTime;}

    public void setLastRunTime(LocalDateTime lastRunTime) {this.lastRunTime = lastRunTime;}

    public JobStatus getStatus() {return status;}

    public int getRetryCount() {return retryCount;}
    public void incrementRetryCount () {this.retryCount++;}
    public int getMaxRetries() {return maxRetries;}
    public void setStatus(JobStatus status) {this.status = status;}

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
