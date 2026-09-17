package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceCategory(val bnTitle: String, val enTitle: String, val icon: ImageVector) {
  ALL("সকল সেবা", "All Services", Icons.Default.Public),
  PAN("প্যান কার্ড", "PAN Services", Icons.Default.Badge),
  VOTER("ভোটার কার্ড", "Voter Services", Icons.Default.HowToVote),
  TRANSPORT("গাড়ি ও লাইসেন্স", "Transport & Vehicles", Icons.Default.DirectionsCar),
  RATION("রেশন কার্ড", "Ration Services", Icons.Default.Restaurant),
  PF("পিএফ (PF) সেবা", "PF Services", Icons.Default.AccountBalance),
  CERTIFICATES("সার্টিফিকেট", "Certificates", Icons.Default.CardMembership),
  OTHER("অন্যান্য সেবা", "Other Digital Services", Icons.Default.Print)
}

data class ServiceItem(
  val id: String,
  val category: ServiceCategory,
  val titleBn: String,
  val titleEn: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val requiredDocsBn: String,
  val icon: ImageVector,
  val isPopular: Boolean = false
)

object ServiceCatalog {
  val services: List<ServiceItem> = listOf(
    // PAN Services
    ServiceItem(
      id = "pan_new",
      category = ServiceCategory.PAN,
      titleBn = "নতুন প্যান কার্ড",
      titleEn = "New PAN Card Application",
      descriptionBn = "নতুন প্যান কার্ড আবেদন ও দ্রুত ই-প্যান প্রাপ্তি।",
      descriptionEn = "Apply for new PAN card with instant e-PAN generation.",
      requiredDocsBn = "আধার কার্ড, পাসপোর্ট সাইজ ছবি, সই",
      icon = Icons.Default.Badge,
      isPopular = true
    ),
    ServiceItem(
      id = "pan_correction",
      category = ServiceCategory.PAN,
      titleBn = "প্যান কার্ড কারেকশন / সংশোধন",
      titleEn = "PAN Card Correction",
      descriptionBn = "নাম, জন্মতারিখ, বাবার নাম বা ছবি সংশোধনের কাজ।",
      descriptionEn = "Update name, date of birth, father's name or photo in existing PAN.",
      requiredDocsBn = "আধার কার্ড, বর্তমান প্যান কার্ড কপি",
      icon = Icons.Default.ContactPage
    ),
    ServiceItem(
      id = "pan_link",
      category = ServiceCategory.PAN,
      titleBn = "আধার ও প্যান কার্ড লিংক",
      titleEn = "Aadhaar PAN Link",
      descriptionBn = "সরকারি নিয়ম মেনে আধার ও প্যান সফলভাবে লিংক করান।",
      descriptionEn = "Mandatory Aadhaar and PAN linking service with verification.",
      requiredDocsBn = "আধার নম্বর ও প্যান নম্বর",
      icon = Icons.Default.Security
    ),

    // Voter Services
    ServiceItem(
      id = "voter_new",
      category = ServiceCategory.VOTER,
      titleBn = "নতুন ভোটার কার্ড (ফর্ম ৬)",
      titleEn = "New Voter Card (Form 6)",
      descriptionBn = "১৮ বছর বা তদূর্ধ্বদের নতুন ভোটার কার্ডের অনলাইন আবেদন।",
      descriptionEn = "Online submission for new voter card registration.",
      requiredDocsBn = "আধার কার্ড, বয়সের প্রমাণ, পাসপোর্ট ছবি",
      icon = Icons.Default.HowToVote,
      isPopular = true
    ),
    ServiceItem(
      id = "voter_correction",
      category = ServiceCategory.VOTER,
      titleBn = "ভোটার কার্ড সংশোধন (ফর্ম ৮)",
      titleEn = "Voter Card Correction (Form 8)",
      descriptionBn = "ভোটার কার্ডের নাম, ঠিকানা বা অন্যান্য ভুল সংশোধন।",
      descriptionEn = "Correction of name, address or photo in existing Voter Card.",
      requiredDocsBn = "ভোটার কার্ড নম্বর, আধার কার্ড",
      icon = Icons.Default.ContactPage
    ),
    ServiceItem(
      id = "voter_mobile_link",
      category = ServiceCategory.VOTER,
      titleBn = "ভোটার কার্ড মোবাইল নম্বর লিংক",
      titleEn = "Voter Card Mobile Linking",
      descriptionBn = "ই-এপিক ডাউনলোড করার জন্য ভোটার কার্ডে মোবাইল লিংক।",
      descriptionEn = "Link active mobile number to Voter Card for OTP and e-EPIC download.",
      requiredDocsBn = "ভোটার আইডি ও ওটিপি নম্বর",
      icon = Icons.Default.Security
    ),
    ServiceItem(
      id = "voter_pdf_download",
      category = ServiceCategory.VOTER,
      titleBn = "ভোটার কার্ড PDF ডাউনলোড (e-EPIC)",
      titleEn = "Voter Card e-EPIC PDF Download",
      descriptionBn = "অরিজিনাল কালার ডিজিটাল ভোটার কার্ড ডাউনলোড ও পিভিসি প্রিন্ট।",
      descriptionEn = "Instant original high quality color e-EPIC download & PVC print.",
      requiredDocsBn = "ভোটার নম্বর বা রেফারেন্স নম্বর",
      icon = Icons.Default.Description,
      isPopular = true
    ),

    // Transport / Vehicle Services
    ServiceItem(
      id = "rc_download",
      category = ServiceCategory.TRANSPORT,
      titleBn = "গাড়ির আরসি (RC) ডাউনলোড",
      titleEn = "Vehicle RC Download",
      descriptionBn = "বাইক, কার বা যেকোনো গাড়ির ডিজিটাল আরসি কপি ডাউনলোড।",
      descriptionEn = "Download verified digital Registration Certificate (RC) for all vehicles.",
      requiredDocsBn = "গাড়ির নম্বর ও চেসিস নম্বর (শেষ ৫ ডিজিট)",
      icon = Icons.Default.DirectionsCar,
      isPopular = true
    ),
    ServiceItem(
      id = "dl_services",
      category = ServiceCategory.TRANSPORT,
      titleBn = "ড্রাইভিং লাইসেন্স আবেদন ও ডাউনলোড",
      titleEn = "Driving License Apply & Download",
      descriptionBn = "লার্নার লাইসেন্স, ড্রাইভিং লাইসেন্স রিনিউয়াল ও ডাউনলোড।",
      descriptionEn = "Learner license, driving license renewal and digital copy download.",
      requiredDocsBn = "আধার কার্ড, ব্লাড গ্রুপ, পুরোনো লাইসেন্স নম্বর (যদি থাকে)",
      icon = Icons.Default.Badge
    ),
    ServiceItem(
      id = "puc_download",
      category = ServiceCategory.TRANSPORT,
      titleBn = "পিইউসি সার্টিফিকেট ডাউনলোড (PUC)",
      titleEn = "PUC Certificate Download",
      descriptionBn = "গাড়ির পলিউশন বা দূষণ নিয়ন্ত্রণ সার্টিফিকেট ডাউনলোড।",
      descriptionEn = "Pollution Under Control (PUC) certificate online verification & download.",
      requiredDocsBn = "গাড়ির রেজিস্টার নম্বর ও চেসিস নম্বর",
      icon = Icons.Default.Description
    ),

    // Ration Card Services
    ServiceItem(
      id = "ration_download",
      category = ServiceCategory.RATION,
      titleBn = "ডিজিটাল রেশন কার্ড ডাউনলোড",
      titleEn = "Digital Ration Card Download (e-Ration)",
      descriptionBn = "পশ্চিমবঙ্গ খাদ্য সাথী ই-রেশন কার্ড ডাউনলোড ও ল্যামিনেশন।",
      descriptionEn = "Official e-Ration Card instant download and PVC printing.",
      requiredDocsBn = "রেশন কার্ড নম্বর ও আধার লিঙ্কড মোবাইল",
      icon = Icons.Default.Restaurant,
      isPopular = true
    ),
    ServiceItem(
      id = "ration_correction",
      category = ServiceCategory.RATION,
      titleBn = "রেশন কার্ড সংশোধন ও স্থানান্তর",
      titleEn = "Ration Card Correction & Family Transfer",
      descriptionBn = "পরিবারের নতুন সদস্য যুক্ত, নাম সংশোধন বা দোকান পরিবর্তন।",
      descriptionEn = "Add family member, correct member details or change dealer/FPS.",
      requiredDocsBn = "আধার কার্ড ও বিদ্যমান রেশন কার্ডের বিবরণ",
      icon = Icons.Default.ContactPage
    ),

    // PF Services
    ServiceItem(
      id = "pf_withdrawal",
      category = ServiceCategory.PF,
      titleBn = "পিএফ (PF) এর টাকা তোলা",
      titleEn = "PF Claim & Full/Advance Withdrawal",
      descriptionBn = "চাকরি ছাড়ার পর বা চাকরিরত অবস্থায় পিএফ ফান্ড থেকে টাকা উত্তোলন।",
      descriptionEn = "Online PF advance and final settlement claim assistance with bank verification.",
      requiredDocsBn = "UAN নম্বর, পাসওয়ার্ড, ব্যাংক পাসবই/চেক, আধার",
      icon = Icons.Default.AccountBalance,
      isPopular = true
    ),
    ServiceItem(
      id = "pf_kyc",
      category = ServiceCategory.PF,
      titleBn = "UAN KYC ও পাসওয়ার্ড রিসেট",
      titleEn = "UAN KYC & Password Reset",
      descriptionBn = "পিএফ অ্যাকাউন্টে আধার, প্যান ও ব্যাংক অ্যাকাউন্ট লিঙ্ক ও পাসওয়ার্ড সমাধান।",
      descriptionEn = "UAN activation, bank KYC approval and forgotten password reset.",
      requiredDocsBn = "UAN নম্বর, আধার ও ব্যাংক অ্যাকাউন্ট",
      icon = Icons.Default.Security
    ),

    // Certificates
    ServiceItem(
      id = "cert_residence",
      category = ServiceCategory.CERTIFICATES,
      titleBn = "রেসিডেন্স সার্টিফিকেট (আবাসিক শংসাপত্র)",
      titleEn = "Residential / Domicile Certificate",
      descriptionBn = "স্থানীয় বাসস্থান বা ডোমিসাইল সার্টিফিকেটের অনলাইন আবেদন।",
      descriptionEn = "Residential and local domicile certificate online application assistance.",
      requiredDocsBn = "আধার কার্ড, পঞ্চায়েত/পৌরসভা প্রধানের সার্টিফিকেট, জমির পর্চা বা ভোটার",
      icon = Icons.Default.CardMembership
    ),
    ServiceItem(
      id = "cert_income",
      category = ServiceCategory.CERTIFICATES,
      titleBn = "ইনকাম সার্টিফিকেট (আয় সংক্রান্ত সনদ)",
      titleEn = "Income Certificate",
      descriptionBn = "স্কলারশিপ ও সরকারি সুবিধার জন্য বার্ষিক ইনকাম সার্টিফিকেট।",
      descriptionEn = "Government income certificate application for scholarships and schemes.",
      requiredDocsBn = "ইনকাম প্রুফ / প্রধানের চিঠি, আধার কার্ড, ছবি",
      icon = Icons.Default.Description
    ),

    // Other Digital Services
    ServiceItem(
      id = "other_passport",
      category = ServiceCategory.OTHER,
      titleBn = "পাসপোর্ট অনলাইন আবেদন ও স্লট বুকিং",
      titleEn = "Passport Application & Slot Booking",
      descriptionBn = "নতুন ভারতীয় পাসপোর্ট আবেদন ও অ্যাপয়েন্টমেন্ট তারিখ বুকিং।",
      descriptionEn = "Fresh Indian Passport registration and PSK appointment booking.",
      requiredDocsBn = "আধার কার্ড, প্যান কার্ড, মাধ্যমিক এডমিট বা জন্ম সার্টিফিকেট",
      icon = Icons.Default.Public
    ),
    ServiceItem(
      id = "other_pmkisan",
      category = ServiceCategory.OTHER,
      titleBn = "পিএম কিষাণ ও কৃষক বন্ধু সেবা",
      titleEn = "PM-Kisan & Krishak Bandhu",
      descriptionBn = "কৃষকদের সরকারি কিস্তির টাকা চেক, e-KYC ও নতুন আবেদন।",
      descriptionEn = "PM-Kisan e-KYC, beneficiary status check and new farmer enrollment.",
      requiredDocsBn = "আধার কার্ড, জমির খতিয়ান/দলিল, ব্যাংক পাসবই",
      icon = Icons.Default.LocalAtm
    ),
    ServiceItem(
      id = "other_print_aeps",
      category = ServiceCategory.OTHER,
      titleBn = "টাকা তোলা, ট্রান্সফার ও জেরক্স-ল্যামিনেশন",
      titleEn = "AEPS Cash Withdrawal, Money Transfer & Print",
      descriptionBn = "আধার দিয়ে যেকোনো ব্যাংকের টাকা তোলা, কালার প্রিন্ট ও ল্যামিনেশন।",
      descriptionEn = "Aadhaar ATM (AEPS) cash withdrawal, instant money transfer, xerox & lamination.",
      requiredDocsBn = "আধার কার্ড ও বায়োমেট্রিক ফিঙ্গারপ্রিন্ট",
      icon = Icons.Default.Print
    )
  )
}
