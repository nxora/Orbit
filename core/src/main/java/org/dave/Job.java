package org.dave;

import java.time.LocalDateTime;
import java.util.UUID;

public class Job {
    private final String id;
    private final String name;
    private final long intervalMillis;
    private final Runnable task;
    private LocalDateTime lastRunTime;

    public Job(  String name, long intervalMillis, Runnable task ) {
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
