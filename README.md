# 📚 Library Management System

A web-based Library Management System built using Java, Spring Boot, MySQL, HTML, CSS, and JavaScript.

## ✨ Features

- Admin Login and Logout
- Dashboard
- Add, Edit, Search, and Manage Books
- User Management
- Issue and Return Books
- Borrowing History
- Library Settings
- MySQL Database Integration

## 🛠️ Technologies Used

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- HTML
- CSS
- JavaScript
- Maven

## ⚙️ Prerequisites

- Java 21 or compatible version
- MySQL Server
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Sachin7017/library-management-system.git
cd library-management-system/library-management
### 2. Configure MySQL

1. Install and start MySQL Server.
2. Open MySQL Workbench and connect to your local MySQL instance.
3. Create the database only if it does not already exist:

   ```sql
   CREATE DATABASE library_management;
   ```

4. Your application uses the `library_management` database.

### 3. Configure Environment Variables

Before starting the application, configure these environment variables:

- `DB_USERNAME` — your MySQL username
- `DB_PASSWORD` — your MySQL password
- `LIBRARY_ADMIN_EMAIL` — admin login email
- `LIBRARY_ADMIN_PASSWORD` — admin login password

Do not publish real credentials or passwords in this repository.

### 4. Run the Application

On Windows PowerShell, run:

```powershell
.\mvnw.cmd spring-boot:run
```

Keep the terminal running while using the application.

### 5. Open the Application

Open this URL in your browser:

http://localhost:8080/login.html

Log in using the admin credentials configured in your environment variables.

## 🔐 Security

- Never commit database passwords or admin credentials.
- Keep real secrets in environment variables.
- Do not upload backup files containing credentials.

## 👨‍💻 Author

Sachin Kumar