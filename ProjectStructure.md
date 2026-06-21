.
├── androidApp
│ ├── build.gradle.kts
│ └── src
│ └── main
│ ├── AndroidManifest.xml
│ ├── ic_launcher-playstore.png
│ ├── kotlin
│ │ └── com
│ │ └── smach
│ │ └── zapmancer
│ │ ├── MainActivity.kt
│ │ └── ZapmancerApp.kt
│ └── res
│ ├── drawable
│ │ └── ic_launcher_background.xml
│ ├── drawable-v24
│ │ └── ic_launcher_foreground.xml
│ ├── mipmap-anydpi-v26
│ │ ├── ic_launcher.xml
│ │ └── ic_launcher_round.xml
│ ├── mipmap-hdpi
│ │ ├── ic_launcher.webp
│ │ ├── ic_launcher_background.webp
│ │ ├── ic_launcher_foreground.webp
│ │ └── ic_launcher_round.webp
│ ├── mipmap-mdpi
│ │ ├── ic_launcher.webp
│ │ ├── ic_launcher_background.webp
│ │ ├── ic_launcher_foreground.webp
│ │ └── ic_launcher_round.webp
│ ├── mipmap-xhdpi
│ │ ├── ic_launcher.webp
│ │ ├── ic_launcher_background.webp
│ │ ├── ic_launcher_foreground.webp
│ │ └── ic_launcher_round.webp
│ ├── mipmap-xxhdpi
│ │ ├── ic_launcher.webp
│ │ ├── ic_launcher_background.webp
│ │ ├── ic_launcher_foreground.webp
│ │ └── ic_launcher_round.webp
│ ├── mipmap-xxxhdpi
│ │ ├── ic_launcher.webp
│ │ ├── ic_launcher_background.webp
│ │ ├── ic_launcher_foreground.webp
│ │ └── ic_launcher_round.webp
│ └── values
│ └── strings.xml
├── build.gradle.kts
├── core
│ ├── build.gradle.kts
│ ├── schemas
│ │ ├── com.smach.scriptside.core.database.db.AppDatabase
│ │ │ ├── 1.json
│ │ │ ├── 2.json
│ │ │ ├── 3.json
│ │ │ └── 4.json
│ │ └── com.smach.zapmancer.core.database.db.AppDatabase
│ │ └── 4.json
│ └── src
│ ├── androidMain
│ │ └── kotlin
│ │ └── com
│ │ └── smach
│ │ └── zapmancer
│ │ └── core
│ │ ├── common
│ │ │ └── utils
│ │ │ └── DataStoreBuilder.android.kt
│ │ └── database
│ │ └── db
│ │ └── AppDatabase.android.kt
│ ├── commonMain
│ │ └── kotlin
│ │ └── com
│ │ └── smach
│ │ └── zapmancer
│ │ └── core
│ │ ├── common
│ │ │ ├── base
│ │ │ │ └── BaseViewModel.kt
│ │ │ ├── di
│ │ │ │ └── CoreModule.kt
│ │ │ └── utils
│ │ │ ├── DataStoreBuilder.kt
│ │ │ ├── DataStoreStorage.kt
│ │ │ ├── ErrorMapper.kt
│ │ │ ├── Paginator.kt
│ │ │ └── Result.kt
│ │ ├── database
│ │ │ ├── dao
│ │ │ │ ├── ArticleDao.kt
│ │ │ │ └── RecentSearchDao.kt
│ │ │ ├── db
│ │ │ │ └── AppDatabase.kt
│ │ │ └── entity
│ │ │ ├── ArticleEntity.kt
│ │ │ └── RecentSearchEntity.kt
│ │ ├── monitoring
│ │ │ ├── AnalyticsService.kt
│ │ │ └── NapierAnalyticsService.kt
│ │ └── network
│ │ ├── ktor
│ │ │ ├── NetworkConstants.kt
│ │ │ ├── provideHttpClient.kt
│ │ │ ├── Response.kt
│ │ │ └── SafeApiCall.kt
│ │ ├── model
│ │ │ └── RefreshToken.kt
│ │ └── session
│ │ └── SessionManager.kt
│ └── iosMain
│ └── kotlin
│ └── com
│ └── smach
│ └── zapmancer
│ └── core
│ ├── common
│ │ └── utils
│ │ └── DataStoreBuilder.ios.kt
│ └── database
│ └── db
│ └── AppDatabase.ios.kt
├── detekt
│ └── detekt.yml
├── feature
│ ├── data
│ │ ├── build.gradle.kts
│ │ └── src
│ │ └── commonMain
│ │ └── kotlin
│ │ └── com
│ │ └── smach
│ │ └── zapmancer
│ │ └── data
│ │ ├── di
│ │ ├── mapper
│ │ ├── model
│ │ └── repository
│ ├── domain
│ │ ├── build.gradle.kts
│ │ └── src
│ │ └── commonMain
│ │ └── kotlin
│ │ └── com
│ │ └── smach
│ │ └── zapmancer
│ │ └── domain
│ │ ├── model
│ │ │ ├── HomeDashboard.kt
│ │ │ ├── Notification.kt
│ │ │ ├── User.kt
│ │ │ └── UserProfile.kt
│ │ ├── repository
│ │ │ ├── AuthRepository.kt
│ │ │ ├── HomeRepository.kt
│ │ │ ├── NotificationRepository.kt
│ │ │ └── ProfileRepository.kt
│ │ └── usecase
│ │ ├── ExecuteNotificationActionUseCase.kt
│ │ ├── ExportActivityCsvUseCase.kt
│ │ ├── GetHomeDashboardUseCase.kt
│ │ ├── GetNotificationsUseCase.kt
│ │ ├── GetUserProfileUseCase.kt
│ │ ├── HireUserUseCase.kt
│ │ └── SendNotificationQuickReplyUseCase.kt
│ └── presentation
│ ├── build.gradle.kts
│ └── src
│ └── commonMain
│ └── kotlin
│ └── com
│ └── smach
│ └── zapmancer
│ └── features
│ ├── alerts
│ │ ├── screen
│ │ │ └── NotificationScreen.kt
│ │ ├── state
│ │ │ └── NotificationUiState.kt
│ │ └── viewmodel
│ │ ├── HomeViewModel.kt
│ │ └── NotificationViewModel.kt
│ ├── auth
│ │ ├── screen
│ │ │ ├── ForgotPasswordScreen.kt
│ │ │ ├── LoginScreen.kt
│ │ │ ├── SignupScreen.kt
│ │ │ └── VerificationScreen.kt
│ │ ├── state
│ │ │ ├── ForgotPasswordUiState.kt
│ │ │ ├── LoginUiState.kt
│ │ │ ├── SignupUiState.kt
│ │ │ └── VerificationUiState.kt
│ │ └── viewmodel
│ │ ├── ForgotPasswordViewModel.kt
│ │ ├── LoginViewModel.kt
│ │ ├── SignupViewModel.kt
│ │ └── VerificationViewModel.kt
│ ├── common
│ │ ├── components
│ │ │ ├── AppImage.kt
│ │ │ ├── AppShimmer.kt
│ │ │ ├── ArticleShimmer.kt
│ │ │ ├── AuthComponents.kt
│ │ │ ├── EmptyState.kt
│ │ │ └── Shimmer.kt
│ │ ├── di
│ │ │ └── PresentationModule.kt
│ │ └── theme
│ │ ├── Color.kt
│ │ ├── Shape.kt
│ │ ├── Theme.kt
│ │ └── Type.kt
│ ├── home
│ │ ├── screen
│ │ │ └── HomeScreen.kt
│ │ ├── state
│ │ │ └── HomeUiState.kt
│ │ └── viewmodel
│ │ └── HomeViewModel.kt
│ ├── messages
│ │ ├── screen
│ │ │ ├── MessagesDetailScreen.kt
│ │ │ └── MessagesListScreen.kt
│ │ ├── state
│ │ │ ├── MessagesDetailUiState.kt
│ │ │ └── MessagesListUiState.kt
│ │ └── viewmodel
│ ├── profile
│ │ ├── screen
│ │ │ └── ProfileScreen.kt
│ │ ├── state
│ │ │ └── ProfileuiState.kt
│ │ └── viewmodel
│ │ └── ProfileViewModel.kt
│ ├── projects
│ │ ├── screen
│ │ │ ├── ProjectDetailScreen.kt
│ │ │ └── ProjectListScreen.kt
│ │ ├── state
│ │ │ ├── ProjectDetailUiState.kt
│ │ │ └── ProjectListUiState.kt
│ │ └── viewmodel
│ │ └── ProjectListViewModel.kt
│ ├── proposal
│ │ ├── screen
│ │ │ └── ProposalScreen.kt
│ │ ├── state
│ │ │ └── ProposalUiState.kt
│ │ └── viewmodel
│ └── settings
│ ├── screen
│ │ └── SettingsScreen.kt
│ ├── state
│ │ └── SettingsUiState.kt
│ └── viewmodel
│ └── SettingsViewModel.kt
├── gradle
│ ├── gradle-daemon-jvm.properties
│ ├── libs.versions.toml
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── iosApp
│ ├── Configuration
│ │ └── Config.xcconfig
│ ├── iosApp
│ │ ├── Assets.xcassets
│ │ │ ├── AccentColor.colorset
│ │ │ │ └── Contents.json
│ │ │ ├── AppIcon.appiconset
│ │ │ │ ├── app-icon-1024.png
│ │ │ │ └── Contents.json
│ │ │ └── Contents.json
│ │ ├── ContentView.swift
│ │ ├── Info.plist
│ │ ├── iOSApp.swift
│ │ └── 'Preview Content'
│ │ └── 'Preview Assets.xcassets'
│ │ └── Contents.json
│ └── iosApp.xcodeproj
│ ├── project.pbxproj
│ └── project.xcworkspace
│ └── contents.xcworkspacedata
├── local.properties
├── ProjectStructure.md
├── README.md
├── settings.gradle.kts
└── shared
├── build.gradle.kts
└── src
├── androidMain
│ └── kotlin
│ └── com
│ └── smach
│ └── zapmancer
├── commonMain
│ ├── composeResources
│ │ └── drawable
│ │ └── compose-multiplatform.xml
│ └── kotlin
│ └── com
│ └── smach
│ └── zapmancer
│ ├── App.kt
│ ├── di
│ │ └── Koin.kt
│ ├── MainViewModel.kt
│ └── nav
│ ├── AuthGraph.kt
│ ├── BottomNavigationBar.kt
│ ├── MainGraph.kt
│ └── Navigation.kt
├── commonTest
│ └── kotlin
│ └── com
│ └── smach
│ └── zapmancer
│ └── ComposeAppCommonTest.kt
└── iosMain
└── kotlin
└── com
└── smach
└── zapmancer
└── MainViewController.kt
