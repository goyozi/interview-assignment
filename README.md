# Interview Assignment

The project requires a JDK 11 to be available in PATH and (optionally) a Docker installation.

## Building the project

```
./gradlew build
```

## Running the application

#### H2 (in-memory) 

```
./gradlew bootRun
```

#### MariaDB (w/ docker)

```
docker-compose up
```

## API Documentation

Run the project using any of the commands above and open:

http://localhost:8080/swagger-ui.html

## Note for Windows Users

When building/running the project using Windows' command line, the Gradle commands above should
be ran without `./` in front of them.

The API documentation URL might also be different depending on Docker's configuration. In such
cases `localhost` should be replaced with host address of the Docker machine.
