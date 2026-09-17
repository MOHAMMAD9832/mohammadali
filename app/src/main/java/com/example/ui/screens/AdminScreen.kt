package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ApplicationEntity
import com.example.data.ApplicationStatus
import com.example.ui.AppLanguage
import com.example.ui.components.IntentHelpers

@Composable
fun AdminScreen(
  language: AppLanguage,
  applications: List<ApplicationEntity>,
  onUpdateStatus: (refId: String, status: ApplicationStatus, remarks: String) -> Unit,
  onDeleteApplication: (ApplicationEntity) -> Unit,
  onLogoutAdmin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var searchQuery by remember { mutableStateOf("") }
  var statusFilter by remember { mutableStateOf<ApplicationStatus?>(null) }

  // Metrics
  val totalCount = applications.size
  val pendingCount = applications.count { it.statusEnum == ApplicationStatus.PENDING }
  val processingCount = applications.count { it.statusEnum == ApplicationStatus.PROCESSING || it.statusEnum == ApplicationStatus.VERIFYING }
  val completedCount = applications.count { it.statusEnum == ApplicationStatus.COMPLETED }

  val filteredApps = remember(applications, searchQuery, statusFilter) {
    applications.filter { app ->
      val matchesStatus = statusFilter == null || app.statusEnum == statusFilter
      val matchesSearch = searchQuery.isBlank() ||
        app.referenceId.contains(searchQuery, ignoreCase = true) ||
        app.customerName.contains(searchQuery, ignoreCase = true) ||
        app.mobileNumber.contains(searchQuery, ignoreCase = true) ||
        app.serviceName.contains(searchQuery, ignoreCase = true)
      matchesStatus && matchesSearch
    }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp)
  ) {
    // Header & Logout
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ক্যাফে অ্যাডমিন কন্ট্রোল প্যানেল",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "আবেদন পরিচালনা ও রিয়েল-টাইম স্ট্যাটাস আপডেট",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        OutlinedButton(
          onClick = onLogoutAdmin,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("admin_logout_button")
        ) {
          Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "লক", fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Stats Grid
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        AdminStatCard(
          title = "মোট আবেদন",
          count = totalCount.toString(),
          color = Color(0xFF0F4C81),
          modifier = Modifier.weight(1f)
        )
        AdminStatCard(
          title = "অপেক্ষমাণ",
          count = pendingCount.toString(),
          color = Color(0xFFD97706),
          modifier = Modifier.weight(1f)
        )
        AdminStatCard(
          title = "প্রসেসিং",
          count = processingCount.toString(),
          color = Color(0xFF7C3AED),
          modifier = Modifier.weight(1f)
        )
        AdminStatCard(
          title = "সম্পন্ন",
          count = completedCount.toString(),
          color = Color(0xFF059669),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Search input
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(text = "রেফারেন্স নম্বর / নাম / মোবাইল দিয়ে খুঁজুন...") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("admin_search_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Filter chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = statusFilter == null,
            onClick = { statusFilter = null },
            label = { Text(text = "সব (${applications.size})", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
        items(ApplicationStatus.entries) { st ->
          val count = applications.count { it.statusEnum == st }
          FilterChip(
            selected = statusFilter == st,
            onClick = { statusFilter = st },
            label = { Text(text = "${st.bnTitle} ($count)", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    // Applications list
    if (filteredApps.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "কোনো আবেদন পাওয়া যায়নি",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(filteredApps, key = { it.referenceId }) { app ->
        AdminApplicationCard(
          app = app,
          onUpdateStatus = { newStatus, remarks ->
            onUpdateStatus(app.referenceId, newStatus, remarks)
          },
          onDelete = { onDeleteApplication(app) },
          onCallCustomer = { IntentHelpers.dialPhoneNumber(context, app.mobileNumber) },
          onWhatsAppCustomer = {
            val msg = "নমস্কার ${app.customerName}, আপনার মোহাম্মদ অনলাইন সাইবার ক্যাফে আবেদন #${app.referenceId} (${app.serviceName}) এর বর্তমান অবস্থা: ${app.statusEnum.bnTitle}।"
            IntentHelpers.openWhatsApp(context, msg, app.mobileNumber)
          }
        )
        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}

@Composable
fun AdminStatCard(
  title: String,
  count: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.1f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = count,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        color = color
      )
      Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
fun AdminApplicationCard(
  app: ApplicationEntity,
  onUpdateStatus: (ApplicationStatus, String) -> Unit,
  onDelete: () -> Unit,
  onCallCustomer: () -> Unit,
  onWhatsAppCustomer: () -> Unit
) {
  var isStatusMenuOpen by remember { mutableStateOf(false) }
  var isEditingRemarks by remember { mutableStateOf(false) }
  var remarksInput by remember(app.adminRemarks) { mutableStateOf(app.adminRemarks) }

  ElevatedCard(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top row: Ref ID + Status changer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = app.referenceId,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = app.appliedDateFormatted,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Box {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.testTag("admin_change_status_${app.referenceId}")
          ) {
            Row(
              modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              StatusChip(status = app.statusEnum)
              IconButton(onClick = { isStatusMenuOpen = true }, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "পরিবর্তন", modifier = Modifier.size(14.dp))
              }
            }
          }

          DropdownMenu(
            expanded = isStatusMenuOpen,
            onDismissRequest = { isStatusMenuOpen = false }
          ) {
            ApplicationStatus.entries.forEach { st ->
              DropdownMenuItem(
                text = { Text(text = st.bnTitle, fontWeight = if (st == app.statusEnum) FontWeight.Bold else FontWeight.Normal) },
                onClick = {
                  onUpdateStatus(st, app.adminRemarks)
                  isStatusMenuOpen = false
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Applicant details
      Text(
        text = app.customerName,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "সেবা: ${app.serviceName} (${app.serviceCategory})",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary
      )
      Text(
        text = "মোবাইল: ${app.mobileNumber}${if (app.email.isNotBlank()) " | ইমেইল: ${app.email}" else ""}",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      if (app.notes.isNotBlank()) {
        Text(
          text = "নোট: ${app.notes}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Remarks section
      if (isEditingRemarks) {
        OutlinedTextField(
          value = remarksInput,
          onValueChange = { remarksInput = it },
          label = { Text("গ্রাহককে জানানোর রিমার্কস") },
          placeholder = { Text("যেমন: আধার ওটিপি প্রয়োজন, কার্ড প্রিন্ট হয়েছে ইত্যাদি") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(
            onClick = {
              onUpdateStatus(app.statusEnum, remarksInput)
              isEditingRemarks = false
            },
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
          ) {
            Text("সংরক্ষণ", fontSize = 11.sp)
          }
          OutlinedButton(
            onClick = { isEditingRemarks = false },
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
          ) {
            Text("বাতিল", fontSize = 11.sp)
          }
        }
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (app.adminRemarks.isBlank()) "রিমার্কস নেই" else "রিমার্কস: ${app.adminRemarks}",
            fontSize = 11.sp,
            color = if (app.adminRemarks.isBlank()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
          )
          OutlinedButton(
            onClick = { isEditingRemarks = true },
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text("রিমার্কস সম্পাদনা", fontSize = 10.sp)
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
      )

      // Direct Customer Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Button(
            onClick = onCallCustomer,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "কল", fontSize = 11.sp)
          }

          Button(
            onClick = onWhatsAppCustomer,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "WhatsApp", fontSize = 11.sp)
          }
        }

        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "মুছে ফেলুন",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
