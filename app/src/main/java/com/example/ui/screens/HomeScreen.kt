package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceCatalog
import com.example.data.ServiceCategory
import com.example.data.ServiceItem
import com.example.ui.AppLanguage
import com.example.ui.AppTab
import com.example.ui.components.IntentHelpers

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  language: AppLanguage,
  onNavigateToTab: (AppTab) -> Unit,
  onSelectServiceToApply: (ServiceItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedCategory by remember { mutableStateOf(ServiceCategory.ALL) }

  val filteredServices = remember(selectedCategory) {
    if (selectedCategory == ServiceCategory.ALL) {
      ServiceCatalog.services
    } else {
      ServiceCatalog.services.filter { it.category == selectedCategory }
    }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 32.dp)
  ) {
    // 1. HERO SECTION
    item {
      HeroBanner(
        language = language,
        onApplyClick = { onNavigateToTab(AppTab.APPLY) },
        onTrackClick = { onNavigateToTab(AppTab.TRACK) },
        onCallClick = { IntentHelpers.dialPhoneNumber(context) }
      )
    }

    // 2. TRUST / HIGHLIGHT METRICS
    item {
      KeyHighlightsRow(language = language)
    }

    // 3. CATEGORIZED SERVICES SECTION
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (language == AppLanguage.BN) "আমাদের অনলাইন পরিষেবাসমূহ" else "Our Digital Services",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = if (language == AppLanguage.BN) "প্যান, ভোটার, আরসি, রেশন ও পিএফ সহ সকল সরকারি কাজ" else "Government & Digital document assistance",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Text(
            text = "${filteredServices.size} সেবা",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips Row
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(ServiceCategory.entries) { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = cat },
              label = {
                Text(
                  text = if (language == AppLanguage.BN) cat.bnTitle else cat.enTitle,
                  fontSize = 12.sp
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = cat.icon,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier.testTag("filter_chip_${cat.name.lowercase()}")
            )
          }
        }
      }
    }

    // 4. SERVICE CARDS
    items(filteredServices) { service ->
      ServiceCardItem(
        service = service,
        language = language,
        onApplyClick = { onSelectServiceToApply(service) },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )
    }

    // 5. HOW IT WORKS SECTION (3 সহজ ধাপ)
    item {
      Spacer(modifier = Modifier.height(20.dp))
      HowItWorksSection(language = language)
    }

    // 6. CONTACT & LOCATION SUMMARY CARD
    item {
      Spacer(modifier = Modifier.height(20.dp))
      ShopContactSummaryCard(
        language = language,
        onCallClick = { IntentHelpers.dialPhoneNumber(context) },
        onWhatsAppClick = { IntentHelpers.openWhatsApp(context) }
      )
    }

    // 7. SEO & FOOTER
    item {
      Spacer(modifier = Modifier.height(24.dp))
      SeoFooter(language = language)
    }
  }
}

