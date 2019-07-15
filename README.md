# Getting Started

### How to use this demo
This app contains a dummy API accessed from http://localhost:8080/app02/{name} which responds with a "hello {name}".

The /app02 will create a JWT token and pass it to the /mysecureapi.  The mysecureapi checks for and requires a JWT Bearer token in the authorization header.

The app will also stand up a URL to manage the public keys of the JWT signers at /jwkkeystore.

com/telus/apip/jwtref/app02/App02Controller - app called by the user
com/telus/apip/jwtref/mysecureapi/MySecureApiController.java - app called by App02Controller and secured by JWT Bearer token
com/telus/apip/jwtref/keystore/JwkKeyStoreController - Access the public keys for the JWS signers


KeySets are identified by the following App ID...
application-01
application-02
application-03
application-04

For each App ID there will be 4 versions of the KeyPairs and each one has it's won Key ID (kid)
application-01.v0, application-01.v1, application-01.v2, application-01.v3   
application-02.v0, application-02.v1, application-02.v2, application-02.v3   
application-03.v0, application-03.v1, application-03.v2, application-03.v3   
application-04.v0, application-04.v1, application-04.v2, application-04.v3   

### References
*[RFC7519 JSON Web Token (JWT)](https://tools.ietf.org/html/rfc7519)
*[RFC7515 JSON Web Signature (JWS)](https://tools.ietf.org/html/rfc7515)
*[RFC7516 JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516)
*[RFC7517 JSON Web Key (JWK)](https://tools.ietf.org/html/rfc7517)
*[RFC7518 JSON Web Algorithms (JWA)](https://tools.ietf.org/html/rfc7518)
*[Java JWT: JSON Web Token for Java and Android](https://github.com/jwtk/jjwt)
*[REST Security with JWT using Java and Spring Security](https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java)
*[RESTFul Service with Spring Boot and Filter Based Security](https://www.codeproject.com/Articles/1267678/RESTFul-Service-with-Spring-Boot-and-Filter-Based)
