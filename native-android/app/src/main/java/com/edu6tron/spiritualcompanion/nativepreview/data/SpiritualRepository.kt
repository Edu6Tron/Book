package com.edu6tron.spiritualcompanion.nativepreview.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class DailyPractice(
  val id: String,
  val title: String,
  val completed: Boolean,
)

@Singleton
class SpiritualRepository @Inject constructor(
  private val dailyPracticeDao: DailyPracticeDao,
) {
  private val defaults = listOf(
    DailyPractice("morning-prayer", "Morning prayer", false),
    DailyPractice("japa", "Japa practice", false),
    DailyPractice("evening-aarti", "Evening aarti", false),
  )

  fun observeDailyPractices(): Flow<List<DailyPractice>> =
    dailyPracticeDao.observeAll().map { stored ->
      val storedById = stored.associateBy { it.id }
      defaults.map { default ->
        val saved = storedById[default.id]
        default.copy(completed = saved?.completed ?: default.completed)
      }
    }

  suspend fun togglePractice(id: String) {
    val definition = defaults.firstOrNull { it.id == id } ?: return
    val saved = dailyPracticeDao.findById(id)
    dailyPracticeDao.upsert(
      DailyPracticeEntity(
        id = definition.id,
        title = definition.title,
        completed = !(saved?.completed ?: definition.completed),
        updatedAt = System.currentTimeMillis(),
      ),
    )
  }
}
