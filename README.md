# 🌌 ORBIT — Lightweight Java Job Scheduler + Web Dashboard

Orbit is a **lightweight, flexible Java-based automation engine** that lets you define background jobs, schedule them, track execution history, and interact with everything through a built-in **REST API** or a **Web Dashboard UI**.

A small but powerful alternative to cron, Airflow, or Quartz — simple enough for beginners, extensible enough for real projects.

---

## 🚀 Features

### ✅ Core Scheduler

* Add unlimited background jobs
* Fixed-interval execution (e.g., every 3s, 5s, 10s)
* Thread-pooled execution for safety & concurrency
* Graceful shutdown support
* Retry logic & status tracking

### 🌐 REST API (Built-In Web Server)

Your Java app exposes a JSON API automatically:

* `GET /api/jobs` — List all jobs
* `POST /api/jobs/run/{name}` — Trigger a job manually
* `GET /api/history` — View job history
* `GET /api/status` — Health/info

Perfect for dashboards, remote execution, or triggering jobs from other systems.

### 🖥️ Web Dashboard (HTML/JS)

Orbit includes an optional tiny web dashboard so you can:

* See registered jobs
* Monitor next run & last run
* View execution history
* Trigger jobs manually

### 🗂️ Persistence

Orbit saves job configuration and history to disk:

* `jobs.json` → list of registered jobs
* `jobs_history.log` → every run saved with timestamp + status

This means Orbit:

* Remembers jobs between restarts
* Keeps a full audit trail of activity

---

## 🧱 Tech Stack

### **Backend (Orbit Core)**

* **Java 20**
* **Maven** (build tool)
* **Jackson** (JSON persistence)
* **JDK HttpServer** (built-in lightweight web server)
* **Custom job scheduler** (no external dependencies)

### **Frontend (Dashboard)**

* HTML + JavaScript
* Fetches JSON from Orbit’s API
* Can be hosted on Vercel, Netlify, GitHub Pages

---

# 📦 Project Structure

```
Orbit/
 ├── core/
 │    ├── src/main/java/org/dave/
 │    │      ├── Job.java
 │    │      ├── JobScheduler.java
 │    │      ├── JobExecutor.java
 │    │      ├── ApiServer.java
 │    │      ├── WebDashboard.java
 │    │      └── Main.java
 │    ├── jobs.json
 │    ├── jobs_history.log
 │    └── pom.xml
 └── dashboard/ (optional React/HTML frontend)
```

---

# 🔨 Build Process (Maven)

## **1. Clean & Build Your JAR**

```bash
mvn clean package -f core/pom.xml
```

## **2. Resulting Shaded JAR**

After building, Maven generates:

```
core/target/core-1.0-SNAPSHOT-shaded.jar
```

This jar includes **all dependencies**, so you can run Orbit anywhere.

---

# ▶️ Run Orbit

```bash
java -jar core/target/core-1.0-SNAPSHOT-shaded.jar
```

You’ll see output like:

```
Orbit is running
[API] Orbit API running at http://localhost:8080/api/jobs
```

---

# 🌐 Using the API

### List all jobs

```
GET http://localhost:8080/api/jobs
```

### Trigger a job manually

```
POST http://localhost:8080/api/jobs/run/JobName
```

### Check job history

```
GET http://localhost:8080/api/history
```

---

# 📄 Job Persistence Files

## **📁 jobs.json** — Registered Jobs

Example:

```json
[
  {
    "name": "Random Number",
    "intervalMillis": 5000,
    "priority": 2,
    "states": ["WAITING", "TRIG"],
    "lastRunTime": "2025-11-14T09:21:00"
  }
]
```

You can edit this file to:

* Change job intervals
* Set priorities
* Add/remove jobs (Orbit reloads at startup)

---

## **📁 jobs_history.log** — All Executions

Example:

```
[2025-11-14T09:22:01] Executed Random Number (status=SUCCESS)
[2025-11-14T09:22:04] Executed Run this Job (status=SUCCESS)
```

You can:

* Track failures
* Debug job behavior
* Analyze timing/performance
* Build analytics on top of it

---

# 🔧 Architecture Overview

```
                   ┌──────────────┐
                   │  Scheduler   │
                   └──────┬───────┘
                          │
                    runs jobs
                          │
          ┌───────────────┴────────────────┐
          │                                │
   ┌──────────────┐                ┌───────────────┐
   │ JobExecutor  │ thread-pool    │   ApiServer   │ REST endpoints
   └──────────────┘                └───────────────┘
                                              │
                                    returns JSON to Dashboard
```

---

# 🧩 Customizing Orbit

## Add a new job

```java
Job emailJob = new Job(
    "SendEmail",         // name
    10000,                // run every 10s
    () -> sendEmail(),    // your logic
    states,
    3                     // priority
);

scheduler.registerJob(emailJob);
```

## Add API routes

Edit `ApiServer.java` — super simple.

---

# 🌍 Deployment

Orbit supports multiple deployment methods:

### **Local Machine (default)**

Just run the jar.

### **Railway / Render / Fly.io**

Orbit works perfectly on free tiers.
Just ensure you use:

```
java -jar core/target/core-1.0-SNAPSHOT-shaded.jar
```

And bind to the platform-provided `PORT` env.

### **Frontend (Dashboard)**

Deploy the HTML/React dashboard separately on:

* Vercel
* Netlify
* GitHub Pages

And point it to your Orbit backend URL.

---

# ⭐ Why Orbit?

Orbit was created to be:

* **Simple** — very little code
* **Understandable** — no magic frameworks
* **Extensible** — you can build a full distributed scheduler later
* **Educational** — great for learning concurrency, servers, APIs

It’s perfect as:

* A portfolio project
* A real automation engine
* Backend for sensors, tasks, microservices
* Developer cron alternative

---

# 🤝 Contributing

Feel free to fork, improve, or extend Orbit.
Future ideas:

* DAG dependencies ✅
* Retry policies ✅
* Distributed clusters
* Priority queues ✅
* WebSocket live updates ✅

---


- Author: [Daveora](https://github.com/davex-ai)

🎉 Final Notes

Orbit is more than a scheduler — it’s your own mini automation platform, built from scratch in Java. It’s transparent, hackable, and powerful.

If you want help extending it: DAG mode, distributed nodes, or a polished React dashboard — just ask!
