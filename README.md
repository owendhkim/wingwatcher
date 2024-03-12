# WingWatcher

Software developement practices class project. Image recognition app for birds.

Extracted backend part of the full project for portfolio and demonstration purposes.


## Description

● Designed and constructed RDBMS using hibernate and Springboot. [Project planning](https://github.com/owendhkim/wingwatcher/blob/main/Documents/ScreenSketches.pdf)

● Using REST API, created an intuitive and straightforward http requests.

● To add a security measure and prevent unauthorized users from accessing http requests by guessing url path, implemented tokenization feature.
I first created a [JwtSerivice class](https://github.com/owendhkim/wingwatcher/blob/main/309DB_Springboot/data/src/main/java/Tables/JwtService.java) with generation, validation, extraction methods.

● Using springboot security API, I created a [security config file]([https://github.com/owendhkim/wingwatcher/blob/main/309DB_Springboot/data/src/main/java/Tables/SecurityConfig.java](https://github.com/owendhkim/wingwatcher/blob/main/309DB_Springboot/data/src/main/java/Tables/SecurityConfig.java)) and injected a securityFilterChain that all http requests has to go through.

● User and token has one to many relationship, when user first signs up or logs in with the authorized id they will be generated a token and stored in a database via [authenticationService class](https://github.com/owendhkim/wingwatcher/blob/main/309DB_Springboot/data/src/main/java/Tables/Authentication/AuthenticationService.java). Through securityFilter, all http request will go through a validation check(username & expiration check with extracClaim()). So that only authorized user bearing valid jwt token will be able to make http requests to the server.


## Main takeaways

In this project, I learned how to work together as a team and develop productively in agile development practice.

I was able to gain experience on making relational database tables, getting used to Springboot annotations and REST API syntax.

As well as understanding Springboot Security API flowchart to implement Jwt token feature.


## Authors

[Owen Kim](https://www.linkedin.com/in/owen-kim-657249169/)
[Brian Xicon](https://www.linkedin.com/in/brian-xicon-b08202240/)
