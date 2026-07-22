# distributed-systems-learning

distributed-systems-learning

├── order-service
│   ├── REST API
│   ├── Outbox
│   ├── PostgreSQL
│   └── Flyway
│
├── payment-service
│
├── inventory-service
│
├── shipping-service
│
├── notification-service
│
├── kafka
│
├── docker
│
├── monitoring
│   ├── Prometheus
│   ├── Grafana
│   └── Loki
│
├── docs
│   ├── Architecture.md
│   ├── SequenceDiagrams
│   ├── Decisions
│   └── Tradeoffs
│
└── README.md

feat(order): implement transactional outbox
feat(kafka): publish OrderCreated events
refactor(outbox): extract publisher abstraction


ADR-001

Why Transactional Outbox?

Problem

HTTP call after DB commit can lose events.

Decision

Use Transactional Outbox.

Consequences

+ Reliable publishing
- Additional table
- Background publisher

ADR-002

Why Kafka instead of RabbitMQ?

I'll review:

Naming
Architecture
Thread safety
Transaction boundaries
Performance
Spring Boot conventions
Error handling
Testing
Readability
Production readiness

I want will build something that demonstrates:
Java
Spring Boot
PostgreSQL
Kafka
Distributed Systems
Clean Architecture
Event-Driven Design
Testing
Docker
Observability