# Presentation Layer Cleanup — Implementation Plan

> **Target module:** `feature/presentation/src/commonMain/kotlin/com/smach/zapmancer/features/`  
> **Purpose:** Guide a lower-power AI model (or developer) through a set of safe, incremental
> refactors that standardise the UI component architecture, eliminate duplicated code, and raise
> accessibility quality across all screens.

---

## 0. Guiding Principles (read before touching any file)

| Rule                                                                                                         | Rationale                                       |
|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| **Use `MaterialTheme.typography.*`** instead of raw `fontSize = N.sp`                                        | Enables future theming and text scaling         |
| **Import from `features.common.components`** for shared UI atoms                                             | Prevents cross-feature coupling and duplication |
| **Every screen must handle `isLoading`, `error`, and empty list**                                            | Consistent developer and user experience        |
| **Decorative icons** → `contentDescription = null`; **interactive icons** → descriptive `contentDescription` | Accessibility / TalkBack compliance             |
| **One change per screen per PR**                                                                             | Keeps diffs reviewable; reduces regression risk |

---

## 1. Shared Common Components

These tasks create or improve reusable components. **Do these first** before editing individual
screens, because later tasks depend on them.

### 1-A. Extract `CategoryFilterChip` to `common/components`

Both `ProjectListScreen.kt` and `SearchScreen.kt` contain **identical** `when (category)`
colour-mapping blocks and identical `FilterChip` rendering. The only difference is the outer
container (`LazyRow` / `Row`).

**File to create:** `features/common/components/CategoryFilterChip.kt`

```kotlin
package com.smach.zapmancer.features.common.components

// Extract the shared FilterChip rendering for a ProjectCategory.
// Parameters: category, isSelected, tintColor (computed from category),
//             onClick, modifier.
// The colour-mapping `when (category)` block should live here once,
// so ProjectListScreen and SearchScreen can simply call CategoryFilterChip(...)
// instead of repeating the entire block.
```

Steps:

1. Copy the colour-mapping `when` block from `ProjectListScreen.CategoryFilterBar` into a
   `fun categoryAccentColor(category: ProjectCategory): Color` extension / top-level function.
2. Create `CategoryFilterChip` composable accepting `(category, isSelected, onClick, modifier)`.
3. Replace the duplicated `FilterChip` blocks in both `ProjectListScreen.CategoryFilterBar` and
   `SearchScreen.SearchFilterHeader`.

### 1-B. Extract `ProjectStatusBadge` to `common/components`

`ProjectListScreen.ProjectItemCard` and `SearchScreen.SearchResultCard` both render a
`Surface + Box(dot) + Text` status badge. Extract to:

**File to create:** `features/common/components/ProjectStatusBadge.kt`

```kotlin
// A small pill showing a colour-coded dot + status label.
// Parameters: status: ProjectStatus, modifier.
// Uses ProjectStatus.color() extension defined in ProjectListScreen –
// move that extension here too (or to a shared util file).
```

### 1-C. `AppShimmer.kt` is already shared — use it everywhere

`AppShimmer.kt` (`shimmerEffect` modifier + `AppShimmer` composable) already exists in
`common/components`. However, `SearchScreen.SearchSkeletonCard` and `ProjectListScreen` roll their
own `animateFloat` + `Brush.linearGradient` shimmer locally.

**Action:** Replace the local shimmer implementations with `Modifier.shimmerEffect(shape)` from
`AppShimmer.kt`.

---

## 2. Screen-by-Screen Tasks

### 2-A. `HomeScreen.kt`

File: `features/home/screen/HomeScreen.kt`

