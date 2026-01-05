# Promotions API

REST API for querying applicable prices for products.

## Requirements

- Java 21
- Gradle 9.x

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd promotions-api
```

2. Build the project:
```bash
./gradlew build
```

3. Verify installation by running tests:
```bash
./gradlew test
```

## Running the Application

### Development

```bash
./gradlew bootRun
```

The application starts on `http://localhost:8080` with default API keys:
- `default-dev-key-1`
- `default-dev-key-2`

### Production

Set environment variables for API keys before starting:

```bash
export API_KEY_1=your-secure-api-key-1
export API_KEY_2=your-secure-api-key-2
./gradlew bootRun
```

Or using Java directly:

```bash
./gradlew build
java -jar build/libs/api-0.0.1-SNAPSHOT.jar
```

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

OpenAPI spec at: `http://localhost:8080/v3/api-docs`

## API Usage

### Find Applicable Price

```bash
curl -H "X-API-Key: default-dev-key-1" \
  "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Response:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

## Running Tests

### Unit and Integration Tests

```bash
./gradlew test
```

### Postman Tests

The `postman/` directory contains a Postman collection with integration tests.

**Using Postman GUI:**

1. Open Postman
2. Import the collection: `File > Import > postman/Promotions_API.postman_collection.json`
3. Start the application: `./gradlew bootRun`
4. Click "Run Collection" to execute all tests

**Using Newman (CLI):**

1. Install Newman:
```bash
npm install -g newman
```

2. Start the application:
```bash
./gradlew bootRun
```

3. Run the tests:
```bash
newman run postman/Promotions_API.postman_collection.json
```

## Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `API_KEY_1` | First API key for authentication | `default-dev-key-1` |
| `API_KEY_2` | Second API key (for rotation) | `default-dev-key-2` |

## Database

- **Development/Test**: H2 in-memory database
- **Migrations**: Flyway (located in `src/main/resources/db/migrations`)
