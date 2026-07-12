.
├── androidApp
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── ic_launcher-playstore.png
│           ├── kotlin
│           │   └── com
│           │       └── smach
│           │           └── zapmancer
│           │               ├── MainActivity.kt
│           │               └── ZapmancerApp.kt
│           └── res
│               ├── drawable
│               │   └── ic_launcher_background.xml
│               ├── drawable-v24
│               │   └── ic_launcher_foreground.xml
│               ├── mipmap-anydpi-v26
│               │   ├── ic_launcher.xml
│               │   └── ic_launcher_round.xml
│               ├── mipmap-hdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-mdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xxhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xxxhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               └── values
│                   └── strings.xml
├── build-logic
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src
│       └── main
│           └── kotlin
│               ├── database-convention.gradle.kts
│               ├── feature-generator.gradle.kts
│               ├── ktor-server-convention.gradle.kts
│               └── template-utils.gradle.kts
├── build.gradle.kts
├── CLAUDE.md
├── cloudbuild.yaml
├── core
│   ├── build.gradle.kts
│   └── src
│       ├── androidMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       ├── common
│       │                       │   └── utils
│       │                       │       └── DataStoreBuilder.android.kt
│       │                       └── network
│       ├── commonMain
│       │   ├── kotlin
│       │   │   └── com
│       │   │       └── smach
│       │   │           └── zapmancer
│       │   │               └── core
│       │   │                   ├── common
│       │   │                   │   ├── base
│       │   │                   │   │   └── BaseViewModel.kt
│       │   │                   │   ├── di
│       │   │                   │   │   └── CoreModule.kt
│       │   │                   │   ├── dto
│       │   │                   │   │   ├── AuthDtos.kt
│       │   │                   │   │   ├── CommonResponse.kt
│       │   │                   │   │   ├── HomeDtos.kt
│       │   │                   │   │   ├── MessageDtos.kt
│       │   │                   │   │   ├── NotificationDtos.kt
│       │   │                   │   │   ├── ProjectDtos.kt
│       │   │                   │   │   ├── ProposalDtos.kt
│       │   │                   │   │   ├── SettingsDtos.kt
│       │   │                   │   │   └── UserDtos.kt
│       │   │                   │   └── utils
│       │   │                   │       ├── DataStoreBuilder.kt
│       │   │                   │       ├── DataStoreStorage.kt
│       │   │                   │       ├── ErrorMapper.kt
│       │   │                   │       ├── Paginator.kt
│       │   │                   │       ├── Result.kt
│       │   │                   │       └── ResultExt.kt
│       │   │                   ├── monitoring
│       │   │                   │   ├── AnalyticsService.kt
│       │   │                   │   └── NapierAnalyticsService.kt
│       │   │                   └── network
│       │   │                       ├── ktor
│       │   │                       │   ├── NetworkConstants.kt
│       │   │                       │   ├── provideHttpClient.kt
│       │   │                       │   ├── Response.kt
│       │   │                       │   └── SafeApiCall.kt
│       │   │                       ├── model
│       │   │                       │   └── RefreshToken.kt
│       │   │                       └── session
│       │   │                           └── SessionManager.kt
│       │   └── sqldelight
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       └── database
│       │                           └── AppDatabase.sq
│       ├── commonTest
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       ├── common
│       │                       │   └── utils
│       │                       │       └── DefaultPaginatorTest.kt
│       │                       └── network
│       │                           └── ktor
│       │                               └── SafeApiCallTest.kt
│       ├── iosMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       └── common
│       │                           └── utils
│       │                               └── DataStoreBuilder.ios.kt
│       ├── jvmMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   ├── common
│       │                   │   ├── Result.kt
│       │                   │   └── Sanitizer.kt
│       │                   ├── core
│       │                   │   └── common
│       │                   │       └── utils
│       │                   │           └── DataStoreBuilder.jvm.kt
│       │                   ├── database
│       │                   │   ├── DatabaseFactory.kt
│       │                   │   └── Tables.kt
│       │                   ├── framework
│       │                   │   ├── di
│       │                   │   │   └── storageModule.kt
│       │                   │   ├── FrameworkPlugins.kt
│       │                   │   └── storage
│       │                   │       ├── GcsStorageService.kt
│       │                   │       ├── S3StorageService.kt
│       │                   │       └── StorageService.kt
│       │                   ├── recommendations
│       │                   │   ├── GorseClient.kt
│       │                   │   └── GorseModule.kt
│       │                   └── security
│       │                       └── Security.kt
│       └── webMain
│           └── kotlin
│               └── com
│                   └── smach
│                       └── zapmancer
│                           └── core
│                               ├── common
│                               │   └── utils
│                               │       └── DataStoreBuilder.web.kt
│                               └── network
├── detekt
│   └── detekt.yml
├── docker-compose.yml
├── Dockerfile
├── domain_implementation_plan.md
├── feature
│   ├── data
│   │   ├── build.gradle.kts
│   │   └── src
│   │       └── commonMain
│   │           └── kotlin
│   │               └── com
│   │                   └── smach
│   │                       └── zapmancer
│   │                           └── data
│   │                               ├── di
│   │                               │   └── DataModule.kt
│   │                               ├── mapper
│   │                               ├── model
│   │                               └── repository
│   │                                   ├── AuthRepositoryImpl.kt
│   │                                   ├── HomeRepositoryImpl.kt
│   │                                   ├── MessageRepositoryImpl.kt
│   │                                   ├── NotificationRepositoryImpl.kt
│   │                                   ├── ProfileRepositoryImpl.kt
│   │                                   ├── ProjectRepositoryImpl.kt
│   │                                   ├── ProposalRepositoryImpl.kt
│   │                                   └── SettingsRepositoryImpl.kt
│   ├── domain
│   │   ├── build.gradle.kts
│   │   └── src
│   │       ├── commonMain
│   │       │   └── kotlin
│   │       │       └── com
│   │       │           └── smach
│   │       │               └── zapmancer
│   │       │                   └── domain
│   │       │                       ├── model
│   │       │                       │   ├── Conversation.kt
│   │       │                       │   ├── HomeDashboard.kt
│   │       │                       │   ├── Message.kt
│   │       │                       │   ├── Notification.kt
│   │       │                       │   ├── Project.kt
│   │       │                       │   ├── ProjectDetail.kt
│   │       │                       │   ├── Proposal.kt
│   │       │                       │   ├── SettingsData.kt
│   │       │                       │   ├── User.kt
│   │       │                       │   └── UserProfile.kt
│   │       │                       ├── repository
│   │       │                       │   ├── AuthRepository.kt
│   │       │                       │   ├── HomeRepository.kt
│   │       │                       │   ├── MessageRepository.kt
│   │       │                       │   ├── NotificationRepository.kt
│   │       │                       │   ├── ProfileRepository.kt
│   │       │                       │   ├── ProjectRepository.kt
│   │       │                       │   ├── ProposalRepository.kt
│   │       │                       │   └── SettingsRepository.kt
│   │       │                       └── usecase
│   │       │                           ├── ApplyProjectUseCase.kt
│   │       │                           ├── ExecuteNotificationActionUseCase.kt
│   │       │                           ├── ExportActivityCsvUseCase.kt
│   │       │                           ├── ForgotPasswordUseCase.kt
│   │       │                           ├── GetConversationsUseCase.kt
│   │       │                           ├── GetHomeDashboardUseCase.kt
│   │       │                           ├── GetMessagesUseCase.kt
│   │       │                           ├── GetNotificationsUseCase.kt
│   │       │                           ├── GetProjectDetailUseCase.kt
│   │       │                           ├── GetProjectProposalsUseCase.kt
│   │       │                           ├── GetProjectsUseCase.kt
│   │       │                           ├── GetSettingsUseCase.kt
│   │       │                           ├── GetUserProfileUseCase.kt
│   │       │                           ├── HireUserUseCase.kt
│   │       │                           ├── LoginUseCase.kt
│   │       │                           ├── LogoutUseCase.kt
│   │       │                           ├── MarkConversationAsReadUseCase.kt
│   │       │                           ├── PostProjectUseCase.kt
│   │       │                           ├── SaveProjectUseCase.kt
│   │       │                           ├── SendMessageUseCase.kt
│   │       │                           ├── SendNotificationQuickReplyUseCase.kt
│   │       │                           ├── SignUpUseCase.kt
│   │       │                           ├── SubmitProposalUseCase.kt
│   │       │                           ├── UpdateProfileUseCase.kt
│   │       │                           ├── UpdateSettingsUseCase.kt
│   │       │                           └── VerifyOtpUseCase.kt
│   │       └── commonTest
│   │           └── kotlin
│   │               └── com
│   │                   └── smach
│   │                       └── zapmancer
│   │                           └── domain
│   │                               └── usecase
│   │                                   └── LoginUseCaseTest.kt
│   └── presentation
│       ├── build.gradle.kts
│       └── src
│           ├── commonMain
│           │   └── kotlin
│           │       └── com
│           │           └── smach
│           │               └── zapmancer
│           │                   └── features
│           │                       ├── alerts
│           │                       │   ├── di
│           │                       │   │   └── AlertsModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── NotificationScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── NotificationUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── NotificationViewModel.kt
│           │                       ├── auth
│           │                       │   ├── di
│           │                       │   │   └── AuthModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── ForgotPasswordScreen.kt
│           │                       │   │   ├── LoginScreen.kt
│           │                       │   │   ├── OnboardingScreen.kt
│           │                       │   │   ├── SignupScreen.kt
│           │                       │   │   └── VerificationScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── ForgotPasswordUiState.kt
│           │                       │   │   ├── LoginUiState.kt
│           │                       │   │   ├── SignupUiState.kt
│           │                       │   │   └── VerificationUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── ForgotPasswordViewModel.kt
│           │                       │       ├── LoginViewModel.kt
│           │                       │       ├── SignupViewModel.kt
│           │                       │       └── VerificationViewModel.kt
│           │                       ├── common
│           │                       │   ├── adaptive
│           │                       │   │   ├── AdaptiveScaffold.kt
│           │                       │   │   ├── ResponsiveContainer.kt
│           │                       │   │   ├── TwoPane.kt
│           │                       │   │   └── WindowSize.kt
│           │                       │   ├── components
│           │                       │   │   ├── AppDrawerScaffold.kt
│           │                       │   │   ├── AppImage.kt
│           │                       │   │   ├── AppShimmer.kt
│           │                       │   │   ├── AuthComponents.kt
│           │                       │   │   ├── DrawerController.kt
│           │                       │   │   ├── EmptyState.kt
│           │                       │   │   ├── ErrorState.kt
│           │                       │   │   ├── UserAvatar.kt
│           │                       │   │   ├── VerticalDivider.kt
│           │                       │   │   └── ZapmancerTopBar.kt
│           │                       │   ├── di
│           │                       │   │   └── PresentationModule.kt
│           │                       │   └── theme
│           │                       │       ├── Color.kt
│           │                       │       ├── Shape.kt
│           │                       │       ├── Theme.kt
│           │                       │       └── Type.kt
│           │                       ├── home
│           │                       │   ├── di
│           │                       │   │   └── HomeModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── HomeScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── HomeUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── HomeViewModel.kt
│           │                       ├── messages
│           │                       │   ├── di
│           │                       │   │   └── MessagesModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── MessagesDetailScreen.kt
│           │                       │   │   └── MessagesListScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── MessagesDetailUiState.kt
│           │                       │   │   └── MessagesListUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── MessagesDetailViewModel.kt
│           │                       │       └── MessagesListViewModel.kt
│           │                       ├── profile
│           │                       │   ├── di
│           │                       │   │   └── ProfileModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── EditProfileScreen.kt
│           │                       │   │   └── ProfileScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── EditProfileUiState.kt
│           │                       │   │   └── ProfileuiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── EditProfileViewModel.kt
│           │                       │       └── ProfileViewModel.kt
│           │                       ├── projects
│           │                       │   ├── di
│           │                       │   │   └── ProjectsModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── PostProjectScreen.kt
│           │                       │   │   ├── ProjectDetailScreen.kt
│           │                       │   │   └── ProjectListScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── PostProjectUiState.kt
│           │                       │   │   ├── ProjectDetailUiState.kt
│           │                       │   │   └── ProjectListUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── PostProjectViewModel.kt
│           │                       │       ├── ProjectDetailViewModel.kt
│           │                       │       └── ProjectListViewModel.kt
│           │                       ├── proposal
│           │                       │   ├── di
│           │                       │   │   └── ProposalModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── ClientProposalsScreen.kt
│           │                       │   │   └── ProposalScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── ClientProposalsUiState.kt
│           │                       │   │   └── ProposalUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── ClientProposalsViewModel.kt
│           │                       │       └── ProposalViewModel.kt
│           │                       ├── search
│           │                       │   ├── di
│           │                       │   │   └── SearchModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── SearchScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── SearchUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── SearchViewModel.kt
│           │                       └── settings
│           │                           ├── di
│           │                           │   └── SettingsModule.kt
│           │                           ├── screen
│           │                           │   └── SettingsScreen.kt
│           │                           ├── state
│           │                           │   └── SettingsUiState.kt
│           │                           └── viewmodel
│           │                               └── SettingsViewModel.kt
│           └── commonTest
│               └── kotlin
│                   └── com
│                       └── smach
│                           └── zapmancer
│                               └── features
│                                   └── auth
│                                       └── viewmodel
│                                           └── LoginViewModelTest.kt
├── gorse-config.toml
├── gradle
│   ├── gradle-daemon-jvm.properties
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── implementation_plan.md
├── init-db.sh
├── iosApp
│   ├── Configuration
│   │   └── Config.xcconfig
│   ├── iosApp
│   │   ├── Assets.xcassets
│   │   │   ├── AccentColor.colorset
│   │   │   │   └── Contents.json
│   │   │   ├── AppIcon.appiconset
│   │   │   │   ├── app-icon-1024.png
│   │   │   │   └── Contents.json
│   │   │   └── Contents.json
│   │   ├── ContentView.swift
│   │   ├── Info.plist
│   │   ├── iOSApp.swift
│   │   └── 'Preview Content'
│   │       └── 'Preview Assets.xcassets'
│   │           └── Contents.json
│   └── iosApp.xcodeproj
│       ├── project.pbxproj
│       └── project.xcworkspace
│           └── contents.xcworkspacedata
├── kotlin-js-store
│   ├── wasm
│   │   └── yarn.lock
│   └── yarn.lock
├── local.properties
├── ProjectStructure.md
├── repomix-output.xml
├── server
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── kotlin
│           │   └── com
│           │       └── smach
│           │           └── zapmancer
│           │               ├── Application.kt
│           │               ├── auth
│           │               │   ├── data
│           │               │   │   └── AuthRepository.kt
│           │               │   ├── di
│           │               │   │   └── AuthModule.kt
│           │               │   ├── domain
│           │               │   │   └── AuthService.kt
│           │               │   └── routing
│           │               │       └── AuthRouting.kt
│           │               ├── home
│           │               │   ├── data
│           │               │   │   └── HomeRepository.kt
│           │               │   ├── di
│           │               │   │   └── HomeModule.kt
│           │               │   ├── domain
│           │               │   │   └── HomeService.kt
│           │               │   └── routing
│           │               │       └── HomeRouting.kt
│           │               ├── messages
│           │               │   ├── data
│           │               │   │   └── MessagesRepository.kt
│           │               │   ├── di
│           │               │   │   └── MessagesModule.kt
│           │               │   ├── domain
│           │               │   │   └── MessagesService.kt
│           │               │   └── routing
│           │               │       └── MessagesRouting.kt
│           │               ├── notifications
│           │               │   ├── data
│           │               │   │   └── NotificationsRepository.kt
│           │               │   ├── di
│           │               │   │   └── NotificationsModule.kt
│           │               │   ├── domain
│           │               │   │   └── NotificationsService.kt
│           │               │   └── routing
│           │               │       └── NotificationsRouting.kt
│           │               ├── projects
│           │               │   ├── data
│           │               │   │   └── ProjectsRepository.kt
│           │               │   ├── di
│           │               │   │   └── ProjectsModule.kt
│           │               │   ├── domain
│           │               │   │   └── ProjectsService.kt
│           │               │   └── routing
│           │               │       └── ProjectsRouting.kt
│           │               ├── proposal
│           │               │   ├── data
│           │               │   │   └── ProposalsRepository.kt
│           │               │   ├── di
│           │               │   │   └── ProposalsModule.kt
│           │               │   ├── domain
│           │               │   │   └── ProposalsService.kt
│           │               │   └── routing
│           │               │       └── ProposalsRouting.kt
│           │               ├── settings
│           │               │   ├── data
│           │               │   │   └── SettingsRepository.kt
│           │               │   ├── di
│           │               │   │   └── SettingsModule.kt
│           │               │   ├── domain
│           │               │   │   └── SettingsService.kt
│           │               │   └── routing
│           │               │       └── SettingsRouting.kt
│           │               └── users
│           │                   ├── data
│           │                   │   └── UsersRepository.kt
│           │                   ├── di
│           │                   │   └── UsersModule.kt
│           │                   ├── domain
│           │                   │   ├── UsersService.kt
│           │                   │   └── UsersServiceImpl.kt
│           │                   └── routing
│           │                       └── UsersRouting.kt
│           └── resources
│               ├── application.conf
│               ├── application.yaml
│               ├── db
│               │   └── migration
│               │       ├── V1__Initial_schema.sql
│               │       └── V2__Rich_mock_data.sql
│               └── logback.xml
├── settings.gradle.kts
├── shared
│   ├── build.gradle.kts
│   └── src
│       ├── androidMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       ├── commonMain
│       │   ├── composeResources
│       │   │   └── drawable
│       │   │       └── compose-multiplatform.xml
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   ├── App.kt
│       │                   ├── di
│       │                   │   └── Koin.kt
│       │                   ├── MainViewModel.kt
│       │                   └── nav
│       │                       ├── AuthGraph.kt
│       │                       ├── BottomNavigationBar.kt
│       │                       ├── MainGraph.kt
│       │                       └── Navigation.kt
│       ├── commonTest
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       └── iosMain
│           └── kotlin
│               └── com
│                   └── smach
│                       └── zapmancer
│                           └── MainViewController.kt
├── viewmodel_implementation_plan.md
└── webApp
    ├── build.gradle.kts
    ├── src
    │   └── webMain
    │       ├── kotlin
    │       │   └── com
    │       │       └── smach
    │       │           └── zapmancer
    │       │               └── main.kt
    │       └── resources
    │           ├── index.html
    │           ├── sql-wasm.wasm
    │           └── styles.css
    └── webpack.config.d
        └── sqljs.js
