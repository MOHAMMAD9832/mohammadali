package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ApplicationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract fun applicationDao(): ApplicationDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "mohammad_cyber_cafe_db"
        )
        .addCallback(DatabaseCallback(scope))
        .fallbackToDestructiveMigration()
        .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.applicationDao())
          }
        }
      }

      private suspend fun populateInitialData(dao: ApplicationDao) {
        val currentTime = System.currentTimeMillis()
        val sampleList = listOf(
          ApplicationEntity(
            referenceId = "MOC-2026-8941",
            customerName = "Sk. Rafiqul Islam",
            mobileNumber = "9832145670",
            email = "rafiqul.islam@example.com",
            serviceCategory = "প্যান কার্ড",
            serviceName = "নতুন প্যান কার্ড",
            notes = "জরুরি আবেদন, ই-প্যান দ্রুত প্রয়োজন।",
            documentType = "আধার কার্ড (Aadhaar Card)",
            documentAttachmentName = "aadhaar_rafiqul_scan.pdf",
            status = "কাজ চলছে",
            statusStep = 3,
            adminRemarks = "এনএসডিএল (NSDL) পোর্টালে ফর্ম জমা হয়েছে। Acknowledgement No: 88402914। ২ দিনের মধ্যে কার্ড প্রস্তুত হবে।",
            createdAt = currentTime - (1000L * 60 * 60 * 18),
            updatedAt = currentTime - (1000L * 60 * 60 * 2)
          ),
          ApplicationEntity(
            referenceId = "MOC-2026-7235",
            customerName = "Anjali Mondal",
            mobileNumber = "9832987123",
            email = "anjali.mondal@example.com",
            serviceCategory = "ভোটার কার্ড",
            serviceName = "ভোটার কার্ড PDF ডাউনলোড (e-EPIC)",
            notes = "কালার পিভিসি প্রিন্ট কপি প্রয়োজন।",
            documentType = "ভোটার স্লিপ / EPIC No",
            documentAttachmentName = "voter_slip_anjali.jpg",
            status = "কাজ সম্পন্ন হয়েছে",
            statusStep = 4,
            adminRemarks = "ভোটার কার্ডের কালার পিভিসি কার্ড প্রিন্ট ও ল্যামিনেশন সম্পন্ন। দোকানে এসে সংগ্রহ করুন অথবা হোয়াটসঅ্যাপে পিডিএফ নেওয়া যাবে।",
            createdAt = currentTime - (1000L * 60 * 60 * 48),
            updatedAt = currentTime - (1000L * 60 * 60 * 6)
          ),
          ApplicationEntity(
            referenceId = "MOC-2026-6190",
            customerName = "Mohibul Haque",
            mobileNumber = "9832456789",
            email = "mohibul.haque@example.com",
            serviceCategory = "পিএফ (PF) সেবা",
            serviceName = "পিএফ (PF) এর টাকা তোলা",
            notes = "কোম্পানি ছাড়ার পর ফুল সেটেলমেন্ট ক্লেম।",
            documentType = "ব্যাংক পাসবই ও প্যান",
            documentAttachmentName = "passbook_cheque_copy.pdf",
            status = "কাগজপত্র যাচাই হচ্ছে",
            statusStep = 2,
            adminRemarks = "ব্যাংক অ্যাকাউন্টে নাম এবং ইপিএফও নামের মিল চেক করা হচ্ছে। শীঘ্রই ফর্ম ১৯ ও ১০সি সাবমিট করা হবে।",
            createdAt = currentTime - (1000L * 60 * 60 * 8),
            updatedAt = currentTime - (1000L * 60 * 60 * 1)
          ),
          ApplicationEntity(
            referenceId = "MOC-2026-5082",
            customerName = "Tanushree Das",
            mobileNumber = "9832789456",
            email = "tanushree.das@example.com",
            serviceCategory = "গাড়ি ও লাইসেন্স",
            serviceName = "গাড়ির আরসি (RC) ডাউনলোড",
            notes = "বাইকের আরসি হারিয়ে গেছে, অরিজিনাল কিউআর ভেরিফাইড কপি চাই।",
            documentType = "গাড়ির রেজিস্ট্রেশন নম্বর",
            documentAttachmentName = "bike_insurance_proof.jpg",
            status = "আবেদন জমা হয়েছে",
            statusStep = 1,
            adminRemarks = "আপনার আবেদনটি গৃহীত হয়েছে। পরিবহন পোর্টালে তথ্য যাচাইয়ের কাজ শুরু হচ্ছে।",
            createdAt = currentTime - (1000L * 60 * 30),
            updatedAt = currentTime - (1000L * 60 * 30)
          )
        )
        dao.insertAll(sampleList)
      }
    }
  }
}
