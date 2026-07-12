# ViewModel Layer Cleanup — Implementation Plan

> **Target module:** `feature/presentation/src/commonMain/kotlin/com/smach/zapmancer/features/`  
> **Companion plan:** See `implementation_plan.md` for the UI/screen layer tasks.  
> **Purpose:** Guide a lower-power AI model (or developer) through incremental, safe ViewModel refactors that improve consistency, correctness, and testability across all features.

---

## 0. Guiding Principles

| Rule | Rationale |
|---|---|
| **`sealed interface` over `sealed class`** for Event/Effect | `interface` is preferred in modern Kotlin/KMP; most VMs already use it, unify the stragglers |
| **`Unit` Effect type is a red flag** | Signals the VM cannot communicate navigation/side-effects; replace with a real sealed interface |
| **Never leak domain objects into `updateState` mapping if a UI model already exists** | Keeps the presentation layer isolated from domain changes |
| **Error state must always be surfaced** — never silently swallowed in `onError = {}` | Every user-triggered action deserves feedback |
| **One private function per coroutine launch** | Avoid launching nested coroutines inside `viewModelScope.launch { launch { ... } }` without structured tracking |
| **`init` block must come before `override fun onEvent`** | Consistent ordering: constructor fields → init → override → private helpers |

---

## 1. Cross-Cutting: Naming & Style Consistency

### 1-A. Unify `sealed class` → `sealed interface` for Events and Effects

Several VMs use `sealed class` while others use `sealed interface`. All new Kotlin best-practices and the rest of the codebase favor `sealed interface`.

**Files to change:**

| File | Current | Fix |
|---|---|---|
| `HomeViewModel.kt` | `sealed class HomeEvent`, `sealed class HomeEffect` | `sealed interface HomeEvent`, `sealed interface HomeEffect` |
| `ProfileViewModel.kt` | `sealed class ProfileEvent`, `sealed class ProfileEffect` | `sealed interface …` |
| `MessagesListViewModel.kt` | `sealed class MessagesListEvent`, `sealed class MessagesListEffect` | `sealed interface …` |
| `MessagesDetailViewModel.kt` | `sealed class MessagesDetailEvent`, `sealed class MessagesDetailEffect` | `sealed interface …` |
| `SettingsViewModel.kt` | `sealed class SettingsEvent`, `sealed class SettingsEffect` | `sealed interface …` |
| `NotificationViewModel.kt` | `sealed class NotificationEvent`, `sealed class NotificationEffect` | `sealed interface …` |
| `LoginViewModel.kt` | `sealed class LoginEvent`, `sealed class LoginSideEffect` | `sealed interface LoginEvent`, `sealed interface LoginSideEffect` |
| `SignupViewModel.kt` | `sealed class SignupEvent` | `sealed interface SignupEvent` |
| `ForgotPasswordViewModel.kt` | `sealed class ForgotPasswordEvent` | `sealed interface ForgotPasswordEvent` |
| `VerificationViewModel.kt` | `sealed class VerificationEvent` | `sealed interface VerificationEvent` |
| `EditProfileViewModel.kt` | `sealed class EditProfileEvent`, `sealed class EditProfileEffect` | `sealed interface …` |

**Already correct** (`sealed interface`): `ProjectListViewModel`, `ProjectDetailViewModel`, `PostProjectViewModel`, `ProposalViewModel`, `ClientProposalsViewModel`.

**How to change** — replace each occurrence:
```diff
-sealed class FooEvent {
-    data object Bar : FooEvent()
-    data class Baz(val id: String) : FooEvent()
-}
+sealed interface FooEvent {
+    data object Bar : FooEvent
+    data class Baz(val id: String) : FooEvent
+}
```
> Note: Remove the parentheses `()` after each member when switching from class to interface.

### 1-B. Standardise `init` block ordering

Kotlin convention and the project's own pattern (see `ProjectListViewModel`) is:

```
class Foo : BaseViewModel(...) {
    init { ... }           // ← FIRST
    override fun onEvent   // ← SECOND
    private fun helpers    // ← LAST
}
```

