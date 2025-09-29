# Orders Management App 📦

Welcome to the **Orders Management App**, a desktop application developed with **Java Swing**.
Its purpose is to manage client orders for a warehouse, including product, client, and order management, with all data stored in a **PostgreSQL database**.

---

# Description 📖

The application is designed using a **layered architecture** and provides a **main GUI window** where users can:

* **Manage Products**: Open the product management window to add, edit, delete, and view products.
* **Manage Clients**: Open the client management window to add, edit, delete, and view clients.
* **Manage Orders**: Open the order management window to create orders, select products and clients, specify quantities, and automatically update stock. Under-stock messages are displayed when quantities are insufficient.
* **Generate Bills**: Automatically generate an immutable **Bill** for each order and store it in the Log table.
* **Reflection-Based Table Handling**: Dynamically generate table headers and populate table data using reflection.

All application logic is separated into **model, business, data access, and presentation layers** for clean code and maintainability. Data is persisted in a **PostgreSQL database**, and generic CRUD operations use reflection for dynamic SQL generation.

---

# Features 🪄

* **Main GUI Window**: Central interface with buttons to manage products, clients, and orders.
* **Layered Architecture**: Separate packages for dataAccessLayer, businessLayer, model, and presentation.
* **Client Management**: Add, edit, delete, and view clients.
* **Product Management**: Add, edit, delete, and view products.
* **Order Management**: Create product orders, update stock, and display under-stock warnings.
* **Immutable Bill Records**: Generate and store bills using Java records.
* **Reflection Techniques**: Dynamically generate table headers and CRUD operations for database objects.
* **Streams and Lambdas**: Utilize modern Java features for processing lists and arrays.
* **User-Friendly GUI**: Simple, intuitive interface built with **Java Swing**.

---

# Tech Stack 🛠

### Back-End:

* Java 17+
* JDBC for PostgreSQL database connectivity
* Object-Oriented Programming
* Reflection
* Lambda expressions and Streams

### Front-End:

* Java Swing (GUI)

### Tools:

* IntelliJ IDEA 
* Maven 
* pgAdmin or any PostgreSQL client 

---

# Installation ⚙️

## Prerequisites:

* Java 17 or newer
* Maven
* PostgreSQL server and pgAdmin or any PostgreSQL client
* IntelliJ IDEA or any Java IDE

## Steps:

1. Clone this repository:

```bash
git clone https://github.com/Ciprian-Popescu-03/OrdersManagement.git
```

2. Open the project in **IntelliJ IDEA**.
3. Build the project using **Maven**.
4. Set up the database using the provided SQL dump file.
5. Run the main GUI class (`MainGUI`) to start the application.
