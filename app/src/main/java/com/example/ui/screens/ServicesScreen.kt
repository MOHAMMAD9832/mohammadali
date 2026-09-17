package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceCatalog
import com.example.data.ServiceCategory
import com.example.data.ServiceItem
import com.example.ui.AppLanguage

@Composable
fun ServicesScreen(
  language: AppLanguage,
  onSelectServiceToApply: (ServiceItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(ServiceCategory.ALL) }

  val filteredServices = remember(searchQuery, selectedCategory) {
    ServiceCatalog.services.filter { service ->
      val matchesCat = selectedCategory == ServiceCategory.ALL || service.category == selectedCategory
      val matchesSearch = searchQuery.isBlank() ||
        service.titleBn.contains(searchQuery, ignoreCase = true) ||
        service.titleEn.contains(searchQuery, ignoreCase = true) ||
        service.descriptionBn.contains(searchQuery, ignoreCase = true) ||
        service.descriptionEn.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesSearch
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 12.dp)
  ) {
    // Header & Search
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Text(
        text = if (language == AppLanguage.BN) "অনলাইন পরিষেবাসমূহ" else "All Digital Services",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = if (language == AppLanguage.BN) "আপনার কাঙ্ক্ষিত সরকারি সেবা খুঁজে নিন এবং সরাসরি আবেদন করুন" else "Search required service and apply directly online",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = {
          Text(text = if (language == AppLanguage.BN) "সেবা খুঁজুন (যেমন: প্যান, ভোটার, আরসি)..." else "Search service (e.g. PAN, Voter, RC)...")
        },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "খুঁজুন")
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "মুছুন")
            }
          }
        },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("services_search_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category Chips
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(ServiceCategory.entries) { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = {
              Text(
                text = if (language == AppLanguage.BN) cat.bnTitle else cat.enTitle,
                fontSize = 12.sp
              )
            },
            leadingIcon = {
              Icon(imageVector = cat.icon, contentDescription = null, modifier = Modifier.size(14.dp))
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
              selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Services list
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 32.dp)
    ) {
      if (filteredServices.isEmpty()) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
          ) {
            Text(
              text = if (language == AppLanguage.BN) "কোনো সেবা পাওয়া যায়নি" else "No matching services found",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        items(filteredServices) { service ->
          ServiceCardItem(
            service = service,
            language = language,
            onApplyClick = { onSelectServiceToApply(service) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
          )
        }
      }
    }
  }
}
