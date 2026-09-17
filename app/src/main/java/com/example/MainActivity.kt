package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ServiceCatalog
import com.example.ui.AppLanguage
import com.example.ui.AppTab
import com.example.ui.CyberCafeViewModel
import com.example.ui.components.AdminPinDialog
import com.example.ui.components.CyberCafeBottomNavigationBar
import com.example.ui.components.CyberCafeTopAppBar
import com.example.ui.components.IntentHelpers
import com.example.ui.components.SubmissionConfirmationDialog
import com.example.ui.components.SupportBanner24x7
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ApplyScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.TrackStatusScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CyberCafeApp()
      }
    }
  }
}

@Composable
fun CyberCafeApp(
  viewModel: CyberCafeViewModel = viewModel()
) {
  val context = LocalContext.current
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val language by viewModel.language.collectAsStateWithLifecycle()
  val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsStateWithLifecycle()
  val allApplications by viewModel.allApplications.collectAsStateWithLifecycle()
  val recentApplications by viewModel.recentApplications.collectAsStateWithLifecycle()
  val trackedApplication by viewModel.trackedApplication.collectAsStateWithLifecycle()
  val trackSearchQuery by viewModel.trackSearchQuery.collectAsStateWithLifecycle()
  val recentSubmission by viewModel.recentSubmission.collectAsStateWithLifecycle()
  val selectedServiceForApply by viewModel.selectedServiceForApply.collectAsStateWithLifecycle()

  var showAdminPinDialog by remember { mutableStateOf(false) }

  // Handle hardware back button navigation: return to HOME tab before exiting
  BackHandler(enabled = currentTab != AppTab.HOME) {
    viewModel.selectTab(AppTab.HOME)
  }

  // Admin PIN Dialog
  if (showAdminPinDialog) {
    AdminPinDialog(
      onDismiss = { showAdminPinDialog = false },
      onUnlock = { pin ->
        val success = viewModel.unlockAdmin(pin)
        if (success) {
          showAdminPinDialog = false
          viewModel.selectTab(AppTab.ADMIN)
        }
        success
      }
    )
  }

  // Application Submission Confirmation Dialog
  recentSubmission?.let { submission ->
    SubmissionConfirmationDialog(
      application = submission,
      language = language,
      onDismiss = { viewModel.dismissRecentSubmissionModal() },
      onTrackClick = { app ->
        viewModel.selectApplicationToTrack(app)
      }
    )
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      Column(modifier = Modifier.fillMaxWidth()) {
        CyberCafeTopAppBar(
          currentTab = currentTab,
          language = language,
          isAdminUnlocked = isAdminUnlocked,
          onLanguageToggle = { viewModel.toggleLanguage() },
          onAdminClick = {
            if (isAdminUnlocked) {
              viewModel.selectTab(AppTab.ADMIN)
            } else {
              showAdminPinDialog = true
            }
          }
        )

        // 24/7 Support Banner under header
        SupportBanner24x7(
          language = language,
          onCallClick = { IntentHelpers.dialPhoneNumber(context) },
          onWhatsAppClick = { IntentHelpers.openWhatsApp(context) }
        )
      }
    },
    bottomBar = {
      CyberCafeBottomNavigationBar(
        currentTab = currentTab,
        language = language,
        onTabSelected = { tab ->
          if (tab == AppTab.ADMIN && !isAdminUnlocked) {
            showAdminPinDialog = true
          } else {
            viewModel.selectTab(tab)
          }
        }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      Crossfade(
        targetState = currentTab,
        label = "TabTransition"
      ) { tab ->
        when (tab) {
          AppTab.HOME -> {
            HomeScreen(
              language = language,
              onNavigateToTab = { targetTab ->
                if (targetTab == AppTab.ADMIN && !isAdminUnlocked) {
                  showAdminPinDialog = true
                } else {
                  viewModel.selectTab(targetTab)
                }
              },
              onSelectServiceToApply = { service ->
                viewModel.prepareApplicationForService(service)
              }
            )
          }
          AppTab.SERVICES -> {
            ServicesScreen(
              language = language,
              onSelectServiceToApply = { service ->
                viewModel.prepareApplicationForService(service)
              }
            )
          }
          AppTab.APPLY -> {
            ApplyScreen(
              language = language,
              selectedService = selectedServiceForApply ?: ServiceCatalog.services.first(),
              onSubmitApplication = { name, mobile, altPhone, service, docType, notes, deliveryMethod ->
                viewModel.submitCustomApplication(
                  customerName = name,
                  mobile = mobile,
                  emailOrAlt = altPhone,
                  service = service,
                  documentType = docType,
                  notes = notes,
                  deliveryMethod = deliveryMethod,
                  onSuccess = { /* Dialog is triggered by recentSubmission state */ }
                )
              }
            )
          }
          AppTab.TRACK -> {
            TrackStatusScreen(
              language = language,
              searchQuery = trackSearchQuery,
              onSearchQueryChanged = { viewModel.updateTrackSearchQuery(it) },
              onSearch = { viewModel.searchApplicationByReference(trackSearchQuery) },
              trackedApplication = trackedApplication,
              recentApplications = recentApplications
            )
          }
          AppTab.ADMIN -> {
            if (isAdminUnlocked) {
              AdminScreen(
                language = language,
                applications = allApplications,
                onUpdateStatus = { refId, status, remarks ->
                  viewModel.updateApplicationStatus(refId, status, remarks)
                },
                onDeleteApplication = { app ->
                  viewModel.deleteApplication(app)
                },
                onLogoutAdmin = {
                  viewModel.lockAdmin()
                }
              )
            } else {
              HomeScreen(
                language = language,
                onNavigateToTab = { targetTab ->
                  if (targetTab == AppTab.ADMIN && !isAdminUnlocked) {
                    showAdminPinDialog = true
                  } else {
                    viewModel.selectTab(targetTab)
                  }
                },
                onSelectServiceToApply = { service ->
                  viewModel.prepareApplicationForService(service)
                }
              )
            }
          }
          AppTab.CONTACT -> {
            ContactScreen(
              language = language
            )
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
