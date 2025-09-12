By choosing **Option 1**, the API design prioritizes simplicity and atomic updates.  
This approach is practical for a system where the "go online" action is infrequent and must be handled with a single, consistent request.

# Domains and APIs for a Ride-Matching Service

Here are the domains and their corresponding RESTful APIs, updated to reflect the combined PATCH request for a driver's status and location.

## Domains
- **Rider**: Represents the user requesting a ride.
- **Driver**: Represents the person providing the ride.
- **Ride**: Represents the service event, connecting a rider and a driver.
- **Location**: A value object representing geographical coordinates (latitude and longitude), used within other domains.

---

## REST APIs

### 1. Rider Endpoints
These endpoints are for the user's application.

- **POST /rider/{riderId}/rides**
    - *Action*: Request a new ride.
    - *Request Body*: Includes the rider's ID and pickup/drop-off locations.

- **GET /rider/{rideId}/rides/{rideId}**
    - *Action*: Get the status and details of a specific ride.

- **DELETE /rides/{rideId}**
    - *Action*: Cancel a ride.

---

### 2. Driver Endpoints
These endpoints are for the driver's application.

- **GET /drivers**
    - *Action*: Find available drivers near a specific location.
    - *Query Parameters*: `latitude` and `longitude`.
  
- **POST /drivers/{driverId}/rides/{rideId}/actions/complete**
    - *Action*: Update a driver's availability and initial location.  
      This single request handles the transition to being "available" and provides the necessary location data.
    - *Request Body*: A JSON object with both the status and location.
