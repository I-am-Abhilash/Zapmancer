# Final Backend API Completeness & Endpoint Audit

Perform a **complete, production-level audit of the entire backend API**. Do not modify code yet. Your first job is to inspect the existing implementation and determine whether the API is complete, consistent, and logically usable.

## 1. Inventory Every Resource

First, identify every major backend resource/entity/module in the project.

For each resource, determine:

* What is the resource?
* Who can create it?
* Who can read it?
* Who can update it?
* Who can delete it?
* Is it user-owned, admin-owned, or system-managed?
* Does it have relationships with other resources?
* Are there lifecycle/state transitions?

Create an internal endpoint matrix for every resource.

For example:

| Resource | Create | Get One | List | Update | Delete | Special Actions  |
| -------- | ------ | ------- | ---- | ------ | ------ | ---------------- |
| User     | ✓      | ✓       | ✓    | ✓      | ?      | suspend, restore |
| Project  | ✓      | ✓       | ✓    | ✓      | ?      | publish, archive |
| Message  | ✓      | ✓       | ✓    | ✗      | ?      | mark-read        |
| Review   | ✓      | ✓       | ✓    | ?      | ?      | approve          |

Do **not** assume every resource needs traditional CRUD. Determine what operations actually make sense from the domain.

---

## 2. Detect Missing CRUD Operations

For every resource, explicitly check whether the following operations are required:

* `POST` — create
* `GET /{id}` — retrieve one
* `GET` — list/search
* `PUT` or `PATCH` — update
* `DELETE` — delete

Flag cases such as:

* Create exists but there is no way to retrieve the created resource.
* Create exists but update is missing even though the resource is editable.
* Create exists but delete/archive/deactivation is missing where users should reasonably be able to remove it.
* List exists but retrieving a specific resource is impossible.
* Update exists but there is no create operation where creation should be possible.
* Delete exists but there is no appropriate soft-delete/archive mechanism where required.
* A resource has CRUD operations but important domain-specific actions are missing.

Pay particular attention to **asymmetric APIs** such as:

> create exists → read exists → update missing → delete missing

Determine whether the asymmetry is intentional or an actual implementation gap.

---

## 3. Audit Relationships Between Resources

Inspect relationships and verify that required operations exist across them.

For example:

* User → Projects
* Project → Proposals
* Project → Contracts
* Contract → Milestones
* User → Reviews
* Conversation → Messages
* Restaurant → Menu Items
* Order → Order Items

For every relationship ask:

* Can the parent retrieve its children?
* Can the child be retrieved independently when appropriate?
* Can children be created?
* Can children be updated?
* Can children be removed?
* Are relationship-specific operations required?
* Are there orphaned resources that cannot be managed?
* Are there endpoints that expose a resource but no way to manage its lifecycle?

Look specifically for missing nested/resource endpoints.

---

## 4. Audit Domain-Specific Actions

Do not limit the audit to CRUD.

Look for business operations that should exist but currently do not.

Examples:

* publish / unpublish
* activate / deactivate
* archive / restore
* approve / reject
* accept / decline
* cancel
* suspend / unsuspend
* block / unblock
* follow / unfollow
* favorite / unfavorite
* mark-read / mark-unread
* submit / withdraw
* start / complete
* assign / unassign
* invite / remove
* verify / reject verification
* apply / withdraw application

Determine these from the **actual domain model and existing code**, not from generic assumptions.

---

## 5. Audit HTTP Semantics

Verify that endpoints use appropriate HTTP methods.

Look for mistakes such as:

* `POST` being used for ordinary updates unnecessarily.
* `GET` modifying server state.
* `DELETE` being used for non-deletion actions.
* `PUT`/`PATCH` semantics being inconsistent.
* Missing path parameters.
* Important identifiers being passed incorrectly.
* Inconsistent endpoint naming.

Check that similar operations follow a consistent convention throughout the project.

---

## 6. Audit Authorization

For every endpoint verify:

* Authentication requirement.
* Authorization requirement.
* Resource ownership checks.
* Admin-only operations.
* Role-based access.
* Whether users can access another user's resources accidentally.
* Whether update/delete operations enforce ownership.
* Whether nested resources verify the parent relationship.

Pay special attention to:

> `GET /resource/{id}`
> `PUT /resource/{id}`
> `DELETE /resource/{id}`

These are common locations for IDOR/authorization bugs.

---

## 7. Audit API Consistency

Compare similar endpoints throughout the backend.

Look for inconsistencies in:

* URL naming
* plural vs singular resource names
* path structure
* HTTP methods
* request DTOs
* response DTOs
* status codes
* pagination
* filtering
* sorting
* error responses
* authentication
* authorization
* validation

