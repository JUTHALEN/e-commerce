# E-Commerce In-Memory System

## Description

This project is an e-commerce backend system designed for managing carts and products without the need for persistent
storage. The system supports operations like creating a cart, adding products, retrieving carts, and deleting carts. All
data is volatile and stored in memory with a Time To Live (TTL) of 10 minutes, ensuring data does not persist beyond the
duration of the application run.

## How to Run

To get the application running, you will need Java and Maven installed on your machine. Once installed, you can start
the application using the following command:

`mvn spring-boot:run`

## Profiles

The application supports one profiles:
- **dev**: This profile is specifically used for development and testing purposes. When running with this profile, the system preloads a set of sample product data into the application to facilitate rapid development and testing. This allows developers and testers to interact with the API without needing to manually populate the database each time the application is started.

`mvn spring-boot:run -Dspring-boot.run.profiles=dev`

## Dependencies

The project uses the following dependencies:

- Spring Boot
- Spring Web
- Lombok
- JUnit 5
- Mockito
- Spring Boot Test

## Endpoints

The application defines several RESTful endpoints for managing carts and products. Here is a comprehensive list of
available endpoints:

| Method | Endpoint                              | Description                                         | Notes                                            |
|--------|---------------------------------------|-----------------------------------------------------|--------------------------------------------------|
| POST   | `/api/product`                        | Creates a new product.                              | Returns the details of the new product.          |
| GET    | `/api/product/{id}`                   | Retrieves a product by its ID.                      |                                                  |
| GET    | `/api/products`                       | Lists all products.                                 |                                                  |
| PUT    | `/api/product/{id}`                   | Updates the specified product.                      |                                                  |
| DELETE | `/api/product/{id}`                   | Deletes the specified product.                      |                                                  |
| POST   | `/api/cart`                           | Creates a new cart with potential initial products. | Returns the details of the new cart.             |
| GET    | `/api/cart/{id}`                      | Retrieves a cart by its ID.                         |                                                  |
| POST   | `/api/cart/{id}/products`             | Adds a product to the specified cart.               | Expected to handle the addition logic.           |
| DELETE | `/api/cart/{id}/products/{productId}` | Removes a specified product from the cart.          |                                                  |
| DELETE | `/api/cart/{id}`                      | Deletes the specified cart.                         |                                                  |


## Scheduled Tasks

- **Cleanup Expired Carts:** The system automatically removes carts that have been in the system for longer than 10
  minutes. This cleanup task runs every 10 minutes.

## Testing

The application includes unit tests for services and repositories. To run these tests, use:

`mvn test`
