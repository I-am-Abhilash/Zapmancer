# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project overview

Zapmancer is a Kotlin Multiplatform (KMP) project targeting **Android, iOS, and Web** (JS/Browser)
from a single codebase. It uses Compose Multiplatform for shared UI and follows a feature‑module
Clean Architecture layout (data → domain → presentation).

Root namespace: `com.smach.zapmancer`

## Module layout

```
:core                  — cross-cutting infra (network, storage, monitoring, base VM, DI)
:feature:data          — repository impls + Koin dataModule (depends on :core, :feature:domain)
:feature:domain        — models, repository interfaces, use-cases (no Android/Ktor deps)
:feature:presentation  — Compose screens, state, view-models, navigation helpers
:shared                — root composable (App()), MainViewModel, AuthGraph + MainGraph, Koin init
:androidApp            — Android entry point (Activity + Application)
:iosApp                — Xcode Swift shell that hosts MainViewController
:webApp                — JS/wrapper that runs App() in a ComposeViewport
```

`settings.gradle.kts` enables `TYPESAFE_PROJECT_ACCESSORS` — Gradle module dependencies in
`build.gradle.kts` are written `projects.feature.data` (not `project(":feature:data")`).

## Build & dev commands

Wrapper: `./gradlew`. JDK 17 (Android CI uses Temurin 17). All Compose/AGP/Kotlin versions are
pinned in `gradle/libs.versions.toml`.

- `./gradlew assembleDebug` — build Android APK
- `./gradlew testDebugUnitTest` — Android JVM unit tests
- `./gradlew detekt` — static analysis (Detekt; config at `detekt/detekt.yml`, applied to every
  subproject)
- `./gradlew ktlintCheck` — Spotless + ktlint style check (applied to every subproject via root
  `build.gradle.kts`)
- `./gradlew spotlessKotlinCheck` / `spotlessApply` — alternative entry points to the same Spotless
  task
- `./gradlew :webApp:jsBrowserDevelopmentRun` — run the web app (Kotlin/JS)
- `./gradlew :webApp:jsBrowserDistribution` — production web bundle

The pre-commit hook at `.github/git-hooks/pre-commit` runs `gradle detekt ktlintCheck`. Wire it with
`git config core.hooksPath .github/git-hooks` if you want local enforcement. CI is in
`.github/workflows/ci.yml` and runs the same `detekt`, `ktlintCheck`, `testDebugUnitTest`, and
`assembleDebug` on push/PR to `main`.

There are no iOS-specific Gradle build steps beyond what `iosArm64()` / `iosSimulatorArm64()`
produce as a static `common.framework` from `:core` (via `iosApp.xcodeproj`); open
`iosApp/iosApp.xcodeproj` in Xcode to run iOS.

## High-level architecture

### Layered feature modules

- **domain** is pure Kotlin (no Android, no Ktor). It defines `*Repository` interfaces, use-cases,
  and `@Serializable` data classes that both data and presentation consume.
- **data** implements those interfaces with Ktor calls. All HTTP responses go through
  `core.network.ktor.safeApiCall`, which unwraps the standard `ApiResponse<T>` envelope (
  `{ success, data, error }`) and maps failures to `core.common.utils.DataError.Network` (
  `UNAUTHORIZED`, `CLIENT_ERROR`, `NO_INTERNET`, `SERIALIZATION`, `SERVICE_UNAVAILABLE`, `UNKNOWN`).
  Repositories return either `Result<D, DataError.Network>` or throw — be consistent within a single
  repo.
- **presentation** holds Compose screens, `*UiState` data classes, `*Event` sealed hierarchies, and
  ViewModels. ViewModels extend `core.common.base.BaseViewModel<State, Event, Effect>` and follow a
  strict MVI pattern: `updateState { copy(...) }` for state, `sendEffect(...)` for one-shot side
  effects, `onEvent(event)` to dispatch UI input. ViewModels are registered in
  `feature/presentation/.../common/di/PresentationModule.kt`.

