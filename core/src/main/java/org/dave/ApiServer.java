package org.dave;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
public class ApiServer {

        private final JobScheduler scheduler;

        public ApiServer(JobScheduler scheduler) {
            this.scheduler = scheduler;
        }

        public void start(int port) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/api/jobs", new JobsHandler());
            server.createContext("/api/run", new RunJobHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("[API] Orbit API running at http://localhost:" + port + "/api/jobs");
        }

        class JobsHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                List<Job> jobs = scheduler.getJobs();
                String json = jobs.stream()
                        .map(job -> String.format(
                                "{\"id\":\"%s\",\"name\":\"%s\",\"status\":\"%s\",\"priority\":%d,\"deps\":%s}",
                                job.getId(), job.getName(), job.getStatus(),
                                job.getPriority(), job.getDependencyIds()))
                        .collect(Collectors.joining(",", "[", "]"));
                send(exchange, 200, json);
            }
        }

        class RunJobHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery(); // e.g. ?id=jobId
                String jobId = query != null && query.startsWith("id=") ? query.substring(3) : null;

                if (jobId != null) {
                    boolean result = scheduler.runJobNow(jobId);
                    send(exchange, 200, "{\"success\":" + result + "}");
                } else {
                    send(exchange, 400, "{\"error\":\"Missing id param\"}");
                }
            }
        }

        private void send(HttpExchange ex, int status, String body) throws IOException {
            ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // allow Vercel frontend
            ex.sendResponseHeaders(status, body.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

