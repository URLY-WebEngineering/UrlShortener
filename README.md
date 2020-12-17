# Web Engineering 2020-2021 / URL Shortener

[![Build Status](https://travis-ci.com/UNIZAR-30246-WebEngineering/UrlShortener.svg?branch=master)](https://travis-ci.com/UNIZAR-30246-WebEngineering/UrlShortener)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=URLY-WebEngineering_UrlShortener&metric=coverage)](https://sonarcloud.io/dashboard?id=URLY-WebEngineering_UrlShortener)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=URLY-WebEngineering_UrlShortener&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=URLY-WebEngineering_UrlShortener)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=URLY-WebEngineering_UrlShortener&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=URLY-WebEngineering_UrlShortener)

This is the start repository for the project developed in this course. 

The __project__ is a [Spring Boot](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that offers a minimum set of functionalities shared by all subprojects.

* __Short URL creation service__:  `POST /` creates a shortened URL from a URL in the request parameter `url`.
* __Redirection service__: `GET /{id}` redirects the request to a URL associated with the parameter `id`.
* __Database service__: Persistence and retrieval of `ShortURL` and `Click` objects.

The presentation for the project is available [here](ProjectPresentation.pdf).

## Deployment

It is necessary to export environment variable:  
`export GSB_API_KEY=askthisvaluetoowners`

The application can be run as follows:
### Linux and MacOS
#### Docker
```
$ ./deploy.sh
```
#### Graddle 
    
```
$ ./gradlew bootRun
```
### Windows

```
$ gradle.bat bootRun
```

## Usage
Gradle will compile project and then run it
Now you have a shortener service running at port 8080. 
You can test that it works as follows:

```bash
$ curl -v -d "url=http://www.unizar.es/" -X POST http://localhost:8080/link
> POST / HTTP/1.1
> User-Agent: curl/7.37.1
> Host: localhost:8080
> Accept: */*
> Content-Length: 25
> Content-Type: application/x-www-form-urlencoded
>
* upload completely sent off: 25 out of 25 bytes
< HTTP/1.1 201 Created
< Server: Apache-Coyote/1.1
< Location: http://localhost:8080/6bb9db44
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
<
* Connection #0 to host localhost left intact
{"hash":"6bb9db44","target":"http://www.unizar.es/","uri":"http://localhost:8080/6bb9db44",
"sponsor":null,"created":"2019-09-10","owner":"112b6444-0a05-4e48-a13f-27ddf23349e2","mode":307,
"safe":true,"ip":"0:0:0:0:0:0:0:1","country":null}%
```

Now, we can navigate to the shortened URL.

```bash
$ curl -v http://localhost:8080/6bb9db44
> GET /6bb9db44 HTTP/1.1
> User-Agent: curl/7.37.1
> Host: localhost:8080
> Accept: */*
>
< HTTP/1.1 307 Temporary Redirect
< Server: Apache-Coyote/1.1
< Location: http://www.unizar.es/
< Content-Length: 0
<
```