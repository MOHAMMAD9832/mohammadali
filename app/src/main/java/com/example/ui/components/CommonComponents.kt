package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ApplicationEntity
import com.example.ui.AppLanguage
import com.example.ui.AppTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberCafeTopAppBar(
  currentTab: AppTab,
  language: AppLanguage,
  isAdminUnlocked: Boolean,
  onLanguageToggle: () -> Unit,
  onAdminClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Surface(
    color = MaterialTheme.colorScheme.primary,
    tonalElevation = 4.dp
  ) {
    TopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
      ),
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(vertical = 4.dp)
        ) {
          // Custom emblem logo
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  listOf(Color(0xFF38BDF8), Color(0xFF10B981))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "MOC",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "MOHAMMAD ONLINE",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp,
              color = Color.White,
              maxLines = 1
            )
            Text(
              text = if (language == AppLanguage.BN) "মোহাম্মদ অনলাইন সাইবার ক্যাফে" else "CYBER CAFE & DIGITAL SEVA",
              fontSize = 11.sp,
              fontWeight = FontWeight.Normal,
              color = Color.White.copy(alpha = 0.85f),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      },
      actions = {
        // Direct Call Button
        IconButton(
          onClick = { IntentHelpers.dialPhoneNumber(context) },
          modifier = Modifier.testTag("top_call_button")
        ) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "কল করুন ৯৮৩২৩১০৮৩৭",
            tint = Color(0xFF86EFAC) // Mint green
          )
        }

        // Direct WhatsApp Button
        IconButton(
          onClick = { IntentHelpers.openWhatsApp(context) },
          modifier = Modifier.testTag("top_whatsapp_button")
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "হোয়াটসঅ্যাপ করুন",
            tint = Color(0xFF4ADE80)
          )
        }

        // Language Switcher Chip
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onLanguageToggle() }
            .testTag("language_toggle_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = "ভাষা পরিবর্তন",
              modifier = Modifier.size(14.dp),
              tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (language == AppLanguage.BN) "বাংলা" else "EN",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        // Admin Lock icon
        IconButton(
          onClick = onAdminClick,
          modifier = Modifier.testTag("admin_lock_button")
        ) {
          Icon(
            imageVector = if (isAdminUnlocked) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
            contentDescription = "অ্যাডমিন প্যানেল",
            tint = if (isAdminUnlocked) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.75f)
          )
        }
      },
      modifier = modifier
    )
  }
}

@Composable
fun SupportBanner24x7(
  language: AppLanguage,
  onCallClick: () -> Unit,
  onWhatsAppClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(0.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFF064E3B) // Dark Emerald Green
    ),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color(0xFF10B981)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SupportAgent,
            contentDescription = "24/7 Support",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
          Text(
            text = if (language == AppLanguage.BN) "২৪/৭ সার্বক্ষণিক কাস্টমার সাপোর্ট" else "24/7 Customer Support Available",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "Helpline: 9832310837 (কল ও হোয়াটসঅ্যাপ)",
            fontSize = 10.sp,
            color = Color(0xFFD1FAE5)
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF10B981),
          modifier = Modifier.clickable { onCallClick() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Call,
              contentDescription = "Call",
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (language == AppLanguage.BN) "কল" else "Call",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF25D366),
          modifier = Modifier.clickable { onWhatsAppClick() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Chat,
              contentDescription = "WhatsApp",
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Chat",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun CyberCafeBottomNavigationBar(
  currentTab: AppTab,
  language: AppLanguage,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp,
    modifier = modifier
  ) {
    val items = listOf(
      Triple(AppTab.HOME, Icons.Default.Home, if (language == AppLanguage.BN) "হোম" else "Home"),
      Triple(AppTab.SERVICES, Icons.Default.Widgets, if (language == AppLanguage.BN) "পরিষেবা" else "Services"),
      Triple(AppTab.APPLY, Icons.Default.PostAdd, if (language == AppLanguage.BN) "আবেদন" else "Apply"),
      Triple(AppTab.TRACK, Icons.Default.FindInPage, if (language == AppLanguage.BN) "ট্র্যাক" else "Track"),
      Triple(AppTab.CONTACT, Icons.Default.Phone, if (language == AppLanguage.BN) "যোগাযোগ" else "Contact")
    )

    items.forEach { (tab, icon, title) ->
      val selected = currentTab == tab
      NavigationBarItem(
        selected = selected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
      )
    }
  }
}

