# LEMS (Backend)

## Project Description

This project serves as the backend for LEMS. It is developed using Java Spring Boot to provide REST API endpoints for interacting with MySQL database. Check the frontend repository [here](https://github.com/MarkJanzenB/cit-lems-frontend.git)

## Tools/Tech Used

![Java](https://skillicons.dev/icons?i=java,spring,mysql) 

### Prerequisites
**MySQL Database**: Ensure MySQL is installed on your system. You can download and install it from [here](https://dev.mysql.com/downloads/installer/)(Windows).

### Steps to Install and Run the Project
1. **Clone the Repository**: 
   ```bash
   git clone https://github.com/MarkJanzenB/cit-lems-backend.git

2. **Ensure MySQL Database and Schema Exist**: 
  - Make sure MySQL is running.
  - Create a database schema named `lemsdb` in your MySQL server. If it doesn't exist, you can create it using MySQL Workbench or any other MySQL management tool.

3. **Configure Application Properties**: 
- Open `src/main/resources/application.properties` file.
- Set the username and password for your MySQL database to match your MySQL Workbench credentials:
  ```properties
  spring.datasource.username=<your-username>
  spring.datasource.password=<your-password>

4. **Run the application**: 
- Run the LemsApplication.java file in `src/main/resources/LemsApplication.java`.