@Composable
fun HeroBanner(
  language: AppLanguage,
  onApplyClick: () -> Unit,
  onTrackClick: () -> Unit,
  onCallClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0F4C81),
            Color(0xFF072B4B)
          )
        )
      )
      .padding(horizontal = 18.dp, vertical = 24.dp)
  ) {
    Column(horizontalAlignment = Alignment.Start) {
      // Badge pill
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF10B981).copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(Color(0xFF10B981))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (language == AppLanguage.BN) "সরকারি ও ডিজিটাল নথি পরিষেবা কেন্দ্র" else "Govt & Digital Citizen Service Center",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF86EFAC)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = if (language == AppLanguage.BN)
          "ডিজিটাল ইন্ডিয়া ও অনলাইন সেবায়\nবিশ্বস্ত প্রতিষ্ঠান"
        else
          "Mohammad Online Cyber Cafe\nYour Trusted Digital Services",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        lineHeight = 32.sp,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = if (language == AppLanguage.BN)
          "প্যান কার্ড, ভোটার আইডি, গাড়ির আরসি, রেশন কার্ড, পিএফ টাকা তোলা ও সকল সার্টিফিকেট অনলাইনে ঘরে বসেই আবেদন করুন।"
        else
          "Instant PAN card, Voter ID, Vehicle RC, Ration card, PF claim withdrawal, certificates and print services.",
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Color(0xFFE0F2FE)
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Hero Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onApplyClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF10B981),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1.1f)
            .height(44.dp)
            .testTag("hero_apply_now_button")
        ) {
          Icon(
            imageVector = Icons.Default.PostAdd,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (language == AppLanguage.BN) "আবেদন করুন" else "Apply Now",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        OutlinedButton(
          onClick = onTrackClick,
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("hero_track_status_button")
        ) {
          Icon(
            imageVector = Icons.Default.FindInPage,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF38BDF8)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (language == AppLanguage.BN) "স্ট্যাটাস চেক" else "Check Status",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

@Composable
fun KeyHighlightsRow(language: AppLanguage) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      .padding(horizontal = 14.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceAround
  ) {
    HighlightPill(
      icon = Icons.Default.Speed,
      text = if (language == AppLanguage.BN) "দ্রুত প্রসেসিং" else "Fast Processing"
    )
    HighlightPill(
      icon = Icons.Default.Security,
      text = if (language == AppLanguage.BN) "১০০% নিরাপদ" else "100% Secure"
    )
    HighlightPill(
      icon = Icons.Default.VerifiedUser,
      text = if (language == AppLanguage.BN) "অনুমোদিত সেবা" else "Govt Verified"
    )
  }
}

@Composable
fun HighlightPill(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  text: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(15.dp)
    )
    Text(
      text = text,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun ServiceCardItem(
  service: ServiceItem,
  language: AppLanguage,
  onApplyClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = service.icon,
              contentDescription = service.titleEn,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = if (language == AppLanguage.BN) service.titleBn else service.titleEn,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (language == AppLanguage.BN) service.category.bnTitle else service.category.enTitle,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        if (service.isPopular) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFEF3C7)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFD97706),
                modifier = Modifier.size(11.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "জনপ্রিয়",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = if (language == AppLanguage.BN) service.descriptionBn else service.descriptionEn,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 17.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Required docs hint
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "প্রয়োজনীয় নথি: ${service.requiredDocsBn}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onApplyClick,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
          modifier = Modifier.testTag("apply_service_${service.id}")
        ) {
          Text(
            text = if (language == AppLanguage.BN) "সার্ভিসের জন্য আবেদন করুন" else "Apply for Service",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
fun HowItWorksSection(language: AppLanguage) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Text(
      text = if (language == AppLanguage.BN) "কিভাবে সেবা পাবেন? (৩টি সহজ ধাপ)" else "How It Works (3 Easy Steps)",
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      StepCard(
        step = "১",
        title = if (language == AppLanguage.BN) "অনলাইনে আবেদন" else "Apply Online",
        desc = if (language == AppLanguage.BN) "সেবা নির্বাচন ও প্রয়োজনীয় তথ্য সাবমিট" else "Select service and submit basic details",
        modifier = Modifier.weight(1f)
      )
      StepCard(
        step = "২",
        title = if (language == AppLanguage.BN) "স্ট্যাটাস ট্র্যাক" else "Track Progress",
        desc = if (language == AppLanguage.BN) "রেফারেন্স আইডি দিয়ে যেকোনো সময় অগ্রগতি দেখুন" else "Check real-time application updates",
        modifier = Modifier.weight(1f)
      )
      StepCard(
        step = "৩",
        title = if (language == AppLanguage.BN) "নথি সংগ্রহ" else "Receive Docs",
        desc = if (language == AppLanguage.BN) "ক্যাফে থেকে সংগ্রহ অথবা হোয়াটসঅ্যাপে ডাউনলোড" else "Pick up at cafe or receive on WhatsApp",
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
fun StepCard(
  step: String,
  title: String,
  desc: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surface,
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = step,
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = desc,
        fontSize = 10.sp,
        textAlign = TextAlign.Center,
        lineHeight = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun ShopContactSummaryCard(
  language: AppLanguage,
  onCallClick: () -> Unit,
  onWhatsAppClick: () -> Unit
) {
  ElevatedCard(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0F2FE)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color(0xFF0F4C81)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "MOHAMMAD ONLINE CYBER CAFE",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "মোহাম্মদ অনলাইন সাইবার ক্যাফে",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      ContactRowItem(
        icon = Icons.Default.Call,
        title = "মোবাইল ও হেল্পলাইন",
        value = "9832310837 (কল ও হোয়াটসঅ্যাপ)"
      )
      ContactRowItem(
        icon = Icons.Default.Email,
        title = "ইমেইল আইডি",
        value = "mohammadali9832310837@gmail.com"
      )
      ContactRowItem(
        icon = Icons.Default.AccessTime,
        title = "ক্যাফে খোলার সময়",
        value = "সকাল ৮:০০ টা - রাত ১০:০০ টা (অনলাইন সেবা ২৪/৭)"
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onCallClick,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "এখনই কল করুন", fontSize = 12.sp)
        }

        Button(
          onClick = onWhatsAppClick,
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
fun ContactRowItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(
        text = title,
        fontSize = 10.sp,
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
fun SeoFooter(language: AppLanguage) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF0A192F))
      .padding(18.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "MOHAMMAD ONLINE CYBER CAFE",
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )
    Text(
      text = "মোহাম্মদ অনলাইন সাইবার ক্যাফে • ডিজিটাল সেবা কেন্দ্র",
      fontSize = 11.sp,
      color = Color(0xFF94A3B8)
    )

    Spacer(modifier = Modifier.height(10.dp))

    HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

    Spacer(modifier = Modifier.height(10.dp))

    // SEO tags representation
    Text(
      text = "SEO Keywords: Mohammad Online Cyber Cafe | 9832310837 | Online PAN Card | Voter Card Download | Vehicle RC | Digital Ration Card | PF Claim Withdrawal | Residential Certificate | Color Xerox Print",
      fontSize = 9.sp,
      color = Color.White.copy(alpha = 0.5f),
      textAlign = TextAlign.Center,
      lineHeight = 14.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "© 2026 Mohammad Online Cyber Cafe. All Rights Reserved.\n২৪ ঘন্টা সহায়তা: ৯৮৩২৩১০৮৩৭",
      fontSize = 11.sp,
      color = Color(0xFFCBD5E1),
      textAlign = TextAlign.Center
    )
  }
}
