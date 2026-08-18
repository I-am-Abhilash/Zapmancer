Yes — for this you want the AI to behave more like a **senior product engineer + UX reviewer**, not simply a code reviewer. It should understand that the current navigation is intentionally messy for testing and produce a **human-readable remediation plan before changing anything**.

# React/TypeScript Web Application — Production Readiness & UX Audit

Analyze the **entire current web application** written in **TypeScript + React**.

The current webapp is **not production-ready**. During development, I intentionally created temporary, half-implemented navigation flows and shortcuts so I could test different screens and features quickly.

For example, some screens may currently be reachable through navigation paths that would not make sense in the real product, some buttons may lead directly to pages for testing, and some flows may be incomplete.

**Do not assume the current navigation structure represents the intended production UX.**

Your job is to understand the application as a real product and produce a **human-readable report describing what should be fixed, redesigned, removed, merged, or implemented before production.**

Do **not modify the code yet.**

---

# 1. Understand the Product First

Before reviewing individual pages, inspect the project and understand:

* What product this webapp represents.
* What the major user types/roles are.
* What each role is supposed to accomplish.
* What the major features are.
* What pages currently exist.
* What routes currently exist.
* How navigation is currently implemented.
* Which pages appear complete, partially implemented, placeholders, or test-only.
* How authentication affects navigation.
* How user roles affect available pages.
* How the frontend communicates with the backend.

Do not judge individual screens in isolation.

Try to understand the **intended product journey** first.

---

# 2. Build a Current Website Map

Create an internal map of the current application.

For example:

```text
Public
├── Landing
├── Login
├── Register
├── Forgot Password
└── ...

Authenticated
├── Dashboard
├── Profile
├── Messages
├── Notifications
└── ...

Client
├── Browse Projects
├── Project Details
├── Create Project
├── Proposals
├── Contracts
└── ...

Freelancer
├── Find Work
├── Applications
├── Active Contracts
├── Earnings
└── ...
```

Use the **actual project**, not this example.

Identify:

* Every route
* Every major page
* Nested routes
* Navigation menus
* Sidebars
* Headers
* Breadcrumbs
* Tabs
* Modals that behave like pages
* Important buttons that navigate somewhere
* Links between major workflows

---

# 3. Separate Testing Navigation From Production Navigation

This is extremely important.

Identify navigation that appears to exist primarily because I was testing the application.

Examples:

* Direct links to unfinished screens
* Temporary buttons
* Fake dashboard shortcuts
* Navigation to pages that users should not normally access directly
* Test-only routes
* Duplicate ways of reaching the same feature
* Pages accessible without satisfying required prerequisites
* Routes that bypass authentication or onboarding
* Routes that bypass normal workflow steps

For every suspicious navigation flow, determine:

1. Why it probably exists.
2. Whether it should exist in production.
3. What the correct production flow should be.
4. Whether the route should be removed, redirected, protected, or redesigned.

---

# 4. Analyze the Application Like a Real Human User

Do not only inspect source code.

Pretend you are an actual user.

Walk through the application from the perspective of each major user role.

For each role ask:

### First-time user

* Where do I start?
* Is it obvious what I should do?
* What happens after registration?
* Is onboarding clear?
* Do I know what to do next?

### Returning user

* Where do I land after login?
* Can I quickly reach the things I use most?
* Is the current state of my work obvious?
* Can I recover from unfinished tasks?

### Normal workflow

For each major workflow:

```text
Entry
 ↓
Decision
 ↓
Action
 ↓
Confirmation
 ↓
Next logical action
```

Determine whether the website actually supports this flow.

Identify places where the user would think:

> "Okay... what am I supposed to do now?"

---

# 5. Analyze Navigation Architecture

Review the current routing/navigation architecture.

Check:

* Route hierarchy
* Protected routes
* Role-based routes
* Nested routes
* Redirects
* Default routes
* 404 handling
* Unauthorized handling
* Deep linking
* Browser back/forward behavior
* Refreshing a page
* Opening a page directly
* Navigation state
* Query parameters
* URL structure

Look for situations where:

```text
Login → random page
```

instead of:

```text
Login
 ↓
Determine user state
 ↓
Determine role
 ↓
Determine onboarding state
 ↓
Determine pending actions
 ↓
Correct destination
```

---

# 6. Find Broken or Impossible User Journeys

Identify workflows that are currently impossible to complete.

