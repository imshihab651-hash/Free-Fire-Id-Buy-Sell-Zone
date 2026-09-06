package com.example.ui.localization

enum class AppLanguage {
  BANGLA,
  ENGLISH;

  val displayName: String
    get() = when (this) {
      BANGLA -> "বাংলা"
      ENGLISH -> "English"
    }

  val toggleText: String
    get() = when (this) {
      BANGLA -> "English"
      ENGLISH -> "বাংলা"
    }
}

object Strings {
  fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "FREE FIRE ID BUY & SELL ZONE" else "FREE FIRE ID BUY & SELL ZONE"
  fun home(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম" else "Home"
  fun deals(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিলসমূহ" else "Deals"
  fun payment(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পেমেন্ট" else "Payment"
  fun alerts(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যালার্ট" else "Alerts"
  fun account(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাকাউন্ট" else "Account"

  fun marketplace(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কমিউনিটি মার্কেটপ্লেস" else "Community Marketplace"
  fun customDeal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কাস্টম অ্যামাউন্ট ডিল" else "Custom Amount Deal"
  fun managerChat(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "🤖 ম্যানেজার চ্যাট" else "🤖 Manager Chat"
  fun support(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সাপোর্ট" else "Support"
  fun notice(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোটিশ" else "Notice"
  fun profile(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রোফাইল" else "Profile"
  fun members(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মেম্বারস" else "Members"
  fun adminDashboard(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন ড্যাশবোর্ড" else "Admin Dashboard"
  fun moderatorPanel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মডারেটর প্যানেল" else "Moderator Panel"

  fun login(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগইন" else "Login"
  fun register(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রেজিস্ট্রেশন" else "Registration"
  fun roleCustomer(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্রেতা (Customer)" else "Customer"
  fun roleSeller(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিক্রেতা (Seller)" else "Seller"
  fun roleModerator(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মডারেটর" else "Moderator"
  fun roleAdmin(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন" else "Admin"
  fun roleOwner(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ওনার" else "Owner"

  fun dealSlotsTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিল স্লট তালিকা (১০টি স্লট)" else "Deal Slots (10 Slots)"
  fun openDeal(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিল খুলুন" else "Open Deal"
  fun adminFee(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডমিন ফি" else "Admin Fee"
  fun taka(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "টাকা" else "BDT"

  // Workflow steps
  fun step1CustomerOpens(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্রেতা ডিল খুলেছেন" else "Customer opened Deal"
  fun step2CustomerMakesPayment(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পেমেন্ট সম্পন্ন করুন" else "Make payment"
  fun step3PaymentVerified(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পেমেন্ট ভেরিফাইড ✅" else "Payment Successful ✅"
  fun step4SellerSubmitsCreds(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিক্রেতা ID ও পাসওয়ার্ড জমা দিবেন" else "Seller ID & Password submission"
  fun step5VerificationWait(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অপেক্ষা করুন, ID যাচাই করা হচ্ছে…" else "Please wait, ID is being verified…"
  fun step6SystemAdminVerifies(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সিস্টেম/অ্যাডমিন ভেরিফিকেশন চলমান" else "ID verification in progress"
  fun step7CredsReleased(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ID ও পাসওয়ার্ড রিলিজ হয়েছে ✅" else "ID & Password Released ✅"
  fun step8CustomerChecks(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্রেতা ব্যক্তিগতভাবে অ্যাকাউন্ট চেক করছেন" else "Customer checking account"
  fun step9AllDone(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ALL DONE" else "ALL DONE"
  fun step10DealCompleted(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিল সম্পন্ন হয়েছে ✅" else "Deal Completed ✅"
  fun step11SettlementEligible(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিক্রেতার সেটেলমেন্ট প্রস্তুত ✅" else "Settlement Ready / Eligible ✅"

  // Payment error
  fun paymentErrorMsg(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "Transaction ভুল হয়েছে। আবার চেষ্টা করুন।" else "Transaction mismatch or incorrect. Try again."
  fun retryBtn(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আবার চেষ্টা করুন" else "Try Again"
  fun paymentPending(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "Payment Pending Verification" else "Payment Pending Verification"

  // Demo banner
  fun demoModeTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "⚠️ ডেমো / টেস্ট মোড সক্রিয়" else "⚠️ DEMO / TEST MODE ACTIVE"
  fun demoModeDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রোডাকশন সিকিউরিটির জন্য ক্লাউড ব্যাকএন্ড প্রয়োজন। এখানে পূর্ণ নেটিভ আর্কিটেকচার ও রোল সুইচ করে টেস্ট করুন।" else "A real backend is required for production settlement & multi-user sync. Test the full native workflow and switch roles here."

  // Settlement
  fun settlementTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিক্রেতা সেটেলমেন্ট" else "Seller Settlement"
  fun settlementDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডিল সম্পূর্ণ হওয়ার পর সুরক্ষিতভাবে টাকা গ্রহণ করুন।" else "Receive payout securely after customer clicks ALL DONE."

  // Notifications
  fun notifications(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোটিফিকেশন" else "Notifications"
  fun markAllRead(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সব পড়া হয়েছে মার্ক করুন" else "Mark All Read"
  fun soundToggle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোটিফিকেশন সাউন্ড" else "Notification Sound"

  // Common UI Actions
  fun accept(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অনুমোদন করুন" else "Accept"
  fun reject(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রত্যাখ্যান" else "Reject"
  fun cancel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাতিল" else "Cancel"
  fun comments(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মন্তব্যসমূহ" else "Comments"
  fun rejectReasonTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রত্যাখ্যানের কারণ" else "Reason for Rejection"
  fun noticeBoard(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোটিশ বোর্ড" else "Notice Board"
  fun managerChatTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ম্যানেজার চ্যাট অ্যাসিস্ট্যান্ট" else "Manager Chat Assistant"
  fun logout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগআউট" else "Logout"
}
