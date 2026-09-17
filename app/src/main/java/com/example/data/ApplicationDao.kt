package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

  @Query("SELECT * FROM applications ORDER BY createdAt DESC")
  fun getAllApplications(): Flow<List<ApplicationEntity>>

  @Query("SELECT * FROM applications WHERE referenceId = :refId LIMIT 1")
  fun getApplicationByRefId(refId: String): Flow<ApplicationEntity?>

  @Query("""
    SELECT * FROM applications 
    WHERE referenceId LIKE '%' || :query || '%' 
       OR customerName LIKE '%' || :query || '%' 
       OR mobileNumber LIKE '%' || :query || '%' 
       OR serviceName LIKE '%' || :query || '%'
    ORDER BY createdAt DESC
  """)
  fun searchApplications(query: String): Flow<List<ApplicationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertApplication(application: ApplicationEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(applications: List<ApplicationEntity>)

  @Update
  suspend fun updateApplication(application: ApplicationEntity)

  @Delete
  suspend fun deleteApplication(application: ApplicationEntity)

  @Query("DELETE FROM applications WHERE referenceId = :refId")
  suspend fun deleteByRefId(refId: String)
}
