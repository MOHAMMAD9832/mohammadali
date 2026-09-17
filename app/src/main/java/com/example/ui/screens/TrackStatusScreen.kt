package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ApplicationEntity
import com.example.data.ApplicationStatus
import com.example.ui.AppLanguage
import com.example.ui.components.IntentHelpers

@Composable
fun TrackStatusScreen(
  language: AppLanguage,
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  onSearch: () -> Unit,
  trackedApplication: ApplicationEntity?,
  recentApplications: List<ApplicationEntity>,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp)
  ) {
    // Heading
    Text(
      text = if (language == AppLanguage.BN) "আবেদনের স্ট্যাটাস ট্র্যাক করুন" else "Track Application Status",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )
    Text(
      text = if (language == AppLanguage.BN)
        "আপনার রেফারেন্স আইডি (যেমন: MOC-2026-1001) অথবা মোবাইল নম্বর দিয়ে সার্চ করুন"
      else
        "Search by Reference ID (e.g., MOC-2026-1001) or Registered Mobile Number",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Search Card
    ElevatedCard(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          label = { Text(text = if (language == AppLanguage.BN) "রেফারেন্স আইডি বা মোবাইল নম্বর" else "Reference ID or Mobile Number") },
          placeholder = { Text(text = "MOC-2026-XXXX / 9832310837") },
          leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("track_status_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = onSearch,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("track_search_button")
        ) {
          Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (language == AppLanguage.BN) "স্ট্যাটাস দেখুন (Track Now)" else "Track Status",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Results Section
    if (trackedApplication != null) {
      ApplicationDetailsCard(
        app = trackedApplication,
        language = language,
        onCopyRefId = {
          clipboardManager.setText(AnnotatedString(trackedApplication.referenceId))
        },
        onCallHelp = { IntentHelpers.dialPhoneNumber(context) },
        onWhatsAppHelp = {
          val msg = "Hello Mohammad Online Cyber Cafe, inquiring regarding my application Ref ID: ${trackedApplication.referenceId} (${trackedApplication.serviceName})"
          IntentHelpers.openWhatsApp(context, msg)
        }
      )
    } else {
      // Empty or not found info
      if (searchQuery.isNotBlank()) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.error,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "কোনো আবেদন পাওয়া যায়নি!",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.error
              )
              Text(
                text = "আইডি অথবা মোবাইল নম্বরটি পুনরায় যাচাই করুন। অথবা সরাসরি হেল্পলাইনে যোগাযোগ করুন।",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onErrorContainer
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      // Quick test sample applications
      if (recentApplications.isNotEmpty()) {
        Text(
          text = if (language == AppLanguage.BN) "সাম্প্রতিক আবেদনসমূহ (ক্লিক করে ট্র্যাক করুন):" else "Recent Submissions (Tap to track):",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        recentApplications.take(4).forEach { item ->
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable {
                onSearchQueryChanged(item.referenceId)
                onSearch()
              }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = item.customerName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${item.serviceName} • ${item.referenceId}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              StatusChip(status = item.statusEnum)
            }
          }
        }
      }
    }
  }
}

@Composable
fun ApplicationDetailsCard(
  app: ApplicationEntity,
  language: AppLanguage,
  onCopyRefId: () -> Unit,
  onCallHelp: () -> Unit,
  onWhatsAppHelp: () -> Unit
) {
  ElevatedCard(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header with ref ID and status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "রেফারেন্স নম্বর",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = app.referenceId,
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onCopyRefId, modifier = Modifier.size(28.dp)) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "কপি করুন",
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }

        StatusChip(status = app.statusEnum)
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
      )

      // Service and Applicant info
      TrackInfoRow(label = "আবেদনকারীর নাম", value = app.customerName)
      TrackInfoRow(label = "নির্বাচিত সেবা", value = app.serviceName)
      TrackInfoRow(label = "মোবাইল নম্বর", value = app.mobileNumber)
      TrackInfoRow(label = "আবেদনের তারিখ", value = app.appliedDateFormatted)
      TrackInfoRow(label = "নথি সংযুক্তি", value = app.documentType)

      if (app.adminRemarks.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFFEF3C7),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "ক্যাফে রিমার্কস / আপডেট:",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF92400E)
            )
            Text(
              text = app.adminRemarks,
              fontSize = 12.sp,
              color = Color(0xFF78350F)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // STEP PROGRESS TRACKER
      Text(
        text = "অগ্রগতির পর্যায় (Processing Stages):",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(12.dp))

      ProgressTimeline(status = app.statusEnum)

      Spacer(modifier = Modifier.height(18.dp))

      // Quick assistance buttons
      Text(
        text = "জরুরি তথ্যের জন্য ক্যাফেতে যোগাযোগ করুন:",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onCallHelp,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "কল করুন", fontSize = 12.sp)
        }

        Button(
          onClick = onWhatsAppHelp,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "হোয়াটসঅ্যাপ", fontSize = 12.sp)
        }
      }
    }
  }
}

@Composable
fun TrackInfoRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun StatusChip(status: ApplicationStatus) {
  val (bgColor, textColor) = when (status) {
    ApplicationStatus.PENDING -> Color(0xFFFEF3C7) to Color(0xFF92400E)
    ApplicationStatus.VERIFYING -> Color(0xFFE0F2FE) to Color(0xFF075985)
    ApplicationStatus.PROCESSING -> Color(0xFFEDE9FE) to Color(0xFF5B21B6)
    ApplicationStatus.COMPLETED -> Color(0xFFD1FAE5) to Color(0xFF065F46)
    ApplicationStatus.REJECTED -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
  }

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = bgColor
  ) {
    Text(
      text = status.bnTitle,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = textColor,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
    )
  }
}

@Composable
fun ProgressTimeline(status: ApplicationStatus) {
  val stages = listOf(
    "আবেদন জমা" to "Application Received",
    "নথি যাচাই" to "Document Verification",
    "প্রসেসিং চলছে" to "In Process with Portal",
    "কাজ সম্পন্ন" to "Ready for Delivery"
  )

  val activeIndex = when (status) {
    ApplicationStatus.PENDING -> 0
    ApplicationStatus.VERIFYING -> 1
    ApplicationStatus.PROCESSING -> 2
    ApplicationStatus.COMPLETED -> 3
    ApplicationStatus.REJECTED -> 1
  }

  Column(modifier = Modifier.fillMaxWidth()) {
    stages.forEachIndexed { index, (stageBn, stageEn) ->
      val isDone = index <= activeIndex
      val isCurrent = index == activeIndex

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Dot / Icon
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(
              if (isDone) {
                if (status == ApplicationStatus.REJECTED && isCurrent) Color(0xFFEF4444)
                else Color(0xFF10B981)
              } else {
                MaterialTheme.colorScheme.surfaceVariant
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isDone) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
          } else {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outline)
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stageBn,
            fontSize = 12.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = stageEn,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (isCurrent) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = "বর্তমান ধাপ",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      if (index < stages.size - 1) {
        Box(
          modifier = Modifier
            .padding(start = 11.dp)
            .width(2.dp)
            .height(20.dp)
            .background(if (index < activeIndex) Color(0xFF10B981) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        )
      }
    }
  }
}
