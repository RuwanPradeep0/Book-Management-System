﻿# Book Management System

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
    - [Backend (book-social-network)](#backend-book-social-network)
- [Learning Objectives](#learning-objectives)
- [License](#license)

[//]: # (- [Getting Started]&#40;#getting-started&#41;)

[//]: # (- [Contributors]&#40;#contributors&#41;)

[//]: # (- [Acknowledgments]&#40;#acknowledgments&#41;)

## Overview

Book Management system is a full-stack application that enables users to manage their book collections and engage with a community of book enthusiasts. It offers features such as user registration, secure email validation, book management (including creation, updating, sharing, and archiving), book borrowing with checks for availability, book return functionality, and approval of book returns. The application ensures security using JWT tokens a, while the frontend is developed using Angular with Bootstrap for styling.
## Features

- User Registration: Users can register for a new account.
- Email Validation: Accounts are activated using secure email validation codes.
- User Authentication: Existing users can log in to their accounts securely.
- Book Management: Users can create, update, share, and archive their books.
- Book Borrowing: Implements necessary checks to determine if a book is borrowable.
- Book Returning: Users can return borrowed books.
- Book Return Approval: Functionality to approve book returns.

#### Class diagram
![Class diagram](images/class_diagram.png)

[//]: # (#### Spring security diagram)

[//]: # (![Security diagram]&#40;screenshots/security.png&#41;)

[//]: # ()
[//]: # (#### Backend pipeline)

[//]: # (![Security diagram]&#40;screenshots/be-pipeline.png&#41;)

[//]: # ()
[//]: # (#### Backend pipeline)

[//]: # (![Security diagram]&#40;screenshots/fe-pipeline.png&#41;)

## Technologies Used

### Backend (book-network)

- Spring Boot 3
- Spring Security 6
- JWT Token Authentication
- Spring Data JPA
- OpenAPI and Swagger UI Documentation


## Learning Objectives

By doing this project, I have learned:

- Implementing a mono repo approach
- Securing an application using JWT tokens with Spring Security
- Registering users and validating accounts via email
- Utilizing inheritance with Spring Data JPA
- Implementing the service layer and handling application exceptions
- Object validation using JSR-303 and Spring Validation
- Handling custom exceptions
- Implementing pagination and REST API best practices
- Using Spring Profiles for environment-specific configurations
- Documenting APIs using OpenAPI and Swagger UI
- Implementing business requirements and handling business exceptions


## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
