package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder

object IntentHelpers {
  const val PHONE_NUMBER = "9832310837"
  const val WHATSAPP_NUMBER = "919832310837"
  const val EMAIL_ADDRESS = "mohammadali9832310837@gmail.com"

  fun dialPhoneNumber(context: Context, number: String = PHONE_NUMBER) {
    try {
      val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$number")
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "কল করতে অক্ষম: $number", Toast.LENGTH_SHORT).show()
    }
  }

  fun openWhatsApp(
    context: Context,
    message: String = "নমস্কার, মোহাম্মদ অনলাইন সাইবার ক্যাফে থেকে সেবা সম্পর্কে জানতে চাই।",
    targetNumber: String = WHATSAPP_NUMBER
  ) {
    try {
      val phoneClean = if (targetNumber.startsWith("91") || targetNumber.startsWith("+")) targetNumber.removePrefix("+") else "91$targetNumber"
      val encoded = URLEncoder.encode(message, "UTF-8")
      val uri = Uri.parse("https://wa.me/$phoneClean?text=$encoded")
      val intent = Intent(Intent.ACTION_VIEW, uri)
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "WhatsApp খোলা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
    }
  }

  fun sendEmail(context: Context, subject: String = "Service Inquiry - Mohammad Online Cyber Cafe", body: String = "") {
    try {
      val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$EMAIL_ADDRESS")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "ইমেইল অ্যাপ খুঁজে পাওয়া যায়নি", Toast.LENGTH_SHORT).show()
    }
  }

  fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "কপি করা হয়েছে: $text", Toast.LENGTH_SHORT).show()
  }
}