Files where `init` appears **after** `onEvent` and must be moved up:
- `HomeViewModel.kt` (init at line 49, onEvent at line 38)
- `MessagesListViewModel.kt` (init at line 50, onEvent at line 28)
- `MessagesDetailViewModel.kt` (init at line 59, onEvent at line 43)
- `SettingsViewModel.kt` (init at line 33, onEvent at line 37 — correct here, verify on edit)

---

## 2. ViewModel-by-ViewModel Tasks

### 2-A. `LoginViewModel.kt`

File: `features/auth/viewmodel/LoginViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| L-1 | Effect sealed type is named `LoginSideEffect` while all other VMs use `*Effect` | Rename to `LoginEffect` to match project convention |
| L-2 | `sealed class LoginEvent` / `sealed class LoginSideEffect` | → `sealed interface` (Task 1-A) |
| L-3 | `OnRememberMeChanged` handler body is a comment: `/* Handle remember me check */` — a no-op event that costs a whole sealed subtype | Remove the event or implement it; a comment-only handler is dead code |
| L-4 | Uses raw `Result` pattern (`when (val result = loginUseCase(...))`) instead of `foldTyped` used everywhere else | Refactor to `loginUseCase(...).foldTyped(onSuccess = ..., onError = ...)` for consistency |

### 2-B. `SignupViewModel.kt`

File: `features/auth/viewmodel/SignupViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| S-1 | Effect type parameter is `Unit` — the VM cannot signal successful registration to the screen | Replace `Unit` with a real `sealed interface SignupEffect` containing at least `data object NavigateToVerification : SignupEffect` |
| S-2 | `object Submit : SignupEvent()` — uses old-style `object` instead of `data object` | Change to `data object Submit : SignupEvent` |
| S-3 | `sealed class SignupEvent` | → `sealed interface SignupEvent` (Task 1-A) |
| S-4 | Uses raw `Result` pattern instead of `foldTyped` | Refactor to `foldTyped` for consistency |
| S-5 | On success, state is updated with `isSuccess = true` but **no navigation effect is emitted** | After S-1, emit `sendEffect(SignupEffect.NavigateToVerification)` on success |

### 2-C. `ForgotPasswordViewModel.kt`

File: `features/auth/viewmodel/ForgotPasswordViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| FP-1 | Effect type parameter is `Unit` | Replace with `sealed interface ForgotPasswordEffect` containing `data object NavigateToVerification` and `data class ShowToast(val message: String)` |
| FP-2 | `sealed class ForgotPasswordEvent` | → `sealed interface ForgotPasswordEvent` (Task 1-A) |
| FP-3 | Uses raw `Result` pattern instead of `foldTyped` | Refactor to `foldTyped` |
| FP-4 | On success the user sees nothing — no navigation, no toast | After FP-1, emit `sendEffect(ForgotPasswordEffect.ShowToast("Password reset email sent!"))` on success |

### 2-D. `VerificationViewModel.kt`

File: `features/auth/viewmodel/VerificationViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| V-1 | Effect type parameter is `Unit` | Replace with `sealed interface VerificationEffect` containing `data object NavigateToHome` and `data class ShowToast(val message: String)` |
| V-2 | `sealed class VerificationEvent` | → `sealed interface VerificationEvent` (Task 1-A) |
| V-3 | Uses raw `Result` pattern instead of `foldTyped` | Refactor to `foldTyped` |
| V-4 | **Hardcoded email: `val email = "user@example.com"`** — the OTP cannot be verified without the real email | The email must be passed to the ViewModel as a constructor parameter (same pattern as `MessagesDetailViewModel`) and propagated to `verifyOtpUseCase` |
| V-5 | No navigation effect on success | After V-1, emit `sendEffect(VerificationEffect.NavigateToHome)` |

### 2-E. `HomeViewModel.kt`