| #   | Issue                                                                                                                                                 | Fix                                                                                                                                                                  |
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| H-1 | `val ZapGold = Color(0xFFFFD700)` declared as top-level file-scope val in `HomeScreen.kt`                                                             | Move to `features/common/theme/` (e.g. `Colors.kt` or `AppColors.kt`) and import it wherever needed (`ActivityRow`, `StatCard`, `ProjectListScreen`, `AlertsScreen`) |
| H-2 | `StatCard` – star rating row renders 5 `Icons.Default.Star` without `contentDescription` that expresses the value                                     | Add `semantics { contentDescription = "Rating: $value out of 5" }` to the outer `Row`                                                                                |
| H-3 | `HomeContent` is not `isLoading`-aware                                                                                                                | Add shimmer skeleton (3 `AppShimmer` cards) when `state.isLoading == true`. The `HomeUiState` likely has this field; verify and add if absent                        |
| H-4 | `EmptyState` in recent activities is wrapped in an extra `Box(contentAlignment = Center)` that is then inside the `EmptyState`'s own centred `Column` | Remove the outer `Box` wrapper                                                                                                                                       |
| H-5 | `CompleteProfileBanner` uses `fontSize = 14.sp` for button text                                                                                       | Replace with `style = MaterialTheme.typography.labelLarge`                                                                                                           |

### 2-B. `ProjectListScreen.kt`

File: `features/projects/screen/ProjectListScreen.kt`

| #    | Issue                                                                                                          | Fix                                                                                                                                                                |
|------|----------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| PL-1 | `CategoryFilterBar` colour-mapping block duplicated vs. `SearchScreen`                                         | Replace with `CategoryFilterChip` from Task 1-A                                                                                                                    |
| PL-2 | `ProjectItemCard` status badge (Surface + dot + Text) duplicated vs. `SearchScreen`                            | Replace with `ProjectStatusBadge` from Task 1-B                                                                                                                    |
| PL-3 | `ProjectItemCard` icon/accentColor `when (category)` blocks are duplicated vs. `SearchScreen.SearchResultCard` | Extract to `fun ProjectCategory.icon(): ImageVector` and `fun ProjectCategory.accentColor(colors: ColorScheme): Color` extension functions in `common/`            |
| PL-4 | `DashedDivider` is defined locally and used only inside `PortfolioHeader`                                      | Either move to `common/components/DashedDivider.kt` for potential reuse or keep private; it is currently unused outside this file. Mark `private` if staying local |
| PL-5 | `PortfolioHeader` is defined but **never called** anywhere in this file                                        | Remove dead code                                                                                                                                                   |
| PL-6 | Error state renders `EmptyState` with `title = state.error`                                                    | Use dedicated `ErrorState` component (already in `common/components/ErrorState.kt`) instead of misusing `EmptyState`                                               |
| PL-7 | `ProjectUiModel` data class is defined inside a screen file                                                    | Move to `features/projects/state/` or `domain/model/` (check if a domain `Project` already covers this)                                                            |
| PL-8 | `ProjectStatus.color()` extension is defined locally                                                           | Move to `common/` alongside `ProjectStatusBadge` (Task 1-B)                                                                                                        |
| PL-9 | `ZapGold` imported from `HomeScreen.kt` cross-feature                                                          | After Task H-1, import from the shared theme file                                                                                                                  |

### 2-C. `SearchScreen.kt`

File: `features/search/screen/SearchScreen.kt`

| #   | Issue                                                                                                                        | Fix                                                                                                                                  |
|-----|------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| S-1 | Category colour-mapping block duplicated vs. `ProjectListScreen`                                                             | Replace with `CategoryFilterChip` from Task 1-A                                                                                      |
| S-2 | Status badge duplicated vs. `ProjectListScreen`                                                                              | Replace with `ProjectStatusBadge` from Task 1-B                                                                                      |
| S-3 | `SearchSkeletonCard` builds its own `animateFloat` + `Brush` shimmer (lines 780–838)                                         | Replace with `Modifier.shimmerEffect()` from `AppShimmer.kt`                                                                         |
| S-4 | Large block of commented-out back-button code (lines 177–186)                                                                | Delete dead comments                                                                                                                 |
| S-5 | `SearchResultCard` icon/accentColor `when` blocks duplicated vs. `ProjectListScreen`                                         | Replace with shared extensions from Task PL-3                                                                                        |
| S-6 | `SearchResultCard` renders members count with `Icons.Default.Group` but `contentDescription = null` on an informational icon | Add `contentDescription = "${project.membersCount} members"`                                                                         |
| S-7 | `SearchScreen` entry point passes `viewModel` as a constructor parameter instead of using `koinViewModel()`                  | Follow the same pattern used in all other screens: `viewModel: SearchViewModel = koinViewModel()` inside the `@Composable` signature |

