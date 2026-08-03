Yes, absolutely. You **do not need a local database** just to store authentication tokens on Android or iOS.

For your KMP app, you can use **secure platform storage** instead.

A typical architecture is:

```text
User logs in
    ↓
Ktor Backend
    ↓
Returns accessToken + refreshToken
    ↓
KMP Client
    ├── Access token → memory
    │
    └── Refresh token → Secure Storage
                          ├── Android → Encrypted storage / Keystore-backed solution
                          └── iOS → Keychain
```

### Recommended approach

Use:

* **Access token** → keep in memory when possible
* **Refresh token** → store securely
* **User profile / cached data** → SQLDelight or another local database if you need offline access

So your app doesn't need:

```text
SQLDelight
    ↓
tokens table
```

just for authentication.

Instead, you can have a small abstraction in your shared KMP code:

```kotlin
interface SecureStorage {

    suspend fun getRefreshToken(): String?

    suspend fun saveRefreshToken(token: String)

    suspend fun removeRefreshToken()
}
```

Then platform implementations:

```text
commonMain
    SecureStorage interface
          │
          ├── Android → secure encrypted storage
          │
          └── iOS → Keychain
```

Your authentication flow becomes:

```text
App starts
    ↓
Read refresh token from secure storage
    ↓
Token exists?
    │
    ├── No → Show Login
    │
    └── Yes
          ↓
    Call /auth/refresh
          │
          ├── Success → User is authenticated
          │
          └── Failure → Delete refresh token → Show Login
```

When the access token expires:

```text
API request
    ↓
401 Unauthorized
    ↓
Use refresh token
    ↓
POST /auth/refresh
    ↓
Get new access token
    ↓
Retry original request
```

### For your project

I would recommend:

```text
             Authentication
                    │
          ┌─────────┴─────────┐
          │                   │
    Access Token        Refresh Token
          │                   │
       Memory          Secure Storage
                              │
                    ┌─────────┴─────────┐
                    │                   │
                 Android              iOS
                Keystore             Keychain
```

Then use a database like SQLDelight **only if you need local persistence for application data**, such as:

* Offline articles
* Drafts
* Cached feeds
* Bookmarks
* Recently viewed articles

So **authentication and local database are completely separate concerns**. You can have a fully authenticated KMP Android/iOS app with **zero local database**.


/home/xe23/Projects-Working/Zapmancer/Plan.md documents that the project does not need a local SQLDelight database for auth tokens — secure platform storage (Android Keystore / iOS Keychain) is the proper path. The current client uses SQLDelight for a trivial KeyValue table (one table, two columns, two queries) accessed through DataStoreStorage from SessionManager (tokens, userId, onboarding flag) and SettingsRepositoryImpl (dark mode toggle).

The project at Zapmancer/ mixes a client (KMP) and a server (Ktor + Exposed + Flyway). SQLDelight only appears in the client modules: core, webApp, plus the version catalog. The server has its own com.smach.zapmancer.core.database package containing Exposed table definitions and a DatabaseFactory — that is a different, hand-written namespace and must not be touched.

Goal: delete every SQLDelight reference (plugin, library, runtime, schema, generated code, driver code, AppDatabase consumer) from the project. Replace the trivial DataStoreStorage with an in-memory implementation that preserves the existing API surface, so SessionManager and SettingsRepositoryImpl continue to compile and run unchanged. Persistence of tokens and dark-mode toggle is lost across app restarts — accepted as a transitional state until a secure storage layer is implemented per Plan.md.

Files to modify

Catalog & root build

- gradle/libs.versions.toml
    - Delete sqldelight = "2.3.2" version ref (line 42).
    - Delete async-extensions1, sqldelight-android-driver, sqldelight-native-driver, sqldelight-webworker, sqldelight-coroutines-extensions library entries (lines 69, 71–74).
    - Delete sqldelight plugin alias (line 177).
- build.gradle.kts — remove alias(libs.plugins.sqldelight) apply false (line 11).

core module (KMP library)

- core/build.gradle.kts
    - Remove alias(libs.plugins.sqldelight) from plugins block (line 7).
    - Remove implementation(libs.sqldelight.coroutines.extensions) from commonMain (line 60).
    - Remove implementation(libs.async.extensions1) from commonMain (line 73) — only consumer was SQLDelight async support.
    - Remove implementation(libs.sqldelight.android.driver) from androidMain (line 83).
    - Remove implementation(libs.sqldelight.native.driver) from iosMain (line 87).
    - Remove implementation(libs.sqldelight.webworker) from webMain (line 99).
    - Delete the entire sqldelight { databases { ... } } block (lines 109–116).