File: `features/home/viewmodel/HomeViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| H-1 | `sealed class HomeEvent` / `sealed class HomeEffect` | → `sealed interface` (Task 1-A) |
| H-2 | `init` block appears after `onEvent` | Reorder: move `init` before `override fun onEvent` (Task 1-B) |
| H-3 | `loadDashboard()` launches a **nested `launch`** inside `viewModelScope.launch` for the profile check (line 66) | Use `async`/`coroutineScope` or two parallel top-level `launch` calls without nesting; nested launch can silently drop exceptions |
| H-4 | `exportCsv()` re-maps `UserActivity` from state to a new `UserActivity` with `tag = it.category` (line 121) — `tag` field is copied from `category`, losing the original tag value | Fix: use `tag = it.tag` |
| H-5 | Client-mode dashboard stats (`totalSpent`, `activeJobPostsCount`, `proposalsReceivedCount`) are never populated by `loadDashboard` | Extend `GetHomeDashboardUseCase` response mapping to populate these fields, or document as intentional stub |
| H-6 | On error, only a toast is emitted; `HomeUiState` has `isLoading` but no `error` field | Add `error: String? = null` to `HomeUiState` and set it on failure so the screen can show `ErrorState` |

### 2-F. `ProfileViewModel.kt`

File: `features/profile/viewmodel/ProfileViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| P-1 | `sealed class ProfileEvent` / `sealed class ProfileEffect` | → `sealed interface` (Task 1-A) |
| P-2 | `onReviewMoreClick` and `onLoadMorePortfolio` both just emit a toast ("coming soon") — these are placeholder stubs | Mark with a `// TODO: implement pagination` comment; or expose a `isPaginationEnabled` flag in state and hide the "More" button when false, rather than showing a misleading toast |
| P-3 | `hireUser()` sets `isLoading = true` and on error sets `error` in state AND emits a toast with the same message | Decide on one error channel: either `state.error` (shown in screen) or `ShowToast` effect, not both, to avoid duplicate error display |
| P-4 | `PortfolioItem` and `ProfileReview` are mapped field-by-field inside `onSuccess` — this is a domain → UI model mapping that belongs in a `toUiModel()` extension (same pattern as `Project.toUiModel()` in `ProjectListUiState.kt`) | Extract `UserProfile.toUiState(): ProfileUiState` or individual `DomainPortfolioItem.toUiModel()` / `DomainReview.toUiModel()` extension functions |

### 2-G. `EditProfileViewModel.kt`

File: `features/profile/viewmodel/EditProfileViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| EP-1 | `sealed class EditProfileEvent` / `sealed class EditProfileEffect` | → `sealed interface` (Task 1-A) |
| EP-2 | `saveProfile()` emits **two effects** on success: `ShowToast` then `NavigateBack` | The screen immediately navigates back so the toast is never visible. Keep only `NavigateBack`; the success is implied by the navigation |
| EP-3 | `loadProfile()` on error emits **both** a state `error` update AND a `ShowToast` effect (same duplication as P-3) | Use only `state.error` and let the screen render `ErrorState`; remove redundant toast |
| EP-4 | `EditProfileEvent.LoadProfile` is an event but is also called directly in `init { loadProfile() }` — the event is redundant | Remove `EditProfileEvent.LoadProfile` (dead event); only call `loadProfile()` from `init` |
| EP-5 | `addSkill` / `removeSkill` operate on `List` via `+` / `-` operators | Valid, but `removeSkill` uses `current - skill` (by value) rather than by index; safe for unique skills but document the uniqueness constraint or enforce it with a `Set` |

### 2-H. `ProjectListViewModel.kt`

File: `features/projects/viewmodel/ProjectListViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| PL-1 | `ProjectListUiState` imports `ProjectUiModel` from the **screen** package (`features/projects/screen/ProjectUiModel`) | `ProjectUiModel` should be in the `state` package; move it there (see also UI plan task PL-7) |
| PL-2 | `CategorySelected` event updates state but the category filter is applied **inside the screen** (client-side `list.filter { it.category == state.category }`) | The VM owns filtering logic; move the `filter` call into the VM's `CategorySelected` handler and expose a `filteredProjects: List<ProjectUiModel>` field in state |
| PL-3 | `loadProjects()` clears `projects = emptyList()` before loading — causes a flash of `EmptyState` if the list already had content (e.g. on refresh) | Set only `isLoading = true`, keep prior projects visible until new data arrives |

### 2-I. `ProjectDetailViewModel.kt`

