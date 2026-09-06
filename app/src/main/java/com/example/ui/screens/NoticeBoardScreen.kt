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
fun NoticeBoardScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val notices by AppRepository.notices.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = Strings.noticeBoard(currentLang),
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
    ) {
      DemoModeBanner(
        currentUser = currentUser,
        onSwitchRole = { AppRepository.switchUserForDemo(it) }
      )

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = GamingSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Campaign, contentDescription = null, tint = FireOrange)
                Text(
                  text = if (currentLang == AppLanguage.BANGLA) "অফিসিয়াল পলিসি ও ডিল নির্দেশিকা" else "Official Policy & Escrow Rules",
                  color = FireOrange,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (currentLang == AppLanguage.BANGLA)
                  "সকল ক্রেতা ও বিক্রেতাকে সতর্ক করা যাচ্ছে যে প্ল্যাটফর্মের অফিসিয়াল ডিল স্লট ছাড়া বাইরের কোনো লেনদেনে ক্ষতি হলে অ্যাডমিন কর্তৃপক্ষ দায়ী থাকবে না।"
                else
                  "Always transact strictly within the official Deal Slot. The escrow protection applies only to platform deals.",
                color = TextSecondary,
                fontSize = 11.sp
              )
            }
          }
        }

        items(notices) { notice ->
          Card(
            modifier = Modifier.fillMaxWidth().testTag("notice_card_${notice.id}"),
            colors = CardDefaults.cardColors(containerColor = GamingSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (notice.isImportant) CyberGold.copy(alpha = 0.5f) else GamingBorder
            )
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (currentLang == AppLanguage.BANGLA) notice.titleBn else notice.titleEn,
                  color = if (notice.isImportant) CyberGold else TextPrimary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(text = notice.date, color = TextMuted, fontSize = 10.sp)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (currentLang == AppLanguage.BANGLA) notice.bodyBn else notice.bodyEn,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
              )
            }
          }
        }
      }
    }
  }
}
