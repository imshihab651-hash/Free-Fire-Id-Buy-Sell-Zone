package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Role
import com.example.ui.components.DemoModeBanner
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

private data class ChatMsg(
  val id: String,
  val sender: String,
  val isAi: Boolean,
  val text: String,
  val time: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerChatScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val deals by AppRepository.deals.collectAsState()

  var messageText by remember { mutableStateOf("") }
  var chatHistory by remember {
    mutableStateOf(
      listOf(
        ChatMsg(
          id = "intro-1",
          sender = "Manager Bot 🤖",
          isAi = true,
          text = if (currentLang == AppLanguage.BANGLA)
            "স্বাগতম! আমি FREE FIRE ID BUY & SELL ZONE এর অফিসিয়াল ম্যানেজার অ্যাসিস্ট্যান্ট। ডিল স্লট, পেমেন্ট, আইডি ভেরিফিকেশন বা কাস্টম ডিল সংক্রান্ত যেকোনো তথ্য জানতে প্রশ্ন করুন।"
          else
            "Welcome! I am the official Manager Assistant for FREE FIRE ID BUY & SELL ZONE. Ask me anything about Deal Slots, Payments, Verification, or Custom Deals.",
          time = "Just now"
        )
      )
    )
  }

  fun handleSend(query: String) {
    if (query.isBlank()) return
    val user = currentUser ?: return
    val now = "Now"
    val userMsg = ChatMsg("u-${System.currentTimeMillis()}", user.name, false, query.trim(), now)
    val reply = AppRepository.queryManagerChat(user, query, currentLang)
    val aiMsg = ChatMsg("ai-${System.currentTimeMillis()}", "Manager Bot 🤖", true, reply, now)

    chatHistory = chatHistory + listOf(userMsg, aiMsg)
    messageText = ""
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier.size(32.dp).clip(CircleShape).background(CyberGold.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.SmartToy, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = Strings.managerChatTitle(currentLang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Safe Escrow & Deal Intelligence",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
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

      // Prompt Suggestion Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(GamingSurface)
          .padding(horizontal = 8.dp, vertical = 6.dp)
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        val suggestions = if (currentLang == AppLanguage.BANGLA) {
          listOf("ডিল স্লটের নিয়ম কী?", "কাস্টম ডিল কীভাবে খুলব?", "পেমেন্ট নিয়মাবলী", "ALL DONE বাটনের কাজ কী?", "ওনার কে?")
        } else {
          listOf("How do Deal Slots work?", "How to open Custom Deal?", "Payment guidelines", "What is ALL DONE?", "Who is Owner?")
        }

        suggestions.forEach { sug ->
          OutlinedButton(
            onClick = { handleSend(sug) },
            modifier = Modifier.height(30.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
          ) {
            Text(text = sug, fontSize = 10.sp, color = CyberGold)
          }
        }
      }

      // Admin Quick Deal Investigation bar (if Privileged)
      if (currentUser?.role == Role.ADMIN || currentUser?.role == Role.OWNER) {
        Surface(
          color = GamingSurfaceElevated,
          modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.FindInPage, contentDescription = null, tint = CyberGold, modifier = Modifier.size(16.dp))
            Text("Admin Deal Audit:", fontSize = 11.sp, color = CyberGold, fontWeight = FontWeight.Bold)
            deals.take(3).forEach { d ->
              Button(
                onClick = { handleSend("Deal ${d.dealNumber} এর বিস্তারিত তদন্ত রিপোর্ট বলো") },
                colors = ButtonDefaults.buttonColors(containerColor = GamingSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
                modifier = Modifier.height(26.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
              ) {
                Text(d.dealNumber, fontSize = 10.sp, color = FireOrange)
              }
            }
          }
        }
      }

      // Chat Messages List
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(chatHistory) { msg ->
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (msg.isAi) Alignment.Start else Alignment.End
          ) {
            Surface(
              color = if (msg.isAi) GamingSurface else FireOrange.copy(alpha = 0.25f),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (msg.isAi) CyberGold.copy(alpha = 0.3f) else FireOrange.copy(alpha = 0.5f)
              ),
              modifier = Modifier.widthIn(max = 320.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = msg.sender,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (msg.isAi) CyberGold else FireOrange
                  )
                  Text(text = msg.time, fontSize = 9.sp, color = TextMuted)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = msg.text,
                  color = TextPrimary,
                  fontSize = 13.sp,
                  lineHeight = 18.sp
                )
              }
            }
          }
        }
      }

      // Input Field
      Surface(
        color = GamingSurface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text(if (currentLang == AppLanguage.BANGLA) "ম্যানেজারকে প্রশ্ন করুন..." else "Ask manager...", fontSize = 12.sp) },
            modifier = Modifier.weight(1f).height(48.dp).testTag("manager_chat_input"),
            singleLine = true
          )
          IconButton(
            onClick = {
              if (messageText.isNotBlank()) {
                handleSend(messageText)
              }
            },
            modifier = Modifier
              .background(FireOrange, RoundedCornerShape(8.dp))
              .size(44.dp)
              .testTag("send_manager_chat_btn")
          ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TextPrimary, modifier = Modifier.size(20.dp))
          }
        }
      }
    }
  }
}
