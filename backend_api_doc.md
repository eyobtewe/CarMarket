# 🚗 CarApp API Documentation

A comprehensive guide for frontend developers to interact with the CarApp API.

---

## 📋 Table of Contents

- [Base URL & Authentication](#-base-url--authentication)
- [Auth Endpoints](#-auth-endpoints)
- [Car Endpoints](#-car-endpoints)
- [Favorites Endpoints](#-favorites-endpoints)
- [AI Chat Endpoint](#-ai-chat-endpoint)

---

## 🌐 Base URL & Authentication

### Base URL

All API endpoints are prefixed with:

```
http://localhost:3000
```

### Authentication

Many endpoints are protected and require a JSON Web Token (JWT) for access. To authenticate:

1. Register or log in to receive a token
2. Include the token in the `Authorization` header for all protected requests

**Header Format:**

```
Authorization: Bearer <your_jwt_here>
```

---

## 🔐 Auth Endpoints

Handles user registration, login, and profile management.

### Register User

Creates a new user account.

| Property     | Value                |
| ------------ | -------------------- |
| **Method**   | `POST`               |
| **Endpoint** | `/api/auth/register` |
| **Access**   | Public               |

**Request Body:**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

---

### Login User

Authenticates a user and returns a JWT.

| Property     | Value             |
| ------------ | ----------------- |
| **Method**   | `POST`            |
| **Endpoint** | `/api/auth/login` |
| **Access**   | Public            |

**Request Body:**

```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

---

### Get User Profile

Retrieves the profile of the currently authenticated user.

| Property     | Value               |
| ------------ | ------------------- |
| **Method**   | `GET`               |
| **Endpoint** | `/api/auth/profile` |
| **Access**   | 🔒 Protected        |

---

## 🚙 Car Endpoints

Handles browsing, creating, and managing car listings.

### Get All Cars

Retrieves a paginated list of all cars with optional filtering and sorting.

| Property     | Value       |
| ------------ | ----------- |
| **Method**   | `GET`       |
| **Endpoint** | `/api/cars` |
| **Access**   | Public      |

**Query Parameters:**

| Parameter   | Type   | Description                                |
| ----------- | ------ | ------------------------------------------ |
| `page`      | number | The page number for pagination             |
| `limit`     | number | The number of items per page               |
| `make`      | string | Filter by car make (e.g., "Toyota")        |
| `model`     | string | Filter by car model (e.g., "Camry")        |
| `minPrice`  | number | Filter by minimum price                    |
| `maxPrice`  | number | Filter by maximum price                    |
| `year`      | number | Filter by a specific year                  |
| `bodyType`  | string | Filter by body type (e.g., "sedan", "suv") |
| `sortBy`    | string | Field to sort by (price, year, mileage)    |
| `sortOrder` | string | The sorting order (asc or desc)            |

---

### Search Cars

Performs a full-text search on car make, model, and description.

| Property     | Value              |
| ------------ | ------------------ |
| **Method**   | `GET`              |
| **Endpoint** | `/api/cars/search` |
| **Access**   | Public             |

**Query Parameters:**

| Parameter | Type   | Required | Description     |
| --------- | ------ | -------- | --------------- |
| `q`       | string | ✅ Yes   | The search term |

---

### Get Single Car

Retrieves details for a single car, including owner information.

| Property     | Value           |
| ------------ | --------------- |
| **Method**   | `GET`           |
| **Endpoint** | `/api/cars/:id` |
| **Access**   | Public          |

---

### Create Car

Creates a new car listing.

| Property     | Value        |
| ------------ | ------------ |
| **Method**   | `POST`       |
| **Endpoint** | `/api/cars`  |
| **Access**   | 🔒 Protected |

**Request Body:**

```json
{
  "make": "Ford",
  "model": "Mustang",
  "year": 2023,
  "price": 45000.0,
  "bodyType": "coupe",
  "mileage": 1500,
  "color": "Red",
  "description": "A beautiful sports car.",
  "features": ["Leather Seats", "Turbo Engine"],
  "images": ["url_to_image1.jpg"]
}
```

---

### Update Car

Updates an existing car listing. Only the owner can update.

| Property     | Value                     |
| ------------ | ------------------------- |
| **Method**   | `PUT`                     |
| **Endpoint** | `/api/cars/:id`           |
| **Access**   | 🔒 Protected (Owner only) |

---

### Delete Car

Deletes a car listing. Only the owner can delete.

| Property     | Value                     |
| ------------ | ------------------------- |
| **Method**   | `DELETE`                  |
| **Endpoint** | `/api/cars/:id`           |
| **Access**   | 🔒 Protected (Owner only) |

---

## ⭐ Favorites Endpoints

Handles a user's favorited cars. All endpoints are protected.

### Get User Favorites

Retrieves a list of the authenticated user's favorited cars.

| Property     | Value            |
| ------------ | ---------------- |
| **Method**   | `GET`            |
| **Endpoint** | `/api/favorites` |
| **Access**   | 🔒 Protected     |

---

### Add Car to Favorites

Adds a car to the user's favorites list.

| Property     | Value            |
| ------------ | ---------------- |
| **Method**   | `POST`           |
| **Endpoint** | `/api/favorites` |
| **Access**   | 🔒 Protected     |

**Request Body:**

```json
{
  "carId": "car_uuid_to_favorite"
}
```

---

### Remove Car from Favorites

Removes a car from the user's favorites list.

| Property     | Value                   |
| ------------ | ----------------------- |
| **Method**   | `DELETE`                |
| **Endpoint** | `/api/favorites/:carId` |
| **Access**   | 🔒 Protected            |

---

## 🤖 AI Chat Endpoint

Provides an AI-powered assistant for car recommendations.

### Chat with AI Assistant

Sends a message to the AI assistant for recommendations.

| Property     | Value          |
| ------------ | -------------- |
| **Method**   | `POST`         |
| **Endpoint** | `/api/ai/chat` |
| **Access**   | 🔒 Protected   |

**Request Body:**

```json
{
  "message": "I'm looking for a reliable SUV under $30,000."
}
```

**Example Response:**

```json
{
  "message": "Based on your request...",
  "recommendedCars": [
    {
      "id": "car_uuid_1",
      "make": "Toyota",
      "model": "RAV4"
    }
  ]
}
```

---

## 📝 Notes

- All protected endpoints require a valid JWT token in the Authorization header
- Ensure proper error handling for authentication failures
- Use appropriate HTTP status codes when handling responses

---

Made with ❤️ for CarApp
