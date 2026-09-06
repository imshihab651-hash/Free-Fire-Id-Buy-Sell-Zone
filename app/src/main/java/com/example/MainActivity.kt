package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.AppRepository
import com.example.model.Role
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainAppContent()
      }
    }
  }
}

sealed class Screen(val route: String, val titleResKey: String, val icon: ImageVector) {
  object Home : Screen("home", "home", Icons.Default.Home)
  object Deals : Screen("deals", "deals", Icons.Default.Handshake)
  object Market : Screen("marketplace", "market", Icons.Default.Storefront)
  object Alerts : Screen("notifications", "alerts", Icons.Default.Notifications)
  object Profile : Screen("profile", "profile", Icons.Default.Person)
}

@Composable
fun MainAppContent() {
  val navController = rememberNavController()
  val currentUser by AppRepository.currentUser.collectAsState()
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val notifications by AppRepository.notifications.collectAsState()

  val unreadAlertsCount = notifications.count { !it.isRead && it.recipientId == currentUser?.id }

  val bottomNavItems = listOf(
    Screen.Home,
    Screen.Deals,
    Screen.Market,
    Screen.Alerts,
    Screen.Profile,
  )

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = currentRoute in listOf(
    Screen.Home.route,
    Screen.Deals.route,
    Screen.Market.route,
    Screen.Alerts.route,
    Screen.Profile.route,
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = GamingDarkBg,
    bottomBar = {
      if (showBottomBar) {
        NavigationBar(
          containerColor = GamingHeaderBg,
          contentColor = FireOrange,
          tonalElevation = 0.dp,
          modifier = Modifier
            .fillMaxWidth()
            .border(androidx.compose.foundation.BorderStroke(1.dp, GamingBorder))
            .testTag("bottom_nav_bar")
        ) {
          bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            val label = when (screen) {
              Screen.Home -> Strings.home(currentLang)
              Screen.Deals -> Strings.deals(currentLang)
              Screen.Market -> Strings.marketplace(currentLang)
              Screen.Alerts -> Strings.alerts(currentLang)
              Screen.Profile -> Strings.profile(currentLang)
            }

            NavigationBarItem(
              icon = {
                if (screen == Screen.Alerts && unreadAlertsCount > 0) {
                  BadgedBox(badge = {
                    Badge(containerColor = GamingRed) {
                      Text("$unreadAlertsCount", color = TextPrimary, fontSize = 9.sp)
                    }
                  }) {
                    Icon(screen.icon, contentDescription = label)
                  }
                } else {
                  Icon(screen.icon, contentDescription = label)
                }
              },
              label = {
                Text(
                  label.uppercase(),
                  fontSize = 9.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  letterSpacing = 0.5.sp
                )
              },
              selected = isSelected,
              onClick = {
                if (currentRoute != screen.route) {
                  navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                      saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FireOrange,
                selectedTextColor = FireOrange,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = FireOrange.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("bottom_nav_${screen.route}")
            )
          }
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = if (currentUser != null) Screen.Home.route else "login",
      modifier = Modifier.padding(innerPadding)
    ) {
      // 1. Login / Registration Screen
      composable("login") {
        LoginRegisterScreen(
          onLoginSuccess = {
            navController.navigate(Screen.Home.route) {
              popUpTo("login") { inclusive = true }
            }
          }
        )
      }

      // 2. Home Screen
      composable(Screen.Home.route) {
        HomeScreen(
          onNavigateToDealBoard = { dealId ->
            navController.navigate("deal_board/$dealId")
          },
          onNavigateToMarketplace = {
            navController.navigate(Screen.Market.route)
          },
          onNavigateToManagerChat = {
            navController.navigate("manager_chat")
          },
          onNavigateToSupport = {
            navController.navigate("support")
          },
          onNavigateToNotices = {
            navController.navigate("notices")
          },
          onNavigateToNotifications = {
            navController.navigate(Screen.Alerts.route)
          },
          onNavigateToMembers = {
            navController.navigate("members")
          },
          onNavigateToAdminDashboard = {
            navController.navigate("admin_dashboard")
          },
          onNavigateToModeratorPanel = {
            navController.navigate("moderator_panel")
          }
        )
      }

      // 3. Deals List Screen
      composable(Screen.Deals.route) {
        DealsListScreen(
          onNavigateToDealBoard = { dealId ->
            navController.navigate("deal_board/$dealId")
          },
          onOpenCustomDeal = {
            navController.navigate(Screen.Home.route)
          }
        )
      }

      // 4. Deal Board Screen (Specific Deal)
      composable(
        route = "deal_board/{dealId}",
        arguments = listOf(navArgument("dealId") { type = NavType.StringType })
      ) { backStackEntry ->
        val dealId = backStackEntry.arguments?.getString("dealId") ?: ""
        DealBoardScreen(
          dealId = dealId,
          onBack = { navController.popBackStack() }
        )
      }

      // 5. Community Marketplace Screen
      composable(Screen.Market.route) {
        MarketplaceScreen(
          onBack = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Home.route) { inclusive = false }
            }
          },
          onOpenDealForPost = { post ->
            currentUser?.let { user ->
              val res = AppRepository.createDeal(
                openerUser = user,
                customAmount = post.price,
                dealInfo = "Marketplace purchase: ${post.caption} (${post.freeFireId})"
              )
              res.onSuccess { newDeal ->
                navController.navigate("deal_board/${newDeal.id}")
              }
            }
          }
        )
      }

      // 6. Manager Chat Screen
      composable("manager_chat") {
        ManagerChatScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 7. Members Directory & Staff Management
      composable("members") {
        MembersScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 8. Admin Dashboard
      composable("admin_dashboard") {
        AdminDashboardScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 9. Moderator Panel
      composable("moderator_panel") {
        ModeratorPanelScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 10. Support Tickets Screen
      composable("support") {
        SupportScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 11. Notice Board Screen
      composable("notices") {
        NoticeBoardScreen(
          onBack = { navController.popBackStack() }
        )
      }

      // 12. Notifications / Alerts Screen
      composable(Screen.Alerts.route) {
        NotificationsScreen(
          onBack = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Home.route) { inclusive = false }
            }
          },
          onNavigateToDeal = { dealId ->
            navController.navigate("deal_board/$dealId")
          }
        )
      }

      // 13. Profile Screen
      composable(Screen.Profile.route) {
        ProfileScreen(
          onBack = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Home.route) { inclusive = false }
            }
          },
          onLogout = {
            navController.navigate("login") {
              popUpTo(0) { inclusive = true }
            }
          },
          onNavigateToAdminDashboard = {
            navController.navigate("admin_dashboard")
          },
          onNavigateToModeratorPanel = {
            navController.navigate("moderator_panel")
          }
        )
      }
    }
  }
}
