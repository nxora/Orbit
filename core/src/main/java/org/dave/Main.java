package org.dave;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        JobScheduler scheduler = new JobScheduler();

        Job FirstJob = new Job("Run this Job", 3000, () -> {
            System.out.println("Running this Job " + System.currentTimeMillis());
        });

        Job SecondJob = new Job("Random Number", 5000, () -> {
            int random = (int) (Math.random() * 100);
            System.out.println("New No: " + random);
        });

        scheduler.registerJob(FirstJob);
        scheduler.registerJob(SecondJob);
        scheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stop));
    }
}