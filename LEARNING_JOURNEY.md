# SeekhoG Backend - Learning Journey

This document records our progress in building the backend for SeekhoG. Since we are starting from scratch with Java and Spring Boot, this file explains *what* we did and *why* we did it in simple terms.

---

## Step 1: The Foundation (Project Setup)

Before we can build features (like a menu or a kitchen), we need to build the building itself.

### 1. The "Shopping List" (`pom.xml`)
**File:** `pom.xml`

Imagine you are building a Lego castle. You need a list of specific blocks to buy. In Java, we use a tool called **Maven** to manage this.
*   **Maven:** The construction manager.
*   **pom.xml:** The instruction manual and shopping list for Maven.

**What we added:**
*   **Spring Boot Starter Web:** This is a "bundle" that includes everything needed to make a website (a server, tools to handle internet requests, etc.).
*   **Lombok:** A helper tool that writes boring code for us automatically later.

### 2. The "Filing Cabinet" (Folder Structure)
**Location:** `src/main/java/com/seekhog/backend`

Java is very organized. It demands that code lives in specific folders (packages) to avoid clutter.
*   `src/main/java`: Where our actual code lives.
*   `com.seekhog.backend`: Our unique "address" so our code doesn't get mixed up with others.

### 3. The "Ignition Key" (The Main Class)
**File:** `src/main/java/com/seekhog/backend/SeekhoGApplication.java`

A car needs a key to start. A Java program needs a `main` method.

```java
@SpringBootApplication // 1. The Sticker
public class SeekhoGApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeekhoGApplication.class, args); // 2. The Key turn
    }
}
```

1.  **The Sticker (`@SpringBootApplication`):** This tells the system, "This is a Spring Boot app. Please set yourself up automatically." It saves us from writing hundreds of lines of configuration code.
2.  **The Key Turn (`SpringApplication.run`):** This line actually starts the program.

### What happens when you run this?
When you press "Run":
1.  Java wakes up.
2.  Spring Boot looks at your `pom.xml` (shopping list).
3.  It sees "Web Starter", so it starts a mini web-server called **Tomcat** inside your app.
4.  It opens **Port 8080** on your computer. This is like opening the front door of a store so customers (your frontend) can walk in.

---

### Current Status
*   [x] Project Created
*   [x] Dependencies Added (pom.xml)
*   [x] Main Application Class Created
*   [ ] First Endpoint Created (Next Step)