- core/src/commonMain/sqldelight/com/smach/zapmancer/core/database/AppDatabase.sq — delete file.
- core/src/commonMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreBuilder.kt
    - Delete entirely (expect decl + DATABASE_NAME constant had no other consumer).
- core/src/commonMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreStorage.kt
    - Replace contents with in-memory implementation. Keep package + class name + public surface (saveString(key, value) suspend, getString(key): Flow<String?>). Internal: MutableMap<String, String> + MutableStateFlow<Map<String, String>> so observers see updates.
- core/src/commonMain/kotlin/com/smach/zapmancer/core/common/di/CoreModule.kt
    - Remove import com.smach.zapmancer.core.common.utils.createDatabaseDriver.
    - Remove import com.smach.zapmancer.core.database.AppDatabase.
    - Remove single { createDatabaseDriver() } and single { AppDatabase(get()) } (lines 14–15). Keep single { DataStoreStorage(get()) } — note: DataStoreStorage no longer needs DI params, change get() to plain construction DataStoreStorage().
- core/src/androidMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreBuilder.android.kt — delete file.
- core/src/iosMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreBuilder.ios.kt — delete file.
- core/src/jvmMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreBuilder.jvm.kt — delete file.
- core/src/webMain/kotlin/com/smach/zapmancer/core/common/utils/DataStoreBuilder.web.kt — delete file.

webApp module

- webApp/build.gradle.kts
    - Remove npm("@cashapp/sqldelight-sqljs-worker", "2.3.2") (line 27).
    - Remove npm("sql.js", "1.8.0") (line 28) — only used by SQLDelight web worker.
    - Remove implementation(libs.sqldelight.webworker) (line 30).
- webApp/src/webMain/kotlin/com/smach/zapmancer/main.kt
    - Remove import app.cash.sqldelight.db.SqlDriver.
    - Remove import com.smach.zapmancer.core.database.AppDatabase.
    - Remove the koinApp.koin.get<SqlDriver>() + AppDatabase.Schema.create(driver).await() block (lines 23–28). Koin init + ComposeViewport boot remain.

Generated / build artefacts (auto-cleared)

- core/build/generated/sqldelight/ — auto-removed on next Gradle run once plugin is gone. No manual deletion needed.

Untouched (verified)

- server/src/main/kotlin/com/smach/zapmancer/core/database/ — server-side Exposed code, not SQLDelight.
- feature/data, feature/domain, feature/presentation — no SQLDelight refs.
- androidApp, iosApp — no SQLDelight refs.
- build-logic/ — no SQLDelight refs.
- settings.gradle.kts — no SQLDelight pluginManagement entry.
- core/src/.../network/session/SessionManager.kt — imports DataStoreStorage only, surface unchanged so it keeps compiling.
- feature/data/src/.../SettingsRepositoryImpl.kt — imports DataStoreStorage only, surface unchanged so it keeps compiling.

New DataStoreStorage shape (commonMain)

package com.smach.zapmancer.core.common.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class DataStoreStorage {
private val state = MutableStateFlow<Map<String, String>>(emptyMap())

    suspend fun saveString(key: String, value: String) {
        state.value = state.value + (key to value)
    }

    fun getString(key: String): Flow<String?> =
        state.asStateFlow().map { it[key] }
}

No expect/actual, no platform driver, no DI params. Tokens and dark-mode toggle live in process memory and reset on app restart.

Verification

1. cd /home/xe23/Projects-Working/Zapmancer && ./gradlew :core:assemble — must compile all four targets (android, jvm, ios*, web) with no SQLDelight classes on classpath.
2. ./gradlew :webApp:assemble — webpack bundle must build without sqljs-worker resolution.
3. grep -rn "sqldelight\|SqlDriver\|AppDatabase\|createDatabaseDriver\|DataStoreBuilder" /home/xe23/Projects-Working/Zapmancer --include="*.kt" --include="*.kts" --include="*.toml" (excluding build/ and .gradle/) must return zero matches in client modules (server core.database is fine — different namespace).
4. ./gradlew :core:test —