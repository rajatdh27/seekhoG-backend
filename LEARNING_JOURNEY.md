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

---

## Step 4: User Authentication (Simplified)

We replaced the "Topic" logic with a simple User Authentication system.

### 1. The User Model (`User.java`)
**File:** `src/main/java/com/seekhog/backend/model/User.java`

A simple class to hold user data: `id`, `username`, `password`, `role`.

### 2. The Auth Controller (`AuthController.java`)
**File:** `src/main/java/com/seekhog/backend/controller/AuthController.java`

This controller handles:
*   **Sign Up:** `POST /api/auth/signup` - Adds a user to our temporary list.
*   **Login:** `POST /api/auth/login` - Checks if username/password match.
*   **Anonymous:** `POST /api/auth/anonymous` - Creates a temporary guest user.

**Note:** We are using an `ArrayList` as a temporary database. If you restart the server, all users will be lost. This is fine for testing.

---

## Step 5: Database Integration (H2 & JPA)

We are now adding a real database to store users permanently.

### Why H2?
*   **Zero Config:** It runs inside the app's memory. No installation required.
*   **Fast:** It's in-memory, so it's instant.
*   **Standard SQL:** It behaves like a real SQL database.
*   **Easy Switch:** Because we use JPA, switching to Postgres later is just a config change.

### 1. The Dependencies (`pom.xml`)
We added two new items to our shopping list:
*   **Spring Data JPA:** The tool that lets Java talk to databases using Objects instead of SQL queries.
*   **H2 Database:** A lightweight database that runs inside the app's memory (great for dev).

### 2. The Configuration (`application.properties`)
**File:** `src/main/resources/application.properties`
**Node.js Equivalent:** `.env` file.

We told Spring Boot:
*   "Use H2 database."
*   "If I change my Java classes, automatically update the database tables (`ddl-auto=update`)."

### 3. The Entity (`User.java`)
**Node.js Equivalent:** Mongoose Schema.

We added `@Entity` to our User class. This tells Spring: "Create a table named `users` with columns `id`, `username`, `password`, `role`."

### 4. The Repository (`UserRepository.java`)
**Node.js Equivalent:** `User.find()`, `User.create()` methods.

This is an **Interface**. We don't write the code! We just say `extends JpaRepository`, and Spring *magically* generates methods like `save()`, `findById()`, `delete()` for us.

### 5. The Controller Update (`AuthController.java`)
We replaced the `ArrayList` with `userRepository`. Now, when we call `userRepository.save(user)`, it actually writes SQL (`INSERT INTO users...`) and saves it to the H2 database.

---

## Step 6: "My Journey" Feature

We added a feature for users to track their learning progress.

### 1. The Model (`LearningEntry.java`)
*   **Table:** `learning_entries`
*   **Fields:** `topic`, `subTopic`, `content` (Large Text), `difficulty`, `referenceLink`, `platform`, `learningDate`.
*   **Relationship:** Linked to `User` via `userId` (String).

### 2. The Repository (`LearningEntryRepository.java`)
*   Added a custom method: `findByUserIdOrderByLearningDateDesc(String userId)`
*   This automatically writes the SQL: `SELECT * FROM learning_entries WHERE user_id = ? ORDER BY learning_date DESC`

### 3. The Controller (`LearningEntryController.java`)
*   **GET** `/api/journey/{userId}`: Fetch all entries for a user.
*   **POST** `/api/journey`: Add a new entry.
*   **DELETE** `/api/journey/{id}`: Delete an entry.

---

## Step 7: Export Feature (Excel & PDF)

We added the ability to export selected learning logs.

### 1. Dependencies (`pom.xml`)
*   **Apache POI:** For generating Excel files.
*   **iText PDF:** For generating PDF files.

### 2. The DTO (`ExportRequest.java`)
*   A simple object to hold the incoming JSON: `{ "userId": "...", "logIds": [1, 2, 3] }`.

