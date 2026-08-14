# Zapmancer Long-Term Product & Technical Roadmap

## Overview

This document outlines the multi-phase implementation roadmap for **Zapmancer** — transforming it into the premier **Hybrid Company OS & Freelance Network**.

---

## 🗺️ Multi-Phase Strategic Plan

### Phase 1: Core Company OS & 3-Dashboard Navigation (Current)
- Topbar Mode Switcher (`Company OS`, `Talent Portal`, `Public Marketplace`).
- `src/features/company/` feature module (`CompanyDashboardPage.tsx`, `company.css`).
- **Team Roster & Employee Management**: Roles (`Owner`, `Manager`, `Dev`, `HR`), status, salary/rate.
- **Sprint & Internal Task Management**: Task assignment, conversion of internal tasks to public bounties.
- **"＋ Add Fellow Dev to Task" Modal**: Instant 1-click hire from the Zapmancer talent network into company tasks.
- **Payroll & Escrow Manager**: Unified monthly salary payouts + milestone releases.

### Phase 2: Talent Portal & Multi-Workspace Experience
- Multi-Workspace Selector in Employee Dashboard (`HomePage.tsx`).
- Developer Task Management & PR Deliverable Submission flow.
- Earnings & Escrow Payout Wallet with bank/crypto transfer logs.

### Phase 3: Automated Escrow & Work-for-Hire Contracts
- Smart Escrow Milestone Funding & Release Engine.
- Automated legal Work-for-Hire IP Transfer contract generator upon milestone payout release.
- 0% Developer Commission + 3% Client Escrow Recovery fee engine.

### Phase 4: Time Tracking, GitHub Sync & Gorse AI Matching
- GitHub PR commit tracking and time log recording for company tasks.
- Gorse AI recommendation engine integration for instant talent suggestions in the "Add Fellow Dev" modal.

### Phase 5: Global Payroll & Legal Compliance
- Cross-border payroll processing via Stripe Connect & Web3 USDC stablecoins.
- Automated tax documentation (W-8BEN / W-9) & KYC biometric identity verification integration.

### Phase 6: Native Mobile Apps (Compose Multiplatform)
- Port Company Workspace & Instant Dev Hire features to Android & iOS clients using Compose Multiplatform shared KMP core.

---

## Technical Standards & Guidelines

- **Architecture**: Clean MVI / Modular Feature Structure.
- **Design Tokens**: 100% adherence to `green-deck-DESIGN.md` tokens.
- **Theme Support**: Verbatim Light & Dark mode support via CSS variables.
- **Typography**: DM Sans (Display & Body) + JetBrains Mono (Code).
