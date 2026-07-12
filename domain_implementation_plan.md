# Domain Module Improvement Plan

> **Target module:** `feature/domain/src/commonMain/kotlin/com/smach/zapmancer/domain/`  
> **Companion plans:** `implementation_plan.md` (UI), `viewmodel_implementation_plan.md` (VMs).  
> **Purpose:** Document every issue in domain models, repository contracts, and use cases so a
> lower-power AI model (or developer) can execute targeted, incremental fixes.

---

## 0. Guiding Principles

| Rule                                                                            | Rationale                                                                                             |
|---------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| **The domain layer must not import from `core.common.dto`**                     | DTOs belong to the data layer; importing them into domain creates upward dependency leakage           |
| **All types must be plain Kotlin (`data class` / `enum`) — no `@Serializable`** | Serialization is a data-layer concern; annotating domain models couples them to kotlinx.serialization |
| **Repository interfaces return domain models only**                             | The data layer maps DTOs → domain models; the domain layer never sees raw DTO fields                  |
| **Use cases are single-responsibility and `operator fun invoke`**               | `UpdateSettingsUseCase` is the sole exception to the pattern — it wraps multiple operations           |
| **One test file per use case**                                                  | Currently only `LoginUseCaseTest` exists; every use case must be independently testable               |

---

## 1. Domain Models

### 1-A. Remove `@Serializable` from all domain models

**All** domain model files carry `@Serializable` from kotlinx.serialization. This couples the domain
layer directly to the serialization library and is the data layer's responsibility.

**Files affected** (every model file):

| File               | Action                                                                                                                    |
|--------------------|---------------------------------------------------------------------------------------------------------------------------|
| `Project.kt`       | Remove `import kotlinx.serialization.Serializable` and `@Serializable` from `Project`, `ProjectCategory`, `ProjectStatus` |
| `ProjectDetail.kt` | Same                                                                                                                      |
| `UserProfile.kt`   | Remove from `UserProfile`, `PortfolioItem`, `ProfileReview`                                                               |
| `User.kt`          | Remove from `User`                                                                                                        |
| `HomeDashboard.kt` | Remove from `HomeDashboard`, `EarningStats`, `ProjectStats`, `UserActivity`, `ActivityStatus`                             |
| `Notification.kt`  | Remove from `NotificationItem`, `NotificationType`, `NotificationAction`                                                  |
| `Conversation.kt`  | Remove from `ConversationItem`                                                                                            |
| `Message.kt`       | Remove from `MessageItem`, `MessageStatus`                                                                                |
| `Proposal.kt`      | Remove from `Proposal`                                                                                                    |
| `SettingsData.kt`  | Remove from `SettingsData`                                                                                                |

> The data-layer repository implementations should apply `@Serializable` to their own DTO types,
> which already exist in `core/common/dto/`.

### 1-B. Fix misaligned nullability in domain models vs. DTOs

Several domain model fields are non-nullable `String` when the DTO (and therefore the API) returns
nullable values. This causes the repository implementation to use `.orEmpty()` or force-unwrap
silently.

| Model           | Field             | Current type | Correct type | Rationale                                                                         |
|-----------------|-------------------|--------------|--------------|-----------------------------------------------------------------------------------|
| `UserProfile`   | `location`        | `String`     | `String?`    | DTO `UserDtos.UserProfile.location` is `String?`                                  |
| `UserProfile`   | `experience`      | `String`     | `String?`    | DTO is `String?`                                                                  |
| `UserProfile`   | `about`           | `String`     | `String?`    | DTO is `String?`                                                                  |
| `UserProfile`   | `ranking`         | `String`     | `String?`    | DTO is `String?`                                                                  |
| `PortfolioItem` | `id`              | `String`     | `Int`        | DTO `PortfolioItem.id` is `Int`; type mismatch causes a silent mapping conversion |
| `PortfolioItem` | `imageUrl`        | `String`     | `String?`    | DTO is `String?`                                                                  |
| `PortfolioItem` | `description`     | `String`     | `String?`    | DTO is `String?`                                                                  |
| `ProfileReview` | `authorAvatarUrl` | `String?`    | `String?`    | Already nullable — correct; verify repository uses it without `.orEmpty()`        |
| `ProjectDetail` | `projectScope`    | `String`     | `String?`    | DTO is `String?` (nullable from server)                                           |
| `ProjectDetail` | `timeline`        | `String`     | `String?`    | DTO is `String?`                                                                  |
| `ProjectDetail` | `estStart`        | `String`     | `String?`    | DTO is `String?`                                                                  |