### Dependency injection (Koin)

A single root composition is built in `shared/src/commonMain/kotlin/com/smach/zapmancer/di/Koin.kt`:

```kotlin
modules(coreModule, dataModule, presentationModule, appModule())
```

Each module corresponds to one Gradle module:

- `core.common.di.coreModule` — `AppDatabase`, `DataStoreStorage`, `SessionManager`, `HttpClient`,
  `AnalyticsService` (currently `NapierAnalyticsService`)
- `data.di.dataModule` — every `*RepositoryImpl` (`singleOf(... bind<...>())`) + every use-case (
  `factoryOf(...)`)
- `features.common.di.presentationModule` — `viewModelOf` for most VMs,
  `viewModel { (param) -> ... }` for ones that need route params (`ProfileViewModel(userId)`,
  `MessagesDetailViewModel(conversationId)`, `ProjectDetailViewModel(projectId)`,
  `ClientProposalsViewModel(projectId)`)

Platform-specific Koin configuration lives in
`androidApp/src/main/kotlin/com/smach/zapmancer/ZapmancerApp.kt` (`androidContext`,
`androidLogger`). Web `main.kt` calls `initKoin()` then synchronously runs
`AppDatabase.Schema.create(driver).await()` before mounting Compose — this Web flow is required
because SQLDelight Web does not auto-create tables.

### Networking

- `core.network.ktor.provideHttpClient(sessionManager)` builds a Ktor `HttpClient` with
  `ContentNegotiation` (kotlinx-serialization), `Logging` (via Napier), and
  `Auth { bearer { ... } }` that auto-refreshes tokens against `auth/refresh`.
- `BASE_URL` is hardcoded in `core.network.ktor.NetworkConstants` (`http://127.0.0.1:8090/`). Update
  there for local dev. `shared/build.gradle.kts` also configures `buildkonfig` with
  `AppConfig.IS_DEBUG` (`true` for the `dev` flavor, `false` otherwise).
- All requests are expected to return the standard envelope; if you need a non-enveloped endpoint,
  add a separate call site — do not change the global interceptor.

### Persistence

SQLDelight is the only persistence layer. Schema is in
`core/src/commonMain/sqldelight/com/smach/zapmancer/core/database/AppDatabase.sq` (currently a
single `KeyValue` table). The `AppDatabase` is registered as a Koin singleton and consumed by
`DataStoreStorage` (key/value façade). Drivers are expect/actual:

- `core/src/androidMain/.../DataStoreBuilder.android.kt` — `AndroidSqliteDriver`
- `core/src/iosMain/.../DataStoreBuilder.ios.kt`
- `core/src/webMain/.../DataStoreBuilder.web.kt` — uses `@cashapp/sqldelight-sqljs-worker`; web's
  `webApp/build.gradle.kts` pins the npm versions

### Session

`core.network.session.SessionManager` is the single point that reads/writes tokens (`access_token`,
`refresh_token`, `user_id`, `onboarding_completed`). It is consumed by `AuthRepositoryImpl` for
login/logout and by `MainViewModel` to derive `AppState` (
`Loading | Onboarding | Unauthenticated | Authenticated(accessToken)`) via
`combine(sessionManager.observeAccessToken(), authRepository.isOnboardingCompleted())`.

### Navigation

Navigation uses Jetpack `navigation3` with type-safe `@Serializable` route keys (`Screen.*` in
`shared/src/commonMain/kotlin/com/smach/zapmancer/nav/Navigation.kt`). Every concrete `Screen`
subclass must be added to the `navConfig` polymorphic serializers module or screen lookup will fail
at runtime. There are two top-level graphs:

- `AuthGraph` (start = `Screen.Login`) — Login → Signup → Verification, ForgotPassword
- `MainGraph` (start = `Screen.Home`) — wraps a `ModalNavigationDrawer` (drawer + scaffold + bottom
  bar/rail)

