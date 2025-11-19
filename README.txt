# TOEIC Fighter – TOEIC Practice Application

## Overview
TOEIC Fighter is a desktop application built with **Java**, **JavaFX** and **MySQL** to help users practice for the TOEIC Listening & Reading exam. It features user authentication, role‑based access control, question management (create, read, update, delete), and timed practice tests that track performance.
Demo: https://www.youtube.com/watch?v=h6VRPpF6iDw

## Features
- User login and registration
- Role‑based access: *Admin* can manage questions, *User* can take tests
- Full CRUD (Create, Read, Update, Delete) functionality for question bank
- Practice test mode for Listening & Reading sections, with timer and score tracking
- Results management: view past test scores and progress over time
- Modern UI built with JavaFX following MVC architecture
- Modular code design using OOP principles for maintainability and extensibility

## Technologies
- Java 21
- JavaFX for desktop UI
- MySQL for data storage
- JDBC for database connectivity
- Git & GitHub for version control

## Getting Started
### Prerequisites
- Java JDK 11 or newer installed on your system
- MySQL server installed and running
- Git (optional, for cloning repo)

### Installation & Setup
1. Clone the repository: git clone https://github.com/duongviethuy/TOEICFighter.git
2. Create a database named `toeic_fighter` in your MySQL server.
3. Import the provided SQL script (`/db/schema.sql`) to create necessary tables.
4. Update the database connection settings in `src/main/resources/config.properties` (or similar) to match your MySQL credentials.
5. Build and run the application using your IDE (IntelliJ IDEA, Eclipse) or via command line:
mvn clean install
java -jar target/TOEICFighter.jar
6. Login as admin (default credentials: `admin / 123`) to add questions, or register as a user to start practising.

## Usage
- Admin: after login, navigate to “Question Management” to add/edit/delete questions.
- User: login or register, then select “Start Test” to begin a timed test. After finishing, view your score and history in “Results”.
- The UI provides a clear flow: Home → Section Selection → Question & Answer → Score Summary.

## Design & Architecture
- Follows **MVC (Model‑View‑Controller)** pattern for separation of concerns.
- Uses **OOP design**: each entity (User, Question, Test) is represented by a class, service and DAO layers handle logic & persistence.
- UI layer uses JavaFX with FXML, event handlers for user actions.
- Database access through JDBC, prepared statements to prevent SQL injection.

## License
This project is open source and available under the MIT License.

## Contact
For questions or contributions, feel free to open an issue or pull request in the repository.
Created by: **Dương Việt Huy**
GitHub: https://github.com/duongviethuy
