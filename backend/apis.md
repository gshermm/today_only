# Today Only! API Documentation

## Overview

REST API for the Today Only! UIUC campus food deals mobile application. All responses are in JSON format.

## Base URL

`averyqian.pythonanywhere.com`

## Endpoints

### 1. List Deals

Get a list of food deals with filtering options.

**Endpoint:** `GET /api/deals`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-------------|---------|----------|-----------------------------------------------------------------------------------------------|
| sort_by | string | No | Sort deals by specific criteria. Options: `rating`, `distance`. Default: `rating` |
| latitude | float | Conditional¹ | Latitude of user's location for distance sorting. Required if `sort_by=distance` |
| longitude | float | Conditional¹ | Longitude of user's location for distance sorting. Required if `sort_by=distance` |
| date | string | No | Filter deals by date. Format: `YYYY-MM-DD`. Default: current date |
| view_type | string | No | View deals by time period. Options: `day`, `week`, `month`. Default: `day` |
| status | string | No | Filter by deal status. Options: `active`, `upcoming`. Default: `active` |

> **¹ Conditional Requirement:** `latitude` and `longitude` are required when `sort_by` is set to `distance`.

**Success Response (200):**

```json
{
	"deals": [
		{
			"id": "123",
			"restaurant_name": "Burgers & Fries",
			"title": "50% Off Burgers",
			"description": "Half price on all burgers",
			"start_time": "11:00",
			"end_time": "14:00",
			"date": "2024-01-20",
			"student_id_required": true,
			"rating": 4.5,
			"total_ratings": 128
		}
	]
}
```

````

### 2. Get Deal Detail

Get detailed information about a specific deal.

**Endpoint:** `GET /api/deals/{deal_id}`

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|--------|----------|----------------------------------|
| deal_id | string | Yes | Unique identifier of the deal |

**Success Response (200):**

```json
{
	"id": "123",
	"restaurant_name": "Burgers & Fries",
	"title": "50% Off Burgers",
	"description": "Half price on all burgers",
	"start_time": "11:00",
	"end_time": "14:00",
	"date": "2024-01-20",
	"student_id_required": true,
	"rating": 4.5,
	"total_ratings": 128,
	"requirements": "Must show valid student ID",
	"location": "123 Green Street, Champaign"
}
```

### 3. Submit Deal

Submit a new deal for review.

**Endpoint:** `POST /api/deals`

**Request Body Parameters:**
| Field | Type | Required | Description |
|----------------------|---------|----------|-----------------------------------------------------------|
| restaurant_name | string | Yes | Name of the restaurant offering the deal |
| title | string | Yes | Short, descriptive title of the deal (max 100 chars) |
| description | string | Yes | Detailed description of the deal (max 500 chars) |
| date | string | Yes | Deal date. Format: `YYYY-MM-DD` |
| start_time | string | Yes | Deal start time. Format: `HH:MM` (24-hour) |
| end_time | string | Yes | Deal end time. Format: `HH:MM` (24-hour) |
| student_id_required | boolean | Yes | Whether student ID is required to claim the deal |
| requirements | string | No | Additional requirements or conditions (max 200 chars) |
| contact_email | string | Yes | Contact email for deal verification |

**Success Response (201):**

```json
{
	"id": "123",
	"status": "pending",
	"message": "Deal submitted successfully and pending review"
}
```

### 4. Rate Deal

Submit a rating for a deal.

**Endpoint:** `POST /api/deals/{deal_id}/rate`

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|--------|----------|-------------------------------|
| deal_id | string | Yes | Unique identifier of the deal |

**Request Body Parameters:**
| Field | Type | Required | Description |
|--------|---------|----------|------------------------------------|
| rating | integer | Yes | Rating value from 1 to 5 |

**Success Response (200):**

```json
{
	"success": true,
	"new_average_rating": 4.5,
	"total_ratings": 129
}
```

## Error Responses

All endpoints may return the following error responses:

### Bad Request (400)

Returned when request parameters are invalid.

```json
{
	"error": "validation_error",
	"message": "Invalid date format. Use YYYY-MM-DD"
}
```

### Not Found (404)

Returned when requested resource doesn't exist.

```json
{
	"error": "not_found",
	"message": "Deal not found"
}
```

### Server Error (500)

Returned when server encounters an error.

```json
{
	"error": "server_error",
	"message": "An unexpected error occurred"
}
```
````
