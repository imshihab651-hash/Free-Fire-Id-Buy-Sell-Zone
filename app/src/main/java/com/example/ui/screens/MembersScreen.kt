package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Role
import com.example.model.User
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val users by AppRepository.users.collectAsState()

  val totalMembers = users.size
  val customerCount = users.count { it.role == Role.CUSTOMER }
  val sellerCount = users.count { it.role == Role.SELLER }
  val activeModsCount = users.count { it.role == Role.MODERATOR && it.isActive }
  val activeUsersCount = users.count { it.isActive }
  val inactiveUsersCount = users.count { !it.isActive }

  val isAdmin = currentUser?.role == Role.ADMIN || currentUser?.role == Role.OWNER
  var alertMessage by remember { mutableStateOf("") }
  var isError by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = Strings.members(currentLang),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Directory & Staff Controls",
              fontSize = 11.sp,
              color = CyberGold
            )
          }
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
    ) {
      DemoModeBanner(
        currentUser = currentUser,
        onSwitchRole = { AppRepository.switchUserForDemo(it) }
      )

      // Summary Statistics Grid
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "MEMBERSHIP OVERVIEW",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            StatItem(label = "Total", value = "$totalMembers", color = TextPrimary)
            StatItem(label = "Customers", value = "$customerCount", color = GamingGreen)
            StatItem(label = "Sellers", value = "$sellerCount", color = NeonCyan)
            StatItem(label = "Active Mods", value = "$activeModsCount/3", color = GamingPurple)
            StatItem(label = "Active", value = "$activeUsersCount", color = CyberGold)
          }
        }
      }

      if (alertMessage.isNotBlank()) {
        Card(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
          colors = CardDefaults.cardColors(containerColor = if (isError) GamingRed.copy(alpha = 0.2f) else GamingGreen.copy(alpha = 0.2f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, if (isError) GamingRed else GamingGreen)
        ) {
          Text(text = alertMessage, color = if (isError) GamingRed else GamingGreen, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
        }
      }

      // Max 3 Active Moderator Notice (when Admin)
      if (isAdmin) {
        Surface(
          color = GamingPurple.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingPurple),
          modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = GamingPurple)
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "🛡️ মডারেটর পলিসি: প্ল্যাটফর্মে সর্বোচ্চ ৩ জন সক্রিয় মডারেটর অনুমোদিত। বর্তমানে $activeModsCount/3 জন সক্রিয় রয়েছেন।"
              else
                "🛡️ Moderator Policy: Maximum 3 active moderators allowed. Currently $activeModsCount/3 active.",
              color = TextPrimary,
              fontSize = 11.sp
            )
          }
        }
      }

      // Members List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(users) { user ->
          Card(
            modifier = Modifier.fillMaxWidth().testTag("member_card_${user.id}"),
            colors = CardDefaults.cardColors(containerColor = GamingSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (user.isOwner) CyberGold.copy(alpha = 0.5f) else GamingBorder
            )
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(38.dp)
                      .clip(CircleShape)
                      .background(GamingSurfaceElevated),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = if (user.isOwner) Icons.Default.WorkspacePremium else Icons.Default.Person,
                      contentDescription = null,
                      tint = if (user.isOwner) CyberGold else TextSecondary,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                  Column {
                    Text(
                      text = user.name,
                      color = TextPrimary,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "@${user.username} • Joined: ${user.joinedDate}",
                      color = TextMuted,
                      fontSize = 10.sp
                    )
                  }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  RoleBadge(role = user.role, isOwnerBadge = user.isOwner)
                }
              }

              // Admin Moderator Management Controls
              if (isAdmin && !user.isOwner) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = GamingDivider)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  if (user.role == Role.MODERATOR) {
                    // Toggle Active / Inactive
                    OutlinedButton(
                      onClick = {
                        val res = AppRepository.toggleModeratorActive(currentUser!!, user.id, !user.isActive)
                        res.onSuccess {
                          alertMessage = it
                          isError = false
                        }.onFailure {
                          alertMessage = it.message ?: "Failed"
                          isError = true
                        }
                      },
                      modifier = Modifier.weight(1f).height(32.dp),
                      contentPadding = PaddingValues(0.dp)
                    ) {
                      Text(
                        text = if (user.isActive) "Deactivate" else "Activate",
                        fontSize = 11.sp,
                        color = if (user.isActive) GamingRed else GamingGreen
                      )
                    }

                    // Remove Moderator
                    OutlinedButton(
                      onClick = {
                        val res = AppRepository.removeModerator(currentUser!!, user.id)
                        res.onSuccess {
                          alertMessage = it
                          isError = false
                        }.onFailure {
                          alertMessage = it.message ?: "Failed"
                          isError = true
                        }
                      },
                      modifier = Modifier.weight(1f).height(32.dp),
                      contentPadding = PaddingValues(0.dp)
                    ) {
                      Text("Demote", fontSize = 11.sp, color = GamingRed)
                    }
                  } else {
                    // Button to Appoint as Moderator
                    Button(
                      onClick = {
                        val res = AppRepository.appointModerator(currentUser!!, user.id)
                        res.onSuccess {
                          alertMessage = it
                          isError = false
                        }.onFailure {
                          alertMessage = it.message ?: "Failed"
                          isError = true
                        }
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = GamingPurple),
                      modifier = Modifier.fillMaxWidth().height(32.dp),
                      contentPadding = PaddingValues(0.dp)
                    ) {
                      Text("Promote to Moderator (Max 3)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun StatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
    Text(text = label, color = TextMuted, fontSize = 9.sp)
  }
}