File: `features/projects/viewmodel/ProjectDetailViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| PD-1 | `ProjectDetailUiState` has no `error: String?` field | On load failure only a toast is emitted; add `error` to state and set it in `onError` so the screen can render `ErrorState` |
| PD-2 | `toggleSave()` does **not** set `isLoading` before the network call | Add `updateState { copy(isLoading = true) }` before calling `saveProjectUseCase` and restore on completion |
| PD-3 | `apply()` has no loading guard — rapid double-tap emits two API calls | Add `if (uiState.value.isApplying) return` guard, and add `isApplying: Boolean = false` to `ProjectDetailUiState` |

### 2-J. `PostProjectViewModel.kt`

File: `features/projects/viewmodel/PostProjectViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| PP-1 | `submitProject()` creates a `ProjectDetail` domain object with **hardcoded placeholder values** (`postedTime = "Just now"`, `location = "Remote"`, `clientName = "You"`, etc.) | These should either come from the authenticated user's session/profile or be removed entirely if the server fills them; document clearly with a `// TODO` or source from a repository |
| PP-2 | Validation only checks `title.isBlank()` and `description.isBlank()`, but the `Submit` button in the screen is also gated on those fields — duplicate validation | Keep the VM-side validation as the single source of truth; the screen's button `enabled` check should mirror `state.isSubmittable` (a derived property) rather than repeating the condition |
| PP-3 | `PostProjectEvent.OnCategoryChanged` is defined but **there is no corresponding `OutlinedTextField` or selector for category in `PostProjectScreen.kt`** — the event is never dispatched | Either wire up a category picker in the screen or remove the event |

### 2-K. `SearchViewModel.kt`

File: `features/search/viewmodel/SearchViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| SK-1 | The entire search pipeline is **client-side** — all projects are fetched once into `allFetchedProjects` and filtered in memory | The backend already supports server-side filtering (see conversation #50889808). Migrate `executeSearch` to call `getProjectsUseCase(query, category, sortBy, page)` parameters instead of doing in-memory filtering |
| SK-2 | `executeSearch` calls `delay(350.milliseconds)` unconditionally when query is non-empty — a manual debounce **on top of** the `queryFlow.debounce(300.milliseconds)` already applied upstream | Remove the `delay` inside `executeSearch`; the upstream debounce on `queryFlow` is sufficient |
| SK-3 | Sort option `BUDGET` sorts by `membersCount` (line 198) — mislabelled | Rename to `MEMBERS` or fix the sort key to the correct budget field once the domain model exposes it |
| SK-4 | `startVoiceListening()` picks a recognised text by calling `uiState.value.trendingSearches.random()` — a stub that simulates voice recognition | Mark with `// TODO: integrate real speech-to-text SDK` so it is not confused for real functionality |
| SK-5 | Recent searches are hardcoded in `init` (`listOf("Smart Contract", "KMP Application", "Compose Multiplatform")`) | Persist recent searches using `SharedPreferences` / `DataStore` via a `SearchHistoryRepository`; load in `init`, save in `addRecent` |
| SK-6 | `loadNextPage` calls `executeSearch(resetPage = false)` which re-launches a new coroutine, but there is no cancellation of an in-flight page load | Add a `private var searchJob: Job? = null` and cancel it before each `executeSearch` launch (same pattern as `voiceSearchJob`) |

### 2-L. `MessagesListViewModel.kt`

