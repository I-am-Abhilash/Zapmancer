package com.smach.zapmancer.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Screen defines the available routes in the application.
 */
@Serializable
sealed class Screen(
    val title: String,
) : NavKey {
    @Serializable
    data object Login : Screen("Login")

    @Serializable
    data object Signup : Screen("Signup")

    @Serializable
    data object ForgotPassword : Screen("ForgotPassword")

    @Serializable
    data class Verification(
        val email: String,
    ) : Screen("Verification")

    @Serializable
    data object Home : Screen("Home")

    @Serializable
    data object Profile : Screen("Profile")

    @Serializable
    data object Explore : Screen("Explore")

    @Serializable
    data object Writing : Screen("Writing")

    @Serializable
    data object Activity : Screen("Alerts")

    @Serializable
    data class Detail(
        val id: Int,
    ) : Screen("Detail")
}

/**
 * List of routes that should be displayed in the bottom navigation bar.
 */

val bottomNavigationRoutes: Set<Screen> =
    setOf(
        Screen.Home,
        Screen.Explore,
        Screen.Writing,
        Screen.Activity,
        Screen.Profile,
    )

/**
 * Extension property to provide an icon for each screen.
 */
val Screen.icon: ImageVector
    get() =
        when (this) {
            Screen.Home -> Icons.Filled.Home
            Screen.Explore -> Icons.Filled.Email
            Screen.Writing -> Icons.Filled.Create
            Screen.Activity -> Icons.Filled.Notifications
            Screen.Profile -> Icons.Filled.Person
            else -> Icons.Filled.Info
        }

/**
 * Extension property to check if a screen is a top-level route (displayed in bottom nav).
 */
val Screen.isTopLevel: Boolean
    get() = this in bottomNavigationRoutes

/**
 * NavigationState manages the backstacks for different top-level routes.
 */
class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    var topLevelRoute: NavKey by topLevelRoute

    val stacksInUse: List<NavKey>
        get() =
            if (topLevelRoute == startRoute) {
                listOf(startRoute)
            } else {
                listOf(startRoute, topLevelRoute)
            }
}

/**
 * rememberNavigationState creates and remembers a NavigationState across recompositions.
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): NavigationState {
    val topLevelRoute =
        rememberSerializable(
            inputs = arrayOf(startRoute, topLevelRoutes),
            serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class)),
            configuration = navConfig,
        )
        {
            mutableStateOf(startRoute)
        }

    val backStacks: Map<NavKey, NavBackStack<NavKey>> =
        topLevelRoutes.associateWith { key ->
            rememberNavBackStack(navConfig, key)
        }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks,
        )
    }
}

/**
 * MainNavigator provides a high-level API for performing navigation actions.
 */
class MainNavigator(
    val state: NavigationState,
) {
    var openSearchKeyboard by mutableStateOf(false)
    val currentScreen: Screen
        get() =
            state.backStacks[state.topLevelRoute]
                ?.lastOrNull() as? Screen
                ?: state.topLevelRoute as Screen

    /**
     * Navigates to a new route.
     * If it's a top-level route, it switches the active stack.
     * Otherwise, it pushes the route onto the current stack.
     */
    fun navigate(route: Screen) {
        if (route.isTopLevel) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    /**
     * Navigates back.
     * If at the root of a non-start stack, it returns to the start stack.
     */
    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

val navConfig =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Screen.Login::class, Screen.Login.serializer())
                    subclass(Screen.Signup::class, Screen.Signup.serializer())
                    subclass(Screen.Verification::class, Screen.Verification.serializer())
                    subclass(Screen.ForgotPassword::class, Screen.ForgotPassword.serializer())
                    subclass(Screen.Home::class, Screen.Home.serializer())
                    subclass(Screen.Activity::class, Screen.Activity.serializer())
                    subclass(Screen.Explore::class, Screen.Explore.serializer())
                    subclass(Screen.Writing::class, Screen.Writing.serializer())
                    subclass(Screen.Profile::class, Screen.Profile.serializer())
                    subclass(Screen.Detail::class, Screen.Detail.serializer())
                }
            }
    }
