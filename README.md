# 🚀 RTB Auction Service – Production-Grade SRE/DevOps Stack

## 📌 Overview

This project demonstrates a **real-time bidding (RTB) auction service** built with a production-style **SRE + DevOps architecture**.

It showcases:

* High-performance Spring Boot service
* Reverse proxy routing via Nginx
* Observability using Prometheus & Grafana
* Database + caching layers (PostgreSQL + Redis)
* Containerized deployment using Docker

---

## 🏗️ Architecture

```
                ┌──────────────┐
                │   Clients    │
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │    Nginx     │ (Port 80)
                └──────┬───────┘
                       │
        ┌──────────────┼──────────────┐
        ▼                              ▼
┌──────────────┐              ┌──────────────┐
│ RTB Service  │              │   Jenkins    │
│ (SpringBoot) │              │ (Optional)   │
│ Port: 8081   │              │ Port: 8080   │
└──────┬───────┘
       │
       ▼
┌──────────────┐      ┌──────────────┐
│ PostgreSQL   │      │ Redis Cache  │
│ Port: 5432   │      │ Port: 6379   │
└──────────────┘      └──────────────┘

       │
       ▼
┌──────────────┐
│ Prometheus   │ (9090)
└──────┬───────┘
       ▼
┌──────────────┐
│ Grafana      │ (3000)
└──────────────┘
```

---

## ⚙️ Tech Stack

* **Backend:** Java 21, Spring Boot
* **Database:** PostgreSQL 16
* **Cache:** Redis
* **Reverse Proxy:** Nginx
* **Monitoring:** Prometheus
* **Visualization:** Grafana
* **Containerization:** Docker
* **CI/CD (optional):** GitHub Actions

---

## 🚀 Running the Application

### 1. Build the application

```bash
./gradlew clean build -x test
```

---

### 2. Start RTB Service

```bash
nohup java \
-Dspring.datasource.url=jdbc:postgresql://localhost:5432/rtbdb \
-Dspring.datasource.username=rtbuser \
-Dspring.datasource.password=rtbpass \
-jar build/libs/demo-0.0.1-SNAPSHOT.jar \
--server.port=8081 > app.log 2>&1 &
```

---

### 3. Start Infrastructure (Docker)

```bash
# PostgreSQL
docker run -d --name rtb-postgres \
-p 5432:5432 \
-e POSTGRES_DB=rtbdb \
-e POSTGRES_USER=rtbuser \
-e POSTGRES_PASSWORD=rtbpass \
postgres:16

# Redis
docker run -d --name rtb-redis -p 6379:6379 redis:7

# Prometheus
docker run -d --name adtech-prometheus \
-p 9090:9090 \
-v /opt/adtech/rtb-auction-service/prometheus.yml:/etc/prometheus/prometheus.yml \
prom/prometheus

# Grafana
docker run -d --name adtech-grafana -p 3000:3000 grafana/grafana

# Nginx
docker run -d --name nginx \
-p 80:80 \
-v ~/nginx/default.conf:/etc/nginx/conf.d/default.conf \
nginx
```

---

## 🌐 Endpoints

| Service    | URL                         |
| ---------- | --------------------------- |
| App Health | http://<EC2-IP>:8081/health |
| App Root   | http://<EC2-IP>/            |
| Prometheus | http://<EC2-IP>:9090        |
| Grafana    | http://<EC2-IP>:3000        |

---

## 📊 Observability

### Prometheus Metrics Endpoint

```bash
curl http://localhost:8081/actuator/prometheus
```

---

### Key Metrics

* JVM Memory Usage
* HTTP Request Rate
* DB Connections
* GC Metrics

---

### Sample PromQL Queries

```promql
up
```

```promql
jvm_memory_used_bytes{job="rtb-auction-service"}
```

```promql
rate(http_server_requests_seconds_count[1m])
```

---

## 📈 Grafana Setup

1. Login:

   ```
   admin / admin
   ```

2. Add Prometheus datasource:

   ```
   http://adtech-prometheus:9090
   ```

3. Create dashboard panels using queries above

---

## 🔧 Nginx Reverse Proxy

```nginx
server {
    listen 80;

    location / {
        proxy_pass http://172.17.0.1:8081;
    }

    location /grafana/ {
        proxy_pass http://172.17.0.1:3000/;
    }

    location /prometheus/ {
        proxy_pass http://172.17.0.1:9090/;
    }
}
```

---

## 🧪 Testing

```bash
curl http://localhost/
curl http://localhost:8081/health
curl http://localhost:9090/targets
```

---

## 🔥 Production SRE Concepts Demonstrated

* Reverse proxy routing (Nginx)
* Metrics scraping (Prometheus)
* Visualization dashboards (Grafana)
* Service observability (Spring Actuator)
* Dockerized infrastructure
* Failure debugging (502, 404, DB issues)

---

## 🚀 Future Enhancements

* Kubernetes deployment (EKS)
* Auto-scaling + Load balancing
* Alerting (Slack/Email)
* Distributed tracing (Jaeger)
* AI-based anomaly detection (AWS Bedrock)

---

## 👨‍💻 Author

**Clement Stephenraj**
SRE | DevOps | Performance Engineering Leader

---
## ⭐ If you like this project

Give it a star ⭐ on GitHub