File: `features/messages/viewmodel/MessagesListViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| ML-1 | `sealed class MessagesListEvent` / `MessagesListEffect` | → `sealed interface` (Task 1-A) |
| ML-2 | `init` block after `onEvent` | Reorder (Task 1-B) |
| ML-3 | `loadConversations()` on error silently calls `updateState { copy(isLoading = false) }` with no message (line 76) | Add `error = "Failed to load conversations"` to the state update and emit a `ShowToast` effect (requires adding `ShowToast` to `MessagesListEffect`) |
| ML-4 | `MessagesListUiState` has `searchQuery` and `selectedFilter` fields but **the ViewModel never uses them to filter `conversations`** — the screen has no filtering logic either | Either implement server-side or client-side filtering on `OnSearchQueryChanged` / `OnFilterSelected`, or remove the fields if they are purely cosmetic stubs |
| ML-5 | `ConversationItem` is mapped field-by-field in `loadConversations` — same domain→UI boilerplate as `ProfileViewModel` | Extract `DomainConversationItem.toUiModel(): ConversationItem` extension function in `MessagesListUiState.kt` |

### 2-M. `MessagesDetailViewModel.kt`

File: `features/messages/viewmodel/MessagesDetailViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| MD-1 | `sealed class MessagesDetailEvent` / `MessagesDetailEffect` | → `sealed interface` (Task 1-A) |
| MD-2 | `init` block after `onEvent` | Reorder (Task 1-B) |
| MD-3 | `observeMessages()` sets `isLoading = true` but never sets it back to `false` on error — if `getMessagesUseCase` emits an error, the spinner never stops | Add error handling: wrap `collectLatest` in a try/catch or use `catch` operator on the flow |
| MD-4 | `sendMessage()` clears `typingText` and calls `sendMessageUseCase` but **intentionally ignores the result** | The current comment "Result intentionally ignored; UI re-collects messages" is a reasonable design for optimistic UI — add the comment at the top of the function (currently it is at the end and easy to miss) |
| MD-5 | `MessageItem.status = MessageStatus.valueOf(domainItem.status.name)` — a `valueOf` by name that will throw `IllegalArgumentException` at runtime if the domain and presentation `MessageStatus` enums diverge | Use a safe mapping function: `domainItem.status.toUiStatus()` with an exhaustive `when` expression |
| MD-6 | `MessageDetailScreen` entry in the screen creates the ViewModel via `koinViewModel(parameters = {...})` passing 4 constructor args — the ViewModel has no `init` loading guard to prevent double-loads if the screen recomposes | Already safe because `init` only calls `observeMessages()` once, but the `markAsRead()` call in `init` **can be called again** via `MessagesDetailEvent.MarkAsRead` — double-mark is harmless but consider removing the event or guarding with a flag |

### 2-N. `NotificationViewModel.kt`

File: `features/alerts/viewmodel/NotificationViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| N-1 | `sealed class NotificationEvent` / `NotificationEffect` | → `sealed interface` (Task 1-A) |
| N-2 | `sendQuickReply()` sets `isLoading = true` at the top-level — all notifications appear loading while one reply is being sent | Add a per-notification loading state (e.g. `loadingNotificationId: String? = null` in `NotificationUiState`) so only the relevant row shows a spinner |
| N-3 | `NotificationType.valueOf(domainItem.type.name)` — same unsafe `valueOf` pattern as MD-5 | Replace with a safe exhaustive `when` mapping extension function |
| N-4 | After `sendQuickReply` succeeds, `loadNotifications()` is called to refresh — this causes a full-list reload and flicker | Update the specific notification in-place: `copy(notifications = notifications.map { if (it.id == notificationId) it.copy(quickReply = null) else it })` |

### 2-O. `SettingsViewModel.kt`

File: `features/settings/viewmodel/SettingsViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| ST-1 | `sealed class SettingsEvent` / `SettingsEffect` | → `sealed interface` (Task 1-A) |
| ST-2 | `toggleNotifications` and `toggleClientMode` have empty `onError = { }` blocks — failures are silently swallowed | Revert the optimistic state update on error or emit a `ShowToast` effect (requires adding `ShowToast` to `SettingsEffect`) |
| ST-3 | `loadSettings` on error only resets `isLoading` — no error state, no toast | Emit a `ShowToast` or set an `error` field in state |
| ST-4 | `SettingsEvent.LoadSettings` is defined but never dispatched from the screen (screen calls `init { loadSettings() }` directly) | Remove dead `LoadSettings` event or wire it to a pull-to-refresh gesture |

### 2-P. `ProposalViewModel.kt`

File: `features/proposal/viewmodel/ProposalViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| PR-1 | `ProposalUiState` has `freelancerName` and `freelancerRole` default to empty string — the ViewModel **never loads the user's profile** to populate them | Load the authenticated user's profile in `init` using `GetUserProfileUseCase` (same as `ProfileViewModel`) |
| PR-2 | `StepChanged` event accepts any arbitrary `Int` — nothing prevents navigating to step 5 or step -1 | Validate: `if (event.step in 1..ProposalStep.entries.size) updateState { copy(currentStep = event.step) }` |
| PR-3 | `submitProposal()` does not guard against double-submission — a second tap while `isSubmitting = true` will launch another coroutine | Add `if (uiState.value.isSubmitting) return` at the top of `submitProposal()` |

### 2-Q. `ClientProposalsViewModel.kt`

File: `features/proposal/viewmodel/ClientProposalsViewModel.kt`

| # | Issue | Fix |
|---|---|---|
| CP-1 | `loadProposals()` on error has `onError = { /* keep state; user can retry */ }` — loading never stops (`isLoading` stays `true`) | Set `isLoading = false` in the `onError` block |
| CP-2 | `AcceptBid` and `MessageFreelancer` handlers only emit toasts — they are stubs | Mark with `// TODO: wire to AcceptBidUseCase / CreateConversationUseCase` so future implementers know the intent |
| CP-3 | `companion object` with `fallbackProposals` data is defined but **never used anywhere** in the VM | Remove dead code |

