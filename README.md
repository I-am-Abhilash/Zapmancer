<h1 align="center">Zapmancer</h1>

<p align="center">
  A modern, open-source, full-stack <strong>Hybrid Company OS & Freelance Network</strong> powered by <strong>Kotlin Multiplatform</strong>, <strong>Compose Multiplatform</strong>, and <strong>Ktor Server</strong>.
</p>

---

## Mission & Hybrid Architecture

**Zapmancer** bridges the gap between **Internal Company Operations** (employee management, sprint task tracking, payroll) and **On-Demand Talent Augmentation** (0% developer fee freelance marketplace).

### 3-Dashboard Ecosystem

1. **🏢 Company Dashboard (Company OS)**: Manage internal employees, track sprint milestones, process salaries, and **instantly hire fellow developers** into active project tasks with 1 click.
2. **👨‍💻 Talent Dashboard (Employee & Freelancer View)**: Track assigned company tasks, submit code PR deliverables, view pending escrow funds, and receive instant payouts.
3. **🌐 Public Marketplace**: Open discovery hub for browsing verified contract bounties and finding top KMP, Mobile, WebAssembly, and Ktor engineers.

* **Community-Driven**: Built collaboratively by open-source contributors around the world under Apache 2.0 / MIT.
* **0% Developer Commission**: Eliminates 20% platform extractions; developers keep 100% of their earned milestone payouts.
* **Legal Safety & IP Protection**: Automated Work-for-Hire legal copyright transfer attached to every milestone payout release.

---

## Multi-Platform Roadmap

We are progressively building a unified ecosystem across mobile and desktop/web:

- 🚀 **Ktor Backend Server**: Asynchronous REST API, WebSockets & PostgreSQL database.
- 📱 **Android Application**: Native experience powered by Compose Multiplatform & Material 3.
- 🍏 **iOS Application**: Native iOS client using Compose Multiplatform & Kotlin Multiplatform shared core.
- 💻 **Web & Desktop Application**: Web/Desktop platform supporting Company OS and Marketplace operations ([ZAPMANCER_HYBRID_ARCHITECTURE.md](docs/ZAPMANCER_HYBRID_ARCHITECTURE.md)).

---

## Key Features

- **Company Operations Portal**: Manage full-time team staff, internal sprint tasks, and payroll in a unified dashboard.
- **1-Click Instant Fellow Dev Hire**: Instantly augment company sprint tasks with verified external contract talent.
- **Milestone Escrow & 0% Developer Fee**: Funds locked in smart escrow before coding begins and released instantly upon sign-off.
- **Real-Time Messaging**: WebSockets-driven chat with live typing status, online presence tracking, and file previews.
- **Gorse AI Recommendation Engine**: Smart matching between project requirements and verified developer skill graphs.
- **Automated Work-for-Hire IP Transfer**: Standardized legal copyright assignment generated upon escrow payout release.

---

## Tech Stack

### Client (Android, iOS & Web)
- **Language**: Kotlin Multiplatform (`2.4.0`)
- **UI Framework**: Compose Multiplatform (`1.11.1`), React 19, TypeScript
- **Styling**: Green Deck Central Design Tokens ([green-deck-DESIGN.md](app/webApp/green-deck-DESIGN.md))
- **Networking**: Ktor Client (`3.5.1`) with WebSockets & Kotlinx Serialization

### Server
- **Framework**: Ktor Server (`3.5.1`) on Netty
- **Database**: PostgreSQL 16 with Exposed ORM (`1.3.1`) & HikariCP
- **Recommendation Engine**: Gorse Recommender System (Master & Worker cluster)
- **Object Storage**: RustFS (S3-compatible storage)

---

## Repository Structure

```
Zapmancer/
├── app/webApp/                  # React + TypeScript Web App & Company OS
│   ├── src/features/company/   # Company Dashboard & Team Management
│   ├── src/features/projects/  # Find Work & Project Bounties
│   ├── src/features/search/    # Find Talent Discovery Hub
│   └── ZAPMANCER_HYBRID_ARCHITECTURE.md # Full Architecture Specification
├── shared/                      # Shared multiplatform application setup
├── core/                        # Core domain models, security, and shared framework
├── server/                      # Ktor backend server
└── docker-compose.yml           # Infrastructure setup (Postgres, Gorse, RustFS)
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
