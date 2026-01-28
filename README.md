# Geolocated Ads API

## Overview

This project is a **Java-based REST API** designed to return **nearby market advertisements** based on the user's geographic location. The API combines **geolocation via Google Geocoding API** with **intelligent flyer processing using the ChatGPT API**, enabling the extraction, normalization, and delivery of promotional data in a structured and searchable format.

The solution addresses the challenge of **local offer discovery**, transforming traditional market flyers (usually images) into actionable, geospatially indexed data.

---

## Key Features

* 🔍 **Search ads by proximity** (latitude/longitude or ZIP code)
* 🌍 **Integration with Google Geocoding API** to convert addresses/ZIP codes into coordinates
* 🧠 **Integration with ChatGPT API** for data extraction from market flyers (JPEG images)
* 📄 **Normalization and persistence of ads and markets**
* 🧪 **Unit and service tests using Spock Framework**
* 📦 **Pagination and advanced search filters**

---

## Architecture

The application follows a **layered architecture**, with clear separation of responsibilities:

```
Controller → Service → Domain → Repository → Database
                ↓
        External Integrations
        (Google Geocode / ChatGPT)
```

### Layers

* **Controller**: Exposes REST endpoints and performs initial request validation
* **Service**: Contains business rules, distance calculations, and integration orchestration
* **Domain**: Business entities such as `Ad`, `Market`, and `Location`
* **Repository**: Data access using JPA/Hibernate
* **Integration**: External clients (Google Geocoding API and ChatGPT API)

---

## Ad Search Flow

1. The user sends a request providing:

    * ZIP code **or** latitude/longitude
2. If a ZIP code is provided:

    * The API calls the **Google Geocoding API** to retrieve latitude and longitude
3. The API validates the search parameters
4. Ads are queried from the database considering:

    * Geographic distance
    * Applied filters
5. Results are returned in a **paginated** format

---

## Flyer Processing with ChatGPT

The API uses ChatGPT to:

* Interpret market flyer images
* Extract relevant information such as:

    * Product name
    * Price
    * Market
    * Validity period
* Generate a standardized JSON structure

Extracted data can be **manually validated** before persistence, ensuring higher data reliability.

---

## Technologies Used

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **Spock Framework** (testing)
* **Google Geocoding API**
* **OpenAI / ChatGPT API**
* **Relational SQL Database**
* **AWS EC2** for deployment

---

## Testing

The project follows testing best practices:

* Unit and service tests with **Spock**
* Mocking of external integrations
* Validation of critical business rules (geolocation, filters, and searches)

---

## Environment Configuration

### Environment Variables

The following variables must be configured:

* `GOOGLE_GEOCODE_API_KEY`
* `OPENAI_API_KEY`
* Database connection variables

---

## Project Goal

This project aims to provide a scalable foundation for:

* Local offer discovery
* Intelligent flyer digitization
* Integration between unstructured data (images) and geospatial search systems

---

## Future Improvements

* Administrative interface for manual validation of extracted ads
* Geocoding result caching
* Ad ranking by relevance
* Support for multiple flyer sources

---

## Author

**Petrus Fernandes**

Java Developer | REST APIs | Integrations | AWS
