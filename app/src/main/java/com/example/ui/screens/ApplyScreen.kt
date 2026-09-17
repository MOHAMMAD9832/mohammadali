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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceCatalog
import com.example.data.ServiceCategory
import com.example.data.ServiceItem
import com.example.ui.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyScreen(
  language: AppLanguage,
  selectedService: ServiceItem?,
  onSubmitApplication: (
    customerName: String,
    mobile: String,
    altPhone: String,
    service: ServiceItem,
    documentType: String,
    notes: String,
    deliveryMethod: String
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  var customerName by remember { mutableStateOf("") }
  var mobileNumber by remember { mutableStateOf("") }
  var altPhone by remember { mutableStateOf("") }
  var currentService by remember(selectedService) {
    mutableStateOf(selectedService ?: ServiceCatalog.services.first())
  }
  var isDropdownExpanded by remember { mutableStateOf(false) }

  var notes by remember { mutableStateOf("") }
  var documentType by remember { mutableStateOf("আধার কার্ড / ভোটার কার্ড") }
  var isDocUploaded by remember { mutableStateOf(false) }
  var uploadedFileName by remember { mutableStateOf("") }

  var deliveryMethod by remember { mutableStateOf("হোয়াটসঅ্যাপে পিডিএফ ও ক্যাফে থেকে হার্ডকপি") }

  // Errors
  var nameError by remember { mutableStateOf<String?>(null) }
  var phoneError by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp)
  ) {
    // Header
    Text(
      text = if (language == AppLanguage.BN) "সার্ভিসের জন্য অনলাইন আবেদন" else "Online Service Application",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )
    Text(
      text = if (language == AppLanguage.BN)
        "সঠিক তথ্য প্রদান করে আবেদন করুন। সাথে সাথে রেফারেন্স আইডি পাবেন।"
      else
        "Fill in details to submit. You will receive an instant tracking ID.",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Form card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {

        // Service Selector Dropdown
        Text(
          text = if (language == AppLanguage.BN) "সেবা নির্বাচন করুন *" else "Select Service *",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
          expanded = isDropdownExpanded,
          onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = if (language == AppLanguage.BN) currentService.titleBn else currentService.titleEn,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
            leadingIcon = {
              Icon(imageVector = currentService.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
              .testTag("apply_service_dropdown")
          )

          ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
          ) {
            ServiceCatalog.services.forEach { srv ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(
                      text = if (language == AppLanguage.BN) srv.titleBn else srv.titleEn,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp
                    )
                    Text(
                      text = if (language == AppLanguage.BN) srv.category.bnTitle else srv.category.enTitle,
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                },
                leadingIcon = {
                  Icon(imageVector = srv.icon, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                onClick = {
                  currentService = srv
                  isDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Required docs preview pill
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "প্রয়োজনীয় নথি:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = currentService.requiredDocsBn,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Name
        OutlinedTextField(
          value = customerName,
          onValueChange = {
            customerName = it
            if (nameError != null) nameError = null
          },
          label = { Text(text = if (language == AppLanguage.BN) "আবেদনকারীর পূর্ণ নাম *" else "Full Applicant Name *") },
          leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
          isError = nameError != null,
          supportingText = {
            if (nameError != null) {
              Text(text = nameError!!, color = MaterialTheme.colorScheme.error)
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("apply_name_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mobile Number
        OutlinedTextField(
          value = mobileNumber,
          onValueChange = {
            if (it.length <= 10 && it.all { char -> char.isDigit() }) {
              mobileNumber = it
              if (phoneError != null) phoneError = null
            }
          },
          label = { Text(text = if (language == AppLanguage.BN) "মোবাইল নম্বর (১০ সংখ্যা) *" else "Mobile Number (10 digits) *") },
          leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          isError = phoneError != null,
          supportingText = {
            if (phoneError != null) {
              Text(text = phoneError!!, color = MaterialTheme.colorScheme.error)
            } else {
              Text(text = "এই নম্বরে স্ট্যাটাস ও আপডেট এসএমএস/হোয়াটসঅ্যাপ যাবে")
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("apply_mobile_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Alternate / WhatsApp Number
        OutlinedTextField(
          value = altPhone,
          onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) altPhone = it },
          label = { Text(text = if (language == AppLanguage.BN) "বিকল্প ফোন / হোয়াটসঅ্যাপ (ঐচ্ছিক)" else "Alt Phone / WhatsApp (Optional)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("apply_alt_phone_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Document Attachment Section
        Text(
          text = if (language == AppLanguage.BN) "প্রয়োজনীয় নথি আপলোড / সংযুক্তি" else "Document Upload / Attachment",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            if (!isDocUploaded) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "নথি নির্বাচন করুন (আধার / ফটো / পুরানো কাগজ)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "পিডিএফ বা ছবি সংযুক্ত করুন (সর্বোচ্চ 5 MB)",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                OutlinedButton(
                  onClick = {
                    isDocUploaded = true
                    uploadedFileName = "${customerName.ifBlank { "Applicant" }}_doc.pdf (1.2 MB)"
                  },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.testTag("apply_attach_doc_button")
                ) {
                  Icon(imageVector = Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "সংযুক্ত করুন", fontSize = 11.sp)
                }
              }
            } else {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = uploadedFileName,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                    Text(
                      text = "সফলভাবে সংযুক্ত হয়েছে",
                      fontSize = 10.sp,
                      color = Color(0xFF047857)
                    )
                  }
                }

                IconButton(
                  onClick = {
                    isDocUploaded = false
                    uploadedFileName = ""
                  }
                ) {
                  Icon(imageVector = Icons.Default.Close, contentDescription = "বাতিল", modifier = Modifier.size(18.dp))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Notes / Details
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text(text = if (language == AppLanguage.BN) "অতিরিক্ত বিবরণ / বিশেষ নির্দেশিকা" else "Additional Notes / Details") },
          placeholder = { Text(text = "যেমন: জরুরি ডেলিভারি, সংশোধনের বিবরণ ইত্যাদি") },
          minLines = 2,
          maxLines = 4,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("apply_notes_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery Preference
        Text(
          text = if (language == AppLanguage.BN) "ডেলিভারি গ্রহণের মাধ্যম" else "Delivery Preference",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )

        val deliveryOptions = listOf(
          "হোয়াটসঅ্যাপে পিডিএফ ও ক্যাফে থেকে হার্ডকপি",
          "সরাসরি সাইবার ক্যাফে থেকে প্রিন্ট কপি সংগ্রহ",
          "শুধুমাত্র জরুরি ডিজিটাল কপি (হোয়াটসঅ্যাপ/ইমেইল)"
        )

        deliveryOptions.forEach { option ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { deliveryMethod = option }
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = deliveryMethod == option,
              onClick = { deliveryMethod = option }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = option, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
          onClick = {
            var hasError = false
            if (customerName.trim().length < 2) {
              nameError = "অনুগ্রহ করে পূর্ণ নাম লিখুন"
              hasError = true
            }
            if (mobileNumber.trim().length < 10) {
              phoneError = "সঠিক ১০ সংখ্যার মোবাইল নম্বর দিন"
              hasError = true
            }

            if (!hasError) {
              onSubmitApplication(
                customerName.trim(),
                mobileNumber.trim(),
                altPhone.trim(),
                currentService,
                documentType,
                notes.trim(),
                deliveryMethod
              )
              // Reset
              customerName = ""
              mobileNumber = ""
              altPhone = ""
              notes = ""
              isDocUploaded = false
              uploadedFileName = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("apply_submit_button")
        ) {
          Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.BN) "আবেদন জমা দিন (Submit)" else "Submit Application",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