### 3. The Service (`ExportService.java`)
*   **Validation:** Fetches logs by ID and filters out any that don't belong to the requesting user (Security).
*   **Excel Generation:** Creates a workbook, sheet, header row, and data rows. Returns a byte stream.
*   **PDF Generation:** Creates a PDF document with a table and populates it. Returns a byte stream.

### 4. The Controller (`ExportController.java`)
*   **POST** `/api/journey/export/excel`: Returns the Excel file as a download.
*   **POST** `/api/journey/export/pdf`: Returns the PDF file as a download.

---

## Step 8: Deployment Preparation (Docker)

We created a `Dockerfile` to allow deployment on platforms like Render.

### What is Docker?
Think of Docker as a **Shipping Container** for your code.
*   **Without Docker:** You send your code to a server. The server might have the wrong Java version, missing libraries, or a different OS. It breaks.
*   **With Docker:** You package your code + Java + Libraries + OS Settings into a sealed box (Image). You send the box. The server just runs the box. It works exactly the same everywhere.

### The Dockerfile Explained
**File:** `Dockerfile`

We used a **Multi-Stage Build** to keep the final image small.

**Stage 1: The Factory (Build)**
```dockerfile
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
```
*   **What it does:** It downloads a heavy image with Maven installed. It copies your source code and runs the build command (`mvn package`). This creates the `.jar` file (the executable).

