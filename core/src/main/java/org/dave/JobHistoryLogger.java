package org.dave;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class JobHistoryLogger {
    private static final String LOG_FILE = "job_history.log";

    public static synchronized void log(Job job, String message) {
        String logEntry = "[" + LocalDateTime.now() + "] " +
                "Job: " + job.getName() +
                " | Status: " + job.getStatus() +
                " | Message: " + message +
                " | Dependencies: " + job.getDependencyIds() +
                " | Priority: " + job.getPriority() +
                System.lineSeparator();

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("[Orbit] Could not write to history log: " + e.getMessage());
        }
    }
}
