package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Deal
import com.example.model.DealStatus
import com.example.model.Role
import com.example.ui.components.DealStatusBadge
import com.example.ui.components.DemoModeBanner
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsListScreen(
  onNavigateToDealBoard: (String) -> Unit,
  onOpenCustomDeal: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val deals by AppRepository.deals.collectAsState()

  var selectedFilter by remember { mutableStateOf(0) } // 0: All, 1: Active, 2: Completed

  // Filter deals based on user role and filter tab
  val filteredDeals = deals.filter { deal ->
    val userMatches = if (currentUser?.role?.isPrivileged == true) {
      true
    } else {
      deal.customerId == currentUser?.id || deal.sellerId == currentUser?.id
    }
    val statusMatches = when (selectedFilter) {
      1 -> !deal.status.isTerminal
      2 -> deal.status.isTerminal
      else -> true
    }
    userMatches && statusMatches
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = Strings.deals(currentLang),
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        },
        actions = {
          Button(
            onClick = onOpenCustomDeal,
            colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .height(36.dp)
              .padding(end = 8.dp)
              .testTag("deals_new_custom_deal_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (currentLang == AppLanguage.BANGLA) "নতুন ডিল" else "New Deal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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

      // Filter Tabs
      TabRow(
        selectedTabIndex = selectedFilter,
        containerColor = GamingSurface,
        contentColor = FireOrange,
        divider = {},
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        listOf(
          (if (currentLang == AppLanguage.BANGLA) "সবগুলো" else "All"),
          (if (currentLang == AppLanguage.BANGLA) "সক্রিয়" else "Active"),
          (if (currentLang == AppLanguage.BANGLA) "সম্পন্ন" else "Completed")
        ).forEachIndexed { index, title ->
          Tab(
            selected = selectedFilter == index,
            onClick = { selectedFilter = index },
            text = {
              Text(
                text = title,
                color = if (selectedFilter == index) FireOrange else TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            },
            modifier = Modifier.testTag("deal_filter_tab_$index")
          )
        }
      }

      if (filteredDeals.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.FolderOpen, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "কোনো ডিল পাওয়া যায়নি" else "No deals found",
              color = TextSecondary,
              fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = onOpenCustomDeal,
              colors = ButtonDefaults.buttonColors(containerColor = FireOrange)
            ) {
              Text(if (currentLang == AppLanguage.BANGLA) "ডিল শুরু করুন" else "Start a Deal")
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredDeals) { deal ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToDealBoard(deal.id) }
                .testTag("deal_card_${deal.id}"),
              colors = CardDefaults.cardColors(containerColor = GamingSurface),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = deal.dealNumber,
                    color = FireOrange,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                  )
                  DealStatusBadge(status = deal.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = deal.slotTitle,
                  color = TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(
                      text = "Buyer: ${deal.customerName} | Seller: ${deal.sellerName}",
                      color = TextSecondary,
                      fontSize = 11.sp
                    )
                    Text(
                      text = "Total: ${deal.totalPayment} BDT (${deal.paymentMethod})",
                      color = CyberGold,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                  Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextMuted
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
