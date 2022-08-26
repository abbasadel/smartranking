## Scope

Design and implement a system that ingests and processes this file, and presents the results
through a smart ranking index REST API that help retailers make sense of this data as follows:

- a time series containing the individual ranks for an ASIN, for a certain keyword
- a time series containing the aggregated ranks for all ASINs for a certain keyword
- a time series containing the aggregated ranks of all keywords for a certain ASIN

## Assumptions

- The REST APIs will provide the response in JSON format
- The format will a JSON array of 2 keys; `timestamp` and `value`
    - `timestamp`: will be in `YYYY-MM-DDThh:mi:ssZ`, eg: `2021-11-02T01:07:14Z`
    - `value`: is double
- The returned JSON array is not guaranteed to be sorted, it is the responsibility of the FE framework to sort the data
  when needed. This limitation comes due to the parallel processing of the provided data.
- Since the aggregation method is not specified, the application is exposing 3 methods to aggregate the data.

## Tech Stack

- Spring Boot 2.7
- Spring WebFlux
- AWS SDK v2
- Java 17

## Getting Started

## Architecture

### Internal Architecture

The App is designed using Layered Architecture

- API/Controller layer: Exposing JSON/Rest API to the app users/clients
- Business/Service Layer: Where business logic and computation take place
- Provider Layer: A Facade layer between the Business Layer and Client Layer to handle data conversion, aggregation and
  validation.
- Client Layer: Handling all the communication and data manipulation with the external API
- Aggregator Strategy: A set of classes following the Strategy Pattern to provide several aggregation methods to the
  data. More aggregation strategy can be added as needed by implementing the `AggregatorStrategy` class

### Architecture Decisions

#### Loading/Querying the data file

| Option                         | Description                                                                                | Pros                                                                                                                                                           | Cons                                                                                                                                                              | Comment |
|--------------------------------|--------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| 1. Query the data file online  | On every request, download the file. Query the file then delete it.                        | - Data is up to data <br />                                                                                                                                    | - Slow response. <br /> - More memory usage                                                                                                                       |         |
| 2. Query the data file offline | Download the data file once or frequently on the application side and query it from there. | - More efficient then option 1. <br />                                                                                                                         | - Stall data. <br /> - Download time may affect the application startup. <br /> - Thread/Synchronization problems                                                 |         |
| 3. Stream the data file        | Reading and processing the data file in chunks                                             | - Less memory usage on the application side <br />  - Scalable when the data grows <br /> - Data is always up to date <br /> - Non-blocking on the client side | - This is a new feature of S3, never battle tested. <br /> - Needs more time to fine tuning                                                                       |         |
| 4. Use Timeseries Database     | Load the data file into a TS database                                                      | - Easier to query \and manipulate<br/> - Scalable when the data grows. <br /> - Less processing and memory usage on the application side                       | - Loading and syncing the data. <br /> - Operating and maintaining the TSDB. <br /> - High learning curve to use and understand TSDB for the code challenge scope |         |

##### Decision

I will go with this option 2 since it more efficient than option 1 and fits the scope and time limit of the code
challenge
The data will be downloaded to a temp storage during the application startup.

## Getting Started

### Prerequisites

To build and rung the containerized application, you need the following

- Java JDK 17+

  or

- Docker (Multistage build, no need to install Java JDK or Gradle)
- docker-compose

### Building and Running the App

You can build the run the app using Java JDK 17 and Gradle. You will need to provide AWS S3 keys as parameters

```bash
./gradlew clean build
java -jar build/libs/smartranking-0.0.1-SNAPSHOT.jar --providers.sellics.s3.accessKeyId=<s3 key> --providers.sellics.s3.secretAccessKey=<s3 secret>

```

or you can run the app using Docker, just type the following commands to start the containerized application

```bash
docker-compose build
export S3KEY=<s3 key>
export S3SECRET=<s3 secret>
docker-compose up
```

Now you can go to:

- GET http://localhost:8080/v1/raking/asin/{asin}
- GET http://localhost:8080/v1/raking/asin/{asin}?keyword={keyword}
- GET http://localhost:8080/v1/raking/asin/{asin}?method={MEAN|MEDIAN|SUM}
- GET http://localhost:8080/v1/raking/keyword/{keyword}

### Output

The output is JSON format similar to the following:

```json
 [
  {
    "timestamp": "2021-11-10T01:08:38Z",
    "value": 2.0
  },
  {
    "timestamp": "2021-10-25T01:06:37Z",
    "value": 183.0
  }
]
```

### Open API Docs:

http://localhost:8080/webjars/swagger-ui/index.html#

### Nice to have

These are some ideas to improve the code base and possible future enhancements

- Using Redis for caching
- Use Spring Security and Actuator
- Use Micrometer to record Hits/Misses of the Cache and other interesting metrics
- Integrate with CI service (GitHub Action or CircleCI)
- Integrate with [Snyk](http://snyk.io)
- Use sonarqube and jacoco
- Introduce API versioning schema