# SeekhoG Backend - Learning Journey

This document records our progress in building the backend for SeekhoG. Since we are coming from a Node.js background, we will use analogies to explain Java/Spring Boot concepts.

---

## The "Big Three" Concepts

Before we dive into the steps, let's clarify the three main tools we are using.

### 1. Java (The Language)
*   **Node.js Equivalent:** JavaScript.
*   **What is it?** The programming language we write the code in. It's statically typed (you have to define types like `String`, `int`), which catches errors early.

### 2. Spring Boot (The Framework)
*   **Node.js Equivalent:** Express.js (but on steroids).
*   **What is it?** A massive framework that sits on top of Java.
    *   **Express:** You have to manually set up the server, body parser, database connection, etc.
    *   **Spring Boot:** It's "opinionated." It assumes you want a standard web app and sets up 80% of it for you automatically (Auto-Configuration). It gives you the server (Tomcat), the router, and the tools to talk to databases right out of the box.

### 3. Maven (The Tool)
*   **Node.js Equivalent:** npm (Node Package Manager).
*   **What is it?** The project manager.
    *   **Dependency Management:** It reads `pom.xml` and downloads libraries (like `npm install`).
    *   **Build Tool:** It compiles your code and packages it into a runnable file (like `npm run build`).

---

## Step 1: The Foundation (Project Setup)

Before we can build features, we need to set up the project structure.

### 1. The "Shopping List" (`pom.xml`)
**File:** `pom.xml`
**Node.js Equivalent:** `package.json`

*   **What is it?** The configuration file for **Maven**.
*   **Purpose:** It lists all the external libraries (dependencies) our project needs.
*   **How to use it:**
    *   **Node:** You run `npm install express`.
    *   **Java:** You paste a `<dependency>` block into `pom.xml` and click the "Reload Maven" button in IntelliJ. This downloads the libraries to a hidden folder on your computer (similar to a global `node_modules`).

**What we added:**
*   **Spring Boot Starter Web:** Equivalent to `express`. It gives us a server and routing tools.
*   **Lombok:** A helper tool to reduce boilerplate code.

### 2. The "Filing Cabinet" (Folder Structure)
**Location:** `src/main/java/com/seekhog/backend`
**Node.js Equivalent:** Your `src` folder, but stricter.

Java enforces a strict folder hierarchy called **packages**.
*   `src/main/java`: The root of your source code.
*   `com.seekhog.backend`: This is our **Package**. It acts like a namespace to prevent naming collisions with other libraries.

### 3. The "Ignition Key" (The Main Class)
**File:** `src/main/java/com/seekhog/backend/SeekhoGApplication.java`
**Node.js Equivalent:** `index.js` or `server.js`

This is the entry point of the application.

```java
@SpringBootApplication // 1. The Sticker
public class SeekhoGApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeekhoGApplication.class, args); // 2. The Key turn
    }
}
```

1.  **`@SpringBootApplication`:** This annotation is like `const app = express();` but more powerful. It automatically configures the app based on your dependencies.
2.  **`SpringApplication.run(...)`:** This starts the server (Tomcat) on port 8080.

---

## Step 2: The First Endpoint (Controller Layer)

Now we need to handle incoming HTTP requests.

### 1. The Waiter (`HelloController.java`)
**File:** `src/main/java/com/seekhog/backend/controller/HelloController.java`
**Node.js Equivalent:** A route handler file (e.g., `routes/hello.js`).

In Spring Boot, we use **Controllers** to handle requests.

```java
@RestController // 1. The Uniform
public class HelloController {

    @GetMapping("/api/hello") // 2. The Menu Item
    public String sayHello() {
        return "Hello from SeekhoG Backend!"; // 3. The Dish
    }
}
```

1.  **`@RestController`:**
    *   **Node:** Similar to `app.get(...)` logic.
    *   **Java:** Tells Spring "This class handles web requests and returns data (JSON/Text), not HTML pages."
2.  **`@GetMapping("/api/hello")`:**
    *   **Node:** `app.get('/api/hello', (req, res) => { ... })`
    *   **Java:** Maps the GET method and URL to this specific function.
3.  **The Return Value:**
    *   **Node:** `res.send("Hello...")`
    *   **Java:** You just `return` the data, and Spring automatically sends it as the response.

### Why do we need this?
Without a controller, the server runs but has no "routes" defined. It would just give 404 errors for everything.

---

## Step 3: Connecting Frontend (CORS)

We need to allow our Next.js frontend (port 3000) to talk to our backend (port 8080).

### 1. The Bouncer (`WebConfig.java`)
**File:** `src/main/java/com/seekhog/backend/config/WebConfig.java`
**Node.js Equivalent:** `app.use(cors({ origin: 'http://localhost:3000' }))`

We created a configuration class to handle CORS (Cross-Origin Resource Sharing).

```java
@Configuration // 1. The Settings Label
public class WebConfig {

    @Bean // 2. The Component
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all URLs
                        .allowedOrigins("http://localhost:3000") // Only from Next.js
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
```

1.  **`@Configuration`:** This tells Spring "This class contains setup instructions." It's like a settings file.
2.  **`@Bean`:** This is a very important concept. It tells Spring "Take the result of this function and keep it in your memory. Use it whenever you need to configure the web server."
3.  **The Logic:** We are explicitly telling the server to accept requests from `localhost:3000`.

### Why do we need this?
By default, browsers block a website on port 3000 from fetching data from port 8080 for security. This config disables that block for our specific frontend.

---

### Current Status
*   [x] Project Created
*   [x] Dependencies Added (pom.xml)
*   [x] Main Application Class Created
*   [x] First Endpoint Created (HelloController)
*   [x] CORS Configuration Created (WebConfig)
*   [ ] Data Structure / Models (Next Step)
