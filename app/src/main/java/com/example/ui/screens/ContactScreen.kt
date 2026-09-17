package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.AppLanguage
import com.example.ui.components.IntentHelpers

@Composable
fun ContactScreen(
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp)
  ) {
    // Header
    Text(
      text = if (language == AppLanguage.BN) "যোগাযোগ ও কাস্টমার সাপোর্ট" else "Contact & Customer Support",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )
    Text(
      text = if (language == AppLanguage.BN)
        "যে কোনো সরকারি ও অনলাইন সেবার সহায়তায় ২৪ ঘন্টা আমাদের সাথে যোগাযোগ করুন"
      else
        "24/7 dedicated customer assistance for all digital services",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Main Contact Card
    ElevatedCard(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color(0xFF0F4C81)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Headphones,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "২৪ ঘন্টা কাস্টমার সাপোর্ট",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "24/7 Direct Online Helpline: 9832310837",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF10B981)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = { IntentHelpers.dialPhoneNumber(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
              .testTag("contact_call_button")
          ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "সরাসরি কল", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = { IntentHelpers.openWhatsApp(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
              .testTag("contact_whatsapp_button")
          ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "WhatsApp চ্যাট", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(12.dp))

        // Contact info rows
        ContactDetailItem(
          icon = Icons.Default.LocationOn,
          label = "সাইবার ক্যাফে ঠিকানা",
          value = "মোহাম্মদ অনলাইন সাইবার ক্যাফে, প্রধান বাজার রোড (সরকারি সেবা কেন্দ্র)"
        )
        ContactDetailItem(
          icon = Icons.Default.Email,
          label = "অফিসিয়াল ইমেইল",
          value = "mohammadali9832310837@gmail.com"
        )
        ContactDetailItem(
          icon = Icons.Default.AccessTime,
          label = "দোকানের কাজের সময়",
          value = "প্রতিদিন সকাল ৮:০০ টা থেকে রাত ১০:০০ টা পর্যন্ত (অনলাইন পোর্টাল ২৪/৭ খোলা)"
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // FAQ Section
    Text(
      text = if (language == AppLanguage.BN) "সাধারণ জিজ্ঞাসা ও উত্তর (FAQ)" else "Frequently Asked Questions",
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(10.dp))

    val faqs = listOf(
      "প্যান কার্ড (PAN Card) পেতে কত দিন সময় লাগে?" to
        "সাধারণত অনলাইন আবেদনের ৩ থেকে ৫ কর্মদিবসের মধ্যে ডিজিটাল ই-প্যান (e-PAN) প্রস্তুত হয়ে যায় এবং ১০ থেকে ১৫ দিনের মধ্যে ডাকযোগে মূল কার্ড পৌঁছায়। জরুরি ভিত্তিতে তাৎক্ষণিক ই-প্যান করে দেওয়া হয়।",
      "ভোটার আইডি কার্ড সংশোধন বা স্থানান্তরে কি কি লাগে?" to
        "আধার কার্ড, ঠিকানার প্রমাণপত্র এবং মোবাইল নম্বর প্রয়োজন। আমাদের ক্যাফেতে এসে ছবি ও তথ্য যাচাই করে আবেদন সাবমিট করা হয়।",
      "পিএফ (PF) একাউন্ট থেকে টাকা তোলার নিয়ম কি?" to
        "আপনার সক্রিয় UAN নম্বর, আধারের সাথে লিংক করা মোবাইল নম্বর এবং ব্যাংকের পাসবই বা ক্যান্সেল চেকবই প্রয়োজন। আবেদন করার ৭-১০ দিনের মধ্যে সরাসরি ব্যাংক একাউন্টে টাকা জমা হয়।",
      "গাড়ির আরসি (RC), ট্যাক্স ও ড্রাইভিং লাইসেন্স সেবা কি উপলব্ধ?" to
        "হ্যাঁ! বাণিজ্যিক ও ব্যক্তিগত গাড়ির রোড ট্যাক্স পেমেন্ট, ফিটনেস নবায়ন, পলিউশন এবং ড্রাইভিং লাইসেন্স স্লট বুকিং ও রিনিউয়াল সবই তাৎক্ষণিক সম্পন্ন করা হয়।"
    )

    faqs.forEach { (question, answer) ->
      FaqItem(question = question, answer = answer)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
fun ContactDetailItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    verticalAlignment = Alignment.Top
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier
        .size(18.dp)
        .padding(top = 2.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = label,
        fontSize = 11.sp,
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
}

@Composable
fun FaqItem(question: String, answer: String) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { isExpanded = !isExpanded }
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.QuestionAnswer,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = question,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Icon(
          imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      AnimatedVisibility(visible = isExpanded) {
        Column {
          Spacer(modifier = Modifier.height(8.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = answer,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 17.sp
          )
        }
      }
    }
  }
}
