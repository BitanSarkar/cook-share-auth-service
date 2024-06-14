# Passwordless Auth Service

## Description
The Passwordless Auth Service is a Spring Boot application that facilitates OTP-based login using the Multi-Factor Authentication (MFA) feature of Amazon Cognito. This service provides a secure and seamless authentication process without requiring traditional passwords. It stores basic user details in MongoDB and includes API endpoints for registration, login, refreshing tokens, and logout.

## Features
- **Passwordless Authentication:** Users can log in using an OTP, enhancing security and user convenience.
- **Integration with Amazon Cognito:** Leverages Cognito's MFA feature for managing OTPs and user sessions.
- **MongoDB Integration:** Stores user information securely in MongoDB.
- **Dockerization:** The application is containerized with Docker, facilitating easy deployment and scalability.
- **API Endpoints:** Includes endpoints for registration, login, token refresh, and logout.

## Prerequisites
- Java JDK 17
- MongoDB (latest version recommended)
- AWS account with Amazon Cognito configured
- Maven 3.4+
- Docker (latest version recommended)

## Installation
1. **Clone the repository:**
    Clone the project to your local machine using the following command:
    ```bash
    git clone https://github.com/BitanSarkar/passwordless-auth-service.git
   ```
2. **Navigate to the project directory:**
    ```bash
   cd passwordless-auth-service
    ```
3. **Install dependencies:**
    ```bash
   mvn clean install
    ```

## Configuration
Configure the application and external services as follows:
- **MongoDB:** Ensure MongoDB is running and accessible. Configure the connection details in `application.properties`.
- **Amazon Cognito:** Set up a user pool and app client in Amazon Cognito. Include the necessary configurations in `application.properties`.
- **Environment Variables:** Set environment variables for sensitive information such as AWS access keys and MongoDB credentials.

## Running the Application
To run the application using Maven:


## Usage
The application supports the following endpoints:
- **Register:** `POST /register` — Register a new user.
- **Login:** `POST /login` — Login using an OTP sent to your registered email.
- **Refresh Token:** `POST /refresh-token` — Refresh your authentication token.
- **Logout:** `POST /logout` — Log out the current user.

## Running the Application
To run the application using Maven in local:
```bash
mvn clean install
java -jar /target/passwordless-auth-service-0.0.1.jar --spring.profiles.active=local --MONGODB_URL="your-mongo-url" --COGNITO_CLIENT_ID="your-cognito-client-id" --COGNITO_CLIENT_SECRET="your-cognito-client-secret" --COGNITO_GLOBAL_PASSWORD="self-generated-global-password"
```

To build and run the application using Docker:
1. **Build the Docker image:**
```bash
docker build -t passwordless-auth-service .
```
2. **Run the Docker container:**
```bash
docker run -p 8080:8080 \
  -e MONGODB_URL="your-mongo-url" \
  -e COGNITO_CLIENT_ID="your-cognito-client-id" \
  -e COGNITO_CLIENT_SECRET="your-cognito-client-secret" \
  -e COGNITO_GLOBAL_PASSWORD="self-generated-global-password" \
  passwordless-auth-service

```

## Contributing
Contributions are welcome! Please fork the repository and submit pull requests with any new features or fixes. Report any issues through the repository's issue tracker.

## Contact
For further questions or collaborations, feel free to contact me at [bitansarkar12345@gmail.com](mailto:bitansarkar12345@gmail).

## Acknowledgements
Thanks to all contributors and testers who helped in refining this authentication service.

