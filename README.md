# java-explore-with-me
Template repository for ExploreWithMe project.


This is a capstone project for Yandex Practicum Java Developer course.
Project task was to create backend part of the event sharing service.
It consists of 2 modules that should be put behind the gateway connected via VPN.
Only calls that have passed the authentication check will reach the service.

### Business logic

**Roles**: admins, users and general public.

*Admins* can create users, change status of the events, change status of the comments, create event compilations and pin them on the main page.
Authenticated *users* can create events, post comments, send and change status of the requests for participation in the events.
*General public* can get lists of the events and get info about particular event.

**Modules are**:

- ***Explore with me service (EWM)*** holds all controllers and business logic;
- ***Statistics collects*** statistics on requests to 2 endpoint in EWS (Public GET requests for event by id and GET request for events by filters), this module also provides statistics via its controller by filers. This service can accomodate other applications and other APIs if needed.

Each module has separate database, PostgresSQL.

Modules are prepared for containers. Each module includes a Docker file.
To run modules in Docker containers from the project directory perform:

`docker build /ewm/Dockerfile`
`docker build /statistics/Dockerfile`

This will create container image for each of the services and one container image for database (total 3 images).

**To see the list of images:**

`docker images`

**To run each container:**

`docker run IMAGE`

Modules can be run separately as applications by running *EWmApplication* and *StatisctisApplication* class in modules respectively.  
Can be run together in containers. From the project directory:

`docker compose up`

This should create 3 images (if not created before) and run 4 containers (2 services and 2 databases).

EWM service will be available on port 8080.
Statistics will be available on port 9090.

Swagger API description will be available when services are running:

[EWS main service]( http://localhost:8080/swagger-ui/index.html)

[Statistics](http://localhost:8080/swagger-ui/index.html)

https://github.com/lilyerma/java-explore-with-me/pull/1
