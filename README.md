# PickMeUp Taxi Service – System Design

## Overview

PickMeUp is a taxi hailing web application. Its purpose is to connect riders who need a ride with drivers who can provide one. The system is designed using Clean Architecture principles to keep everything organized and easy to manage.

## Core Components

- **Rider**: The customer who requests a ride.
- **Driver**: The person who accepts and completes the ride.
- **Ride**: The core connection between a rider and a driver, tracking the trip's details and status.

## Architecture

The system follows a layered approach based on Clean Architecture, which separates the core business rules from external details like the database or web APIs.

- **Controllers**: These expose REST APIs that allow riders and drivers to interact with the system.
- **Business Services**: These handle the main logic, such as coordinating ride requests, matching riders to drivers, and updating ride statuses.
- **Domain Models**: These are the core data structures for key entities like Driver, Rider, Ride, and Location. They don't contain any business logic.
- **Repositories**: These are interfaces that define how data is accessed and stored. In this version, they use an in-memory solution.
- **Transaction Management**: A custom system to ensure that a group of updates (like creating a ride and changing a driver's status) either all succeed or all fail together.

## Domain Models

- **Driver**: A driver has an `id`, `name`, `currentLocation`, and a `status` indicating if they are available. They can update their location and toggle their availability.
- **Rider**: A rider has an `id` and a `name`.
- **Ride**: A ride includes an `id`, the `rider`, the assigned `driver`, `pickupLocation`, `dropoffLocation`, and a `status`. The status progresses through a lifecycle: `REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED`.
- **Location**: An immutable record with latitude and longitude coordinates.

## Core Features

- **Rider Features**: Riders can request a ride and track its progress in real-time.
- **Driver Features**: Drivers can go online/offline, accept or complete rides, and send real-time location updates.
- **System Features**: The system matches riders to the nearest available drivers and updates driver availability automatically upon ride completion.

## Business Logic

### Ride Request Flow

1. A rider sends a ride request with a pickup and drop-off location.
2. The system finds the nearest available driver using a distance calculation.
3. The chosen driver is assigned to the ride and marked as unavailable. A new ride is created.
4. The system returns the ride details to the rider.

### Ride Completion Flow

1. The driver marks the ride as complete.
2. The ride's status is updated to `COMPLETED`.
3. The driver's location is updated, and their availability is set back to true.

## Persistence

- **DriverRepository, RiderRepository, RideRepository**: The interface for managing driver data.
- **InMemoryDriverRepository, InMemoryRiderRepository, InMemoryRideRepository**: A temporary implementation using a `ConcurrentHashMap` to store data with transactional support.
- **TransactionalMapRepository**: Ensures thread-safe ACID-like operations on the in-memory data.
- **InMemoryTransactionManager**: Handles the commit and rollback of in-memory operations.

## Transaction Management

The system uses custom transaction management to ensure ride creation and driver updates are atomic. This is achieved using `@Transactional` annotations and a custom transaction manager that emulates commit and rollback over the in-memory repositories. Thread-local storage ensures that multiple requests don't interfere with each other.

## Testing

- **SpringBootTests**: Ensures that repositories always behave consistently under different scenarios.
- **Unit Tests**: Business services are covered with unit tests to verify that core ride request and ride completion logic works as expected.

## High-Level API Endpoints

### Rider APIs

- `POST /rider/{riderId}/ride` – Requests a new ride.

### Driver APIs

- `POST /drivers/{driverId}/rides/{rideId}/actions/complete` – Completes an assigned ride.

## Next Steps / Improvements

- Replace the in-memory storage with a real database (e.g., PostgreSQL, MySQL).
- Add authentication and JWT for secure rider and driver logins.
- Integrate a payment system for ride completion.
- Enhance the ride matching algorithm to include factors like load balancing and estimated time of arrival (ETA).
- Add more unit and integration tests to ensure the entire system is robust and reliable.