Both use `rememberNavigationState` + `MainNavigator` (defined in `Navigation.kt`). `MainGraph`
consults `currentWindowAdaptiveInfo()` to swap between bottom nav (compact), rail (medium), drawer (
expanded). The standalone `feature/presentation/.../common/adaptive/AdaptiveScaffold.kt` is an
alternate adaptive chrome (used by some screens independently of the main graphs) — the two
scaffolds coexist intentionally.

`MainViewModel` (in `shared`, not `feature/presentation`) selects which graph to render based on
`AppState`. New top-level destinations belong in `bottomNavigationRoutes` and on the `Screen.icon`
extension.

### Compose conventions

- Screens live in `feature/presentation/.../<feature>/screen/*.kt`, paired with `state/*UiState.kt`
  and `viewmodel/*ViewModel.kt`. Most screens accept `(onBackClick, showSnackbar, ...)` callbacks
  rather than reaching into the navigator directly — the entry provider in `MainGraph` wires those.
- Adaptive widgets: `feature/.../common/adaptive/` provides `WindowLayout`, `LocalWindowLayout`,
  `rememberWindowLayout`, `AdaptiveScaffold`, `ResponsiveContainer`, `TwoPane`. Use
  `LocalWindowLayout.current` to switch layouts based on size class.
- Reusable chrome: `feature/.../common/components/` (`AppDrawerScaffold`, `AppImage`, `AppShimmer`,
  `EmptyState`, `ErrorState`, `UserAvatar`, `ZapmancerTopBar`, `AuthComponents`). The drawer is
  controlled through `LocalDrawerController`.
- Theme: `feature/.../common/theme/{Color,Shape,Theme,Type}.kt` exposes `AppTheme`.

## Linting rules in effect

- **Detekt** (config: `detekt/detekt.yml`, applied per-subproject in root `build.gradle.kts`).
- **Spotless + ktlint** (root `build.gradle.kts`): every `**/*.kt` and `**/*.gradle.kts`. Disabled
  rules: `filename`, `ktlint_standard_function-naming`, `ktlint_standard_filename`. Format Kotlin
  sources before pushing (`./gradlew spotlessApply`).
- The pre-commit hook runs `gradle detekt ktlintCheck`. CI runs the same plus `testDebugUnitTest`
  and `assembleDebug`.

## Cursor / Copilot / agent rules

- `.agents/rules/antigravity-rtk-rules.md` — instructs to prefix shell commands with `rtk` (Rust
  Token Killer CLI) to compress shell output for token efficiency. Use `rtk <cmd>` instead of raw
  shell commands when available; `rtk gain` reports savings.

## Common conventions / gotchas

- The backend base URL is hardcoded — point `core.network.ktor.NetworkConstants.BASE_URL` at your
  local server before running. Endpoints are relative (`auth/login`, `projects`, `home/dashboard`,
  etc.) and rely on the standard `ApiResponse<T>` envelope.
- `MainViewModel` is registered in `shared/.../di/Koin.kt` (`appModule()`), not in
  `presentationModule`, because it composes state from both `core` and `feature:domain`.
- New route → add a `@Serializable` data object/class in `Screen`, register it in the `navConfig`
  `polymorphic(NavKey::class)` block, then add an `entry<Screen.X>` in `MainGraph.appEntryProvider`
  or `AuthGraph.authEntryProvider`.
- New ViewModel with a route parameter → register with `viewModel { (param: T) -> ... }` in
  `PresentationModule.kt` and obtain it in the screen via `koinViewModel { parametersOf(...) }`.
- New repository → define the interface in `feature:domain`, implement in `feature:data`, bind in
  `data.di.DataModule`. Use `safeApiCall` for HTTP.
- Network errors are typed (`DataError.Network.*`) — convert to a user message via
  `core.common.utils.toUserMessage` (see `LoginViewModel.submit`).
- Web build needs the `sql-wasm.wasm` resource — keep it under `webApp/src/webMain/resources/`.