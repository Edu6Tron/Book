package com.edu6tron.spiritualcompanion.nativepreview.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaSelectionDao {
  @Query("SELECT * FROM media_selection WHERE id = :id LIMIT 1")
  fun observe(id: String): Flow<MediaSelectionEntity?>

  @Upsert
  suspend fun upsert(selection: MediaSelectionEntity)

  @Query("DELETE FROM media_selection WHERE id = :id")
  suspend fun clear(id: String)
}
