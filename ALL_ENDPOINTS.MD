# ScriptSide API Documentation

This document provides a comprehensive list of all available endpoints in the ScriptSide backend.

## Base Configuration

- **Base URL**: `http://localhost:8080` (Development)
- **Content-Type**: `application/json`
- **Response Wrapper**: All responses follow a standardized format:

```json
{
  "success": true,
  "data": { ... },
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable message"
  }
}
```

## Authentication

The backend uses JWT (JSON Web Tokens). Some endpoints require an `Authorization` header:
`Authorization: Bearer <your_access_token>`

### 1. Signup
- **URL**: `/auth/signup`
- **Method**: `POST`
- **Body**:
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "username": "testuser"
}
```
- **Response**: `AuthResponse` (contains `accessToken` and `refreshToken`)

### 2. Login
- **URL**: `/auth/login`
- **Method**: `POST`
- **Body**:
```json
{
  "email": "user@example.com",
  "password": "Password123!"
}
```
- **Response**: `AuthResponse`

### 3. Refresh Token
- **URL**: `/auth/refresh`
- **Method**: `POST`
- **Body**:
```json
{
  "refreshToken": "your_refresh_token_here"
}
```
- **Response**: `AuthResponse` (new token pair)

### 4. OTP Request
- **URL**: `/auth/otp/request`
- **Method**: `POST`
- **Body**:
```json
{
  "identifier": "+1234567890",
  "type": "PHONE" 
}
```
*Note: Type can be "PHONE" or "EMAIL".*

### 5. OTP Verify
- **URL**: `/auth/otp/verify`
- **Method**: `POST`
- **Body**:
```json
{
  "identifier": "+1234567890",
  "code": "123456",
  "sessionInfo": "session_info_from_request"
}
```
- **Response**: `AuthResponse`

## Articles

### 1. Search/List Articles
- **URL**: `/articles`
- **Method**: `GET`
- **Query Params**: 
    - `q` or `search`: Full-text search (TSVector)
    - `category`: Filter by category
    - `tag`: Filter by specific tag
    - `page`: Page number (default 1)
    - `size`: Items per page (default 20)
- **Response**: `List<ArticleResponse>`

### 2. Get Article Detail
- **URL**: `/articles/{id}`
- **Method**: `GET`
- **Response**: `ArticleResponse` (includes `commentsCount` and `recentComments` list)

### 3. Create Article
- **URL**: `/articles/create`
- **Method**: `POST` (Auth required)
- **Body**: `CreateArticleRequest` (title, content, tags, category, thumbnailUrl)

### 4. Update Article
- **URL**: `/articles/{id}`
- **Method**: `PUT` (Auth required)
- **Body**: `UpdateArticleRequest` (all fields optional)

### 5. Delete Article
- **URL**: `/articles/{id}`
- **Method**: `DELETE` (Auth required)

### 6. Following Feed
- **URL**: `/articles/feed`
- **Method**: `GET` (Auth required)
- **Description**: Chronological feed of articles from people you follow.

### 7. Trending Articles
- **URL**: `/articles/trending`
- **Method**: `GET`
- **Description**: Top articles fetched from Gorse.

### 8. Recommended Articles
- **URL**: `/articles/recommended`
- **Method**: `GET` (Auth required)
- **Description**: Personalized AI recommendations from Gorse.

### 9. Engagement
- **POST /articles/{id}/clap**: Like an article. (Auth required)
- **POST /articles/{id}/bookmark**: Toggle bookmark. (Auth required)
- **POST /articles/{id}/read**: Record that the user finished reading. (Auth required)

### 10. Comments
- **POST /articles/{id}/comments**: Post a comment. (Auth required)
- **GET /articles/{id}/comments**: Fetch all comments for an article.

### 11. Tags
- **GET /articles/tags/popular**: Fetch most used tags across the platform.

## Users

### 1. User Profile
- **GET /users/{userId}/profile**: Fetch a user's public profile and stats.
- **GET /users/me**: Fetch current user's profile. (Auth required)

### 2. Update Profile
- **PUT /users/profile**: Update your avatar, bio, or username. (Auth required)

### 3. Search Users
- **GET /users/search?q=...**: Find other users to follow.

### 4. Follow System
- **POST /users/{userId}/follow**: Toggle follow/unfollow. (Auth required)

### 5. Notifications
- **GET /users/notifications**: Fetch all notifications. (Auth required)
- **POST /users/notifications/{id}/read**: Mark a notification as read. (Auth required)

### 6. Delete Account
- **DELETE /users/me**: Soft delete account with cooling period. (Auth required)

## Storage

### 1. Upload Image
- **URL**: `/articles/upload`
- **Method**: `POST` (Auth required)
- **Body**: `Multipart` (form data with file)
- **Response**: `ApiResponse` with `imageUrl`

## Monitoring & Health

- **GET /health**: Returns "OK" if the service is up.
- **GET /metrics**: Prometheus-formatted metrics (Micrometer).
- **GET /swagger**: Open API Documentation UI.

## DTO Models

### ArticleResponse
```json
{
  "id": 0,
  "title": "String",
  "content": "HTML String (Sanitized Markdown)",
  "authorName": "String",
  "authorAvatarUrl": "String?",
  "thumbnailUrl": "String?",
  "readingTimeMinutes": 5,
  "publishedDate": "ISO8601 String",
  "category": "String",
  "tags": ["tag1", "tag2"],
  "likesCount": 0,
  "commentsCount": 0,
  "viewsCount": 0,
  "recentComments": [],
  "status": "PUBLISHED",
  "authorId": "String"
}
```

### NotificationResponse
```json
{
  "id": 0,
  "type": "FOLLOW | CLAP | COMMENT | NEW_ARTICLE",
  "actorId": "String",
  "actorName": "String",
  "actorAvatarUrl": "String?",
  "targetId": "String? (Article ID or Comment ID)",
  "isRead": false,
  "createdAt": "ISO8601 String"
}
```
