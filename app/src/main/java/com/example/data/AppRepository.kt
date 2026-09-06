package com.example.data

import com.example.model.*
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object AppRepository {

  // Current Language
  private val _currentLanguage = MutableStateFlow(AppLanguage.BANGLA)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  fun toggleLanguage() {
    _currentLanguage.value = if (_currentLanguage.value == AppLanguage.BANGLA) {
      AppLanguage.ENGLISH
    } else {
      AppLanguage.BANGLA
    }
  }

  fun setLanguage(lang: AppLanguage) {
    _currentLanguage.value = lang
  }

  // Pre-configured Users
  val ownerUser = User(
    id = "user-owner-shihab",
    name = "Shihab Talukder",
    username = "shihab_owner",
    role = Role.OWNER,
    isActive = true,
    avatarUrl = "",
    joinedDate = "2023-01-01",
    phone = "01711-223344",
    isOwner = true,
  )

  val modUser1 = User(
    id = "user-mod-rafiq",
    name = "Rafiqul Islam",
    username = "rafiq_mod",
    role = Role.MODERATOR,
    isActive = true,
    avatarUrl = "",
    joinedDate = "2023-06-10",
    phone = "01811-334455",
  )

  val customerUser = User(
    id = "user-customer-tanvir",
    name = "Tanvir Ahmed",
    username = "tanvir_ff",
    role = Role.CUSTOMER,
    isActive = true,
    avatarUrl = "",
    joinedDate = "2024-02-14",
    phone = "01911-778899",
  )

  val sellerUser = User(
    id = "user-seller-mahmud",
    name = "Mahmud Hasan",
    username = "mahmud_pro",
    role = Role.SELLER,
    isActive = true,
    avatarUrl = "",
    joinedDate = "2024-01-20",
    phone = "01611-445566",
  )

  // Users State
  private val _users = MutableStateFlow<List<User>>(
    listOf(ownerUser, modUser1, customerUser, sellerUser)
  )
  val users: StateFlow<List<User>> = _users.asStateFlow()

  // Current User Session
  private val _currentUser = MutableStateFlow<User?>(customerUser)
  val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

  // Admin Config
  private val _adminConfig = MutableStateFlow(AdminConfig())
  val adminConfig: StateFlow<AdminConfig> = _adminConfig.asStateFlow()

  // Notices
  private val _notices = MutableStateFlow<List<NoticeItem>>(
    listOf(
      NoticeItem(
        id = "n-1",
        titleBn = "নিরাপদ লেনদেনের অফিসিয়াল নিয়মাবলী",
        titleEn = "Official Rules for Safe Deal Exchange",
        bodyBn = "যেকোনো Free Fire ID ক্রয়-বিক্রয়ের ক্ষেত্রে প্ল্যাটফর্মের অফিসিয়াল এসক্রো ছাড়া সরাসরি কোনো লেনদেন করবেন না। ক্রেতা ALL DONE দেওয়ার পূর্বে বিক্রেতাকে পেমেন্ট রিলিজ করা হবে না।",
        bodyEn = "Never trade directly outside the platform's official escrow. Payment is strictly held until the buyer checks the account and clicks ALL DONE.",
        date = "2024-03-01",
        isActive = true,
        isImportant = true,
      ),
      NoticeItem(
        id = "n-2",
        titleBn = "bKash ও Nagad পেমেন্ট নির্দেশনা",
        titleEn = "bKash & Nagad Payment Verification Notice",
        bodyBn = "অ্যাডমিন কর্তৃক নির্ধারিত নম্বরে সেন্ড মানি বা ক্যাশ ইন করে সঠিক Transaction ID সাবমিট করুন। ভুল তথ্য দিলে লাল সতর্কতা দেখাবে এবং আবার চেষ্টা করতে হবে।",
        bodyEn = "Send payment to the Admin verified bKash or Nagad number and submit your Transaction ID accurately. Mismatched data will prompt a retry.",
        date = "2024-03-05",
        isActive = true,
      ),
    )
  )
  val notices: StateFlow<List<NoticeItem>> = _notices.asStateFlow()

  // Deal Slots (10 predefined slots)
  private val _dealSlots = MutableStateFlow<List<DealSlot>>(
    listOf(
      DealSlot(1, "স্লট ১ (১০০০–১৫০০ টাকা)", "Slot 1 (1000–1500 BDT)", 1000, 1500, 150),
      DealSlot(2, "স্লট ২ (২০০০–২৫০০ টাকা)", "Slot 2 (2000–2500 BDT)", 2000, 2500, 200),
      DealSlot(3, "স্লট ৩ (৩০০০–৩৫০০ টাকা)", "Slot 3 (3000–3500 BDT)", 3000, 3500, 300),
      DealSlot(4, "স্লট ৪ (৪০০০–৪৫০০ টাকা)", "Slot 4 (4000–4500 BDT)", 4000, 4500, 400),
      DealSlot(5, "স্লট ৫ (৫০০০–৫৫০০ টাকা)", "Slot 5 (5000–5500 BDT)", 5000, 5500, 500),
      DealSlot(6, "স্লট ৬ (৬০০০–৬৫০০ টাকা)", "Slot 6 (6000–6500 BDT)", 6000, 6500, 600),
      DealSlot(7, "স্লট ৭ (৭০০০–৭৫০০ টাকা)", "Slot 7 (7000–7500 BDT)", 7000, 7500, 700),
      DealSlot(8, "স্লট ৮ (৮০০০–৮৫০০ টাকা)", "Slot 8 (8000–8500 BDT)", 8000, 8500, 800),
      DealSlot(9, "স্লট ৯ (৯০০০–৯৫০০ টাকা)", "Slot 9 (9000–9500 BDT)", 9000, 9500, 900),
      DealSlot(10, "স্লট ১০ (১০০০০–১০৫০০ টাকা)", "Slot 10 (10000–10500 BDT)", 10000, 10500, 1000),
    )
  )
  val dealSlots: StateFlow<List<DealSlot>> = _dealSlots.asStateFlow()

  // Deals List
  private var dealCounter = 1
  private val _deals = MutableStateFlow<List<Deal>>(emptyList())
  val deals: StateFlow<List<Deal>> = _deals.asStateFlow()

  // Notifications
  private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  // Audit Logs
  private val _auditLogs = MutableStateFlow<List<AuditLogItem>>(emptyList())
  val auditLogs: StateFlow<List<AuditLogItem>> = _auditLogs.asStateFlow()

  // Community Info
  private val _communityInfo = MutableStateFlow(CommunityInfo())
  val communityInfo: StateFlow<CommunityInfo> = _communityInfo.asStateFlow()

  // Marketplace Posts
  private val _marketplacePosts = MutableStateFlow<List<MarketplacePost>>(emptyList())
  val marketplacePosts: StateFlow<List<MarketplacePost>> = _marketplacePosts.asStateFlow()

  // Support Tickets
  private val _supportTickets = MutableStateFlow<List<SupportTicket>>(emptyList())
  val supportTickets: StateFlow<List<SupportTicket>> = _supportTickets.asStateFlow()

  init {
    initializeSampleData()
  }

  private fun getCurrentTimeString(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
  }

  private fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
  }

  private fun initializeSampleData() {
    // Initial sample deal
    val deal1 = Deal(
      id = "deal-1",
      dealNumber = "DEAL #0001",
      customerId = customerUser.id,
      customerName = customerUser.name,
      sellerId = sellerUser.id,
      sellerName = sellerUser.name,
      amount = 3500,
      adminFee = 300,
      totalPayment = 3800,
      slotTitle = "Slot 3 (3000–3500 BDT)",
      status = DealStatus.PAYMENT_VERIFIED,
      paymentMethod = "bKash",
      senderNumber = "01799-887766",
      transactionId = "TRX9876543210",
      createdAt = "2024-03-06 10:15",
      updatedAt = "2024-03-06 10:25",
      timelineEvents = listOf(
        TimelineEvent("ক্রেতা ডিল খুলেছেন", "Customer opened Deal", "10:15", true),
        TimelineEvent("পেমেন্ট সম্পন্ন হয়েছে (bKash)", "Payment submitted (bKash)", "10:20", true),
        TimelineEvent("পেমেন্ট ভেরিফাইড ✅", "Payment Successful ✅", "10:25", true, isCurrent = true),
        TimelineEvent("বিক্রেতা ID ও পাসওয়ার্ড জমা", "Seller ID & Password Submission", "-", false),
        TimelineEvent("ID ভেরিফিকেশন চলমান", "ID Verification Started", "-", false),
        TimelineEvent("ID ভেরিফিকেশন সফল ✅", "ID Verification Successful ✅", "-", false),
        TimelineEvent("ক্রেতার নিকট ক্রেডেনশিয়াল রিলিজ", "ID & Password Released ✅", "-", false),
        TimelineEvent("ক্রেতা ALL DONE কনফার্মেশন", "Customer All Done ✅", "-", false),
        TimelineEvent("ডিল সম্পূর্ণ হয়েছে ✅", "Deal Completed ✅", "-", false),
        TimelineEvent("বিক্রেতা সেটেলমেন্ট প্রস্তুত ✅", "Settlement Ready / Eligible ✅", "-", false),
      ),
      messages = listOf(
        DealMessage("m-1", "deal-1", customerUser.id, customerUser.name, Role.CUSTOMER, "ভাই, আমি ৩,৮০০ টাকা পেমেন্ট করেছি। অ্যাডমিন ভেরিফাই করেছে। আপনার ID ডিটেইলস দিন।", "10:26"),
        DealMessage("m-2", "deal-1", sellerUser.id, sellerUser.name, Role.SELLER, "ঠিক আছে ভাই, আমি সিকিউর ফর্মে Free Fire ID ও ওয়ান-টাইম কোড সাবমিট করছি।", "10:28"),
      )
    )

    // Initial sample deal 2 (Completed)
    val deal2 = Deal(
      id = "deal-2",
      dealNumber = "DEAL #0002",
      customerId = customerUser.id,
      customerName = customerUser.name,
      sellerId = sellerUser.id,
      sellerName = sellerUser.name,
      amount = 1200,
      adminFee = 150,
      totalPayment = 1350,
      slotTitle = "Slot 1 (1000–1500 BDT)",
      status = DealStatus.DEAL_COMPLETED,
      paymentMethod = "Nagad",
      senderNumber = "01688-112233",
      transactionId = "NGD1122334455",
      credentialUid = "FF-UID-98765432",
      credentialType = "Google",
      credentialSecretToken = "Account verified and successfully transferred",
      sellerSettlementMethod = "Nagad",
      sellerSettlementNumber = "01611-445566",
      createdAt = "2024-03-05 14:00",
      updatedAt = "2024-03-05 15:30",
      timelineEvents = listOf(
        TimelineEvent("ক্রেতা ডিল খুলেছেন", "Customer opened Deal", "14:00", true),
        TimelineEvent("পেমেন্ট সম্পন্ন হয়েছে (Nagad)", "Payment submitted (Nagad)", "14:10", true),
        TimelineEvent("পেমেন্ট ভেরিফাইড ✅", "Payment Successful ✅", "14:15", true),
        TimelineEvent("বিক্রেতা ID জমা দিয়েছেন", "Seller ID & Password Submitted ✅", "14:25", true),
        TimelineEvent("ID ভেরিফিকেশন সফল ✅", "ID Verification Successful ✅", "14:40", true),
        TimelineEvent("ক্রেতার নিকট ক্রেডেনশিয়াল রিলিজ ✅", "ID & Password Released ✅", "14:45", true),
        TimelineEvent("ক্রেতা ALL DONE নিশ্চিত করেছেন ✅", "Customer All Done ✅", "15:00", true),
        TimelineEvent("ডিল সম্পন্ন হয়েছে ✅", "Deal Completed ✅", "15:10", true),
        TimelineEvent("বিক্রেতা সেটেলমেন্ট প্রস্তুত ✅", "Settlement Ready / Eligible ✅", "15:30", true),
      )
    )

    _deals.value = listOf(deal1, deal2)
    dealCounter = 3

    // Sample Marketplace posts
    val post1 = MarketplacePost(
      id = "post-1",
      sellerId = sellerUser.id,
      sellerName = sellerUser.name,
      sellerRole = Role.SELLER,
      freeFireId = "UID: 88449921 (Level 74)",
      price = 4500,
      photos = listOf(
        "drawable/ic_ff_zone_logo",
      ),
      caption = "Free Fire Pro Account | Old Incubator + Cobra MP40 Max + Titan Scar",
      description = "Level 74 account, 48+ EVO guns, 12 Elite Passes, 4600+ likes. 100% safe handover through Admin Escrow deal slot.",
      status = PostStatus.ACCEPTED,
      createdAt = "2024-03-05 12:00",
      comments = listOf(
        CommentItem("c-1", "post-1", customerUser.id, customerUser.name, Role.CUSTOMER, "ভাই, ৩,৮০০ টাকায় ডিল স্লট ওপেন করবেন?", "12:30"),
      )
    )

    val post2 = MarketplacePost(
      id = "post-2",
      sellerId = sellerUser.id,
      sellerName = sellerUser.name,
      sellerRole = Role.SELLER,
      freeFireId = "UID: 77112233 (Level 68)",
      price = 2200,
      photos = emptyList(),
      caption = "Fresh Grandmaster Badge Account | 5 EVO weapons",
      description = "Clean Google login, instant transfer ready through Deal Slot 2.",
      status = PostStatus.PENDING_REVIEW,
      createdAt = "2024-03-06 09:00",
    )

    _marketplacePosts.value = listOf(post1, post2)

    // Sample notifications
    _notifications.value = listOf(
      NotificationItem(
        id = "notif-1",
        recipientId = customerUser.id,
        type = NotificationType.PAYMENT_SUCCESSFUL,
        titleBn = "পেমেন্ট ভেরিফাইড ✅",
        titleEn = "Payment Successful ✅",
        messageBn = "DEAL #0001 এর ৩,৮০০ টাকা পেমেন্ট অ্যাডমিন কর্তৃক ভেরিফাই হয়েছে।",
        messageEn = "Payment of 3800 BDT for DEAL #0001 was verified by Admin.",
        timestamp = "10:25",
        dealId = "deal-1",
      ),
      NotificationItem(
        id = "notif-2",
        recipientId = customerUser.id,
        type = NotificationType.ADMIN_ANNOUNCEMENT,
        titleBn = "অ্যাডমিন নোটিশ",
        titleEn = "Admin Announcement",
        messageBn = "সকল ডিল শুধুমাত্র অফিসিয়াল Deal Slot বা Custom Deal Box-এ সম্পন্ন করুন।",
        messageEn = "Please conduct all trades strictly within the official Deal Box.",
        timestamp = "09:00",
      ),
    )

    // Sample Audit Logs
    _auditLogs.value = listOf(
      AuditLogItem(
        id = "log-1",
        timestamp = "2024-03-06 10:25",
        actorId = ownerUser.id,
        actorName = ownerUser.name,
        actorRole = Role.OWNER,
        action = "VERIFY_PAYMENT",
        details = "Verified 3,800 BDT (bKash TRX9876543210) for DEAL #0001",
      ),
      AuditLogItem(
        id = "log-2",
        timestamp = "2024-03-05 13:00",
        actorId = ownerUser.id,
        actorName = ownerUser.name,
        actorRole = Role.OWNER,
        action = "APPROVE_POST",
        details = "Accepted Marketplace Post #post-1 by Mahmud Hasan",
      ),
    )
  }

  // Auth Operations
  fun login(username: String, pass: String): Result<User> {
    val user = _users.value.find { it.username.equals(username.trim(), ignoreCase = true) }
    return if (user != null) {
      if (!user.isActive) {
        Result.failure(Exception("Account is inactive. Contact Admin."))
      } else {
        _currentUser.value = user
        logAction(user, "LOGIN", "Logged into system")
        Result.success(user)
      }
    } else {
      Result.failure(Exception("Invalid username or password."))
    }
  }

  fun register(name: String, username: String, pass: String, phone: String, selectedRole: Role): Result<User> {
    // CRITICAL SECURITY: Public registration must NEVER allow ADMIN or MODERATOR
    if (selectedRole == Role.ADMIN || selectedRole == Role.MODERATOR || selectedRole == Role.OWNER) {
      return Result.failure(Exception("Security Violation: Public registration for ADMIN or MODERATOR is strictly forbidden."))
    }
    if (_users.value.any { it.username.equals(username.trim(), ignoreCase = true) }) {
      return Result.failure(Exception("Username already taken. Please choose another."))
    }
    if (name.isBlank() || username.isBlank() || pass.length < 6) {
      return Result.failure(Exception("Please provide valid information (password min 6 chars)."))
    }

    // Password hashed
    val hashedPassword = hashPassword(pass)
    val newUser = User(
      id = "user-${UUID.randomUUID().toString().take(8)}",
      name = name.trim(),
      username = username.trim(),
      role = selectedRole,
      isActive = true,
      avatarUrl = "",
      joinedDate = getCurrentTimeString().take(10),
      phone = phone.trim(),
    )

    _users.value = _users.value + newUser
    _currentUser.value = newUser
    logAction(newUser, "REGISTER", "Registered as ${selectedRole.name} with hash ${hashedPassword.take(8)}...")
    return Result.success(newUser)
  }

  fun switchUserForDemo(targetRole: Role) {
    val target = when (targetRole) {
      Role.OWNER, Role.ADMIN -> ownerUser
      Role.MODERATOR -> _users.value.find { it.role == Role.MODERATOR } ?: modUser1
      Role.SELLER -> _users.value.find { it.role == Role.SELLER } ?: sellerUser
      Role.CUSTOMER -> _users.value.find { it.role == Role.CUSTOMER } ?: customerUser
    }
    _currentUser.value = target
  }

  fun logout() {
    _currentUser.value = null
  }

  // Moderator Management (Only Owner/Admin can manage, Max 3 Active Moderators)
  fun appointModerator(adminUser: User, targetMemberId: String): Result<String> {
    if (adminUser.role != Role.ADMIN && adminUser.role != Role.OWNER) {
      return Result.failure(Exception("Only Admin can manage Moderators."))
    }
    val activeMods = _users.value.count { it.role == Role.MODERATOR && it.isActive }
    if (activeMods >= 3) {
      return Result.failure(Exception("Maximum 3 active moderators allowed."))
    }
    val target = _users.value.find { it.id == targetMemberId }
      ?: return Result.failure(Exception("Member not found."))
    if (target.isOwner || target.role == Role.OWNER) {
      return Result.failure(Exception("Cannot change Owner identity."))
    }

    _users.value = _users.value.map {
      if (it.id == targetMemberId) it.copy(role = Role.MODERATOR, isActive = true) else it
    }
    logAction(adminUser, "APPOINT_MODERATOR", "Appointed ${target.name} as Moderator")
    return Result.success("Moderator appointed successfully.")
  }

  fun toggleModeratorActive(adminUser: User, modUserId: String, active: Boolean): Result<String> {
    if (adminUser.role != Role.ADMIN && adminUser.role != Role.OWNER) {
      return Result.failure(Exception("Only Admin can manage Moderators."))
    }
    val target = _users.value.find { it.id == modUserId }
      ?: return Result.failure(Exception("Moderator not found."))
    if (target.isOwner) {
      return Result.failure(Exception("Cannot modify Owner status."))
    }
    if (active) {
      val activeMods = _users.value.count { it.role == Role.MODERATOR && it.isActive && it.id != modUserId }
      if (activeMods >= 3) {
        return Result.failure(Exception("Maximum 3 active moderators allowed."))
      }
    }
    _users.value = _users.value.map {
      if (it.id == modUserId) it.copy(isActive = active) else it
    }
    val action = if (active) "ACTIVATE_MODERATOR" else "DEACTIVATE_MODERATOR"
    logAction(adminUser, action, "${action} for ${target.name}")
    return Result.success("Moderator status updated.")
  }

  fun removeModerator(adminUser: User, modUserId: String): Result<String> {
    if (adminUser.role != Role.ADMIN && adminUser.role != Role.OWNER) {
      return Result.failure(Exception("Only Admin can remove Moderators."))
    }
    val target = _users.value.find { it.id == modUserId }
      ?: return Result.failure(Exception("User not found."))
    if (target.isOwner) {
      return Result.failure(Exception("Cannot modify Owner."))
    }
    _users.value = _users.value.map {
      if (it.id == modUserId) it.copy(role = Role.CUSTOMER) else it
    }
    logAction(adminUser, "REMOVE_MODERATOR", "Demoted ${target.name} to Customer")
    return Result.success("Moderator removed.")
  }

  // Create Deal from Slot or Custom
  fun createDeal(
    openerUser: User,
    slot: DealSlot? = null,
    customAmount: Int? = null,
    dealInfo: String = "",
  ): Result<Deal> {
    val amount = customAmount ?: (slot?.minAmount ?: 1000)
    val adminFee = if (slot != null) {
      slot.adminFee
    } else {
      // Calculate 10% fee with minimum 150
      maxOf(150, (amount * 0.10).toInt())
    }

    val dNum = "DEAL #%04d".format(dealCounter++)
    val isCustomerOpener = openerUser.role == Role.CUSTOMER

    val customer = if (isCustomerOpener) openerUser else customerUser
    val seller = if (!isCustomerOpener) openerUser else sellerUser

    val slotName = slot?.nameEn ?: "Custom Deal (${amount} BDT)"

    val now = getCurrentTimeString()
    val newDeal = Deal(
      id = "deal-${UUID.randomUUID().toString().take(8)}",
      dealNumber = dNum,
      customerId = customer.id,
      customerName = customer.name,
      sellerId = seller.id,
      sellerName = seller.name,
      amount = amount,
      adminFee = adminFee,
      totalPayment = amount + adminFee,
      slotTitle = slotName,
      status = DealStatus.CREATED,
      createdAt = now,
      updatedAt = now,
      timelineEvents = listOf(
        TimelineEvent("ক্রেতা/বিক্রেতা ডিল খুলেছেন ($dNum)", "Deal opened ($dNum)", now.takeLast(5), true, isCurrent = true),
        TimelineEvent("পেমেন্ট সম্পন্ন করুন", "Make payment", "-", false),
        TimelineEvent("পেমেন্ট ভেরিফিকেশন", "Payment Verification", "-", false),
        TimelineEvent("বিক্রেতা ID ও পাসওয়ার্ড জমা", "Seller ID & Password Submission", "-", false),
        TimelineEvent("ID ভেরিফিকেশন", "ID Verification", "-", false),
        TimelineEvent("ক্রেতার নিকট ক্রেডেনশিয়াল রিলিজ", "ID & Password Released", "-", false),
        TimelineEvent("ক্রেতা ALL DONE নিশ্চিতকরণ", "Customer ALL DONE", "-", false),
        TimelineEvent("ডিল সম্পন্ন", "Deal Completed", "-", false),
        TimelineEvent("বিক্রেতা সেটেলমেন্ট প্রস্তুত", "Settlement Ready", "-", false),
      ),
      messages = listOf(
        DealMessage(
          id = "m-${UUID.randomUUID().toString().take(6)}",
          dealId = "",
          senderId = "system",
          senderName = "System",
          senderRole = Role.ADMIN,
          text = "ডিল $dNum খোলা হয়েছে। মোট পেমেন্ট: ${amount + adminFee} টাকা। অনুগ্রহ করে অ্যাডমিন নম্বরে পেমেন্ট করুন।",
          timestamp = now.takeLast(5),
        )
      )
    )

    _deals.value = listOf(newDeal) + _deals.value

    sendNotification(
      recipientId = if (isCustomerOpener) seller.id else customer.id,
      type = NotificationType.NEW_DEAL,
      titleBn = "নতুন ডিল খোলা হয়েছে ($dNum)",
      titleEn = "New Deal Opened ($dNum)",
      messageBn = "${openerUser.name} নতুন ডিল খুলেছেন। পরিমাণ: $amount টাকা।",
      messageEn = "${openerUser.name} opened a new deal for $amount BDT.",
      dealId = newDeal.id,
    )

    logAction(openerUser, "CREATE_DEAL", "Created $dNum with amount $amount BDT")
    return Result.success(newDeal)
  }

  // Payment Submission
  fun submitPayment(dealId: String, method: String, senderNumber: String, txId: String): Result<Deal> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    if (senderNumber.isBlank() || txId.isBlank()) {
      return Result.failure(Exception("অনুগ্রহ করে প্রেরক নম্বর এবং Transaction ID দিন।"))
    }

    val now = getCurrentTimeString()
    val updatedTimeline = deal.timelineEvents.map {
      when {
        it.titleEn.contains("payment", ignoreCase = true) -> it.copy(
          titleBn = "পেমেন্ট জমা দেওয়া হয়েছে ($method)",
          titleEn = "Payment submitted ($method)",
          timestamp = now.takeLast(5),
          isCompleted = true,
          isCurrent = false
        )
        it.titleEn.contains("verification", ignoreCase = true) && !it.titleEn.contains("id", ignoreCase = true) -> it.copy(
          titleBn = "পেমেন্ট ভেরিফিকেশন পেন্ডিং...",
          titleEn = "Payment Pending Verification...",
          isCurrent = true
        )
        else -> it
      }
    }

    val updatedDeal = deal.copy(
      status = DealStatus.PAYMENT_PENDING_VERIFICATION,
      paymentMethod = method,
      senderNumber = senderNumber.trim(),
      transactionId = txId.trim(),
      paymentErrorReason = "",
      updatedAt = now,
      timelineEvents = updatedTimeline,
    )

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    sendNotification(
      recipientId = ownerUser.id,
      type = NotificationType.PAYMENT_SUBMITTED,
      titleBn = "পেমেন্ট যাচাইয়ের অপেক্ষায় (${deal.dealNumber})",
      titleEn = "Payment Pending Verification (${deal.dealNumber})",
      messageBn = "TxID: $txId (${deal.totalPayment} টাকা, $method)",
      messageEn = "TxID: $txId (${deal.totalPayment} BDT, $method)",
      dealId = dealId,
    )

    return Result.success(updatedDeal)
  }

  // Admin verifies payment
  fun verifyPayment(actor: User, dealId: String, isApproved: Boolean, errorReason: String = ""): Result<Deal> {
    if (actor.role != Role.ADMIN && actor.role != Role.OWNER && actor.role != Role.MODERATOR) {
      return Result.failure(Exception("Unauthorized. Only Admin or authorized Moderator can verify payment."))
    }
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    val now = getCurrentTimeString()
    val updatedDeal = if (isApproved) {
      val updatedTimeline = deal.timelineEvents.map {
        if (it.titleEn.contains("Payment", ignoreCase = true) && !it.titleEn.contains("make", ignoreCase = true)) {
          it.copy(titleBn = "পেমেন্ট ভেরিফাইড ✅", titleEn = "Payment Successful ✅", timestamp = now.takeLast(5), isCompleted = true, isCurrent = false)
        } else if (it.titleEn.contains("Seller ID", ignoreCase = true)) {
          it.copy(isCurrent = true)
        } else it
      }
      deal.copy(
        status = DealStatus.PAYMENT_VERIFIED,
        paymentErrorReason = "",
        updatedAt = now,
        timelineEvents = updatedTimeline
      )
    } else {
      deal.copy(
        status = DealStatus.PAYMENT_FAILED,
        paymentErrorReason = if (errorReason.isNotBlank()) errorReason else "Transaction ভুল হয়েছে। আবার চেষ্টা করুন।",
        updatedAt = now,
      )
    }

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    if (isApproved) {
      sendNotification(
        recipientId = deal.customerId,
        type = NotificationType.PAYMENT_SUCCESSFUL,
        titleBn = "পেমেন্ট সফল ✅ (${deal.dealNumber})",
        titleEn = "Payment Successful ✅ (${deal.dealNumber})",
        messageBn = "আপনার পেমেন্ট যাচাই করা হয়েছে। বিক্রেতা এখন ID সাবমিট করবেন।",
        messageEn = "Your payment was verified. Seller will now submit account credentials.",
        dealId = dealId,
      )
      sendNotification(
        recipientId = deal.sellerId,
        type = NotificationType.PAYMENT_SUCCESSFUL,
        titleBn = "পেমেন্ট সম্পন্ন হয়েছে! ID সাবমিট করুন (${deal.dealNumber})",
        titleEn = "Payment Verified! Submit ID (${deal.dealNumber})",
        messageBn = "ক্রেতার পেমেন্ট নিরাপদে অ্যাডমিনের কাছে জমা হয়েছে। এখন ID ও পাসওয়ার্ড দিন।",
        messageEn = "Buyer payment verified. Please submit account credentials now.",
        dealId = dealId,
      )
    } else {
      sendNotification(
        recipientId = deal.customerId,
        type = NotificationType.TRANSACTION_ERROR,
        titleBn = "Transaction ভুল হয়েছে (${deal.dealNumber})",
        titleEn = "Transaction Error (${deal.dealNumber})",
        messageBn = "Transaction ভুল হয়েছে। আবার চেষ্টা করুন।",
        messageEn = "Transaction mismatch or incorrect. Try again.",
        dealId = dealId,
      )
    }

    logAction(actor, if (isApproved) "APPROVE_PAYMENT" else "REJECT_PAYMENT", "Payment for ${deal.dealNumber} - ${if (isApproved) "Approved" else "Rejected"}")
    return Result.success(updatedDeal)
  }

  fun retryPayment(dealId: String): Result<Deal> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    val updated = deal.copy(
      status = DealStatus.CREATED,
      paymentErrorReason = "",
      senderNumber = "",
      transactionId = "",
    )
    _deals.value = _deals.value.map { if (it.id == dealId) updated else it }
    return Result.success(updated)
  }

  // Seller submits ID & Password
  fun submitSellerCredentials(seller: User, dealId: String, uid: String, loginType: String, secretToken: String): Result<Deal> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    if (deal.status != DealStatus.PAYMENT_VERIFIED) {
      return Result.failure(Exception("Payment must be verified before submitting ID & Password."))
    }
    if (uid.isBlank() || secretToken.isBlank()) {
      return Result.failure(Exception("Please provide Free Fire UID and credentials."))
    }

    val now = getCurrentTimeString()
    val updatedTimeline = deal.timelineEvents.map {
      if (it.titleEn.contains("Seller ID", ignoreCase = true)) {
        it.copy(titleBn = "বিক্রেতা ID ও পাসওয়ার্ড জমা দিয়েছেন ✅", titleEn = "ID & Password Submitted ✅", timestamp = now.takeLast(5), isCompleted = true, isCurrent = false)
      } else if (it.titleEn.contains("ID Verification", ignoreCase = true)) {
        it.copy(titleBn = "অপেক্ষা করুন, ID যাচাই করা হচ্ছে…", titleEn = "ID Verification in Progress...", isCurrent = true)
      } else it
    }

    val updatedDeal = deal.copy(
      status = DealStatus.ID_SUBMITTED_PENDING,
      credentialUid = uid.trim(),
      credentialType = loginType,
      credentialSecretToken = secretToken.trim(),
      updatedAt = now,
      timelineEvents = updatedTimeline,
    )

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    sendNotification(
      recipientId = ownerUser.id,
      type = NotificationType.ID_SUBMITTED,
      titleBn = "ID ও পাসওয়ার্ড জমা পড়েছে (${deal.dealNumber})",
      titleEn = "ID & Password Submitted (${deal.dealNumber})",
      messageBn = "বিক্রেতা অ্যাকাউন্ট ক্রেডেনশিয়াল সাবমিট করেছেন। যাচাই করুন।",
      messageEn = "Seller submitted credentials. Verification required.",
      dealId = dealId,
    )

    logAction(seller, "SUBMIT_CREDENTIALS", "Submitted credentials for ${deal.dealNumber}")
    return Result.success(updatedDeal)
  }

  // Admin/System verifies ID and releases credentials to customer
  fun verifyAndReleaseCredentials(actor: User, dealId: String): Result<Deal> {
    if (actor.role != Role.ADMIN && actor.role != Role.OWNER && actor.role != Role.MODERATOR) {
      return Result.failure(Exception("Only Admin or authorized Moderator can verify credentials."))
    }
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    val now = getCurrentTimeString()
    val updatedTimeline = deal.timelineEvents.map {
      when {
        it.titleEn.contains("ID Verification", ignoreCase = true) -> it.copy(
          titleBn = "ID ভেরিফিকেশন সফল ✅",
          titleEn = "ID Verification Successful ✅",
          timestamp = now.takeLast(5),
          isCompleted = true,
          isCurrent = false
        )
        it.titleEn.contains("Released", ignoreCase = true) -> it.copy(
          titleBn = "ID ও পাসওয়ার্ড রিলিজ হয়েছে ✅",
          titleEn = "ID & Password Released ✅",
          timestamp = now.takeLast(5),
          isCompleted = true,
          isCurrent = false
        )
        it.titleEn.contains("ALL DONE", ignoreCase = true) -> it.copy(
          titleBn = "ক্রেতা অ্যাকাউন্ট চেক করে ALL DONE দিবেন",
          titleEn = "Customer check & ALL DONE pending",
          isCurrent = true
        )
        else -> it
      }
    }

    val updatedDeal = deal.copy(
      status = DealStatus.ID_RELEASED,
      updatedAt = now,
      timelineEvents = updatedTimeline,
    )

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    sendNotification(
      recipientId = deal.customerId,
      type = NotificationType.ID_RELEASED,
      titleBn = "ID ও পাসওয়ার্ড রিলিজ হয়েছে! (${deal.dealNumber})",
      titleEn = "ID & Password Released! (${deal.dealNumber})",
      messageBn = "অ্যাকাউন্ট ভেরিফিকেশন সফল হয়েছে। ডিল বোর্ডে লগইন তথ্য চেক করুন এবং সব ঠিক থাকলে ALL DONE চাপুন।",
      messageEn = "Credentials released. Verify your account and press ALL DONE.",
      dealId = dealId,
    )

    logAction(actor, "RELEASE_CREDENTIALS", "Verified and released credentials for ${deal.dealNumber}")
    return Result.success(updatedDeal)
  }

  // Customer presses ALL DONE
  fun customerConfirmAllDone(customer: User, dealId: String): Result<Deal> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    if (deal.status != DealStatus.ID_RELEASED) {
      return Result.failure(Exception("Credentials must be released and verified before ALL DONE."))
    }

    val now = getCurrentTimeString()
    val updatedTimeline = deal.timelineEvents.map {
      when {
        it.titleEn.contains("ALL DONE", ignoreCase = true) -> it.copy(
          titleBn = "ক্রেতা ALL DONE নিশ্চিত করেছেন ✅",
          titleEn = "Customer All Done ✅",
          timestamp = now.takeLast(5),
          isCompleted = true,
          isCurrent = false
        )
        it.titleEn.contains("Deal Completed", ignoreCase = true) -> it.copy(
          titleBn = "ডিল সফলভাবে সম্পন্ন হয়েছে ✅",
          titleEn = "Deal Completed ✅",
          timestamp = now.takeLast(5),
          isCompleted = true,
          isCurrent = false
        )
        it.titleEn.contains("Settlement", ignoreCase = true) -> it.copy(
          titleBn = "বিক্রেতা সেটেলমেন্ট প্রস্তুত ✅",
          titleEn = "Settlement Ready / Eligible ✅",
          isCompleted = true,
          isCurrent = true
        )
        else -> it
      }
    }

    val updatedDeal = deal.copy(
      status = DealStatus.DEAL_COMPLETED,
      updatedAt = now,
      timelineEvents = updatedTimeline,
    )

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    sendNotification(
      recipientId = deal.sellerId,
      type = NotificationType.CUSTOMER_ALL_DONE,
      titleBn = "ক্রেতা ALL DONE নিশ্চিত করেছেন! (${deal.dealNumber})",
      titleEn = "Customer Confirmed ALL DONE! (${deal.dealNumber})",
      messageBn = "ডিল সম্পন্ন হয়েছে। আপনার ${deal.amount} টাকা সেটেলমেন্টের জন্য প্রস্তুত।",
      messageEn = "Deal Completed! Your settlement of ${deal.amount} BDT is ready.",
      dealId = dealId,
    )

    logAction(customer, "ALL_DONE", "Customer marked ${deal.dealNumber} as ALL DONE")
    return Result.success(updatedDeal)
  }

  // Seller submits settlement details
  fun submitSellerSettlementInfo(seller: User, dealId: String, method: String, number: String): Result<Deal> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    val updated = deal.copy(
      sellerSettlementMethod = method,
      sellerSettlementNumber = number.trim(),
      updatedAt = getCurrentTimeString(),
    )
    _deals.value = _deals.value.map { if (it.id == dealId) updated else it }
    return Result.success(updated)
  }

  // Admin releases settlement to Seller
  fun releaseSettlement(actor: User, dealId: String): Result<Deal> {
    if (actor.role != Role.ADMIN && actor.role != Role.OWNER) {
      return Result.failure(Exception("Only Admin can release settlement."))
    }
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    val updated = deal.copy(
      status = DealStatus.SETTLEMENT_COMPLETED,
      updatedAt = getCurrentTimeString(),
    )
    _deals.value = _deals.value.map { if (it.id == dealId) updated else it }

    sendNotification(
      recipientId = deal.sellerId,
      type = NotificationType.SETTLEMENT_READY,
      titleBn = "সেটেলমেন্ট সম্পন্ন হয়েছে (${deal.dealNumber})",
      titleEn = "Settlement Released (${deal.dealNumber})",
      messageBn = "${deal.amount} টাকা ${deal.sellerSettlementMethod} নম্বরে সফলভাবে পাঠানো হয়েছে।",
      messageEn = "Settlement of ${deal.amount} BDT sent to ${deal.sellerSettlementMethod}.",
      dealId = dealId,
    )

    logAction(actor, "RELEASE_SETTLEMENT", "Released ${deal.amount} BDT to seller for ${deal.dealNumber}")
    return Result.success(updated)
  }

  // Messaging in Deal
  fun sendDealMessage(dealId: String, sender: User, text: String): Result<DealMessage> {
    val deal = _deals.value.find { it.id == dealId }
      ?: return Result.failure(Exception("Deal not found."))

    if (text.isBlank()) return Result.failure(Exception("Message cannot be empty."))

    val msg = DealMessage(
      id = "m-${UUID.randomUUID().toString().take(6)}",
      dealId = dealId,
      senderId = sender.id,
      senderName = sender.name,
      senderRole = sender.role,
      text = text.trim(),
      timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    )

    val updatedDeal = deal.copy(
      messages = deal.messages + msg,
      updatedAt = getCurrentTimeString(),
    )

    _deals.value = _deals.value.map { if (it.id == dealId) updatedDeal else it }

    val recipientId = if (sender.id == deal.customerId) deal.sellerId else deal.customerId
    sendNotification(
      recipientId = recipientId,
      type = NotificationType.NEW_MESSAGE,
      titleBn = "নতুন মেসেজ (${deal.dealNumber})",
      titleEn = "New Message (${deal.dealNumber})",
      messageBn = "${sender.name}: ${text.take(40)}...",
      messageEn = "${sender.name}: ${text.take(40)}...",
      dealId = dealId,
    )

    return Result.success(msg)
  }

  // Marketplace Operations
  fun createMarketplacePost(
    seller: User,
    freeFireId: String,
    price: Int,
    caption: String,
    description: String,
    photos: List<String>,
    videoUrl: String? = null,
  ): Result<MarketplacePost> {
    if (freeFireId.isBlank() || price <= 0 || caption.isBlank()) {
      return Result.failure(Exception("Please fill required fields (ID, Price, Caption)."))
    }
    if (photos.size > 6) {
      return Result.failure(Exception("Maximum 6 photos allowed per post."))
    }

    val post = MarketplacePost(
      id = "post-${UUID.randomUUID().toString().take(8)}",
      sellerId = seller.id,
      sellerName = seller.name,
      sellerRole = seller.role,
      freeFireId = freeFireId.trim(),
      price = price,
      photos = photos,
      videoUrl = videoUrl,
      caption = caption.trim(),
      description = description.trim(),
      status = PostStatus.PENDING_REVIEW, // All posts start as PENDING REVIEW
      createdAt = getCurrentTimeString(),
    )

    _marketplacePosts.value = listOf(post) + _marketplacePosts.value
    logAction(seller, "CREATE_POST", "Created marketplace post: ${post.caption}")
    return Result.success(post)
  }

  fun reviewMarketplacePost(actor: User, postId: String, accept: Boolean, reason: String = ""): Result<String> {
    if (actor.role != Role.ADMIN && actor.role != Role.OWNER && actor.role != Role.MODERATOR) {
      return Result.failure(Exception("Only Admin or Moderator can review posts."))
    }
    val post = _marketplacePosts.value.find { it.id == postId }
      ?: return Result.failure(Exception("Post not found."))

    // Seller cannot approve their own post
    if (post.sellerId == actor.id) {
      return Result.failure(Exception("Seller cannot approve their own post."))
    }

    val newStatus = if (accept) PostStatus.ACCEPTED else PostStatus.REJECTED
    _marketplacePosts.value = _marketplacePosts.value.map {
      if (it.id == postId) it.copy(status = newStatus, rejectionReason = reason) else it
    }

    val action = if (accept) "ACCEPT_POST" else "REJECT_POST"
    logAction(actor, action, "$action on Post $postId. Reason: $reason")
    return Result.success("Post status updated to ${newStatus.name}")
  }

  fun addComment(user: User, postId: String, text: String): Result<CommentItem> {
    if (text.isBlank()) return Result.failure(Exception("Comment cannot be empty."))
    val comment = CommentItem(
      id = "c-${UUID.randomUUID().toString().take(6)}",
      postId = postId,
      userId = user.id,
      userName = user.name,
      userRole = user.role,
      text = text.trim(),
      createdAt = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    )
    _marketplacePosts.value = _marketplacePosts.value.map { post ->
      if (post.id == postId) post.copy(comments = post.comments + comment) else post
    }
    return Result.success(comment)
  }

  fun deleteComment(actor: User, postId: String, commentId: String): Result<String> {
    val post = _marketplacePosts.value.find { it.id == postId }
      ?: return Result.failure(Exception("Post not found."))
    val comment = post.comments.find { it.id == commentId }
      ?: return Result.failure(Exception("Comment not found."))

    // Allowed if actor is author or admin or moderator
    if (comment.userId != actor.id && actor.role != Role.ADMIN && actor.role != Role.OWNER && actor.role != Role.MODERATOR) {
      return Result.failure(Exception("Unauthorized to delete comment."))
    }

    _marketplacePosts.value = _marketplacePosts.value.map { p ->
      if (p.id == postId) p.copy(comments = p.comments.filter { it.id != commentId }) else p
    }
    return Result.success("Comment deleted.")
  }

  // Notifications
  fun sendNotification(
    recipientId: String,
    type: NotificationType,
    titleBn: String,
    titleEn: String,
    messageBn: String,
    messageEn: String,
    dealId: String? = null,
  ) {
    val notif = NotificationItem(
      id = "notif-${UUID.randomUUID().toString().take(8)}",
      recipientId = recipientId,
      type = type,
      titleBn = titleBn,
      titleEn = titleEn,
      messageBn = messageBn,
      messageEn = messageEn,
      timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
      dealId = dealId,
    )
    _notifications.value = listOf(notif) + _notifications.value
  }

  fun markNotificationAsRead(id: String) {
    _notifications.value = _notifications.value.map {
      if (it.id == id) it.copy(isRead = true) else it
    }
  }

  fun markAllNotificationsAsRead(userId: String) {
    _notifications.value = _notifications.value.map {
      if (it.recipientId == userId) it.copy(isRead = true) else it
    }
  }

  fun toggleNotificationSound(enabled: Boolean) {
    _adminConfig.value = _adminConfig.value.copy(notificationSoundEnabled = enabled)
  }

  // Admin Customization
  fun updatePaymentNumbers(actor: User, bKash: String, nagad: String): Result<String> {
    if (actor.role != Role.ADMIN && actor.role != Role.OWNER) {
      return Result.failure(Exception("Unauthorized. Only Admin/Owner can update payment numbers."))
    }
    _adminConfig.value = _adminConfig.value.copy(
      bKashNumber = bKash.trim(),
      nagadNumber = nagad.trim(),
    )
    logAction(actor, "UPDATE_PAYMENT_NUMBERS", "Updated bKash to $bKash and Nagad to $nagad")
    return Result.success("Payment numbers updated.")
  }

  // Audit Logging
  fun logAction(actor: User, action: String, details: String) {
    val item = AuditLogItem(
      id = "log-${UUID.randomUUID().toString().take(6)}",
      timestamp = getCurrentTimeString(),
      actorId = actor.id,
      actorName = actor.name,
      actorRole = actor.role,
      action = action,
      details = details,
    )
    _auditLogs.value = listOf(item) + _auditLogs.value
  }

  // Support
  fun submitSupportTicket(user: User, category: String, desc: String): Result<SupportTicket> {
    if (desc.isBlank()) return Result.failure(Exception("Please explain your issue."))
    val ticket = SupportTicket(
      id = "ticket-${UUID.randomUUID().toString().take(6)}",
      userId = user.id,
      userName = user.name,
      category = category,
      description = desc.trim(),
      createdAt = getCurrentTimeString(),
    )
    _supportTickets.value = listOf(ticket) + _supportTickets.value
    return Result.success(ticket)
  }

  // Manager Chat System & Deal Investigation
  fun queryManagerChat(caller: User, query: String, lang: AppLanguage): String {
    val q = query.trim().lowercase()

    // Check if it is a deal investigation request
    val dealNumRegex = Regex("deal\\s*#?(\\d+)", RegexOption.IGNORE_CASE)
    val match = dealNumRegex.find(query)
    if (match != null) {
      val numStr = match.groupValues[1]
      val formattedNum = "DEAL #%04d".format(numStr.toIntOrNull() ?: 1)
      val deal = _deals.value.find { it.dealNumber.equals(formattedNum, ignoreCase = true) }

      // CRITICAL SECURITY RULE: Only authorized Admin/Owner can ask Manager Chat for detailed investigation of a specific Deal!
      if (caller.role != Role.ADMIN && caller.role != Role.OWNER) {
        return if (lang == AppLanguage.BANGLA) {
          "🔒 দুঃখিত, নির্দিষ্ট ডিলের অভ্যন্তরীণ তদন্ত রিপোর্ট শুধুমাত্র অ্যাডমিন/ওনার দেখতে পারেন। আপনি আপনার নিজের ডিলের অবস্থা ডিল বোর্ডে সরাসরি দেখতে পারেন।"
        } else {
          "🔒 Unauthorized: Detailed deal investigation reports are strictly restricted to the authorized Admin/Owner. You can view your deal status on your Deal Board."
        }
      }

      if (deal == null) {
        return if (lang == AppLanguage.BANGLA) {
          "❌ $formattedNum এর কোনো তথ্য সিস্টেমে পাওয়া যাচ্ছে না।"
        } else {
          "❌ $formattedNum was not found in the system."
        }
      }

      // Structured investigation report strictly from actual system data
      val statusBn = when (deal.status) {
        DealStatus.CREATED -> "ডিল খোলা হয়েছে (পেমেন্ট বকেয়া)"
        DealStatus.PAYMENT_PENDING_VERIFICATION -> "পেমেন্ট ভেরিফিকেশন পেন্ডিং (TxID: ${deal.transactionId})"
        DealStatus.PAYMENT_FAILED -> "পেমেন্ট ব্যর্থ (${deal.paymentErrorReason})"
        DealStatus.PAYMENT_VERIFIED -> "পেমেন্ট ভেরিফাইড (আইডি সাবমিশনের অপেক্ষায়)"
        DealStatus.ID_SUBMITTED_PENDING -> "আইডি ও পাসওয়ার্ড জমা হয়েছে (যাচাই পেন্ডিং)"
        DealStatus.ID_VERIFICATION_STARTED -> "আইডি ভেরিফিকেশন চলমান"
        DealStatus.ID_VERIFICATION_SUCCESSFUL -> "আইডি ভেরিফিকেশন সফল"
        DealStatus.ID_RELEASED -> "আইডি ক্রেতার নিকট রিলিজ করা হয়েছে"
        DealStatus.CUSTOMER_ALL_DONE -> "ক্রেতা ALL DONE নিশ্চিত করেছেন"
        DealStatus.DEAL_COMPLETED -> "ডিল সম্পন্ন হয়েছে ✅"
        DealStatus.SETTLEMENT_READY -> "বিক্রেতা সেটেলমেন্ট প্রস্তুত"
        DealStatus.SETTLEMENT_COMPLETED -> "সেটেলমেন্ট সম্পন্ন হয়েছে ✅"
      }

      return if (lang == AppLanguage.BANGLA) {
        """
        📋 [অ্যাডমিন ইনভেস্টিগেশন রিপোর্ট: ${deal.dealNumber}]
        ------------------------------------------
        • ডিল নম্বর: ${deal.dealNumber}
        • ক্রেতা: ${deal.customerName}
        • বিক্রেতা: ${deal.sellerName}
        • পরিমাণ: ${deal.amount} টাকা (অ্যাডমিন ফি: ${deal.adminFee} টাকা, মোট: ${deal.totalPayment} টাকা)
        • পেমেন্ট স্ট্যাটাস: ${deal.paymentMethod} | নম্বর: ${deal.senderNumber.ifBlank { "N/A" }} | TxID: ${deal.transactionId.ifBlank { "N/A" }}
        • ক্রেডেনশিয়াল স্ট্যাটাস: ${if (deal.credentialUid.isNotBlank()) "সাবমিটেড (${deal.credentialType})" else "এখনও সাবমিট হয়নি"}
        • বর্তমান ডিল স্ট্যাটাস: $statusBn
        • মোট মেসেজ আদান-প্রদান: ${deal.messages.size} টি
        • সেটেলমেন্ট তথ্য: ${deal.sellerSettlementMethod} | ${if (deal.sellerSettlementNumber.isNotBlank()) "প্রাইভেট নম্বর সংরক্ষিত" else "এখনও দেয়নি"}
        • সাম্প্রতিক টাইমলাইন: ${deal.timelineEvents.lastOrNull { it.isCompleted }?.titleBn ?: "শুরু হয়েছে"}
        """.trimIndent()
      } else {
        """
        📋 [Admin Investigation Report: ${deal.dealNumber}]
        ------------------------------------------
        • Deal Number: ${deal.dealNumber}
        • Customer: ${deal.customerName}
        • Seller: ${deal.sellerName}
        • Amount: ${deal.amount} BDT (Admin Fee: ${deal.adminFee} BDT, Total: ${deal.totalPayment} BDT)
        • Payment Status: ${deal.paymentMethod} | Number: ${deal.senderNumber.ifBlank { "N/A" }} | TxID: ${deal.transactionId.ifBlank { "N/A" }}
        • Credential Status: ${if (deal.credentialUid.isNotBlank()) "Submitted (${deal.credentialType})" else "Not submitted yet"}
        • Current Status: ${deal.status.name}
        • Message Count: ${deal.messages.size}
        • Settlement: ${deal.sellerSettlementMethod} | ${if (deal.sellerSettlementNumber.isNotBlank()) "Private number recorded" else "Pending"}
        • Latest Event: ${deal.timelineEvents.lastOrNull { it.isCompleted }?.titleEn ?: "Initialized"}
        """.trimIndent()
      }
    }

    // General step-by-step assistance
    return when {
      q.contains("slot") || q.contains("স্লট") -> {
        if (lang == AppLanguage.BANGLA) {
          "আমাদের অ্যাপে ১ থেকে ১০ পর্যন্ত ডিল স্লট রয়েছে (১,০০০ টাকা থেকে ১০,৫০০ টাকা)। প্রতিটি স্লটে নির্ধারিত অ্যাডমিন ফি থাকে। হোমে স্লট নির্বাচন করে 'ডিল খুলুন' বাটনে ক্লিক করলেই স্বয়ংক্রিয়ভাবে সুরক্ষিত ডিল তৈরি হয়।"
        } else {
          "We offer 10 official Deal Slots (1,000 to 10,500 BDT) with predefined Admin fees. Tap 'Open Deal' on any slot to initiate an escrow transaction."
        }
      }
      q.contains("custom") || q.contains("কাস্টম") -> {
        if (lang == AppLanguage.BANGLA) {
          "যেকোনো পরিমাণের জন্য 'কাস্টম অ্যামাউন্ট ডিল' ব্যবহার করতে পারেন। হোম স্ক্রিন থেকে সরাসরি ক্রেতা বা বিক্রেতা যেকোনো সময় কাস্টম অ্যামাউন্ট ডিল খুলতে পারেন। অ্যাডমিন উপস্থিতি ছাড়াও তাৎক্ষণিক DEAL # তৈরি হয়।"
        } else {
          "Both Customers and Sellers can initiate Custom Amount Deals directly from Home without waiting for Admin. A unique Deal Number (e.g. DEAL #0003) is generated instantly."
        }
      }
      q.contains("payment") || q.contains("পেমেন্ট") || q.contains("টাকা") || q.contains("bkash") || q.contains("nagad") -> {
        if (lang == AppLanguage.BANGLA) {
          "পেমেন্ট পদ্ধতি: bKash ও Nagad। ডিল অ্যামাউন্ট + অ্যাডমিন ফি মিলিয়ে মোট টাকা অ্যাডমিন নম্বরে পাঠিয়ে আপনার প্রেরক নম্বর ও Transaction ID দিন। তথ্য ভুল হলে 'Transaction ভুল হয়েছে। আবার চেষ্টা করুন' বার্তা আসবে।"
        } else {
          "Supported payment methods: bKash and Nagad. Send Total Payment (Deal Amount + Admin Fee) and submit sender number & Transaction ID. Incorrect inputs will prompt a retry."
        }
      }
      q.contains("all done") || q.contains("ডান") -> {
        if (lang == AppLanguage.BANGLA) {
          "ক্রেতা আইডি ও পাসওয়ার্ড পাওয়ার পর নিজে লগইন করে সম্পূর্ণ চেক করবেন। সবকিছু সঠিক থাকলে তবেই 'ALL DONE' বাটনে চাপবেন। ALL DONE চাপার পরই ডিল সমাপ্ত হবে এবং বিক্রেতা সেটেলমেন্টের জন্য প্রস্তুত হবে।"
        } else {
          "After receiving credentials, Customer must verify account personally. Tap 'ALL DONE' only when fully satisfied. This marks the deal completed and unlocks seller settlement."
        }
      }
      q.contains("moderator") || q.contains("মডারেটর") -> {
        if (lang == AppLanguage.BANGLA) {
          "মডারেটর সর্বোচ্চ ৩ জন সক্রিয় থাকতে পারেন। শুধুমাত্র অ্যাডমিন মডারেটর নিয়োগ ও অপসারণ করতে পারেন। মডারেটর কোনো পেমেন্ট নম্বর বা অ্যাডমিন তৈরি করতে পারেন না।"
        } else {
          "There is a strict maximum of 3 active Moderators. Only Admin can manage Moderators. Moderators cannot change payment numbers or create Admins."
        }
      }
      q.contains("owner") || q.contains("admin") || q.contains("ওনার") || q.contains("মালিক") || q.contains("shihab") -> {
        if (lang == AppLanguage.BANGLA) {
          "প্ল্যাটফর্মের একমাত্র ওনার ও প্রধান অ্যাডমিন হলেন Shihab Talukder। কোনো সাধারণ ব্যবহারকারী বা মডারেটর ওনার পরিচয় বা নিরাপত্তা সেটিংস পরিবর্তন করতে পারে না।"
        } else {
          "The sole Owner and Admin of this platform is Shihab Talukder. No user or moderator can alter Owner identity or core security settings."
        }
      }
      else -> {
        if (lang == AppLanguage.BANGLA) {
          "আমি ম্যানেজার চ্যাট এআই অ্যাসিস্ট্যান্ট। ডিল স্লট, পেমেন্ট ভেরিফিকেশন, কাস্টম ডিল, আইডি সিকিউরিটি বা বিক্রেতা সেটেলমেন্ট সম্পর্কিত যেকোনো প্রশ্ন করতে পারেন। আরও সহায়তার জন্য সাপোর্ট সেকশনে যোগাযোগ করুন।"
        } else {
          "I am Manager Chat AI Assistant. You can ask about Deal Slots, Payment verification, Custom Deals, ID security, or Seller settlement. For further help, visit Support."
        }
      }
    }
  }
}
