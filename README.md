# Coffee Delivery

A simple CRUD-based online store originally planned as a coffee/chocolate shop, now featuring product management, moderation, and basic e-commerce functionality.

## Features

- Product CRUD  
- Moderation for new products and updates  
- Comments/Reviews for products  
- Shopping cart  
- Search & filtering (Specification API)  

## Tech Stack

- Spring Boot (Web, Security, Data JPA/JDBC, OAuth2 Resource Server)  
- PostgreSQL + Flyway  
- Redis  
- JWT (jjwt)  
- Lombok  
- Springdoc OpenAPI  

## Run

```bash
git clone https://github.com/defix-dev/pet-project-coffee-delivery.git
cd pet-project-coffee-delivery
mvn clean package
````

Start with JAR:

```bash
java -jar target/coffee-delivery.jar
```

Or run via Docker (from `cd_docker`):

```bash
docker compose up
```

App runs on: **[http://localhost:8080](http://localhost:8080)**

## Testing

To run the tests, use the following command:

```bash
mvn clean test
```

This will execute all the tests in the project.

## License

No license.
