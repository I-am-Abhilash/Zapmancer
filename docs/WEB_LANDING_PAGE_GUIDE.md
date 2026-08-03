# Zapmancer Web Landing Page Architecture & Integration Guide

This guide documents the full end-to-end architecture pattern used in **Zapmancer** for building and serving the web-only marketing landing page (`LandingPageScreen.kt`).

---

## 1. Quick File Reference Map

```
Zapmancer Codebase Map
├── core/src/commonMain/.../core/common/
│   ├── dto/LandingPageDtos.kt                     <-- Wire Format DTOs
│   └── utils/Platform.kt                          <-- expect val isWebPlatform: Boolean
├── feature/domain/src/commonMain/.../domain/
│   ├── model/LandingPageData.kt                   <-- Domain Models
│   ├── repository/LandingPageRepository.kt        <-- Repository Interface
│   └── usecase/GetLandingPageUseCase.kt           <-- UseCase Provider
├── feature/data/src/commonMain/.../data/
│   └── repository/LandingPageRepositoryImpl.kt    <-- Repository Implementation (@Single)
├── server/src/main/kotlin/.../
│   ├── landingpage/service/LandingPageService.kt   <-- Server Service
│   ├── landingpage/routing/LandingPageRouting.kt  <-- Public Ktor Route (GET /landing-page/data)
│   ├── core/di/serverModule.kt                    <-- Koin Module (landingPageModule)
│   └── Application.kt                             <-- Application Entry Point
├── feature/presentation/src/commonMain/.../presentation/landingpage/
│   ├── state/LandingPageUiState.kt                <-- State Object
│   ├── viewmodel/LandingPageViewModel.kt          <-- ViewModel (@KoinViewModel)
│   └── screen/LandingPageScreen.kt                <-- Web Compose UI
└── shared/src/commonMain/.../
    ├── nav/Navigation.kt                          <-- Screen.LandingPage Route Key
    ├── nav/MainGraph.kt                           <-- Navigation Graph Entry
    └── App.kt                                     <-- Root Switcher (isWebPlatform == true)
```

---

## 2. Architecture & Data Flow

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

## 3. Platform Detection (`isWebPlatform == true`)

The app automatically detects web target via multiplatform expect/actual:

```kotlin
// core/src/commonMain/.../core/common/utils/Platform.kt
expect val isWebPlatform: Boolean

// androidMain / iosMain / jvmMain
actual val isWebPlatform: Boolean = false

// jsMain / wasmJsMain
actual val isWebPlatform: Boolean = true
```

---

## 4. Root Application Switcher (`App.kt`)

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

## 5. Screen Components Breakdown (`LandingPageScreen.kt`)

1. **Global Navigation Header**: Sticky header with logo, mega-menu dropdown triggers ("Find Talent", "Find Work", "Solutions", "Resources"), and Log in / Sign up buttons.
2. **Mega Menu Overlays**: Interactive floating dropdown panels for sub-category exploration.
3. **Hero Section**: Headline ("Great work starts with the right people."), subtext, primary CTA buttons, popular roles chips, and central marketplace flow diagram.
4. **Two Paths Section**: "Whatever you're building, start here" (I'm Hiring vs I'm Freelancing).
5. **Products Section**: "Everything you need to get work done." (6 product cards).
6. **Solutions Section**: Startups, Small Businesses, Agencies, Enterprises.
7. **How Zapmancer Works**: 4-step visual workflow.
8. **Marketplace & Skills Showcase**: Category skill chips.
9. **Featured Showcase**: Featured talent & featured projects cards.
10. **Trust & Safety**: Verified profiles, milestone escrow protection, dispute support.
11. **Success Stories & Resources**: Customer spotlights and guide articles.
12. **Final CTA & Web Footer**: Full site footer.
