package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Random

class ApplicationRepository(private val dao: ApplicationDao) {

  val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()

  fun getApplicationByRefId(refId: String): Flow<ApplicationEntity?> {
    return dao.getApplicationByRefId(refId)
  }

  fun searchApplications(query: String): Flow<List<ApplicationEntity>> {
    return dao.searchApplications(query)
  }

  suspend fun insertApplication(application: ApplicationEntity) {
    dao.insertApplication(application)
  }

  suspend fun updateApplication(application: ApplicationEntity) {
    dao.updateApplication(application)
  }

  suspend fun deleteApplication(application: ApplicationEntity) {
    dao.deleteApplication(application)
  }

  suspend fun deleteByRefId(refId: String) {
    dao.deleteByRefId(refId)
  }

  companion object {
    fun generateReferenceId(): String {
      val randomNum = 1000 + Random().nextInt(9000)
      return "MOC-2026-$randomNum"
    }
  }
}