---

## 3. State File Audit

### 3-A. Files with missing `error` field

These state classes have `isLoading` but no `error: String?` field, preventing the screen from showing `ErrorState`:

| State file | Missing field | Action |
|---|---|---|
| `HomeUiState.kt` | `error: String? = null` | Add; populate in `HomeViewModel.loadDashboard` onError |
| `MessagesListUiState.kt` | `error: String? = null` | Add; populate in `MessagesListViewModel.loadConversations` onError |
| `MessagesDetailUiState.kt` | `error: String? = null` | Add; populate in `MessagesDetailViewModel.observeMessages` catch |
| `ProjectDetailUiState.kt` | `error: String? = null` | Add; populate in `ProjectDetailViewModel.loadProject` onError (task PD-1) |

### 3-B. `ProjectListUiState.kt` — circular import

`ProjectListUiState.kt` imports `ProjectUiModel` from `features/projects/screen/ProjectUiModel` (a screen package). State files must not import from screen packages.

**Fix:** Move `ProjectUiModel` and `Project.toUiModel()` into `features/projects/state/` (aligns with UI plan task PL-7).

### 3-C. `SearchUiState.kt` — hardcoded trending searches

`trendingSearches` in the default constructor contains hardcoded strings. These should come from a use case / repository in `SearchViewModel.init`.

---

## 4. Execution Order

```
1-A: sealed class → sealed interface (all files)     # sweeping rename, no logic change
1-B: init block reordering                           # cosmetic, zero risk

# Auth VMs (smallest, best to warm up on)
L-3, L-4           # LoginViewModel
S-1..5             # SignupViewModel
FP-1..4            # ForgotPasswordViewModel
V-1..5             # VerificationViewModel

# State file fixes (before touching screens)
3-A  →  3-B  →  3-C

# Feature VMs (logic changes)
H-2..6             # HomeViewModel
P-2..4             # ProfileViewModel
EP-2..5            # EditProfileViewModel
PL-1..3            # ProjectListViewModel
PD-1..3            # ProjectDetailViewModel
PP-1..3            # PostProjectViewModel
SK-2..6            # SearchViewModel  (SK-1 is a larger backend integration)
ML-2..5            # MessagesListViewModel
MD-2..6            # MessagesDetailViewModel
N-2..4             # NotificationViewModel
ST-2..4            # SettingsViewModel
PR-1..3            # ProposalViewModel
CP-1..3            # ClientProposalsViewModel
```

---

## 5. Verification Checklist

After each VM task:

- [ ] `./gradlew :feature:presentation:compileKotlinMetadata` passes with zero errors.
- [ ] No `sealed class` remains for Event/Effect types (grep: `sealed class.*Event\|sealed class.*Effect`).
- [ ] No `onError = { }` or `onError = { /* ... */ }` empty blocks remain (grep: `onError\s*=\s*\{[\s]*\}`).
- [ ] No `valueOf(` calls on enum types — all use exhaustive `when` mapping (grep: `\.valueOf(`).
- [ ] No hardcoded placeholder strings in business logic (grep: `"Just now"\|"Remote"\|"user@example.com"`).
- [ ] `init` block appears before `override fun onEvent` in every ViewModel.

---

## 6. Files NOT in Scope

The following are already well-structured and require no changes:

- `BaseViewModel.kt` — clean architecture; well-documented; used correctly by all VMs.
- `ProjectListViewModel.kt` event/effect types — already `sealed interface`.
- `ProposalViewModel.kt` event/effect types — already `sealed interface`.
- `ClientProposalsViewModel.kt` event/effect types — already `sealed interface`.
- `MessagesDetailViewModel.sendMessage()` — intentional optimistic UI; result-ignore is documented.