**Stage 2: The Delivery Truck (Run)**
```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
*   **What it does:** It starts fresh with a tiny, lightweight Java image (no Maven, no source code). It copies *only* the `.jar` file from Stage 1. It tells the server to run that jar on port 8080.

---

## Step 9: Leaderboard Feature

We added a leaderboard to show the top 10 users with the most learning logs.

### 1. The DTO (`LeaderboardEntryDTO.java`)
*   A simple class to hold the result: `username`, `totalLogs`, `lastActive`.
*   This is needed because the result of our query isn't a "User" or a "Log", it's a mix of both.

### 2. The Repository Query (`LearningEntryRepository.java`)
*   We wrote a **JPQL (Java Persistence Query Language)** query.
*   **Logic:**
    *   `SELECT new ...DTO(...)`: Create a DTO object directly from the query.
    *   `FROM LearningEntry l, User u`: Join the two tables.
    *   `WHERE l.userId = u.id`: Match the IDs.
    *   `GROUP BY u.username`: Group logs by user.
    *   `ORDER BY COUNT(l) DESC`: Sort by who has the most logs.

### 3. The Controller (`LeaderboardController.java`)
*   **GET** `/api/leaderboard`: Calls the repository and asks for the top 10 results (`PageRequest.of(0, 10)`).

---

## Step 10: Chat Feature (Real-time)

We are adding a real-time chat feature using WebSockets.

### 1. WebSocket Configuration (`WebSocketConfig.java`)
*   **Dependency:** `spring-boot-starter-websocket`.
*   **Endpoint:** `/ws` (This is where the frontend connects).
*   **Broker:** `/topic` (This is the channel where messages are broadcasted).
*   **Prefix:** `/app` (This is where clients send messages to).

### 2. Chat Models (`ChatMessage.java`)
*   **Table:** `chat_messages`
*   **Fields:** `sender`, `content`, `timestamp`.
*   **Repository:** `ChatMessageRepository` to save messages to the DB.

### 3. Chat Controller (`ChatController.java`)
*   **Real-time:** `@MessageMapping("/sendMessage")` receives a message, saves it, and `@SendTo("/topic/public")` broadcasts it to everyone.
*   **History:** `@GetMapping("/api/chat/history")` returns the list of old messages so new users can see what they missed.

---

## Step 11: Social Features (Phase 1 - Friendships)

We are building the foundation for a social network.

### 1. The Friendship Model (`Friendship.java`)
*   **Table:** `friendships`
*   **Fields:** `requesterId`, `addresseeId`, `status` (PENDING, ACCEPTED, BLOCKED).
*   **Constraint:** Unique constraint ensures A cannot friend B twice.

### 2. The Repository (`FriendshipRepository.java`)
*   **`findRelationship`:** Checks if two users are already friends (or pending) regardless of who sent the request.
*   **`findAllFriends`:** Finds all accepted friendships for a user.

### 3. The Controller (`FriendshipController.java`)
*   **POST** `/api/friends/request`: Send a friend request.
*   **POST** `/api/friends/accept/{id}`: Accept a request.
*   **GET** `/api/friends/{userId}`: List all friends.
*   **GET** `/api/friends/pending/{userId}`: List incoming requests.

---

## Step 12: Advanced Chat (Phase 2 - Private Conversations)

We are upgrading the chat system to support private (1-on-1) and group conversations.

### 1. The Models
*   **`Conversation.java`:** Represents a chat room (DIRECT or GROUP).
*   **`ConversationParticipant.java`:** Links users to conversations.
*   **`Message.java`:** Replaces the old `ChatMessage` model. It links to a `conversationId` instead of being global.

### 2. The Repositories
*   **`ConversationRepository`:** Includes a complex query `findDirectConversation` to check if a 1-on-1 chat already exists between two users.
*   **`MessageRepository`:** Fetches messages for a specific conversation.

### 3. The Controller (`AdvancedChatController.java`)
*   **POST** `/api/chat/create/private/{targetUserId}`: Creates a new chat or returns the existing one.
*   **GET** `/api/chat/conversations`: Lists all chats for a user.
*   **GET** `/api/chat/history/{conversationId}`: Fetches messages for a specific chat.

---

## Step 13: Real-Time Presence & Private Messaging (Phase 3)

We updated the WebSocket configuration to support private messaging and presence detection.

### 1. WebSocket Config Update
*   Added `/queue` to the broker (for private messages).
*   Added `/user` destination prefix (for targeting specific users).

### 2. RealTimeChatController
*   **`@MessageMapping("/private-message")`:** Handles incoming private messages.
*   **Logic:** Saves the message to the DB and then broadcasts it to the specific conversation topic (`/topic/conversation.{id}`).

### 3. WebSocketEventListener
*   Listens for `SessionConnectedEvent` and `SessionDisconnectEvent`.
*   This is where we can add logic to mark users as "Online" or "Offline" in the database.

---

## Step 14: User Search

We added a way to find users to friend.

### 1. Repository Update (`UserRepository.java`)
*   Added `findByUsernameContainingIgnoreCase(String query)`: This allows partial matching (e.g., "raj" finds "Rajat").

### 2. Controller (`UserController.java`)
*   **GET** `/api/users/search?query=...`: Returns a list of matching users.

---

## Step 15: Friendship Improvements (DTO & Sent Requests)

We fixed the "Unknown User" issue and added the "Sent Requests" tab.

### 1. The DTO (`FriendshipResponse.java`)
*   Instead of returning raw IDs (`requesterId`), we now return a DTO that includes the **Username** (`requesterName`).
*   This saves the frontend from having to make extra API calls to look up names.

### 2. Controller Updates (`FriendshipController.java`)
*   **`sendRequest`:** Now accepts `targetUsername` instead of `addresseeId`. It looks up the ID internally. This is much easier for the frontend.
*   **`getPendingRequests`:** Now returns `List<FriendshipResponse>` (with names).
*   **`getSentRequests`:** Added this new endpoint so users can see who they sent requests to.

---

## Step 16: Friend Stats (Total Logs)

We added `totalLogs` to the Friendship Response so you can see how active your friends are.

### 1. Repository Update (`LearningEntryRepository.java`)
*   Added `countByUserId(String userId)` to quickly count logs.

### 2. DTO Update (`FriendshipResponse.java`)
*   Added `Long totalLogs` field.

### 3. Controller Update (`FriendshipController.java`)
*   In `mapToDTO`, we now call `learningRepository.countByUserId(friendId)` and pass it to the DTO.

---

## Step 17: Read Receipts (Seen Feature)

We added the ability to mark messages as "Read" and notify the sender.

### 1. Model Update (`Message.java`)
*   Added `MessageStatus status` (SENT, DELIVERED, READ).

### 2. Repository Update (`MessageRepository.java`)
*   Added `markMessagesAsRead(conversationId, readerId)` query to bulk update unread messages.

### 3. Controller Update (`RealTimeChatController.java`)
*   **`@MessageMapping("/mark-read")`:** Handles the "I saw this" signal.
*   **Logic:** Updates DB -> Broadcasts `READ_RECEIPT` event to the conversation topic.

---

## Step 18: Video Call (Skipped for Now)

We attempted to implement WebRTC signaling but encountered frontend/backend payload mismatches. We decided to skip this feature for now to focus on stability.

---

## Step 19: Optimization - Display Name & Indexing

We improved the database schema for better performance and UX.

### 1. User Entity Update
*   Added `name` (Display Name) separate from `username` (Login/Email).
*   Added `@Index` on `username` for faster login/search.

### 2. Auth Controller Update
*   Signup now accepts `name`. If not provided, it defaults to `username`.

---

## Step 20: Optimization - Last Message Preview

We optimized the Chat List to load instantly without querying the messages table.

### 1. Conversation Entity Update
*   Added `lastMessageContent` and `lastMessageAt`.

### 2. RealTimeChatController Update
*   When a message is sent, we now update the `Conversation` entity with the message content and timestamp.
*   This allows the frontend to show the "Inbox" list with previews instantly.

---

## Step 21: Optimization - Database Indexing

We added indexes to all critical foreign keys to ensure the app remains fast as data grows.

### 1. Friendship Entity
*   Added indexes on `requesterId` and `addresseeId`.

### 2. Message Entity
*   Added indexes on `conversationId` and `senderId`.

### 3. ConversationParticipant Entity
*   Added indexes on `userId` and `conversationId`.

---

## Step 22: Optimization - UUID Migration

We migrated all database IDs from `Long` (Auto-Increment) to `String` (UUID) for better security and scalability.

### 1. Entity Updates
*   Changed `@Id Long id` to `@Id String id` in `Friendship`, `Conversation`, `ConversationParticipant`, `Message`, `LearningEntry`.
*   Added `@PrePersist` method to generate UUIDs automatically if null.

### 2. Repository Updates
*   Changed `JpaRepository<Entity, Long>` to `JpaRepository<Entity, String>`.

### 3. Controller Updates
*   Updated all `@PathVariable` and method signatures to accept `String` IDs.

### 4. Frontend Impact
*   The frontend must now handle IDs as strings (e.g., `"550e8400-e29b..."`) instead of numbers.

---

## Optimization Summary (Before vs. After)

This section explains the impact of the optimizations we implemented in Steps 19-22.

### 1. Display Name
*   **Before:** Users were identified only by their login email (e.g., `rajatdh27@gmail.com`). This was bad for privacy and looked ugly in the UI.
*   **After:** Users have a separate `name` field (e.g., "Rajat Thakur").
*   **Frontend Change Required:**
    *   **Signup:** Add a "Full Name" input field.
    *   **Profile/Chat:** Display `user.name` instead of `user.username`.

### 2. Chat List Performance (Last Message Preview)
*   **Before:** To show the "Inbox" list with the last message (e.g., "Hey, how are you?"), the backend had to query the `messages` table for *every single conversation*.
    *   *Cost:* If you have 50 chats, that's 51 database queries (N+1 problem).
*   **After:** The `conversations` table now stores `lastMessageContent` directly.
    *   *Cost:* Only 1 database query to load the entire inbox.
*   **Frontend Change Required:**
    *   The `/api/chat/conversations` endpoint now returns the preview directly. You don't need to fetch history to show the list.

### 3. Database Indexing
*   **Before:** Searching for "My Friends" required the database to scan the entire `friendships` table (Full Table Scan).
    *   *Impact:* Slow as the app grows to 10,000+ users.
*   **After:** We added `@Index` to all foreign keys (`requesterId`, `conversationId`, etc.).
    *   *Impact:* Searches are instant (O(log n) complexity).
*   **Frontend Change Required:** None. It just feels faster.

### 4. UUID Migration
*   **Before:** IDs were predictable numbers (`1`, `2`, `3`).
    *   *Risk:* Someone could guess `GET /api/chat/history/5` to see someone else's chat.
*   **After:** IDs are random strings (`a1b2-c3d4...`).
    *   *Benefit:* Impossible to guess.
*   **Frontend Change Required:** Treat all IDs as strings.

---

## Troubleshooting & Fixes

We encountered some common setup issues. Here is how we fixed them:

### Issue 1: "Green Play Button" Missing / Not Runnable
*   **Problem:** IntelliJ didn't recognize the project structure or the `main` method.
*   **Fix:** We had to explicitly tell IntelliJ that this is a Maven project.
    *   Right-click `pom.xml` -> **"Add as Maven Project"**.
    *   This forced IntelliJ to download dependencies and index the project.

### Issue 2: "Cannot resolve symbol 'springframework'"
*   **Problem:** The code was red because the Spring Boot libraries weren't downloaded yet.
*   **Fix:**
    *   Open the **Maven** tool window (Right sidebar).
    *   Click the **Refresh** (Reload) icon.
    *   Wait for the bottom progress bar to finish downloading JARs.

### Issue 3: "Package name does not correspond to file path"
*   **Problem:** We manually marked `src` as the "Sources Root", which confused the package structure.
*   **Fix:**
    *   Unmark `src`.
    *   Mark `src/main/java` as the **Sources Root** (Blue folder).
    *   Reload Maven project.

### Issue 4: "package org.springframework.data.jpa.repository does not exist"
*   **Problem:** We added the dependency to `pom.xml`, but Maven hadn't finished downloading it, so the code couldn't find it.
*   **Fix:**
    *   Open **Maven** tool window.
    *   Click **Reload All Maven Projects**.
    *   Wait for the download to complete.

### Issue 5: "Cannot resolve method 'getLearningDate'"
*   **Problem:** Lombok annotations (`@Data`) weren't being processed by IntelliJ.
*   **Fix:**
    *   Go to **Settings** > **Build, Execution, Deployment** > **Compiler** > **Annotation Processors**.
    *   Check **Enable annotation processing**.
    *   (Alternatively) Install the Lombok plugin if missing.

### Issue 6: Docker "openjdk:17-jdk-slim not found"
*   **Problem:** The official OpenJDK Docker image was deprecated/moved.
*   **Fix:** We switched to `eclipse-temurin:17-jdk-jammy`, which is the modern, stable standard for Java 17 images.

### Issue 7: "Invalid Credentials" on Render (H2 Reset)
*   **Problem:** H2 is an in-memory database. Every time Render restarts (or sleeps/wakes), the database is wiped clean. Users created yesterday are gone today.
*   **Fix:**
    *   **Short Term:** We updated `AuthController` to return a specific error: *"User not found. The database might have reset. Please Sign Up again."*
    *   **Long Term:** We will eventually switch to PostgreSQL for permanent storage.

### Issue 8: "Unknown User" in Friend Requests
*   **Problem:** The API was returning raw IDs, so the frontend couldn't display names.
*   **Fix:** We created `FriendshipResponse` DTO to include usernames in the response.

### Issue 9: Video Call "User must not be null"
*   **Problem:** Frontend was sending WebSocket messages without `targetUserId`.
*   **Resolution:** We decided to skip the Video Call feature for now to focus on core stability.

### Issue 10: How to Access the H2 Database
Since H2 is in-memory, it has a built-in web interface to view your data.

1.  **Open Browser:** Go to `http://localhost:8080/h2-console`
2.  **Enter Settings:**
    *   **Driver Class:** `org.h2.Driver`
    *   **JDBC URL:** `jdbc:h2:mem:seekhogdb`
    *   **User Name:** `sa`
    *   **Password:** `password`
