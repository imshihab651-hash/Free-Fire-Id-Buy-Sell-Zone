package com.example.model

enum class Role(val badgeText: String, val level: Int) {
  CUSTOMER("CUSTOMER", 1),
  SELLER("SELLER", 2),
  MODERATOR("MODERATOR", 3),
  ADMIN("ADMIN", 4),
  OWNER("OWNER", 5);

  val isPrivileged: Boolean get() = this == ADMIN || this == OWNER || this == MODERATOR
}

data class User(
  val id: String,
  val name: String,
  val username: String,
  val role: Role,
  val isActive: Boolean = true,
  val avatarUrl: String = "",
  val joinedDate: String = "2024-01-15",
  val phone: String = "017XXXXXXXX",
  val isOwner: Boolean = false,
)

enum class DealStatus(val code: String) {
  CREATED("CREATED"),
  PAYMENT_PENDING_VERIFICATION("PAYMENT_PENDING_VERIFICATION"),
  PAYMENT_FAILED("PAYMENT_FAILED"),
  PAYMENT_VERIFIED("PAYMENT_VERIFIED"),
  ID_SUBMITTED_PENDING("ID_SUBMITTED_PENDING"),
  ID_VERIFICATION_STARTED("ID_VERIFICATION_STARTED"),
  ID_VERIFICATION_SUCCESSFUL("ID_VERIFICATION_SUCCESSFUL"),
  ID_RELEASED("ID_RELEASED"),
  CUSTOMER_ALL_DONE("CUSTOMER_ALL_DONE"),
  DEAL_COMPLETED("DEAL_COMPLETED"),
  SETTLEMENT_READY("SETTLEMENT_READY"),
  SETTLEMENT_COMPLETED("SETTLEMENT_COMPLETED");

  val isTerminal: Boolean get() = this == DEAL_COMPLETED || this == SETTLEMENT_COMPLETED
}

data class DealSlot(
  val id: Int,
  val nameBn: String,
  val nameEn: String,
  val minAmount: Int,
  val maxAmount: Int,
  val adminFee: Int,
  val status: String = "AVAILABLE",
)

data class TimelineEvent(
  val titleBn: String,
  val titleEn: String,
  val timestamp: String,
  val isCompleted: Boolean,
  val isCurrent: Boolean = false,
)

data class DealMessage(
  val id: String,
  val dealId: String,
  val senderId: String,
  val senderName: String,
  val senderRole: Role,
  val text: String,
  val timestamp: String,
)

data class Deal(
  val id: String,
  val dealNumber: String, // e.g. "DEAL #0001"
  val customerId: String,
  val customerName: String,
  val sellerId: String,
  val sellerName: String,
  val amount: Int,
  val adminFee: Int,
  val totalPayment: Int = amount + adminFee,
  val slotTitle: String = "Custom Deal",
  val status: DealStatus = DealStatus.CREATED,
  val paymentMethod: String = "bKash", // "bKash" or "Nagad"
  val senderNumber: String = "",
  val transactionId: String = "",
  val paymentErrorReason: String = "",
  val credentialUid: String = "",
  val credentialType: String = "", // "Google", "Facebook", "Twitter/VK"
  val credentialSecretToken: String = "", // Protected - only released when verified to customer
  val sellerSettlementMethod: String = "bKash",
  val sellerSettlementNumber: String = "", // Private
  val createdAt: String = "",
  val updatedAt: String = "",
  val messages: List<DealMessage> = emptyList(),
  val timelineEvents: List<TimelineEvent> = emptyList(),
)

enum class NotificationType {
  NEW_MESSAGE,
  NEW_DEAL,
  PAYMENT_SUBMITTED,
  PAYMENT_SUCCESSFUL,
  PAYMENT_FAILED,
  TRANSACTION_ERROR,
  ID_SUBMITTED,
  ID_VERIFICATION_STARTED,
  ID_VERIFICATION_SUCCESSFUL,
  ID_RELEASED,
  CUSTOMER_ALL_DONE,
  DEAL_COMPLETED,
  SETTLEMENT_READY,
  ADMIN_ANNOUNCEMENT,
  NOTICE,
  MODERATOR_ROLE_UPDATE,
  SYSTEM_ALERT,
}

data class NotificationItem(
  val id: String,
  val recipientId: String,
  val type: NotificationType,
  val titleBn: String,
  val titleEn: String,
  val messageBn: String,
  val messageEn: String,
  val timestamp: String,
  val isRead: Boolean = false,
  val dealId: String? = null,
)

enum class PostStatus {
  PENDING_REVIEW,
  ACCEPTED,
  REJECTED,
  REMOVED,
}

data class CommentItem(
  val id: String,
  val postId: String,
  val userId: String,
  val userName: String,
  val userRole: Role,
  val text: String,
  val createdAt: String,
)

data class MarketplacePost(
  val id: String,
  val sellerId: String,
  val sellerName: String,
  val sellerRole: Role,
  val freeFireId: String,
  val price: Int,
  val photos: List<String>, // up to 6
  val videoUrl: String? = null,
  val caption: String,
  val description: String,
  val status: PostStatus = PostStatus.PENDING_REVIEW,
  val rejectionReason: String = "",
  val createdAt: String,
  val comments: List<CommentItem> = emptyList(),
)

data class NoticeItem(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val bodyBn: String,
  val bodyEn: String,
  val date: String,
  val isActive: Boolean = true,
  val isImportant: Boolean = false,
)

data class AuditLogItem(
  val id: String,
  val timestamp: String,
  val actorId: String,
  val actorName: String,
  val actorRole: Role,
  val action: String,
  val details: String,
)

data class CommunityInfo(
  val name: String = "Free Fire Bangladesh Official Exchange",
  val descriptionBn: String = "নিরাপদ Free Fire ID ক্রয়-বিক্রয়ের একমাত্র অফিসিয়াল প্ল্যাটফর্ম। ১০০% ট্রাস্টেড ও অ্যাডমিন এসক্রো ভেরিফাইড।",
  val descriptionEn: String = "The official secure platform for Free Fire ID exchange. 100% trusted and Admin Escrow verified.",
  val link: String = "https://facebook.com/groups/freefirebdexchange",
)

data class AdminConfig(
  val bKashNumber: String = "01711-223344 (Personal)",
  val nagadNumber: String = "01911-556677 (Merchant)",
  val notificationSoundEnabled: Boolean = true,
  val customAmountMin: Int = 500,
  val customAmountMax: Int = 100000,
  val defaultFeePercentage: Int = 10,
)

data class SupportTicket(
  val id: String,
  val userId: String,
  val userName: String,
  val category: String,
  val description: String,
  val status: String = "OPEN",
  val createdAt: String,
)