### 1-C. `Project.kt` — structural issues

| #   | Issue                                                                                                                                                                    | Fix                                                                                                                                                                                                                 |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| C-1 | `ProjectCategory` enum is declared with **extra indentation** inside `Project.kt` but at file scope — formatting inconsistency                                           | Move `ProjectCategory` out of any nesting and ensure consistent indentation; ideally split into its own file `ProjectCategory.kt` since it is referenced by `SearchUiState`, `ProjectListUiState`, and screen files |
| C-2 | `ProjectCategory.ALL` is a domain enum value representing "no filter applied" — this is a **presentation concern** (the filter chip shows "All") not a business category | Remove `ALL` from the domain enum; create a UI-layer `SelectedCategory` sealed type `(All                                                                                                                           | Specific(category: ProjectCategory))` in `SearchUiState` / `ProjectListUiState` |
| C-3 | `Project.showImagePlaceholder` is a UI rendering hint living in the domain model                                                                                         | Move to `ProjectUiModel` in the presentation layer (aligns with UI plan task PL-7)                                                                                                                                  |
| C-4 | `Project.footerText` is a formatted display string (e.g. "Milestone 2/4") living in the domain model                                                                     | Move to `ProjectUiModel`; the domain `Project` should expose raw fields (`milestoneCurrent: Int?`, `milestoneTotal: Int?`)                                                                                          |
| C-5 | `ProjectStatus.DONE` — "Done" is ambiguous (completed by all parties? closed by client?)                                                                                 | Rename to `COMPLETED` for clarity; update all call sites                                                                                                                                                            |

### 1-D. `HomeDashboard.kt` — structural issues

| #   | Issue                                                                                                                                                                                           | Fix                                                                                                                                                                                                     |
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| D-1 | `EarningStats.amount` and `EarningStats.growthPercentage` are `String` — monetary/percentage values as strings cannot be compared or formatted without parsing                                  | Change `amount` to `Double` (raw value); let the presentation layer format it as currency. Same for `growthPercentage` → `Double`                                                                       |
| D-2 | `HomeDashboard` has no client-mode equivalent stats (`totalSpent`, `activeJobPostsCount`, `proposalsReceivedCount`) — these are referenced in `HomeUiState` but never populated from the domain | Add `clientStats: ClientDashboardStats?` to `HomeDashboard` (nullable — only populated for client accounts) with fields `totalSpent: Double`, `activeJobPostsCount: Int`, `proposalsReceivedCount: Int` |
| D-3 | `UserActivity.category` is `String` while `Project.category` is `ProjectCategory` enum — the same concept has inconsistent types                                                                | Change `UserActivity.category` to `ProjectCategory`                                                                                                                                                     |
| D-4 | `UserActivity.timestamp` is `String` — timestamps should use `kotlinx.datetime.Instant` or `Long` (epoch ms) for correct sorting and display                                                    | Change to `Long` (epoch milliseconds) and let the presentation layer format for display                                                                                                                 |
| D-5 | `ActivityStatus` enum lives in `HomeDashboard.kt` alongside `HomeDashboard`, `EarningStats`, `ProjectStats`, `UserActivity` — 4 unrelated types in one file                                     | Split into separate files: `UserActivity.kt`, `ActivityStatus.kt`, `EarningStats.kt` (or nest them logically)                                                                                           |

### 1-E. `Proposal.kt` — missing fields