@Composable
fun SubmissionConfirmationDialog(
  application: ApplicationEntity,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onTrackClick: (ApplicationEntity) -> Unit
) {
  val context = LocalContext.current

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Box(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(Color(0xFFD1FAE5)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "সফল",
          tint = Color(0xFF059669),
          modifier = Modifier.size(36.dp)
        )
      }
    },
    title = {
      Text(
        text = if (language == AppLanguage.BN) "আবেদন সফলভাবে জমা হয়েছে!" else "Application Submitted Successfully!",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 18.sp
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (language == AppLanguage.BN)
            "আপনার রেফারেন্স আইডিটি সংরক্ষণ করুন। এটি দিয়ে স্ট্যাটাস ট্র্যাক করতে পারবেন।"
          else
            "Please save your unique Reference ID to track application progress.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Highlight Reference ID Box
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = if (language == AppLanguage.BN) "রেফারেন্স নম্বর (Ref ID):" else "Reference ID:",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
              Text(
                text = application.referenceId,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }

            IconButton(
              onClick = {
                IntentHelpers.copyToClipboard(context, "Reference ID", application.referenceId)
              },
              modifier = Modifier.testTag("copy_ref_id_button")
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "কপি করুন",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Application Summary Details
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(10.dp)
        ) {
          Text(
            text = "• নাম: ${application.customerName}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "• সেবা: ${application.serviceName}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "• মোবাইল: ${application.mobileNumber}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Share to WhatsApp button
        FilledTonalButton(
          onClick = {
            val message = "নমস্কার মোহাম্মদ অনলাইন সাইবার ক্যাফে,\nআমি অনলাইন আবেদন জমা দিয়েছি।\nরেফারেন্স আইডি: ${application.referenceId}\nনাম: ${application.customerName}\nসেবা: ${application.serviceName}\nমোবাইল: ${application.mobileNumber}।"
            IntentHelpers.openWhatsApp(context, message)
          },
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFFD1FAE5),
            contentColor = Color(0xFF064E3B)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "WhatsApp",
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.BN) "ক্যাফে হোয়াটসঅ্যাপে পাঠান" else "Send Details to WhatsApp",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onDismiss()
          onTrackClick(application)
        },
        modifier = Modifier.testTag("dialog_track_now_button")
      ) {
        Text(text = if (language == AppLanguage.BN) "স্ট্যাটাস ট্র্যাক করুন" else "Track Status Now")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(text = if (language == AppLanguage.BN) "বন্ধ করুন" else "Close")
      }
    }
  )
}

@Composable
fun AdminPinDialog(
  onDismiss: () -> Unit,
  onUnlock: (String) -> Boolean
) {
  var pin by remember { mutableStateOf("") }
  var hasError by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Default.AdminPanelSettings,
        contentDescription = "Admin Login",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(36.dp)
      )
    },
    title = {
      Text(
        text = "মালিক / অ্যাডমিন লগইন",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
    },
    text = {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "দোকানের মালিকের জন্য সুরক্ষিত প্যানেল। ডিফল্ট পিন: 1234",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = pin,
          onValueChange = {
            if (it.length <= 6) {
              pin = it
              hasError = false
            }
          },
          label = { Text("৪-ডিজিট পিন কোড") },
          placeholder = { Text("1234") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              val success = onUnlock(pin)
              if (!success) hasError = true
            }
          ),
          visualTransformation = PasswordVisualTransformation(),
          isError = hasError,
          modifier = Modifier.fillMaxWidth().testTag("admin_pin_input")
        )

        if (hasError) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "ভুল পিন কোড! সঠিক পিন লিখুন (1234)",
            color = MaterialTheme.colorScheme.error,
            fontSize = 11.sp
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val success = onUnlock(pin)
          if (!success) hasError = true
        },
        modifier = Modifier.testTag("admin_pin_submit_button")
      ) {
        Text("আনলক করুন")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("বাতিল")
      }
    }
  )
}
