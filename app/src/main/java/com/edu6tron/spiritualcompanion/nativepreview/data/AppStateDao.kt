package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {
  @Query("SELECT aartiId FROM favourite_aarti ORDER BY createdAt DESC")
  fun observeFavouriteIds(): Flow<List<String>>

  @Query("SELECT EXISTS(SELECT 1 FROM favourite_aarti WHERE aartiId = :aartiId)")
  suspend fun isFavourite(aartiId: String): Boolean

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveFavourite(favourite: FavouriteAartiEntity)

  @Query("DELETE FROM favourite_aarti WHERE aartiId = :aartiId")
  suspend fun removeFavourite(aartiId: String)

  @Query("SELECT value FROM app_preference WHERE `key` = :key LIMIT 1")
  fun observeLong(key: String): Flow<Long?>

  @Query("SELECT value FROM app_preference WHERE `key` = :key LIMIT 1")
  suspend fun getLong(key: String): Long?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun savePreference(preference: AppPreferenceEntity)

  @Query("SELECT value FROM string_preference WHERE `key` = :key LIMIT 1")
  fun observeString(key: String): Flow<String?>

  @Query("SELECT value FROM string_preference WHERE `key` = :key LIMIT 1")
  suspend fun getString(key: String): String?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveStringPreference(preference: StringPreferenceEntity)

  @Query("DELETE FROM string_preference WHERE `key` = :key")
  suspend fun deleteStringPreference(key: String)
}
