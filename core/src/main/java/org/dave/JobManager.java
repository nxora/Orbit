package org.dave;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JobManager {

        private static final String FILE_PATH = "jobs.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        public static synchronized void saveJobs(List<Job> jobs) {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), jobs);
                System.out.println("Orbit Jobs saved to " + FILE_PATH);
            } catch (IOException e) {
                System.err.println("Orbit Failed to save jobs: " + e.getMessage());
            }
        }

        public static synchronized List<Job> loadJobs() {
            try {
                File file = new File(FILE_PATH);
                if (!file.exists()) return List.of();
                return mapper.readValue(file, new TypeReference<List<Job>>() {});
            } catch (IOException e) {
                System.err.println("Orbit Failed to load jobs: " + e.getMessage());
                return List.of();
            }
        }
    }


