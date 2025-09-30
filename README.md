PII API Spring Boot Project
```
This project is a Spring Boot application for managing PII data with H2 database.
```

Prerequisites
```
- Java 17
- Maven 3.x
- Git
```

Clone the repo and run the app:
```
git clone https://github.com/yourusername/pii-api.git
cd pii-api
mvn clean install
mvn spring-boot:run
```

App runs starts on:
```
http://localhost:8080
```
H2 Database URL:
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (leave blank)
```

Sample API calls:
```
Create user:
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Bob",
        "ssn": "987654321",
        "dob": "1990-03-10"
      }'

Get user by id:
curl -X GET http://localhost:8080/api/users/1

Admin Get user by id:
curl -X GET http://localhost:8080/api/admin/users/1 \
  -H "X-Auth-Token: admin-secret-token"
```


