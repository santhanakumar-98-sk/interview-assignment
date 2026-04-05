# Meeting Room Booking Service – Design

## 1. Data Model

### Room
- id (Long)
- name (String, unique)
- capacity (Integer)
- floor (Integer)
- amenities (List<String>)

### Booking
- id (Long)
- room (ManyToOne → Room)
- title (String)
- organizerEmail (String)
- startTime (LocalDateTime)
- endTime (LocalDateTime)
- status (Enum: CONFIRMED, CANCELLED)

### IdempotencyRecord
- id (Long)
- idempotencyKey (String)
- organizerEmail (String)
- bookingId (Long)
- status (PROCESSING, COMPLETED)
- createdAt (LocalDateTime)

---

## 2. No Overlap Enforcement

We prevent overlapping bookings using a database query:

Condition:
(startTime < requestedEnd) AND (endTime > requestedStart)

Additionally:
- Only bookings with status = CONFIRMED are considered
- CANCELLED bookings are ignored

This ensures:
- No double booking for the same room
- Cancelled bookings do not block new bookings

---

## 3. Error Handling Strategy

We use:
- Custom exceptions:
  - BadRequestException → 400
  - NotFoundException → 404
  - ConflictException → 409

- Centralized handling via @RestControllerAdvice

Response format:
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error message"
}

---

## 4. Idempotency Implementation

We use a dedicated table: idempotency_records

Key points:
- Unique constraint on (idempotencyKey, organizerEmail)
- On request:
  1. Check if key exists
  2. If COMPLETED → return existing booking
  3. If PROCESSING → reject duplicate request
  4. Else → create PROCESSING record

- After booking creation:
  - Update record to COMPLETED
  - Store bookingId

This ensures:
- No duplicate bookings
- Safe retries

---

## 5. Concurrency Handling

Handled using:

1. Database unique constraint (idempotency key)
2. Transactional boundary (@Transactional)
3. Status = PROCESSING for in-flight requests

Effect:
- Parallel requests with same key cannot create duplicates
- Only one request completes successfully

---

## 6. Utilization Calculation

### Formula:

Utilization = Total booked hours / Total business hours

### Business Hours:
- Monday to Friday
- 08:00 to 20:00

### Booking Hours:
- Only CONFIRMED bookings considered
- Overlap is calculated using:

overlap = max(0, min(end, to) - max(start, from))

### Edge Cases:
- No bookings → utilization = 0
- Partial overlaps handled correctly
- Weekends ignored

---

## Assumptions

- All times are in same timezone
- Idempotency key is unique per organizer
- Booking duration constraints enforced (15 min – 4 hrs)