Examples:

* User can create something but cannot view it afterward.
* User can view something but cannot edit it.
* User can edit something but cannot cancel/delete/archive it.
* User completes an action but has no obvious next step.
* User can access a detail page but cannot get back to the appropriate parent page.
* User can reach a page without satisfying prerequisites.
* A button exists but its destination is missing.
* A page exists but there is no realistic way for a user to discover it.
* A workflow requires manually entering a URL.
* A page depends on data that the user cannot create through the UI.

For each issue explain the complete expected workflow.

---

# 7. Analyze Every Page

For every significant page, evaluate:

### Purpose

* What is this page supposed to accomplish?
* Is that purpose obvious?

### Entry

* Where should users normally come from?

### Exit

* Where should users normally go next?

### Actions

* What actions should be available?
* Are important actions missing?
* Are irrelevant actions present?

### Information

* Is the information hierarchy logical?
* Is anything important missing?
* Is there unnecessary information?

### States

Check:

* Loading
* Empty
* Error
* Success
* Disabled
* Unauthorized
* Not found
* Partial data

### Responsiveness

Check whether the layout makes sense for:

* Desktop
* Tablet
* Smaller screens

Do not merely check whether the layout technically responds.

Check whether the **information architecture** still makes sense.

---

# 8. Identify Missing Pages

Determine whether the product appears to require pages that do not currently exist.

Examples:

* Missing settings
* Missing account management
* Missing onboarding
* Missing confirmation pages
* Missing detail pages
* Missing management pages
* Missing search/filter results
* Missing empty states
* Missing error pages
* Missing transaction/order/project history
* Missing notifications
* Missing help/support
* Missing admin functionality

Do not invent unnecessary pages.

Only recommend a page when there is a clear product or workflow reason.

For every recommendation explain:

* Why it is needed.
* Who needs it.
* What problem it solves.
* Where users should access it.

---

# 9. Identify Pages That Should Be Merged

Some pages may exist only because the original mobile/application structure was translated directly to web.

Determine whether multiple pages should instead become one coherent desktop experience.

Look for:

* Duplicate information
* Tiny single-purpose pages
* Pages that naturally belong together
* Separate screens that should be tabs
* Separate screens that should be sections
* Pages that create unnecessary navigation depth
* Mobile-style flows that feel awkward on desktop

For every proposed merge explain the reasoning.

---

# 10. Analyze Desktop Web UX

Do not treat the web application as a collection of mobile screens stretched horizontally.

Evaluate whether desktop space is being used intelligently.

Look for opportunities to use:

* Two-column layouts
* Side-by-side information
* Persistent sidebars
* Secondary panels
* Tabs
* Split views
* Tables
* Filters
* Context panels
* Inline editing
* Master/detail layouts
* Dashboard summaries

If a page contains large amounts of empty space, determine whether that space should be used meaningfully or whether the page should be redesigned.

---

# 11. Analyze Information Architecture

Determine whether the website has a coherent hierarchy.

Answer:

* What belongs in the primary navigation?
* What belongs in secondary navigation?
* What belongs in user/account menus?
* What belongs inside a resource page?
* What should be a tab?
* What should be a modal?
* What should be a dedicated page?
* What should never appear in navigation?

Recommend a production navigation structure.

---

# 12. Analyze User Roles & Permissions

For every major user role determine:

* What can they see?
* What can they create?
* What can they edit?
* What can they delete?
* What can they approve/reject?
* What pages should they have access to?
* What pages should be hidden?
* What happens when they attempt to access something unauthorized?

Make sure the UI does not merely hide buttons.

The routing layer should also enforce appropriate access.

---

# 13. Analyze Backend Integration

Inspect API/client integration where possible.

Find:

* Pages calling nonexistent APIs.
* APIs that have no UI.
* UI actions with no backend implementation.
* Wrong request/response assumptions.
* Missing loading states.
* Missing error handling.
* Optimistic updates that could become inconsistent.
* Pages using hardcoded/mock/test data.
* Temporary test navigation that assumes data exists.

Clearly distinguish:

```text
Frontend problem
Backend problem
Integration problem
Unknown / needs verification
```

---

# 14. Analyze State Management

Review how application state is handled.

Look for:

* Duplicate state
* Unnecessary global state
* Page state that should be URL state
* Filters that disappear on navigation
* State that breaks after refresh
* Stale data
* Incorrect caching
* State that should be server-derived
* Navigation depending on fragile local state