### 2-D. `MessagesListScreen.kt`

File: `features/messages/screen/MessagesListScreen.kt`

| #    | Issue                                                                                                 | Fix                                                                                            |
|------|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| ML-1 | `fontSize = 24.sp` for the "Zapmancer" title in `ZapmancerTopBar`                                     | Replace with `style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)`  |
| ML-2 | `fontSize = 12.sp`, `fontSize = 16.sp`, `fontSize = 14.sp` scattered throughout `ConversationItemRow` | Replace with `MaterialTheme.typography.labelSmall`, `.titleMedium`, `.bodyMedium` respectively |
| ML-3 | `fontSize = 12.sp` placeholder in `OutlinedTextField`                                                 | Replace with `style = MaterialTheme.typography.bodySmall`                                      |
| ML-4 | No `isLoading` / error handling — state fields not confirmed; verify `MessagesListUiState`            | If fields exist, add skeleton shimmer for loading and `ErrorState` for error                   |
| ML-5 | `Preview` uses `MaterialTheme` instead of `AppTheme`                                                  | Replace with `AppTheme` for consistent preview rendering                                       |

### 2-E. `MessagesDetailScreen.kt`

File: `features/messages/screen/MessagesDetailScreen.kt`

| #    | Issue                                                                                                                             | Fix                                                                               |
|------|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| MD-1 | Fully-qualified animation references (`androidx.compose.animation.core.rememberInfiniteTransition(...)`) inside `TypingIndicator` | Add proper imports and use short names                                            |
| MD-2 | `fontSize = 16.sp`, `fontSize = 12.sp`, `fontSize = 14.sp`, `fontSize = 11.sp` in `MessageBubble` and top bar                     | Replace with `MaterialTheme.typography.*` tokens                                  |
| MD-3 | Commented-out `MessageInput` call in `body` lambda (line 172)                                                                     | Remove dead comment                                                               |
| MD-4 | `TypingIndicator` animates `translationY` but the computed value is **never applied** to the `Box` modifier (the dots don't move) | Apply `graphicsLayer { translationY = translationY }` to each dot `Box`           |
| MD-5 | Action icons (`Videocam`, `Call`, `MoreVert`) have `contentDescription = null` but are interactive                                | Add descriptive `contentDescription` ("Video call", "Voice call", "More options") |

### 2-F. `SettingsScreen.kt`

File: `features/settings/screen/SettingsScreen.kt`

| #    | Issue                                                                                                  | Fix                                                                                               |
|------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| ST-1 | `fontSize = 30.sp` page title, `fontSize = 16.sp` / `fontSize = 13.sp` / `fontSize = 12.sp` throughout | Replace with `MaterialTheme.typography.displaySmall`, `.titleMedium`, `.bodySmall`, `.labelSmall` |
| ST-2 | `SettingsItem` `Row` has `.clickable {}` with a no-op lambda                                           | Either wire it to a real event or remove the `clickable` modifier                                 |
| ST-3 | `SettingsSection` `Card` border is `MaterialTheme.colorScheme.surface` (invisible)                     | Change to `MaterialTheme.colorScheme.outlineVariant`                                              |
| ST-4 | Search icon in the top bar has no associated action                                                    | Remove the icon or wire it to a navigation event (not a silent no-op)                             |

### 2-G. `PostProjectScreen.kt`

File: `features/projects/screen/PostProjectScreen.kt`

| #    | Issue                                                                                                                                                                           | Fix                                                                                                                          |
|------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| PP-1 | `fontSize = 30.sp`, `fontSize = 14.sp`, `fontSize = 16.sp`, `fontSize = 12.sp` scattered                                                                                        | Replace with `MaterialTheme.typography.*` tokens                                                                             |
| PP-2 | `OutlinedTextField` colour blocks for `focusedBorderColor / unfocusedBorderColor / focusedContainerColor / unfocusedContainerColor` repeated verbatim in every field (7 fields) | Extract to a `val sharedTextFieldColors = OutlinedTextFieldDefaults.colors(...)` local val at the top of the card composable |
| PP-3 | `Add` `IconButton` uses raw `.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))` modifier inside an `IconButton`                                          | Prefer `FilledIconButton` from Material 3 or add a `colors` parameter                                                        |
| PP-4 | `Icon(Icons.Default.Close)` for remove-skill / remove-deliverable uses `Modifier.clickable { ... }` instead of `IconButton`                                                     | Wrap in `IconButton` for correct touch target (48×48 dp)                                                                     |

### 2-H. `ProposalScreen.kt`

File: `features/proposal/screen/ProposalScreen.kt`

| #    | Issue                                                                                                                                                                   | Fix                                                                                                                                                                        |
|------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| PR-1 | `ProposalScreen` composable is overloaded — the same name `ProposalScreen` is used for both the VM-connected entry and the stateless content wrapper (lines 73 and 102) | Rename the inner version to `ProposalScreenContent` or `ProposalContentScaffold`                                                                                           |
| PR-2 | `containerColor = Color.White.copy(alpha = 0.8f)` hardcoded white in `ZapmancerTopBar`                                                                                  | Replace with `MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)`                                                                                                        |
| PR-3 | `fontSize = 18.sp`, `fontSize = 14.sp`, `fontSize = 11.sp`, `fontSize = 12.sp` scattered                                                                                | Replace with `MaterialTheme.typography.*` tokens                                                                                                                           |
| PR-4 | `BudgetSection` `Card` has no border                                                                                                                                    | Add `border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)` for visual consistency                                                                         |
| PR-5 | `PitchStep` PRO TIP banner uses `color = MaterialTheme.colorScheme.secondary` as `Surface` background (makes text unreadable on dark themes)                            | Use `color = MaterialTheme.colorScheme.secondaryContainer` and `contentColor = MaterialTheme.colorScheme.onSecondaryContainer`                                             |
| PR-6 | `Preview` uses `MaterialTheme {}` without calling the default `viewModel` constructor                                                                                   | The `ProposalScreen()` call in preview will try to inject a ViewModel; replace preview call with `ProposalScreenContent(state = ProposalUiState(), ...)` once PR-1 is done |

---

## 3. Execution Order

```
1-A → 1-B → 1-C          # shared components first
H-1                        # move ZapGold to theme
PL-3, S-5                 # extract category/icon extensions
PL-1, S-1                 # use CategoryFilterChip
PL-2, S-2                 # use ProjectStatusBadge
PL-4 → PL-5 → PL-6 → PL-7 → PL-8 → PL-9
S-3 → S-4 → S-6 → S-7
H-2 → H-3 → H-4 → H-5
ML-1..5
MD-1..5
ST-1..4
PP-1..4
PR-1..6
```

---

## 4. Verification Checklist

After each screen task, verify:

- [ ] `./gradlew :feature:presentation:compileKotlinMetadata` (or equivalent KMP compile task)
  passes with no errors.
- [ ] Android Studio Compose Preview renders the screen's `@Preview` function without crash.
- [ ] No raw `fontSize = N.sp` values remain in the edited file (grep: `fontSize\s*=\s*\d+\.sp`).
- [ ] No cross-feature VerticalDivider or shimmer re-implementations remain (grep:
  `animateFloat.*shimmer\|shimmerColors`).
- [ ] All interactive icons have non-null `contentDescription`.

---

## 5. Files NOT in Scope

The following files were reviewed and are already well-structured; no changes needed:

- `ProfileScreen.kt` — already cleaned up in the previous session.
- `ProjectDetailScreen.kt` — `VerticalDivider` already migrated to `common/components`.
- `AppShimmer.kt`, `EmptyState.kt`, `ErrorState.kt`, `ZapmancerTopBar.kt`, `UserAvatar.kt` — already
  clean shared components; only import them where missing.
