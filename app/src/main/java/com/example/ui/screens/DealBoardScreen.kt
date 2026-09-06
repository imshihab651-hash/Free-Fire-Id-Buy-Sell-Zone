package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Deal
import com.example.model.DealStatus
import com.example.model.Role
import com.example.model.User
import com.example.ui.components.DealStatusBadge
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealBoardScreen(
  dealId: String,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  val currentUser by AppRepository.currentUser.collectAsState()
  val deals by AppRepository.deals.collectAsState()
  val adminConfig by AppRepository.adminConfig.collectAsState()

  val deal = deals.find { it.id == dealId }

  if (deal == null) {
    Box(
      modifier = modifier.fillMaxSize().background(GamingDarkBg),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = if (currentLang == AppLanguage.BANGLA) "ডিল পাওয়া যায়নি" else "Deal not found",
        color = TextSecondary
      )
    }
    return
  }

  // Local Form states
  var paymentMethod by remember { mutableStateOf("bKash") }
  var senderNumber by remember { mutableStateOf("") }
  var txId by remember { mutableStateOf("") }

  var ffUid by remember { mutableStateOf("") }
  var loginType by remember { mutableStateOf("Google") }
  var credentialSecret by remember { mutableStateOf("") }

  var settlementMethod by remember { mutableStateOf("bKash") }
  var settlementNumber by remember { mutableStateOf("") }

  var chatInput by remember { mutableStateOf("") }
  var actionError by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "${deal.dealNumber} - ${deal.slotTitle}",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "${Strings.taka(currentLang)} ${deal.totalPayment} (${deal.amount} + ${deal.adminFee} Fee)",
              fontSize = 12.sp,
              color = CyberGold
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("deal_board_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
          }
        },
        actions = {
          DealStatusBadge(status = deal.status, modifier = Modifier.padding(end = 12.dp))
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
        .verticalScroll(rememberScrollState())
    ) {
      // Demo Role Switcher
      DemoModeBanner(
        currentUser = currentUser,
        onSwitchRole = { AppRepository.switchUserForDemo(it) }
      )

      // Deal Summary Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = "DEAL NUMBER", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text(text = deal.dealNumber, color = FireOrange, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "TOTAL PAYMENT", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text(text = "${deal.totalPayment} ${Strings.taka(currentLang)}", color = CyberGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider(color = GamingDivider)
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Person, contentDescription = null, tint = GamingGreen, modifier = Modifier.size(16.dp))
              Column {
                Text(text = "Customer", color = TextMuted, fontSize = 10.sp)
                Text(text = deal.customerName, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Storefront, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
              Column(horizontalAlignment = Alignment.End) {
                Text(text = "Seller", color = TextMuted, fontSize = 10.sp)
                Text(text = deal.sellerName, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      if (actionError.isNotBlank()) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
          colors = CardDefaults.cardColors(containerColor = GamingRed.copy(alpha = 0.2f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, GamingRed)
        ) {
          Text(text = actionError, color = GamingRed, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
        }
      }

      // ==========================================
      // 1. CUSTOMER PAYMENT SECTION
      // ==========================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (deal.status == DealStatus.PAYMENT_FAILED) GamingRed else if (deal.status == DealStatus.CREATED) FireOrange else GamingBorder
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Payment, contentDescription = null, tint = FireOrange)
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "১. ক্রেতার পেমেন্ট সেকশন" else "1. Customer Payment Section",
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Case A: Payment Failed / Error
          if (deal.status == DealStatus.PAYMENT_FAILED) {
            Surface(
              color = GamingRed.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, GamingRed),
              modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "❌ " + Strings.paymentErrorMsg(currentLang),
                  color = GamingRed,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                if (deal.paymentErrorReason.isNotBlank()) {
                  Text(
                    text = "Reason: ${deal.paymentErrorReason}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                  onClick = {
                    AppRepository.retryPayment(deal.id)
                    senderNumber = ""
                    txId = ""
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = GamingRed),
                  modifier = Modifier.height(36.dp).testTag("payment_retry_btn")
                ) {
                  Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = Strings.retryBtn(currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          } else if (deal.status == DealStatus.CREATED) {
            // Unpaid - show payment instructions and input form
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "নিচের যেকোনো অ্যাডমিন ভেরিফাইড নম্বরে মোট ${deal.totalPayment} টাকা পাঠিয়ে Transaction ID সাবমিট করুন:"
              else
                "Send exactly ${deal.totalPayment} BDT to either verified number below and submit your TxID:",
              color = TextSecondary,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              color = GamingSurfaceElevated,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "bKash: ${adminConfig.bKashNumber}", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "Nagad: ${adminConfig.nagadNumber}", color = FireOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("bKash", "Nagad").forEach { m ->
                OutlinedButton(
                  onClick = { paymentMethod = m },
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (paymentMethod == m) FireOrange.copy(alpha = 0.2f) else GamingSurface
                  ),
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (paymentMethod == m) FireOrange else GamingBorder
                  ),
                  modifier = Modifier.weight(1f).height(36.dp).testTag("method_$m")
                ) {
                  Text(m, color = if (paymentMethod == m) FireOrange else TextPrimary, fontSize = 12.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = senderNumber,
              onValueChange = { senderNumber = it },
              label = { Text(if (currentLang == AppLanguage.BANGLA) "প্রেরক মোবাইল নম্বর" else "Sender Mobile Number") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              modifier = Modifier.fillMaxWidth().testTag("sender_number_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
              value = txId,
              onValueChange = { txId = it },
              label = { Text(if (currentLang == AppLanguage.BANGLA) "Transaction ID (TrxID)" else "Transaction ID") },
              modifier = Modifier.fillMaxWidth().testTag("tx_id_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                actionError = ""
                val res = AppRepository.submitPayment(deal.id, paymentMethod, senderNumber, txId)
                res.onFailure { actionError = it.message ?: "Failed" }
              },
              colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
              modifier = Modifier.fillMaxWidth().height(42.dp).testTag("submit_payment_btn")
            ) {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "পেমেন্ট তথ্য সাবমিট করুন" else "Submit Payment Info",
                fontWeight = FontWeight.Bold
              )
            }
          } else if (deal.status == DealStatus.PAYMENT_PENDING_VERIFICATION) {
            Surface(
              color = CyberGold.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = "⏳ " + Strings.paymentPending(currentLang),
                  color = CyberGold,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${deal.paymentMethod} | Sender: ${deal.senderNumber} | TxID: ${deal.transactionId}",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
                Text(
                  text = if (currentLang == AppLanguage.BANGLA)
                    "অ্যাডমিন ভেরিফিকেশন সম্পন্ন হলে বিক্রেতা ID সাবমিট করতে পারবেন।"
                  else
                    "Awaiting Admin verification. Seller can submit credentials once verified.",
                  color = TextMuted,
                  fontSize = 10.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }
          } else {
            // Payment verified or completed
            Surface(
              color = GamingGreen.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GamingGreen)
                Column {
                  Text(text = "Payment Verified ✅ (${deal.paymentMethod})", color = GamingGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  Text(text = "TxID: ${deal.transactionId} | Total: ${deal.totalPayment} BDT", color = TextSecondary, fontSize = 11.sp)
                }
              }
            }
          }
        }
      }

      // ==========================================
      // 2. SELLER ID & PASSWORD SUBMISSION SECTION
      // ==========================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.VpnKey, contentDescription = null, tint = NeonCyan)
            Text(
              text = if (currentLang == AppLanguage.BANGLA) "২. বিক্রেতা ID ও ক্রেডেনশিয়াল সেকশন" else "2. Seller ID & Credential Section",
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          val paymentIsDone = deal.status != DealStatus.CREATED &&
              deal.status != DealStatus.PAYMENT_PENDING_VERIFICATION &&
              deal.status != DealStatus.PAYMENT_FAILED

          if (!paymentIsDone) {
            Surface(
              color = GamingSurfaceElevated,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                Text(
                  text = if (currentLang == AppLanguage.BANGLA)
                    "🔒 পেমেন্ট সফল হওয়ার পরেই এই সেকশনটি বিক্রেতার জন্য সক্রিয় হবে।"
                  else
                    "🔒 Active ONLY after Payment is verified by Admin.",
                  color = TextMuted,
                  fontSize = 11.sp
                )
              }
            }
          } else if (deal.credentialUid.isBlank() && deal.status == DealStatus.PAYMENT_VERIFIED) {
            // Form for seller to submit credentials
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "ক্রেতার পেমেন্ট ভেরিফাইড হয়েছে। অনুগ্রহ করে আপনার Free Fire অ্যাকাউন্টের লগইন তথ্য দিন:"
              else
                "Buyer payment confirmed! Submit Free Fire account login credentials:",
              color = TextSecondary,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = ffUid,
              onValueChange = { ffUid = it },
              label = { Text("Free Fire UID / Player ID") },
              modifier = Modifier.fillMaxWidth().testTag("ff_uid_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              listOf("Google", "Facebook", "Twitter").forEach { t ->
                OutlinedButton(
                  onClick = { loginType = t },
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (loginType == t) NeonCyan.copy(alpha = 0.2f) else GamingSurface
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (loginType == t) NeonCyan else GamingBorder),
                  modifier = Modifier.weight(1f).height(36.dp)
                ) {
                  Text(t, fontSize = 11.sp, color = if (loginType == t) NeonCyan else TextPrimary)
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
              value = credentialSecret,
              onValueChange = { credentialSecret = it },
              label = { Text(if (currentLang == AppLanguage.BANGLA) "পাসওয়ার্ড বা ওটিপি/কোড" else "Account Password / Access Token") },
              modifier = Modifier.fillMaxWidth().testTag("credential_secret_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                actionError = ""
                currentUser?.let { user ->
                  val res = AppRepository.submitSellerCredentials(user, deal.id, ffUid, loginType, credentialSecret)
                  res.onFailure { actionError = it.message ?: "Failed" }
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = NeonCyanDark),
              modifier = Modifier.fillMaxWidth().height(42.dp).testTag("submit_creds_btn")
            ) {
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "ক্রেডেনশিয়াল সাবমিট করুন" else "Submit Credentials",
                color = GamingDarkBg,
                fontWeight = FontWeight.Bold
              )
            }
          } else if (deal.status == DealStatus.ID_SUBMITTED_PENDING || deal.status == DealStatus.ID_VERIFICATION_STARTED) {
            Surface(
              color = CyberGold.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "⏳ " + Strings.step5VerificationWait(currentLang),
                  color = CyberGold,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "UID: ${deal.credentialUid} (${deal.credentialType})",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
                Text(
                  text = if (currentLang == AppLanguage.BANGLA)
                    "অ্যাডমিন/সিস্টেম অ্যাকাউন্টটির পাসওয়ার্ড ও স্ট্যাটাস চেক করছে। ভেরিফিকেশন সফল হলে ক্রেতার নিকট রিলিজ করা হবে।"
                  else
                    "Admin is verifying credentials. They will be released to buyer once verified.",
                  color = TextMuted,
                  fontSize = 10.sp,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }
          } else {
            // Credentials released or deal completed
            Surface(
              color = GamingGreen.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "ID & Password Released ✅", color = GamingGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "UID: ${deal.credentialUid} (${deal.credentialType})", color = TextPrimary, fontSize = 12.sp)

                // Protected credential displayed only to authorized customer, seller, or admin
                if (currentUser?.id == deal.customerId || currentUser?.role?.isPrivileged == true || currentUser?.id == deal.sellerId) {
                  Spacer(modifier = Modifier.height(4.dp))
                  Surface(color = GamingSurface, shape = RoundedCornerShape(6.dp)) {
                    Text(
                      text = "Credentials: ${deal.credentialSecretToken}",
                      color = NeonCyan,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(6.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      // ==========================================
      // 3. ADMIN / MODERATOR VERIFICATION CONTROLS (Demo Friendly)
      // ==========================================
      if (currentUser?.role?.isPrivileged == true || currentUser?.role == Role.OWNER) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          colors = CardDefaults.cardColors(containerColor = GamingSurfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CyberGold)
              Text(
                text = "Admin/Mod Action Controls",
                color = CyberGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              if (deal.status == DealStatus.PAYMENT_PENDING_VERIFICATION) {
                Button(
                  onClick = {
                    currentUser?.let { AppRepository.verifyPayment(it, deal.id, true) }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = GamingGreen),
                  modifier = Modifier.weight(1f).height(38.dp).testTag("admin_approve_payment_btn")
                ) {
                  Text("Approve Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                  onClick = {
                    currentUser?.let { AppRepository.verifyPayment(it, deal.id, false, "Transaction ID mismatch") }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = GamingRed),
                  modifier = Modifier.weight(1f).height(38.dp).testTag("admin_reject_payment_btn")
                ) {
                  Text("Reject Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              } else if (deal.status == DealStatus.ID_SUBMITTED_PENDING || deal.status == DealStatus.ID_VERIFICATION_STARTED) {
                Button(
                  onClick = {
                    currentUser?.let { AppRepository.verifyAndReleaseCredentials(it, deal.id) }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = NeonCyanDark),
                  modifier = Modifier.fillMaxWidth().height(38.dp).testTag("admin_release_creds_btn")
                ) {
                  Text("Verify & Release ID to Buyer", color = GamingDarkBg, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              } else if (deal.status == DealStatus.DEAL_COMPLETED) {
                Button(
                  onClick = {
                    currentUser?.let { AppRepository.releaseSettlement(it, deal.id) }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                  modifier = Modifier.fillMaxWidth().height(38.dp).testTag("admin_release_settlement_btn")
                ) {
                  Text("Release Settlement to Seller (${deal.amount} BDT)", color = GamingDarkBg, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              } else {
                Text(
                  text = "Current Status: ${deal.status.name} (No pending admin action)",
                  color = TextMuted,
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }

      // ==========================================
      // 4. CUSTOMER VERIFICATION & ALL DONE
      // ==========================================
      if (deal.status == DealStatus.ID_RELEASED) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          colors = CardDefaults.cardColors(containerColor = GamingSurface),
          border = androidx.compose.foundation.BorderStroke(2.dp, GamingGreen),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "৩. অ্যাকাউন্ট যাচাই ও ALL DONE কনফার্মেশন"
              else
                "3. Customer Check & ALL DONE Confirmation",
              color = GamingGreen,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (currentLang == AppLanguage.BANGLA)
                "আইডি ও পাসওয়ার্ড চেক করুন। সবকিছু সঠিক পেলে তবেই নিচের ALL DONE বাটনে চাপুন।"
              else
                "Check the login carefully. If satisfied, tap ALL DONE to complete the deal.",
              color = TextSecondary,
              fontSize = 11.sp,
              modifier = Modifier.padding(vertical = 6.dp)
            )

            Button(
              onClick = {
                currentUser?.let { AppRepository.customerConfirmAllDone(it, deal.id) }
              },
              colors = ButtonDefaults.buttonColors(containerColor = GamingGreen),
              modifier = Modifier.fillMaxWidth().height(46.dp).testTag("customer_all_done_btn")
            ) {
              Icon(Icons.Default.DoneAll, contentDescription = null, tint = GamingDarkBg)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = Strings.step9AllDone(currentLang),
                color = GamingDarkBg,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        }
      }

      // ==========================================
      // 5. SELLER SETTLEMENT SECTION
      // ==========================================
      if (deal.status == DealStatus.DEAL_COMPLETED || deal.status == DealStatus.SETTLEMENT_COMPLETED) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          colors = CardDefaults.cardColors(containerColor = GamingSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = CyberGold)
              Text(
                text = Strings.settlementTitle(currentLang),
                color = CyberGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = Strings.settlementDesc(currentLang),
              color = TextSecondary,
              fontSize = 11.sp,
              modifier = Modifier.padding(vertical = 4.dp)
            )

            if (deal.status == DealStatus.SETTLEMENT_COMPLETED) {
              Surface(
                color = GamingGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "✅ Settlement Released (${deal.amount} BDT to ${deal.sellerSettlementMethod})",
                  color = GamingGreen,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(10.dp)
                )
              }
            } else {
              if (deal.sellerSettlementNumber.isBlank()) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf("bKash", "Nagad").forEach { m ->
                    OutlinedButton(
                      onClick = { settlementMethod = m },
                      colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (settlementMethod == m) CyberGold.copy(alpha = 0.2f) else GamingSurface
                      ),
                      border = androidx.compose.foundation.BorderStroke(1.dp, if (settlementMethod == m) CyberGold else GamingBorder),
                      modifier = Modifier.weight(1f).height(34.dp)
                    ) {
                      Text(m, fontSize = 11.sp, color = if (settlementMethod == m) CyberGold else TextPrimary)
                    }
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                  value = settlementNumber,
                  onValueChange = { settlementNumber = it },
                  label = { Text("Private Settlement Number (bKash/Nagad)") },
                  modifier = Modifier.fillMaxWidth().testTag("settlement_number_input"),
                  singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                  onClick = {
                    currentUser?.let {
                      AppRepository.submitSellerSettlementInfo(it, deal.id, settlementMethod, settlementNumber)
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                  modifier = Modifier.fillMaxWidth().height(38.dp).testTag("submit_settlement_info_btn")
                ) {
                  Text("Save Settlement Info", color = GamingDarkBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
              } else {
                Surface(
                  color = GamingSurfaceElevated,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Status: Settlement Ready / Eligible ✅", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Method: ${deal.sellerSettlementMethod} | Account: ${deal.sellerSettlementNumber}", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "Amount: ${deal.amount} BDT", color = FireOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }

      // ==========================================
      // 6. PRIVATE CUSTOMER <-> SELLER MESSAGING
      // ==========================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Chat, contentDescription = null, tint = FireOrange)
              Text(
                text = if (currentLang == AppLanguage.BANGLA) "প্রাইভেট ডিল মেসেজিং (${deal.dealNumber})" else "Private Deal Messages (${deal.dealNumber})",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(text = "${deal.messages.size} msgs", color = TextMuted, fontSize = 11.sp)
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Messages List
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(min = 80.dp, max = 220.dp)
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            deal.messages.forEach { msg ->
              val isMe = msg.senderId == currentUser?.id
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
              ) {
                Surface(
                  color = if (isMe) FireOrange.copy(alpha = 0.2f) else GamingSurfaceElevated,
                  shape = RoundedCornerShape(8.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, if (isMe) FireOrange.copy(alpha = 0.5f) else GamingBorder),
                  modifier = Modifier.widthIn(max = 280.dp)
                ) {
                  Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Text(text = msg.senderName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isMe) FireOrange else TextSecondary)
                      Text(text = msg.timestamp, fontSize = 9.sp, color = TextMuted)
                    }
                    Text(text = msg.text, fontSize = 12.sp, color = TextPrimary, modifier = Modifier.padding(top = 2.dp))
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Message Input
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            OutlinedTextField(
              value = chatInput,
              onValueChange = { chatInput = it },
              placeholder = { Text(if (currentLang == AppLanguage.BANGLA) "মেসেজ লিখুন..." else "Type message...") },
              modifier = Modifier.weight(1f).height(48.dp).testTag("deal_chat_input"),
              singleLine = true
            )
            IconButton(
              onClick = {
                if (chatInput.isNotBlank()) {
                  currentUser?.let { user ->
                    AppRepository.sendDealMessage(deal.id, user, chatInput)
                    chatInput = ""
                  }
                }
              },
              modifier = Modifier
                .background(FireOrange, RoundedCornerShape(8.dp))
                .size(44.dp)
                .testTag("send_deal_chat_btn")
            ) {
              Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TextPrimary)
            }
          }
        }
      }

      // ==========================================
      // 7. PROOF & STATUS TIMELINE
      // ==========================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GamingSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = if (currentLang == AppLanguage.BANGLA) "প্রুফ ও স্ট্যাটাস টাইমলাইন" else "Proof & Status Timeline",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          deal.timelineEvents.forEachIndexed { idx, ev ->
            Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (ev.isCompleted) Icons.Default.CheckCircle else if (ev.isCurrent) Icons.Default.Pending else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (ev.isCompleted) GamingGreen else if (ev.isCurrent) CyberGold else TextMuted,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = if (currentLang == AppLanguage.BANGLA) ev.titleBn else ev.titleEn,
                  fontSize = 12.sp,
                  fontWeight = if (ev.isCompleted || ev.isCurrent) FontWeight.Bold else FontWeight.Normal,
                  color = if (ev.isCompleted) TextPrimary else if (ev.isCurrent) CyberGold else TextMuted
                )
              }
              if (ev.timestamp != "-") {
                Text(text = ev.timestamp, fontSize = 10.sp, color = TextMuted)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