| #   | Issue                                                                                                                                           | Fix                                                                                                                                                                    |
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| E-1 | `Proposal` has no `id` field                                                                                                                    | The DTO `core.common.dto.Proposal` has `id: Int`; add `id: String = ""` to the domain model (or `id: String?` for new proposals)                                       |
| E-2 | `Proposal` has no `projectId` field                                                                                                             | `SubmitProposalRequest` in the DTO layer has `projectId: String`; the domain `Proposal` must carry this to avoid requiring the repository to have a separate parameter |
| E-3 | `Proposal` is used both as a **submission request** and as a **received proposal** in `ClientProposalsViewModel` — these are different concepts | Split into `ProposalRequest` (for submission — outgoing) and `ProposalItem` (for listing — incoming with author/status info)                                           |

### 1-F. `Notification.kt` — missing state

| #   | Issue                                                                                                                                                     | Fix                                                                                   |
|-----|-----------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| F-1 | `NotificationItem` has no `isRead: Boolean` field — read/unread state cannot be tracked in the model                                                      | Add `isRead: Boolean = false`                                                         |
| F-2 | `NotificationType` has a catch-all `GENERAL` value — any notification that doesn't map to a known type falls into `GENERAL`, hiding classification errors | Add a dedicated `UNKNOWN` fallback and deprecate `GENERAL` after migrating call sites |

### 1-G. `User.kt` — token exposure

| #   | Issue                                                                                                                                                                      | Fix                                                                                                                                                                            |
|-----|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| G-1 | `User.accessToken` and `User.refreshToken` live on the domain model — tokens are infrastructure / session-management concerns and should not flow through the domain layer | Remove token fields from `User`; the repository implementation stores them via `SessionManager` after successful login. The use case returns only `User(id, email, isNewUser)` |

### 1-H. `SettingsData.kt` — missing fields

| #   | Issue                                                                                                                                                                   | Fix                                                                                                                                                     |
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| H-1 | `SettingsData.isDarkModeEnabled` is a **local device preference**, not a server-persisted setting — it is already treated locally in `SettingsViewModel.toggleDarkMode` | Move `isDarkModeEnabled` out of `SettingsData` and into a separate `LocalPreferences` model fetched from `DataStore` directly (not from the server API) |

---

## 2. Repository Interfaces

### 2-A. `ProfileRepository` — leaks DTO type in contract

```kotlin
// CURRENT
suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfile, DataError.Network>
```

`UpdateProfileRequest` is defined in `core.common.dto` — a data-layer package. The domain repository
contract must not import from the data layer.

**Fix:** Create `domain/model/UpdateProfileParams.kt` with a plain data class:

```kotlin
data class UpdateProfileParams(
    val name: String?,
    val roleTitle: String?,
    val location: String?,
    val about: String?,
    val experience: String?,
    val skills: List<String>?,
    val avatarUrl: String?,
)
```

Then change `ProfileRepository.updateProfile(request: UpdateProfileParams)`. The data-layer
implementation converts `UpdateProfileParams → UpdateProfileRequest` (DTO) before the HTTP call.

### 2-B. `ProjectRepository` — `postProject` takes a domain model as input

```kotlin
suspend fun postProject(project: ProjectDetail): Result<Unit, DataError.Network>
```

`ProjectDetail` is the **read** model returned by `getProjectDetail`. Using it as the **write**
model for `postProject` forces callers to construct a `ProjectDetail` with meaningless placeholder
fields (`id = ""`, `postedTime = "Just now"`, etc. — as seen in `PostProjectViewModel`).

**Fix:** Create `domain/model/CreateProjectParams.kt`:

```kotlin
data class CreateProjectParams(
    val category: String,
    val title: String,
    val description: String,
    val budgetRange: String,
    val timeline: String,
    val deliverables: List<String>,
    val skills: List<String>,
)
```

Update repository signature:
`suspend fun postProject(params: CreateProjectParams): Result<Unit, DataError.Network>`.

### 2-C. `ProjectRepository` — missing server-side search parameters

```kotlin
// CURRENT
suspend fun getProjects(): Result<List<Project>, DataError.Network>
```

