# Getting Started

### How to use this demo
This is a self-contained demo application showing the creation and validation of JWT signed tokens.  Upon startup the application will generate four sets of RSA key pairs and make available the public keys as a JWKSet through the /jwkkeystore/{id} URL where {id} represents the JWS signer id.

The URL /app02/{name} does not have any security and can be called from a browser.  When invoking this URL it will in turn call the /mysecureapi2/{name} URL which is secured with a JWT Bearer token via a Spring Filter.

The controller for /app02 will generate a JWS token using the RSA KeyPair for application-02.  It will populate the JKU header with the URL for the JWK Key Set.

com/telus/apip/jwtref/app02/App02Controller - app called by the user
com/telus/apip/jwtref/mysecureapi/MySecureApiController2.java - app called by App02Controller and secured by JWT Bearer token
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
* [RFC7519 JSON Web Token (JWT)](https://tools.ietf.org/html/rfc7519)
* [RFC7515 JSON Web Signature (JWS)](https://tools.ietf.org/html/rfc7515)
* [RFC7516 JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516)
* [RFC7517 JSON Web Key (JWK)](https://tools.ietf.org/html/rfc7517)
* [RFC7518 JSON Web Algorithms (JWA)](https://tools.ietf.org/html/rfc7518)
* [Java JWT: JSON Web Token for Java and Android](https://github.com/jwtk/jjwt)
* [REST Security with JWT using Java and Spring Security](https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java)
* [RESTFul Service with Spring Boot and Filter Based Security](https://www.codeproject.com/Articles/1267678/RESTFul-Service-with-Spring-Boot-and-Filter-Based)
