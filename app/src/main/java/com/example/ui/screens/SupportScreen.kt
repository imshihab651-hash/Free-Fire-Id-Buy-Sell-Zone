package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.components.DemoModeBanner
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val tickets by AppRepository.supportTickets.collectAsState()

  val categories = listOf(
    "Login problem",
    "Account problem",
    "Payment problem",
    "Deal problem",
    "Verification problem",
    "Marketplace problem",
    "Other"
  )

  var selectedCategory by remember { mutableStateOf(categories[0]) }
  var issueDesc by remember { mutableStateOf("") }
  var successMsg by remember { mutableStateOf("") }
  var errorMsg by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = Strings.support(currentLang),
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
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        DemoModeBanner(
          currentUser = currentUser,
          onSwitchRole = { AppRepository.switchUserForDemo(it) }
        )
      }

      // Direct Escrow Support Card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = GamingSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingPurple.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.HeadsetMic, contentDescription = null, tint = GamingPurple)
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "২৪/৭ অফিসিয়াল এসক্রো সাপোর্ট" else "24/7 Official Escrow Support",
                color = GamingPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "যেকোনো লেনদেন বা ডিল সংক্রান্ত সমস্যায় সরাসরি সাপোর্ট টিকিট সাবমিট করুন। অ্যাডমিন দল অগ্রাধিকার ভিত্তিতে আপনার সমস্যার সমাধান করবে।"
              else
                "Submit a ticket for deal assistance. The administration prioritizes user security and resolution.",
              color = TextSecondary,
              fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Admin Contact: admin@ffzone.escrow", color = CyberGold, fontSize = 11.sp)
            Text(text = "Platform Head: Shihab Talukder", color = TextMuted, fontSize = 10.sp)
          }
        }
      }

      // Ticket Creation Form
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = GamingSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "নতুন টিকিট ওপেন করুন" else "Open New Support Ticket",
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (successMsg.isNotBlank()) {
              Surface(color = GamingGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                Text(text = successMsg, color = GamingGreen, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
            }
            if (errorMsg.isNotBlank()) {
              Surface(color = GamingRed.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                Text(text = errorMsg, color = GamingRed, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = "Select Category:", color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))

            // Category selector
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              categories.take(3).forEach { cat ->
                OutlinedButton(
                  onClick = { selectedCategory = cat },
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedCategory == cat) FireOrange.copy(alpha = 0.2f) else GamingSurface
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedCategory == cat) FireOrange else GamingBorder),
                  modifier = Modifier.weight(1f).height(32.dp),
                  contentPadding = PaddingValues(0.dp)
                ) {
                  Text(cat.take(10), fontSize = 10.sp, color = if (selectedCategory == cat) FireOrange else TextPrimary)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = issueDesc,
              onValueChange = { issueDesc = it },
              label = { Text(if (currentLang == AppLanguage.BANGLA) "সমস্যার বিস্তারিত বিবরণ" else "Explain your issue clearly") },
              modifier = Modifier.fillMaxWidth().height(100.dp).testTag("support_issue_input"),
              maxLines = 4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                successMsg = ""
                errorMsg = ""
                currentUser?.let { user ->
                  val res = AppRepository.submitSupportTicket(user, selectedCategory, issueDesc)
                  res.onSuccess {
                    successMsg = "Ticket submitted successfully! Ticket ID: #${it.id}"
                    issueDesc = ""
                  }.onFailure {
                    errorMsg = it.message ?: "Failed to submit ticket."
                  }
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
              modifier = Modifier.fillMaxWidth().height(42.dp).testTag("submit_support_ticket_btn")
            ) {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "টিকিট সাবমিট করুন" else "Submit Ticket",
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Recent Tickets Section
      item {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "আপনার সাবমিট করা টিকিটসমূহ" else "Your Submitted Tickets",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }

      items(tickets) { ticket ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = GamingSurface),
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Ticket #${ticket.id}", color = FireOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Surface(color = CyberGold.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                Text(text = ticket.status, color = CyberGold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Category: ${ticket.category}", color = TextMuted, fontSize = 10.sp)
            Text(text = ticket.description, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