.
├── androidApp
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── ic_launcher-playstore.png
│           ├── kotlin
│           │   └── com
│           │       └── smach
│           │           └── zapmancer
│           │               ├── MainActivity.kt
│           │               └── ZapmancerApp.kt
│           └── res
│               ├── drawable
│               │   └── ic_launcher_background.xml
│               ├── drawable-v24
│               │   └── ic_launcher_foreground.xml
│               ├── mipmap-anydpi-v26
│               │   ├── ic_launcher.xml
│               │   └── ic_launcher_round.xml
│               ├── mipmap-hdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-mdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xxhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               ├── mipmap-xxxhdpi
│               │   ├── ic_launcher.webp
│               │   ├── ic_launcher_background.webp
│               │   ├── ic_launcher_foreground.webp
│               │   └── ic_launcher_round.webp
│               └── values
│                   └── strings.xml
├── build-logic
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src
│       └── main
│           └── kotlin
│               ├── database-convention.gradle.kts
│               ├── feature-generator.gradle.kts
│               ├── ktor-server-convention.gradle.kts
│               └── template-utils.gradle.kts
├── build.gradle.kts
├── CLAUDE.md
├── cloudbuild.yaml
├── core
│   ├── build.gradle.kts
│   └── src
│       ├── androidMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       ├── common
│       │                       │   └── utils
│       │                       │       └── DataStoreBuilder.android.kt
│       │                       └── network
│       ├── commonMain
│       │   ├── kotlin
│       │   │   └── com
│       │   │       └── smach
│       │   │           └── zapmancer
│       │   │               └── core
│       │   │                   ├── common
│       │   │                   │   ├── base
│       │   │                   │   │   └── BaseViewModel.kt
│       │   │                   │   ├── di
│       │   │                   │   │   └── CoreModule.kt
│       │   │                   │   ├── dto
│       │   │                   │   │   ├── AuthDtos.kt
│       │   │                   │   │   ├── CommonResponse.kt
│       │   │                   │   │   ├── HomeDtos.kt
│       │   │                   │   │   ├── MessageDtos.kt
│       │   │                   │   │   ├── NotificationDtos.kt
│       │   │                   │   │   ├── ProjectDtos.kt
│       │   │                   │   │   ├── ProposalDtos.kt
│       │   │                   │   │   ├── SettingsDtos.kt
│       │   │                   │   │   └── UserDtos.kt
│       │   │                   │   └── utils
│       │   │                   │       ├── DataStoreBuilder.kt
│       │   │                   │       ├── DataStoreStorage.kt
│       │   │                   │       ├── ErrorMapper.kt
│       │   │                   │       ├── Paginator.kt
│       │   │                   │       ├── Result.kt
│       │   │                   │       └── ResultExt.kt
│       │   │                   ├── monitoring
│       │   │                   │   ├── AnalyticsService.kt
│       │   │                   │   └── NapierAnalyticsService.kt
│       │   │                   └── network
│       │   │                       ├── ktor
│       │   │                       │   ├── NetworkConstants.kt
│       │   │                       │   ├── provideHttpClient.kt
│       │   │                       │   ├── Response.kt
│       │   │                       │   └── SafeApiCall.kt
│       │   │                       ├── model
│       │   │                       │   └── RefreshToken.kt
│       │   │                       └── session
│       │   │                           └── SessionManager.kt
│       │   └── sqldelight
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       └── database
│       │                           └── AppDatabase.sq
│       ├── commonTest
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       ├── common
│       │                       │   └── utils
│       │                       │       └── DefaultPaginatorTest.kt
│       │                       └── network
│       │                           └── ktor
│       │                               └── SafeApiCallTest.kt
│       ├── iosMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   └── core
│       │                       └── common
│       │                           └── utils
│       │                               └── DataStoreBuilder.ios.kt
│       ├── jvmMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   ├── common
│       │                   │   ├── Result.kt
│       │                   │   └── Sanitizer.kt
│       │                   ├── core
│       │                   │   └── common
│       │                   │       └── utils
│       │                   │           └── DataStoreBuilder.jvm.kt
│       │                   ├── database
│       │                   │   ├── DatabaseFactory.kt
│       │                   │   └── Tables.kt
│       │                   ├── framework
│       │                   │   ├── di
│       │                   │   │   └── storageModule.kt
│       │                   │   ├── FrameworkPlugins.kt
│       │                   │   └── storage
│       │                   │       ├── GcsStorageService.kt
│       │                   │       ├── S3StorageService.kt
│       │                   │       └── StorageService.kt
│       │                   ├── recommendations
│       │                   │   ├── GorseClient.kt
│       │                   │   └── GorseModule.kt
│       │                   └── security
│       │                       └── Security.kt
│       └── webMain
│           └── kotlin
│               └── com
│                   └── smach
│                       └── zapmancer
│                           └── core
│                               ├── common
│                               │   └── utils
│                               │       └── DataStoreBuilder.web.kt
│                               └── network
├── detekt
│   └── detekt.yml
├── docker-compose.yml
├── Dockerfile
├── domain_implementation_plan.md
├── feature
│   ├── data
│   │   ├── build.gradle.kts
│   │   └── src
│   │       └── commonMain
│   │           └── kotlin
│   │               └── com
│   │                   └── smach
│   │                       └── zapmancer
│   │                           └── data
│   │                               ├── di
│   │                               │   └── DataModule.kt
│   │                               ├── mapper
│   │                               ├── model
│   │                               └── repository
│   │                                   ├── AuthRepositoryImpl.kt
│   │                                   ├── HomeRepositoryImpl.kt
│   │                                   ├── MessageRepositoryImpl.kt
│   │                                   ├── NotificationRepositoryImpl.kt
│   │                                   ├── ProfileRepositoryImpl.kt
│   │                                   ├── ProjectRepositoryImpl.kt
│   │                                   ├── ProposalRepositoryImpl.kt
│   │                                   └── SettingsRepositoryImpl.kt
│   ├── domain
│   │   ├── build.gradle.kts
│   │   └── src
│   │       ├── commonMain
│   │       │   └── kotlin
│   │       │       └── com
│   │       │           └── smach
│   │       │               └── zapmancer
│   │       │                   └── domain
│   │       │                       ├── model
│   │       │                       │   ├── Conversation.kt
│   │       │                       │   ├── HomeDashboard.kt
│   │       │                       │   ├── Message.kt
│   │       │                       │   ├── Notification.kt
│   │       │                       │   ├── Project.kt
│   │       │                       │   ├── ProjectDetail.kt
│   │       │                       │   ├── Proposal.kt
│   │       │                       │   ├── SettingsData.kt
│   │       │                       │   ├── User.kt
│   │       │                       │   └── UserProfile.kt
│   │       │                       ├── repository
│   │       │                       │   ├── AuthRepository.kt
│   │       │                       │   ├── HomeRepository.kt
│   │       │                       │   ├── MessageRepository.kt
│   │       │                       │   ├── NotificationRepository.kt
│   │       │                       │   ├── ProfileRepository.kt
│   │       │                       │   ├── ProjectRepository.kt
│   │       │                       │   ├── ProposalRepository.kt
│   │       │                       │   └── SettingsRepository.kt
│   │       │                       └── usecase
│   │       │                           ├── ApplyProjectUseCase.kt
│   │       │                           ├── ExecuteNotificationActionUseCase.kt
│   │       │                           ├── ExportActivityCsvUseCase.kt
│   │       │                           ├── ForgotPasswordUseCase.kt
│   │       │                           ├── GetConversationsUseCase.kt
│   │       │                           ├── GetHomeDashboardUseCase.kt
│   │       │                           ├── GetMessagesUseCase.kt
│   │       │                           ├── GetNotificationsUseCase.kt
│   │       │                           ├── GetProjectDetailUseCase.kt
│   │       │                           ├── GetProjectProposalsUseCase.kt
│   │       │                           ├── GetProjectsUseCase.kt
│   │       │                           ├── GetSettingsUseCase.kt
│   │       │                           ├── GetUserProfileUseCase.kt
│   │       │                           ├── HireUserUseCase.kt
│   │       │                           ├── LoginUseCase.kt
│   │       │                           ├── LogoutUseCase.kt
│   │       │                           ├── MarkConversationAsReadUseCase.kt
│   │       │                           ├── PostProjectUseCase.kt
│   │       │                           ├── SaveProjectUseCase.kt
│   │       │                           ├── SendMessageUseCase.kt
│   │       │                           ├── SendNotificationQuickReplyUseCase.kt
│   │       │                           ├── SignUpUseCase.kt
│   │       │                           ├── SubmitProposalUseCase.kt
│   │       │                           ├── UpdateProfileUseCase.kt
│   │       │                           ├── UpdateSettingsUseCase.kt
│   │       │                           └── VerifyOtpUseCase.kt
│   │       └── commonTest
│   │           └── kotlin
│   │               └── com
│   │                   └── smach
│   │                       └── zapmancer
│   │                           └── domain
│   │                               └── usecase
│   │                                   └── LoginUseCaseTest.kt
│   └── presentation
│       ├── build.gradle.kts
│       └── src
│           ├── commonMain
│           │   └── kotlin
│           │       └── com
│           │           └── smach
│           │               └── zapmancer
│           │                   └── features
│           │                       ├── alerts
│           │                       │   ├── di
│           │                       │   │   └── AlertsModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── NotificationScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── NotificationUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── NotificationViewModel.kt
│           │                       ├── auth
│           │                       │   ├── di
│           │                       │   │   └── AuthModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── ForgotPasswordScreen.kt
│           │                       │   │   ├── LoginScreen.kt
│           │                       │   │   ├── OnboardingScreen.kt
│           │                       │   │   ├── SignupScreen.kt
│           │                       │   │   └── VerificationScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── ForgotPasswordUiState.kt
│           │                       │   │   ├── LoginUiState.kt
│           │                       │   │   ├── SignupUiState.kt
│           │                       │   │   └── VerificationUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── ForgotPasswordViewModel.kt
│           │                       │       ├── LoginViewModel.kt
│           │                       │       ├── SignupViewModel.kt
│           │                       │       └── VerificationViewModel.kt
│           │                       ├── common
│           │                       │   ├── adaptive
│           │                       │   │   ├── AdaptiveScaffold.kt
│           │                       │   │   ├── ResponsiveContainer.kt
│           │                       │   │   ├── TwoPane.kt
│           │                       │   │   └── WindowSize.kt
│           │                       │   ├── components
│           │                       │   │   ├── AppDrawerScaffold.kt
│           │                       │   │   ├── AppImage.kt
│           │                       │   │   ├── AppShimmer.kt
│           │                       │   │   ├── AuthComponents.kt
│           │                       │   │   ├── CategoryFilterChip.kt
│           │                       │   │   ├── DrawerController.kt
│           │                       │   │   ├── EmptyState.kt
│           │                       │   │   ├── ErrorState.kt
│           │                       │   │   ├── ProjectCategoryExtensions.kt
│           │                       │   │   ├── ProjectStatusBadge.kt
│           │                       │   │   ├── UserAvatar.kt
│           │                       │   │   ├── VerticalDivider.kt
│           │                       │   │   └── ZapmancerTopBar.kt
│           │                       │   ├── di
│           │                       │   │   └── PresentationModule.kt
│           │                       │   └── theme
│           │                       │       ├── Color.kt
│           │                       │       ├── Shape.kt
│           │                       │       ├── Theme.kt
│           │                       │       └── Type.kt
│           │                       ├── home
│           │                       │   ├── di
│           │                       │   │   └── HomeModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── HomeScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── HomeUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── HomeViewModel.kt
│           │                       ├── messages
│           │                       │   ├── di
│           │                       │   │   └── MessagesModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── MessagesDetailScreen.kt
│           │                       │   │   └── MessagesListScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── MessagesDetailUiState.kt
│           │                       │   │   └── MessagesListUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── MessagesDetailViewModel.kt
│           │                       │       └── MessagesListViewModel.kt
│           │                       ├── profile
│           │                       │   ├── di
│           │                       │   │   └── ProfileModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── EditProfileScreen.kt
│           │                       │   │   └── ProfileScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── EditProfileUiState.kt
│           │                       │   │   └── ProfileuiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── EditProfileViewModel.kt
│           │                       │       └── ProfileViewModel.kt
│           │                       ├── projects
│           │                       │   ├── di
│           │                       │   │   └── ProjectsModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── PostProjectScreen.kt
│           │                       │   │   ├── ProjectDetailScreen.kt
│           │                       │   │   └── ProjectListScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── PostProjectUiState.kt
│           │                       │   │   ├── ProjectDetailUiState.kt
│           │                       │   │   └── ProjectListUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── PostProjectViewModel.kt
│           │                       │       ├── ProjectDetailViewModel.kt
│           │                       │       └── ProjectListViewModel.kt
│           │                       ├── proposal
│           │                       │   ├── di
│           │                       │   │   └── ProposalModule.kt
│           │                       │   ├── screen
│           │                       │   │   ├── ClientProposalsScreen.kt
│           │                       │   │   └── ProposalScreen.kt
│           │                       │   ├── state
│           │                       │   │   ├── ClientProposalsUiState.kt
│           │                       │   │   └── ProposalUiState.kt
│           │                       │   └── viewmodel
│           │                       │       ├── ClientProposalsViewModel.kt
│           │                       │       └── ProposalViewModel.kt
│           │                       ├── search
│           │                       │   ├── di
│           │                       │   │   └── SearchModule.kt
│           │                       │   ├── screen
│           │                       │   │   └── SearchScreen.kt
│           │                       │   ├── state
│           │                       │   │   └── SearchUiState.kt
│           │                       │   └── viewmodel
│           │                       │       └── SearchViewModel.kt
│           │                       └── settings
│           │                           ├── di
│           │                           │   └── SettingsModule.kt
│           │                           ├── screen
│           │                           │   └── SettingsScreen.kt
│           │                           ├── state
│           │                           │   └── SettingsUiState.kt
│           │                           └── viewmodel
│           │                               └── SettingsViewModel.kt
│           └── commonTest
│               └── kotlin
│                   └── com
│                       └── smach
│                           └── zapmancer
│                               └── features
│                                   └── auth
│                                       └── viewmodel
│                                           └── LoginViewModelTest.kt
├── gorse-config.toml
├── gradle
│   ├── gradle-daemon-jvm.properties
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── implementation_plan.md
├── init-db.sh
├── iosApp
│   ├── Configuration
│   │   └── Config.xcconfig
│   ├── iosApp
│   │   ├── Assets.xcassets
│   │   │   ├── AccentColor.colorset
│   │   │   │   └── Contents.json
│   │   │   ├── AppIcon.appiconset
│   │   │   │   ├── app-icon-1024.png
│   │   │   │   └── Contents.json
│   │   │   └── Contents.json
│   │   ├── ContentView.swift
│   │   ├── Info.plist
│   │   ├── iOSApp.swift
│   │   └── 'Preview Content'
│   │       └── 'Preview Assets.xcassets'
│   │           └── Contents.json
│   └── iosApp.xcodeproj
│       ├── project.pbxproj
│       └── project.xcworkspace
│           └── contents.xcworkspacedata
├── kotlin-js-store
│   ├── wasm
│   │   └── yarn.lock
│   └── yarn.lock
├── local.properties
├── ProjectStructure.md
├── repomix-output.xml
├── server
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── kotlin
│           │   └── com
│           │       └── smach
│           │           └── zapmancer
│           │               ├── Application.kt
│           │               ├── auth
│           │               │   ├── data
│           │               │   │   └── AuthRepository.kt
│           │               │   ├── di
│           │               │   │   └── AuthModule.kt
│           │               │   ├── domain
│           │               │   │   └── AuthService.kt
│           │               │   └── routing
│           │               │       └── AuthRouting.kt
│           │               ├── home
│           │               │   ├── data
│           │               │   │   └── HomeRepository.kt
│           │               │   ├── di
│           │               │   │   └── HomeModule.kt
│           │               │   ├── domain
│           │               │   │   └── HomeService.kt
│           │               │   └── routing
│           │               │       └── HomeRouting.kt
│           │               ├── messages
│           │               │   ├── data
│           │               │   │   └── MessagesRepository.kt
│           │               │   ├── di
│           │               │   │   └── MessagesModule.kt
│           │               │   ├── domain
│           │               │   │   └── MessagesService.kt
│           │               │   └── routing
│           │               │       └── MessagesRouting.kt
│           │               ├── notifications
│           │               │   ├── data
│           │               │   │   └── NotificationsRepository.kt
│           │               │   ├── di
│           │               │   │   └── NotificationsModule.kt
│           │               │   ├── domain
│           │               │   │   └── NotificationsService.kt
│           │               │   └── routing
│           │               │       └── NotificationsRouting.kt
│           │               ├── projects
│           │               │   ├── data
│           │               │   │   └── ProjectsRepository.kt
│           │               │   ├── di
│           │               │   │   └── ProjectsModule.kt
│           │               │   ├── domain
│           │               │   │   └── ProjectsService.kt
│           │               │   └── routing
│           │               │       └── ProjectsRouting.kt
│           │               ├── proposal
│           │               │   ├── data
│           │               │   │   └── ProposalsRepository.kt
│           │               │   ├── di
│           │               │   │   └── ProposalsModule.kt
│           │               │   ├── domain
│           │               │   │   └── ProposalsService.kt
│           │               │   └── routing
│           │               │       └── ProposalsRouting.kt
│           │               ├── settings
│           │               │   ├── data
│           │               │   │   └── SettingsRepository.kt
│           │               │   ├── di
│           │               │   │   └── SettingsModule.kt
│           │               │   ├── domain
│           │               │   │   └── SettingsService.kt
│           │               │   └── routing
│           │               │       └── SettingsRouting.kt
│           │               └── users
│           │                   ├── data
│           │                   │   └── UsersRepository.kt
│           │                   ├── di
│           │                   │   └── UsersModule.kt
│           │                   ├── domain
│           │                   │   ├── UsersService.kt
│           │                   │   └── UsersServiceImpl.kt
│           │                   └── routing
│           │                       └── UsersRouting.kt
│           └── resources
│               ├── application.conf
│               ├── application.yaml
│               ├── db
│               │   └── migration
│               │       ├── V1__Initial_schema.sql
│               │       └── V2__Rich_mock_data.sql
│               └── logback.xml
├── settings.gradle.kts
├── shared
│   ├── build.gradle.kts
│   └── src
│       ├── androidMain
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       ├── commonMain
│       │   ├── composeResources
│       │   │   └── drawable
│       │   │       └── compose-multiplatform.xml
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       │                   ├── App.kt
│       │                   ├── di
│       │                   │   └── Koin.kt
│       │                   ├── MainViewModel.kt
│       │                   └── nav
│       │                       ├── AuthGraph.kt
│       │                       ├── BottomNavigationBar.kt
│       │                       ├── MainGraph.kt
│       │                       └── Navigation.kt
│       ├── commonTest
│       │   └── kotlin
│       │       └── com
│       │           └── smach
│       │               └── zapmancer
│       └── iosMain
│           └── kotlin
│               └── com
│                   └── smach
│                       └── zapmancer
│                           └── MainViewController.kt
├── viewmodel_implementation_plan.md
└── webApp
    ├── build.gradle.kts
    ├── src
    │   └── webMain
    │       ├── kotlin
    │       │   └── com
    │       │       └── smach
    │       │           └── zapmancer
    │       │               └── main.kt
    │       └── resources
    │           ├── index.html
    │           ├── sql-wasm.wasm
    │           └── styles.css
    └── webpack.config.d
        └── sqljs.js
