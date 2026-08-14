# Zapmancer Hybrid Architecture: Production Role-Aware Company OS & Marketplace

## Executive Summary

**Zapmancer** is a hybrid software engineering operating system and talent network. It bridges the gap between **Internal Team Operations** (employee management, task tracking, payroll) and **On-Demand Talent Augmentation** (0% developer fee freelance marketplace).

---

## Production Role-Aware Navigation Model

Rather than static dev mode toggles in the main topbar, Zapmancer uses a **production-grade role-aware navigation engine** based on the logged-in user session context:

### 1. 🏢 Company Account Context (Client / Founder / CTO View)
- **Top Navigation Bar**:
  - `Company Dashboard` (`/company/dashboard`)
  - `Team Roster`
  - `Post Project Bounty` (`/projects/new`)
  - `Find Talent` (`/search`)
- **Profile Dropdown**:
  - Displays Company Name (`Acme AI Systems`) & Admin Role
  - Account Switcher: *"Switch to Freelancer Profile"*

### 2. 👨‍💻 Freelancer Account Context (Engineer View)
- **Top Navigation Bar**:
  - `My Dashboard` (`/home`)
  - `Find Work` (`/projects`)
  - `My Deliverables & PRs`
  - `Earnings & Wallet`
- **Profile Dropdown**:
  - Displays Developer Name (`Elena Rostova`) & Verified Talent Badge
  - Account Switcher: *"Switch to Company: Acme AI Systems"*

### 3. 🌐 Public / Guest Context (Discovery Hub)
- **Top Navigation Bar**:
  - `Find Work` (`/projects`)
  - `Find Talent` (`/search`)
  - `Pricing & Escrow`
  - `Log In` / `Sign Up`

---

## Target Audience & Product Fit

| Target Category | Primary Pain Point | Zapmancer Solution |
| :--- | :--- | :--- |
| **Tech Startups (5-50 devs)** | Paying for 4+ fragmented SaaS tools (Jira, Deel, Upwork, Slack). | Unified Company Portal: Manage team sprints, process payroll, and instant-hire extra devs in 1 click. |
| **Software Agencies & Dev Shops** | Deadlines hit when internal staff is 100% allocated. | Manage core employee roster in Zapmancer, and pull in vetted specialized freelancers for 2-week sub-tasks. |
| **Remote KMP & Web3 Labs** | Cross-border payments, IP ownership transfer, and auditability. | Milestone escrow locking, automated legal Work-for-Hire IP transfer, and Apache 2.0 open-source transparency. |

---

## Technical Stack & Design System Compliance

- **Framework**: React 19, TypeScript, Vite.
- **Design System**: Green Deck System (`green-deck-DESIGN.md`).
- **Styling**: Vanilla CSS feature modules (`company.css`, `settings.css`, `projects.css`) powered by Tailwind design system tokens.
- **Typography**: DM Sans (Display & Body) + JetBrains Mono (Code).
- **Theme Support**: Seamless Light & Dark mode support via central CSS custom properties.
