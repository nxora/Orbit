package org.dave;


import java.time.LocalDateTime;

public class JobExecutor {

    public void execute(Job job){
        System.out.println("Orbit is Running job: " + job.getName());
        try {
            job.getTask().run();
            job.setLastRunTime(LocalDateTime.now());
            System.out.println("Orbit Says Job " + job.getName() + " finished running at " + job.getLastRunTime());

        } catch (Exception e) {
            System.err.println("Orbit says Job: " + job.getName() + " failed. " + e.getMessage());
            e.printStackTrace();
        }
    }

}
