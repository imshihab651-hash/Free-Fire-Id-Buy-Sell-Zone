package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.MarketplacePost
import com.example.model.PostStatus
import com.example.model.Role
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
  onBack: () -> Unit,
  onOpenDealForPost: (MarketplacePost) -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val posts by AppRepository.marketplacePosts.collectAsState()

  var showCreatePostDialog by remember { mutableStateOf(false) }
  var ffIdInput by remember { mutableStateOf("") }
  var priceInput by remember { mutableStateOf("") }
  var captionInput by remember { mutableStateOf("") }
  var descInput by remember { mutableStateOf("") }
  var videoUrlInput by remember { mutableStateOf("") }
  var photoCount by remember { mutableStateOf(3) }
  var createError by remember { mutableStateOf("") }

  // Reject Dialog state
  var postToReject by remember { mutableStateOf<MarketplacePost?>(null) }
  var rejectReason by remember { mutableStateOf("") }

  // Expanded comment post IDs
  var expandedCommentsPostId by remember { mutableStateOf<String?>(null) }
  var commentInput by remember { mutableStateOf("") }

  // For regular users: only show ACCEPTED posts.
  // For Admin / Moderator: show all posts (or tabs: Approved / Pending Review)
  val isStaff = currentUser?.role?.isPrivileged == true || currentUser?.role == Role.OWNER
  var selectedTab by remember { mutableStateOf(0) } // 0: Approved, 1: Pending

  val visiblePosts = if (isStaff) {
    if (selectedTab == 0) posts.filter { it.status == PostStatus.ACCEPTED }
    else posts.filter { it.status == PostStatus.PENDING_REVIEW || it.status == PostStatus.REJECTED }
  } else {
    posts.filter { it.status == PostStatus.ACCEPTED || it.sellerId == currentUser?.id }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = Strings.marketplace(currentLang),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Free Fire ID Exchange",
              fontSize = 11.sp,
              color = NeonCyan
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
          }
        },
        actions = {
          if (currentUser?.role == Role.SELLER || isStaff) {
            Button(
              onClick = { showCreatePostDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.height(34.dp).padding(end = 8.dp).testTag("create_marketplace_post_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(if (currentLang == AppLanguage.BANGLA) "পোস্ট করুন" else "New Post", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
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

      if (isStaff) {
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
            text = { Text("Approved (${posts.count { it.status == PostStatus.ACCEPTED }})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Pending Review (${posts.count { it.status == PostStatus.PENDING_REVIEW }})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberGold) }
          )
        }
      }

      if (visiblePosts.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "কোনো পোস্ট পাওয়া যায়নি" else "No posts found",
              color = TextSecondary,
              fontSize = 14.sp
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(visiblePosts) { post ->
            Card(
              modifier = Modifier.fillMaxWidth().testTag("post_card_${post.id}"),
              colors = CardDefaults.cardColors(containerColor = GamingSurface),
              shape = RoundedCornerShape(14.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (post.status == PostStatus.PENDING_REVIEW) CyberGold.copy(alpha = 0.5f) else GamingBorder
              )
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                // Post Header
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GamingSurfaceElevated),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Column {
                      Text(text = post.sellerName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                      Text(text = post.createdAt, color = TextMuted, fontSize = 10.sp)
                    }
                  }
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    RoleBadge(role = post.sellerRole)
                    if (post.status != PostStatus.ACCEPTED) {
                      Surface(
                        color = (if (post.status == PostStatus.PENDING_REVIEW) CyberGold else GamingRed).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (post.status == PostStatus.PENDING_REVIEW) CyberGold else GamingRed)
                      ) {
                        Text(
                          text = post.status.name,
                          color = if (post.status == PostStatus.PENDING_REVIEW) CyberGold else GamingRed,
                          fontSize = 10.sp,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Post Title & Details
                Text(
                  text = post.caption,
                  color = TextPrimary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = post.description,
                  color = TextSecondary,
                  fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Free Fire UID & Price banner
                Surface(
                  color = GamingSurfaceElevated,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(text = "FREE FIRE ACCOUNT", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      Text(text = post.freeFireId, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                      Text(text = "ASKING PRICE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      Text(text = "${post.price} ${Strings.taka(currentLang)}", color = CyberGold, fontSize = 15.sp, fontWeight = FontWeight.Black)
                    }
                  }
                }

                // Photos Carousel (Up to 6)
                if (post.photos.isNotEmpty()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    post.photos.forEachIndexed { i, _ ->
                      Surface(
                        modifier = Modifier.size(70.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = GamingSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
                      ) {
                        Box(contentAlignment = Alignment.Center) {
                          Icon(Icons.Default.Image, contentDescription = null, tint = FireOrange, modifier = Modifier.size(24.dp))
                          Text(
                            text = "Photo ${i + 1}",
                            color = TextMuted,
                            fontSize = 9.sp,
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 2.dp)
                          )
                        }
                      }
                    }
                  }
                }

                if (!post.videoUrl.isNullOrBlank()) {
                  Spacer(modifier = Modifier.height(6.dp))
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    Text(text = "Video Preview Attached", color = NeonCyan, fontSize = 11.sp)
                  }
                }

                // Staff Moderation Actions (Accept / Reject with reason)
                if (isStaff && post.status == PostStatus.PENDING_REVIEW) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Button(
                      onClick = {
                        currentUser?.let { user ->
                          AppRepository.reviewMarketplacePost(user, post.id, true)
                        }
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = GamingGreen),
                      modifier = Modifier.weight(1f).height(36.dp).testTag("accept_post_${post.id}")
                    ) {
                      Text(Strings.accept(currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                      onClick = {
                        postToReject = post
                        rejectReason = ""
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = GamingRed),
                      modifier = Modifier.weight(1f).height(36.dp).testTag("reject_post_${post.id}")
                    ) {
                      Text(Strings.reject(currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = GamingDivider)
                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Actions: Deal button + Comments toggle
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  TextButton(
                    onClick = {
                      expandedCommentsPostId = if (expandedCommentsPostId == post.id) null else post.id
                    },
                    modifier = Modifier.testTag("toggle_comments_${post.id}")
                  ) {
                    Icon(Icons.Default.Comment, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${Strings.comments(currentLang)} (${post.comments.size})",
                      color = TextSecondary,
                      fontSize = 12.sp
                    )
                  }

                  if (post.status == PostStatus.ACCEPTED && post.sellerId != currentUser?.id) {
                    Button(
                      onClick = { onOpenDealForPost(post) },
                      colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.height(34.dp).testTag("buy_deal_for_post_${post.id}")
                    ) {
                      Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = if (currentLang == AppLanguage.BANGLA) "ডিল খুলুন" else "Open Deal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }

                // Expandable Comments Section
                AnimatedVisibility(visible = expandedCommentsPostId == post.id) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 8.dp)
                  ) {
                    post.comments.forEach { comment ->
                      Surface(
                        color = GamingSurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                      ) {
                        Row(
                          modifier = Modifier.padding(8.dp),
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                          Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                              Text(text = comment.userName, color = FireOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                              Text(text = comment.createdAt, color = TextMuted, fontSize = 9.sp)
                            }
                            Text(text = comment.text, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                          }

                          // Delete comment button if author or staff
                          if (comment.userId == currentUser?.id || isStaff) {
                            IconButton(
                              onClick = {
                                currentUser?.let { user ->
                                  AppRepository.deleteComment(user, post.id, comment.id)
                                }
                              },
                              modifier = Modifier.size(28.dp)
                            ) {
                              Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = GamingRed, modifier = Modifier.size(16.dp))
                            }
                          }
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Add comment input
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        placeholder = { Text(if (currentLang == AppLanguage.BANGLA) "মন্তব্য লিখুন..." else "Add comment...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).height(44.dp).testTag("comment_input_${post.id}"),
                        singleLine = true
                      )
                      IconButton(
                        onClick = {
                          if (commentInput.isNotBlank()) {
                            currentUser?.let { user ->
                              AppRepository.addComment(user, post.id, commentInput)
                              commentInput = ""
                            }
                          }
                        },
                        modifier = Modifier
                          .background(FireOrange, RoundedCornerShape(8.dp))
                          .size(40.dp)
                          .testTag("submit_comment_${post.id}")
                      ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TextPrimary, modifier = Modifier.size(18.dp))
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

  // Create Post Dialog
  if (showCreatePostDialog) {
    AlertDialog(
      onDismissRequest = {
        showCreatePostDialog = false
        createError = ""
      },
      containerColor = GamingSurface,
      title = {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "Free Fire আইডি বিক্রয় পোস্ট তৈরি" else "Create Free Fire ID Sale Post",
          color = TextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column(
          modifier = Modifier.verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (createError.isNotBlank()) {
            Text(text = createError, color = GamingRed, fontSize = 11.sp)
          }

          OutlinedTextField(
            value = ffIdInput,
            onValueChange = { ffIdInput = it },
            label = { Text("Free Fire UID / Account Level") },
            modifier = Modifier.fillMaxWidth().testTag("post_uid_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = priceInput,
            onValueChange = { priceInput = it },
            label = { Text("Price (BDT)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("post_price_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = captionInput,
            onValueChange = { captionInput = it },
            label = { Text("Post Title / Caption") },
            modifier = Modifier.fillMaxWidth().testTag("post_caption_input"),
            singleLine = true
          )

          OutlinedTextField(
            value = descInput,
            onValueChange = { descInput = it },
            label = { Text("Description & EVO/Gun Details") },
            modifier = Modifier.fillMaxWidth().testTag("post_desc_input"),
            maxLines = 3
          )

          OutlinedTextField(
            value = videoUrlInput,
            onValueChange = { videoUrlInput = it },
            label = { Text("Video URL (YouTube/Drive - Optional)") },
            modifier = Modifier.fillMaxWidth().testTag("post_video_input"),
            singleLine = true
          )

          Text(
            text = "Photos: $photoCount selected (Max 6 photos allowed)",
            color = TextSecondary,
            fontSize = 11.sp
          )
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(1, 2, 3, 4, 5, 6).forEach { num ->
              OutlinedButton(
                onClick = { photoCount = num },
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (photoCount == num) FireOrange.copy(alpha = 0.2f) else GamingSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (photoCount == num) FireOrange else GamingBorder),
                modifier = Modifier.size(34.dp),
                contentPadding = PaddingValues(0.dp)
              ) {
                Text("$num", fontSize = 11.sp, color = if (photoCount == num) FireOrange else TextPrimary)
              }
            }
          }

          Text(
            text = "⚠️ পলিসি: সাবমিট করার পর পোস্টটি PENDING REVIEW মোডে যাবে। অ্যাডমিন বা মডারেটর যাচাই করে অনুমোদন দিলে তা পাবলিক মার্কেটপ্লেসে প্রদর্শিত হবে।",
            color = CyberGold,
            fontSize = 10.sp
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val price = priceInput.toIntOrNull()
            if (price == null || price <= 0 || ffIdInput.isBlank() || captionInput.isBlank()) {
              createError = "Please enter valid UID, price and caption."
            } else {
              currentUser?.let { seller ->
                val photos = (1..photoCount).map { "photo-$it" }
                val res = AppRepository.createMarketplacePost(
                  seller = seller,
                  freeFireId = ffIdInput,
                  price = price,
                  caption = captionInput,
                  description = descInput,
                  photos = photos,
                  videoUrl = videoUrlInput.ifBlank { null }
                )
                res.onSuccess {
                  showCreatePostDialog = false
                  ffIdInput = ""
                  priceInput = ""
                  captionInput = ""
                  descInput = ""
                  videoUrlInput = ""
                }.onFailure {
                  createError = it.message ?: "Failed to create post."
                }
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
          modifier = Modifier.testTag("submit_marketplace_post_btn")
        ) {
          Text(if (currentLang == AppLanguage.BANGLA) "সাবমিট করুন" else "Submit Post")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreatePostDialog = false }) {
          Text(if (currentLang == AppLanguage.BANGLA) "বাতিল" else "Cancel", color = TextSecondary)
        }
      }
    )
  }

  // Reject Dialog with Reason
  postToReject?.let { post ->
    AlertDialog(
      onDismissRequest = { postToReject = null },
      containerColor = GamingSurface,
      title = {
        Text(text = Strings.rejectReasonTitle(currentLang), color = GamingRed, fontSize = 15.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          Text(
            text = "Post: ${post.caption}",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          OutlinedTextField(
            value = rejectReason,
            onValueChange = { rejectReason = it },
            label = { Text("Reason for Rejection") },
            modifier = Modifier.fillMaxWidth().testTag("reject_reason_input"),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            currentUser?.let { user ->
              AppRepository.reviewMarketplacePost(user, post.id, false, rejectReason)
              postToReject = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = GamingRed),
          modifier = Modifier.testTag("confirm_reject_btn")
        ) {
          Text(Strings.reject(currentLang))
        }
      },
      dismissButton = {
        TextButton(onClick = { postToReject = null }) {
          Text(Strings.cancel(currentLang), color = TextSecondary)
        }
      }
    )
  }
}
