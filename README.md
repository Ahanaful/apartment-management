# apartment-management
## Setting Up the Database

1. Install MySQL on your system.
2. Import the database using the provided SQL file:
   ```bash
   mysql -u [username] -p < apartment_management.sql

make .env file where:
DB_URL=jdbc:mysql://localhost:3306/Apartment_management
DB_USER=root //possibly it will be root
DB_PASSWORD=rootpassword //whatever is your password