If two resources perform essentially the same operation but expose completely different API conventions, flag it.

---

## 8. Audit Pagination, Search & Filtering

For every potentially large collection endpoint determine whether it needs:

* pagination
* limit
* offset/cursor
* sorting
* filtering
* search
* date filtering
* status filtering

Flag endpoints that return potentially unbounded datasets.

Also verify that pagination conventions are consistent across the API.

---

## 9. Audit Error & Status-Code Behavior

Verify that endpoints return appropriate status codes.

Check at minimum:

* `200 OK`
* `201 Created`
* `204 No Content`
* `400 Bad Request`
* `401 Unauthorized`
* `403 Forbidden`
* `404 Not Found`
* `409 Conflict`
* `422 Unprocessable Entity` where appropriate
* `500` only for unexpected server failures

Look for endpoints that incorrectly return `200` for every situation.

---

## 10. Audit Validation

For every create/update endpoint inspect:

* Required fields
* Optional fields
* String length limits
* Numeric ranges
* Enum validation
* ID validation
* Business rules
* Duplicate handling
* Invalid state transitions

Determine whether validation exists at the appropriate layer.

---

## 11. Audit Lifecycle Completeness

For every important entity determine its complete lifecycle.

Example:

```text
CREATED
   ↓
ACTIVE
   ↓
PAUSED
   ↓
COMPLETED
   ↓
ARCHIVED
```

Check whether the API provides the necessary operations to move an entity through its legitimate lifecycle.

Identify states that can be entered but cannot be exited.

Also identify operations that can produce impossible or invalid states.

---

## 12. Audit Authentication & Account Management

Inspect the complete authentication/account lifecycle.

Check for appropriate endpoints for things such as:

* registration
* login
* logout/session invalidation
* token refresh
* email verification
* phone verification
* password change
* password reset
* account retrieval
* profile update
* account deletion
* account deactivation
* account restoration where appropriate

Do not automatically assume every feature is required. Determine what the current application's requirements imply.

---

## 13. Audit Tests Against the API

Inspect existing tests and determine:

* Which endpoints have tests?
* Which endpoints have no tests?
* Are happy paths tested?
* Are unauthorized requests tested?
* Are forbidden requests tested?
* Are not-found cases tested?
* Are validation failures tested?
* Are duplicate/conflict cases tested?
* Are delete operations actually tested?
* Are update operations actually tested?

Identify important endpoints that have zero meaningful coverage.

---

# Final Deliverable

After inspecting the entire backend, produce a report with these sections:

## A. Complete Endpoint Inventory

List every existing endpoint grouped by resource.

## B. Missing Endpoints

List every endpoint that appears to be genuinely missing.

For each one provide:

* Resource
* Recommended HTTP method
* Recommended path
* Purpose
* Why it is required
* Priority: `CRITICAL`, `HIGH`, `MEDIUM`, or `LOW`

## C. Suspicious Asymmetries

Examples:

* Create without update
* Update without delete
* Delete without retrieval
* Nested resource without independent retrieval
* Resource accessible through one relationship but not another

For each, explain whether it is probably intentional or likely a bug.

## D. Missing Domain Operations

List business actions that appear to be missing beyond CRUD.

## E. Security/Authorization Gaps

List potential authentication, authorization, ownership, or IDOR issues.

## F. API Consistency Problems

List inconsistent naming, HTTP methods, response formats, status codes, pagination, validation, etc.

## G. Test Coverage Gaps

List important endpoints and behaviors that are not adequately tested.

## H. Overall Completeness Score

Give the backend a score from **0–100** based on:

* Endpoint completeness
* CRUD completeness
* Domain lifecycle completeness
* Security
* Validation
* API consistency
* Error handling
* Testing

Explain the score.

---

# Important Rules

**Do not blindly add endpoints just to make every resource have CRUD.**

Some resources intentionally should not have all CRUD operations.

For every missing operation, determine whether it is actually required by the application's domain.

**Do not modify the code during this audit.**

First inspect:

* routing
* controllers/routes
* services
* repositories
* DTOs
* database entities
* authentication
* authorization
* tests
* frontend/API clients if available

Use the existing implementation to understand the intended API.

If the frontend/client calls an endpoint that the backend does not implement, flag it as a **critical integration gap**.

If the backend implements an endpoint that appears impossible for the frontend/client to use, flag that as well.

Do not stop after inspecting the obvious routes. Trace the entire application architecture and perform a **final exhaustive endpoint completeness audit**.

The goal is to answer:

> **"If this backend were released to production today, what API operations are missing, inconsistent, insecure, or impossible for users to perform?"**

Do not make assumptions silently. Clearly distinguish:

* **Confirmed missing**
* **Probably missing**
* **Potentially intentional**
* **No issue found**
