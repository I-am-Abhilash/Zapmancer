---
name: web-landing-page
description: Guide and instructions for creating full web-only landing pages in Compose Multiplatform Kotlin projects across Ktor backend, data layer, domain models, ViewModels, and platform target switching (isWebPlatform == true).
---

# Web Landing Page Architecture Pattern Guide

This document defines the standard pattern for implementing web-only marketing landing pages in Compose Multiplatform (KMP) codebases.

---

## Architecture Overview

```
 ┌───────────────────────────────────────────────────────────┐
 │ Ktor Server (GET /landing-page/data)                     │
 └─────────────────────────────┬─────────────────────────────┘
                               │ LandingPageDto
 ┌─────────────────────────────▼─────────────────────────────┐
 │ Data Layer (LandingPageRepositoryImpl)                    │
 └─────────────────────────────┬─────────────────────────────┘
                               │ LandingPageData Domain Model
 ┌─────────────────────────────▼─────────────────────────────┐
 │ Domain Layer (GetLandingPageUseCase & LandingPageRepo)    │
 └─────────────────────────────┬─────────────────────────────┘
                               │ UI State Flow
 ┌─────────────────────────────▼─────────────────────────────┐
 │ Presentation (LandingPageViewModel & LandingPageUiState)  │
 └─────────────────────────────┬─────────────────────────────┘
                               │ Compose Multiplatform
 ┌─────────────────────────────▼─────────────────────────────┐
 │ LandingPageScreen (Sticky Nav, Hero, TwoPaths, Products)  │
 └─────────────────────────────┬─────────────────────────────┘
                               │ Platform Switcher
 ┌─────────────────────────────▼─────────────────────────────┐
 │ App.kt (if (isWebPlatform) LandingPageScreen else Onboarding)│
 └───────────────────────────────────────────────────────────┘
```

---

## 1. File Path Map

| Layer | File Path | Purpose |
| :--- | :--- | :--- |
| **DTOs** | `core/src/commonMain/kotlin/.../core/common/dto/LandingPageDtos.kt` | Wire format `@Serializable` DTOs |
| **Domain Models** | `feature/domain/src/commonMain/kotlin/.../domain/model/LandingPageData.kt` | Domain entities for landing sections |
| **Domain Repository** | `feature/domain/src/commonMain/kotlin/.../domain/repository/LandingPageRepository.kt` | Repository interface |
| **Domain UseCase** | `feature/domain/src/commonMain/kotlin/.../domain/usecase/GetLandingPageUseCase.kt` | Data fetching usecase |
| **Data Repository** | `feature/data/src/commonMain/kotlin/.../data/repository/LandingPageRepositoryImpl.kt` | Fetch from Ktor API with mock fallback |
| **Server Service** | `server/src/main/kotlin/.../landingpage/service/LandingPageService.kt` | Server data provider |
| **Server Routing** | `server/src/main/kotlin/.../landingpage/routing/LandingPageRouting.kt` | Ktor GET `/landing-page/data` route |
| **Server DI** | `server/src/main/kotlin/.../core/di/serverModule.kt` & `Application.kt` | Server module registration |
| **UI State** | `feature/presentation/src/commonMain/kotlin/.../presentation/landingpage/state/LandingPageUiState.kt` | Screen state object |
| **ViewModel** | `feature/presentation/src/commonMain/kotlin/.../presentation/landingpage/viewmodel/LandingPageViewModel.kt` | `@KoinViewModel` state management |
| **Compose Screen** | `feature/presentation/src/commonMain/kotlin/.../presentation/landingpage/screen/LandingPageScreen.kt` | Web Compose UI |
| **Platform Target** | `core/src/commonMain/kotlin/.../core/common/utils/Platform.kt` | `expect val isWebPlatform: Boolean` |
| **Navigation Key** | `shared/src/commonMain/kotlin/.../nav/Navigation.kt` | `Screen.LandingPage` & serializer |
| **Navigation Graph** | `shared/src/commonMain/kotlin/.../nav/MainGraph.kt` | `entry<Screen.LandingPage>` entry provider |
| **Root Switcher** | `shared/src/commonMain/kotlin/.../App.kt` | Switches Web (`LandingPageScreen`) vs Mobile (`OnboardingScreen`) |

---

## 2. Step-by-Step Creation Recipe

### Step 1: Platform Detection (`Platform.kt`)
Define cross-platform target detection in `core/common/utils/Platform.kt`:
```kotlin
// commonMain
expect val isWebPlatform: Boolean

// androidMain, iosMain, jvmMain
actual val isWebPlatform: Boolean = false

// jsMain / wasmJsMain
actual val isWebPlatform: Boolean = true
```

### Step 2: DTO Wire Contracts (`LandingPageDtos.kt`)
Define serializable DTOs for landing content:
- `LandingPageDto` (contains hero, twoPaths, products, solutions, workflow, categories, featured talent/projects, trust items, success stories, resources).

### Step 3: Domain & Data Layers
1. **Domain**: `LandingPageData`, `LandingPageRepository`, and `GetLandingPageUseCase`.
2. **Data**: `LandingPageRepositoryImpl` fetching `client.get("landing-page/data")` via `safeApiCall` with fallback mock data for offline development. Annotated `@Single(binds = [LandingPageRepository::class])`.

### Step 4: Server Endpoint (`server`)
1. Create `LandingPageService.kt` emitting landing page DTOs.
2. Route `GET /landing-page/data` in `LandingPageRouting.kt`.
3. Register `landingPageModule` in `serverModule.kt` and `Application.kt`.

### Step 5: Compose Multiplatform UI (`LandingPageScreen.kt`)
Implement the screen with rich web aesthetics:
- **Sticky Top Bar**: Logo, Mega Menu triggers ("Find Talent", "Find Work", "Solutions", "Resources"), Log in & Sign up CTAs.
- **Mega Menu Overlays**: Floating dropdown panels.
- **Hero Section**: Headline, subtext, primary CTAs, popular roles chips, and interactive flow graphic.
- **Two Paths**: Hiring vs Freelancing path cards.
- **Products & Solutions Grids**: Modern card layouts.
- **How It Works**: Step-by-step visual workflow.
- **Marketplace Showcase & Featured Cards**: Category skill chips, featured talent/project cards.
- **Trust & Safety / Success Stories / Resources**: Social proof and content blocks.
- **Final Banner & Footer**: High-contrast CTA banner and full sitemap footer.

### Step 6: Root Switcher in `App.kt`
In `shared/src/commonMain/kotlin/com/smach/zapmancer/App.kt`:
```kotlin
is AppState.Onboarding -> {
    if (isWebPlatform) {
        LandingPageScreen(
            onNavigateToSearch = { mainViewModel.completeOnboarding() },
            onNavigateToProjects = { mainViewModel.completeOnboarding() },
            onNavigateToLogin = { mainViewModel.completeOnboarding() },
            onNavigateToSignUp = { mainViewModel.completeOnboarding() },
        )
    } else {
        OnboardingScreen(
            onFinished = { mainViewModel.completeOnboarding() },
        )
    }
}
```

---

## 3. Fast Checklist for New Projects
1. Copy/create `LandingPageDtos.kt`, `LandingPageData.kt`, `LandingPageRepository.kt`, `GetLandingPageUseCase.kt`, and `LandingPageRepositoryImpl.kt`.
2. Set up Ktor server endpoint `GET /landing-page/data`.
3. Build Compose Multiplatform UI in `LandingPageScreen.kt`.
4. Ensure `Platform.kt` exposes `isWebPlatform`.
5. Switch `App.kt` routing based on `isWebPlatform`.
