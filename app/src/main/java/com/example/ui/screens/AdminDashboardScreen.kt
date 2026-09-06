package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.DealStatus
import com.example.model.PostStatus
import com.example.model.Role
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val users by AppRepository.users.collectAsState()
  val deals by AppRepository.deals.collectAsState()
  val posts by AppRepository.marketplacePosts.collectAsState()
  val adminConfig by AppRepository.adminConfig.collectAsState()
  val auditLogs by AppRepository.auditLogs.collectAsState()

  var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Payment Numbers, 2: Audit Logs

  // Form states for Payment Numbers
  var bKashInput by remember { mutableStateOf(adminConfig.bKashNumber) }
  var nagadInput by remember { mutableStateOf(adminConfig.nagadNumber) }
  var saveSuccessMsg by remember { mutableStateOf("") }
  var saveErrorMsg by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = Strings.adminDashboard(currentLang),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = FireOrange
            )
            Text(
              text = "Executive Control Panel",
              fontSize = 10.sp,
              color = TextSecondary
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

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = GamingSurface,
        contentColor = FireOrange,
        divider = {},
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Overview", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Payment Settings", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("Audit Logs", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        )
      }

      when (selectedTab) {
        0 -> {
          // Overview Stats
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            item {
              // Owner Card
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GamingSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold)
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text(text = "PLATFORM OWNER & CHIEF ADMIN", color = CyberGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Shihab Talukder", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Text(text = "Unrestricted Executive Authority", color = TextSecondary, fontSize = 11.sp)
                  }
                  RoleBadge(role = Role.OWNER, isOwnerBadge = true)
                }
              }
            }

            item {
              Text(text = "CORE METRICS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(4.dp))
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricCard(title = "Total Users", value = "${users.size}", color = TextPrimary, modifier = Modifier.weight(1f))
                MetricCard(title = "Active Deals", value = "${deals.count { !it.status.isTerminal }}", color = FireOrange, modifier = Modifier.weight(1f))
                MetricCard(title = "Completed Deals", value = "${deals.count { it.status.isTerminal }}", color = GamingGreen, modifier = Modifier.weight(1f))
              }
            }

            item {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricCard(title = "Payment Pending", value = "${deals.count { it.status == DealStatus.PAYMENT_PENDING_VERIFICATION }}", color = CyberGold, modifier = Modifier.weight(1f))
                MetricCard(title = "Pending Posts", value = "${posts.count { it.status == PostStatus.PENDING_REVIEW }}", color = NeonCyan, modifier = Modifier.weight(1f))
                MetricCard(title = "Market Posts", value = "${posts.count { it.status == PostStatus.ACCEPTED }}", color = GamingPurple, modifier = Modifier.weight(1f))
              }
            }

            item {
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GamingSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(text = "SECURITY ENFORCEMENT SUMMARY", color = FireOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(text = "• Public registration escalation: Blocked (Customer/Seller only)", color = TextSecondary, fontSize = 11.sp)
                  Text(text = "• Password security: SHA-256 cryptographic hashing", color = TextSecondary, fontSize = 11.sp)
                  Text(text = "• Active Moderator cap: Max 3 enforced (${users.count { it.role == Role.MODERATOR && it.isActive }}/3)", color = TextSecondary, fontSize = 11.sp)
                  Text(text = "• Moderator restrictions: Cannot change numbers or create admins", color = TextSecondary, fontSize = 11.sp)
                  Text(text = "• Settlement settlement safety: Gated on Customer ALL DONE", color = TextSecondary, fontSize = 11.sp)
                }
              }
            }
          }
        }

        1 -> {
          // Payment Settings
          Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = GamingSurface),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Official Escrow Payment Numbers", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                  text = "Customers send deal payments to these numbers. Changing these updates the entire application immediately.",
                  color = TextSecondary,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(vertical = 6.dp)
                )

                if (saveSuccessMsg.isNotBlank()) {
                  Text(text = saveSuccessMsg, color = GamingGreen, fontSize = 12.sp)
                  Spacer(modifier = Modifier.height(4.dp))
                }
                if (saveErrorMsg.isNotBlank()) {
                  Text(text = saveErrorMsg, color = GamingRed, fontSize = 12.sp)
                  Spacer(modifier = Modifier.height(4.dp))
                }

                OutlinedTextField(
                  value = bKashInput,
                  onValueChange = { bKashInput = it },
                  label = { Text("bKash Merchant / Personal Number") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                  modifier = Modifier.fillMaxWidth().testTag("admin_bkash_input"),
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                  value = nagadInput,
                  onValueChange = { nagadInput = it },
                  label = { Text("Nagad Merchant / Personal Number") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                  modifier = Modifier.fillMaxWidth().testTag("admin_nagad_input"),
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                  onClick = {
                    saveSuccessMsg = ""
                    saveErrorMsg = ""
                    currentUser?.let { admin ->
                      val res = AppRepository.updatePaymentNumbers(admin, bKashInput, nagadInput)
                      res.onSuccess {
                        saveSuccessMsg = "Payment numbers updated successfully!"
                      }.onFailure {
                        saveErrorMsg = it.message ?: "Failed to update."
                      }
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                  modifier = Modifier.fillMaxWidth().height(44.dp).testTag("save_payment_numbers_btn")
                ) {
                  Text("Confirm & Save Payment Numbers", color = GamingDarkBg, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        2 -> {
          // Audit Logs
          LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(auditLogs) { log ->
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GamingSurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                      Text(text = log.action, color = FireOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                      RoleBadge(role = log.actorRole)
                    }
                    Text(text = log.timestamp, color = TextMuted, fontSize = 9.sp)
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = "Actor: ${log.actorName}", color = TextSecondary, fontSize = 11.sp)
                  Text(text = log.details, color = TextPrimary, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
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
fun MetricCard(title: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = GamingSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
  ) {
    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
      Text(text = title, color = TextMuted, fontSize = 10.sp)
    }
  }
}
