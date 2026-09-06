package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.NotificationType
import com.example.ui.components.DemoModeBanner
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
  onBack: () -> Unit,
  onNavigateToDeal: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val notifications by AppRepository.notifications.collectAsState()
  val adminConfig by AppRepository.adminConfig.collectAsState()

  val myNotifications = notifications.filter {
    it.recipientId == currentUser?.id || it.recipientId == "all"
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = Strings.alerts(currentLang),
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
          }
        },
        actions = {
          TextButton(
            onClick = {
              currentUser?.let { AppRepository.markAllNotificationsAsRead(it.id) }
            },
            modifier = Modifier.testTag("mark_all_read_btn")
          ) {
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "সব পড়া হয়েছে" else "Mark All Read",
              color = CyberGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
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

      // Sound Setting Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
              imageVector = if (adminConfig.notificationSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
              contentDescription = null,
              tint = CyberGold,
              modifier = Modifier.size(20.dp)
            )
            Column {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "নোটিফিকেশন সাউন্ড" else "Notification Sound",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (adminConfig.notificationSoundEnabled) "Sound alerts active" else "Muted",
                color = TextMuted,
                fontSize = 10.sp
              )
            }
          }
          Switch(
            checked = adminConfig.notificationSoundEnabled,
            onCheckedChange = { AppRepository.toggleNotificationSound(it) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = GamingDarkBg,
              checkedTrackColor = FireOrange
            ),
            modifier = Modifier.testTag("notification_sound_switch")
          )
        }
      }

      if (myNotifications.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "কোনো নতুন নোটিফিকেশন নেই" else "No notifications yet",
              color = TextSecondary,
              fontSize = 13.sp
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(myNotifications) { notif ->
            val (icon, color) = when (notif.type) {
              NotificationType.PAYMENT_SUCCESSFUL, NotificationType.CUSTOMER_ALL_DONE, NotificationType.SETTLEMENT_READY ->
                Icons.Default.CheckCircle to GamingGreen
              NotificationType.TRANSACTION_ERROR ->
                Icons.Default.Error to GamingRed
              NotificationType.NEW_MESSAGE ->
                Icons.Default.Chat to NeonCyan
              NotificationType.ID_SUBMITTED, NotificationType.ID_RELEASED ->
                Icons.Default.VpnKey to CyberGold
              else ->
                Icons.Default.Notifications to FireOrange
            }

            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  AppRepository.markNotificationAsRead(notif.id)
                  notif.dealId?.let { onNavigateToDeal(it) }
                }
                .testTag("notif_card_${notif.id}"),
              colors = CardDefaults.cardColors(
                containerColor = if (!notif.isRead) GamingSurfaceElevated else GamingSurface
              ),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (!notif.isRead) color.copy(alpha = 0.5f) else GamingBorder
              )
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = if (currentLang == AppLanguage.BANGLA) notif.titleBn else notif.titleEn,
                      color = TextPrimary,
                      fontSize = 13.sp,
                      fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(text = notif.timestamp, color = TextMuted, fontSize = 9.sp)
                  }
                  Text(
                    text = if (currentLang == AppLanguage.BANGLA) notif.messageBn else notif.messageEn,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                  if (notif.dealId != null) {
                    Text(
                      text = "Tap to open Deal Board ➔",
                      color = CyberGold,
                      fontSize = 10.sp,
                      modifier = Modifier.padding(top = 4.dp)
                    )
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
