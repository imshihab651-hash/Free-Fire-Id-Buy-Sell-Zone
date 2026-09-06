package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.DealStatus
import com.example.model.Role
import com.example.model.User
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun RoleBadge(
  role: Role,
  modifier: Modifier = Modifier,
  isOwnerBadge: Boolean = false,
) {
  val (bgColor, textColor, borderColor, icon) = when {
    isOwnerBadge || role == Role.OWNER -> Quad(
      FireOrange.copy(alpha = 0.2f),
      FireOrange,
      FireOrange.copy(alpha = 0.4f),
      Icons.Default.WorkspacePremium
    )
    role == Role.ADMIN -> Quad(
      FireOrangeLight.copy(alpha = 0.2f),
      FireOrangeLight,
      FireOrangeLight.copy(alpha = 0.4f),
      Icons.Default.Security
    )
    role == Role.MODERATOR -> Quad(
      GamingPurple.copy(alpha = 0.2f),
      GamingPurple,
      GamingPurple.copy(alpha = 0.4f),
      Icons.Default.Shield
    )
    role == Role.SELLER -> Quad(
      NeonCyan.copy(alpha = 0.2f),
      NeonCyan,
      NeonCyan.copy(alpha = 0.4f),
      Icons.Default.Storefront
    )
    else -> Quad(
      GamingGreen.copy(alpha = 0.2f),
      GamingGreen,
      GamingGreen.copy(alpha = 0.4f),
      Icons.Default.Person
    )
  }

  Surface(
    modifier = modifier.clip(RoundedCornerShape(8.dp)),
    color = bgColor,
    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
    shape = RoundedCornerShape(8.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = role.badgeText,
        tint = textColor,
        modifier = Modifier.size(11.dp)
      )
      Text(
        text = if (isOwnerBadge) "OWNER" else role.badgeText.uppercase(),
        color = textColor,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )
    }
  }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun DealStatusBadge(status: DealStatus, modifier: Modifier = Modifier) {
  val (text, color) = when (status) {
    DealStatus.CREATED -> "Deal Opened" to TextSecondary
    DealStatus.PAYMENT_PENDING_VERIFICATION -> "Payment Pending" to CyberGold
    DealStatus.PAYMENT_FAILED -> "Payment Failed" to GamingRed
    DealStatus.PAYMENT_VERIFIED -> "Payment Verified ✅" to GamingGreen
    DealStatus.ID_SUBMITTED_PENDING -> "ID Verification Pending" to CyberGold
    DealStatus.ID_VERIFICATION_STARTED -> "ID Verification Started" to NeonCyan
    DealStatus.ID_VERIFICATION_SUCCESSFUL -> "ID Verified ✅" to GamingGreen
    DealStatus.ID_RELEASED -> "ID Released ✅" to NeonCyan
    DealStatus.CUSTOMER_ALL_DONE -> "Customer All Done ✅" to GamingGreen
    DealStatus.DEAL_COMPLETED -> "Deal Completed ✅" to GamingGreen
    DealStatus.SETTLEMENT_READY -> "Settlement Ready ✅" to CyberGold
    DealStatus.SETTLEMENT_COMPLETED -> "Settlement Released ✅" to GamingGreen
  }

  Surface(
    modifier = modifier.clip(RoundedCornerShape(8.dp)),
    color = color.copy(alpha = 0.12f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f)),
    shape = RoundedCornerShape(8.dp)
  ) {
    Text(
      text = text,
      color = color,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
    )
  }
}

@Composable
fun DemoModeBanner(
  currentUser: User?,
  onSwitchRole: (Role) -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 4.dp),
    colors = CardDefaults.cardColors(containerColor = GamingSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Science,
            contentDescription = "Demo Mode",
            tint = FireOrangeLight,
            modifier = Modifier.size(18.dp)
          )
          Column {
            Text(
              text = Strings.demoModeTitle(currentLang),
              color = FireOrangeLight,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Role: ${currentUser?.role?.name ?: "Guest"} (${currentUser?.name ?: ""})",
              color = TextSecondary,
              fontSize = 11.sp
            )
          }
        }
        TextButton(
          onClick = { expanded = !expanded },
          modifier = Modifier.testTag("toggle_demo_roles_btn")
        ) {
          Text(
            text = if (expanded) "Hide Roles" else "Switch Role",
            color = FireOrange,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      AnimatedVisibility(visible = expanded) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
          Text(
            text = Strings.demoModeDesc(currentLang),
            color = TextMuted,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 6.dp)
          )
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Role.entries.forEach { role ->
              val isSelected = currentUser?.role == role
              OutlinedButton(
                onClick = { onSwitchRole(role) },
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (isSelected) FireOrange.copy(alpha = 0.2f) else Color.Transparent
                ),
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) FireOrange else GamingBorder
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .height(34.dp)
                  .testTag("switch_to_${role.name.lowercase()}")
              ) {
                Text(
                  text = when (role) {
                    Role.OWNER -> "Owner (Shihab)"
                    Role.ADMIN -> "Admin"
                    Role.MODERATOR -> "Moderator"
                    Role.SELLER -> "Seller"
                    Role.CUSTOMER -> "Customer"
                  },
                  fontSize = 11.sp,
                  color = if (isSelected) FireOrange else TextPrimary
                )
              }
            }
          }
        }
      }
    }
  }
}
