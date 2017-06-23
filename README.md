# spring-session-demo
## POC for Spring Session

## Overview
This POC was done to understand and evaluate the capabilities of Spring Session in relation to solving the multi user multi tab session scenario.

* Spring Session provides an API and implementation for managing a user’s session. It abstracts the HttpSession implementation with Spring Session and thus allowing support of clustered sessions (e.g using Redis key/value store or other distributed map stores) without being tied to application container specific implementation.

In addition it provides:   
* Support for managing multiple distinct sessions within a single browser instance using session aliases. This enables support for managing different sessions in each browser tab/window.
* Support for maintaining sessionIds in Http Header to work with RESTful API
* Support for keeping the HttpSession alive when working with WebSocket messages. This works only with Spring’s Websocket support and not with JSR-356. This was not fully evaluated as part of this POC.
* An early version of Spring Session 1.0.0 was released in Nov 2014. It is still in early stage of adoption but does have good support from spring community.
* Spring Session was evaluated using JPetstore sample application in conjunction with spring namespace extensions and http conversation scope.

Following Dependency is required to enable spring session support.
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session</artifactId>
        <version>1.0.0.RELEASE</version>
    </dependency>
</dependencies>
```

Optionally you may also need other dependencies if working with Redis, Hazlecast or gemfire instances for session persistence.
```xml
<dependency>
  <groupId>org.springframework.session</groupId>
  <artifactId>spring-session-data-redis</artifactId>
  <version>1.0.0.RC1</version>
  <type>pom</type>
</dependency>

<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-redis</artifactId>
  <version>1.3.0.RELEASE</version>
</dependency>

<dependency>
  <groupId>com.orange.redis-embedded</groupId>
  <artifactId>embedded-redis</artifactId>
  <version>0.6</version>
</dependency>
```

For Hazlecast:

```xml
<dependency>
  <groupId>com.hazelcast</groupId>
  <artifactId>hazelcast-client</artifactId>
  <version>3.3.3</version>
</dependency>
```

Observations   
* At the heart of spring session is its web Filter SessionRepositoryFilter that is responsible for bridging HttpSession implementation to be backed by Spring Session.
* In order for spring session to work properly it must be ensured that this filter is initialized before any other filter. The framework however provides a utility classAbstractHttpSessionApplicationInitializer that takes care of proper ordering and registration of this filter.
* By default it creates Cookie by name SESSION to store session alias and corresponding sessionId(s) at client side that gets resolved to the server side HttpSession. It does not use JSessionId and for the prevention of XSS it marks the cookie as HttpOnly.
* Multiple sessions are supported from the same browser instance. Once a session is established with the browser, another session can be initiated by specifying a unique session alias. This session alias is recognized by passing a unique session-alias-key (“_s”) as part of http request parameter. The filter then intercepts this request and resolves it to an existing or new server side session.
* http://localhost:8080/spring-session/app/session?_s=1422468853273
* The session alias key and cookie name can be customized by extending default cookie session strategy.
* By default URLs are rewritten to include current session alias. Default session alias value is 0 or null. All urls from page to page must use jstl tag c:url for automatic inclusion of session alias.
<c:url var="logout" value="/logout" />
* The session data storage is controlled through a SessionRepository interface. The default implementation uses in memory concurrent HashMap, but other distributed maps provided by NoSQL stores like Redis, Hazlecast or gemfire could be used. It does provide support for integrating Redis key/value store. This POC was evaluated with in memory HashMap and distributed hashmap backed by Hazlecast.
* Optionally sessions can be managed by using HttpSessionManager and SessionRepository interfaces.

## Pros & Cons

### Pros:
* Enables multiple http sessions to be alive within the same browser instance. Each browser’s tab or window can have its own unique session.
* Eliminates dependency on container specific session implementation and thus allows you to plugin custom session storage mechanism (e.g Redis, hazlecast, gemfire, distributed hasMap etc)
* Integrates well with custom http conversation support (see possible issues section below).
* Provides API for managing user’s session. User may be able to control (through client side API) how many sessions are active at a time.
* The framework is fairly simple and extensible. Can be customized to cater needs through spring namespace extensions support.
Is now part of spring framework project and has good support from spring community.

