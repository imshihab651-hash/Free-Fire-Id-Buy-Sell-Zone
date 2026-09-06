package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Role
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  onBack: () -> Unit,
  onLogout: () -> Unit,
  onNavigateToAdminDashboard: () -> Unit,
  onNavigateToModeratorPanel: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val adminConfig by AppRepository.adminConfig.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = Strings.profile(currentLang),
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = GamingHeaderBg)
      )
    },
    containerColor = GamingDarkBg
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 14.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      DemoModeBanner(
        currentUser = currentUser,
        onSwitchRole = { AppRepository.switchUserForDemo(it) }
      )

      // Profile Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(68.dp)
              .clip(CircleShape)
              .background(Brush.linearGradient(listOf(FireOrange, CyberGold))),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (currentUser?.isOwner == true) Icons.Default.WorkspacePremium else Icons.Default.Person,
              contentDescription = null,
              tint = GamingDarkBg,
              modifier = Modifier.size(38.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = currentUser?.name ?: "User",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
          )

          Text(
            text = "@${currentUser?.username ?: "user"}",
            color = TextSecondary,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          currentUser?.let { user ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              RoleBadge(role = user.role, isOwnerBadge = user.isOwner)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          HorizontalDivider(color = GamingDivider)
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Phone Number", color = TextMuted, fontSize = 10.sp)
              Text(text = currentUser?.phone ?: "N/A", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "Member Since", color = TextMuted, fontSize = 10.sp)
              Text(text = currentUser?.joinedDate ?: "2024", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Security Policy Notice
      Surface(
        color = GamingSurfaceElevated,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Security, contentDescription = null, tint = CyberGold, modifier = Modifier.size(16.dp))
            Text(
              text = "ROLE SECURITY ENFORCEMENT",
              color = CyberGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "User roles and permissions are strictly enforced on the server-side. The Android client never trusts self-reported credentials.",
            color = TextMuted,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      // Privileged Panels quick shortcuts
      if (currentUser?.role == Role.ADMIN || currentUser?.role == Role.OWNER) {
        Button(
          onClick = onNavigateToAdminDashboard,
          colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(44.dp).testTag("profile_admin_dash_btn")
        ) {
          Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = Strings.adminDashboard(currentLang), fontWeight = FontWeight.Bold)
        }
      } else if (currentUser?.role == Role.MODERATOR) {
        Button(
          onClick = onNavigateToModeratorPanel,
          colors = ButtonDefaults.buttonColors(containerColor = GamingPurple),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(44.dp).testTag("profile_mod_panel_btn")
        ) {
          Icon(Icons.Default.Shield, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = Strings.moderatorPanel(currentLang), fontWeight = FontWeight.Bold)
        }
      }

      // App Preferences Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(text = "App Preferences", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(10.dp))

          // Language Switch
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Language / ভাষা", color = TextSecondary, fontSize = 12.sp)
            OutlinedButton(
              onClick = { AppRepository.toggleLanguage() },
              modifier = Modifier.height(34.dp).testTag("profile_lang_btn")
            ) {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "বাংলা (Switch to En)" else "English (বাংলা করুন)",
                fontSize = 11.sp,
                color = CyberGold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider(color = GamingDivider)
          Spacer(modifier = Modifier.height(10.dp))

          // Notification Sound
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Sound Alerts", color = TextSecondary, fontSize = 12.sp)
            Switch(
              checked = adminConfig.notificationSoundEnabled,
              onCheckedChange = { AppRepository.toggleNotificationSound(it) },
              colors = SwitchDefaults.colors(
                checkedThumbColor = GamingDarkBg,
                checkedTrackColor = FireOrange
              )
            )
          }
        }
      }

      // Logout Button
      OutlinedButton(
        onClick = {
          AppRepository.logout()
          onLogout()
        },
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingRed),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().height(44.dp).testTag("logout_btn")
      ) {
        Icon(Icons.Default.Logout, contentDescription = null, tint = GamingRed, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = Strings.logout(currentLang), color = GamingRed, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
