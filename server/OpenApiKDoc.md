# Universal KDoc Standard for Ktor OpenAPI & Scalar UI

> **Target Audience**: AI Agents & Backend Engineers  
> **Purpose**: Standardize KDoc formatting across any Ktor project to generate flawless, interactive, and beautifully categorized OpenAPI 3.0+ specifications rendered with **Scalar UI** and **Swagger UI**.

---

## 1. How It Works

The **Ktor OpenAPI Compiler Plugin** (`io.ktor.plugin.openapi`) traverses your `routing { ... }` trees during compilation, parses KDocs directly preceding HTTP verb blocks (`get`, `post`, `put`, `delete`, `patch`, `webSocket`), correlates request/response types with Kotlin `@Serializable` data classes, and compiles a complete `openapi.json` specification.

**Scalar UI** parses this specification and converts:
1. **Line 1 of KDoc** $\rightarrow$ Endpoint Title / Summary (displayed in navigation sidebars and command palette `Ctrl+K`).
2. **Markdown Body** $\rightarrow$ In-depth description panel (renders headers, lists, code blocks, and markdown callouts).
3. **Block Tags** (`@tags`, `@security`, `@path`, `@query`, `@response`) $\rightarrow$ Structured OpenAPI metadata, interactive auth panels, parameter sidebars, and component schemas.

---

## 2. Anatomical Blueprint of a Production KDoc

Every route KDoc **must** follow this strict top-to-bottom layout:

```kotlin
/**
 * Short Actionable Summary
 *
 * Detailed Markdown description explaining route behavior, business logic,
 * authorization requirements, and edge cases.
 *
 * ### Access Control & Constraints
 * * Requires a valid `Bearer` JSON Web Token.
 * * Rate-limited to 30 requests/min.
 *
 * @tags Primary Tag, Secondary Tag
 * @security BearerAuth
 * @path resourceId The unique UUID or string identifier of the entity.
 * @query page The page index to fetch (1-indexed).
 * @query limit Maximum number of records returned per page (default: 20).
 * @response 200 Entity retrieved successfully. [EntityResponseDto]
 * @response 400 Invalid query parameter or malformed identifier. [ApiError]
 * @response 401 Missing or expired authentication token. [ApiError]
 * @response 404 Entity not found for the given identifier. [ApiError]
 */
get("/resources/{resourceId}") { ... }
```

---

## 3. Tag Directives Reference

| Tag | Purpose | Best Practices & Rules | Example |
| :--- | :--- | :--- | :--- |
| **`Line 1`** | **Operation Summary** | • Capitalized, actionable sentence.<br>• Keep under 60 characters.<br>• Do NOT include markdown styling or punctuation at the end. | `Fetch authenticated user profile` |
| **`Markdown Body`** | **Operation Description** | • Use standard Markdown (bold, lists, backticks).<br>• Split into sections (`### Access Control`, `### Notes`).<br>• Document required headers or workflow context. | `Retrieves the full profile details including skills and portfolio.` |
| **`@tags`** | **Feature Categorization** | • Groups endpoints into collapsible categories in the Scalar sidebar.<br>• Use PascalCase with spaces (e.g. `Users & Profiles`, `Authentication`).<br>• Comma-separate multiple tags if an endpoint belongs to multiple domains. | `@tags Authentication, Identity Verification` |
| **`@security`** | **Security Schemes** | • Activates the interactive **Authorize** modal in Scalar.<br>• Match the exact security scheme name configured in Ktor (e.g., `BearerAuth`, `AdminAuth`, `ApiKeyAuth`).<br>• Omit on public routes. | `@security BearerAuth` |
| **`@path`** | **Path Parameters** | • Document every `{param}` in the route path.<br>• Keep explanations short (< 15 words) for Scalar's compact parameter table. | `@path userId The unique user identifier.` |
| **`@query`** | **Query Parameters** | • Document all supported query parameters.<br>• Mention default values or boundaries if applicable.<br>• Keep explanations short (< 15 words). | `@query limit Maximum records to return (default: 20).` |
| **`@response`** | **HTTP Status & Schema** | • Format: `@response <StatusCode> <Description>. [DtoClassName]`<br>• Always wrap data class names in brackets `[ClassName]`.<br>• For collections, use `[List<DtoClassName>]`.<br>• Document unique business responses (200, 201, 400, 404, 403). | `@response 200 User profile payload. [UserProfileDto]` |

---

## 4. DTO Reference & Schema Generation Rules

### 1. Bracket Notation `[ClassName]`
Do **not** write raw JSON examples in the KDoc text. When you reference a class in brackets (e.g., `[UserProfileDto]`), the Ktor OpenAPI plugin extracts the class properties, nullability, and serializers to generate interactive JSON examples and component schema tables automatically.

```kotlin
// ✅ CORRECT: Scalar renders dynamic JSON playground and model schema
@response 200 Successful response. [UserProfileDto]

// ❌ WRONG: Hardcoded text that breaks when the model changes
@response 200 Returns {"id": "123", "name": "John"}
```

### 2. Multi-Part & Binary Uploads
For multipart endpoints (file uploads, image verifications), document the accepted parts in the description and use `[ResponseDto]` for the response:

```kotlin
/**
 * Upload chat file attachment
 *
 * Accepts a multi-part binary payload containing `file`, `filename`, and `content_type`.
 * Uploads payload to secure object storage and returns the CDN URL.
 *
 * @tags Direct Messaging
 * @security BearerAuth
 * @response 200 File uploaded successfully. [AttachmentUploadResponse]
 * @response 400 Missing file binary payload. [ApiError]
 * @response 401 Missing or invalid authentication token. [ApiError]
 */
post("/attachment") { ... }
```

### 3. WebSocket Endpoints
Document WebSocket routes with `@tags` and `@security` so developers know the connection requirements and protocol:

```kotlin
/**
 * Real-time chat WebSocket
 *
 * Establishes persistent bidirectional WebSocket connection for instant messaging,
 * typing indicators, presence events, and read receipts.
 *
 * @tags Direct Messaging
 * @security BearerAuth
 */
webSocket("/chat") { ... }
```

---

## 5. Universal Rules of Thumb for AI Agents

1. **Every Route Must Have a KDoc**: Never leave a route undocumented. If a route exists in the routing tree, add a complete KDoc.
2. **First Line is Summary**: Never put `@tags` or paragraphs on line 1. Line 1 is strictly the summary title.
3. **Group by Feature Tags**: Always assign consistent `@tags` to cluster related routes together (e.g. all auth routes under `@tags Authentication`).
4. **Use `@security BearerAuth` on Protected Routes**: Any route wrapped in `authenticate("local-jwt")` or equivalent must have `@security BearerAuth`.
5. **Concise Parameter Explanations**: Keep `@path` and `@query` descriptions under 15 words to avoid layout wrapping in Scalar UI.
6. **Consistent Error Modeling**: Reference a common error schema (e.g. `[ApiError]`) across 400/401/403/404 responses.

---

## 6. Complete Real-World Route Patterns

### Pattern A: Public Search & Filter Route (GET)
```kotlin
/**
 * Browse and search projects
 *
 * Fetches a paginated list of project contracts with filtering by keyword search, category, and sorting criteria.
 *
 * @tags Projects & Bounties
 * @query query Keyword search string for project title or description.
 * @query category Category filter (e.g. Mobile, Backend, AI).
 * @query sortBy Sorting order (e.g. newest, budget_high, budget_low).
 * @query page The page index to fetch (1-indexed).
 * @query limit Maximum number of project records per page.
 * @response 200 Paginated list of projects. [List<ProjectDto>]
 * @response 400 Invalid filter parameters. [ApiError]
 */
get("/projects") { ... }
```

### Pattern B: Authenticated Resource Creation (POST with 201)
```kotlin
/**
 * Submit proposal bid for project
 *
 * Submits a formal freelancer proposal bid for a project posting with cover letter, milestone budget breakdown, and estimated delivery days.
 *
 * @tags Proposals & Escrow
 * @security BearerAuth
 * @response 201 Proposal created and submitted. [ProposalDto]
 * @response 400 Invalid proposal payload or duplicate submission. [ApiError]
 * @response 401 Missing or invalid authentication token. [ApiError]
 */
post("/proposals") { ... }
```

### Pattern C: Entity Mutation by ID (PUT / PATCH)
```kotlin
/**
 * Update project posting
 *
 * Modifies the title, description, budget, category, or milestone list of an existing project posting. Restricted to the project owner.
 *
 * @tags Projects & Bounties
 * @security BearerAuth
 * @path id The unique project identifier.
 * @response 200 Project updated successfully. [ProjectDetailDto]
 * @response 400 Invalid update payload or missing id. [ApiError]
 * @response 401 Missing or invalid authentication token. [ApiError]
 * @response 404 Project record not found. [ApiError]
 */
put("/projects/{id}") { ... }
```

### Pattern D: Soft / Hard Deletion by ID (DELETE)
```kotlin
/**
 * Deactivate user account
 *
 * Deactivates and soft-deletes the authenticated user account and revokes active sessions.
 *
 * @tags Users & Profiles
 * @security BearerAuth
 * @response 200 Account deactivated successfully. [CommonResponse]
 * @response 401 Missing or invalid authentication token. [ApiError]
 */
delete("/users/account") { ... }
```

### Pattern E: Role-Restricted Admin Route
```kotlin
/**
 * Submit KYC manual review decision
 *
 * Records an administrative approval or rejection decision with reviewer audit notes. Restricted to administrators.
 *
 * @tags OpenBiometrics KYC, Admin Operations
 * @security BearerAuth
 * @path id The unique verification session identifier.
 * @response 200 Updated verification status. [KycStatusResponse]
 * @response 400 Missing verification id. [ApiError]
 * @response 401 Missing or invalid authentication token. [ApiError]
 * @response 403 Admin privileges required. [ApiError]
 * @response 404 Verification record not found. [ApiError]
 */
post("/kyc/admin/{id}/review") { ... }
```

---

## 7. Mounting Scalar UI in Ktor

To serve this documentation interactively with Scalar in your Ktor application:

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation(files("libs/ktor-server-scalar-jvm-3.6.0-SNAPSHOT.jar")) // or official scalar dependency
}

ktor {
    openApi {
        enabled = true
        codeInferenceEnabled = true
        onlyCommented = false
    }
}
```

```kotlin
// Application routing configuration
routing {
    scalarUI("/scalarUI") {
        info = OpenApiInfo("Zapmancer Platform API", "1.0.0")
        source = OpenApiDocSource.Routing(
            contentType = ContentType.Application.Json,
        )
        theme = "purple"
        layout = "modern"
    }
}
```
Access the interactive documentation playground at **`http://localhost:8080/scalarUI`**.