Recommend improvements only when they materially improve the application.

---

# 15. Analyze UX States

Every important user-facing operation should have appropriate states.

Check for:

### Loading

What does the user see while data loads?

### Empty

What happens when there is no data?

### Error

What happens when the API fails?

### Success

Does the user receive meaningful confirmation?

### Destructive action

Are delete/cancel operations confirmed appropriately?

### Unsaved changes

Can users accidentally lose work?

### Permission

What happens when access is denied?

### Not found

What happens when a resource no longer exists?

Flag missing states.

---

# 16. Analyze Visual Consistency

Review:

* Typography
* Spacing
* Buttons
* Forms
* Cards
* Tables
* Modals
* Dialogs
* Icons
* Navigation
* Colors
* Borders
* Shadows
* Empty states
* Error states

Look for inconsistent components that should become reusable components.

Do not recommend creating abstractions simply for the sake of abstraction.

---

# 17. Identify Dead/Temporary Code

Find:

* Test-only routes
* Placeholder pages
* Fake buttons
* Hardcoded data
* TODO navigation
* Dead components
* Unused routes
* Duplicate pages
* Mock API calls
* Temporary redirects
* Development-only shortcuts

Categorize each as:

* Remove
* Replace
* Complete
* Keep intentionally

---

# 18. Produce a Human-Readable Final Report

Do NOT simply dump a list of code issues.

Write the report as if you are explaining the website to the product owner and engineering team.

Use these sections:

## Executive Summary

Explain:

* Current overall state.
* What feels production-ready.
* What feels unfinished.
* Biggest UX problems.
* Biggest architectural problems.
* Biggest navigation problems.

## Current Product Structure

Explain what the website currently appears to be and how the major sections relate to each other.

## Current Navigation Problems

List the major navigation problems and why they are problematic.

## Recommended Production Navigation

Describe the navigation structure you recommend.

## User Journey Problems

For each major user role, describe broken or awkward journeys.

## Missing Pages / Features

List genuinely missing pages or functionality.

## Pages That Should Be Merged

Explain which pages should be combined and why.

## Pages That Should Be Removed

Identify test-only or unnecessary pages/routes.

## Page-by-Page Problems

For each important page:

```text
Page:
Current purpose:
Current problems:
Production purpose:
Recommended changes:
Missing functionality:
Navigation changes:
Priority:
```

## Backend Integration Problems

Separate frontend, backend, and integration issues.

## UX/UI Problems

Prioritize issues that materially affect usability.

## Technical Problems

Identify important React/TypeScript architecture problems.

## Production Readiness Score

Give scores for:

* Navigation
* UX
* Information architecture
* Functionality
* Backend integration
* Authentication/authorization
* Responsive design
* Error/loading states
* Code architecture
* Overall production readiness

Score each from 0–100.

---

# 19. Create a Prioritized Fix Roadmap

At the end, create a practical implementation roadmap.

### P0 — Must Fix Before Production

Issues that make the product unusable, insecure, or fundamentally confusing.

### P1 — High Priority

Major UX and workflow problems.

### P2 — Important

Significant improvements that should be completed before a polished release.

### P3 — Nice to Have

Improvements that can reasonably come later.

For each item include:

```text
Issue
Why it matters
Affected pages
Recommended solution
Dependencies
Priority
Estimated complexity: Low / Medium / High
```

---

# Most Important Rule

**Do not blindly preserve the existing navigation just because it already exists.**

The existing navigation contains temporary testing shortcuts.

Design the recommended production experience based on:

1. The actual product/domain.
2. The actual user roles.
3. The actual backend capabilities.
4. The actual frontend features.
5. Logical human workflows.
6. Good desktop web UX.
7. Consistency and discoverability.

Think like a combination of:

* Senior React engineer
* Product designer
* UX architect
* QA engineer
* Product manager

But **do not redesign things arbitrarily**.

Every recommendation should have a clear reason.

Before suggesting implementation, understand the existing system thoroughly.

The final goal is to answer:

> **"If we removed all of our temporary testing shortcuts and released this webapp to real users tomorrow, what would confuse them, what would break, what would be impossible to accomplish, and what should we change to make the website feel like a coherent production product?"**

**Do not modify any code yet. Produce the audit and recommended implementation plan first.**
