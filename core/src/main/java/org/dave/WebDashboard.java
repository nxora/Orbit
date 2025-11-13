package org.dave;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class WebDashboard {

    private final JobScheduler scheduler;

    public WebDashboard(JobScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/jobs", new JobsHandler());
        server.createContext("/", new JobsHandler()); // redirect root to jobs
        server.setExecutor(null); // default executor
        server.start();
        System.out.println("Orbit Server running on http://localhost:" + port);
    }

    class JobsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Job> jobs = scheduler.getJobs(); // need a getter
            StringBuilder response = new StringBuilder();
            response.append("<html><head><title>Orbit Dashboard</title>");
            response.append("<meta http-equiv=\"refresh\" content=\"2\">"); // auto-refresh every 2s
            response.append("</head><body>");
            response.append("<h1>Orbit Job Scheduler Dashboard</h1>");
            response.append("<table border='1' style='background-color: red;'><tr><th>Job Name</th><th>Status</th><th>Last Run</th><th>Retries</th><th>Dependencies</th></tr>");
            for (Job job : jobs) {
                response.append("<tr>")
                        .append("<td>").append(job.getName()).append("</td>")
                        .append("<td>").append(job.getStatus()).append("</td>")
                        .append("<td>").append(job.getLastRunTime()).append("</td>")
                        .append("<td>").append(job.getRetryCount()).append("</td>")
                        .append("<td>").append(job.getDependencyIds()).append("</td>")
                        .append("</tr>");
            }
            response.append("</table></body></html>");

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
}
