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
import com.example.model.MarketplacePost
import com.example.model.PostStatus
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeratorPanelScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val posts by AppRepository.marketplacePosts.collectAsState()

  val pendingPosts = posts.filter { it.status == PostStatus.PENDING_REVIEW }

  var rejectPostTarget by remember { mutableStateOf<MarketplacePost?>(null) }
  var rejectReason by remember { mutableStateOf("") }
  var statusMessage by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = Strings.moderatorPanel(currentLang),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = GamingPurple
            )
            Text(
              text = "Content Review & Integrity",
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

      // Moderator Restrictions Notice
      Surface(
        color = GamingPurple.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingPurple),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Security, contentDescription = null, tint = GamingPurple, modifier = Modifier.size(16.dp))
            Text(
              text = "MODERATOR BOUNDARIES & POWERS",
              color = GamingPurple,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "• Allowed: Review marketplace posts, approve/reject with reason, moderate spam comments.\n• FORBIDDEN: Cannot alter bKash/Nagad payment numbers, cannot create moderators or admins, cannot modify owner.",
            color = TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }

      if (statusMessage.isNotBlank()) {
        Card(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
          colors = CardDefaults.cardColors(containerColor = GamingGreen.copy(alpha = 0.2f))
        ) {
          Text(text = statusMessage, color = GamingGreen, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
        }
      }

      Text(
        text = "Pending Posts for Review (${pendingPosts.size})",
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
      )

      if (pendingPosts.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = GamingGreen, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "All caught up! No pending posts to review.",
              color = TextSecondary,
              fontSize = 13.sp
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(pendingPosts) { post ->
            Card(
              modifier = Modifier.fillMaxWidth().testTag("mod_review_card_${post.id}"),
              colors = CardDefaults.cardColors(containerColor = GamingSurface),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = post.sellerName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                  RoleBadge(role = post.sellerRole)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = post.caption, color = FireOrange, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = post.description, color = TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "UID: ${post.freeFireId} | Price: ${post.price} BDT", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Button(
                    onClick = {
                      currentUser?.let { user ->
                        val res = AppRepository.reviewMarketplacePost(user, post.id, true)
                        res.onSuccess { statusMessage = it }
                      }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamingGreen),
                    modifier = Modifier.weight(1f).height(36.dp)
                  ) {
                    Text("Accept Post", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }

                  Button(
                    onClick = {
                      rejectPostTarget = post
                      rejectReason = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamingRed),
                    modifier = Modifier.weight(1f).height(36.dp)
                  ) {
                    Text("Reject...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  rejectPostTarget?.let { post ->
    AlertDialog(
      onDismissRequest = { rejectPostTarget = null },
      containerColor = GamingSurface,
      title = { Text("Reject Post Reason", color = GamingRed, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
      text = {
        OutlinedTextField(
          value = rejectReason,
          onValueChange = { rejectReason = it },
          label = { Text("Specify Reason") },
          modifier = Modifier.fillMaxWidth()
        )
      },
      confirmButton = {
        Button(
          onClick = {
            currentUser?.let { user ->
              AppRepository.reviewMarketplacePost(user, post.id, false, rejectReason)
              rejectPostTarget = null
              statusMessage = "Post rejected."
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = GamingRed)
        ) {
          Text("Confirm Reject")
        }
      },
      dismissButton = {
        TextButton(onClick = { rejectPostTarget = null }) {
          Text("Cancel", color = TextSecondary)
        }
      }
    )
  }
}
