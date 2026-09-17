package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ApplicationStatus(val step: Int, val bnTitle: String, val enTitle: String) {
  PENDING(1, "আবেদন জমা হয়েছে", "Application Submitted"),
  VERIFYING(2, "নথি যাচাই চলছে", "Document Verification"),
  PROCESSING(3, "কাজ প্রক্রিয়াধীন", "Processing at Portal"),
  COMPLETED(4, "কাজ সম্পন্ন হয়েছে", "Completed & Ready"),
  REJECTED(2, "আবেদন স্থগিত/বাতিল", "Action Required");

  companion object {
    fun fromStatus(status: String): ApplicationStatus {
      return entries.firstOrNull { it.bnTitle.equals(status, ignoreCase = true) || it.name.equals(status, ignoreCase = true) }
        ?: when {
          status.contains("জমা") -> PENDING
          status.contains("যাচাই") -> VERIFYING
          status.contains("প্রক্রিয়া") || status.contains("প্রসেস") -> PROCESSING
          status.contains("সম্পন্ন") -> COMPLETED
          status.contains("বাতিল") || status.contains("স্থগিত") -> REJECTED
          else -> PENDING
        }
    }
  }
}

@Entity(tableName = "applications")
data class ApplicationEntity(
  @PrimaryKey
  val referenceId: String, // e.g., "MOC-2026-8492"
  val customerName: String,
  val mobileNumber: String,
  val email: String = "",
  val serviceCategory: String,
  val serviceName: String,
  val notes: String = "",
  val documentType: String = "আধার কার্ড (Aadhaar Card)",
  val documentAttachmentName: String = "",
  val status: String = "আবেদন জমা হয়েছে",
  val statusStep: Int = 1, // 1: Submitted, 2: Verification, 3: Under Process, 4: Completed
  val adminRemarks: String = "আপনার আবেদনটি সফলভাবে গৃহীত হয়েছে। আমাদের প্রতিনিধি শীঘ্রই প্রক্রিয়া শুরু করবেন।",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
) {
  val statusEnum: ApplicationStatus
    get() = ApplicationStatus.fromStatus(status)

  val appliedDateFormatted: String
    get() {
      val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
      return sdf.format(Date(createdAt))
    }
}

