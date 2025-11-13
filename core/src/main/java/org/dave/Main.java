package org.dave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");

        List<String> list = new ArrayList<>();
        list.add("WAITING");
        list.add("TRIG");
        JobScheduler scheduler = new JobScheduler();

        Job FirstJob = new Job("Run this Job", 3000, () -> {
            System.out.println("Running this Job " + System.currentTimeMillis());
        },list,4);Job TJob = new Job("Blah", 3000, () -> {
            System.out.println("Saaa " + System.currentTimeMillis());
        },list,4);

        Job SecondJob = new Job("Random Number", 5000, () -> {
            int random = (int) (Math.random() * 100);
            System.out.println("New No: " + random);
        },list,2 );

        scheduler.registerJob(FirstJob);
        scheduler.registerJob(SecondJob);
        scheduler.registerJob(TJob);
        scheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stop));

        WebDashboard dashboard = new WebDashboard(scheduler);
        dashboard.start(8080);
    }
}