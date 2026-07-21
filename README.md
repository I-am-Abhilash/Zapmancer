<h1 align="center">Zapmancer</h1>

<p align="center">
  A modern, open-source, full-stack cross-platform Freelance & Talent Marketplace powered by <strong>Kotlin Multiplatform</strong>, <strong>Compose Multiplatform</strong>, and <strong>Ktor Server</strong>.
</p>

---

## Mission & Vision

Existing freelancing platforms often extract high fees (10% to 20%+) from hard-working freelancers and clients. **Zapmancer** is being built by the community as a transparent, open-source alternative designed to put workers first.

* **Community-Driven**: Built collaboratively by open-source contributors around the world.
* **Managed Infrastructure**: Core hosted infrastructure is maintained with minimal cost-recovery overhead to keep freelancing fair and accessible to everyone.
* **Fair Payments & Escrow**: Transparent payment workflows, milestone protection, and escrow solutions are actively being designed with community feedback.

---

## Multi-Platform Roadmap

We are progressively building a unified ecosystem across mobile and desktop/web:

-  **Ktor Backend Server**: Asynchronous REST API, WebSockets & PostgreSQL database.
-  **Android Application**: Native experience powered by Compose Multiplatform & Material 3.
-  **iOS Application**: Native iOS client using Compose Multiplatform & Kotlin Multiplatform shared core.
-  **Web Application**: Browser version to support seamless desktop access.
-  **Payment & Escrow Integration**: Transparent payment gateway and milestone escrow solutions.

---

## Key Features

- **Cross-Platform Client**: Unified Android, iOS, and Web clients built on Compose Multiplatform.
- **Asynchronous Ktor Server**: High-concurrency backend engine built on Ktor and Netty.
- **Real-Time Messaging**: WebSockets-driven chat with live typing status, online presence tracking, and read receipts.
- **AI Recommendation Engine**: Integrated with Gorse for smart developer-project matching and personalized recommendation feeds.
- **Project & Proposal Hub**: End-to-end workflow for posting projects, browsing listings, submitting proposals, and managing hires.
- **Authentication & Security**: Secure JWT authentication, bcrypt password hashing, 2FA, and OTP verification flows.
- **Database & Storage**: PostgreSQL 16 database managed via Exposed ORM, paired with RustFS S3-compatible object storage.
- **Adaptive Design**: Modern UI with Clean MVI Architecture and light/dark theme support.

---

## Tech Stack

### Client (Android, iOS & Web)
- **Language**: Kotlin Multiplatform (`2.4.0`)
- **UI Framework**: Compose Multiplatform (`1.11.1`) & Material 3 Adaptive Layouts
- **Architecture**: Clean Architecture with MVI (Model-View-Intent)
- **Dependency Injection**: Koin (`4.2.2`)
- **Networking**: Ktor Client (`3.5.1`) with WebSockets & Kotlinx Serialization
- **Persistence**: SQLDelight (`2.3.2`) & Room (`2.8.4`)

### Server
- **Framework**: Ktor Server (`3.5.1`) on Netty
- **Database**: PostgreSQL 16 with Exposed ORM (`1.3.1`) & HikariCP
- **Recommendation Engine**: Gorse Recommender System (Master & Worker cluster)
- **Object Storage**: RustFS (S3-compatible storage)
- **Monitoring & Docs**: Micrometer / Prometheus & Swagger OpenAPI

---

## Repository Structure

```
Zapmancer/
├── androidApp/          # Android entry point & configuration
├── iosApp/              # iOS entry point & Xcode project workspace
├── shared/              # Shared multiplatform application setup
├── core/                # Core domain models, security, and shared framework
├── feature/
│   ├── data/            # Feature repositories & API client sources
│   ├── domain/          # Use cases & domain business logic
│   └── presentation/    # Compose UI composables & ViewModels
├── server/              # Ktor backend server
├── docker-compose.yml   # Infrastructure setup (Postgres, Gorse, RustFS, App)
└── gorse-config.toml    # Recommendation engine configuration
```

---

## Call for Contributions

Zapmancer is an ambitious open-source initiative built by developers, for developers. We are actively looking for contributors of all skill levels to help build a fairer freelancing ecosystem.

### How You Can Contribute

* **Android & iOS Engineers**: Help refine Compose Multiplatform UI components, state management, and native platform features.
* **Web Developers**: Join the effort to build the Compose Web / Kotlin Wasm frontend version.
* **Backend Developers**: Enhance Ktor routes, optimize PostgreSQL queries, improve Gorse recommendation algorithms, and expand security features.
* **Payment & Escrow Strategists**: Contribute to designing low-fee payment workflows, milestone mechanisms, and dispute resolution models.
* **UI/UX Designers**: Help craft intuitive, high-precision user interfaces and design systems.

### How to Get Started

1. Fork this repository and clone it to your local machine.
2. Check out the open issues or open a discussion with your ideas.
3. Submit a Pull Request (PR) with your improvements.

All feedback, feature suggestions, and pull requests are warmly welcomed.

---

## License

This project is open source and available under the [MIT License](LICENSE).