3.  **Connect:** Click the "Connect" button.
4.  **View Data:** Click on the `USERS` table in the left sidebar and click "Run" to see your registered users.

---

## System Architecture & Data Flow

This section explains how the different parts of the system interact.

### 1. The Request Flow (REST API)
When a user clicks a button (e.g., "Add Friend"):
1.  **Frontend (Next.js):** Sends an HTTP POST request to `/api/friends/request`.
2.  **Controller (Spring Boot):** Receives the request, validates input, and calls the Repository.
3.  **Repository (JPA):** Translates the Java method call into a SQL query (`INSERT INTO friendships...`).
4.  **Database (H2):** Executes the SQL and stores the data.
5.  **Response:** The Controller returns a JSON object to the Frontend.

### 2. The Real-Time Flow (WebSockets)
When a user sends a chat message:
1.  **Frontend:** Emits a STOMP message to `/app/private-message`.
2.  **Controller:** `@MessageMapping` intercepts the message.
3.  **Database:** The message is saved to the `messages` table via Repository.
4.  **Broker:** The message is pushed to the specific topic `/topic/conversation.123`.
5.  **Subscriber:** Any user listening to that topic (the friend) receives the message instantly.

---

## Final Database Architecture & Relationships

Here is the complete schema of what we are storing in the database.

