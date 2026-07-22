# Architecture Decision Records

## ADR-001: Use Transactional Outbox for Domain Event Reliability

- Status: Accepted
- Date: 2026-07-22

### Context
`order-service` must persist business state (for example, orders) and publish integration events (for example, `OrderCreated`).
If event publication happens outside the same database transaction, process crashes or network issues can leave data committed but events unpublished.

### Decision
Use the **Transactional Outbox** pattern:
- Write the domain change and an `outbox_events` record in the same local database transaction.
- Persist the event payload and metadata (`aggregate_type`, `aggregate_id`, `event_type`, `created_at`, `published`).
- Treat the outbox table as the source of pending events for asynchronous publication.

### Consequences
Positive:
- Prevents "order saved but event lost" scenarios.
- Preserves event intent durably before broker publication.
- Supports retries and operational replay from persisted outbox records.

Trade-offs:
- Adds schema and code complexity.
- Requires background publishing and monitoring of outbox lag.
- Introduces eventual consistency between DB commit and external consumers.

---

## ADR-002: Use Polling Publisher to Publish Outbox Events

- Status: Accepted
- Date: 2026-07-22

### Context
After adopting transactional outbox, the system needs a reliable way to publish pending outbox records.
Change Data Capture (CDC) is an option but adds extra infrastructure and operational overhead for this learning project stage.

### Decision
Use a **Polling Publisher** in `order-service`:
- Run a scheduled task at a fixed interval.
- Fetch a bounded batch of unpublished outbox rows (`findTop100ByPublishedFalseOrderByCreatedAt`).
- Publish each event to the downstream channel.
- Mark events as `published = true` after successful publish.

### Consequences
Positive:
- Simple to implement with current Spring Boot stack.
- Easy to reason about and debug in local/dev environments.
- Enables controlled throughput by batch size and polling interval.

Trade-offs:
- Adds publication latency (depends on polling interval).
- Requires careful retry/error handling to avoid duplicate publishes.
- Can be less efficient than CDC at high scale.

### Notes
- Delivery semantics are typically **at-least-once**; consumers should be idempotent.
- For higher scale or stricter operational goals, consider migrating to CDC (for example, Debezium) later.