The backend supports `query`, `category`, `sortBy`, and `page` parameters (see conversation
#50889808 — the search backend task). The domain contract must expose these so `SearchViewModel` can
migrate off client-side filtering (VM plan task SK-1).

**Fix:** Add an overload or update the signature:

```kotlin
suspend fun getProjects(
    query: String? = null,
    category: ProjectCategory? = null,
    sortBy: String? = null,
    page: Int = 1,
    pageSize: Int = 20,
): Result<List<Project>, DataError.Network>
```

### 2-D. `AuthRepository.saveTokens` — domain layer exposing an infrastructure operation

```kotlin
suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit, DataError.Network>
```

Saving session tokens is a data/infrastructure concern handled by `SessionManager`. This method
should not be in the domain repository interface.

**Fix:** Remove `saveTokens` from `AuthRepository`. The data-layer `AuthRepositoryImpl.login()`
implementation calls `SessionManager.saveSession(...)` directly. No use case should ever need to
manually trigger token saves.

### 2-E. `SettingsRepository.updateDarkMode` — missing method

Dark mode toggle is handled locally in `SettingsViewModel` by bypassing the repository entirely. If
the local preference must persist across sessions, a dedicated method is needed.

**Fix:** Add `suspend fun updateDarkMode(enabled: Boolean)` to `SettingsRepository` (or, better, a
separate `LocalPreferencesRepository`) that writes to `DataStore` without an HTTP call. This keeps
the repository contract honest about where each setting is persisted.

### 2-F. `MessageRepository.getMessages` — no error channel

```kotlin
// CURRENT
fun getMessages(conversationId: String): Flow<List<MessageItem>>
```

A `Flow` with no error type means errors are emitted as exceptions on the flow. Callers must use
`catch` or risk uncaught coroutine exceptions. This is inconsistent with every other repository
method which returns `Result`.

**Fix options (choose one):**

- Change to `Flow<Result<List<MessageItem>, DataError.Network>>` — callers get typed errors.
- Keep `Flow<List<MessageItem>>` but document that implementations must catch internally and emit an
  empty list + log; and add a separate suspend
  `fun getMessagesOrError(...): Result<List<MessageItem>, DataError.Network>` for the initial load.

Recommended: option 1 — consistent with the rest of the codebase.

---

## 3. Use Cases

### 3-A. `UpdateSettingsUseCase` — violates single-responsibility

```kotlin
class UpdateSettingsUseCase(private val repository: SettingsRepository) {
    suspend fun updateTwoFactor(enabled: Boolean): ...
    suspend fun updateEmailNotifications(enabled: Boolean): ...
    suspend fun updateClientMode(enabled: Boolean): ...
}
```

This is the **only** use case that is not `operator fun invoke`. It bundles three independent
settings operations into one class, and it does not follow the `operator fun invoke` convention.

**Fix:** Split into three separate use cases:

- `UpdateTwoFactorUseCase` → `operator fun invoke(enabled: Boolean)`
- `UpdateEmailNotificationsUseCase` → `operator fun invoke(enabled: Boolean)`
- `UpdateClientModeUseCase` → `operator fun invoke(enabled: Boolean)`

Update `SettingsViewModel` and DI module after splitting.

### 3-B. `ExportActivityCsvUseCase` — wrong error type for empty-list guard

```kotlin
if (activities.isEmpty()) {
    return Result.Error(DataError.Network.CLIENT_ERROR)
}
```

`CLIENT_ERROR` is a network error code returned by the server. Using it for a local validation
failure (empty list) is semantically wrong — it will display "Something went wrong with your
request" to the user.

**Fix options (choose one):**

- Use `DataError.Local.UNKNOWN` as a closer approximation until a dedicated
  `DataError.Local.INVALID_INPUT` variant is added.
- Add `DataError.Local.INVALID_INPUT` to the `DataError.Local` enum and use it here.

Recommended: add `INVALID_INPUT` to `DataError.Local` and use it. Update
`ErrorMapper.toUserMessage()` to return `"No activities to export."`.

### 3-C. `GetHomeDashboardUseCase` — magic number

```kotlin
recentActivities = result.data.recentActivities.take(5)
```

The value `5` is a business rule ("show the 5 most recent activities") but is hardcoded.

**Fix:** Extract to a named constant:

```kotlin
private const val MAX_RECENT_ACTIVITIES = 5
```

### 3-D. `GetProjectsUseCase` — no search parameters (linked to 2-C)

Once `ProjectRepository.getProjects(query, category, ...)` is updated (task 2-C),
`GetProjectsUseCase` must pass these parameters through:

```kotlin
suspend operator fun invoke(
    query: String? = null,
    category: ProjectCategory? = null,
    sortBy: String? = null,
    page: Int = 1,
): Result<List<Project>, DataError.Network>
```

### 3-E. `HireUserUseCase` — no validation

`hireUser(userId)` is called with `userId ?: ""` in `ProfileViewModel`. An empty string reaches the
repository and will cause a malformed HTTP request.

**Fix:** Add a guard:

```kotlin
suspend operator fun invoke(userId: String): Result<Unit, DataError.Network> {
    if (userId.isBlank()) return Result.Error(DataError.Network.CLIENT_ERROR)
    return repository.hireUser(userId)
}
```

### 3-F. `SubmitProposalUseCase` — does not validate required fields

No field-level validation before delegating to repository. An empty `pitchContent` or `budget`
reaches the server.

**Fix:** Add minimum validation:

```kotlin
suspend operator fun invoke(proposal: Proposal): Result<Unit, DataError.Network> {
    if (proposal.pitchContent.isBlank()) return Result.Error(DataError.Network.CLIENT_ERROR)
    if (proposal.budget.isBlank()) return Result.Error(DataError.Network.CLIENT_ERROR)
    return repository.submitProposal(proposal)
}
```

---

## 4. Core Utilities (supporting domain layer)

### 4-A. `DataError` — missing `Local.INVALID_INPUT`

The domain has legitimate input-validation failures (empty activities list in
`ExportActivityCsvUseCase`, blank proposal fields, blank `hireUser` ID) but no appropriate error
variant.

**Fix in** `core/src/commonMain/kotlin/com/smach/zapmancer/core/common/utils/Result.kt`:

```kotlin
enum class Local : DataError {
    DISK_FULL,
    PERMISSION_DENIED,
    INVALID_INPUT,   // ← add
    UNKNOWN,
}
```

Update `ErrorMapper.toUserMessage()`:

```kotlin
DataError.Local.INVALID_INPUT -> "The provided input is invalid."
```

### 4-B. `Result` — commented-out `Loading` state

```kotlin
// data object Loading : Result<Nothing, Nothing>
```

This comment has been left in the sealed interface. Either implement it (useful for streaming use
cases) or remove the comment to avoid confusion.

### 4-C. `DefaultPaginator` — not used by any ViewModel

The `Paginator` / `DefaultPaginator` in `core` is well-implemented and tested (
`DefaultPaginatorTest` exists), but `SearchViewModel` implements its own manual pagination (
`page: Int` + `take(itemsToShow)`) without using `DefaultPaginator`.

**Fix:** Once `SearchViewModel` is migrated to server-side search (VM plan task SK-1), wire it
through `DefaultPaginator` to leverage the tested guard (`isMakingRequest`) and page-key management.

---

## 5. Missing Use Cases

The following operations exist in repository interfaces but have **no corresponding use case**:

| Missing use case                | Repository method                                       | Used by                                                                                                                      |
|---------------------------------|---------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| `IsOnboardingCompletedUseCase`  | `AuthRepository.isOnboardingCompleted(): Flow<Boolean>` | Navigation graph (`AuthGraph.kt`) calls this directly — should go through a use case                                         |
| `SetOnboardingCompletedUseCase` | `AuthRepository.setOnboardingCompleted(Boolean)`        | Same — called directly in nav                                                                                                |
| `GetTrendingSearchesUseCase`    | (does not exist yet)                                    | `SearchUiState.trendingSearches` is hardcoded in state; a use case backed by a repository would allow server-driven trending |

---

## 6. Test Coverage

Currently only `LoginUseCaseTest` exists. Every use case should have a corresponding test following
the same pattern:

**Priority order for new test files:**

| Use case                   | Why prioritise                                                 |
|----------------------------|----------------------------------------------------------------|
| `GetProjectsUseCase`       | Core flow; important after adding filter parameters (task 3-D) |
| `SubmitProposalUseCase`    | Has new validation logic (task 3-F)                            |
| `ExportActivityCsvUseCase` | Has existing guard logic (task 3-B) — easiest to test          |
| `HireUserUseCase`          | Has new validation (task 3-E)                                  |
| `GetHomeDashboardUseCase`  | Has business logic (`take(5)`) — test that rule (task 3-C)     |
| All remaining use cases    | Pure delegation — test that errors are never swallowed         |

**Test file location:** `feature/domain/src/commonTest/kotlin/com/smach/zapmancer/domain/usecase/`

**Test pattern to follow** (from `LoginUseCaseTest`):

1. Success case — repository returns `Result.Success`, use case returns same.
2. Error case — repository returns `Result.Error`, use case propagates it.
3. Exception case — repository throws, use case does **not** catch it.

---

## 7. Execution Order

```
# Step 1 — Additive / non-breaking changes first
4-A  DataError.Local.INVALID_INPUT
5    Missing use cases (IsOnboarding, SetOnboarding)

# Step 2 — Model fixes (break DTO dependency)
1-A  Remove @Serializable from all domain models
1-G  Remove tokens from User model
2-A  UpdateProfileParams (remove DTO import from domain)
2-B  CreateProjectParams (remove ProjectDetail misuse)

# Step 3 — Model nullability and structural fixes
1-B  Fix nullability mismatches
1-C  Project structural issues (split file, remove ALL, UI fields)
1-D  HomeDashboard structural issues
1-E  Proposal split into Request/Item
1-F  NotificationItem.isRead

# Step 4 — Repository contract changes (coordinate with data layer impl)
2-C  getProjects() search parameters
2-D  Remove saveTokens from AuthRepository
2-E  SettingsRepository.updateDarkMode
2-F  MessageRepository.getMessages Flow error type

# Step 5 — Use case logic changes
3-A  Split UpdateSettingsUseCase
3-B  Fix ExportActivityCsvUseCase error type
3-C  Extract MAX_RECENT_ACTIVITIES constant
3-D  Update GetProjectsUseCase parameters
3-E  HireUserUseCase validation
3-F  SubmitProposalUseCase validation

# Step 6 — Tests
6    Add test files for all use cases in priority order
```

---

## 8. Verification Checklist

After each task:

- [ ] `./gradlew :feature:domain:compileKotlinMetadata` passes — zero errors.
- [ ] No `import kotlinx.serialization.*` in domain model files (grep:
  `import kotlinx.serialization`).
- [ ] No `import com.smach.zapmancer.core.common.dto` in domain files (grep: `core.common.dto`).
- [ ] No `@Serializable` annotation on domain model classes.
- [ ] `./gradlew :feature:domain:allTests` — all unit tests pass.
- [ ] No hardcoded numeric literals without a named constant in use case logic (grep:
  `\.take\(\d\)`).

---

## 9. Files NOT in Scope

- `core/common/utils/Result.kt` — well-designed; only the `Loading` comment cleanup needed (task
  4-B).
- `core/common/utils/ResultExt.kt` — clean utility; no changes needed.
- `core/common/utils/ErrorMapper.kt` — clean; only extend with new `INVALID_INPUT` message (task
  4-A).
- `core/network/ktor/SafeApiCall.kt` — already handles all HTTP error cases correctly.
- `core/network/session/SessionManager.kt` — clean; used correctly by data layer.
- `core/common/utils/DefaultPaginator.kt` — well-implemented and tested; only needs to be *used* (
  task 4-C).
- All repository **implementations** in the data layer — out of scope for this plan; they will need
  updating after interface changes but are not audited here.