### 1. `users` Table
*   **Role:** The center of the universe.
*   **Relationships:**
    *   **One-to-Many** with `learning_entries` (A user has many logs).
    *   **One-to-Many** with `friendships` (A user has many friends).
    *   **One-to-Many** with `conversation_participants` (A user is in many chats).

### 2. `learning_entries` Table
*   **Role:** Stores the "Journey" data.
*   **Join:** `user_id` -> `users.id`.
*   **Usage:** Used for the "My Journey" page and the "Leaderboard" (by counting entries per user).

### 3. `friendships` Table
*   **Role:** Connects two users.
*   **Join:** `requester_id` -> `users.id` AND `addressee_id` -> `users.id`.
*   **Usage:** Used to show the "Friends List" and allow "Private Chat" creation.

### 4. `conversations` Table
*   **Role:** Represents a "Room".
*   **Usage:** It's just a container. It doesn't know *who* is in it directly. It relies on the participants table.

### 5. `conversation_participants` Table
*   **Role:** The "Bridge" table (Many-to-Many).
*   **Join:** `conversation_id` -> `conversations.id` AND `user_id` -> `users.id`.
*   **Usage:**
    *   To find "My Chats": `SELECT * FROM participants WHERE user_id = ME`.
    *   To find "Direct Chat with Bob": Find a conversation where BOTH Me and Bob are participants.

