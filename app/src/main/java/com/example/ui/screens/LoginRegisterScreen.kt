package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.model.Role
import com.example.ui.components.RoleBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun LoginRegisterScreen(
  onLoginSuccess: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val currentLang by AppRepository.currentLanguage.collectAsState()
  var isRegisterMode by remember { mutableStateOf(false) }

  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var name by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var selectedRole by remember { mutableStateOf(Role.CUSTOMER) }
  var showPassword by remember { mutableStateOf(false) }

  var errorMessage by remember { mutableStateOf("") }
  var successMessage by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(GamingDarkBg)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Bar Language toggle
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "🛡️ SECURE ESCROW",
        color = NeonCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      OutlinedButton(
        onClick = { AppRepository.toggleLanguage() },
        modifier = Modifier.height(36.dp).testTag("lang_toggle_btn"),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
      ) {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "English" else "বাংলা",
          color = CyberGold,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // App Header Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = GamingSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, FireOrange.copy(alpha = 0.3f))
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.LocalFireDepartment,
          contentDescription = "Logo",
          tint = FireOrange,
          modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "FREE FIRE ID BUY & SELL ZONE",
          color = TextPrimary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 0.5.sp
        )
        Text(
          text = if (currentLang == AppLanguage.BANGLA)
            "১০০% ট্রাস্টেড ও নিরাপদ গেমিং এসক্রো প্ল্যাটফর্ম"
          else
            "100% Trusted & Secure Gaming Escrow Platform",
          color = TextSecondary,
          fontSize = 12.sp,
          modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Owner Display Banner
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = GamingSurfaceVariant,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Shihab Talukder",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Authorized Platform Head",
                color = TextMuted,
                fontSize = 10.sp
              )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              RoleBadge(role = Role.ADMIN)
              RoleBadge(role = Role.OWNER, isOwnerBadge = true)
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Tab Row: Login / Registration
    TabRow(
      selectedTabIndex = if (isRegisterMode) 1 else 0,
      containerColor = GamingSurface,
      contentColor = FireOrange,
      divider = {},
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, GamingBorder, RoundedCornerShape(12.dp))
    ) {
      Tab(
        selected = !isRegisterMode,
        onClick = {
          isRegisterMode = false
          errorMessage = ""
          successMessage = ""
        },
        text = {
          Text(
            text = Strings.login(currentLang),
            fontWeight = FontWeight.Bold,
            color = if (!isRegisterMode) FireOrange else TextSecondary
          )
        },
        modifier = Modifier.testTag("login_tab")
      )
      Tab(
        selected = isRegisterMode,
        onClick = {
          isRegisterMode = true
          errorMessage = ""
          successMessage = ""
        },
        text = {
          Text(
            text = Strings.register(currentLang),
            fontWeight = FontWeight.Bold,
            color = if (isRegisterMode) FireOrange else TextSecondary
          )
        },
        modifier = Modifier.testTag("register_tab")
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Error or Success
    if (errorMessage.isNotBlank()) {
      Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = GamingRed.copy(alpha = 0.15f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingRed)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.ErrorOutline, contentDescription = "Error", tint = GamingRed)
          Text(text = errorMessage, color = GamingRed, fontSize = 12.sp)
        }
      }
    }

    if (successMessage.isNotBlank()) {
      Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = GamingGreen.copy(alpha = 0.15f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GamingGreen)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.CheckCircleOutline, contentDescription = "Success", tint = GamingGreen)
          Text(text = successMessage, color = GamingGreen, fontSize = 12.sp)
        }
      }
    }

    // Input Fields
    if (isRegisterMode) {
      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text(if (currentLang == AppLanguage.BANGLA) "আপনার নাম" else "Full Name") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FireOrange) },
        modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = FireOrange,
          unfocusedBorderColor = GamingBorder
        ),
        singleLine = true
      )
      Spacer(modifier = Modifier.height(10.dp))
    }

    OutlinedTextField(
      value = username,
      onValueChange = { username = it },
      label = { Text(if (currentLang == AppLanguage.BANGLA) "ইউজারনেম" else "Username") },
      leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = FireOrange) },
      modifier = Modifier.fillMaxWidth().testTag("username_input"),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FireOrange,
        unfocusedBorderColor = GamingBorder
      ),
      singleLine = true
    )

    if (isRegisterMode) {
      Spacer(modifier = Modifier.height(10.dp))
      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text(if (currentLang == AppLanguage.BANGLA) "মোবাইল নম্বর" else "Phone Number") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = FireOrange) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = FireOrange,
          unfocusedBorderColor = GamingBorder
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Registration Role Selection (Strictly CUSTOMER or SELLER only)
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "আপনার রোল নির্বাচন করুন:" else "Select Registration Role:",
          color = TextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          listOf(Role.CUSTOMER, Role.SELLER).forEach { role ->
            val isSelected = selectedRole == role
            OutlinedButton(
              onClick = { selectedRole = role },
              modifier = Modifier.weight(1f).height(44.dp).testTag("role_select_${role.name.lowercase()}"),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSelected) FireOrange.copy(alpha = 0.2f) else GamingSurface
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) FireOrange else GamingBorder
              )
            ) {
              Text(
                text = if (role == Role.CUSTOMER) Strings.roleCustomer(currentLang) else Strings.roleSeller(currentLang),
                color = if (isSelected) FireOrange else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        // Public registration security note
        Text(
          text = if (currentLang == AppLanguage.BANGLA)
            "🔒 সিকিউরিটি পলিসি: পাবলিক রেজিস্ট্রেশনে শুধুমাত্র CUSTOMER ও SELLER অনুমোদিত। ADMIN বা MODERATOR পাবলিকলি রেজিস্টার করা সম্পূর্ণ নিষিদ্ধ।"
          else
            "🔒 Security Architecture: Public registration is strictly limited to CUSTOMER and SELLER. ADMIN/MODERATOR escalation is server-enforced and blocked.",
          color = TextMuted,
          fontSize = 10.sp,
          modifier = Modifier.padding(top = 6.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text(if (currentLang == AppLanguage.BANGLA) "পাসওয়ার্ড" else "Password") },
      leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FireOrange) },
      trailingIcon = {
        IconButton(onClick = { showPassword = !showPassword }) {
          Icon(
            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = "Toggle Password",
            tint = TextSecondary
          )
        }
      },
      visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      modifier = Modifier.fillMaxWidth().testTag("password_input"),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FireOrange,
        unfocusedBorderColor = GamingBorder
      ),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Main Action Button
    Button(
      onClick = {
        errorMessage = ""
        successMessage = ""
        if (isRegisterMode) {
          val res = AppRepository.register(name, username, password, phone, selectedRole)
          res.onSuccess {
            successMessage = "Registration successful!"
            onLoginSuccess()
          }.onFailure {
            errorMessage = it.message ?: "Registration failed."
          }
        } else {
          val res = AppRepository.login(username, password)
          res.onSuccess {
            onLoginSuccess()
          }.onFailure {
            errorMessage = it.message ?: "Login failed."
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("auth_submit_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = FireOrange),
      shape = RoundedCornerShape(10.dp)
    ) {
      Text(
        text = if (isRegisterMode) Strings.register(currentLang) else Strings.login(currentLang),
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Quick Login for Demo/Testing
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = GamingSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, GamingBorder)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = if (currentLang == AppLanguage.BANGLA) "⚡ ডেমো মোড কুইক লগইন" else "⚡ Demo Mode Quick Switch",
          color = CyberGold,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = if (currentLang == AppLanguage.BANGLA)
            "নিচের যেকোনো একাউন্টে ক্লিক করে তাৎক্ষণিক লগইন করতে পারেন:"
          else
            "Tap any persona below to quickly test full system workflows:",
          color = TextSecondary,
          fontSize = 11.sp,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          OutlinedButton(
            onClick = {
              AppRepository.switchUserForDemo(Role.CUSTOMER)
              onLoginSuccess()
            },
            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_login_customer"),
            border = androidx.compose.foundation.BorderStroke(1.dp, GamingGreen.copy(alpha = 0.5f))
          ) {
            Text("Customer", fontSize = 11.sp, color = GamingGreen, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              AppRepository.switchUserForDemo(Role.SELLER)
              onLoginSuccess()
            },
            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_login_seller"),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
          ) {
            Text("Seller", fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              AppRepository.switchUserForDemo(Role.MODERATOR)
              onLoginSuccess()
            },
            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_login_mod"),
            border = androidx.compose.foundation.BorderStroke(1.dp, GamingPurple.copy(alpha = 0.5f))
          ) {
            Text("Mod", fontSize = 11.sp, color = GamingPurple, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              AppRepository.switchUserForDemo(Role.OWNER)
              onLoginSuccess()
            },
            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_login_admin"),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
          ) {
            Text("Admin", fontSize = 11.sp, color = CyberGold, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
