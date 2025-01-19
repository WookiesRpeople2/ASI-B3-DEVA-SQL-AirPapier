# ASI-B3-DEVA-SQL-AirPapier

<a href="https://app.eraser.io/workspace/sHyYKt10oFa4tpWLeij8?elements=s7DYVSXYDmJn-LXi_mUujQ"><img src="https://app.eraser.io/workspace/sHyYKt10oFa4tpWLeij8/preview?elements=s7DYVSXYDmJn-LXi_mUujQ&type=embed" /></a>
---
# IMPORTANT
if you follwed the steps in the V1 please drop and run `mvn initialize flyway:clean` in order to re-init the schema seeing as the tables have changed and then follow the run setps as seen down below 

# RUN

In order to run this project please follow the steps below
- clone the project into your local repository
- `cd air-papier`
- run `docker-compose up -d`
- next copy the environment variables from the .env.example to a new file called .env in the same folder `air-papier/src/main/resources`
- next run `mvn initialize flyway:migrate` in order to add the database migration, the migrations can be fount is `src/java/main/Database/migrations`
- next in order to seed your database run the command `mvn package`

# .env.example
````
PORT=3000
DATABASE_URL=jdbc:mysql://localhost:3306/airPapier
DATABASE_USER=root
DATABASE_PASSWORD=password
DATABASE=airPapier
````
