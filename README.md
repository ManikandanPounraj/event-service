# EventService

**Port**: `8082`

## 📘 Description
Manages events including creation, search, seat availability, and booking updates.

## 📡 API Endpoints
- `POST /api/events - Create event`
- `GET /api/events - List all events or filter by location/date`
- `GET /api/events/{id} - Get event by ID`
- `PUT /api/events/{id} - Update event`
- `DELETE /api/events/{id} - Delete event`
- `GET /api/events/{id}/availability - Check available seats`
- `PUT /api/events/{id}/bookedSeats - Update booked seat count`

## ▶️ How to Run
1. Make sure you have Java 17+ and Maven installed.
2. Navigate to this microservice folder.
3. Run `mvn spring-boot:run`

## 🔍 Swagger UI
- Visit: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
