package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.DealSlot
import com.example.model.Role
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun HomeScreen(
  onNavigateToDealBoard: (String) -> Unit,
  onNavigateToMarketplace: () -> Unit,
  onNavigateToManagerChat: () -> Unit,
  onNavigateToSupport: () -> Unit,
  onNavigateToNotices: () -> Unit,
  onNavigateToNotifications: () -> Unit,
  onNavigateToMembers: () -> Unit,
  onNavigateToAdminDashboard: () -> Unit,
  onNavigateToModeratorPanel: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val dealSlots by AppRepository.dealSlots.collectAsState()
  val notices by AppRepository.notices.collectAsState()
  val communityInfo by AppRepository.communityInfo.collectAsState()
  val notifications by AppRepository.notifications.collectAsState()

  val unreadCount = notifications.count { !it.isRead && it.recipientId == currentUser?.id }

  // Custom Deal Dialog state
  var showCustomDealDialog by remember { mutableStateOf(false) }
  var customAmountInput by remember { mutableStateOf("") }
  var customDealInfo by remember { mutableStateOf("") }
  var customDealError by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(GamingDarkBg)
  ) {
    // 1. Top Header Bar
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(GamingHeaderBg)
          .border(androidx.compose.foundation.BorderStroke(1.dp, GamingBorder))
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(FireOrange),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "FF",
              fontWeight = FontWeight.Black,
              fontSize = 14.sp,
              color = Color.White
            )
          }
          Column {
            Text(
              text = "ID BUY & SELL ZONE",
              fontSize = 13.sp,
              fontWeight = FontWeight.Black,
              color = TextPrimary,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "OFFICIAL ESCROW ZONE",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 0.5.sp
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Language Toggle Pill
          Surface(
            onClick = { AppRepository.toggleLanguage() },
            shape = RoundedCornerShape(6.dp),
            color = Color(0x3327272A),
            border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorderProminent),
            modifier = Modifier.testTag("home_lang_toggle")
          ) {
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "EN | বাংলা" else "বাংলা | EN",
              fontSize = 10.sp,
              color = FireOrangeLight,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Notification Bell with Badge
          IconButton(
            onClick = onNavigateToNotifications,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0x3327272A))
              .border(androidx.compose.foundation.BorderStroke(1.dp, GamingBorder), CircleShape)
              .testTag("notifications_bell_btn")
          ) {
            BadgedBox(badge = {
              if (unreadCount > 0) {
                Badge(containerColor = GamingRed) {
                  Text("$unreadCount", color = TextPrimary, fontSize = 9.sp)
                }
              }
            }) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }

    // 2. Demo Mode Banner
    item {
      DemoModeBanner(
        currentUser = currentUser,
        onSwitchRole = { AppRepository.switchUserForDemo(it) }
      )
    }

    // 3. Official Notice Hero Card (Prominent Immersive Card)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorderProminent)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                listOf(Color(0xFF27272A), Color(0xFF18181B))
              )
            )
            .padding(14.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "OFFICIAL NOTICE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
              )
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = FireOrange.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange.copy(alpha = 0.3f))
              ) {
                Text(
                  text = "OWNER",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = FireOrange,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  letterSpacing = 0.5.sp
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Admin: Shihab Talukder",
              color = FireOrangeLight,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "\"স্বাগতম! আমরা দিচ্ছি শতভাগ নিরাপত্তার নিশ্চয়তা। কোনো লেনদেন এডমিন ছাড়া করবেন না।\""
              else
                "\"Welcome! We ensure 100% security assurance. Never conduct deals without official escrow Admin.\"",
              color = TextSecondary,
              fontSize = 11.sp,
              lineHeight = 16.sp
            )
          }
        }
      }
    }

    // 4. User Welcome Bar
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
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
              Icon(Icons.Default.Person, contentDescription = null, tint = FireOrange, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = currentUser?.name ?: "Guest User",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "@${currentUser?.username ?: "guest"}",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }
          currentUser?.let { user ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              RoleBadge(role = user.role, isOwnerBadge = user.isOwner)
            }
          }
        }
      }
    }

    // 4. Quick Actions Carousel (Marketplace, Custom Deal, Manager Chat, Support, Notice)
    item {
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "প্রধান ফিচারসমূহ" else "Main Features",
          color = TextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Custom Amount Deal Quick Card
          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "কাস্টম ডিল" else "Custom Deal",
            subtitle = if (currentLang == AppLanguage.BANGLA) "যেকোনো পরিমাণ" else "Any Amount",
            icon = Icons.Default.AddCard,
            color = FireOrange,
            onClick = { showCustomDealDialog = true },
            modifier = Modifier.weight(1f).testTag("open_custom_deal_card")
          )

          // Community Marketplace Card
          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "মার্কেটপ্লেস" else "Marketplace",
            subtitle = if (currentLang == AppLanguage.BANGLA) "FF আইডি পোস্ট" else "FF ID Posts",
            icon = Icons.Default.Storefront,
            color = NeonCyan,
            onClick = onNavigateToMarketplace,
            modifier = Modifier.weight(1f).testTag("open_marketplace_card")
          )

          // Manager Chat Card
          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "ম্যানেজার চ্যাট" else "Manager Chat",
            subtitle = if (currentLang == AppLanguage.BANGLA) "🤖 এআই হেল্প" else "🤖 AI Help",
            icon = Icons.Default.SmartToy,
            color = CyberGold,
            onClick = onNavigateToManagerChat,
            modifier = Modifier.weight(1f).testTag("open_manager_chat_card")
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "নোটিশ বোর্ড" else "Notice Board",
            subtitle = if (currentLang == AppLanguage.BANGLA) "জরুরি নিয়ম" else "Alerts & Rules",
            icon = Icons.Default.Campaign,
            color = FireOrangeLight,
            onClick = onNavigateToNotices,
            modifier = Modifier.weight(1f).testTag("open_notices_card")
          )

          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "মেম্বারস" else "Members",
            subtitle = if (currentLang == AppLanguage.BANGLA) "কমিউনিটি তালিকা" else "User Directory",
            icon = Icons.Default.Group,
            color = GamingGreen,
            onClick = onNavigateToMembers,
            modifier = Modifier.weight(1f).testTag("open_members_card")
          )

          QuickActionCard(
            title = if (currentLang == AppLanguage.BANGLA) "সাপোর্ট" else "Support",
            subtitle = if (currentLang == AppLanguage.BANGLA) "সহায়তা নিন" else "Get Help",
            icon = Icons.Default.SupportAgent,
            color = GamingPurple,
            onClick = onNavigateToSupport,
            modifier = Modifier.weight(1f).testTag("open_support_card")
          )
        }
      }
    }

    // Role-specific quick panels (Admin Dashboard / Moderator Panel)
    item {
      if (currentUser?.role == Role.ADMIN || currentUser?.role == Role.OWNER) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onNavigateToAdminDashboard() },
          colors = CardDefaults.cardColors(containerColor = FireOrange.copy(alpha = 0.15f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = FireOrange)
              Column {
                Text(
                  text = Strings.adminDashboard(currentLang),
                  fontWeight = FontWeight.Bold,
                  color = FireOrange,
                  fontSize = 14.sp
                )
                Text(
                  text = "Payment, Customization, Moderator Mgmt, Audit Logs",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
              }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = FireOrange)
          }
        }
      } else if (currentUser?.role == Role.MODERATOR) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onNavigateToModeratorPanel() },
          colors = CardDefaults.cardColors(containerColor = GamingPurple.copy(alpha = 0.15f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingPurple),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = GamingPurple)
              Column {
                Text(
                  text = Strings.moderatorPanel(currentLang),
                  fontWeight = FontWeight.Bold,
                  color = GamingPurple,
                  fontSize = 14.sp
                )
                Text(
                  text = "Review Marketplace Posts & Comments",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
              }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GamingPurple)
          }
        }
      }
    }

    // 5. 10 DEAL SLOTS HEADER
    item {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ACTIVE DEAL SLOTS",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          )
          Text(
            text = "10 Official Slots",
            color = FireOrange,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = if (currentLang == AppLanguage.BANGLA)
            "যেকোনো স্লট নির্বাচন করে তাৎক্ষণিক সুরক্ষিত এসক্রো ডিল খুলুন:"
          else
            "Select an amount range to open an instant escrow deal box:",
          color = TextSecondary,
          fontSize = 11.sp,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }

    // 6. 10 DEAL SLOTS LIST
    items(dealSlots) { slot ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FireOrange.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "S${slot.id}",
                color = FireOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
              )
            }
            Column {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) slot.nameBn else slot.nameEn,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Row(
                modifier = Modifier.padding(top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${Strings.adminFee(currentLang)}: ${slot.adminFee} ${Strings.taka(currentLang)}",
                  color = TextMuted,
                  fontSize = 10.sp
                )
                Text(
                  text = "• ${slot.status}",
                  color = GamingGreen,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          Button(
            onClick = {
              currentUser?.let { user ->
                val res = AppRepository.createDeal(openerUser = user, slot = slot)
                res.onSuccess { newDeal ->
                  onNavigateToDealBoard(newDeal.id)
                }
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier
              .height(36.dp)
              .testTag("open_slot_${slot.id}_btn")
          ) {
            Text(
              text = Strings.openDeal(currentLang).uppercase(),
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
        }
      }
    }

    // 7. Community Section
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Groups, contentDescription = null, tint = NeonCyan)
            Text(
              text = communityInfo.name,
              color = NeonCyan,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (currentLang == AppLanguage.BANGLA) communityInfo.descriptionBn else communityInfo.descriptionEn,
            color = TextSecondary,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Official Group: ${communityInfo.link}",
            color = CyberGold,
            fontSize = 11.sp
          )
        }
      }
    }

    // 8. Notices Highlight
    item {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "সাম্প্রতিক নোটিশ" else "Important Notices",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 6.dp)
        )
        notices.take(2).forEach { notice ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = GamingSurface),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) notice.titleBn else notice.titleEn,
                color = FireOrange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (currentLang == AppLanguage.BANGLA) notice.bodyBn else notice.bodyEn,
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(40.dp))
    }
  }

  // Custom Amount Deal Dialog
  if (showCustomDealDialog) {
    AlertDialog(
      onDismissRequest = {
        showCustomDealDialog = false
        customDealError = ""
      },
      containerColor = GamingSurface,
      title = {
        Text(
          text = Strings.customDeal(currentLang),
          color = TextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column {
          Text(
            text = if (currentLang == AppLanguage.BANGLA)
              "ক্রেতা বা বিক্রেতা উভয়ই সরাসরি কাস্টম ডিল তৈরি করতে পারেন। স্বয়ংক্রিয় ইউনিক DEAL # তৈরি হবে।"
            else
              "Both Customer and Seller can open custom deals directly. Unique Deal Number is created instantly.",
            color = TextSecondary,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          if (customDealError.isNotBlank()) {
            Text(text = customDealError, color = GamingRed, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
          }

          OutlinedTextField(
            value = customAmountInput,
            onValueChange = { customAmountInput = it },
            label = { Text(if (currentLang == AppLanguage.BANGLA) "টাকার পরিমাণ (BDT)" else "Amount in BDT") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("custom_amount_input"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customDealInfo,
            onValueChange = { customDealInfo = it },
            label = { Text(if (currentLang == AppLanguage.BANGLA) "ডিল বিবরণ (ঐচ্ছিক)" else "Deal Description (Optional)") },
            modifier = Modifier.fillMaxWidth().testTag("custom_deal_info_input"),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amount = customAmountInput.toIntOrNull()
            if (amount == null || amount < 100) {
              customDealError = "Please enter valid amount (min 100 BDT)."
            } else {
              currentUser?.let { user ->
                val res = AppRepository.createDeal(
                  openerUser = user,
                  customAmount = amount,
                  dealInfo = customDealInfo
                )
                res.onSuccess { newDeal ->
                  showCustomDealDialog = false
                  customAmountInput = ""
                  customDealInfo = ""
                  onNavigateToDealBoard(newDeal.id)
                }.onFailure {
                  customDealError = it.message ?: "Failed"
                }
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
          modifier = Modifier.testTag("create_custom_deal_btn")
        ) {
          Text(if (currentLang == AppLanguage.BANGLA) "ডিল শুরু করুন" else "Start Deal")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCustomDealDialog = false }) {
          Text(if (currentLang == AppLanguage.BANGLA) "বাতিল" else "Cancel", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
fun QuickActionCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = GamingSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 6.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title.uppercase(),
        color = TextPrimary,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        letterSpacing = 0.3.sp
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 9.sp,
        maxLines = 1
      )
    }
  }
}