### 6. `messages` Table
*   **Role:** The actual content.
*   **Join:** `conversation_id` -> `conversations.id` AND `sender_id` -> `users.id`.
*   **Usage:** Fetched when you open a chat window.

---

## Detailed Schema Reference

### 1. `users`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `username` | VARCHAR | Login ID / Email |
| `name` | VARCHAR | Display Name |
| `password` | VARCHAR | Plain text (for now) |
| `role` | VARCHAR | USER / ANONYMOUS |

### 2. `learning_entries`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `user_id` | VARCHAR | FK to Users |
| `topic` | VARCHAR | e.g., "Arrays" |
| `sub_topic` | VARCHAR | e.g., "Two Pointers" |
| `content` | TEXT | Large notes |
| `difficulty` | INT | 1-5 |
| `platform` | VARCHAR | e.g., "LeetCode" |
| `learning_date` | DATE | When it happened |
| `created_at` | TIMESTAMP | System time |

### 3. `friendships`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `requester_id` | VARCHAR | Who sent it |
| `addressee_id` | VARCHAR | Who received it |
| `status` | VARCHAR | PENDING / ACCEPTED |
| `created_at` | TIMESTAMP | System time |

### 4. `conversations`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `type` | VARCHAR | DIRECT / GROUP |
| `name` | VARCHAR | Group Name (Optional) |
| `last_message_content` | VARCHAR | Preview of last msg |
| `last_message_at` | TIMESTAMP | Time of last msg |
| `created_at` | TIMESTAMP | System time |

