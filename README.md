# Code Sharing Platform

<!-- TOC -->
* [Code Sharing Platform](#code-sharing-platform)
  * [Description](#description)
  * [Endpoints](#endpoints)
    * [API](#api)
    * [Web](#web)
  * [Build and run](#build-and-run)
<!-- TOC -->

## Description

Code sharing platform is a simple web application for hosting and sharing your code snippets. With this app, you can post
public code, or you can hide it from anyone else with some restrictions for time and view limits.

It is written in Java 17. It uses [Spring Boot](https://spring.io/projects/spring-boot#overview) framework,
[Apache FreeMarker](https://freemarker.apache.org/) for generation HTML pages, and H2 as a database.

I used [Postman](https://www.postman.com/) for testing web pages and REST API endpoints.

This project is a part of [JetBrains Java Backend Developer course](https://hyperskill.org/tracks/12).

## Endpoints
### API

- `POST /api/code/new` - allow you to post new code snippet
  - required
    - `code: String` - text of your code 
  - optional
    - `time: long` - time for time restriction in seconds; after that time your code would be deleted
    - `views: long` - views for view restriction; after that count of views your code would be deleted 

Request body example:
```json
{
  "code": "public static void main(String[] args) {}",
  "time": 600,
  "views": 10
}
```
Returns your code UUID:
```json
{
    "id": "f4f5a153-ac54-497e-8c27-ae2baef978d8"
}
```

- `GET /api/code/latest` - return 10 latest uploaded code snippets:
```json
[
  {
    "code": "some of uploaded code",
    "time": 0,
    "views": 0,
    "date": "2023/03/08 10:30:27"
  },
  {
    "code": "string",
    "time": 0,
    "views": 0,
    "date": "2023/03/08 10:15:37"
  }
]
```

- `GET /api/code/{uuid}` - return code snippet by UUID:

```json
{
    "code": "string",
    "date": "2023/04/08 15:23:36",
    "time": 0,
    "views": 0
}
```

### Web

- `GET /code/new` - returns page with a form to upload code snippet
- `GET /code/latest` - returns page with 10 latest uploaded code snippets without restrictions
- `GET /code/{uuid}` - returns page with code snippet by applied uuid

## Build and run

- Clone repo and go to project directory

```shell
git clone https://github.com/ex-neskoro/CodeSharingPlatform.git 
```

- You can already start the app by command
```shell
./gradlew bootRun
```

- Or you can create .jar first
```shell
./gradlew bootJar
java -jar CodeSharingPlatform/build/libs/CodeSharingPlatform-0.0.1.jar
```
