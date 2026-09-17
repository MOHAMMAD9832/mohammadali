package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ApplicationEntity
import com.example.data.ApplicationRepository
import com.example.data.ApplicationStatus
import com.example.data.ServiceCategory
import com.example.data.ServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleBn: String, val titleEn: String) {
  HOME("হোম", "Home"),
  SERVICES("পরিষেবাসমূহ", "Services"),
  APPLY("আবেদন করুন", "Apply Online"),
  TRACK("স্ট্যাটাস ট্র্যাক", "Track Status"),
  ADMIN("অ্যাডমিন", "Admin"),
  CONTACT("যোগাযোগ", "Contact")
}

enum class AppLanguage {
  BN, EN
}

data class NewApplicationFormState(
  val customerName: String = "",
  val mobileNumber: String = "",
  val email: String = "",
  val selectedCategory: ServiceCategory = ServiceCategory.PAN,
  val selectedService: String = "নতুন প্যান কার্ড",
  val notes: String = "",
  val documentType: String = "আধার কার্ড (Aadhaar Card)",
  val documentFileName: String = "",
  val isSubmitting: Boolean = false,
  val errorMessage: String? = null
)

class CyberCafeViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: ApplicationRepository
  val allApplications: StateFlow<List<ApplicationEntity>>

  // Active navigation tab
  private val _currentTab = MutableStateFlow(AppTab.HOME)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  // App language (Bengali primary, English secondary)
  private val _language = MutableStateFlow(AppLanguage.BN)
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  // Track Status Search
  private val _trackSearchQuery = MutableStateFlow("")
  val trackSearchQuery: StateFlow<String> = _trackSearchQuery.asStateFlow()

  // Selected service to apply from home/services screen
  private val _selectedServiceForApply = MutableStateFlow<ServiceItem?>(null)
  val selectedServiceForApply: StateFlow<ServiceItem?> = _selectedServiceForApply.asStateFlow()

  // Selected or tracked application
  private val _trackedApplication = MutableStateFlow<ApplicationEntity?>(null)
  val trackedApplication: StateFlow<ApplicationEntity?> = _trackedApplication.asStateFlow()

  // Application form state
  private val _formState = MutableStateFlow(NewApplicationFormState())
  val formState: StateFlow<NewApplicationFormState> = _formState.asStateFlow()

  // Newly submitted application modal
  private val _recentSubmission = MutableStateFlow<ApplicationEntity?>(null)
  val recentSubmission: StateFlow<ApplicationEntity?> = _recentSubmission.asStateFlow()

  // Admin section state
  private val _adminUnlocked = MutableStateFlow(false)
  val adminUnlocked: StateFlow<Boolean> = _adminUnlocked.asStateFlow()
  val isAdminUnlocked: StateFlow<Boolean> = adminUnlocked

  val recentApplications: StateFlow<List<ApplicationEntity>>
    get() = allApplications

  private val _adminSearchQuery = MutableStateFlow("")
  val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

  private val _adminStatusFilter = MutableStateFlow("সকল")
  val adminStatusFilter: StateFlow<String> = _adminStatusFilter.asStateFlow()

  // Filtered applications for Admin view
  val adminFilteredApplications: StateFlow<List<ApplicationEntity>>

  init {
    val db = AppDatabase.getDatabase(application, viewModelScope)
    repository = ApplicationRepository(db.applicationDao())

    allApplications = repository.allApplications.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    adminFilteredApplications = combine(
      allApplications,
      _adminSearchQuery,
      _adminStatusFilter
    ) { apps, query, filter ->
      apps.filter { app ->
        val matchesQuery = query.isBlank() ||
          app.referenceId.contains(query, ignoreCase = true) ||
          app.customerName.contains(query, ignoreCase = true) ||
          app.mobileNumber.contains(query, ignoreCase = true) ||
          app.serviceName.contains(query, ignoreCase = true)

        val matchesFilter = filter == "সকল" || filter == "All" || app.status.contains(filter)
        matchesQuery && matchesFilter
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )
  }

  fun setTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun selectTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun toggleLanguage() {
    _language.value = if (_language.value == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
  }

  fun setTrackSearchQuery(query: String) {
    _trackSearchQuery.value = query
    if (query.isNotBlank()) {
      // Find matching application
      val match = allApplications.value.firstOrNull {
        it.referenceId.equals(query.trim(), ignoreCase = true) ||
        it.mobileNumber.trim() == query.trim()
      }
      _trackedApplication.value = match
    } else {
      _trackedApplication.value = null
    }
  }

  fun updateTrackSearchQuery(query: String) {
    _trackSearchQuery.value = query
  }

  fun searchApplicationByReference(query: String) {
    setTrackSearchQuery(query)
  }

  fun selectApplicationToTrack(app: ApplicationEntity) {
    _trackSearchQuery.value = app.referenceId
    _trackedApplication.value = app
    _currentTab.value = AppTab.TRACK
  }

  fun prepareApplicationForService(service: ServiceItem) {
    _selectedServiceForApply.value = service
    _formState.value = _formState.value.copy(
      selectedCategory = service.category,
      selectedService = service.titleBn,
      errorMessage = null
    )
    _currentTab.value = AppTab.APPLY
  }

  fun updateFormField(
    name: String? = null,
    mobile: String? = null,
    email: String? = null,
    category: ServiceCategory? = null,
    service: String? = null,
    notes: String? = null,
    docType: String? = null,
    fileName: String? = null
  ) {
    _formState.value = _formState.value.copy(
      customerName = name ?: _formState.value.customerName,
      mobileNumber = mobile ?: _formState.value.mobileNumber,
      email = email ?: _formState.value.email,
      selectedCategory = category ?: _formState.value.selectedCategory,
      selectedService = service ?: _formState.value.selectedService,
      notes = notes ?: _formState.value.notes,
      documentType = docType ?: _formState.value.documentType,
      documentFileName = fileName ?: _formState.value.documentFileName,
      errorMessage = null
    )
  }

  fun submitApplication(onSuccess: (ApplicationEntity) -> Unit) {
    val state = _formState.value
    if (state.customerName.trim().length < 2) {
      _formState.value = state.copy(errorMessage = "অনুগ্রহ করে আপনার পুরো নাম লিখুন (Please enter your name)")
      return
    }
    if (state.mobileNumber.trim().length != 10 || !state.mobileNumber.all { it.isDigit() }) {
      _formState.value = state.copy(errorMessage = "সঠিক ১০ ডিজিটের মোবাইল নম্বর দিন (Enter valid 10-digit mobile)")
      return
    }

    viewModelScope.launch {
      _formState.value = state.copy(isSubmitting = true)
      val refId = ApplicationRepository.generateReferenceId()
      val newApp = ApplicationEntity(
        referenceId = refId,
        customerName = state.customerName.trim(),
        mobileNumber = state.mobileNumber.trim(),
        email = state.email.trim(),
        serviceCategory = state.selectedCategory.bnTitle,
        serviceName = state.selectedService,
        notes = state.notes.trim(),
        documentType = state.documentType,
        documentAttachmentName = if (state.documentFileName.isNotBlank()) state.documentFileName else "documents_${state.customerName.take(4).lowercase()}.pdf",
        status = "আবেদন জমা হয়েছে",
        statusStep = 1,
        adminRemarks = "আপনার আবেদনটি গৃহীত হয়েছে। ক্যাফে প্রতিনিধি শীঘ্রই যাচাই প্রক্রিয়া সম্পন্ন করবেন।",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )

      repository.insertApplication(newApp)

      _formState.value = NewApplicationFormState() // Reset
      _recentSubmission.value = newApp
      _trackedApplication.value = newApp
      _trackSearchQuery.value = newApp.referenceId
      onSuccess(newApp)
    }
  }

  fun dismissRecentSubmissionModal() {
    _recentSubmission.value = null
  }

  // Admin controls
  fun unlockAdmin(pin: String): Boolean {
    if (pin == "1234" || pin == "9832") {
      _adminUnlocked.value = true
      return true
    }
    return false
  }

  fun lockAdmin() {
    _adminUnlocked.value = false
  }

  fun setAdminSearch(query: String) {
    _adminSearchQuery.value = query
  }

  fun setAdminStatusFilter(filter: String) {
    _adminStatusFilter.value = filter
  }

  fun updateApplicationStatus(refId: String, newStatus: String, newStep: Int, remarks: String) {
    viewModelScope.launch {
      val existing = allApplications.value.find { it.referenceId == refId } ?: return@launch
      val updated = existing.copy(
        status = newStatus,
        statusStep = newStep,
        adminRemarks = remarks.ifBlank { existing.adminRemarks },
        updatedAt = System.currentTimeMillis()
      )
      repository.updateApplication(updated)
      if (_trackedApplication.value?.referenceId == refId) {
        _trackedApplication.value = updated
      }
    }
  }

  fun submitCustomApplication(
    customerName: String,
    mobile: String,
    emailOrAlt: String,
    service: ServiceItem,
    documentType: String,
    notes: String,
    deliveryMethod: String,
    onSuccess: (ApplicationEntity) -> Unit
  ) {
    viewModelScope.launch {
      val refId = ApplicationRepository.generateReferenceId()
      val fullNotes = if (deliveryMethod.isNotBlank()) "$notes [ডেলিভারি: $deliveryMethod]".trim() else notes.trim()
      val newApp = ApplicationEntity(
        referenceId = refId,
        customerName = customerName.trim(),
        mobileNumber = mobile.trim(),
        email = emailOrAlt.trim(),
        serviceCategory = service.category.bnTitle,
        serviceName = service.titleBn,
        notes = fullNotes,
        documentType = documentType,
        documentAttachmentName = "documents_${customerName.take(4).lowercase()}.pdf",
        status = ApplicationStatus.PENDING.bnTitle,
        statusStep = 1,
        adminRemarks = "আপনার আবেদনটি সফলভাবে গৃহীত হয়েছে। আমাদের প্রতিনিধি শীঘ্রই প্রক্রিয়া শুরু করবেন।",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )
      repository.insertApplication(newApp)
      _recentSubmission.value = newApp
      _trackedApplication.value = newApp
      _trackSearchQuery.value = newApp.referenceId
      onSuccess(newApp)
    }
  }

  fun updateApplicationStatus(refId: String, status: ApplicationStatus, remarks: String) {
    updateApplicationStatus(refId, status.bnTitle, status.step, remarks)
  }

  fun deleteApplication(app: ApplicationEntity) {
    viewModelScope.launch {
      repository.deleteApplication(app)
      if (_trackedApplication.value?.referenceId == app.referenceId) {
        _trackedApplication.value = null
      }
    }
  }
}