### 5. `conversation_participants`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `conversation_id` | VARCHAR | FK to Conversations |
| `user_id` | VARCHAR | FK to Users |
| `joined_at` | TIMESTAMP | System time |

### 6. `messages`
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | VARCHAR (UUID) | Primary Key |
| `conversation_id` | VARCHAR | FK to Conversations |
| `sender_id` | VARCHAR | FK to Users |
| `content` | TEXT | Message body |
| `type` | VARCHAR | TEXT / IMAGE |
| `status` | VARCHAR | SENT / READ |
| `sent_at` | TIMESTAMP | System time |

---

### Example Scenario: "Rajat chats with Rahul"

1.  **Friendship:** Rajat (`requester_id`) sends request to Rahul (`addressee_id`). Status becomes `ACCEPTED`.
2.  **Start Chat:** Frontend calls API. Backend checks `conversation_participants`. If no common room exists, it creates a new `conversation` (ID 101) and adds both as `participants`.
3.  **Send Message:** Rajat sends "Hello". Backend saves to `messages` with `conversation_id = 101` and `sender_id = Rajat`.
4.  **Real-Time:** Backend pushes message to `/topic/conversation.101`. Rahul (who is subscribed) sees it instantly.
5.  **Read Receipt:** Rahul opens chat. Backend updates `messages` status to `READ` where `conversation_id = 101` and `sender_id != Rahul`.

---

### Current Status
*   [x] Project Created
*   [x] Dependencies Added (pom.xml)
*   [x] Main Application Class Created
*   [x] First Endpoint Created (HelloController)
*   [x] CORS Configuration Created (WebConfig)
*   [x] User Model Created
*   [x] Auth Controller Created (Signup, Login, Anonymous)
*   [x] **Verified:** Signup endpoint works via curl!
*   [x] Database Dependencies Added (H2, JPA)
*   [x] User Entity Configured
*   [x] UserRepository Created
*   [x] AuthController Connected to DB
*   [x] **Verified:** Database persistence works (checked via H2 Console)
*   [x] **Verified:** Frontend successfully connected!
*   [x] "My Journey" Feature Implemented (Model, Repo, Controller)
*   [x] Export Feature Implemented (Excel & PDF with Selection)
*   [x] Dockerfile Created for Deployment
*   [x] **Deployed:** Live on Render!
*   [x] Leaderboard Feature Implemented (DTO, Repo Query, Controller)
*   [x] WebSocket Configured (Step 1 of Chat)
*   [x] Chat Models Created (Step 2 of Chat)
*   [x] Chat Controller Created (Step 3 of Chat)
*   [x] Friendship System Implemented (Phase 1 of Social)
*   [x] Advanced Chat Models & Repos Created (Phase 2 of Social)
*   [x] Advanced Chat Controller Created (Phase 2 of Social)
*   [x] Real-Time Private Chat & Presence Configured (Phase 3 of Social)
*   [x] User Search API Implemented
*   [x] Friendship DTO & Sent Requests Implemented
*   [x] Friend Stats (Total Logs) Added
*   [x] Read Receipts (Seen Feature) Implemented
*   [ ] Video Call (Skipped)
*   [x] Optimization: Display Name & Indexing Added
*   [x] Optimization: Last Message Preview Added
*   [x] Optimization: Database Indexes Added
*   [x] Optimization: UUID Migration Completed
