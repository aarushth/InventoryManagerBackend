@echo off

call .\venv\Scripts\activate.bat

REM === Set environment variables ===
set JWT_SECRET_KEY=6ODIrYklSWEwdKV9rLoEcPrbC1Vq2dx3
set DB_USERNAME=serveracc@leopardseal
set DB_PASS=MtFbwu2!

REM === Navigate to the project directory (optional) ===
REM cd path\to\your\springboot\project

REM === Run the Spring Boot application using Maven ===
mvn spring-boot:run

REM === Optional: pause the window to read output ===
pause 
