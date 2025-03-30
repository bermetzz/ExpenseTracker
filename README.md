# Expense Tracker
Expense Tracker is an expense tracking app with support for JWT authentication, filtering, analytics, and a **Telegram bot** for receiving expense data. The app provides an API for managing expenses, filtering by date and category, and visualizing data on charts using **Chart.js**.

## Technologies Used

- Backend: Java, Spring Boot, Spring Security, PostgreSQL
- Frontend: HTML, Chart.js
- API: REST API using JWT for authentication
- Databases: PostgreSQL
- Telegram bot: TelegramBots API

## Installation

1. Clone the repository
   ```bash
git clone https://github.com/username/ExpenseTracker.git
cd ExpenseTracker
3. Configure application.properties file:
  spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
  spring.datasource.username=YOUR_USERNAME
  spring.datasource.password=YOUR_PASSWORD
  app.jwt.secret=YOUR_SECRET_KEY
  app.jwt.expiration=3600000
  telegram.bot.token=YOUR_BOT_TOKEN
  telegram.bot.username=YOUR_BOT_USERNAME
4. Run the application
  mvn spring-boot:run

## TODO

- Add support for multiple currencies
- Improve graphical data visualization
- Add the ability to export reports to PDF
